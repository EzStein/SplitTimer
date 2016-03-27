package xyz.ezstein.fx;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

public class SplitTimerController {
	
	@FXML private Label aLabel;
	public void initialize(){
		System.out.println("INITIALIZED");
	}
	
	@FXML
	private void action(ActionEvent ae){
		aLabel.setText("123");
	}
}
