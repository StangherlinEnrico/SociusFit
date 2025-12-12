package com.sociusfit.core.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.sociusfit.core.domain.model.Municipality

/**
 * City Autocomplete TextField
 *
 * TextField con dropdown autocomplete usando ExposedDropdownMenuBox di Material3
 * Supporta navigazione al campo successivo con onNext
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CityAutocompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    onCitySelected: (Municipality) -> Unit,
    municipalities: List<Municipality>,
    modifier: Modifier = Modifier,
    label: String = "Città",
    placeholder: String = "Es. Milano",
    isError: Boolean = false,
    errorMessage: String? = null,
    onNext: () -> Unit = {}
) {
    var expanded by remember { mutableStateOf(false) }

    // Filtra i comuni in base al testo inserito
    val filteredMunicipalities = remember(value, municipalities) {
        if (value.length < 2) {
            emptyList()
        } else {
            municipalities.filter {
                it.municipalityName.contains(value, ignoreCase = true) ||
                        it.regionName.contains(value, ignoreCase = true)
            }.take(10)
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded && filteredMunicipalities.isNotEmpty(),
        onExpandedChange = { /* Controlliamo manualmente */ },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = {
                onValueChange(it)
                expanded = it.length >= 2
            },
            label = { Text(label) },
            placeholder = { Text(placeholder) },
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            singleLine = true,
            isError = isError,
            supportingText = errorMessage?.let {
                {
                    Text(
                        it,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { onNext() }
            )
        )

        ExposedDropdownMenu(
            expanded = expanded && filteredMunicipalities.isNotEmpty(),
            onDismissRequest = { expanded = false }
        ) {
            filteredMunicipalities.forEach { municipality ->
                DropdownMenuItem(
                    text = {
                        Column(modifier = Modifier.padding(vertical = 4.dp)) {
                            Text(
                                text = municipality.municipalityName,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = municipality.regionName,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    onClick = {
                        onCitySelected(municipality)
                        onValueChange(municipality.municipalityName)
                        expanded = false
                    },
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
                )
            }
        }
    }
}