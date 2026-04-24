package spp.presentation;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/spp/presentation/view/CoordinatorOptionsView.fxml")
        );
        Scene scene = new Scene(loader.load(), 420, 380);
        stage.setTitle("Registrar Usuario");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}