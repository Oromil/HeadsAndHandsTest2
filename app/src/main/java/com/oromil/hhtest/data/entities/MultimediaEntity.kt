package com.oromil.hhtest.data.entities

import com.google.gson.annotations.SerializedName

data class MultimediaEntity(
        @SerializedName("url")
        var url: String = "",
        @SerializedName("format")
        var format: String = "",
        @SerializedName("height")
        var height: Int = -1,
        @SerializedName("width")
        var width: Int = -1,
        @SerializedName("type")
        var type: String = "",
        @SerializedName("subtype")
        var subtype: String = "",
        @SerializedName("caption")
        var caption: String = "",
        @SerializedName("copyright")
        var copyright: String = ""
)
