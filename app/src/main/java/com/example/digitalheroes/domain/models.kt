package com.example.digitalheroes.domain

import com.example.digitalheroes.network.*
import com.squareup.moshi.Json

data class SuperHeroPieces(
    val name : String,
    val id : String,
    val powerstats : SuperStats,
    var image : SuperImage,
    var biography : SuperBioPieces,
    var connections : SuperConnection,
    var appearance: SuperAppearance
)

data class SuperBioPieces(
    val publisher : String,
    val fullName : String
)