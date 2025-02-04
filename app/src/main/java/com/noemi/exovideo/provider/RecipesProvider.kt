package com.noemi.exovideo.provider

import com.noemi.exovideo.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipesProvider {

    fun getRecipes(): Flow<List<Recipe>>
}