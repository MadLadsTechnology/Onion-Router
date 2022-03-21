package API;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

/**
 * Class to make api calls
 */
public class APIService {

    /**
     * Make an empty get request
     * @param url url to the wanted target
     * @return the response from the target
     * @throws Exception if a connection could not be established
     */
    public static String apiGETRequest(String url) throws Exception {
        URL urlForGetRequest = new URL(url);
        return getString(urlForGetRequest);

    }

    /**
     * Specific method to do a get request with one param named payload
     *
     * @param url target url
     * @param payload the value payload
     * @return the response from the target
     * @throws Exception if the connection cannot be established
     */
    public static String apiGETRequestWithPayload(String url, String payload) throws Exception {
        payload = stringRefactoring(payload);
        URL urlForGetRequest = new URL(url + "?payload=" + payload);
        return getString(urlForGetRequest);

    }

    /**
     * Method to make a http get request
     *
     * @param urlForGetRequest the url to the api ypu want to make a get request to
     * @return returns teh response from the given url
     * @throws Exception thrown if a connection could not be established
     */
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

    /**
     * Method to make a string url compatible
     *
     * @param payload the string to be refactored
     * @return the new refactored string
     */
    private static String stringRefactoring(String payload){
        payload = payload.replaceAll("\\+", "%2b" );
        payload = payload.replaceAll("\\\\", "%5c" );
        return payload;
    }

    /**
     * Specific post method to publish a node to the OnionRouterServer
     *
     * @param url the target url
     * @param address the ip and port of the node
     * @throws Exception if the connection cannot be established
     * @return response code
     */
    public static int apiPOSTNode(String url,  String address) throws Exception {

        URL urlPost = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urlPost.openConnection();
        http.setRequestMethod("POST");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"address\":\"" + address + "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);

        OutputStream stream = http.getOutputStream();
        stream.write(out);

        int responseCode = http.getResponseCode();
        http.disconnect();

        return responseCode;

    }

    public static void apiDELETENode(String url, String address) throws IOException {
        URL urlPost = new URL(url);
        HttpURLConnection http = (HttpURLConnection) urlPost.openConnection();
        http.setRequestMethod("DELETE");
        http.setDoOutput(true);
        http.setRequestProperty("Content-Type", "application/json");

        String data = "{  \"address\":\"" + address + "\"}";

        byte[] out = data.getBytes(StandardCharsets.UTF_8);
        System.out.println(address);
        OutputStream stream = http.getOutputStream();
        stream.write(out);
        System.out.println(http.getResponseCode() + " " + http.getResponseMessage());
        http.disconnect();
    }
}