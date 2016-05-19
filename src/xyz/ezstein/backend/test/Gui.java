package xyz.ezstein.backend.test;

import javafx.application.*;
import javafx.scene.*;
import javafx.scene.canvas.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.*;
import xyz.ezstein.backend.test.KnightsTour.*;
import javafx.scene.paint.*;

public class Gui extends Application {
	private Canvas canvas;
	private final int cellSize = 5;
	private final int cellW=100;
	private final int cellH=100;
	public static void main(String[] args){
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
		HBox box = new HBox();
		canvas = new Canvas(cellW*cellSize,cellH*cellSize);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		
		box.getChildren().add(canvas);
		Scene scene = new Scene(box);
		stage.setScene(scene);
		stage.show();
		new Thread(()->{
			KnightsTour t = new KnightsTour(cellW,cellH,10,10);
			CellData[][] data = t.iterate((grid)->{
				//System.out.println("A");
				Platform.runLater(()->{
					draw(gc,grid);
				});
				return null;
			},2);
			Platform.runLater(()->{
				draw(gc,data);
			});
			
		}).start();
		
		
	}
	
	public void draw(GraphicsContext gc,CellData[][] data){
		gc.setFill(Color.WHITE);
		gc.setStroke(Color.BLACK);
		gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(double i=0.5; i<=canvas.getWidth(); i+=cellSize){
			gc.strokeLine(i, 0, i, canvas.getHeight()-0.5);
		}
		for(double i=0.5; i<=canvas.getHeight(); i+=cellSize){
			gc.strokeLine(0, i, canvas.getWidth()-0.5, i);
		}
		
		
		for(int x=0; x<data.length; x++){
			for(int y=0; y<data[0].length; y++){
				for(Direction dir : data[x][y].getDirections()){
					
					double x1=cellSize*((double)x)+0.5;
					double y1=cellSize*((double)y)+0.5;
					
					/*gc.setStroke(Color.TRANSPARENT);
					gc.setGlobalAlpha(1);
					if(dir==Direction.N){
						gc.strokeLine(x1+1, y1, x1+cellSize-1, y1);
					} else if(dir==Direction.S){
						gc.strokeLine(x1+1, y1+cellSize, x1+cellSize-1, y1+cellSize);
					} else if(dir==Direction.E){
						gc.strokeLine(x1+cellSize, y1+1, x1+cellSize, y1+cellSize-1);
					} else if(dir==Direction.W){
						gc.strokeLine(x1, y1+1, x1, y1+cellSize-1);
					}*/
					
					gc.setStroke(Color.TRANSPARENT);
					gc.setGlobalAlpha(1);
					if(dir==Direction.N){
						gc.clearRect(x1+1, y1, cellSize-2, 1);
					} else if(dir==Direction.S){
						gc.clearRect(x1+1, y1+cellSize, cellSize-2, 1);
					} else if(dir==Direction.E){
						gc.clearRect(x1+cellSize, y1+1, 1, cellSize-2);
					} else if(dir==Direction.W){
						gc.clearRect(x1, y1+1, 1, cellSize-2);
					}
					
					if(data[x][y].isTraced()){
						//gc.setGlobalAlpha(0.5);
						gc.setFill(Color.ORANGE);
						if(dir==Direction.N){
							gc.fillRect(x1+1, y1-2, cellSize-2, cellSize+2);
						} else if(dir==Direction.S){
							gc.fillRect(x1+1, y1+1, cellSize-2, cellSize+1);
						} else if(dir==Direction.E){
							gc.fillRect(x1+1, y1+1, cellSize+1, cellSize-2);
						} else if(dir==Direction.W){
							gc.fillRect(x1-2, y1+1, cellSize+2, cellSize-2);
						}
					}
				}
			}
		}
	}

}
