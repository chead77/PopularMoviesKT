package com.cheadtech.popularmovieskt.models

import android.os.Parcel
import android.os.Parcelable

data class Movie(
    var vote_count: Int?,
    var id: Int?,
    var video: Boolean?,
    var vote_average: Double?,
    var title: String?,
    var popularity: Double?,
    var poster_path: String?,
    var original_language: String?,
    var original_title: String?,
    var backdrop_path: String?,
    var adult: Boolean?,
    var overview: String?,
    var release_date: String?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readValue(Double::class.java.classLoader) as? Double,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Boolean::class.java.classLoader) as? Boolean,
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(p0: Parcel?, p1: Int) {
        p0?.writeValue(vote_count)
        p0?.writeValue(id)
        p0?.writeValue(video)
        p0?.writeValue(vote_average)
        p0?.writeString(title)
        p0?.writeValue(popularity)
        p0?.writeString(poster_path)
        p0?.writeString(original_language)
        p0?.writeString(original_title)
        p0?.writeString(backdrop_path)
        p0?.writeValue(adult)
        p0?.writeString(overview)
        p0?.writeString(release_date)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<Movie> {
        override fun createFromParcel(parcel: Parcel): Movie {
            return Movie(parcel)
        }

        override fun newArray(size: Int): Array<Movie?> {
            return arrayOfNulls(size)
        }
    }
}