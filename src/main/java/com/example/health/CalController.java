package com.example.health;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CalController{
    @FXML
    private Button back_Button;

    @FXML
    private TextField searchFoodTextField;

    @FXML
    private ListView<String> searchedFoodListView;
    @FXML
    private ListView<String> selectedFoodList;
    @FXML
    private Button searchFoodButton;
    String selectedFood;
    @FXML
    Label calLabel;
    @FXML
    Label proteinLabel;
    @FXML
    Label fatLabel;
    @FXML
    Label carbsLabel;
    int todaycalories = 0;
    float todayacccarbs = 0;
    float todayaccfats = 0;
    float todayaccprotiens = 0;
    String id;

    int userHeight;
    int userWeight;
    int userAge;
    //double result = userHeight + userWeight;
    int result = (int) ((88.362 + (13.397 * userWeight) + (4.8 * userHeight) - (userAge * 5.677))*1.2);
    // 1.2 는 활동지수 > 상수값인데 운동 강도에 따라 바뀌는 기능 구현 예정
    int a = todaycalories-result;
    private List<String> searchedFoodList = new ArrayList<>();

    @FXML
    private void initialize() {
        // FXML 파일이 로드될 때 자동으로 호출되는 메서드
        // 초기화 코드 추가 가능// 음식 데이터를 가져와서 초기화
        searchedFoodListView.setItems(FXCollections.observableArrayList());
        id = primary();
        initializeFoodData();
    }
    private void initializeFoodData() {
        try {
            URL url = new URL("http://jkh75622.dothome.co.kr/allfood.php");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            // 연결 설정 및 데이터 읽기
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            // JSON 데이터 파싱
            JSONObject jsonData = new JSONObject(response.toString());
            JSONArray foodArray = jsonData.getJSONArray("data");

            // searchedFoodListView에 데이터 추가
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject foodObject = foodArray.getJSONObject(i);
                String foodmajorcat = foodObject.getString("foodmajorcat");
                searchedFoodList.add(foodmajorcat);
            }

            // searchedFoodListView 초기화
            searchedFoodListView.setItems(FXCollections.observableArrayList(searchedFoodList));

            // 연결 종료
            connection.disconnect();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
    }
    @FXML
    private void searchFoodButtonAction(ActionEvent event) {
        String keyword = searchFoodTextField.getText();
        filterFoodList(keyword);
    }
    private void filterFoodList(String keyword) {
        // 입력값이 없을 경우 모든 음식을 표시
        if (keyword == null || keyword.trim().isEmpty()) {
            searchedFoodListView.setItems(FXCollections.observableArrayList(searchedFoodList));
        } else {
            // 키워드를 포함하는 음식만 필터링
            List<String> filteredList = searchedFoodList.stream()
                    .filter(food -> food.toLowerCase().contains(keyword.toLowerCase()))
                    .collect(Collectors.toList());
            searchedFoodListView.setItems(FXCollections.observableArrayList(filteredList));
        }
    }
    // 완료한 메소드
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
    private void handleFoodItemDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            String selectedFood = searchedFoodListView.getSelectionModel().getSelectedItem();
            if (selectedFood != null && !selectedFood.isEmpty()) {
                // 선택된 음식을 selectedFoodList에 추가
                selectedFoodList.getItems().add(selectedFood);
                System.out.println(selectedFood);

                // DB 연동

                // searchedFoodListView 초기화
                searchedFoodListView.getItems().clear();
            }
        }
    }
    @FXML
    private void handleSelectedFoodDoubleClick(MouseEvent event) {
        if (event.getClickCount() == 2) {
            selectedFood = selectedFoodList.getSelectionModel().getSelectedItem();
            if (selectedFood != null && !selectedFood.isEmpty()) {
                // 선택된 음식을 selectedFoodList에서 제거
                selectedFoodList.getItems().remove(selectedFood);

                // 다른 처리를 수행할 수 있음 (예: 칼로리 갱신, UI 업데이트 등)
            }
        } else if (event.getClickCount() == 1) {
            selectedFood = selectedFoodList.getSelectionModel().getSelectedItem();
            try {
                String serverURL = "http://jkh75622.dothome.co.kr/foodsearch.php";
                URL url = new URL(serverURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);

                String postData = "selectedFood=" + selectedFood;
                OutputStream os = connection.getOutputStream();
                os.write(postData.getBytes("UTF-8"));
                os.close();
                System.out.println(selectedFood + "확인용");
                int responseCode = connection.getResponseCode();


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                    StringBuilder response = new StringBuilder();

                    String inputLine;
                    while ((inputLine = in.readLine()) != null) {
                        response.append(inputLine);
                    }
                    System.out.println("Server Response: " + response.toString());

                    // 데이터 파싱 (구분자로 구분된 문자열을 배열로 분리)
                    String[] data = response.toString().split(",");
                    if (data.length >= 4) {
                        String caloriesStr = data[0];
                        String carbsStr = data[1];
                        String proteinsStr = data[2];
                        String fatsStr = data[3];

                        if (!caloriesStr.isEmpty() && !carbsStr.isEmpty() && !proteinsStr.isEmpty() && !fatsStr.isEmpty()) {
                            int calories = Integer.parseInt(caloriesStr);
                            float carbs = Float.parseFloat(carbsStr);
                            float proteins = Float.parseFloat(proteinsStr);
                            float fats = Float.parseFloat(fatsStr);



                            System.out.println("Calories: " + calories);

                            calLabel.setText(String.valueOf(calories));
                            fatLabel.setText(String.valueOf(fats));
                            proteinLabel.setText(String.valueOf(proteins));
                            carbsLabel.setText(String.valueOf(carbs));
                        } else {
                            System.out.println("Invalid data format");
                        }
                    } else if (response.toString().equals("Data not found")) {
                        System.out.println("Food data not found");
                    } else {
                        System.out.println("Invalid response from the server");
                    }
                } else {
                    System.out.println("Failed to get response from the server");
                }
            }catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void showresultButtonAction(ActionEvent event){
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("showresult")) {

                ObservableList<String> items = selectedFoodList.getItems();
                for (int i = 0 ; i <selectedFoodList.getItems().size() ; i++){
                    selectedFood = items.get(i);
                    try {
                        String serverURL = "http://jkh75622.dothome.co.kr/foodsearch.php";
                        URL url = new URL(serverURL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);

                        String postData = "selectedFood=" + selectedFood;
                        OutputStream os = connection.getOutputStream();
                        os.write(postData.getBytes("UTF-8"));
                        os.close();
                        System.out.println(selectedFood + "확인용");
                        int responseCode = connection.getResponseCode();


                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            StringBuilder response = new StringBuilder();

                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                            System.out.println("Server Response: " + response.toString());

                            // 데이터 파싱 (구분자로 구분된 문자열을 배열로 분리)
                            String[] data = response.toString().split(",");
                            if (data.length >= 4) {
                                String caloriesStr = data[0];
                                String carbsStr = data[1];
                                String proteinsStr = data[2];
                                String fatsStr = data[3];

                                if (!caloriesStr.isEmpty() && !carbsStr.isEmpty() && !proteinsStr.isEmpty() && !fatsStr.isEmpty()) {
                                    int calories = Integer.parseInt(caloriesStr);
                                    float carbs = Float.parseFloat(carbsStr);
                                    float proteins = Float.parseFloat(proteinsStr);
                                    float fats = Float.parseFloat(fatsStr);

                                    todaycalories += calories;
                                    todayacccarbs += carbs;
                                    todayaccprotiens += proteins;
                                    todayaccfats += fats;

                                } else {
                                    System.out.println("Invalid data format");
                                }
                            } else if (response.toString().equals("Data not found")) {
                                System.out.println("Food data not found");
                            } else {
                                System.out.println("Invalid response from the server");
                            }
                        } else {
                            System.out.println("Failed to get response from the server");
                        }
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/initialize.php"; // 서버 URL

                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + id + "&tableName=userPage";

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
                            userHeight = (int) Double.parseDouble(data[0]);
                            userWeight = (int) Double.parseDouble(data[1]);
                            userAge = Integer.parseInt(data[2]);
                            System.out.println("키 : "+userHeight);
                            System.out.println("나이 : "+userAge);
                            System.out.println("몸무게 : "+userWeight);


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
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("cal_result.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();

                    CalPopupController calresultController = loader.getController();

                    result = (int) ((88.362 + (13.397 * userWeight) + (4.8 * userHeight) - (userAge * 5.677))*1.2);
                    a = result - todaycalories;
                    System.out.println("result : "+ result);
                    System.out.println("a : "+ a);
                    calresultController.initData(todaycalories,todayacccarbs,todayaccprotiens,todayaccfats,result,a);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                todaycalories = 0;
                todayacccarbs = 0;
                todayaccfats = 0;
                todayaccprotiens = 0;

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
                    System.out.println("유저 데이터가 존재하지 않습니다.");
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