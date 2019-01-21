package com.oromil.hhtest.data.local.dao

import android.arch.persistence.room.*
import com.oromil.hhtest.data.entities.StoryEntity
import com.oromil.hhtest.data.entities.UserAccount

@Dao
interface DataBaseDao {

    @Query("SELECT * FROM stories_table")
    fun getAll(): List<StoryEntity>

    @Query("SELECT * FROM stories_table WHERE url = :url")
    fun getById(url: String): StoryEntity

    @Query("SELECT*FROM users_table WHERE email = :email")
    fun getUserWithEmail(email: String): UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: StoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUser(user: UserAccount): Long

    @Update
    fun update(entity: StoryEntity)

    @Delete
    fun delete(entity: StoryEntity)
}