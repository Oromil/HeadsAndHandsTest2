package com.oromil.hhtest.data.entities

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class MultimediaEntity(
        @SerializedName("url")
        @Expose
        var url: String = "",
        @SerializedName("format")
        @Expose
        var format: String = "",
        @SerializedName("height")
        @Expose
        var height: Int = -1,
        @SerializedName("width")
        @Expose
        var width: Int = -1,
        @SerializedName("type")
        @Expose
        var type: String = "",
        @SerializedName("subtype")
        @Expose
        var subtype: String = "",
        @SerializedName("caption")
        @Expose
        var caption: String = "",
        @SerializedName("copyright")
        @Expose
        var copyright: String = ""
)
