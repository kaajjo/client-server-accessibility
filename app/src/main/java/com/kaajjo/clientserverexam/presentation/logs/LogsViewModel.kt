package com.kaajjo.clientserverexam.presentation.logs

import androidx.lifecycle.ViewModel
import com.kaajjo.clientserverexam.domain.repository.ActionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LogsViewModel @Inject constructor(
    private val actionRepository: ActionRepository
) : ViewModel() {
    val logs = actionRepository.get()
}