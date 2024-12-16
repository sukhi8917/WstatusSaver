package com.example.wastatussaver.viewmodels.factories

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.wastatussaver.data.StatusRepo
import com.example.wastatussaver.viewmodels.StatusViewModel


class StatusViewModelFactory(private val repo: StatusRepo): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return StatusViewModel(repo) as T
    }
}