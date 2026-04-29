package com.monarca.smarttravel.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.monarca.smarttravel.R
import com.monarca.smarttravel.ui.screens.trip.PlanType
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Model de dades que representa un element de l'itinerari d'un viatge.
 *
 * Un element pot ser de dos tipus principals:
 * - **Transport** (FLIGHT, BOAT, TRAIN): usa els camps [origin], [destination], [company],
 *   [transportNumber] i [departureDate].
 * - **Allotjament / Punt d'interès** (HOTEL, RESTAURANT, LOCATION): usa els camps
 *   [locationName], [address], [checkInDate].
 *
 * @param id Identificador únic de l'element.
 * @param tripId Identificador del viatge al qual pertany.
 * @param type Tipus de pla, definit per [PlanType].
 * @param price Cost associat a aquest element del viatge.
 * @param origin Lloc de sortida (només transport).
 * @param destination Lloc d'arribada (només transport).
 * @param company Nom de la companyia de transport (només transport).
 * @param transportNumber Número de vol, tren o vaixell (només transport).
 * @param departureDate Data i hora de sortida (només transport).
 * @param locationName Nom del lloc, restaurant o hotel (no transport).
 * @param address Adreça del lloc (no transport).
 * @param checkInDate Data d'entrada o visita (no transport).
 */

@Entity(
    tableName = "itinerary_items",
    indices = [
        Index(value = ["tripId"]),
    ],
    foreignKeys = [
        ForeignKey(
            entity = Trip::class,
            parentColumns = ["id"],
            childColumns = ["tripId"],
            onDelete = ForeignKey.CASCADE
        )
    ])
data class ItineraryItem(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val tripId: Int,
    val type: PlanType,
    val price: Double,

    // Camps per a plans de transport (FLIGHT, BOAT, TRAIN)
    val origin: String? = null,
    val destination: String? = null,
    val company: String? = null,
    val transportNumber: String? = null,
    val departureDate: Date? = null,

    // Camps per a allotjament i punts d'interès (HOTEL, RESTAURANT, LOCATION)
    val locationName: String? = null,
    val address: String? = null,
    val checkInDate: Date? = null,
) {
     /**
     * Retorna la data d'entrada rellevant per a aquest element:
     * [checkInDate] per a allotjament/POI, o [departureDate] per a transport.
     */
    fun getInDate(): Date? = checkInDate ?: departureDate

    /**
     * Retorna l'hora d'entrada formatada com "HH:mm".
     * Si no hi ha data disponible, retorna el text localitzat de "sense data".
     */
    @Composable
    fun getDateInTime(): String {
        val date = getInDate() ?: return stringResource(R.string.no_date)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    /**
     * Formata una data com a clau de dia per agrupar elements de l'itinerari.
     * Exemple de resultat: "dilluns, 23 de març de 2026".
     *
     * @param date Data a formatar.
     * @return Cadena amb el dia complet en el format regional del dispositiu.
     */
    fun formatDateKey(date: Date): String {
        val sdf = SimpleDateFormat("EEEE, d MMMM yyyy", Locale.getDefault())
        return sdf.format(date)
    }
}