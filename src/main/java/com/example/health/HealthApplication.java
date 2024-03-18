package com.example.health;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HealthApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HealthApplication.class.getResource("login.fxml"));

        LoginController loginController = new LoginController();
        fxmlLoader.setController(loginController);

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Health");
        stage.setScene(scene);
        stage.show();

        try {
            String serverURL = "http://jkh75622.dothome.co.kr/delete.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "&tableName=pri";
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                in.close();
                System.out.println("Response: " + response.toString());
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        launch();
    }
}
