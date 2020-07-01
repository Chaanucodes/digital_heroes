package com.example.digitalheroes.compare

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.digitalheroes.network.SuperHeroApi
import com.example.digitalheroes.network.SuperHeroes
import com.example.digitalheroes.search.LoadStatus
import kotlinx.coroutines.*
import java.lang.Exception

class CompareViewModel : ViewModel() {

    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

//    private var viewModelJob2 = Job()
//    private val uiScope2 = CoroutineScope(Dispatchers.Main + viewModelJob2)

    private val _superhero1 = MutableLiveData<List<SuperHeroes>>()
    val superhero1 : LiveData<List<SuperHeroes>>
        get() = _superhero1

    private val _loadingStatus1 = MutableLiveData<LoadStatus>()
    val loadingStatus1 : LiveData<LoadStatus>
        get() = _loadingStatus1

    private val _showRecycler1 = MutableLiveData<Boolean>()
    val showRecycler1 : LiveData<Boolean>
        get() = _showRecycler1

    private val _superhero2 = MutableLiveData<List<SuperHeroes>>()
    val superhero2 : LiveData<List<SuperHeroes>>
        get() = _superhero2

    private val _loadingStatus2 = MutableLiveData<LoadStatus>()
    val loadingStatus2 : LiveData<LoadStatus>
        get() = _loadingStatus2

    private val _showRecycler2 = MutableLiveData<Boolean>()
    val showRecycler2 : LiveData<Boolean>
        get() = _showRecycler2

    init {
        _showRecycler1.value = false
        _showRecycler2.value = false
    }


    fun getFirstSuperHero(searchQuery : String){

        val getInfoDeferred = SuperHeroApi.retrofitService.getSuperHeroList(searchQuery)
        uiScope.launch {
            try {

                _loadingStatus1.value = LoadStatus.LOADING
                if (_showRecycler1.value!!) _showRecycler1.value = false

                val list = mutableListOf<SuperHeroes>()
                val listResult = withContext(Dispatchers.IO){getInfoDeferred.await()}
                for (element in listResult.results){
                    list.add(element)
                }
                _superhero1.value = list
                _loadingStatus1.value = LoadStatus.DONE
                _showRecycler1.value = true


            }catch (e : Exception){
                val es= e.toString()
                if (es.contains("java.net.UnknownHostException") || es.contains("java.net.SocketTimeoutException")){
                    _loadingStatus1.value = LoadStatus.ERROR
                    if (_showRecycler1.value!!) _showRecycler1.value = false
                }else{
                    _loadingStatus1.value = LoadStatus.NOT_FOUND
                    if (_showRecycler1.value!!) _showRecycler1.value = false
                }
            }
        }

    }

    fun getSecondSuperHero(searchQuery : String){

        val getInfoDeferred = SuperHeroApi.retrofitService.getSuperHeroList(searchQuery)
        uiScope.launch {
            try {
                _loadingStatus2.value = LoadStatus.LOADING
                if (_showRecycler2.value!!) _showRecycler2.value = false

                val list = mutableListOf<SuperHeroes>()
                val listResult = withContext(Dispatchers.IO){getInfoDeferred.await()}
                for (element in listResult.results){
                    list.add(element)
                }
                _superhero2.value = list
                _loadingStatus2.value = LoadStatus.DONE
                _showRecycler2.value = true

            }catch (e : Exception){
                val es= e.toString()
                if (es.contains("java.net.UnknownHostException") || es.contains("java.net.SocketTimeoutException")){
                    _loadingStatus2.value = LoadStatus.ERROR
                    if (_showRecycler2.value!!) _showRecycler2.value = false
                }else{
                    _loadingStatus2.value = LoadStatus.NOT_FOUND
                    if (_showRecycler2.value!!) _showRecycler2.value = false
                }
            }
        }

    }



    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

}