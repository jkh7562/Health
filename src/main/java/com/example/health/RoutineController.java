package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class RoutineController {
    String[] routineName;
    @FXML
    Label routine_1;
    @FXML
    Label routine_2;
    @FXML
    Label routine_3;
    @FXML
    Label routine_4;
    @FXML
    Label routine_5;
    @FXML
    Label routine_6;
    @FXML
    Label routine_7;
    @FXML
    Label routine_8;

    @FXML
    private void initialize() {
        String userID = primary();
        System.out.println(userID);
        // 사용자 ID를 기반으로 PHP 스크립트를 호출하여 루틴 정보를 가져옵니다.
        try {
            String serverURL = "http://jkh75622.dothome.co.kr/findroutineinformation.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + userID;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder(); // response 초기화

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // 여기서 jsonResponse 초기화
                JSONObject jsonResponse = new JSONObject(response.toString());

                boolean IDfindSuccess = jsonResponse.getBoolean("success");


                if (IDfindSuccess) {
                    System.out.println("Server Response: " + jsonResponse);
                    if (jsonResponse.has("routineNames")) { // "routineNames" 키가 있는지 확인
                        JSONArray routineArray = jsonResponse.getJSONArray("routineNames");

                        for (int i = 0; i < routineArray.length(); i++) {
                            String routineName = routineArray.getString(i);
                            switch (i) {
                                case 0:
                                    routine_1.setText(routineName);
                                    break;
                                case 1:
                                    routine_2.setText(routineName);
                                    break;
                                case 2:
                                    routine_3.setText(routineName);
                                    break;
                                case 3:
                                    routine_4.setText(routineName);
                                    break;
                                case 4:
                                    routine_5.setText(routineName);
                                    break;
                                case 5:
                                    routine_6.setText(routineName);
                                    break;
                                case 6:
                                    routine_7.setText(routineName);
                                    break;
                                case 7:
                                    routine_8.setText(routineName);
                                    break;
                                default:
                                    // 예외 처리
                                    break;
                            }

                            // 여기서 루틴 이름을 배열에 추가하거나 필요에 맞게 처리하세요.
                            // routineName 배열을 초기화한 코드도 추가 필요할 수 있습니다.
                        }
                    } else {
                        System.out.println("No data for key 'routineNames'");
                    }
                } else {
                    // 응답이 없거나 비어있는 경우
                    System.out.println("데이터가 존재하지 않습니다.");
                }
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading data from the server");
        }
        System.out.println(Arrays.toString(routineName));
    }

    @FXML
    private void GotoRoutineDetailButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button clickedButton) {
            String routineName = null;

            // 각 버튼에 따라 routineName 설정
            switch (clickedButton.getId()) {
                case "routinePopUpButton1":
                    routineName = routine_1.getText();
                    break;
                case "routinePopUpButton2":
                    routineName = routine_2.getText();
                    break;
                case "routinePopUpButton3":
                    routineName = routine_3.getText();
                    break;
                case "routinePopUpButton4":
                    routineName = routine_4.getText();
                    break;
                case "routinePopUpButton5":
                    routineName = routine_5.getText();
                    break;
                case "routinePopUpButton6":
                    routineName = routine_6.getText();
                    break;
                case "routinePopUpButton7":
                    routineName = routine_7.getText();
                    break;
                case "routinePopUpButton8":
                    routineName = routine_8.getText();
                    break;
            }

            // routineName이 null이 아니면 상세 정보를 보여주기
            if (routineName != null) {
                // 현재 창 닫기
                Node sourceNode = (Node) event.getSource();
                Stage currentStage = (Stage) sourceNode.getScene().getWindow();
                currentStage.hide();

                // 새로운 FXML 로딩 및 상세 정보 보여주기
                showRoutineDetail(currentStage, routineName);
            }
        }
    }

    private void showRoutineDetail(Stage currentStage, String routineName) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("routine1_8.fxml"));
            Parent root = loader.load();

            Routine1Controller detailController = loader.getController();
            detailController.setRoutineDetailText(routineName);

            // 현재 창 대신에 새로운 창으로 설정
            Stage detailStage = new Stage();
            detailStage.setScene(new Scene(root));

            // 현재 창을 닫음
            currentStage.close();

            // 새 창을 보여줌
            detailStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            // 예외를 적절히 처리하세요 (예: 오류 메시지 표시)
        }
    }
    @FXML
    private void backButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button clickedButton) {
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
            os.write(postData.getBytes(StandardCharsets.UTF_8));
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
