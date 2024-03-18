package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class SaveFail {
    @FXML
    Button okay_button;
    @FXML
    private void okayButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("okay_button")) {
                // 현재 창을 닫음
                Stage currentStage = (Stage) clickedButton.getScene().getWindow();
                currentStage.close();
            }
        }
    }
}
