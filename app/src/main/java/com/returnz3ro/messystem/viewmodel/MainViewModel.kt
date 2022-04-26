package com.returnz3ro.messystem.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.returnz3ro.messystem.service.model.datamodel.Joborder
import com.returnz3ro.messystem.service.model.datamodel.Slitter

import com.returnz3ro.messystem.service.model.datamodel.WorkResult
import com.returnz3ro.messystem.service.repository.JoborderRepository
import com.returnz3ro.messystem.service.repository.SlitterRepository
import com.returnz3ro.messystem.service.repository.WorkResultRepository


class MainViewModel(val context: Context) : ViewModel() {


    fun getAllJoborders(): LiveData<List<Joborder>>?{
        return JoborderRepository.getInstance(context).getJoborderList()
    }

    fun getSlitterList(): LiveData<List<Slitter>>?{
        return SlitterRepository.getInstance(context).getSlitterList()
    }

    fun setStartWork(startworkdata: WorkResult): Int?{
        return WorkResultRepository.getInstance(context).workStart(startworkdata)
    }

    fun setFinishWork(finishworkdata: WorkResult): Int?{
        return WorkResultRepository.getInstance(context).workFinish(finishworkdata)
    }

    fun recogQrcode(jobname: String): LiveData<List<Joborder>>?{
        return JoborderRepository.getInstance(context).recogQrcode(jobname)
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