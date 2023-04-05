package com.galemu00.acronyms.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.galemu00.acronyms.data.AcronymRepository
import com.galemu00.acronyms.data.model.Acronyms
import com.galemu00.acronyms.util.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response

class AcronymsViewModel(private val repository: AcronymRepository = AcronymRepository()) :
    ViewModel() {

    val acronymList: MutableLiveData<Resource<Acronyms>> = MutableLiveData()

    fun getAcronyms(sf: String) {
        viewModelScope.launch(Dispatchers.Main) {
            acronymList.postValue(Resource.Loading())
            val response = repository.getAcronyms(sf)
            acronymList.postValue(handleResponse(response))
        }
    }

    fun clearResults() {
        viewModelScope.launch(Dispatchers.Main) {
            acronymList.postValue(Resource.Idel())
        }
    }

    private fun handleResponse(response: Response<Acronyms>): Resource<Acronyms> {
        if (response.isSuccessful) {
            response.body()?.let { acronyms ->
                return Resource.Success(acronyms)

            }
        }
        return Resource.Error(message = response.message())
    }


}