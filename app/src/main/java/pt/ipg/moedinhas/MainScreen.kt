package pt.ipg.moedinhas

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    repository: CurrencyRepository,
    loginViewModel: LoginViewModel,
    onBackToWelcome: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var valorTexto by remember { mutableStateOf("") }
    var moedaOrigem by remember { mutableStateOf("EUR") }
    var moedaDestino by remember { mutableStateOf("USD") }
    var resultado by remember { mutableStateOf<Double?>(null) }
    val historico = remember { mutableStateListOf<String>() }
    var mensagemErro by remember { mutableStateOf<String?>(null) }
    val moedas = repository.obterMoedas()
    val budget by loginViewModel.userBudget

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("Conversor de Moedas", style = MaterialTheme.typography.headlineMedium)

        OutlinedTextField(
            value = valorTexto,
            onValueChange = { valorTexto = it },
            label = { Text("Valor a converter") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            modifier = Modifier.fillMaxWidth()
        )

        DropdownMenuMoeda(
            label = "Moeda de origem",
            selecionada = moedaOrigem,
            opcoes = moedas,
            aoSelecionar = { moedaOrigem = it }
        )

        Button(
            onClick = {
                val temp = moedaOrigem
                moedaOrigem = moedaDestino
                moedaDestino = temp
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Inverter Moedas")
        }

        DropdownMenuMoeda(
            label = "Moeda de destino",
            selecionada = moedaDestino,
            opcoes = moedas,
            aoSelecionar = { moedaDestino = it }
        )

        Button(
            onClick = {
                val valor = valorTexto.toDoubleOrNull()
                if (valor != null) {
                    if (valor > budget) {
                        mensagemErro = "Budget insuficiente"
                    } else {
                        scope.launch {
                            val resultadoConversao =
                                repository.converter(valor, moedaOrigem, moedaDestino)
                            resultado = resultadoConversao
                            resultadoConversao?.let {
                                historico.add(
                                    0,
                                    "%.2f $moedaOrigem → %.2f $moedaDestino".format(valor, it)
                                )
                            }
                            mensagemErro = null
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Converter")
        }

        mensagemErro?.let {
            Text(
                text = it,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        resultado?.let {
            Text(
                "Resultado: %.2f $moedaDestino".format(it),
                style = MaterialTheme.typography.bodyLarge
            )
        }

        if (historico.isNotEmpty()) {
            Text("Histórico:", style = MaterialTheme.typography.titleMedium)
            historico.forEach {
                Text(it)
            }

            Button(
                onClick = { historico.clear() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Limpar histórico")
            }
        }

        Spacer(Modifier.height(16.dp))

        OutlinedButton(
            onClick = onBackToWelcome,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Voltar ao Início")
        }
    }
}