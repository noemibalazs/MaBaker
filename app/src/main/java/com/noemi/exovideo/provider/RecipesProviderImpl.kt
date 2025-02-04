package com.noemi.exovideo.provider

import android.content.Context
import com.google.common.reflect.TypeToken
import com.google.gson.Gson
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.util.ASSETS_JSON_FILE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RecipesProviderImpl @Inject constructor(private val context: Context) : RecipesProvider {

    override fun getRecipes(): Flow<List<Recipe>> = flow {
        val json = getJsonString()
        val recipes: List<Recipe> = Gson().fromJson(json, object : TypeToken<List<Recipe>>() {}.type)
        emit(recipes)
    }

    private fun getJsonString(): String {
        return context.assets.open(ASSETS_JSON_FILE).bufferedReader().use { it.readText() }
    }
}