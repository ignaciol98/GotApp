package com.example.gameofthrones

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performTextInput
import com.example.gameofthrones.model.House
import com.example.gameofthrones.ui.theme.houses.SearchableHouseListScreen
import org.junit.Rule
import org.junit.Test

class SearchableHouseListScreenTest {

    @get:Rule val rule = createComposeRule()

    @Test
    fun filters_list_by_query() {
        val houses = listOf(
            House(
                id = "stark",
                name = "Stark",
                description = "House of the North",
                motto = "Winter is Coming",
                // sigilUrl usa default = ""
                notableCharacters = emptyList(),
                locationName = "Winterfell"
            ),
            House(
                id = "lannister",
                name = "Lannister",
                description = "House of the Westerlands",
                motto = "Hear Me Roar!",
                notableCharacters = emptyList(),
                locationName = "Casterly Rock"
            )
        )

        rule.setContent {
            SearchableHouseListScreen(houses = houses, onHouseClick = {})
        }


        rule.onNodeWithTag("house-stark").assertIsDisplayed()
        rule.onNodeWithTag("house-lannister").assertIsDisplayed()

        // Filtro
        rule.onNodeWithTag("search").performTextInput("star")

        // Stark permanece visible
        rule.onNodeWithTag("house-stark").assertIsDisplayed()

    }
}
