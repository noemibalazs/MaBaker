package com.noemi.exovideo

import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.noemi.exovideo.model.Ingredient
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.model.Step
import com.noemi.exovideo.screens.details.RecipeDetailsActivity
import com.noemi.exovideo.screens.details.RecipeDetailsScreen
import com.noemi.exovideo.screens.details.RecipeDetailsViewModel
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class RecipeDetailsActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RecipeDetailsActivity>()

    @Before
    fun setupRecipeDetailsScreen() {
        val viewModel = composeRule.activity.viewModels<RecipeDetailsViewModel>().value
        viewModel.getRecipeDetails(recipe)
        composeRule.activity.setContent {
            RecipeDetailsScreen(viewModel = viewModel)
        }
    }

    @Test
    fun testAppBarDisplayed() {
        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_app_bar_tag)).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.label_recipe_steps).assertIsDisplayed()
        composeRule.onNode(hasContentDescription(composeRule.activity.getString(R.string.label_icon_content_description))).assertExists()
    }

    @Test
    fun testRecipeIngredientIsDisplayed(){
        composeRule.onNodeWithStringId(R.string.label_recipe_ingredients).assertIsDisplayed()
    }

    @Test
    fun testRecipeIngredientsAreDisplayed() {
        composeRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag(composeRule.activity.getString(R.string.label_ingredient_tag)), 2100L
        )

        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_recipe_content_tag))
            .onChildren()[0]
            .assertIsDisplayed()
            .performClick()
    }

    @Test
    fun testRecipeVideoSurface() {
        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_recipe_video_surface_tag)).assertIsDisplayed()
    }

    companion object {

        val recipe = Recipe(
            id = 3,
            name = "Yellow Cake",
            servings = 8,
            image = "https://upload.wikimedia.org/wikipedia/commons/7/70/Slice_of_pound_cake.jpg",
            ingredients = listOf(
                Ingredient(
                    quantity = "400",
                    measure = "g",
                    ingredient = "sifted cake flour"
                ),
                Ingredient(
                    quantity = "700",
                    measure = "g",
                    ingredient = "granulated sugar"
                )
            ),
            steps = listOf(
                Step(
                    id = 0,
                    shortDescription = "Recipe Introduction",
                    description = "Recipe Introduction",
                    videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffddf0_-intro-yellow-cake/-intro-yellow-cake.mp4",
                    thumbnailURL = ""
                ),
                Step(
                    id = 1,
                    shortDescription = "Starting prep",
                    description = "Recipe Introduction",
                    videoURL = "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffde28_1-mix-all-dry-ingredients-yellow-cake/1-mix-all-dry-ingredients-yellow-cake.mp4",
                    thumbnailURL = ""
                )
            )
        )
    }
}