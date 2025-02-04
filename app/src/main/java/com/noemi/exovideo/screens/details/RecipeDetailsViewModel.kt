package com.noemi.exovideo.screens.details

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import com.noemi.exovideo.model.Recipe
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailsViewModel @Inject constructor() : ViewModel() {

    private val _ingredients = MutableStateFlow(emptyList<String>())
    val ingredients = _ingredients.asStateFlow()

    private val _mediaItems = MutableStateFlow(emptyList<MediaItem>())
    val mediaItems = _mediaItems.asStateFlow()

    var playerCurrentPosition by mutableLongStateOf(0L)
        private set

    var stepIndex by mutableIntStateOf(0)
        private set

    fun getRecipeDetails(selectedRecipe: Recipe) {
        val ingredients = mutableListOf<String>()
        val items = mutableListOf<MediaItem>()

        selectedRecipe.ingredients.map { ingredient ->
            ingredients.add(ingredient.toString())
        }

        selectedRecipe.steps.map { step ->

            val uri = when {
                step.videoURL.isNotEmpty() -> step.videoURL
                step.thumbnailURL.isNotEmpty() -> step.thumbnailURL
                else -> selectedRecipe.image
            }

            items.add(
                MediaItem.Builder()
                    .setUri(uri)
                    .setMediaId(step.description)
                    .build()
            )
        }

        viewModelScope.launch {
            _ingredients.emit(ingredients)
            _mediaItems.emit(items)
        }
    }

    fun onStepIndexChanged(index: Int) {
        stepIndex = index
    }

    fun onPlayerPositionChanged(position: Long) {
        playerCurrentPosition = position
    }
}