package com.example.flightsearchapp.ui.screens

import SearchField
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.flightsearchapp.data.models.Favorite
import com.example.flightsearchapp.ui.views.AirportList
import com.example.flightsearchapp.ui.views.FavoriteList
import com.example.flightsearchapp.ui.views.FlightList
import com.example.flightsearchapp.ui.viewmodels.FlightSearchViewModel
import androidx.compose.foundation.layout.statusBars
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.res.imageResource
import com.example.flightsearchapp.R


@Composable
fun MainScreen(viewModel: FlightSearchViewModel) {
    val query by viewModel.searchQuery.collectAsState()
    var searchText by remember { mutableStateOf(query) }

    val airports by viewModel.airports.collectAsState()
    val flights by viewModel.flights.collectAsState()
    val currAirport by viewModel.currAirport.collectAsState()
    val favorites by viewModel.favorites.collectAsState()
    val isSearchEnabled = currAirport == null

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
            .padding(horizontal = 16.dp)
    ) {
        if (isSearchEnabled) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Авиасейлс",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    ),
                    modifier = Modifier.weight(1f)
                )

                Image(
                    modifier = Modifier
                        .padding(5.dp)
                        .width(25.dp)
                        .height(25.dp),
                    bitmap = ImageBitmap.imageResource(R.drawable.icon),
                    contentDescription = "Самолёт"
                )
            }
        }

        if (isSearchEnabled) {
            SearchField(
                query = query,
                onQueryChange = {
                    searchText = it
                    viewModel.saveQuery(searchText)
                    viewModel.searchAirports(it)
                },
                modifier = Modifier.padding(bottom = 16.dp)
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        viewModel.clearSelection()
                        viewModel.searchAirports(query)
                    },
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Назад",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    text = "Выбор аэропорта назначения",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        if (query.isNotEmpty() && currAirport == null) {
            AirportList(
                airports = airports,
                onClickAirport = { airport ->
                    viewModel.selectAirport(airport)
                    viewModel.getAllDestinations(airport.id)
                }
            )
        }

        if (currAirport != null) {
            FlightList(
                departureAirport = currAirport!!,
                flights = flights,
                favorites = favorites,
                onClickFlight = { flight ->
                    viewModel.insertOrDeleteFavorite(
                        Favorite(
                            destinationCode = flight.destinationAirport.iata,
                            departureCode = flight.departureAirport.iata
                        )
                    )
                }
            )
        } else if (query.isEmpty()) {
            FavoriteList(
                favorites = favorites,
                onFavoriteClick = { favorite ->
                    viewModel.deleteFavorite(favorite)
                }
            )
        }
    }
}