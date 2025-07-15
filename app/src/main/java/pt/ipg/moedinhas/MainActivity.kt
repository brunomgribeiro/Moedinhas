package pt.ipg.moedinhas

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import pt.ipg.moedinhas.screens.LoginScreen
import pt.ipg.moedinhas.screens.RegisterScreen
import pt.ipg.moedinhas.screens.WelcomeScreen

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val context = LocalContext.current
            val db = remember { AppDatabase.getInstance(context) }
            val userDao = db.userDao()
            val loginViewModel = remember { LoginViewModel(userDao) }

            var currentScreen by remember { mutableStateOf("welcome") }

            when (currentScreen) {
                "welcome" -> WelcomeScreen(
                    onLoginClick = { currentScreen = "login" },
                    onRegisterClick = { currentScreen = "register" }
                )

                "login" -> LoginScreen(
                    viewModel = loginViewModel,
                    onLoginSuccess = { currentScreen = "main" },
                    onBack = { currentScreen = "welcome" }
                )

                "register" -> RegisterScreen(
                    viewModel = loginViewModel,
                    onRegisterSuccess = { currentScreen = "main" },
                    onBack = { currentScreen = "welcome" }
                )

                "main" -> MainScreen(
                    repository = CurrencyRepository(),
                    loginViewModel = loginViewModel,
                    onBackToWelcome = { currentScreen = "welcome" },
                )
            }
        }
    }
}
