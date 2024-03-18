package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class sign_upColtroller {
    @FXML TextField name_textfield;
    @FXML TextField phone_textfield;
    @FXML TextField id_textfield;
    @FXML TextField pass_textfield;
    @FXML TextField repass_textfield;
    @FXML Button signup_button;
    @FXML Button close_button;

    String userID;
    @FXML
    private void signupButtonAction(ActionEvent event){
        if(event.getSource() instanceof Button){
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("signup_button")){
                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://jkh75622.dothome.co.kr/Register.php/"; // 서버 URL

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // 사용자 정보를 서버로 보내기
                    userID = id_textfield.getText();

                    // 추가: 이미 존재하는 아이디인지 확인
                    if (isUserExist(userID)) {
                        System.out.println("이미 존재하는 아이디입니다.");

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("signupfail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                        return;
                    }else{
                        System.out.println("생성 가능한 아이디입니다.");
                    }



                    // 사용자 정보를 서버로 보내기
                    String userpass = pass_textfield.getText();
                    String userrePass = repass_textfield.getText();
                    String userName = name_textfield.getText();
                    String phoneNumber = phone_textfield.getText();
                    String pass;

                    if(userpass.equals(userrePass)){
                        pass = userpass;
                    } else {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("save_fail.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                        return;
                    }


                    String postData = "userID=" + userID + "&userPass=" + pass + "&userName=" + userName + "&phoneNumber=" + phoneNumber;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 서버 응답 처리
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String response;
                        while ((response = in.readLine()) != null) {
                            System.out.println(response);
                        }
                        in.close();

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("sign_cpt.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

                    connection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    // 서버의 PHP 스크립트 URL로 설정
                    String serverURL = "http://jkh75622.dothome.co.kr/give8routine.php"; // 아이디 확인용 서버 URL
                    System.out.println("userID : " + userID);
                    String params = "userID=" + userID; // 전달할 파라미터

                    URL url = new URL(serverURL + "?" + params);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 서버 응답을 읽어옴
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String response = in.readLine();
                        in.close();

                        System.out.println("Server Response: " + response); // 서버 응답 출력
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // 이곳에 루틴 8개 넣어주는 php
                // userID 주면
                // 회원가입 완료
                // 루틴 8개 이름 루틴 1 ,2,3,4, ... 주자
            }
        }
    }

    private boolean isUserExist(String userID) {
        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/CheckUser.php"; // 아이디 확인용 서버 URL
            String params = "userID=" + userID; // 전달할 파라미터

            URL url = new URL(serverURL + "?" + params);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 서버 응답을 읽어옴
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String response = in.readLine();
                in.close();

                // 서버에서 "true" 문자열을 반환하면 이미 존재하는 아이디
                return "true".equals(response.trim());
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    @FXML
    private void closeButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("close_button")) {
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
