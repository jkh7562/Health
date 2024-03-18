package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class SignCpt {
    @FXML
    private Button okay_button;

    @FXML
    public void okayButtonAction(ActionEvent event) {
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("okay_button")) {
                Stage currentStage = (Stage) clickedButton.getScene().getWindow();
                Stage[] openStages = Stage.getWindows().toArray(new Stage[0]);
                for (Stage stage : openStages) {
                    if (stage != currentStage) {
                        stage.close();
                    }
                }
                Stage currentStage1 = (Stage) clickedButton.getScene().getWindow();
                currentStage1.close();

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("login.fxml"));
                    loader.setController(new LoginController());
                    Parent root = loader.load();

                    // 새로운 창을 열음
                    Stage newStage = new Stage();
                    newStage.setScene(new Scene(root));
                    newStage.show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
