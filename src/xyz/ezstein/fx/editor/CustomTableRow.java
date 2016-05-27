package xyz.ezstein.fx.editor;

import javafx.scene.control.*;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import xyz.ezstein.backend.*;

public class CustomTableRow extends TableRow<SplitEvent> {
	
	private static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
	
	public CustomTableRow(){
		
		
		
		this.setOnDragDetected(event -> {
			
			if (! isEmpty()) {
	           Integer index = this.getIndex();
	           Dragboard db = this.startDragAndDrop(TransferMode.MOVE);
	           db.setDragView(this.snapshot(null, null));
	           ClipboardContent cc = new ClipboardContent();
	           cc.put(SERIALIZED_MIME_TYPE, index);
	           db.setContent(cc);
	           event.consume();
	       }
	   });
	
	   this.setOnDragOver(event -> {
	       Dragboard db = event.getDragboard();
	       if (db.hasContent(SERIALIZED_MIME_TYPE)) {
	           if (this.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
	               event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
	               event.consume();
	           }
	       }
	   });
	
	   this.setOnDragDropped(event -> {
		   TableView<SplitEvent> tableView = super.getTableView();
	       Dragboard db = event.getDragboard();
	       if (db.hasContent(SERIALIZED_MIME_TYPE)) {
	           int draggedIndex = (Integer) db.getContent(SERIALIZED_MIME_TYPE);
	           SplitEvent draggedPerson = tableView.getItems().remove(draggedIndex);
	
	          int dropIndex;
	           if (this.isEmpty()) {
	               dropIndex = tableView.getItems().size() ;
	           } else {
	               dropIndex = this.getIndex();
	           }
	
	           tableView.getItems().add(dropIndex, draggedPerson);
	
	           event.setDropCompleted(true);
	           tableView.getSelectionModel().select(dropIndex);
	           event.consume();
	       }
	   });
	}
}
