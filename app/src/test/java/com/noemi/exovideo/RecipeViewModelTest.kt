package com.noemi.exovideo

import app.cash.turbine.test
import io.mockk.mockk
import io.mockk.junit4.MockKRule
import com.google.common.truth.Truth.assertThat
import com.noemi.exovideo.provider.RecipesProvider
import com.noemi.exovideo.screens.recipes.RecipeViewModel
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
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class RecipeViewModelTest {

    @get:Rule
    val mockkRule = MockKRule(this)

    private val dispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val provider: RecipesProvider = mockk()

    private lateinit var viewModel: RecipeViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        viewModel = RecipeViewModel(provider)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test load recipes should be successful`() = runBlocking {

        val job = launch {
            assertThat(viewModel.isLoading.value).isTrue()
            assertThat(viewModel.recipes.value.isEmpty()).isTrue()

            viewModel.recipes.test {
                val results = awaitItem()

                provider.getRecipes().test {
                    val event = awaitItem()
                    assertThat(results).isEqualTo(event)
                    assertThat(viewModel.isLoading.value).isFalse()
                }

                cancelAndConsumeRemainingEvents()
            }
        }

        job.cancelAndJoin()
    }

    @Test
    fun `test load recipes should throw an error`() = runBlocking {

        val job = launch {
            assertThat(viewModel.isLoading.value).isTrue()
            assertThat(viewModel.recipes.value.isEmpty()).isTrue()

            viewModel.recipes.test {
                val recipes = awaitItem()

                provider.getRecipes().test {
                    val eventError = awaitError()

                    assertThat(viewModel.errorMessage.value).isEqualTo(eventError.message)
                    assertThat(viewModel.isLoading.value).isFalse()
                    assertThat(recipes.isEmpty()).isTrue()
                }

                cancelAndConsumeRemainingEvents()
            }
        }

        job.cancelAndJoin()
    }
}