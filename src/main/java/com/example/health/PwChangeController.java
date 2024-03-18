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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class PwChangeController {
    @FXML
    Button profile_button;
    @FXML
    Button delaccount_button;
    @FXML
    Button save_button;
    @FXML
    TextField nowpw_textfield;
    @FXML
    TextField chngpw_textfield;
    @FXML
    TextField checkpw_textfield;
    @FXML
    Label pw_label;
    String id;

    @FXML
    private void initialize(){
        id = primary();
    }

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("save_button")) {
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/pwget.php"; // 서버 URL

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String inputnowpw = nowpw_textfield.getText();
                    System.out.println("id:"+id);
                    // POST 데이터 생성 (수정된 부분)
                    String postData = "userID=" + id + "&userPass=" + inputnowpw;

                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 서버 응답 처리
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String response;
                        StringBuilder responseBuilder = new StringBuilder();
                        while ((response = in.readLine()) != null) {
                            responseBuilder.append(response);
                        }
                        in.close();

                        response = responseBuilder.toString();
                        if (response != null && !response.isEmpty() && response.equals("Match found")) {
                            try {
                                String serverURL1 = "http://jkh75622.dothome.co.kr/pwchng.php"; // 서버 URL

                                URL url1 = new URL(serverURL1);
                                HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                connection1.setRequestMethod("POST");
                                connection1.setDoOutput(true);

                                String inputchngpw = chngpw_textfield.getText();
                                String inputcheckpw = checkpw_textfield.getText();

                                if (!inputchngpw.equals(inputcheckpw)) {
                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("save_fail.fxml"));
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);

                                    Stage failStage = new Stage();
                                    failStage.setScene(scene);
                                    failStage.show();
                                    return;
                                }

                                // POST 데이터 생성 (수정된 부분)
                                String postData1 = "userID=" + id + "&userPass=" + inputnowpw + "&chngPass=" + inputchngpw;

                                OutputStream os1 = connection1.getOutputStream();
                                os1.write(postData1.getBytes("UTF-8"));
                                os1.close();

                                int responseCode1 = connection1.getResponseCode();
                                if (responseCode1 == HttpURLConnection.HTTP_OK) {
                                    // 서버 응답 처리
                                    BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()));
                                    String response1;
                                    StringBuilder responseBuilder1 = new StringBuilder();
                                    while ((response1 = in1.readLine()) != null) {
                                        responseBuilder1.append(response1);
                                    }
                                    in1.close();

                                    response1 = responseBuilder1.toString();

                                    System.out.println("Save Response: " + response1);

                                    pw_label.setText(inputchngpw);

                                    FXMLLoader loader = new FXMLLoader(getClass().getResource("save_cpt.fxml"));
                                    Parent root = loader.load();
                                    Scene scene = new Scene(root);

                                    Stage successStage = new Stage();
                                    successStage.setScene(scene);
                                    successStage.show();
                                } else {
                                    // 에러 처리
                                    System.out.println("HTTP Error Code: " + responseCode1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        } else {
                            System.out.println(response);
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("save_fail.fxml"));
                            Parent root = loader.load();
                            Scene scene = new Scene(root);

                            Stage failStage = new Stage();
                            failStage.setScene(scene);
                            failStage.show();
                        }
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }


            }
        }
    }

    public String primary() {
        String primaryid = null;

        try {
            String serverURL = "http://jkh75622.dothome.co.kr/priget.php";
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
                // 서버 응답 처리
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder(); // response 초기화 추가
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // 데이터 파싱 (단순한 문자열을 ,로 분리하여 사용)
                String[] data = response.toString().split(",");
                if (data.length > 0) {
                    primaryid = data[0];
                    System.out.println(primaryid);
                } else {
                    System.out.println("데이터가 존재하지 않습니다.");
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println(primaryid);

        return primaryid;
    }

    @FXML
    private void profileButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("profile_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
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
    private void delaccountButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("delaccount_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("delete_account.fxml"));
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
    private void backButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("back_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("Main.fxml"));
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
