package com.example.monarcasmarttravel.domain.model

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.monarcasmarttravel.R
import com.example.monarcasmarttravel.ui.screens.trip.PlanType
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
 *   [locationName], [address], [checkInDate] i, en el cas d'hotels, [checkOutDate].
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
 * @param checkOutDate Data de sortida; s'usa principalment per a hotels.
 */
data class ItineraryItem(
    val id: Int,
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
    val checkOutDate: Date? = null,   // Rellevant principalment per a HOTEL
) {
    /**
     * Afegeix aquest element a l'itinerari del viatge al backend.
     * Pendent d'implementar.
     */
    fun addItineraryItem() {
        // @TODO Implement add itinerary item
    }

    /**
     * Elimina aquest element de l'itinerari del viatge.
     * Pendent d'implementar.
     */
    fun deleteItineraryItem() {
        // @TODO Implement delete itinerary item
    }

    /**
     * Actualitza les dades d'aquest element (per exemple, si canvia l'hora de vol).
     * Pendent d'implementar.
     */
    fun updateItem() {
        // @TODO Updates the item when the time has arrived
    }

    /**
     * Comprova si hi ha un conflicte horari entre aquest element i un altre [other].
     * Retorna true si les hores es solapen; false en cas contrari.
     * Pendent d'implementar.
     */
    fun hasScheduleConflict(other: ItineraryItem): Boolean {
        // @TODO Logic to compare departureDate or checkInDate between items
        return false
    }

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
     * Retorna l'hora de sortida formatada com "HH:mm".
     * Si no hi ha [checkOutDate], retorna el text localitzat de "sense data".
     */
    @Composable
    fun getDateOutTime(): String {
        val date = checkOutDate ?: return stringResource(R.string.no_date)
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