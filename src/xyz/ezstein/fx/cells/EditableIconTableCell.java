package xyz.ezstein.fx.cells;

import java.io.*;
import javafx.beans.property.*;
import javafx.beans.value.*;
import javafx.event.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.*;
import javafx.stage.*;
import xyz.ezstein.backend.*;

public class EditableIconTableCell extends TableCell<SplitEvent,String> {
	private ImageView imageView;
	private SimpleStringProperty boundToCurrently;
	private final int id = (int)(1000*Math.random());
	
	public EditableIconTableCell(){
		super();
		Image image = new Image(this.getClass().getResourceAsStream("/res/background.jpg"));
		imageView=new ImageView();
		imageView.setImage(image);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
		
		super.setOnMousePressed(me->{
			if(super.isEmpty()){
				return;
			}
			System.out.println("Clicked");
			FileChooser fc = new FileChooser();
			
			fc.setTitle("Choose Icon");
			FileChooser.ExtensionFilter allImages = new FileChooser.ExtensionFilter("All Image Files (*.jpg, *.png, *.gif)", "*.JPG","*.PNG","*.GIF");
			FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            fc.getExtensionFilters().addAll(allImages, extFilterJPG, extFilterPNG);
			File file;
			if((file = fc.showOpenDialog(null)) == null) {
				
				return;
			}
			System.out.println(file.getAbsolutePath());
			boundToCurrently.set(file.getAbsolutePath());
			me.consume();
		});
		setGraphic(imageView);
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
			} else if(boundToCurrently!=sp){
				boundToCurrently=sp;
			}
			
			Image image = new Image(this.getClass().getResourceAsStream("/res/background.jpg"));
			try{
				image = new Image(new FileInputStream(boundToCurrently.get()));
			} catch(FileNotFoundException iae){
				//iae.printStackTrace();
			}
			
			imageView.setImage(image);
			setGraphic(imageView);
		}
	}
}