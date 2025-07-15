package pt.ipg.moedinhas

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun DropdownMenuMoeda(
    label: String,
    selecionada: String,
    opcoes: List<String>,
    aoSelecionar: (String) -> Unit
) {
    val bandeiras = mapOf(
        "EUR" to "🇪🇺",
        "USD" to "🇺🇸",
        "GBP" to "🇬🇧",
        "JPY" to "🇯🇵",
        "BRL" to "🇧🇷",
        "AUD" to "🇦🇺"
    )

    var expandido by remember { mutableStateOf(false) }

    Column {
        Text(label, style = MaterialTheme.typography.labelLarge)

        OutlinedTextField(
            value = "${bandeiras[selecionada] ?: "💱"} $selecionada",
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { expandido = true }
        )

        DropdownMenu(
            expanded = expandido,
            onDismissRequest = { expandido = false }
        ) {
            opcoes.forEach { moeda ->
                val emoji = bandeiras[moeda] ?: "💱"
                DropdownMenuItem(
                    text = { Text("$emoji $moeda") },
                    onClick = {
                        aoSelecionar(moeda)
                        expandido = false
                    }
                )
            }
        }
    }
}
