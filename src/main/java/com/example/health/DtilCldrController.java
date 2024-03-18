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

public class DtilCldrController {

    @FXML
    Button back_button;
    @FXML
    TextField title_textfield;
    @FXML
    TextArea content_textarea;
    @FXML
    DatePicker date_picker;
    @FXML
    Button save_button;
    @FXML
    Label del_label;

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
        Id = primary();

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
    private void onSaveButtonClick(ActionEvent event) {
        Id = primary();
        String title = title_textfield.getText();
        String content = content_textarea.getText();
        LocalDate selectedDate = date_picker.getValue();
        String year = String.valueOf(selectedDate.getYear());
        String month = String.valueOf(selectedDate.getMonthValue());
        String day = String.valueOf(selectedDate.getDayOfMonth());

        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/calendar.php/"; // 서버 URL

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 수정 또는 저장할 데이터를 찾기 위한 정보 전송
            String postData = "userID=" + Id + "&year=" + year + "&month=" + month + "&day=" + day;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // 서버 응답 처리
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String jsonResponse = in.readLine();
                in.close();

                if (jsonResponse != null && !jsonResponse.equals("{}")) {
                    System.out.println("helphelphelphelphelphelphelphelphelphelphelphelp");
                    saveData();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("save_cpt.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();
                } else {
                    updateData();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("save_cpt.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();
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

    // 수정된 데이터를 처리하는 메서드
    private void updateData() {
        Id = primary();
        String title = title_textfield.getText();
        String content = content_textarea.getText();
        LocalDate selectedDate = date_picker.getValue();
        String year = String.valueOf(selectedDate.getYear());
        String month = String.valueOf(selectedDate.getMonthValue());
        String day = String.valueOf(selectedDate.getDayOfMonth());
        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/update_calendar.php/"; // 서버 URL

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 수정할 데이터 전송
            String postData = "userID=" + Id + "&title=" + title + "&content=" + content + "&year=" + year + "&month=" + month + "&day=" + day;
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
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 새로운 데이터를 저장하는 메서드
    private void saveData() {
        Id = primary();
        String title = title_textfield.getText();
        String content = content_textarea.getText();
        LocalDate selectedDate = date_picker.getValue();
        String year = String.valueOf(selectedDate.getYear());
        String month = String.valueOf(selectedDate.getMonthValue());
        String day = String.valueOf(selectedDate.getDayOfMonth());
        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/save_calendar.php/"; // 서버 URL

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // 저장할 데이터 전송
            String postData = "userID=" + Id + "&title=" + title + "&content=" + content + "&year=" + year + "&month=" + month + "&day=" + day;
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
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void backButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("back_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
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
    private void onDeleteLabelClick(MouseEvent event) {
        Id = primary();
        LocalDate selectedDate = date_picker.getValue();
        String year = String.valueOf(selectedDate.getYear());
        String month = String.valueOf(selectedDate.getMonthValue());
        String day = String.valueOf(selectedDate.getDayOfMonth());

        try {
            // 서버의 PHP 스크립트 URL로 설정
            String serverURL = "http://jkh75622.dothome.co.kr/deletecalendar.php";

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + Id + "&year=" + year + "&month=" + month + "&day=" + day;
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

                // 삭제 성공 시 메시지 출력 또는 다른 처리 수행
                System.out.println("일정이 삭제되었습니다.");

                FXMLLoader loader = new FXMLLoader(getClass().getResource("calendar.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) date_picker.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }

            connection.disconnect();
        } catch (Exception e) {
            e.printStackTrace();
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
}
