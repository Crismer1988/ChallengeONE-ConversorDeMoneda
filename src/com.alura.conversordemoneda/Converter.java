import java.util.Map;

public class Converter {
    private final Map<String, Double> exchangeRates;

    public Converter() {
        // Inicializa las tasas de cambio obtenidas desde la API
        this.exchangeRates = APIHandler.getExchangeRates();
    }

    /**
     * Calcula el monto convertido de una moneda a otra.
     *
     * @param from   La moneda de origen.
     * @param to     La moneda de destino.
     * @param amount La cantidad a convertir.
     * @return El monto convertido.
     */

    public double convert(String from, String to, double amount) {
        // Verifica que las monedas de origen y destino estén en el mapa de tasas de cambio
        if (exchangeRates.containsKey(from) && exchangeRates.containsKey(to)) {
            // Obtiene las tasas de cambio relativas a USD (base)
            double rateFrom = exchangeRates.get(from);
            double rateTo = exchangeRates.get(to);

            // Convierte el monto de la moneda de origen a la moneda de destino
            double amountInUSD = amount / rateFrom;  // Primero lo convertimos a USD
            return amountInUSD * rateTo;             // Luego lo convertimos a la moneda de destino
        } else {
            throw new IllegalArgumentException("Una de las monedas no está disponible.");
        }
    }
}

