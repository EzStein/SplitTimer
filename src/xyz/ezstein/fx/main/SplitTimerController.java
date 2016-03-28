package xyz.ezstein.fx.main;

import java.io.IOException;

import de.codecentric.centerdevice.*;
import xyz.ezstein.backend.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;

public class SplitTimerController {
	
	@FXML private Label aLabel;
	@FXML private MenuBar mainMenuBar;
	@FXML private Menu applicationMenu;
	
	/**
	 * Called by the FXML loader immediately after this controller object is constructed.
	 */
	public void initialize(){
		if(Locator.isMac())
		{
			mainMenuBar.getMenus().remove(applicationMenu);
			MenuToolkit tk = MenuToolkit.toolkit();
			applicationMenu.getItems().add(1, tk.createHideMenuItem(Locator.appTitle));
			applicationMenu.getItems().add(1, tk.createHideOthersMenuItem());
			applicationMenu.getItems().add(1, tk.createBringAllToFrontItem());
			applicationMenu.getItems().add(1, new SeparatorMenuItem());
			tk.setApplicationMenu(applicationMenu);
		}
		System.out.println("INITIALIZED");
	}
	
	@FXML
	private void startButtonClick(ActionEvent ae){
		
	}
	@FXML
	private void newMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void saveMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void saveAsMenuItemClick(ActionEvent ae){
		
	}
	
	@FXML
	private void optionsMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void startMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void stopMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void pauseMenuItemClick(ActionEvent ae){
		
	}
	
	@FXML
	private void quitMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void preferencesMenuItemClick(ActionEvent ae){
		
	}
	@FXML
	private void aboutMenuItemClick(ActionEvent ae){
		
	}
}
