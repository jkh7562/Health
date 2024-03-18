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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Routine1Controller {
    @FXML
    private ListView<String> routineList;

    @FXML
    Label weightLabel;
    @FXML
    Label repsLabel;
    @FXML
    Label setsLabel;
    private String[][] exerciseDetails;
    private ObservableList<String> exerciseNames = FXCollections.observableArrayList();

    @FXML
    private ImageView routine_img;
    @FXML
    Button back_button;
    @FXML
    private TextField routineDetailTextField;

    String routineName;
    String userID;
    @FXML
    public void initialize() {
        userID = primary();

        // 데이터를 추가한 후에 routineList.setItems(exerciseNames); 호출
        setRoutineDetailText("someDefaultRoutineName"); // 예시로 더미 루틴 이름 전달

        // routineList를 초기화하기 전에 데이터를 추가
        routineList.setItems(exerciseNames);

        // 이 메소드가 무조건 화면 전환 될 때 먼저 시작됨
    }

    public void setRoutineDetailText(String text) {
        routineName = text; // 필요한 경우 routineName을 여기서 설정
        routineDetailTextField.setText(text);

        // 여기서는 routineName 이 잘 출력됨
        // initialize 다음으로 이 메서드가 시작됨

        // 여기서 php 연결이 필요함
        try {
            String serverURL = "http://jkh75622.dothome.co.kr/GetRoutineInfo.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + userID + "&routineName=" + routineName;
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            int responseCode = connection.getResponseCode();
            System.out.println(responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                StringBuilder response = new StringBuilder(); // response 초기화

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                String jsonResponseString = response.toString().trim();

                // 문자열을 파이프(|)를 기준으로 나눠서 루틴 정보 추출
                String[] routineInfos = jsonResponseString.split("\\|");

                // exerciseNames 리스트 초기화
                exerciseNames.clear();

                // 운동 루틴 정보를 가져와 exerciseDetails에 저장
                exerciseDetails = new String[routineInfos.length][4];

                int index = 0;

                for (String routineInfo : routineInfos) {
                    // JSON 문자열을 JSONObject로 변환
                    String[] routines = routineInfo.split("\n");
                    // 각 루틴 정보에 대해 반복
                    for (String routine : routines) {
                        // 쉼표를 기준으로 루틴 세부 정보를 나눔
                        String[] details = routine.split(",");
                        if (details.length >= 4) {
                            String exerciseName = details[0].trim(); // '|' 문자 제거
                            String weight = details[1].trim();
                            String reps = details[2].trim();
                            String sets = details[3].trim();
                            sets = sets.substring(0, sets.length() - 2);
                            if (sets.endsWith("\\n")) {
                                // Remove the last two characters
                                sets = sets.substring(0, sets.length() - 2);
                            }

                            System.out.println(sets);
                            // 추출한 값들을 exerciseDetails 배열에 저장
                            exerciseDetails[index][0] = exerciseName;
                            exerciseDetails[index][1] = weight;
                            exerciseDetails[index][2] = reps;
                            exerciseDetails[index][3] = sets;

                            // 운동 이름만 리스트에 추가
                            exerciseNames.add(exerciseName);

                            index++;
                        } else {
                            // 오류 처리 또는 로깅
                            System.out.println("잘못된 형식의 데이터: " + routine);
                        }
                    }
                }

                // routineList에 데이터 설정
                routineList.setItems(exerciseNames);

            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    public void deleteRoutine(ActionEvent actionEvent) {
        // 현재 선택된 항목의 인덱스를 가져옵니다.
        int selectedIndex = routineList.getSelectionModel().getSelectedIndex();

        // 선택된 항목이 유효한지 확인하고 삭제합니다.
        if (selectedIndex >= 0) {
            // 선택된 항목을 삭제합니다.
            routineList.getItems().remove(selectedIndex);

            // 선택된 행을 2차원 배열에서 삭제합니다.
            if (selectedIndex < exerciseDetails.length) {
                // 배열을 복사하여 해당 행을 제외하고 다시 할당합니다.
                exerciseDetails = removeRow(exerciseDetails, selectedIndex);
            }

            // 삭제 후 첫 번째 항목을 선택합니다.
            if (routineList.getItems().size() > 0) {
                routineList.getSelectionModel().select(0);
                selectedIndex = routineList.getSelectionModel().getSelectedIndex();

                // 선택된 값으로 텍스트 필드를 설정합니다.
                String selectedValue = routineList.getItems().get(selectedIndex);

                // 선택된 값에 기반하여 해당 이미지를 로드하고 표시합니다.
                try {
                    String imagePath = "src/main/resources/" + selectedValue + ".gif";
                    FileInputStream fis = new FileInputStream(imagePath);
                    BufferedInputStream bis = new BufferedInputStream(fis);
                    Image img = new Image(bis);
                    routine_img.setImage(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                int lastIndex = routineList.getItems().size() - 1;

                // 선택된 값에 따른 기능 실행
                if (selectedIndex >= 0 && selectedIndex < exerciseDetails.length) {
                    String input = exerciseDetails[selectedIndex][3]; // 세트 정보는 [3]에 있음
                    String result = removeSubstring(input, "\\n");

                    weightLabel.setText(exerciseDetails[selectedIndex][1] + "  kg");
                    repsLabel.setText(exerciseDetails[selectedIndex][2] + "  번");
                    setsLabel.setText(result + "  세트");

                    // 만약 선택된 항목이 리스트뷰의 마지막 요소이면 "마지막 요소입니다" 출력
                    if (selectedIndex == lastIndex) {
                        input = exerciseDetails[selectedIndex][3];
                        String lastresult = removeSubstring(input, "\\n\"}");
                    }
                }
            } else {
                // 모든 항목이 삭제된 경우 텍스트 필드와 라벨 초기화
                routineDetailTextField.setText("");
                weightLabel.setText("");
                repsLabel.setText("");
                setsLabel.setText("");
                routine_img.setImage(null);
            }
        } else {
            System.out.println("삭제할 항목을 선택하세요.");
        }
    }

    // 2차원 배열에서 특정 행을 삭제하는 메서드
    private String[][] removeRow(String[][] array, int rowIndex) {
        int numRows = array.length;
        int numCols = array[0].length;

        // 새로운 배열을 생성하여 특정 행을 제외한 나머지 행을 복사합니다.
        String[][] newArray = new String[numRows - 1][numCols];
        for (int i = 0, destRow = 0; i < numRows; i++) {
            if (i != rowIndex) {
                System.arraycopy(array[i], 0, newArray[destRow++], 0, numCols);
            }
        }

        return newArray;
    }

    @FXML
    public void prevRoutine(ActionEvent actionEvent) {
        // 현재 선택된 항목의 인덱스를 가져옵니다.
        int selectedIndex = routineList.getSelectionModel().getSelectedIndex();

        // 현재 선택된 항목이 첫 번째 항목이 아니면 이전 항목을 선택합니다.
        if (selectedIndex > 0) {
            routineList.getSelectionModel().select(selectedIndex - 1);
            // 선택된 항목의 인덱스를 다시 가져옵니다.
            selectedIndex = routineList.getSelectionModel().getSelectedIndex();

            // 선택된 값으로 텍스트 필드를 설정합니다.
            String selectedValue = routineList.getItems().get(selectedIndex);

            // 선택된 값에 기반하여 해당 이미지를 로드하고 표시합니다.
            try {
                String imagePath = "src/main/resources/" + selectedValue + ".gif";
                FileInputStream fis = new FileInputStream(imagePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Image img = new Image(bis);
                routine_img.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 선택된 값에 따른 기능 실행
            if (selectedIndex >= 0 && selectedIndex < exerciseDetails.length) {
                // exerciseDetails 배열에서 선택된 항목의 정보를 가져옴
                String[] details = exerciseDetails[selectedIndex];

                // 추출한 값들을 사용하여 레이블에 설정
                if (details.length >= 4) {
                    String exerciseName = details[0].trim().replace("|", ""); // '|' 문자 제거
                    String weight = details[1].trim();
                    String reps = details[2].trim();
                    String sets = details[3].trim();

                    weightLabel.setText(weight + "  kg");
                    repsLabel.setText(reps + "  번");
                    setsLabel.setText(sets + "  세트");
                } else {
                    // 오류 처리 또는 로깅
                    System.out.println("잘못된 형식의 데이터: " + Arrays.toString(details));
                }

                // 만약 선택된 항목이 리스트뷰의 마지막 요소이면 "마지막 요소입니다" 출력
                int lastIndex = routineList.getItems().size() - 1;
//                if (selectedIndex == lastIndex) {
//                    String input = exerciseDetails[selectedIndex][3];
//                    String lastresult = removeSubstring(input, "\"}");
//                    setsLabel.setText(lastresult + "  세트");
//                }
            }
        } else {
            System.out.println("첫 번째 항목입니다.");
        }
    }

    @FXML
    public void nextRoutine(ActionEvent actionEvent) {
        // 현재 선택된 항목의 인덱스를 가져옵니다.
        int selectedIndex = routineList.getSelectionModel().getSelectedIndex();

        // 현재 선택된 항목이 마지막 항목이 아니면 다음 항목을 선택합니다.
        if (selectedIndex < routineList.getItems().size() - 1) {
            routineList.getSelectionModel().select(selectedIndex + 1);
            // 선택된 항목의 인덱스를 다시 가져옵니다.
            selectedIndex = routineList.getSelectionModel().getSelectedIndex();

            // 선택된 값으로 텍스트 필드를 설정합니다.
            String selectedValue = routineList.getItems().get(selectedIndex);

            // 선택된 값에 기반하여 해당 이미지를 로드하고 표시합니다.
            try {
                String imagePath = "src/main/resources/" + selectedValue + ".gif";
                FileInputStream fis = new FileInputStream(imagePath);
                BufferedInputStream bis = new BufferedInputStream(fis);
                Image img = new Image(bis);
                routine_img.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
            }

            // 선택된 값에 따른 기능 실행
            if (selectedIndex >= 0 && selectedIndex < exerciseDetails.length) {
                // exerciseDetails 배열에서 선택된 항목의 정보를 가져옴
                String[] details = exerciseDetails[selectedIndex];

                // 추출한 값들을 사용하여 레이블에 설정
                if (details.length >= 4) {
                    String exerciseName = details[0].trim().replace("|", ""); // '|' 문자 제거
                    String weight = details[1].trim();
                    String reps = details[2].trim();
                    String sets = details[3].trim();

                    weightLabel.setText(weight + "  kg");
                    repsLabel.setText(reps + "  번");
                    setsLabel.setText(sets + "  세트");
                } else {
                    // 오류 처리 또는 로깅
                    System.out.println("잘못된 형식의 데이터: " + Arrays.toString(details));
                }

                // 만약 선택된 항목이 리스트뷰의 마지막 요소이면 "마지막 요소입니다" 출력
                int lastIndex = routineList.getItems().size() - 1;
//                if (selectedIndex == lastIndex) {
//                    String input = exerciseDetails[selectedIndex][3];
//                    String lastresult = removeSubstring(input, "\\n\"}");
//                    setsLabel.setText(lastresult + "  세트");
//                }
            }
        } else {
            System.out.println("마지막 항목입니다.");
        }
    }


    @FXML
    private void backButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("back_button")) {
                // 현재 창을 닫고 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routine.fxml"));
                    Parent root = loader.load();

                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
// DB 내용 삭제
                    try {
                        // 서버의 PHP 스크립트 URL로 설정
                        String serverURL = "http://jkh75622.dothome.co.kr/RoutineDelete.php"; // 아이디 확인용 서버 URL
                        // 전송할 파라미터
                        String params = "routineName=" + routineName +
                                "&userID=" + userID;
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
                    String exName = null;
                    String w = null;
                    String r = null;
                    String s = null;
                    // 2차원 배열의 모든 값을 출력
                    for (int i = 0; i < exerciseDetails.length - 1; i++) {
                        System.out.print("|");
                        for (int j = 0; j < exerciseDetails[i].length; j++) {
                            switch (j){
                                case 0:
                                    exName = exerciseDetails[i][j];
                                case 1:
                                    w = exerciseDetails[i][j];
                                case 2:
                                    r = exerciseDetails[i][j];
                                case 3:
                                    s = exerciseDetails[i][j];
                            }
                        }
                        // i 와 j 가 마지막이면 뒤에 3글자 삭제

                        try {
                            // 서버의 PHP 스크립트 URL로 설정
                            String serverURL = "http://jkh75622.dothome.co.kr/SaveRoutine.php"; // 아이디 확인용 서버 URL
                            // 전송할 파라미터
                            String params = "routineName=" + routineName +
                                    "&userID=" + userID +
                                    "&exerciseName=" + exName +
                                    "&exerciseWeight=" + w +
                                    "&exerciseReps=" + r +
                                    "&exerciseSets=" + s;
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
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @FXML
    public void routinecheck(MouseEvent mouseEvent) {
        // ListView에서 선택된 값을 가져옵니다
        String selectedValue = routineList.getSelectionModel().getSelectedItem();

        if (selectedValue != null) {
            // 선택된 값으로 텍스트 필드를 설정합니다 (선택적)
            int selectedIndex = routineList.getSelectionModel().getSelectedIndex();

            // 리스트뷰의 마지막 요소의 인덱스를 계산
            int lastIndex = routineList.getItems().size() - 1;

            // 선택된 값에 따른 기능 실행
            if (selectedIndex >= 0 && selectedIndex < exerciseDetails.length) {
                // exerciseDetails 배열에서 선택된 항목의 정보를 가져옴
                String[] details = exerciseDetails[selectedIndex];

                // 추출한 값들을 사용하여 레이블에 설정
                if (details.length >= 4) {
                    String weight = details[1].trim();
                    String reps = details[2].trim();
                    String sets = details[3].trim();

                    weightLabel.setText(weight + "  kg");
                    repsLabel.setText(reps + "  번");
                    setsLabel.setText(sets + "  세트");
                } else {
                    // 오류 처리 또는 로깅
                    System.out.println("잘못된 형식의 데이터: " + Arrays.toString(details));
                }

                // 만약 선택된 항목이 리스트뷰의 마지막 요소이면 "마지막 요소입니다" 출력
//                if (selectedIndex == lastIndex) {
//                    String input = exerciseDetails[selectedIndex][3];
//                    String lastresult = removeSubstring(input, "\\n\"}");
//                    setsLabel.setText(lastresult + "  세트");
//                }
            } else {
                System.out.println("유효하지 않은 인덱스입니다.");
            }

            // 선택된 값에 기반하여 해당 이미지를 로드하고 표시합니다
            try {
                String imagePath = "src/main/resources/" + selectedValue + ".gif";
                FileInputStream fis = new FileInputStream(imagePath);
                BufferedInputStream bis = new BufferedInputStream(fis);

                Image img = new Image(bis);
                routine_img.setImage(img);
            } catch (Exception e) {
                e.printStackTrace();
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
    private static String removeSubstring(String input, String substring) {
        int index = input.indexOf(substring);
        if (index != -1) {
            return input.substring(0, index) + input.substring(index + substring.length());
        } else {
            return input;
        }
    }


    public void changeRoutineNameButtonAction(ActionEvent event) {
        try {
            String userID = primary();
            String previousRoutineName = routineName;
            String changedRoutineName = routineDetailTextField.getText();

            // PHP script URL
            String serverURL = "http://jkh75622.dothome.co.kr/ChangeRoutineName.php";

            // Create the POST data string
            String postData = URLEncoder.encode("userID", "UTF-8") + "=" + URLEncoder.encode(userID, "UTF-8") +
                    "&" + URLEncoder.encode("previousRoutineName", "UTF-8") + "=" + URLEncoder.encode(previousRoutineName, "UTF-8") +
                    "&" + URLEncoder.encode("changedRoutineName", "UTF-8") + "=" + URLEncoder.encode(changedRoutineName, "UTF-8");

            // Create the connection
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            // Write data to the server
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes("UTF-8"));
            os.close();

            // Get the response from the server
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Process the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }

                // Display the response
                System.out.println(response.toString());
            } else {
                System.out.println("HTTP Error Code: " + responseCode);
            }

            // Close the connection
            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
