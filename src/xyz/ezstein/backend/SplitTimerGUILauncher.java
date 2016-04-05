package xyz.ezstein.backend;

import java.io.*;
import java.util.*;
import javafx.application.*;
import javafx.fxml.*;
import javafx.scene.*;
import javafx.stage.*;
import xyz.ezstein.fx.main.*;

/**
 * This class launches the application
 * @author Ezra Stein
 * @version 1.0
 * @since 2015
 *
 */
public class SplitTimerGUILauncher extends Application {
	
	/**
	 * This main method launches a javafx application GUI.
	 * @param args	unused
	 */
	public static void main(String[] args){
		launch(args);
	}

	/**
	 * Loads the nodes for the main gui through and fxml file.
	 */
	@Override
	public void start(Stage stage) {
		Parent root = null;
		FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/xyz/ezstein/fx/main/SplitTimer.fxml"));
		try {
			root = loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(1);
		}
		Scene scene = new Scene(root);
		stage.setScene(scene);
		
		SplitTimerController controller = loader.getController();
		controller.initializeAsGUI(stage,scene);
		
		
		
		stage.show();
		
	}
	
	public byte[] readFile(String path, int chunkSize){
		byte[] bytesTotal = new byte[0];
		
		try(FileInputStream in = new FileInputStream(path)) {
			while(true){
				byte[] b = new byte[chunkSize];
				if(in.read(b, 0, b.length)==-1){
					break;
				}
				bytesTotal = concat(bytesTotal, b);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return bytesTotal;
	}
	
	public byte[] concat(byte[] a, byte[] b) {
		   int aLen = a.length;
		   int bLen = b.length;
		   byte[] c= new byte[aLen+bLen];
		   System.arraycopy(a, 0, c, 0, aLen);
		   System.arraycopy(b, 0, c, aLen, bLen);
		   return c;
		}
}
