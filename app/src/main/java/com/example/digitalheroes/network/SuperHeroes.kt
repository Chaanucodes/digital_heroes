package com.example.digitalheroes.network

import com.example.digitalheroes.database.DatabaseHeroes
import com.example.digitalheroes.domain.SuperBioPieces
import com.squareup.moshi.Json
import java.net.ProtocolFamily

data class SuperHeroesList(
    val results : List<SuperHeroes>
)

data class SuperHeroes(
    val name : String,
    val id : String,
    val powerstats : SuperStats,
    var image : SuperImage,
    var biography : SuperBio,
    var connections : SuperConnection,
    var appearance: SuperAppearance
)


data class SuperStats(
    val speed : String,
    val strength : String,
    val intelligence : String,
    val combat : String
)

data class SuperImage(
    val url: String
)

data class SuperBio(
    val publisher : String,

    @Json(name = "full-name")
    val fullName : String
)

data class SuperConnection(
    val relatives : String
)

data class SuperAppearance(
    val height: List<String>,
    val weight : List<String>
)


fun SuperHeroesList.asDatabaseModel() : List<DatabaseHeroes>{

    return results.map {
        DatabaseHeroes(
            name = it.name,
            id = it.id,
            image = it.image.url,
            speed = it.powerstats.speed,
            strength = it.powerstats.strength,
            intelligence = it.powerstats.intelligence,
            combat = it.powerstats.combat,
            publisher = it.biography.publisher,
            fullName = it.biography.fullName,
            relatives = it.connections.relatives,
            height = it.appearance.height,
            weight = it.appearance.weight
        )
    }
}
