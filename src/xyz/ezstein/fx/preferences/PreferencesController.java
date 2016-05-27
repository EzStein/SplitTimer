package xyz.ezstein.fx.preferences;


import java.util.*;

import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.stage.*;
import xyz.ezstein.backend.util.Util;

public class PreferencesController {
	
	private Stage stage;
	@FXML private TextField startTextField;
	@FXML private TextField pauseTextField;
	@FXML private TextField stopTextField;
	@FXML private TextField splitTextField;
	@FXML private TextField backgroundTextField;
	@FXML private TextField delayTextField;
	
	public void initializeAsGUI(Stage stage){
		this.stage=stage;
		Properties props = Util.getProperties();
		startTextField.setText(props.getProperty("start"));
		stopTextField.setText(props.getProperty("stop"));
		splitTextField.setText(props.getProperty("split"));
		pauseTextField.setText(props.getProperty("pause"));
		
		startTextField.setOnKeyReleased(ke->{
			startTextField.setText(ke.getCode().toString());
		});
		pauseTextField.setOnKeyReleased(ke->{
			pauseTextField.setText(ke.getCode().toString());
		});
		splitTextField.setOnKeyReleased(ke->{
			splitTextField.setText(ke.getCode().toString());
		});
		stopTextField.setOnKeyReleased(ke->{
			stopTextField.setText(ke.getCode().toString());
		});
		
	}
	
	@FXML
	public void okButtonClick(ActionEvent ae){
		HashMap<String,String> props = new HashMap<String,String>();
		props.put("start", startTextField.getText());
		props.put("stop", stopTextField.getText());
		props.put("split", splitTextField.getText());
		props.put("pause", pauseTextField.getText());
		Util.setProperties(props);
		stage.close();
	}
	@FXML
	public void cancelButtonClick(ActionEvent ae){
		stage.close();
	}
}
