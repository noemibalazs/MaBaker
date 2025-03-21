package com.noemi.exovideo.screens.details

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.OptIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.noemi.exovideo.R
import com.noemi.exovideo.util.RecipeAppBar

@OptIn(UnstableApi::class)
@Composable
fun RecipeDetailsScreen(viewModel: RecipeDetailsViewModel, modifier: Modifier = Modifier) {

    val mediaItems by viewModel.mediaItems.collectAsStateWithLifecycle()
    val ingredients by viewModel.ingredients.collectAsStateWithLifecycle()

    val context = LocalContext.current
    val lifeCycleOwner = LocalLifecycleOwner.current
    val lazyState = rememberLazyListState()

    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {

            setMediaItems(mediaItems)
            prepare()
            playWhenReady = true
            if (viewModel.playerCurrentPosition != C.TIME_UNSET) seekTo(viewModel.playerCurrentPosition)

            addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    viewModel.onStepIndexChanged(currentPeriodIndex)
                }
            })
        }
    }

    DisposableEffect(lifeCycleOwner) {
        val listener = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_START -> if (exoPlayer.isPlaying.not()) exoPlayer.play()
                Lifecycle.Event.ON_STOP -> exoPlayer.pause()
                else -> {}
            }
        }

        lifeCycleOwner.lifecycle.addObserver(listener)
        onDispose {
            viewModel.onPlayerPositionChanged(exoPlayer.contentPosition)
            exoPlayer.release()
            lifeCycleOwner.lifecycle.removeObserver(listener)
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .testTag(stringResource(id = R.string.label_recipe_content_tag)),
        state = lazyState,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        item {
            RecipeAppBar(titleResource = R.string.label_recipe_steps)
        }

        item {
            RecipeVideoSurface(exoPlayer = exoPlayer, context = context)
        }

        if (mediaItems.isNotEmpty()) item {
            RecipeStepDescription(mediaItems[viewModel.stepIndex].mediaId)
        }

        item {
            RecipeIngredients()
        }

        item { Spacer(modifier = modifier.padding(bottom = 4.dp)) }

        items(
            items = ingredients,
            key = { ingredient -> ingredient.hashCode() }
        ) { ingredient ->
            IngredientItem(ingredient = ingredient)
        }

        item { Spacer(modifier = modifier.padding(bottom = 8.dp)) }
    }
}

@OptIn(UnstableApi::class)
@Composable
fun RecipeVideoSurface(modifier: Modifier = Modifier, exoPlayer: ExoPlayer, context: Context) {

    AndroidView(
        modifier = modifier
            .fillMaxWidth()
            .height(270.dp)
            .testTag(stringResource(id = R.string.label_recipe_video_surface_tag)),
        factory = {
            PlayerView(context).apply {
                player = exoPlayer

                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
                layoutParams = FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            }
        })
}

@Composable
fun RecipeStepDescription(description: String, modifier: Modifier = Modifier) {
    Text(
        text = description,
        textAlign = TextAlign.Justify,
        modifier = modifier
            .padding(top = 12.dp, start = 20.dp, end = 20.dp, bottom = 12.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp, fontWeight = FontWeight.SemiBold, fontStyle = FontStyle.Italic)
    )
}

@Composable
fun RecipeIngredients(modifier: Modifier = Modifier) {
    Text(
        text = stringResource(id = R.string.label_recipe_ingredients),
        modifier = modifier
            .padding(horizontal = 20.dp)
            .fillMaxWidth(),
        style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp, fontWeight = FontWeight.Bold)
    )
}

@Composable
fun IngredientItem(ingredient: String, modifier: Modifier = Modifier) {

    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.label_ingredient_tag)),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {

        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                painter = painterResource(id = R.drawable.star),
                contentDescription = null, tint = Color.Red,
                modifier = modifier.padding(horizontal = 6.dp)
            )

            Text(
                text = ingredient, color = Color.Black,
                textAlign = TextAlign.Justify,
                modifier = modifier.padding(6.dp),
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            )
        }
    }

}