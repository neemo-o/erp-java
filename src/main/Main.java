package main;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;

//SOS
public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
		 	FXMLLoader loader = new FXMLLoader(getClass().getResource("/main/view/Login.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root);
			primaryStage.centerOnScreen();
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}