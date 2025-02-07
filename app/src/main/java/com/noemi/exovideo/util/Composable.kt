package com.noemi.exovideo.util

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.noemi.exovideo.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeAppBar(titleResource: Int, modifier: Modifier = Modifier) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(id = titleResource),
                style = MaterialTheme.typography.titleLarge
            )
        },
        navigationIcon = {
            Icon(
                painter = painterResource(id = R.drawable.icon),
                contentDescription = stringResource(id = R.string.label_icon_content_description),
                modifier = modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .size(32.dp),
                tint = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
            titleContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        modifier = modifier.testTag(stringResource(id = R.string.label_app_bar_tag))
    )
}