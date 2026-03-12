package com.example.monarcasmarttravel.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.monarcasmarttravel.domain.Trip
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

/**
 * Repositori de viatges amb persistència via SharedPreferences.
 *
 * Classe que rep el context al constructor i usa propietats amb get/set.
 */
class TripRepository(private val context: Context) {

    private val preferences: SharedPreferences = context.getSharedPreferences(
        "monarca_trips",
        Context.MODE_PRIVATE
    )

    private var nextId: Int
        get() = preferences.getInt("next_id", 1)
        set(value) { preferences.edit().putInt("next_id", value).commit() }

    /**
     * Llista de viatges desats. Cada accés a get deserialitza des de
     * SharedPreferences, igual que les altres propietats del repositori.
     * El set serialitza i persisteix immediatament.
     */
    private var tripList: List<Trip>
        get() {
            val json = preferences.getString("trips", null) ?: return emptyList()
            val array = JSONArray(json)
            val result = mutableListOf<Trip>()
            for (i in 0 until array.length()) {
                val obj = array.getJSONObject(i)
                result.add(
                    Trip(
                        id          = obj.getInt("id"),
                        destination = obj.getString("destination"),
                        dateIn      = Date(obj.getLong("dateIn")),
                        dateOut     = Date(obj.getLong("dateOut")),
                        imageResId  = nameToResId(
                            if (obj.isNull("imageName")) null
                            else obj.getString("imageName")
                        ),
                        userId      = obj.getInt("userId")
                    )
                )
            }
            return result
        }
        set(value) {
            val array = JSONArray()
            value.forEach { trip ->
                val obj = JSONObject().apply {
                    put("id",          trip.id)
                    put("destination", trip.destination)
                    put("dateIn",      trip.dateIn.time)
                    put("dateOut",     trip.dateOut.time)
                    put("imageName",   resIdToName(trip.imageResId) ?: JSONObject.NULL)
                    put("userId",      trip.userId)
                }
                array.put(obj)
            }
            preferences.edit().putString("trips", array.toString()).commit()
        }

    /**
     * Retorna una còpia immutable de tots els viatges desats.
     */
    fun getAllTrips(): List<Trip> = tripList

    /**
     * Afegeix un viatge assignant-li un ID únic i el persisteix.
     * Cridat des de [Trip.createTrip].
     * @throws IllegalArgumentException si les dades no són vàlides.
     */
    fun addTrip(trip: Trip): Trip {
        validateTrip(trip)
        val id = nextId
        val newTrip = trip.copy(id = id)
        tripList = tripList + newTrip
        nextId = id + 1
        return newTrip
    }

    /**
     * Elimina el viatge amb l'ID indicat i persisteix el canvi.
     * Cridat des de [Trip.deleteTrip].
     * @return true si s'ha eliminat, false si no s'ha trobat.
     */
    fun deleteTrip(tripId: Int): Boolean {
        val current = tripList
        val filtered = current.filter { it.id != tripId }
        if (filtered.size == current.size) return false
        tripList = filtered
        return true
    }

    /**
     * Actualitza la imatge d'un viatge existent i persisteix el canvi.
     * Cridat des de [Trip.updateTrip].
     * @return El viatge actualitzat, o null si no s'ha trobat.
     */
    fun updateImage(tripId: Int, newImageResId: Int?): Trip? {
        val current = tripList.toMutableList()
        val index = current.indexOfFirst { it.id == tripId }
        if (index == -1) return null
        val updated = current[index].copy(imageResId = newImageResId)
        current[index] = updated
        tripList = current
        return updated
    }

    /**
     * Retorna el viatge futur més proper (dateIn >= avui).
     * Si no n'hi ha cap, retorna null.
     */
    fun getNextUpcomingTrip(): Trip? {
        val now = Date()
        return tripList
            .filter { it.dateIn >= now }
            .minByOrNull { it.dateIn }
    }

    // ── Helpers de serialització d'imatges ───────────────────────────────────

    /**
     * Converteix un resId (Int) al nom del drawable.
     */
    private fun resIdToName(resId: Int?): String? {
        if (resId == null) return null
        return try {
            context.resources.getResourceEntryName(resId)
        } catch (e: Exception) {
            null
        }
    }

    /**
     * Converteix el nom d'un drawable al resId actual.
     */
    private fun nameToResId(name: String?): Int? {
        if (name == null) return null
        val id = context.resources.getIdentifier(name, "drawable", context.packageName)
        return if (id != 0) id else null
    }

    private fun validateTrip(trip: Trip) {
        require(trip.destination.isNotBlank()) {
            "La destinació no pot estar buida."
        }
        require(trip.dateOut.after(trip.dateIn)) {
            "La data d'inici ha de ser anterior a la data de fi."
        }
    }
}