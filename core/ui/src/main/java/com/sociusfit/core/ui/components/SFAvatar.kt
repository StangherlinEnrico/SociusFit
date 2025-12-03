package com.sociusfit.core.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun SFAvatar(
    photoUrl: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    size: Int = 48
) {
    AsyncImage(
        model = photoUrl,
        contentDescription = contentDescription,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape),
        contentScale = ContentScale.Crop
    )
}