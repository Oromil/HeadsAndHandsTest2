package com.oromil.hhtest.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories_table")
data class StoryEntity(

        @SerializedName("url")
        @PrimaryKey
        var url: String = "",
        @SerializedName("section")
        var section: String = "",
        @SerializedName("subsection")
        var subsection: String = "",
        @SerializedName("title")
        var title: String = "",
        @SerializedName("abstract")
        var abstract: String = "",
        @SerializedName("updated_date")
        var updatedDate: String = "",
        @SerializedName("created_date")
        var createdDate: String = "",
        @SerializedName("published_date")
        var publishedDate: String = "",
        @SerializedName("kicker")
        var kicker: String = "",
        @SerializedName("multimedia")
        @Embedded
        var multimedia: ArrayList<MultimediaEntity> = arrayListOf(MultimediaEntity()),
        @SerializedName("short_url")
        var shortUrl: String = ""
) {
    val imageUrl: String?
        get() = multimedia.run { if (isNotEmpty()) get(lastIndex).url else null }
}

