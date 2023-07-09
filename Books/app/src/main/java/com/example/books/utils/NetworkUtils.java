package com.example.books.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class NetworkUtils {

    private static final String TAG = "NetworkUtils";
    private static final String POST = "POST";
    private static final String GET = "GET";
    private static final String PUT = "PUT";
    private static final String DELETE = "DELETE";
    private static final String JWT_TOKEN_KEY = "jwtToken";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String AUTHORIZATION_HEADER_PREFIX = "Bearer ";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";
    private static final String JSON_CONTENT_TYPE = "application/json";
    private static final String FORM_MULTIPART = "multipart/form-data;boundary=";
    private static final String LINE_END = "\r\n";
    private static final String TWO_HYPHENS = "--";
    private static final String BOUNDARY = "*****";
    private static final String CONNECTION_HEADER = "Connection";
    private static final String KEEP_ALIVE = "Keep-Alive";
    private static final String ENCTYPE_HEADER = "ENCTYPE";
    private static final String ENCTYPE = "multipart/form-data";
    private static final String UPLOADED_FILE = "uploaded_file";
    private static final String CONTENT_DISPOSITION =
            "Content-Disposition: form-data; name=\"file\";filename=\"";
    private final SessionManager sessionManager;

    public NetworkUtils (Context context) {
        sessionManager = new SessionManager(context.getApplicationContext());
    }

    public String sendPostWithResponseRequest(String urlStr, String data) {
        try {
            // Send request
            HttpURLConnection conn = getConnection(urlStr);
            conn.setRequestMethod(POST);
            writeData(conn, data);

            // Get the server response
            log(urlStr, conn.getResponseCode(), POST);
            return readData(conn);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return "";
        }
    }

    public int sendPostRequest(String urlStr, String data) {
        try {
            // Send request
            HttpURLConnection conn = getConnection(urlStr);
            conn.setRequestMethod(POST);
            writeData(conn, data);

            // Get the server response code
            log(urlStr, conn.getResponseCode(), POST);
            return conn.getResponseCode();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    public String sendGetRequest(String urlStr) {
        try {
            // Send request
            HttpURLConnection conn = getConnection(urlStr);
            conn.setRequestMethod(GET);

            // Get the server response
            log(urlStr, conn.getResponseCode(), GET);
            return readData(conn);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return "";
    }

    public int sendPutRequest(String urlStr, String data) {
        try {
            // Send request
            HttpURLConnection conn = getConnection(urlStr);
            conn.setRequestMethod(PUT);

            writeData(conn, data);

            // Get the server response
            log(urlStr, conn.getResponseCode(), PUT);
            return conn.getResponseCode();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    public int sendDeleteRequest(String urlStr) {
        try {
            // Send request
            HttpURLConnection conn = getConnection(urlStr);
            conn.setRequestMethod(DELETE);

            // Get the server response
            log(urlStr, conn.getResponseCode(), DELETE);
            return conn.getResponseCode();

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    private HttpURLConnection getConnection(String urlStr) throws IOException {
        URL url = new URL(urlStr);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.addRequestProperty(CONTENT_TYPE_HEADER, JSON_CONTENT_TYPE);
        if (sessionManager.isLoggedIn()) {
            String jwtToken = sessionManager.getStringData(JWT_TOKEN_KEY);
            conn.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + jwtToken);
        }
        return conn;
    }

    private void writeData(HttpURLConnection conn, String data) throws IOException {
        conn.setDoOutput(true);
        OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
        wr.write(data);
        wr.flush();
    }

    private String readData(HttpURLConnection conn) throws IOException {
        BufferedReader reader = null;
        reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;

        // Read Server Response
        while ((line = reader.readLine()) != null) {
            // Append server response in string
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }


    public int uploadImage(String fileName, String serverUrl, Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte[] imageBytes =  baos.toByteArray();
        HttpURLConnection conn = null;
        DataOutputStream dos = null;

        try {
            // open a URL connection to the Servlet
            URL url = new URL(serverUrl);

            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod(POST);
            if (sessionManager.isLoggedIn()) {
                String jwtToken = sessionManager.getStringData(JWT_TOKEN_KEY);
                conn.setRequestProperty(AUTHORIZATION_HEADER, AUTHORIZATION_HEADER_PREFIX + jwtToken);
            }
            conn.setRequestProperty(CONNECTION_HEADER, KEEP_ALIVE);
            conn.setRequestProperty(ENCTYPE_HEADER, ENCTYPE);
            conn.setRequestProperty(CONTENT_TYPE_HEADER, FORM_MULTIPART + BOUNDARY);
            conn.setRequestProperty(UPLOADED_FILE, fileName);

            dos = new DataOutputStream(conn.getOutputStream());
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + LINE_END);
            dos.writeBytes(CONTENT_DISPOSITION + fileName + "\"" + LINE_END);
            dos.writeBytes(LINE_END);
            dos.write(imageBytes);
            // send multipart form data necesssary after file data...
            dos.writeBytes(LINE_END);
            dos.writeBytes(TWO_HYPHENS + BOUNDARY + TWO_HYPHENS + LINE_END);
            //close the streams //
            dos.flush();
            dos.close();

            log(serverUrl, conn.getResponseCode(), POST);
            //return response code
            return conn.getResponseCode();
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
            return -1;
        }
    }

    private void log(String url, int code, String method) {
        if (code == 200) {
            Log.i(TAG, code + " " + method + " " + url);
        } else {
            Log.e(TAG, code + " " + method + " " + url);
        }
    }
}
