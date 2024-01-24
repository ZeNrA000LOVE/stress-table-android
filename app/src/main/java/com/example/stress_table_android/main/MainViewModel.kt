package com.example.stress_table_android.main

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.core.repository.MyRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val myRepository: MyRepository,
    private val savedState: SavedStateHandle
) : ViewModel() {
    var uiState by mutableStateOf(MainUiState())

    fun onValueChange(text: String) {
        uiState = uiState.copy(text = text)
    }

    fun addText() {
        uiState = uiState.copy(
            text = "",
            stressTextList = uiState.stressTextList + uiState.text
        )
    }
}