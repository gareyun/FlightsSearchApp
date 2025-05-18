package com.example.flightsearchapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.example.flightsearchapp.ui.screens.MainScreen
import com.example.flightsearchapp.ui.viewmodels.FlightSearchViewModel

@Composable
fun App(
    viewModel: FlightSearchViewModel
){
    Surface(color = MaterialTheme.colorScheme.background) {
        MainScreen (viewModel = viewModel)
    }
}
