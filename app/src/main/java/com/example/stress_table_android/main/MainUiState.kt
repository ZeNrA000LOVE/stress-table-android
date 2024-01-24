package com.example.stress_table_android.main

data class MainUiState(
    val text:  String = "",
    val stressTextList: List<Stress> = emptyList(),
    val selectText: String = ""
)

data class Stress(
    val text: String = "",
    val type: ButtonType? = null
)
