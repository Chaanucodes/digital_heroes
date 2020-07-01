package com.example.digitalheroes.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface HeroesDao{
    @Query("select * from databaseheroes")
    fun getHeroes() :LiveData<List<DatabaseHeroes>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(heroes: List<DatabaseHeroes>)

}

@Database(entities = [DatabaseHeroes::class], version = 1, exportSchema = false)
abstract class HeroDatabase: RoomDatabase() {
    abstract val heroDao: HeroesDao

    companion object{

        @Volatile
        private var INSTANCE: HeroDatabase? = null

        fun getDatabase(context: Context): HeroDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        HeroDatabase::class.java,
                        "heroes")
                        .fallbackToDestructiveMigration()
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}

