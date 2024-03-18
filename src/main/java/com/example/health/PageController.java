package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

public class PageController implements Initializable {
    @FXML
    Button back_button;
    @FXML
    Button pw_button;
    @FXML
    Button delaccount_button;
    @FXML
    TextField name_textfield;
    @FXML
    TextField height_textfield;
    @FXML
    TextField weight_textfield;
    @FXML
    TextField age_textfield;
    @FXML
    Label name_label;
    @FXML
    Label height_label;
    @FXML
    Label weight_label;
    @FXML
    Label age_label;
    @FXML
    Button save_button;

    private String Id;
    private StringBuilder response = new StringBuilder(); // response를 초기화

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Id = primary();

        try {
            String serverURL = "http://jkh75622.dothome.co.kr/initialize.php"; // 서버 URL

            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + Id + "&tableName=userPage";

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

                // 데이터 파싱 (단순한 문자열을 ,로 분리하여 사용)
                String[] data = response.toString().split(",");
                if (data.length >= 3) {
                    double userHeight = Double.parseDouble(data[0]);
                    double userWeight = Double.parseDouble(data[1]);
                    String userAge = data[2];

                    height_label.setText(Double.toString(userHeight));
                    weight_label.setText(Double.toString(userWeight));
                    age_label.setText(userAge);
                } else {
                    System.out.println("데이터가 존재하지 않습니다.");
                }

                in.close();
            } else {
                // 에러 처리
                System.out.println("HTTP Error Code: " + responseCode);
            }

            connection.disconnect();
        } catch (IOException e) {
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

    @FXML
    private void saveButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("save_button")) {
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/save.php"; // 서버 URL

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    double inputheight = Double.parseDouble(height_textfield.getText());
                    double inputweight = Double.parseDouble(weight_textfield.getText());
                    String inputage = age_textfield.getText();

                    // POST 데이터 생성 (수정된 부분)
                    String postData = "userID=" + Id +
                            "&userHeight=" + inputheight +
                            "&userWeight=" + inputweight +
                            "&userAge=" + inputage +
                            "&tableName=userPage";

                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes("UTF-8"));
                    os.close();

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 서버 응답 처리
                        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        String response;
                        while ((response = in.readLine()) != null) {
                            System.out.println("Save Response: " + response);
                        }
                        in.close();

                        // initialize 메서드 호출
                        initialize(null, null);

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("save_cpt.fxml"));
                        Parent root = loader.load();
                        Scene scene = new Scene(root);

                        Stage failStage = new Stage();
                        failStage.setScene(scene);
                        failStage.show();
                    } else {
                        // 에러 처리
                        System.out.println("HTTP Error Code: " + responseCode);
                    }

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
    @FXML
    private void pwButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("pw_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("pw_change.fxml"));
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
}