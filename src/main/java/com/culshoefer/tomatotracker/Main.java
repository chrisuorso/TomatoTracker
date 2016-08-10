package com.culshoefer.tomatotracker;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        ClassLoader clss = getClass().getClassLoader();
        URL res = clss.getResource("views/TimerView/TimerView.fxml");
        FXMLLoader loader = new FXMLLoader(res);
        Parent root = loader.load();//FXMLNodeInjecter.getNodeFromLocationAsParent(res);
        PomodoroTimerController ctrl = loader.getController();
        ctrl.setStage(primaryStage);
        primaryStage.setTitle("Pomodoro Timer");
        primaryStage.setScene(new Scene(root, 400, 200));
        primaryStage.setResizable(false);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
