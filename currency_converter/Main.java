package currency_converter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.json.JSONObject;

public class Main {
    public static void main(String[] args) {
        try(BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            System.out.print("Base currency: ");
            String baseCurrency = br.readLine().toUpperCase();
            System.out.print("Target currency: ");
            String targetCurrency = br.readLine().toUpperCase();

            double amount;
            while (true){
                try {
                    System.out.printf("Enter amount: ");
                    amount = Double.parseDouble(br.readLine());
                    break;
                }catch (NumberFormatException e){
                    System.out.println("Invalid Input!!");
                }
            }

            try {
                double value = currencyExchangeValue(baseCurrency , targetCurrency , amount);
                if (value == -1){
                    System.out.println("Exchange not found.");
                }
                else {
                    System.out.printf("%.2f %s is equivalent to %.2f %s", amount , baseCurrency , value , targetCurrency);
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        }catch (IOException e){
            System.out.println("An error occurred while reading input: " + e.getMessage());
        }
    }

    private static double currencyExchangeValue(String fromCurrency , String toCurrency , double amount) throws IOException, InterruptedException {
        String url = "https://v6.exchangerate-api.com/v6/4b9905874c942e039cb2df79/latest/" + fromCurrency;
        HttpRequest request = HttpRequest.newBuilder(URI.create(url)).GET().build();
        HttpClient client = HttpClient.newHttpClient();
        HttpResponse<String> response = client.send(request , HttpResponse.BodyHandlers.ofString());
//        System.out.println(response.body());

        if (response.statusCode() != 200){
//            System.out.println("Error!! Unable to fetch data. HTTP status : " + response.statusCode());
            return -1;
        }

        JSONObject jsonResponse = new JSONObject(response.body());

        if(!jsonResponse.has("conversion_rates")){
//            System.out.println("Error!! Data not found.");
            return -1;
        }

        JSONObject conversionRates = jsonResponse.getJSONObject("conversion_rates");

        if(!conversionRates.has(toCurrency)){
//            System.out.println("Error!! Data not found.");
            return -1;
        }

        double exchangeRate = conversionRates.getDouble(toCurrency);
        double convertedAmount = amount * exchangeRate;

        return convertedAmount;
    }
}
