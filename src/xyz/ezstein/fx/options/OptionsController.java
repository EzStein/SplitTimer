package xyz.ezstein.fx.options;

import java.io.*;

import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.scene.control.cell.*;
import javafx.scene.input.*;
import javafx.stage.*;
import javafx.util.*;
import xyz.ezstein.backend.*;

public class OptionsController {
	
	@FXML private TableView<SplitEvent> splitEventTable;
	@FXML private TableColumn<SplitEvent, Number> timeTableColumn;
	@FXML private TableColumn<SplitEvent, String> iconTableColumn;
	@FXML private TableColumn<SplitEvent, Number> splitTimeTableColumn;
	@FXML private TableColumn<SplitEvent, String> nameTableColumn;
	private SplitCollection splitCollection;
	private Callback<SplitCollection, Void> callBackOnClose;
	private Stage stage;
	
	public void initialize(){
		System.out.println("OPTIONS INITIALIZED");
	}
	
	public void initializeAsGUI(Stage stage){
		initializeAsGUI(stage, new SplitCollection());
	}
	
	public void initializeAsGUI(Stage stage,SplitCollection splitCollection){
		this.stage=stage;
		this.splitCollection = splitCollection;
		splitEventTable.setItems(splitCollection.splitEventsProperty());
		stage.setOnCloseRequest(we->{
			we.consume();
			
			close();
			stage.close();
		});
		timeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				return new SimpleLongProperty(0);
			}
		});
		splitTimeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				return new SimpleLongProperty(0);
			}
		});
		
		
		
		nameTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<SplitEvent, String> splitEvent) {
				return splitEvent.getValue().nameProperty();
			}
		});
		nameTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent, String>,TableCell<SplitEvent,String>>(){

			@Override
			public TableCell<SplitEvent, String> call(TableColumn<SplitEvent, String> column) {
				return new EditableNameTableCell();
			}
		});
		
		iconTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, String>, ObservableValue<String>>(){
			@Override
			public ObservableValue<String> call(CellDataFeatures<SplitEvent, String> splitEvent) {
				return splitEvent.getValue().iconProperty();
			}
		});
		iconTableColumn.setCellFactory(new Callback<TableColumn<SplitEvent,String>,TableCell<SplitEvent,String>>(){

			@Override
			public TableCell<SplitEvent, String> call(TableColumn<SplitEvent, String> param) {
				return new EditableIconTableCell();
			}
		});
		
		splitEventTable.setRowFactory(new Callback<TableView<SplitEvent>,TableRow<SplitEvent>>(){
			@Override
			public TableRow<SplitEvent> call(TableView<SplitEvent> tableView) {
				return new CustomTableRow();
			}
			
		});
	}
	
	public void close(){
		if(callBackOnClose!=null){
			callBackOnClose.call(splitCollection);
		}
	}
	
	public void setOnClose(Callback<SplitCollection,Void> callBack){
		callBackOnClose=callBack;
	}
	
	@FXML
	private void addEventButtonClick(ActionEvent ae){
		splitEventTable.getItems().add(new SplitEvent());
	}
	
	@FXML
	private void removeEventButtonClick(ActionEvent ae){
		splitEventTable.getItems().remove(splitEventTable.getSelectionModel().getSelectedItem());
	}
	
}
