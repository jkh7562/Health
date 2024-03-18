package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class CalPopupController {
    int cal;
    int carbs;
    int protiens;
    int fats;
    int result;
    int a;
    @FXML
    Label accCalories;
    @FXML
    Label accFats;
    @FXML
    Label accPortiens;
    @FXML
    Label accCarbs;
    @FXML
    Label rcmdCal;
    @FXML
    Label todayCal;
    @FXML
    Label tomorrowCal;

    public void initData(int todaycalories, float todayacccarbs, float todayaccprotiens, float todayaccfats, int result, int a) {
        cal = todaycalories;
        carbs = (int) todayacccarbs;
        protiens = (int) todayaccprotiens;
        fats = (int) todayaccfats;
        this.result = result;
        this.a = a;
        initialize();
    }
    public void initialize() {

        accCalories.setText(String.valueOf(cal));
        accCarbs.setText(String.valueOf(carbs));
        accPortiens.setText(String.valueOf(protiens));
        accFats.setText(String.valueOf(fats));
        rcmdCal.setText(String.valueOf(result));
        todayCal.setText(String.valueOf(cal));
        tomorrowCal.setText(String.valueOf(a));
    }
    @FXML
    private void closePopup(ActionEvent event) {
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.close();
    }
}