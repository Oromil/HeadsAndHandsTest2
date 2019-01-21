package com.oromil.hhtest.data.entities

import android.arch.persistence.room.Embedded
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(tableName = "stories_table")
data class StoryEntity(

        @SerializedName("url")
        @Expose
        @PrimaryKey
        var url: String="",

        @SerializedName("section")
        @Expose
        var section: String="",
        @SerializedName("subsection")
        @Expose
        var subsection: String="",
        @SerializedName("title")
        @Expose
        var title: String="",
        @SerializedName("abstract")
        @Expose
        var abstract: String="",

        @SerializedName("updated_date")
        @Expose
        var updatedDate: String="",
        @SerializedName("created_date")
        @Expose
        var createdDate: String="",
        @SerializedName("published_date")
        @Expose
        var publishedDate: String="",
        @SerializedName("kicker")
        @Expose
        var kicker: String="",
        @SerializedName("multimedia")
        @Expose
        @Embedded
        var multimedia: ArrayList<MultimediaEntity> = arrayListOf(MultimediaEntity()),
        @SerializedName("short_url")
        @Expose
        var shortUrl: String=""
)

