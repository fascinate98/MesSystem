package com.returnz3ro.messystem.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.service.model.Joborder

import com.returnz3ro.messystem.service.model.User
import com.returnz3ro.messystem.service.repository.JoborderRepository
import com.returnz3ro.messystem.service.repository.UserRepository


class MainViewModel(val context: Context) : ViewModel() {

    fun getJoborderListService(): LiveData<List<Joborder>>?{
        return JoborderRepository.getInstance(context).getJoborderList()}

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(val context: Context) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(context) as T
        }
    }
}