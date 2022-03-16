package API;

import nodeManager.Node;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class APIService {

    public static String apiGETRequest(String url) throws Exception {

        URL urlForGetRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            return String.valueOf(response);

        } else {
            throw new Exception("Could not connect");
        }

    }

    public static String apiPOSTRequest(String url) throws Exception {

        URL urlForGetRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String readLine;
            while ((readLine = in.readLine()) != null) {
                response.append(readLine);
            }
            in.close();

            return String.valueOf(response);

        } else {
            throw new Exception("Could not connect");
        }

    }



}
