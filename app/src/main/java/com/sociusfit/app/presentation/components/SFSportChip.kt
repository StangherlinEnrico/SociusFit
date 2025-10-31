package com.sociusfit.app.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.sociusfit.app.domain.model.SportLevel

/**
 * Chip per visualizzare uno sport (non selezionabile)
 *
 * @param sportName Nome dello sport
 * @param level Livello opzionale
 * @param modifier Modifier opzionale
 */
@Composable
fun SFSportChip(
    sportName: String,
    level: SportLevel? = null,
    modifier: Modifier = Modifier
) {
    AssistChip(
        onClick = { },
        label = {
            val text = if (level != null) {
                "$sportName - ${level.displayName}"
            } else {
                sportName
            }
            Text(text)
        },
        modifier = modifier.padding(end = 8.dp),
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            labelColor = MaterialTheme.colorScheme.onSecondaryContainer
        )
    )
}

/**
 * Chip selezionabile per sport (usato in selezione)
 *
 * @param sportName Nome dello sport
 * @param selected Se il chip è selezionato
 * @param onSelectedChange Callback quando cambia la selezione
 * @param modifier Modifier opzionale
 */
@Composable
fun SFSelectableSportChip(
    sportName: String,
    selected: Boolean,
    onSelectedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    FilterChip(
        selected = selected,
        onClick = { onSelectedChange(!selected) },
        label = { Text(sportName) },
        modifier = modifier.padding(end = 8.dp),
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = MaterialTheme.colorScheme.primary,
            selectedLabelColor = MaterialTheme.colorScheme.onPrimary
        )
    )
}

/**
 * Chip per visualizzare il livello sportivo con colore
 *
 * @param level Livello sportivo
 * @param modifier Modifier opzionale
 */
@Composable
fun SFLevelChip(
    level: SportLevel,
    modifier: Modifier = Modifier
) {
    val containerColor = when (level) {
        SportLevel.BEGINNER -> MaterialTheme.colorScheme.tertiaryContainer
        SportLevel.INTERMEDIATE -> MaterialTheme.colorScheme.secondaryContainer
        SportLevel.ADVANCED -> MaterialTheme.colorScheme.primaryContainer
        SportLevel.PRO -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (level) {
        SportLevel.BEGINNER -> MaterialTheme.colorScheme.onTertiaryContainer
        SportLevel.INTERMEDIATE -> MaterialTheme.colorScheme.onSecondaryContainer
        SportLevel.ADVANCED -> MaterialTheme.colorScheme.onPrimaryContainer
        SportLevel.PRO -> MaterialTheme.colorScheme.onErrorContainer
    }

    AssistChip(
        onClick = { },
        label = { Text(level.displayName) },
        modifier = modifier,
        colors = AssistChipDefaults.assistChipColors(
            containerColor = containerColor,
            labelColor = contentColor
        )
    )
}