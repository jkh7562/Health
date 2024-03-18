package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.time.LocalDate;

public class CldrController {

    @FXML
    Button back_button;
    @FXML
    Button add_button;

    @FXML
    private Button btnBMonth;

    @FXML
    private Button btnNMonth;

    @FXML
    private Label yearLabel;

    @FXML
    private Label monthLabel;

    @FXML
    private ImageView stamp00, stamp10, stamp20, stamp30, stamp40, stamp50, stamp60, stamp01, stamp11, stamp21, stamp31, stamp41, stamp51, stamp61, stamp02, stamp12, stamp22, stamp32, stamp42, stamp52, stamp62, stamp03, stamp13, stamp23, stamp33, stamp43, stamp53, stamp63, stamp04, stamp14, stamp24, stamp34, stamp44, stamp54, stamp64, stamp05, stamp15, stamp25, stamp35, stamp45, stamp55, stamp65;

    @FXML
    private Label day00, day10, day20, day30, day40, day50, day60, day01, day11, day21, day31, day41, day51, day61, day02, day12, day22, day32, day42, day52, day62, day03, day13, day23, day33, day43, day53, day63, day04, day14, day24, day34, day44, day54, day64, day05, day15, day25, day35, day45, day55, day65;
    @FXML
    private VBox box00, box10, box20, box30, box40, box50, box60, box01, box11, box21, box31, box41, box51, box61, box02, box12, box22, box32, box42, box52, box62, box03, box13, box23, box33, box43, box53, box63, box04, box14, box24, box34, box44, box54, box64, box05, box15, box25, box35, box45, box55, box65;

    private int currentYear;
    private int currentMonth;

    private Label[] dayLabels;
    private VBox[][] boxes;
    ImageView[] stamps;

    private String clickyear, clickmonth, clickday;

    private String Id;
    private GridPane grid;

    @FXML
    private Button[][] dayButtons = new Button[6][7];

    // 다른 필요한 필드 및 메서드들을 추가할 수 있습니다.

