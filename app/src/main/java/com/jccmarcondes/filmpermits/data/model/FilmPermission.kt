package com.jccmarcondes.filmpermits.data.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.jccmarcondes.filmpermits.util.DateUtil
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "permission_film_table")
data class FilmPermission(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @SerializedName("eventId")
    val eventId: String? = null,

    @SerializedName("eventtype")
    val eventType: String?,

    @SerializedName("startdatetime")
    val startDateTime: String?,

    @SerializedName("enddatetime")
    val endDateTime: String?,

    @SerializedName("eventagency")
    val eventAgency: String?,

    @SerializedName("parkingheld")
    val parkingHeld: String?,

    @SerializedName("borough")
    val borough: String?,

    @SerializedName("category")
    val category: String?,

    @SerializedName("country")
    val country: String?,

    @SerializedName("zipcode_s")
    val zipcode: String?
): Parcelable{
    val formattedStartDateTimeAt : String get() {
        startDateTime?.let {
            return DateUtil.changeDateFormat(startDateTime)
        }
        return ""
    }
    val formattedEndDateTimeAt : String get() {
        endDateTime?.let {
            return DateUtil.changeDateFormat(endDateTime)
        }
        return ""
    }

    companion object {
        fun mock() = FilmPermission(
            eventId = "671005",
            eventType = "Shooting Permit",
            startDateTime = "2022-09-06T07:00:00.000",
            endDateTime = "2022-09-06T20:00:00.000",
            eventAgency = "Mayor's Office of Film, Theatre & Broadcasting",
            parkingHeld = "CAMPUS ROAD between EAST   22 STREET and EAST   23 STREET,  E 23rd St between CAMPUS ROAD and E 22nd St,  E 23rd St between CAMPUS ROAD and E 22nd St,  EAST   23 STREET between CAMPUS ROAD and GLENWOOD ROAD,  CAMPUS ROAD between EAST   23 STREET and BEDFORD AVENUE,  BEDFORD AVENUE between CAMPUS ROAD and AVENUE I,  CAMPUS ROAD between BEDFORD AVENUE and EAST   26 STREET,  EAST   26 STREET between GLENWOOD ROAD and CAMPUS ROAD,  CAMPUS ROAD between EAST   26 STREET and AMERSFORT PLACE",
            borough = "Brooklyn",
            category = "Television",
            country = "United States of America",
            zipcode = "11210"
        )
    }
}