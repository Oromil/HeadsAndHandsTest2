package com.oromil.hendsandheadstest.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResponseEntity(
        @SerializedName("status")
        @Expose
        var status: String,
        @SerializedName("copyright")
        @Expose
        var copyright: String,
        @SerializedName("section")
        @Expose
        var section: String,
        @SerializedName("last_updated")
        @Expose
        var lastUpdated: String,
        @SerializedName("num_results")
        @Expose
        var numResults: Int,
        @SerializedName("results")
        @Expose
        var results: List<StoryEntity>
)


