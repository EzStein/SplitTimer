package xyz.ezstein.fx.main;

import java.io.*;
import java.util.concurrent.*;

import de.codecentric.centerdevice.*;
import xyz.ezstein.backend.app.*;
import xyz.ezstein.fx.options.OptionsController;
import xyz.ezstein.backend.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.collections.FXCollections;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.scene.control.cell.*;
import javafx.stage.*;
import javafx.util.*;

public class SplitTimerController {
	
	private Stage stage;
	private Scene scene;
	private Updater updater;
	private SplitCollection currentSplitCollection;
	@FXML private Label timeLabel;
	@FXML private MenuBar mainMenuBar;
	@FXML private Menu applicationMenu;
	@FXML private Button startButton;
	@FXML private TableView<SplitEvent> splitEventTable;
	@FXML private TableColumn<SplitEvent, Number> timeTableColumn;
	@FXML private TableColumn<SplitEvent, String> iconTableColumn;
	@FXML private TableColumn<SplitEvent, Number> splitTimeTableColumn;
	@FXML private TableColumn<SplitEvent, String> nameTableColumn;
	
	/**
	 * Called by the FXML loader immediately after this controller object is constructed.
	 */
	public void initialize(){
		
		System.out.println("INITIALIZED");
	}
	
	public void initializeAsGUI(Stage stage, Scene scene){
		this.stage=stage;
		this.scene=scene;
		stage.setOnCloseRequest(e->{
			e.consume();
			close(e);
		});
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
		
		timeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				// TODO Auto-generated method stub
				return new SimpleLongProperty(0);
			}
		});
		
		
		splitTimeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				// TODO Auto-generated method stub
				return new SimpleLongProperty(0);
			}
		});
		nameTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<SplitEvent, String> splitEvent) {
				// TODO Auto-generated method stub
				return splitEvent.getValue().nameProperty();
			}
		});
		
		
		iconTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<SplitEvent, String> splitEvent) {
				// TODO Auto-generated method stub
				return splitEvent.getValue().iconProperty();
			}
		});
	}
	
	private class Updater implements Runnable {
		private long startTime;
		private long currentTime;
		private boolean close;
		
		public Updater(){
			this.close=false;
			startTime=0;
			currentTime=0;
		}
		
		@Override
		public void run() {
			startTime=System.nanoTime();
			while(!close){
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentTime=System.nanoTime();
				long elapsed = currentTime-startTime;
				updateUITime(elapsed);
			}
		}
		
		public void close(){
			close=true;
		}
	}
	
	public void updateUITime(long elapsedTime){
		Platform.runLater(()->{
			long hours = TimeUnit.NANOSECONDS.toHours(elapsedTime);
			long minutes = TimeUnit.NANOSECONDS.toMinutes(elapsedTime)-TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(elapsedTime));
			long seconds = TimeUnit.NANOSECONDS.toSeconds(elapsedTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(elapsedTime));
			long milliseconds = TimeUnit.NANOSECONDS.toMillis(elapsedTime)-TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(elapsedTime));
			String timeText = hours+":"+minutes+":"+seconds+":"+milliseconds;
			timeLabel.setText(timeText);
		});
	}
	
	
	public void close(WindowEvent we){
		if(updater!=null)
			updater.close();
		System.out.println("CLOSING");
		stage.close();
	}
	
	public void open(File file) throws FileNotFoundException{
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
			currentSplitCollection = (SplitCollection)in.readObject();
			splitEventTable.setItems(currentSplitCollection.splitEventsProperty());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void startButtonClick(ActionEvent ae){
		updater = new Updater();
		new Thread(updater).start();
		startButton.setDisable(true);
	}
	@FXML
	private void newMenuItemClick(ActionEvent ae){
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/xyz/ezstein/fx/options/Options.fxml"));
		
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
		}
		
		Stage stage = new Stage();
		OptionsController oc = ((OptionsController) loader.getController());
		oc.initializeAsGUI(stage);
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void openMenuItemClick(ActionEvent ae){
		FileChooser fc = new FileChooser();
		File file = null;
		if((file = fc.showOpenDialog(null))==null){
			return;
		}
		try {
			open(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
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
