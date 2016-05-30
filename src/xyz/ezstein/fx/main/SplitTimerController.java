package xyz.ezstein.fx.main;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import com.sun.javafx.scene.control.skin.*;

import de.codecentric.centerdevice.*;
import xyz.ezstein.backend.app.*;
import xyz.ezstein.backend.util.*;
import xyz.ezstein.fx.editor.*;
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
import xyz.ezstein.fx.cells.*;
import xyz.ezstein.fx.observable.SplitTime;
import xyz.ezstein.fx.preferences.PreferencesController;

public class SplitTimerController {
	
	private Stage stage;
	private Updater updater;
	private ObjectProperty<SplitCollection> splitCollectionProperty;
	private int eventIndex;
	private boolean promptSave;
	private Scene preferencesRoot;
	private Scene editorRoot;
	private EditorController editorController;
	private PreferencesController preferencesController;
	@FXML private Label timeLabel;
	@FXML private MenuBar mainMenuBar;
	@FXML private Menu applicationMenu;
	@FXML private TableView<SplitEvent> splitEventTable;
	@FXML private TableColumn<SplitEvent, Number> timeTableColumn;
	@FXML private TableColumn<SplitEvent, String> iconTableColumn;
	@FXML private TableColumn<SplitEvent, SplitTime> splitTimeTableColumn;
	@FXML private TableColumn<SplitEvent, SplitTime> eventTimeTableColumn;
	@FXML private TableColumn<SplitEvent, String> nameTableColumn;
	
	
	/**
	 * Called by the FXML loader immediately after this controller object is constructed.
	 */
	public void initialize(){
		System.out.println("INITIALIZED");
	}
	
