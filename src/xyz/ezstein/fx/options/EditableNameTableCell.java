package xyz.ezstein.fx.options;

import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.scene.control.*;
import xyz.ezstein.backend.*;

public class EditableNameTableCell extends TableCell<SplitEvent,String> {
	private TextField nameField;
	private SimpleStringProperty boundToCurrently;
	private final int id = (int)(1000*Math.random());
	
	public EditableNameTableCell(){
		super();
		nameField=new TextField();
		setGraphic(nameField);
	}
	
	@Override
	public void updateItem(String name, boolean empty){
		super.updateItem(name, empty);
		if(empty||name==null){
			setGraphic(null);
		} else{
			SimpleStringProperty sp = (SimpleStringProperty)getTableColumn().getCellObservableValue(getIndex());
			if(boundToCurrently==null){
				boundToCurrently=sp;
				nameField.textProperty().bindBidirectional(sp);
			} else if(boundToCurrently!=sp){
				nameField.textProperty().unbindBidirectional(boundToCurrently);
				boundToCurrently=sp;
				nameField.textProperty().bindBidirectional(boundToCurrently);
			}
			setGraphic(nameField);
		}
	}
}