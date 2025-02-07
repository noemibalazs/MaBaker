package com.noemi.exovideo

import androidx.activity.compose.setContent
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onChildren
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import com.noemi.exovideo.screens.recipes.RecipesActivity
import com.noemi.exovideo.screens.recipes.RecipesScreen
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalTestApi::class)
class RecipesActivityTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<RecipesActivity>()

    @Before
    fun setupRecipesScreen() {
        composeRule.activity.setContent {
            RecipesScreen()
        }
    }

    @Test
    fun testAppBarDisplayed() {
        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_app_bar_tag)).assertIsDisplayed()
        composeRule.onNodeWithStringId(R.string.app_name).assertIsDisplayed()
        composeRule.onNode(hasContentDescription(composeRule.activity.getString(R.string.label_icon_content_description))).assertExists()
    }

    @Test
    fun testProgressBarIsDisplayed() {
        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_progress_indicator_tag)).assertIsDisplayed()
    }

    @Test
    fun testRecipesAreDisplayed(){
        composeRule.waitUntilAtLeastOneExists(
            matcher = hasTestTag(composeRule.activity.getString(R.string.label_recipe_item_tag)), 2100L
        )

        composeRule.onNodeWithTag(composeRule.activity.getString(R.string.label_lazy_column_tag))
            .onChildren()[0]
            .assertIsDisplayed()
            .performClick()
    }
}