	public void initializeAsGUI(Stage stage) {
		this.eventIndex=0;
		this.stage=stage;
		this.promptSave=false;
		this.updater = new Updater(this);
		this.splitCollectionProperty=new SimpleObjectProperty<SplitCollection>(new SplitCollection());
		splitCollectionProperty.addListener(new ChangeListener<SplitCollection>(){
			@Override
			public void changed(ObservableValue<? extends SplitCollection> observable, SplitCollection oldValue,
					SplitCollection newValue) {
				splitEventTable.setItems(splitCollectionProperty.get().splitEventsProperty());
			}
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
		
		loadFXML();
		buildTable();
		
		stage.setOnCloseRequest(e->{
			close(e);
		});
		
		stage.getScene().setOnKeyReleased((ke)->{
			ke.consume();
			switch(ke.getCode()){
				case S:
					if(!updater.isTerminated()){
						if(!updater.isPaused()){
							split();
						}
					} else {
						start();
					}
					break;
				case SPACE:
					if(!updater.isTerminated()){
						pause();
					}
					break;
				case BACK_SPACE:
					if(!updater.isTerminated()){
						undo();
					}
					break;
				case ENTER:
					if(!updater.isTerminated()){
						skip();
					}
					break;
				case COMMAND:
					if(!updater.isTerminated()){
						stop();
					}
					break;
			}
		});
		
		

	}
	
	private void pause(){
		System.out.println("CHECKING");
		if(updater.isPaused()){
			updater.unpause();
		} else {
			updater.pause();
		}
	}
	
	private void start(){
		promptSave=true;
		splitCollectionProperty.get().newSession("TMP " + UUID.randomUUID());
		for(int i=0; i<splitCollectionProperty.get().splitEventsProperty().size();i++){
			splitCollectionProperty.get().updateDisplay(i, 0);
		}
		eventIndex=0;
		
		updater.runLater();
	}
	
	private void stop(){
		new Thread(()->{
			updater.stop();
			updater.awaitTermination();
			eventIndex=0;
			splitCollectionProperty.get().deleteSession(splitCollectionProperty.get().getSplitSessionSize()-1);
			updateUITime(0);
			for(int i=0; i<splitCollectionProperty.get().splitEventsProperty().size();i++){
				splitCollectionProperty.get().updateDisplay(i, 0);
			}
		}).start();
		
	}
	
	/*
	 * FIX!!!!
	 */
	private void skip(){
		if(eventIndex+1!=splitCollectionProperty.get().splitEventsProperty().size()){
			int sessionIndex = splitCollectionProperty.get().getUnmodifiableSplitSessions().size()-1;
			splitCollectionProperty.get().updateSession(eventIndex, sessionIndex, -1);
			eventIndex++;
		}
	}
	
	private void undo(){
		if(eventIndex!=0){
			int sessionIndex = splitCollectionProperty.get().getUnmodifiableSplitSessions().size()-1;
			splitCollectionProperty.get().updateSession(eventIndex, sessionIndex, -1);
			eventIndex--;
		}
	}
	
	private void split(){
		int sessionIndex = splitCollectionProperty.get().getUnmodifiableSplitSessions().size()-1;
		splitCollectionProperty.get().updateSession(eventIndex, sessionIndex, updater.getTime());
		if(eventIndex+1!=splitCollectionProperty.get().splitEventsProperty().size()){
			eventIndex++;
		} else {
			new Thread(()->{
				updater.stop();
				updater.awaitTermination();
				eventIndex=0;
			}).start();
		}
	}
	
	private void loadFXML(){
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/xyz/ezstein/fx/editor/Editor.fxml"));
		
		try {
			editorRoot = new Scene(loader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
				System.exit(1);
		}
		editorController = ((EditorController) loader.getController());
		editorController.initializeAsGUI();
		
		loader = new FXMLLoader(this.getClass().getResource("/xyz/ezstein/fx/preferences/Preferences.fxml"));
		try {
			preferencesRoot = new Scene(loader.load());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		preferencesController = (PreferencesController)loader.getController();
		preferencesController.initializeAsGUI();
	}
	
	
	private void buildTable(){
		splitEventTable.widthProperty().addListener(new ChangeListener<Number>()
		{
		    @Override
		    public void changed(ObservableValue<? extends Number> source, Number oldWidth, Number newWidth)
		    {
		        TableHeaderRow header = (TableHeaderRow) splitEventTable.lookup("TableHeaderRow");
		        header.reorderingProperty().addListener(new ChangeListener<Boolean>() {
		            @Override
		            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		                header.setReordering(false);
		            }
		        });
		    }
		});
		
		splitTimeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, SplitTime>, ObservableValue<SplitTime>>(){
			@Override
			public ObservableValue<SplitTime> call(CellDataFeatures<SplitEvent, SplitTime> splitEvent) {
				// TODO Auto-generated method stub
				return splitEvent.getValue().deltaSplitTimeProperty();
			}
		});
		
		splitTimeTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent, SplitTime>, TableCell<SplitEvent, SplitTime>>(){
			@Override
			public TableCell<SplitEvent, SplitTime> call(TableColumn<SplitEvent, SplitTime> param) {
				return new SplitTimeTableCell();
			}
		});
		
		eventTimeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, SplitTime>, ObservableValue<SplitTime>>(){
			@Override
			public ObservableValue<SplitTime> call(CellDataFeatures<SplitEvent, SplitTime> splitEvent) {
				// TODO Auto-generated method stub
				return splitEvent.getValue().deltaEventTimeProperty();
			}
		});
		
		eventTimeTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent, SplitTime>, TableCell<SplitEvent, SplitTime>>(){
			@Override
			public TableCell<SplitEvent, SplitTime> call(TableColumn<SplitEvent, SplitTime> param) {
				return new SplitTimeTableCell();
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
	}
	
	
	
	public void updateUITime(long elapsedTime){
		Platform.runLater(()->{
			if(!updater.isTerminated()){
				timeLabel.setText(Util.nanosToReadable(elapsedTime));
				splitCollectionProperty.get().updateDisplay(eventIndex, elapsedTime);
			}
		});
	}
	
	
	public void close(WindowEvent we){
		we.consume();
		if(updater!=null){
			updater.stop();
		}
		
		ResultType type = showSaveDialog();
		switch(type){
		case save:
			saveCollection();
			break;
		case noSave:
			stage.close();
			break;
		case cancel:
			break;
		}
	}
	
	public ResultType showSaveDialog(){
		if(!promptSave) {
			return ResultType.noSave;
		}
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		ButtonType saveButton = new ButtonType("Save");
		ButtonType cancelButton = new ButtonType("Cancel");
		ButtonType noSaveButton = new ButtonType("Don't Save");
		alert.getButtonTypes().setAll(saveButton, noSaveButton,cancelButton);
		Optional<ButtonType> type = alert.showAndWait();
		if(type.isPresent()){
			if(type.get().equals(saveButton)){
				return ResultType.save;
			} else if(type.get().equals(noSaveButton)){
				return ResultType.noSave;
			} else if(type.get().equals(cancelButton)){
				return ResultType.cancel;
			}
		}
		System.out.println("A");
		return ResultType.noSave;
	}
	
	
	private enum ResultType {
		save,noSave,cancel;
	}
	
	public void open(File file) {
		try(ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))){
			splitCollectionProperty.set((SplitCollection)in.readObject());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(splitCollectionProperty.get());
	}
	
	
	@FXML
	private void openMenuItemClick(ActionEvent ae) {
		ResultType type = showSaveDialog();
		switch(type){
		case save:
			saveCollection();
			break;
		case noSave:
			break;
		case cancel:
			return;
		}
		FileChooser fc = new FileChooser();
		fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Split Collection (*.spc)","*.spc"));
		File file = null;
		if((file = fc.showOpenDialog(stage))==null){
			return;
		}
		open(file);
	}
	
	@FXML
	private void editMenuItemClick(ActionEvent ae){
		promptSave = true;
		Stage stage = new Stage();
		stage.initOwner(this.stage);
		stage.initModality(Modality.WINDOW_MODAL);
		editorController.reinitialize(stage,splitCollectionProperty.get());
		editorController.setOnClose((a)->{return null;});
		stage.setScene(editorRoot);
		stage.show();
	}
	
	@FXML
	private void newMenuItemClick(ActionEvent ae){
		
		ResultType type = showSaveDialog();
		switch(type){
		case save:
			saveCollection();
			break;
		case noSave:
			break;
		case cancel:
			return;
		}
		
		
		Stage stage = new Stage();
		stage.initOwner(this.stage);
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setScene(editorRoot);
		editorController.reinitialize(stage, new SplitCollection());
		editorController.setOnClose((splitCollection)->{
			splitCollectionProperty.set(splitCollection);
			
			return null;
		});
		
		stage.show();
		promptSave=true;
	}
	
	public void saveAsCollection() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Save...");
		File file = null;
		if((file = fileChooser.showSaveDialog(stage))==null){
			return;
		}
		String filePath = file.getAbsolutePath();
		if(!filePath.endsWith(".spc")){
			filePath+=".spc";
		}
		splitCollectionProperty.get().setSavePath(Paths.get(filePath));
		
		try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filePath))) {
			out.writeObject(splitCollectionProperty.get());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setContentText("Saved!");
		alert.setTitle("Saving...");
		alert.show();
	}
	
	public void saveCollection(){
		SplitCollection collection = splitCollectionProperty.get();
		if(collection.getSavePath()==null){
			saveAsCollection();
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
			Alert alert = new Alert(Alert.AlertType.INFORMATION);
			alert.setContentText("Saved!");
			alert.setTitle("Saving...");
			alert.show();
		}
	}
	
	@FXML
	private void saveMenuItemClick(ActionEvent ae){
		saveCollection();
	}
	
	@FXML
	private void saveAsMenuItemClick(ActionEvent ae){
		saveAsCollection();
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
		this.close(null);
	}
	@FXML
	private void preferencesMenuItemClick(ActionEvent ae){
		
		Stage stage = new Stage();
		stage.initOwner(this.stage);
		stage.initModality(Modality.WINDOW_MODAL);
		preferencesController.reinitialize(stage);
		stage.setScene(preferencesRoot);
		stage.show();
		
	}
	
	@FXML
	private void aboutMenuItemClick(ActionEvent ae){
		
	}
}
