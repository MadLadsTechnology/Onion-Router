package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class to make api calls
 */
public class APIService {

    /**
     * Method to make GET request
     * @param url url to the wanted host
     * @return the response from the host
     * @throws Exception if a connection could not be established
     */
    public static String apiGETRequest(String url) throws Exception {
        URL urlForGetRequest = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlForGetRequest.openConnection();
        connection.setRequestMethod("GET");

        if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
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

    /**
     * Specific post method to publish a node to the OnionRouterServer
     *
     * @param url the target url
     * @param address the ip and port of the node
     * @throws Exception if the connection cannot be established
     * @return response code from server
     */
    public static int apiPOSTNode(String url,  String address) throws Exception {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return nodeAction(http, "POST", address);

    }

    /**
     * Specific post method to publish a node to the OnionRouterServer
     *
     * @param url the target url
     * @param address the ip and port of the node
     * @throws Exception if the connection cannot be established
     * @return response code from server
     */

    public static int apiDELETENode(String url, String address) throws IOException {
        HttpURLConnection http = (HttpURLConnection) new URL(url).openConnection();
        return nodeAction(http, "DELETE", address);

    }

    private static int nodeAction(HttpURLConnection http, String action, String nodeAddress) throws IOException {

        http.setRequestMethod(action);
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"address\":\"" + nodeAddress + "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        int responseCode = http.getResponseCode();

        http.disconnect();

        return responseCode;
    }
}