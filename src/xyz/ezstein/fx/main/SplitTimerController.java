package xyz.ezstein.fx.main;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.concurrent.*;

import de.codecentric.centerdevice.*;
import xyz.ezstein.backend.app.*;
import xyz.ezstein.fx.options.*;
import xyz.ezstein.backend.*;
import javafx.application.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.util.*;

public class SplitTimerController {
	
	private Stage stage;
	private Scene scene;
	private Updater updater;
	private ObjectProperty<SplitCollection> splitCollectionProperty;
	private boolean timing;
	private int eventIndex;
	private String currentSession;
	@FXML private Label timeLabel;
	@FXML private MenuBar mainMenuBar;
	@FXML private Menu applicationMenu;
	@FXML private Button startStopButton;
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
		this.currentSession="";
		this.eventIndex=0;
		this.timing=false;
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
		iconTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent,String>,TableCell<SplitEvent,String>>(){

			@Override
			public TableCell<SplitEvent, String> call(TableColumn<SplitEvent, String> param) {
			
				return new IconTableCell();
			}
			
		});
		timeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				// TODO Auto-generated method stub
				return splitEvent.getValue().currentTimeProperty();
			}
		});
		
		timeTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent, Number>,TableCell<SplitEvent, Number>>(){

			@Override
			public TableCell<SplitEvent, Number> call(TableColumn<SplitEvent, Number> param) {
				return new TimeTableCell();
			}
		});
		
		
		splitCollectionProperty=new SimpleObjectProperty<SplitCollection>();
		splitCollectionProperty.addListener(new ChangeListener<SplitCollection>(){
			@Override
			public void changed(ObservableValue<? extends SplitCollection> observable, SplitCollection oldValue,
					SplitCollection newValue) {
				splitEventTable.setItems(splitCollectionProperty.get().splitEventsProperty());
			} 
		});
		splitCollectionProperty.set(new SplitCollection());
		
		scene.setOnKeyReleased((ke)->{
			if(ke.getCode().equals(KeyCode.SPACE)){
				if(eventIndex+1!=splitCollectionProperty.get().splitEventsProperty().size()){
					
					eventIndex++;
				} else {
					new Thread(()->{
						updater.stop();
						updater.join();
						eventIndex=0;
					}).start();
				}
			}
		});

	}
	
	private class Updater implements Runnable {
		private long startTime;
		private long currentTime;
		private boolean close;
		private boolean terminated;
		private Object lock;
		
		public Updater(){
			lock = new Object();
			this.close=false;
			terminated=false;
			startTime=0;
			currentTime=0;
		}
		
		@Override
		public void run() {
			startTime=System.nanoTime();
			while(!close){
				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				currentTime=System.nanoTime();
				long elapsed = currentTime-startTime;
				updateUITime(elapsed);
			}
			terminated=true;
			synchronized(lock){
				lock.notifyAll();
			}
			System.out.println("ABC");
		}
		
		public void join(){
			synchronized(lock){
				while(!terminated){
					try {
						lock.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			System.out.println("DONE");
			
		}
		
		public void stop(){
			close=true;
		}
		
		public boolean isTerminated(){
			return terminated;
		}
	}
	
	public void updateUITime(long elapsedTime){
		Platform.runLater(()->{
			if(!updater.isTerminated()){
				long hours = TimeUnit.NANOSECONDS.toHours(elapsedTime);
				long minutes = TimeUnit.NANOSECONDS.toMinutes(elapsedTime)-TimeUnit.HOURS.toMinutes(TimeUnit.NANOSECONDS.toHours(elapsedTime));
				long seconds = TimeUnit.NANOSECONDS.toSeconds(elapsedTime)-TimeUnit.MINUTES.toSeconds(TimeUnit.NANOSECONDS.toMinutes(elapsedTime));
				long milliseconds = TimeUnit.NANOSECONDS.toMillis(elapsedTime)-TimeUnit.SECONDS.toMillis(TimeUnit.NANOSECONDS.toSeconds(elapsedTime));
				String timeText = hours+":"+minutes+":"+seconds+":"+milliseconds;
				timeLabel.setText(timeText);
				splitCollectionProperty.get().changeDisplayedTime(eventIndex, elapsedTime);
			}
		});
	}
	
	
	public void close(WindowEvent we){
		if(updater!=null){
			updater.stop();
		}
		System.out.println("CLOSING");
		stage.close();
	}
	
	public void open(File file) throws FileNotFoundException{
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
			splitCollectionProperty.set((SplitCollection)in.readObject());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@FXML
	private void startStopButtonClick(ActionEvent ae){
		if(startStopButton.getText().equals("Start")){
			eventIndex=0;
			currentSession = "TMP_NAME" + UUID.randomUUID();
			splitCollectionProperty.get().newSession(currentSession);
			updater = new Updater();
			new Thread(updater).start();
			startStopButton.setText("Stop");;
		} else if(startStopButton.getText().equals("Stop")){
			startStopButton.setText("Start");
			updater.stop();
		} else {
			System.err.println("ERROR");
		}
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
		oc.setOnClose((splitCollection)->{
			splitCollectionProperty.set(splitCollection);
			return null;
		});
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
	private void editMenuItemClick(ActionEvent ae){
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
		oc.initializeAsGUI(stage,splitCollectionProperty.get());
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	@FXML
	private void saveMenuItemClick(ActionEvent ae){
		SplitCollection collection = splitCollectionProperty.get();
		if(collection.getSavePath()==null){
			saveAsMenuItemClick(ae);
		} else {
			try(ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(collection.getSavePath()))) {
				out.writeObject(collection);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@FXML
	private void saveAsMenuItemClick(ActionEvent ae){
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save...");
		File file = null;
		if((file = fileChooser.showSaveDialog(null))==null){
			return;
		}
		
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file))) {
			out.writeObject(splitCollectionProperty.get());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
