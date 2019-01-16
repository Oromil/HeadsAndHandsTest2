package com.oromil.hendsandheadstest.data.local.dao

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import android.provider.ContactsContract
import com.oromil.hendsandheadstest.data.entities.StoryEntity
import com.oromil.hendsandheadstest.data.entities.UserAccount
import io.reactivex.Flowable

@Dao
interface DataBaseDao {

    @Query("SELECT * FROM stories_table")
    fun getAllAsFlowable(): Flowable<List<StoryEntity>>

    @Query("SELECT * FROM stories_table")
    fun getAll(): List<StoryEntity>

    @Query("SELECT * FROM stories_table WHERE url = :url")
    fun getById(url: String): Flowable<StoryEntity>

    @Query("SELECT*FROM users_table WHERE email = :email")
    fun getUserWithEmail(email: String):UserAccount?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: StoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entities: List<StoryEntity>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun saveUser(user: UserAccount):Long

    @Update
    fun update(entity: StoryEntity)

    @Delete
    fun delete(entity: StoryEntity)
}