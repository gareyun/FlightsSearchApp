package com.example.flightsearchapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "airport")
data class Airport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "iata_code") val iata: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "passengers")val passengers: Int
)
