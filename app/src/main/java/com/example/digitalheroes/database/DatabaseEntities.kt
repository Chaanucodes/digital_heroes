package com.example.digitalheroes.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.digitalheroes.domain.SuperBioPieces
import com.example.digitalheroes.domain.SuperHeroPieces
import com.example.digitalheroes.network.SuperAppearance
import com.example.digitalheroes.network.SuperConnection
import com.example.digitalheroes.network.SuperImage
import com.example.digitalheroes.network.SuperStats

@Entity
data class DatabaseHeroes(
    @PrimaryKey
    val id: String,
    val name: String,
    val speed: String,
    val strength: String,
    val intelligence: String,
    val combat: String,
    val image: String,
    val publisher: String,
    val fullName: String,
    val relatives: String,
    val height: String,
    val weight: String
)

fun List<DatabaseHeroes>.asDomainModel() : List<SuperHeroPieces>{
    return map {
        SuperHeroPieces(
            name = it.name,
            id = it.id,
            powerstats = SuperStats(it.speed, it.strength, it.intelligence, it.combat),
            image = SuperImage(it.image),
            biography = SuperBioPieces(it.publisher, it.fullName),
            connections = SuperConnection(it.relatives),
            appearance = SuperAppearance(listOf(it.height), listOf("",it.weight))
        )
    }
}