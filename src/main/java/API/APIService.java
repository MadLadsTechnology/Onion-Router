package API;

import nodeManager.Node;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.regex.Pattern;

public class APIService {

    public static String apiGETRequest(String url) throws Exception {
        URL urlForGetRequest = new URL(url);
        return getString(urlForGetRequest);

    }
    public static String apiGETRequestWithPayload(String url, String payload) throws Exception {
        payload = stringRefactoring(payload);
        URL urlForGetRequest = new URL(url + "?payload=" + payload);
        return getString(urlForGetRequest);

    }

    private static String getString(URL urlForGetRequest) throws Exception {
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

    private static String stringRefactoring(String payload){
        payload = payload.replaceAll("\\+", "%2b" );
        payload = payload.replaceAll("\\\\", "%5c" );
        return payload;
    }

    public static void apiPOSTNode(String url, String publicKey, String address) throws Exception {

        URL urlPost = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urlPost.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{ \"publicKey\":\""  + publicKey +"\", \"address\":\"" + address + "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }



}
