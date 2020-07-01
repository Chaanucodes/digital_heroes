package com.example.digitalheroes.search

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.digitalheroes.database.DatabaseHeroes
import com.example.digitalheroes.database.HeroDatabase
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

    private val heroesRepository = HeroesRepository(HeroDatabase.getDatabase(application))
    var heroList = heroesRepository.heroes

    private val _loadingStatus = MutableLiveData<LoadStatus>()
    val loadingStatus : LiveData<LoadStatus>
    get() = _loadingStatus

    private val _showRecycler = MutableLiveData<Boolean>()
    val showRecycler : LiveData<Boolean>
    get() = _showRecycler

    init {
//        getSuperHero("Batman")
        refreshDataFromRepository("Batman")
        _loadingStatus.value = LoadStatus.LOADING
        _showRecycler.value = false
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
                val es= e.toString()
                if (es.contains("java.net.UnknownHostException") || es.contains("java.net.SocketTimeoutException")){
                    _loadingStatus.value = LoadStatus.ERROR
                    if (_showRecycler.value!!) _showRecycler.value = false
                }else{
                    _loadingStatus.value = LoadStatus.NOT_FOUND
                    if (_showRecycler.value!!) _showRecycler.value = false
                }
            }

        }
    }

//    fun getSuperHero(searchQuery : String){
//
//        val getInfoDeferred = SuperHeroApi.retrofitService.getSuperHeroList(searchQuery)
//        uiScope.launch {
//            try {
//
//                _loadingStatus.value = LoadStatus.LOADING
//                if (_showRecycler.value!!) _showRecycler.value = false
//
//                val list = mutableListOf<SuperHeroes>()
//                val listResult = getInfoDeferred.await()
//                for (element in listResult.results){
//                    list.add(element)
//                }
//                _superhero.value = list
//                _loadingStatus.value = LoadStatus.DONE
//                _showRecycler.value = true
//
//
//            }catch (e : Exception){
//                val es= e.toString()
//                if (es.contains("java.net.UnknownHostException") || es.contains("java.net.SocketTimeoutException")){
//                    _loadingStatus.value = LoadStatus.ERROR
//                    if (_showRecycler.value!!) _showRecycler.value = false
//                }else{
//                    _loadingStatus.value = LoadStatus.NOT_FOUND
//                    if (_showRecycler.value!!) _showRecycler.value = false
//                }
//            }
//        }
//
//    }

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