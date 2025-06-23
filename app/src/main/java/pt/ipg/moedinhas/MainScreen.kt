package pt.ipg.moedinhas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(repository: CurrencyRepository) {
    var valorTexto by remember { mutableStateOf("") }
    var moedaOrigem by remember { mutableStateOf("EUR") }
    var moedaDestino by remember { mutableStateOf("USD") }
    var resultado by remember { mutableStateOf<Double?>(null) }
    val historico = remember { mutableStateListOf<String>() }

    val moedas = repository.obterMoedas()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedTextField(
                value = valorTexto,
                onValueChange = { valorTexto = it },
                label = { Text("Valor a converter") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done
                )
            )

            DropdownMenuMoeda(
                label = "Moeda de origem",
                selecionada = moedaOrigem,
                opcoes = moedas
            ) { selecionada -> moedaOrigem = selecionada }

            Button(
                onClick = {
                    val temp = moedaOrigem
                    moedaOrigem = moedaDestino
                    moedaDestino = temp
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Inverter moedas")
            }

            DropdownMenuMoeda(
                label = "Moeda de destino",
                selecionada = moedaDestino,
                opcoes = moedas
            ) { selecionada -> moedaDestino = selecionada }

            Button(
                onClick = {
                    val valor = valorTexto.toDoubleOrNull()
                    if (valor != null) {
                        val resultadoConversao = repository.converter(valor, moedaOrigem, moedaDestino)
                        resultado = resultadoConversao

                        // Adiciona ao histórico
                        val entrada = "%.2f $moedaOrigem → %.2f $moedaDestino".format(valor, resultadoConversao)
                        historico.add(0, entrada) // adiciona no topo
                    } else {
                        resultado = null
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Converter")
            }

            resultado?.let {
                Text(
                    text = "Resultado: %.2f $moedaDestino".format(it),
                    style = MaterialTheme.typography.bodyLarge
                )
            }

            if (historico.isNotEmpty()) {
                Text("Histórico de conversões:", style = MaterialTheme.typography.titleMedium)
                historico.forEach { entrada ->
                    Text(entrada, style = MaterialTheme.typography.bodySmall)
                }

                Button(
                    onClick = { historico.clear() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Limpar histórico")
                }
            }
        }
    }
}
