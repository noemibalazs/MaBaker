package com.noemi.exovideo.screens.recipes

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.noemi.exovideo.R
import com.noemi.exovideo.model.Recipe
import com.noemi.exovideo.screens.details.RecipeDetailsActivity
import com.noemi.exovideo.util.RecipeAppBar

@Composable
fun RecipesScreen(modifier: Modifier = Modifier) {
    val viewModel = hiltViewModel<RecipeViewModel>()

    val recipes by viewModel.recipes.collectAsStateWithLifecycle()
    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val errorMessage by viewModel.errorMessage.collectAsStateWithLifecycle()

    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        RecipeAppBar(titleResource = R.string.app_name, iconDescriptionResource = R.string.label_icon_content_description)

        when (isLoading) {
            true -> RecipeProgressBar()
            else -> RecipesLazyGrid(recipes = recipes)
        }

        if (errorMessage.isNotEmpty()) {
            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun RecipeProgressBar(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = modifier
                .size(90.dp)
                .testTag(stringResource(id = R.string.label_progress_indicator_tag)),
            strokeWidth = 9.dp,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun RecipesLazyGrid(recipes: List<Recipe>, modifier: Modifier = Modifier) {
    val gridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .testTag(stringResource(id = R.string.label_lazy_column_tag)),
        content = {
            items(
                count = recipes.size
            ) { index ->
                RecipeItem(recipe = recipes[index])
            }
        }
    )
}

@Composable
fun RecipeItem(recipe: Recipe, modifier: Modifier = Modifier) {
    val itemHeight = LocalConfiguration.current.screenHeightDp.dp / 2
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(itemHeight)
            .clickable {
                RecipeDetailsActivity.newInstance(context, recipe)
            }
            .testTag(stringResource(id = R.string.label_recipe_item_tag)),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = recipe.image,
            contentDescription = stringResource(id = R.string.label_recipe_avatar),
            modifier = modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Text(
            text = recipe.name,
            modifier = modifier
                .padding(20.dp)
                .align(Alignment.BottomStart),
            color = MaterialTheme.colorScheme.errorContainer,
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold)
        )
    }
}