package xyz.ezstein.fx;

import java.io.IOException;

import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;

public class SplitTimerGUILauncher extends Application {
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("SplitTimer.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		//SplitTimerController controller = loader.getController();
		//controller.initializes();
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
