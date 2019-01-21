package com.oromil.hhtest.data.entities

import com.google.gson.annotations.SerializedName

data class NewsResponseEntity(
        @SerializedName("status")
        var status: String,
        @SerializedName("copyright")
        var copyright: String,
        @SerializedName("section")
        var section: String,
        @SerializedName("last_updated")
        var lastUpdated: String,
        @SerializedName("num_results")
        var numResults: Int,
        @SerializedName("results")
        var results: List<StoryEntity>
)


