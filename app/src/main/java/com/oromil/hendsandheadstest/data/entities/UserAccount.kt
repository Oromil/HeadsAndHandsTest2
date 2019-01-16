package com.oromil.hendsandheadstest.data.entities

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "users_table")
data class UserAccount(
        @PrimaryKey
        var email: String,
        var name: String,
        var password: String
)