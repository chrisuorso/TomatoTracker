package com.culshoefer.tomatotracker;

import com.airhacks.afterburner.injection.Injector;
import com.culshoefer.tomatotracker.views.mainscreen.TimerView;

import java.util.HashMap;
import java.util.Map;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Map<Object, Object> customProperties = new HashMap<>();
        Injector.setConfigurationSource(customProperties::get);

        TimerView appView = new TimerView();
        Scene scene = new Scene(appView.getView());
        primaryStage.setTitle("TomatoTracker");
        final String stageCss = getClass().getResource("Timer.css").toExternalForm();
        scene.getStylesheets().add(stageCss);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        Injector.forgetAll();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
