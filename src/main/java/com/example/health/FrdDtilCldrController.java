package com.example.health;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

public class FrdDtilCldrController {

    @FXML
    Button back_button;
    @FXML
    TextField title_textfield;
    @FXML
    TextArea content_textarea;
    @FXML
    DatePicker date_picker;

    private String Id;

    private int year,month,day;
    private String setyear, setmonth, setday;

    public void initData(String clickyear, String clickmonth, String clickday) {
        try {
            // String으로 받은 년, 월, 일을 정수로 변환
            year = Integer.parseInt(clickyear);
            month = Integer.parseInt(clickmonth);
            day = Integer.parseInt(clickday);

            // DatePicker를 초기화
            date_picker.setValue(LocalDate.of(year, month, day));

            setyear = String.valueOf(year);
            setmonth = String.valueOf(month);
            setday = String.valueOf(day);
            initialize();
        } catch (NumberFormatException e) {
            // 숫자로 변환 중 예외 발생 시 처리
            e.printStackTrace();
            // 또는 다른 적절한 예외 처리를 수행
        }
    }

    @FXML
    private void initialize() {
        Id = friendprimary();

        if(setyear!=null){
            try {
                // 서버의 PHP 스크립트 URL로 설정
                String serverURL = "http://jkh75622.dothome.co.kr/getcalendar.php";

                URL url = new URL(serverURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postData = "userID=" + Id + "&year=" + setyear + "&month=" + setmonth + "&day=" + setday;
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
                        String title = jsonResponse.getString("title");
                        String content = jsonResponse.getString("content");

                        // UI 업데이트는 Platform.runLater 블록 안에서 수행
                        Platform.runLater(() -> {
                            title_textfield.setText(title);
                            content_textarea.setText(content);
                        });
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("friendcalendar.fxml"));
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


    public String friendprimary() {
        String friendprimaryid = null;

        try {
            String serverURL = "http://jkh75622.dothome.co.kr/friendpriget.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "&tableName=prifriend";
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
                    friendprimaryid = data[0];
                    System.out.println(friendprimaryid);
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

        System.out.println(friendprimaryid);

        return friendprimaryid;
    }
}
