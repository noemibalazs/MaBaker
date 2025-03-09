package com.noemi.exovideo.repository

import com.noemi.exovideo.model.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {

    fun getRecipes(): Flow<List<Recipe>>
}