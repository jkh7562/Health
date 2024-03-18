package com.example.health;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public class ArmController {
    @FXML Button putRoutine1;
    @FXML Button putRoutine2;
    @FXML Button putRoutine3;
    @FXML Button putRoutine4;
    @FXML Button putRoutine5;
    @FXML Button putRoutine6;
    @FXML Button putRoutine7;
    @FXML Button putRoutine8;
    @FXML Button putRoutine9;
    @FXML Button putRoutine10;

    @FXML Button back_button;
    static String exerciseName;

    @FXML
    private void putRoutineButtonAction1(ActionEvent event) {
        exerciseName = "Barbell-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine1")) {
                try {
                    System.out.println("Barbell-Curl");
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction2(ActionEvent event) {
        exerciseName = "Concentration-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine2")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction3(ActionEvent event) {
        exerciseName = "Double-Arm-Dumbbell-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine3")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction4(ActionEvent event) {
        exerciseName = "Dumbbell-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine4")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction5(ActionEvent event) {
        exerciseName = "Dumbbell-Preacher-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine5")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction6(ActionEvent event) {
        exerciseName = "Hammer-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine6")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction7(ActionEvent event) {
        exerciseName = "Seated-Zottman-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine7")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void putRoutineButtonAction8(ActionEvent event) {
        exerciseName = "Standing-Barbell-Concentration-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine8")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }

    @FXML
    private void putRoutineButtonAction9(ActionEvent event) {
        exerciseName = "waiter-curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine9")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
                }
            }
        }
    }
    @FXML
    private void putRoutineButtonAction10(ActionEvent event) {
        exerciseName = "Z-Bar-Preacher-Curl";
        if (event.getSource() instanceof Button) {
            Button clickedButton = (Button) event.getSource();
            if (clickedButton.getId().equals("putRoutine10")) {
                try {
                    System.out.println(exerciseName);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("routinePopUp.fxml"));
                    Parent root = loader.load();
                    Scene scene = new Scene(root);

                    // 새로운 Stage를 생성하여 로그인 실패 창을 표시
                    Stage failStage = new Stage();
                    failStage.setScene(scene);
                    failStage.show();


                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (e.g., show an error message)
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
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("exercise.fxml"));
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
