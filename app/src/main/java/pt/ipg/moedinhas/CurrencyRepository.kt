package pt.ipg.moedinhas

class CurrencyRepository {

    // Mapa de taxas em relação ao EUR (moeda base)
    private val taxas = mapOf(
        "EUR" to 1.0,
        "USD" to 1.1,
        "GBP" to 0.85,
        "JPY" to 150.0
    )


    fun obterMoedas(): List<String> = taxas.keys.toList()

    fun converter(valor: Double, de: String, para: String): Double {
        val valorEmEuro = valor / (taxas[de] ?: 1.0)
        return valorEmEuro * (taxas[para] ?: 1.0)
    }
}
