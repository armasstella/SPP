package spp.presentation;


import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                getClass().getResource("/spp/presentation/view/LoginView.fxml")
        );

        Scene scene = new Scene(loader.load());
        stage.setScene(scene);
        stage.setTitle("Inicio");
        stage.setMinWidth(400);
        stage.setMinHeight(480);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);

    }

}