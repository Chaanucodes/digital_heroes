package com.example.digitalheroes.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.example.digitalheroes.database.HeroDatabase
import com.example.digitalheroes.database.asDomainModel
import com.example.digitalheroes.domain.SuperHeroPieces
import com.example.digitalheroes.network.SuperHeroApi
import com.example.digitalheroes.network.asDatabaseModel
import com.example.digitalheroes.search.LoadStatus
import com.example.digitalheroes.search.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.withContext

class HeroesRepository(private val database: HeroDatabase) {
    private var queryS : String = ""

    init {
        queryS = "Batman"
    }

    val heroes : LiveData<List<SuperHeroPieces>> =
        Transformations.map(database.heroDao.getHeroes()){
            it.asDomainModel()
        }




    suspend fun refreshHeroes(query : String){
        withContext(Dispatchers.IO){
            val heroesList = SuperHeroApi.retrofitService.getSuperHeroList(query).await()
            if (heroesList.results.isNotEmpty()){
//                database.heroDao.deleteAll("%Batman%")
//                queryS= heroesList.results[0].name
                database.heroDao.delete()
            }
            database.heroDao.insertAll(heroesList.asDatabaseModel())
        }
    }
}