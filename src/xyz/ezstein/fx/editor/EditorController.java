package xyz.ezstein.fx.editor;

import java.time.*;
import java.time.format.DateTimeFormatter;

import com.sun.javafx.scene.control.skin.TableHeaderRow;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.*;
import javafx.stage.*;
import javafx.util.*;
import xyz.ezstein.backend.*;
import xyz.ezstein.backend.util.*;
import xyz.ezstein.fx.cells.*;

public class EditorController {
	
	@FXML private TableView<SplitEvent> splitEventTable;
	@FXML private TableColumn<SplitEvent, String> iconTableColumn;
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
			closeRequest(false);
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
		
		for(SplitSession session : splitCollection.getUnmodifiableSplitSessions()){
			TableColumn<SplitEvent, Number> column = new TableColumn<SplitEvent, Number>(session.getDate().atZone(ZoneId.systemDefault()).toString());
			column.setCellValueFactory((dataFeatures)->{
				return new SimpleLongProperty(dataFeatures.getValue().getTime(session));
			});
			column.setCellFactory((col)->{
				return new TimeTableCell();
			});
			LocalDateTime time = LocalDateTime.ofInstant(session.getDate(),ZoneId.systemDefault());
			column.setText(time.format(DateTimeFormatter.ofPattern("hh:mm MMM/d/yyyy")));
			splitEventTable.getColumns().add(column);
			column.setSortable(false);
		}
		
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
	}
	
	public void closeRequest(boolean apply){
		if(callBackOnClose!=null && apply){
			callBackOnClose.call(splitCollection);
		}
		stage.close();
	}
	
	public void setOnClose(Callback<SplitCollection,Void> callBack){
		callBackOnClose=callBack;
	}
	
	@FXML
	private void addEventButtonClick(ActionEvent ae){
		//splitEventTable.getItems().add(new SplitEvent());
		splitCollection.newEvent("");
	}
	
	@FXML
	private void removeEventButtonClick(ActionEvent ae){
		splitEventTable.getItems().remove(splitEventTable.getSelectionModel().getSelectedItem());
	}
	
	/*
	 * RESOLVE APPLY ISSUE For EDIT BUT NOT NEW
	 */
	@FXML
	private void applyButtonClick(ActionEvent ae){
		closeRequest(true);
	}
	
	@FXML
	private void cancelButtonClick(ActionEvent ae){
		closeRequest(false);
	}
}
