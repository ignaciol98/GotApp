package com.example.gameofthrones.data

import com.example.gameofthrones.model.Region
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RegionRepository @Inject constructor() {

    fun getAllRegions(): List<Region> = listOf(
        Region(
            id = "north",
            name = "El Norte",
            houseIds = listOf(
                "stark",
                "bolton",
                "mormont",
                "karstark",
                "cerwyn",
                "glover",
                "umber",
                "manderly",
                "reed"
            )
        ),
        Region(
            id = "vale",
            name = "El Valle de Arryn",
            houseIds = listOf("arryn", "royce", "templeton", "corbray")
        ),
        Region(
            id = "riverlands",
            name = "Tierras de los Ríos",
            houseIds = listOf("tully", "frey", "blackwood", "mallister", "darry")
        ),
        Region(
            id = "westerlands",
            name = "Tierras del Oeste",
            houseIds = listOf("lannister", "reynolds", "caswell", "clegane")
        ),
        Region(
            id = "iron_islands",
            name = "Islas del Hierro",
            houseIds = listOf("greyjoy", "harlaw", "slaney", "goodbrother")
        ),
        Region(
            id = "crownlands",
            name = "Tierras de la Corona",
            houseIds = listOf("targaryen", "baratheon", "velaryon", "swan")
        ),
        Region(
            id = "reach",
            name = "El Dominio",
            houseIds = listOf("tyrell", "hightower", "tarly", "meryn", "oakheart", "florent")
        ),
        Region(
            id = "stormlands",
            name = "Tierras de la Tormenta",
            houseIds = listOf("baratheon", "dondarrion", "tarth", "swann")
        ),
        Region(
            id = "dorne",
            name = "Dorne",
            houseIds = listOf("martell", "ylre", "manwoody", "dayne", "blackmont")
        )
    )
}