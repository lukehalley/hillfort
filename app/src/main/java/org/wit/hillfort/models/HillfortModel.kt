package org.wit.hillfort.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class HillfortModel(var id: Long = 0,
                         var title: String = "",
                         var description: String = "",
                         var visited: Boolean = false,
                         var dateVisited: Date,
                         var firstImage: String = "",
                         var secondImage: String = "",
                         var thirdImage: String = "",
                         var fourthImage: String = "") : Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable

