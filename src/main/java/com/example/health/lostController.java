package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class lostController {

    @FXML TextField name_textfield;
    @FXML TextField phone_textfield;
    @FXML Button id_button;
    @FXML Button back_button;
    @FXML Label id_label;

    @FXML
    public void initialize() {
        back_button.setDefaultButton(true);
    }

    private void handleEscKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            backButtonAction(new ActionEvent());
        }
    }


    @FXML
    private void idButtonAction(ActionEvent event) {
        if(event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("id_button")) {
                String userName = name_textfield.getText();
                String phoneNumber = phone_textfield.getText();

                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://jkh75622.dothome.co.kr/IdFinder.php";

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userName=" + userName + "&phoneNumber=" + phoneNumber;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {

                        // 서버 응답 처리
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String inputLine;

                        while ((inputLine = in.readLine()) != null) {
                            response.append(inputLine);
                        }

                        System.out.println("Server Response: " + response.toString());

                        // JSON 파싱
                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean IDfindSuccess = jsonResponse.getBoolean("success");

                        if (IDfindSuccess) {
                            // 데이터의 id 값을 얻어옴
                            String userID = jsonResponse.getString("userID");

                            // id_label의 text 값을 userId 값으로 설정
                            id_label.setText("사용자 ID: " + userID);

                            // 여기에서 userId 값을 사용하여 필요한 작업 수행
                            System.out.println("사용자 ID: " + userID);
                        } else {
                            id_label.setText("***값을 잘못 입력하셨습니다.***");
                            // 응답이 없거나 비어있는 경우
                            System.out.println("데이터가 존재하지 않습니다.");
                        }
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("back_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
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
}
