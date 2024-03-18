package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
import java.util.Arrays;


public class RoutinePopUpController {
    String[] routineName;
    @FXML
    Button back_button;
    @FXML
    Label routinename1;
    @FXML
    Label routinename2;
    @FXML
    Label routinename3;
    @FXML
    Label routinename4;
    @FXML
    Label routinename5;
    @FXML
    Label routinename6;
    @FXML
    Label routinename7;
    @FXML
    Label routinename8;
    public static String routineLabelText1;
    public static String routineLabelText2;
    public static String routineLabelText3;
    public static String routineLabelText4;
    public static String routineLabelText5;
    public static String routineLabelText6;
    public static String routineLabelText7;
    public static String routineLabelText8;

    public static String transferred_data;

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
            os.write(postData.getBytes("UTF-8"));
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
                    System.out.println("Server Response: " + jsonResponse.toString());
                    if (jsonResponse.has("routineNames")) { // "routineNames" 키가 있는지 확인
                        JSONArray routineArray = jsonResponse.getJSONArray("routineNames");

                        for (int i = 0; i < routineArray.length(); i++) {
                            String routineName = routineArray.getString(i);
                            switch (i + 1) {
                                case 1:
                                    routinename1.setText(routineName);
                                    this.routineLabelText1 = routineName;

                                    break;
                                case 2:
                                    routinename2.setText(routineName);
                                    this.routineLabelText2 = routineName;

                                    break;
                                case 3:
                                    routinename3.setText(routineName);
                                    this.routineLabelText3 = routineName;

                                    break;
                                case 4:
                                    routinename4.setText(routineName);
                                    this.routineLabelText4 = routineName;

                                    break;
                                case 5:
                                    routinename5.setText(routineName);
                                    this.routineLabelText5 = routineName;

                                    break;
                                case 6:
                                    routinename6.setText(routineName);
                                    this.routineLabelText6 = routineName;

                                    break;
                                case 7:
                                    routinename7.setText(routineName);
                                    this.routineLabelText7 = routineName;

                                    break;
                                case 8:
                                    routinename8.setText(routineName);
                                    this.routineLabelText8 = routineName;

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
    private void addToRoutine1(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine1")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText1;
                    System.out.println(routineLabelText1);
                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine2(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine2")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText2;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine3(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine3")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText3;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine4(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine4")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText4;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine5(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine5")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText5;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine6(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine6")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText6;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine7(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine7")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText7;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void addToRoutine8(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("AddToRoutine8")) {
                // RoutineSetController로 이동
                try {
                    transferred_data = routineLabelText8;

                    Stage stage = new Stage();
                    stage.setScene(new Scene(FXMLLoader.load(getClass().getResource("routineSet.fxml"))));
                    stage.show();
                    Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    currentStage.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
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
