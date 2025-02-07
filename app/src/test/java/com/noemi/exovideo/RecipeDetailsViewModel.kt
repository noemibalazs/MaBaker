package com.noemi.exovideo

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.noemi.exovideo.model.Ingredient
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.model.Step
import com.noemi.exovideo.screens.details.RecipeDetailsViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeDetailsViewModel {

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()

    private lateinit var viewModel: RecipeDetailsViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = RecipeDetailsViewModel()
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test get recipe details and should be successful`() = runBlocking {

        val job = launch {
            assertThat(viewModel.ingredients.value.isEmpty()).isTrue()
            assertThat(viewModel.mediaItems.value.isEmpty()).isTrue()

            viewModel.mediaItems.test {

                val size = recipe.steps.size

                val mediaResults = awaitItem()
                assertThat(mediaResults.size).isEqualTo(size)

                cancelAndConsumeRemainingEvents()
            }

            viewModel.ingredients.test {
                val results = awaitItem()

                val size = recipe.ingredients.size
                assertThat(results.size).isEqualTo(size)

                cancelAndConsumeRemainingEvents()
            }
        }

        viewModel.getRecipeDetails(recipe)

        job.cancelAndJoin()
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