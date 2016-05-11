package xyz.ezstein.fx.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import xyz.ezstein.backend.SplitEvent;

public class IconTableCell extends TableCell<SplitEvent, String> {
	
	private ImageView imageView;
	private SimpleStringProperty boundToCurrently;
	private final int id = (int)(1000*Math.random());
	
	public IconTableCell(){
		super();
		Image image = new Image(this.getClass().getResourceAsStream("/res/background.jpg"));
		imageView=new ImageView();
		imageView.setImage(image);
		imageView.setFitHeight(30);
		imageView.setFitWidth(30);
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
