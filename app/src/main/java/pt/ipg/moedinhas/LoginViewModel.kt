package pt.ipg.moedinhas

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pt.ipg.moedinhas.db.User
import pt.ipg.moedinhas.db.UserDao
import androidx.compose.runtime.State

class LoginViewModel(private val userDao: UserDao) : ViewModel() {

    private val _userBudget = mutableStateOf(1000.0)
    val userBudget: State<Double> = _userBudget

    fun login(username: String, password: String, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val user = userDao.getUserByUsernameAndPassword(username, password)
            withContext(Dispatchers.Main) {
                Log.d("LOGIN", "Utilizador $username tentou fazer login")
                onResult(user != null)
            }
        }
    }

    fun register(username: String, password: String, onResult: (Boolean) -> Unit) {
        CoroutineScope(Dispatchers.IO).launch {
            val existingUser = userDao.getUserByUsername(username)
            if (existingUser == null) {
                userDao.inserir(User(username = username, password = password))
                withContext(Dispatchers.Main) {
                    Log.d("Register", "Utilizador $username tentou registar-se")
                    onResult(true)
                }
            } else {
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            }
        }
    }

    fun listarTodosOsUtilizadores() {
        CoroutineScope(Dispatchers.IO).launch {
            val todos = userDao.getAllUsers()
            todos.forEach {
                Log.d("USERS", "Utilizador: ${it.username}, Password: ${it.password}")
            }
        }
    }

    val apiKey = "14f8e2f8deeb45291b6d8a08"
    val baseCurrency = "EUR"

    fun obterTaxasAtualizadas(
        base: String,
        onResult: (Map<String, Double>?) -> Unit
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.api.getRates(apiKey, base)
                if (response.isSuccessful) {
                    val rates = response.body()?.conversion_rates
                    withContext(Dispatchers.Main) {
                        onResult(rates)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(null)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(null)
                }
            }
        }
    }

}