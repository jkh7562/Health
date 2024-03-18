package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
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
import java.util.ResourceBundle;

public class AddfriendController implements Initializable {
    @FXML
    TextField search_textfield;
    @FXML
    Button search_button;
    @FXML
    Button add_button;
    @FXML
    Button del_button;
    @FXML
    Button view_button;
    @FXML
    ListView friend_list;
    @FXML
    ListView search_list;

    String friend_id;
    String friend_name;
    String fid;
    String fname;
    String id;
    String delId;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        id = primary();
        try{
            String serverURL = "http://jkh75622.dothome.co.kr/initfriend.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + id;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
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
                boolean searchSuccess = jsonResponse.getBoolean("success");

                if (searchSuccess) {
                    System.out.println("검색 성공");

                    JSONArray userDataArray = jsonResponse.getJSONArray("userData");

                    for (int i = 0; i < userDataArray.length(); i++) {
                        JSONObject userData = userDataArray.getJSONObject(i);
                        friend_id = userData.getString("friendID");
                        friend_name = userData.getString("friendName");

                        String displayText = friend_name + " - " + friend_id;
                        friend_list.getItems().add(displayText);
                    }
                } else {
                    System.out.println("검색 실패");
                }

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void searchButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("search_button")) {
                String searchid = search_textfield.getText();
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/searchfriend.php";
                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    String postData = "userID=" + searchid;
                    OutputStream os = connection.getOutputStream();
                    os.write(postData.getBytes(StandardCharsets.UTF_8));
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
                        boolean searchSuccess = jsonResponse.getBoolean("success");

                        if (searchSuccess) {
                            System.out.println("검색 성공");

                            JSONObject userData = jsonResponse.getJSONObject("userData");
                            friend_id = userData.getString("userID");
                            friend_name = userData.getString("userName");

                            String displayText = friend_name + " - " + friend_id;
                            search_list.getItems().clear();
                            search_list.getItems().add(displayText);
                            fname = friend_name;
                            fid = friend_id;
                        } else {
                            System.out.println("검색 실패");
                        }

                    } else {
                        System.out.println("HTTP Error Code: " + responseCode);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    private void addButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("add_button")) {
                String selectedFriend = (String) search_list.getSelectionModel().getSelectedItem();

                if (selectedFriend != null && !friend_list.getItems().contains(selectedFriend) && !id.equals(fid)) {
                    // friend_list에 선택된 항목 추가
                    friend_list.getItems().add(selectedFriend);
                    try{
                        String serverURL = "http://jkh75622.dothome.co.kr/addfriend.php";
                        URL url = new URL(serverURL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);

                        String postData = "userID=" + id + "&friendName=" + fname + "&friendID=" + fid;
                        OutputStream os = connection.getOutputStream();
                        os.write(postData.getBytes(StandardCharsets.UTF_8));
                        os.close();

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                        } else {
                            System.out.println("HTTP Error Code: " + responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @FXML
    private void delButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("del_button")) {
                String selectedFriend = (String) friend_list.getSelectionModel().getSelectedItem();


                if (selectedFriend != null) {

                    String[] parts = selectedFriend.split(" - ");


                    if (parts.length > 1) {
                        delId = parts[1];
                        System.out.println("아이디: " + delId);

                        // friend_list에서 선택된 항목 삭제
                        friend_list.getItems().remove(selectedFriend);

                        // DB에서도 해당 항목 삭제
                        deleteFriendFromDB(delId);
                    }
                }
            }
        }
    }

    private void deleteFriendFromDB(String delId) {
        try {
            String serverURL = "http://jkh75622.dothome.co.kr/delfriend.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            System.out.println(id);
            System.out.println(delId);
            String postData = "userID=" + id + "&friendID=" + delId;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes(StandardCharsets.UTF_8));
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
                boolean deleteSuccess = jsonResponse.getBoolean("success");

                if (deleteSuccess) {
                    System.out.println("삭제 성공");
                } else {
                    System.out.println("삭제 실패" + jsonResponse.getString("message"));
                }

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
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

    @FXML
    private void viewButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("view_button")) {
                String selectedFriend = (String) friend_list.getSelectionModel().getSelectedItem();

                if (selectedFriend != null) {

                    String[] parts = selectedFriend.split(" - ");

                    // 아이디 추출
                    if (parts.length > 1) { // 변경: parts.length > 0에서 parts.length > 1로 변경
                        delId = parts[1]; // 변경: delName 대신 delId 사용
                        System.out.println("아이디: " + delId); // 변경: "이름" 대신 "아이디"로 출력
                    }

                    try{
                        String serverURL = "http://jkh75622.dothome.co.kr/prifriend.php";
                        URL url = new URL(serverURL);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setDoOutput(true);

                        String postData = "friendID=" + delId;
                        OutputStream os = connection.getOutputStream();
                        os.write(postData.getBytes(StandardCharsets.UTF_8));
                        os.close();

                        int responseCode = connection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            StringBuilder response = new StringBuilder();
                            String inputLine;

                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }
                        } else {
                            System.out.println("HTTP Error Code: " + responseCode);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
