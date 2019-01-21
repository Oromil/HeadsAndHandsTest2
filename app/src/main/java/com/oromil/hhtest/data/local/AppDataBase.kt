package com.oromil.hhtest.data.local

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase
import com.oromil.hhtest.data.local.dao.DataBaseDao
import com.oromil.hhtest.data.entities.StoryEntity
import com.oromil.hhtest.data.entities.UserAccount
import javax.inject.Singleton

@Singleton
@Database(entities = [StoryEntity::class, UserAccount::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun mDao(): DataBaseDao
}