package pt.ipg.moedinhas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import pt.ipg.moedinhas.ui.theme.MoedinhasTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MoedinhasTheme {
                val repository = CurrencyRepository()
                MainScreen(repository)
            }
        }
    }
}
