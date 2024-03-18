package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainController {
    @FXML
    Button logout_button;
    @FXML
    Button page_button;
    @FXML
    Button exe_button;
    @FXML
    Button cal_button;
    @FXML
    Button daily_button;
    @FXML
    Button rou_button;

    @FXML
    private void rouButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("rou_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routine.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }


    @FXML
    private void exeButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("exe_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("exercise.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void calButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("cal_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("calories.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void dailyButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("daily_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("calendar.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void logoutButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("logout_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
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

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    loader.setController(new LoginController());
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void pageButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("page_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("my_page.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void friendButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("friend_button")) {
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("addfriend.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
