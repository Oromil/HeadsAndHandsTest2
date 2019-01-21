package com.oromil.hhtest.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "users_table")
data class UserAccount(
        @PrimaryKey
        var email: String,
        var name: String,
        var password: String
):Serializable