import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class APIHandler {

    private static final String API_URL = "https://v6.exchangerate-api.com/v6/6f0adf5280cc0f6bdf312dcf/latest/USD";
    private static final Gson gson = new Gson();

    public static Map<String, Double> getExchangeRates() {
        try {
            // Conexión HTTP
            HttpURLConnection connection = (HttpURLConnection) new URL(API_URL).openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(5000);

            // Respuesta de la API
            InputStreamReader reader = new InputStreamReader(connection.getInputStream());
            JsonObject responseObject = gson.fromJson(reader, JsonObject.class);

            // Validar respuesta
            if ("success".equals(responseObject.get("result").getAsString())) {
                JsonObject conversionRates = responseObject.getAsJsonObject("conversion_rates");
                Map<String, Double> exchangeRates = new HashMap<>();

                // Transformar datos a mapa
                for (Map.Entry<String, JsonElement> entry : conversionRates.entrySet()) {
                    exchangeRates.put(entry.getKey(), entry.getValue().getAsDouble());
                }
                return exchangeRates;
            } else {
                throw new IllegalStateException("Error al obtener los datos de la API");
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error al realizar la solicitud a la API", e);
        }
    }

    // Obtener las monedas disponibles
    public static String[] getAvailableCurrencies() {
        Map<String, Double> rates = getExchangeRates();
        return rates.keySet().toArray(new String[0]);
    }
}

