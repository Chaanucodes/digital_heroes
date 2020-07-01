package com.example.digitalheroes.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.digitalheroes.database.HeroDatabase
import com.example.digitalheroes.database.asDomainModel
import com.example.digitalheroes.domain.SuperHeroPieces
import com.example.digitalheroes.network.SuperHeroApi
import com.example.digitalheroes.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class HeroesRepository(private val database: HeroDatabase) {
    val heroes : LiveData<List<SuperHeroPieces>> = Transformations.map(database.heroDao.getHeroes()){
        it.asDomainModel()
    }

    suspend fun refreshHeroes(query : String){
        withContext(Dispatchers.IO){
            val heroesList = SuperHeroApi.retrofitService.getSuperHeroList(query).await()
            database.heroDao.insertAll(heroesList.asDatabaseModel())
        }
    }
}