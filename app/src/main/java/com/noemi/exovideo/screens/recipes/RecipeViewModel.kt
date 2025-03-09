package com.noemi.exovideo.screens.recipes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.repository.RecipeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeViewModel @Inject constructor(
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _recipes = MutableStateFlow(emptyList<Recipe>())
    val recipes = _recipes.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow("")
    val errorMessage = _errorMessage.asStateFlow()

    init {
        loadRecipes()
    }

    private fun loadRecipes() {
        viewModelScope.launch {
            _isLoading.emit(true)

            delay(1000)

            recipeRepository.getRecipes()
                .catch {
                    _isLoading.emit(false)
                    _errorMessage.emit(it.message ?: "Error while getting recipes, try it again!")
                }
                .collect { recipes ->
                    _recipes.emit(recipes)
                    _isLoading.emit(false)
                }
        }
    }
}