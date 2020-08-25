package com.example.digitalheroes.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digitalheroes.database.DatabaseHeroes
import com.example.digitalheroes.database.HeroDatabase
import com.example.digitalheroes.database.getDatabase
//import com.example.digitalheroes.database.getDatabase
import com.example.digitalheroes.network.SuperHeroApi
import com.example.digitalheroes.network.SuperHeroes
import com.example.digitalheroes.repository.HeroesRepository
import kotlinx.coroutines.*
import java.lang.Exception

class SearchViewModel(application: Application) : ViewModel() {

    private var viewModelJob = SupervisorJob()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private val _superhero = MutableLiveData<List<SuperHeroes>>()
//    val superhero : LiveData<List<SuperHeroes>>
//    get() = _superhero

    private val heroesRepository = HeroesRepository(getDatabase(application))
    var heroList = heroesRepository.heroes

    private val _loadingStatus = MutableLiveData<LoadStatus>()
    val loadingStatus : LiveData<LoadStatus>
    get() = _loadingStatus

    private val _showRecycler = MutableLiveData<Boolean>()
    val showRecycler : LiveData<Boolean>
    get() = _showRecycler

    init {
        _showRecycler.value = true
    }

    fun refreshDataFromRepository(query: String) {
        uiScope.launch {
            try {
                _loadingStatus.value = LoadStatus.LOADING
                if (_showRecycler.value!!) _showRecycler.value = false
                heroesRepository.refreshHeroes(query)
                _loadingStatus.value = LoadStatus.DONE
                _showRecycler.value = true
            }catch (e : Exception){

                if(heroList.value.isNullOrEmpty()){
                    _loadingStatus.value = LoadStatus.NOT_FOUND
                    if (!_showRecycler.value!!) _showRecycler.value = true
                }
                    _loadingStatus.value = LoadStatus.NOT_FOUND
                    if (!_showRecycler.value!!) _showRecycler.value = true
            }

        }
    }


    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SearchViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }

}
enum class LoadStatus{
    LOADING, ERROR, DONE, NOT_FOUND
}