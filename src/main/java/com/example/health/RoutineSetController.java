package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
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

public class RoutineSetController {
    @FXML
    private TextField setExerciseWeight;

    @FXML
    private TextField setExerciseRaps;

    @FXML
    private TextField setExerciseSets;

    @FXML
    private Button routineSaveButton;
    String userID;
    String routineName;
    String exerciseName;
    @FXML
    private void initialize(){
        routineName =  RoutinePopUpController.transferred_data;
        exerciseName = ArmController.exerciseName;
        System.out.println("가져온 운동 이름은 : "+ exerciseName);

        System.out.println("가져온 루틴 이름은 : "+ routineName);
        userID = primary();
        System.out.println("userID : "+userID);
        // 루틴 이름과 userID가 일치하는 테이블의 행의 routineINFO 에 운동이름 , 무게 , 반복횟수 , 세트수 순으로 입력
    }

    @FXML
    private void routineSaveButtonAction(ActionEvent event) {
        String weight = setExerciseWeight.getText();
        String reps = setExerciseRaps.getText();
        String sets = setExerciseSets.getText();

        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/SaveRoutine.php"; // 아이디 확인용 서버 URL
            // 전송할 파라미터
            String params = "routineName=" + routineName +
                    "&userID=" + userID +
                    "&exerciseName=" + exerciseName +
                    "&exerciseWeight=" + weight +
                    "&exerciseReps=" + reps +
                    "&exerciseSets=" + sets;
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // POST 메서드를 사용하도록 설정
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 파라미터를 바이트로 변환하여 출력 스트림에 쓰기
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = params.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 서버 응답을 읽어옴
                try (BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
                    String response = in.readLine();
                    System.out.println("Server Response: " + response); // 서버 응답 출력
                }
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
        // 여기에 필요한 추가 로직을 수행할 수 있습니다.
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
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
        return primaryid;
    }
}
