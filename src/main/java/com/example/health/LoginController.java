package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;

public class LoginController { //로그인 컨트롤러
    @FXML private Button signup_button;
    @FXML private Button login_button;
    @FXML private TextField id_textfield;
    @FXML private PasswordField pass_textfield;
    @FXML private Label lostid_label;
    @FXML private Label lostpass_label;
    public String primary_id;

    @FXML
    private void initialize() {
        // login_button에 대한 엔터 키 핸들러 등록
        login_button.setDefaultButton(true);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            // 엔터 키를 눌렀을 때 로그인 버튼 핸들러 호출
            handleLoginButtonAction(new ActionEvent());
        }
    }

    @FXML
    private void signupButtonAction(ActionEvent event){
        if(event.getSource() instanceof Button){
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("signup_button")){
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("sign_up.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) signup_button.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void lostidlabelAction(MouseEvent event) {
        if (event.getSource() instanceof Label) {
            Label clickedLabel = (Label) event.getSource();
            if (clickedLabel.getId().equals("lostid_label")) {
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("lost.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedLabel.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void lostpasslabelAction(MouseEvent event) {
        if (event.getSource() instanceof Label) {
            Label clickedLabel = (Label) event.getSource();
            if (clickedLabel.getId().equals("lostpass_label")) {
                try {
                    // FXML 파일을 로드합니다.
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("lostpass.fxml"));
                    Parent root = loader.load();

                    // 현재 Stage를 가져옵니다.
                    Stage currentStage = (Stage) clickedLabel.getScene().getWindow();

                    // 현재 Stage의 Scene을 새로운 Scene으로 교체합니다.
                    currentStage.setScene(new Scene(root));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    @FXML
    private void handleLoginButtonAction(ActionEvent event) {
        String userID = id_textfield.getText();
        String userPass = pass_textfield.getText();
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("login_button")) {
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/Login.php";
                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + userID + "&userPass=" + userPass;
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

                        System.out.println("Server Response: " + response.toString());

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        boolean loginSuccess = jsonResponse.getBoolean("success");

                        if (loginSuccess) {
                            System.out.println("로그인 성공");

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
                            Parent root = loader.load();

                            // 현재 창을 닫음
                            Stage stage = (Stage) clickedButton.getScene().getWindow();
                            stage.setScene(new Scene(root));
                            stage.show();

                            connection.disconnect();

                            try{

                                String serverURL1 = "http://jkh75622.dothome.co.kr/primary.php"; // 서버 URL

                                URL url1 = new URL(serverURL1);
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.setRequestMethod("POST");
                                connection1.setDoOutput(true);

                                String postData1 = "priid=" + userID + "&tableName = pri";
                                OutputStream os1 = connection1.getOutputStream();
                                os1.write(postData1.getBytes("UTF-8"));
                                os1.close();

                                int responseCode1 = connection1.getResponseCode();
                                if (responseCode1 == HttpURLConnection.HTTP_OK) {
                                    // 서버 응답 처리
                                    BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                                    String response1;
                                    while ((response1 = in1.readLine()) != null) {
                                        System.out.println(response1);
                                    }
                                    in1.close();

                                }else {
                                    System.out.println("HTTP Error Code: " + responseCode1);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            System.out.println("로그인 실패");

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("loginfail.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root);

                            // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                            Stage failStage = new Stage();
                            failStage.setScene(scene);
                            failStage.show();
                        }

                    } else {
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String maskedPassword = getMaskedPassword(pass_textfield.getText());
                System.out.println("Entered Password: " + maskedPassword);
            }
        }
    }
    private String getMaskedPassword(String password) {
        // 비밀번호를 '*'로 표시하는 메서드
        StringBuilder maskedPassword = new StringBuilder();
        for (int i = 0; i < password.length(); i++) {
            maskedPassword.append('*');
        }
        return maskedPassword.toString();
    }
}