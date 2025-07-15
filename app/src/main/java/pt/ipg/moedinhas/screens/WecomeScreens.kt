package pt.ipg.moedinhas.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun WelcomeScreen(
    onLoginClick: () -> Unit,
    onRegisterClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bem-vindo à Moedinhas!", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(24.dp))

        Button(onClick = onLoginClick, modifier = Modifier.fillMaxWidth()) {
            Text("Login")
        }
        Spacer(Modifier.height(12.dp))

        Button(onClick = onRegisterClick, modifier = Modifier.fillMaxWidth()) {
            Text("Registar")
        }
    }
}
