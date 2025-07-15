package pt.ipg.moedinhas

import android.os.Build
import androidx.annotation.RequiresApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pt.ipg.moedinhas.RetrofitClient

class CurrencyRepository {
    private val apiKey = "14f8e2f8deeb45291b6d8a08"
    fun obterMoedas(): List<String> = listOf("EUR", "USD", "GBP", "JPY", "BRL", "AUD")

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun converter(valor: Double, origem: String, destino: String): Double? {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getRates(apiKey, origem)
                if (response.isSuccessful) {
                    val taxa = response.body()?.conversion_rates?.get(destino)
                    taxa?.let { valor * it }
                } else null
            } catch (e: Exception) {
                null
            }
        }
    }
}