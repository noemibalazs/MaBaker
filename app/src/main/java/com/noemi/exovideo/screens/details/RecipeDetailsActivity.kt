package com.noemi.exovideo.screens.details

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.ui.theme.ExoVideoTheme
import dagger.hilt.android.AndroidEntryPoint

@Suppress("DEPRECATION")
@AndroidEntryPoint
class RecipeDetailsActivity : ComponentActivity() {

    private val viewModel: RecipeDetailsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {

            val recipe = intent.getParcelableExtra(RECIPE_KEY) ?: Recipe()
            viewModel.getRecipeDetails(recipe)

            ExoVideoTheme {
                RecipeDetailsScreen(viewModel)
            }
        }
    }

    companion object {

        const val RECIPE_KEY = "recipe_key"

        fun newInstance(context: Context, recipe: Recipe) {
            val intent = Intent(context, RecipeDetailsActivity::class.java)
            intent.putExtra(RECIPE_KEY, recipe)
            context.startActivity(intent)
        }
    }
}