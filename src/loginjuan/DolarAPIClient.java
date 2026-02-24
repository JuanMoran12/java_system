package loginjuan;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Cliente para consumir la API del precio del dólar.
 * Implementa buenas prácticas de seguridad para APIs locales.
 */
public class DolarAPIClient {
    
    // Configuración centralizada (puede moverse a un archivo de configuración)
    private static final String API_BASE_URL = "http://127.0.0.1:8000";
    private static final String API_ENDPOINT = "/api/v1/usd/";
    private static final int TIMEOUT_MS = 5000; // 5 segundos
    
    /**
     * Obtiene el precio actualizado del dólar desde la API local.
     * 
     * Medidas de seguridad implementadas:
     * 1. Timeout configurado para evitar bloqueos
     * 2. Validación de respuesta antes de parsear
     * 3. Manejo de excepciones robusto
     * 4. No expone credenciales (API local sin autenticación)
     * 5. Validación del formato de datos recibidos
     * 6. Parseo manual de JSON sin dependencias externas
     * 
     * @return Precio del dólar como String, o mensaje de error
     */
    public static String obtenerPrecioDolar() {
        HttpURLConnection connection = null;
        BufferedReader reader = null;
        
        try {
            // Construir URL completa
            URL url = new URL(API_BASE_URL + API_ENDPOINT);
            
            // Configurar conexión
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(TIMEOUT_MS);
            connection.setReadTimeout(TIMEOUT_MS);
            
            // Headers de seguridad básicos
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("User-Agent", "SistemaGestion/1.0");
            
            // Verificar código de respuesta
            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                return "Error: API no disponible";
            }
            
            // Leer respuesta
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            
            // Parsear JSON manualmente (buscar el campo update_price)
            String jsonResponse = response.toString();
            String precio = extraerCampoJSON(jsonResponse, "update_price");
            
            if (precio != null && !precio.isEmpty()) {
                try {
                    double precioDouble = Double.parseDouble(precio);
                    return String.format("%.2f", precioDouble);
                } catch (NumberFormatException e) {
                    return "Error: Formato inválido";
                }
            } else {
                return "Error: Campo no encontrado";
            }
            
        } catch (java.net.SocketTimeoutException e) {
            return "Timeout";
        } catch (java.net.ConnectException e) {
            return "No disponible";
        } catch (Exception e) {
            System.err.println("Error al obtener precio del dólar: " + e.getMessage());
            return "Error";
        } finally {
            // Cerrar recursos
            try {
                if (reader != null) reader.close();
                if (connection != null) connection.disconnect();
            } catch (Exception e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
    
    /**
     * Extrae el valor de un campo de un JSON simple.
     * Método básico para evitar dependencias externas.
     * 
     * @param json String con el JSON
     * @param campo Nombre del campo a extraer
     * @return Valor del campo o null si no se encuentra
     */
    private static String extraerCampoJSON(String json, String campo) {
        try {
            // Buscar el patrón "campo": valor
            String patron = "\"" + campo + "\"";
            int inicio = json.indexOf(patron);
            
            if (inicio == -1) {
                return null;
            }
            
            // Buscar el inicio del valor (después de los dos puntos)
            int dosPuntos = json.indexOf(":", inicio);
            if (dosPuntos == -1) {
                return null;
            }
            
            // Saltar espacios en blanco
            int inicioValor = dosPuntos + 1;
            while (inicioValor < json.length() && Character.isWhitespace(json.charAt(inicioValor))) {
                inicioValor++;
            }
            
            // Determinar si el valor es string (entre comillas) o número
            boolean esString = json.charAt(inicioValor) == '"';
            if (esString) {
                inicioValor++; // Saltar la comilla inicial
            }
            
            // Buscar el final del valor
            int finValor = inicioValor;
            if (esString) {
                // Buscar la comilla de cierre
                finValor = json.indexOf("\"", inicioValor);
            } else {
                // Buscar coma, llave de cierre o fin de string
                while (finValor < json.length()) {
                    char c = json.charAt(finValor);
                    if (c == ',' || c == '}' || c == ']' || Character.isWhitespace(c)) {
                        break;
                    }
                    finValor++;
                }
            }
            
            if (finValor > inicioValor) {
                return json.substring(inicioValor, finValor).trim();
            }
            
            return null;
        } catch (Exception e) {
            System.err.println("Error al parsear JSON: " + e.getMessage());
            return null;
        }
    }
    
    /**
     * Verifica si la API está disponible.
     * @return true si la API responde, false en caso contrario
     */
    public static boolean verificarDisponibilidad() {
        try {
            URL url = new URL(API_BASE_URL + API_ENDPOINT);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(2000);
            connection.setReadTimeout(2000);
            
            int responseCode = connection.getResponseCode();
            connection.disconnect();
            
            return responseCode == HttpURLConnection.HTTP_OK;
        } catch (Exception e) {
            return false;
        }
    }
}
