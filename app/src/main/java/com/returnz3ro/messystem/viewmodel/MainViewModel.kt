package com.returnz3ro.messystem.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.service.model.Joborder
import com.returnz3ro.messystem.service.model.Slitter

import com.returnz3ro.messystem.service.model.User
import com.returnz3ro.messystem.service.model.WorkResult
import com.returnz3ro.messystem.service.repository.JoborderRepository
import com.returnz3ro.messystem.service.repository.SlitterRepository
import com.returnz3ro.messystem.service.repository.UserRepository
import com.returnz3ro.messystem.service.repository.WorkResultRepository


class MainViewModel(val context: Context) : ViewModel() {


    fun getAllJoborders(): LiveData<List<Joborder>>?{
        return JoborderRepository.getInstance(context).getJoborderList()
    }

    fun getSlitterList(): LiveData<List<Slitter>>?{
        return SlitterRepository.getInstance(context).getSlitterList()
    }

    fun setStartWork(): LiveData<Joborder>?{
        return WorkResultRepository.getInstance(context).workStart()
    }

    override fun onCleared() {
        super.onCleared()
    }

    class Factory(val context: Context) : ViewModelProvider.NewInstanceFactory() {

        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return MainViewModel(context) as T
        }
    }
}