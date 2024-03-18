package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class lostpassController {
    @FXML TextField id_textfield;
    @FXML TextField phone_textfield;
    @FXML Button pass_button;
    @FXML Button back_button;
    @FXML Label pass_label;

    @FXML
    private void passButtonAction(ActionEvent event) {
        if(event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("pass_button")) {
                String userID = id_textfield.getText();
                String phoneNumber = phone_textfield.getText();

                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://jkh75622.dothome.co.kr/PassFinder.php";

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + userID + "&phoneNumber=" + phoneNumber;
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
                            // 데이터의 pass 값을 얻어옴
                            String userPass = jsonResponse.getString("userPass");

                            // id_label의 text 값을 userId 값으로 설정
                            pass_label.setText("사용자 Password: " + userPass);

                            // 여기에서 userId 값을 사용하여 필요한 작업 수행
                            System.out.println("사용자 password: " + userPass);
                        } else {
                            pass_label.setText("***값을 잘못 입력하셨습니다.***");
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
