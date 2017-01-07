package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    public static PrintedStrings PrintedStrings;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("AAAclient.fxml"));
        primaryStage.setTitle("AAA Client");
        primaryStage.setScene(new Scene(root, 359, 465));
        primaryStage.show();
    }


    public static void main(String[] args) {
        PrintedStrings = new PrintedStrings();
        ClientStarter cs = new ClientStarter(args);
        launch(args);
    }
}
