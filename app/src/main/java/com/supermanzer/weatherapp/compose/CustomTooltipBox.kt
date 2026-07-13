package com.supermanzer.weatherapp.compose

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TooltipBox
import androidx.compose.material3.TooltipDefaults
import androidx.compose.material3.rememberTooltipState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun customTooltipBox(
    tooltipText: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
 ) {
    val tooltipState = rememberTooltipState()
    val scope = rememberCoroutineScope()

    TooltipBox(
        positionProvider = TooltipDefaults
            .rememberPlainTooltipPositionProvider(),
        tooltip = {
            Card {
                Text(
                    text = tooltipText,
                    modifier = Modifier.padding(8.dp)
                )
            }
        },
        state = tooltipState,
        modifier = modifier
    ) {
        content()
    }
}