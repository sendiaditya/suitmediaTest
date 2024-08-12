package com.magang.suitmediatest

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.magang.suitmediatest.api.ApiConfig
import com.magang.suitmediatest.api.DataItem
import com.magang.suitmediatest.api.ReqresResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ThirdViewModel : ViewModel() {

    private val _users = MutableLiveData<List<DataItem>>()
    val users: LiveData<List<DataItem>> = _users

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> = _loading

    private val allUsers = mutableListOf<DataItem>()

    fun loadUsers(page: Int) {
        _loading.postValue(true)
        viewModelScope.launch(Dispatchers.IO) {
            ApiConfig.getApiService().getUsers(page).enqueue(object : Callback<ReqresResponse> {
                override fun onResponse(call: Call<ReqresResponse>, response: Response<ReqresResponse>) {
                    _loading.postValue(false)
                    if (response.isSuccessful) {
                        response.body()?.data?.let { data ->
                            val nonNullData = data.filterNotNull()
                            if (page == 1) {
                                allUsers.clear()
                            }
                            if (nonNullData.isEmpty() && page > 1) {
                                _error.postValue("No more users found")
                            } else {
                                allUsers.addAll(nonNullData)
                                _users.postValue(allUsers)
                            }
                        }
                    } else {
                        _error.postValue("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ReqresResponse>, t: Throwable) {
                    _loading.postValue(false)
                    _error.postValue("Failure: ${t.message}")
                }
            })
        }
    }

    fun refreshUsers(page: Int) {
        allUsers.clear()
        loadUsers(page)
    }
}