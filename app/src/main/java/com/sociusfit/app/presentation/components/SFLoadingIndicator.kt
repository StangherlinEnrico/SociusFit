package com.sociusfit.app.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Indicatore di caricamento centrato
 *
 * @param modifier Modifier opzionale
 * @param size Dimensione del progress indicator
 */
@Composable
fun SFLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 48.dp
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.size(size),
            color = MaterialTheme.colorScheme.primary
        )
    }
}

/**
 * Indicatore di caricamento inline (non centrato)
 *
 * @param modifier Modifier opzionale
 * @param size Dimensione del progress indicator
 */
@Composable
fun SFInlineLoadingIndicator(
    modifier: Modifier = Modifier,
    size: Dp = 24.dp
) {
    CircularProgressIndicator(
        modifier = modifier.size(size),
        color = MaterialTheme.colorScheme.primary,
        strokeWidth = 3.dp
    )
}