    // 예시로 몇 가지 초기화 메서드를 추가합니다.
    @FXML
    private void initialize() {
        Id = primary();

        dayLabels = new Label[]{
                day00, day10, day20, day30, day40, day50, day60,
                day01, day11, day21, day31, day41, day51, day61,
                day02, day12, day22, day32, day42, day52, day62,
                day03, day13, day23, day33, day43, day53, day63,
                day04, day14, day24, day34, day44, day54, day64,
                day05, day15, day25, day35, day45, day55, day65
        };

        stamps = new ImageView[]{
                stamp00, stamp10, stamp20, stamp30, stamp40, stamp50, stamp60,
                stamp01, stamp11, stamp21, stamp31, stamp41, stamp51, stamp61,
                stamp02, stamp12, stamp22, stamp32, stamp42, stamp52, stamp62,
                stamp03, stamp13, stamp23, stamp33, stamp43, stamp53, stamp63,
                stamp04, stamp14, stamp24, stamp34, stamp44, stamp54, stamp64,
                stamp05, stamp15, stamp25, stamp35, stamp45, stamp55, stamp65
        };
        boxes = new VBox[][]{
                {box00, box10, box20, box30, box40, box50, box60},
                {box01, box11, box21, box31, box41, box51, box61},
                {box02, box12, box22, box32, box42, box52, box62},
                {box03, box13, box23, box33, box43, box53, box63},
                {box04, box14, box24, box34, box44, box54, box64},
                {box05, box15, box25, box35, box45, box55, box65}
        };

        setStampVisibility(false, stamps);

        LocalDate localDate = LocalDate.now();
        currentYear = localDate.getYear();
        currentMonth = localDate.getMonthValue();

        updateDayLabels(currentYear, currentMonth);

        yearLabel.setText(Integer.toString(currentYear));
        monthLabel.setText(Integer.toString(currentMonth));

        try {
            String serverURL = "http://jkh75622.dothome.co.kr/stamp.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + Id + "&year=" + currentYear + "&month=" + currentMonth;
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
                    if (jsonResponse.has("days")) { // "day" 키가 있는지 확인
                        JSONArray daysArray = jsonResponse.getJSONArray("days");

                        for (int i = 0; i < daysArray.length(); i++) {
                            int stampday = daysArray.getInt(i);
                            activateStamp(stampday);
                        }
                    }else {
                        System.out.println("No data for key 'days'");
                    }
                    if (jsonResponse.has("day")) {
                        String stampday = jsonResponse.getString("day");

                        if (stampday != null) {
                            // "day" 키가 존재하고, 그 값이 null이 아닌 경우
                            // 여기에서 추가적인 작업 수행
                            activateStamp(Integer.parseInt(stampday));
                        } else {
                            // "day" 키가 존재하지만 값이 null인 경우
                            System.out.println("Value for key 'day' is null");
                        }
                    } else {
                        // "day" 키가 존재하지 않는 경우
                        System.out.println("Key 'day' not found");
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
    private void boxMouseClick(MouseEvent event) {
        // 클릭한 상자의 행 및 열 값을 얻음
        VBox box = (VBox) event.getSource();

        // GridPane의 행과 열 제약 조건을 이용하여 인덱스를 추정
        int rowIndex = GridPane.getRowIndex(box) == null ? 0 : GridPane.getRowIndex(box);
        int colIndex = GridPane.getColumnIndex(box) == null ? 0 : GridPane.getColumnIndex(box);

        Label dayLabel = dayLabels[rowIndex * 7 + colIndex]; // 1차원 배열을 2차원 배열로 변경
        String date = (String) box.getUserData();
        System.out.println("클릭한 상자의 위치: 행 = " + colIndex + ", 열 = " + rowIndex);
        System.out.println("날짜 레이블의 값: " + dayLabel.getText());


        String dayLabelText = dayLabel.getText();
        if (!dayLabelText.isEmpty()) {
            clickday = dayLabel.getText();
            System.out.println("날짜 레이블의 값: " + clickday);
            clickyear = String.valueOf(currentYear);
            clickmonth = String.valueOf(currentMonth);
            String title=null;
            String content=null;
            /*try {
                try {
                    String serverURL = "http://jkh75622.dothome.co.kr/clickcalendar.php";
                    URL url = new URL(serverURL);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("POST");
                    connection.setDoOutput(true);

                    // 데이터 전송
                    String postData = "userID=" + Id + "&year=" + clickyear + "&month=" + clickmonth + "&day=" + clickday;
                    try (OutputStream os = connection.getOutputStream()) {
                        os.write(postData.getBytes("UTF-8"));
                    }

                    int responseCode = connection.getResponseCode();
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // 서버 응답 처리
                        try {
                            // 서버 응답 처리
                            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                            StringBuilder response = new StringBuilder();
                            String inputLine;
                            while ((inputLine = in.readLine()) != null) {
                                response.append(inputLine);
                            }

                            // 응답에서 필요한 데이터 추출 및 처리
                            // 예: JSON 형태의 응답이라 가정
                            String jsonResponseString = response.toString().trim();
                            if (jsonResponseString.startsWith("{") && jsonResponseString.endsWith("}")) {
                                // 응답이 JSON 형식인 경우에만 파싱 수행
                                JSONObject jsonResponse = new JSONObject(jsonResponseString);

                                title = jsonResponse.getString("title");
                                content = jsonResponse.getString("content");
                            } else {
                                System.out.println("Invalid JSON response: " + jsonResponseString);
                            }
                                try {
                                    String serverURL1 = "http://jkh75622.dothome.co.kr/addcalendar.php";
                                    URL url1 = new URL(serverURL1);
                                    HttpURLConnection connection1 = (HttpURLConnection) url1.openConnection();
                                    connection1.setRequestMethod("POST");
                                    connection1.setDoOutput(true);

                                    // 데이터 전송
                                    String postData1 = "title=" + title + "&content=" + content + "&year=" + clickyear + "&month=" + clickmonth + "&day=" + clickday;
                                    try (OutputStream os1 = connection1.getOutputStream()) {
                                        os1.write(postData1.getBytes("UTF-8"));
                                    }

                                    int responseCode1 = connection1.getResponseCode();
                                    if (responseCode1 == HttpURLConnection.HTTP_OK) {
                                        // 서버 응답 처리
                                        try (BufferedReader in1 = new BufferedReader(new InputStreamReader(connection1.getInputStream()))) {
                                            StringBuilder response1 = new StringBuilder();
                                            String inputLine1;
                                            while ((inputLine1 = in1.readLine()) != null) {
                                                response1.append(inputLine1);
                                            }
                                        }
                                    }
                                } catch (Exception e1) {
                                    e1.printStackTrace();
                                }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                } catch (NumberFormatException e) {
                    throw new RuntimeException(e);
                }
            } catch (ProtocolException e) {
                throw new RuntimeException(e);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            } catch (UnsupportedEncodingException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }*/
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("detailCalendar.fxml"));
            Parent root = loader.load();

            // 상세 일정 페이지 컨트롤러에 클릭한 날짜 정보 전달
            DtilCldrController dtilCldrController = loader.getController();
            dtilCldrController.initData(clickyear, clickmonth, clickday);

            // 현재 창을 닫음
            Stage stage = (Stage) box.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }





    private void updateDayLabels(int year, int month) {
        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);

        boolean isLeapYear = firstDayOfMonth.isLeapYear();

        int daysInFebruary = isLeapYear ? 29 : 28;

        int startDayOfWeek = (firstDayOfMonth.getDayOfWeek().getValue()) % 7+1;

        // 해당 월의 일수를 구합니다.
        int daysInMonth;
        switch (month) {
            case 2:
                daysInMonth = daysInFebruary;
                break;
            case 4:
            case 6:
            case 9:
            case 11:
                daysInMonth = 30;
                break;
            default:
                daysInMonth = 31;
        }
// 1주차의 시작 부분을 살펴봅니다.
        int dayIndex = startDayOfWeek - 1; // 0부터 시작하도록 조정

        // 1주차의 시작 부분까지는 빈 칸으로 처리합니다.
        for (int i = 0; i < dayIndex; i++) {
            setDayLabel(dayLabels[i], "");
        }

        // 1주차의 시작 부분부터 해당 월의 일수만큼 날짜 레이블에 숫자를 표시합니다.
        for (int day = 1; day <= daysInMonth; day++) {
            setDayLabel(dayLabels[dayIndex], Integer.toString(day));
            dayIndex++;
        }
    }

    private void setDayLabel(Label label, String text) {
        if (label != null) {
            label.setText(text);
        }
    }

    private void setDayLabelsVisibility(boolean visible, Label[] dayLabels) {
        for (Label label : dayLabels) {
            if (label != null) {
                label.setVisible(visible);
            }
        }
    }

    private void setStampVisibility(boolean visible, ImageView... stamps) {
        for (ImageView stamp : stamps) {
            if (stamp != null) {
                stamp.setVisible(visible);
            }
        }
    }

    private void activateStamp(int day) {
        int index = findIndexForDay(day);
        if (index != -1 && index < stamps.length) {
            stamps[index].setVisible(true);
        }
    }

    private int findIndexForDay(int day) {
        for (int i = 0; i < dayLabels.length; i++) {
            if (dayLabels[i].getText().equals(String.valueOf(day))) {
                return i;
            }
        }
        return -1; // 해당 날짜를 찾지 못한 경우
    }


    @FXML
    private void onPreviousMonthButtonClick(ActionEvent event) {

        // 현재 월을 이전 월로 감소시킴
        currentMonth--;
        if (currentMonth < 1) {
            // 현재 월이 1월보다 작으면 연도를 감소시키고 12월로 설정
            currentMonth = 12;
            currentYear--;
        }

        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }

        // 연도와 월을 기반으로 달력을 갱신
        updateDayLabels(currentYear, currentMonth);

        // 레이블에 연도와 월을 업데이트
        yearLabel.setText(Integer.toString(currentYear));
        monthLabel.setText(Integer.toString(currentMonth));

        setDayLabelsVisibility(true, dayLabels);

        setStampVisibility(false, stamps);
        try {
            String serverURL = "http://jkh75622.dothome.co.kr/stamp.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + Id + "&year=" + currentYear + "&month=" + currentMonth;
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
                    if (jsonResponse.has("days")) { // "day" 키가 있는지 확인
                        JSONArray daysArray = jsonResponse.getJSONArray("days");

                        for (int i = 0; i < daysArray.length(); i++) {
                            int stampday = daysArray.getInt(i);
                            activateStamp(stampday);
                        }
                    }else {
                        System.out.println("No data for key 'days'");
                    }
                    if (jsonResponse.has("day")) {
                        String stampday = jsonResponse.getString("day");

                        if (stampday != null) {
                            // "day" 키가 존재하고, 그 값이 null이 아닌 경우
                            // 여기에서 추가적인 작업 수행
                            activateStamp(Integer.parseInt(stampday));
                        } else {
                            // "day" 키가 존재하지만 값이 null인 경우
                            System.out.println("Value for key 'day' is null");
                        }
                    } else {
                        // "day" 키가 존재하지 않는 경우
                        System.out.println("Key 'day' not found");
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

    }

    @FXML
    private void onNextMonthButtonClick(ActionEvent event) {

        // 현재 월을 다음 월로 증가시킴
        currentMonth++;
        if (currentMonth > 12) {
            // 현재 월이 12월보다 크면 연도를 증가시키고 1월로 설정
            currentMonth = 1;
            currentYear++;
        }

        for (Label label : dayLabels) {
            setDayLabel(label, ""); // 텍스트를 빈 문자열로 설정하여 일자 정보를 없앰
        }

        // 연도와 월을 기반으로 달력을 갱신
        updateDayLabels(currentYear, currentMonth);

        // 레이블에 연도와 월을 업데이트
        yearLabel.setText(Integer.toString(currentYear));
        monthLabel.setText(Integer.toString(currentMonth));

        setDayLabelsVisibility(true, dayLabels);

        setStampVisibility(false, stamps);
        try {
            String serverURL = "http://jkh75622.dothome.co.kr/stamp.php";
            URL url = new URL(serverURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            String postData = "userID=" + Id + "&year=" + currentYear + "&month=" + currentMonth;
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
                    if (jsonResponse.has("days")) { // "day" 키가 있는지 확인
                        JSONArray daysArray = jsonResponse.getJSONArray("days");

                        for (int i = 0; i < daysArray.length(); i++) {
                            int stampday = daysArray.getInt(i);
                            activateStamp(stampday);
                        }
                    }else {
                        System.out.println("No data for key 'days'");
                    }
                    if (jsonResponse.has("day")) {
                        String stampday = jsonResponse.getString("day");

                        if (stampday != null) {
                            // "day" 키가 존재하고, 그 값이 null이 아닌 경우
                            // 여기에서 추가적인 작업 수행
                            activateStamp(Integer.parseInt(stampday));
                        } else {
                            // "day" 키가 존재하지만 값이 null인 경우
                            System.out.println("Value for key 'day' is null");
                        }
                    } else {
                        // "day" 키가 존재하지 않는 경우
                        System.out.println("Key 'day' not found");
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
    private void addButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("add_button")) {
                // 이전 화면을 보여줌 (hello-view.fxml를 사용한 예시)
                LocalDate today = LocalDate.now();
                int year = today.getYear();
                int month = today.getMonthValue();
                int day = today.getDayOfMonth();
                System.out.println(year);
                System.out.println(month);
                System.out.println(day);
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("detailCalendar.fxml"));
                    Parent root = loader.load();
                    DtilCldrController dtilCldrController = loader.getController();
                    dtilCldrController.initData(String.valueOf(year), String.valueOf(month), String.valueOf(day));
                    // 현재 창을 닫음
                    Stage stage = (Stage) clickedButton.getScene().getWindow();
                    stage.setScene(new Scene(root));
                    stage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }}
