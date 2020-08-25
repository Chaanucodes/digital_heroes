package com.example.digitalheroes.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface HeroesDao{
    @Query("select * from databaseheroes")
    fun getHeroes() :LiveData<List<DatabaseHeroes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(heroes: List<DatabaseHeroes>)

    @Query("select * from databaseheroes where name like :gName")
    fun getTheseHeroes(gName : String) : Flow<List<DatabaseHeroes>>

    @Query("DELETE from databaseheroes where name not like :gName")
    fun deleteAll(gName: String)

    @Query("DELETE from databaseheroes")
    fun delete()

}

@Database(entities = [DatabaseHeroes::class], version = 3, exportSchema = false)
abstract class HeroDatabase: RoomDatabase() {
    abstract val heroDao: HeroesDao

}

private lateinit var INSTANCE: HeroDatabase

fun getDatabase(context: Context): HeroDatabase {
    synchronized(HeroDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                HeroDatabase::class.java,
                "videos")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}