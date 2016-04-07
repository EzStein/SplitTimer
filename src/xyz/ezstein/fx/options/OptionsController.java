package xyz.ezstein.fx.options;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.*;
import javafx.scene.control.*;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.*;
import javafx.util.Callback;
import xyz.ezstein.backend.*;

public class OptionsController {
	
	@FXML private TableView<SplitEvent> splitEventTable;
	@FXML private TableColumn<SplitEvent, Number> timeTableColumn;
	@FXML private TableColumn<SplitEvent, String> iconTableColumn;
	@FXML private TableColumn<SplitEvent, Number> splitTimeTableColumn;
	@FXML private TableColumn<SplitEvent, String> nameTableColumn;
	private final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	public void initialize(){
		System.out.println("OPTIONS INITIALIZED");
		initializeAsGUI();
	}
	
	public void initializeAsGUI(){
		splitEventTable.setEditable(true);
		timeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				return splitEvent.getValue().timeProperty();
			}
		});
		splitTimeTableColumn.setCellValueFactory(new Callback<CellDataFeatures<SplitEvent, Number>, ObservableValue<Number>>(){
			@Override
			public ObservableValue<Number> call(CellDataFeatures<SplitEvent, Number> splitEvent) {
				return splitEvent.getValue().splitTimeProperty();
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
		
		
		
		
		
		
		splitEventTable.setItems(FXCollections.observableArrayList(new SplitEvent()));
		
		splitEventTable.setRowFactory(new Callback<TableView<SplitEvent>,TableRow<SplitEvent>>(){

			@Override
			public TableRow<SplitEvent> call(TableView<SplitEvent> tableView) {
				TableRow<SplitEvent> row = new TableRow<SplitEvent>();
				
				
				
				 row.setOnDragDetected(event -> {
		                if (! row.isEmpty()) {
		                    Integer index = row.getIndex();
		                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
		                    db.setDragView(row.snapshot(null, null));
		                    ClipboardContent cc = new ClipboardContent();
		                    cc.put(SERIALIZED_MIME_TYPE, index);
		                    db.setContent(cc);
		                    event.consume();
		                }
		            });

		            row.setOnDragOver(event -> {
		                Dragboard db = event.getDragboard();
		                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
		                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
		                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
		                        event.consume();
		                    }
		                }
		            });

		            row.setOnDragDropped(event -> {
		                Dragboard db = event.getDragboard();
		                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
		                    int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
		                    SplitEvent draggedPerson = tableView.getItems().remove(draggedIndex);

		                    int dropIndex ; 

		                    if (row.isEmpty()) {
		                        dropIndex = tableView.getItems().size() ;
		                    } else {
		                        dropIndex = row.getIndex();
		                    }

		                    tableView.getItems().add(dropIndex, draggedPerson);

		                    event.setDropCompleted(true);
		                    tableView.getSelectionModel().select(dropIndex);
		                    event.consume();
		                }
		            });
				
				return row;
			}
			
		});
		
	}
	
	@FXML
	private void addEventButtonClick(ActionEvent ae){
		splitEventTable.getItems().add(new SplitEvent("123",100,100,"123",0));
	}
	
	@FXML
	private void removeEventButtonClick(ActionEvent ae){
		splitEventTable.getItems().remove(splitEventTable.getSelectionModel().getSelectedItem());
	}
	
	@FXML
	private void randomize(ActionEvent ae){
		splitEventTable.getItems().get(0).nameProperty().set(""+Math.random());
	}
}
