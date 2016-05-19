package xyz.ezstein.backend.test;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import javafx.util.Callback;

public class KnightsTour {
	private CellData[][] grid;
	private final Point[] moves;
	private final int startX;
	private final int startY;
	private boolean first;
	
	public KnightsTour(int rows, int columns,int startX, int startY){
		grid = new CellData[rows][columns];
		for(int y=0;y<grid[0].length;y++){
			for(int x=0; x<grid.length;x++){
				grid[x][y]=new CellData(false);
			}
		}
		moves=new Point[]{
				new Point(1,0),
				new Point(-1,0),
				new Point(0,1),
				new Point(0,-1)
		};	
		this.startX=startX;
		this.startY=startY;
		first=true;
	}
	
	public CellData[][] tour(){
		tour(startX,startY);
		return grid;
	}
	
	public CellData[][] iterate(Callback<CellData[][],Void> callback, long wait){
		iterate(startX,startY, callback, wait);
		return grid;
	}
	
	private boolean iterate(int x, int y, Callback<CellData[][],Void> callback, long wait){
		grid[x][y].setVisited(true);
		grid[x][y].setTraced(true);
		//printGrid(x,y);
		if(x==startX&&y==startY&&!first){
			return true;
		}
		first=false;
		Point[] myMoves = Arrays.copyOf(moves, moves.length);
		shuffleArray(myMoves);
		for(Point p:myMoves){
			int nextX=x+p.x;
			int nextY=y+p.y;
			if(nextX<0||nextX>=grid.length){
				continue;
			} else if (nextY<0||nextY>=grid[0].length){
				continue;
			} else if(grid[nextX][nextY].isVisited()){
				continue;
			} else {
				grid[x][y].addDirection(p.direction);
				try {
					Thread.sleep(wait);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				callback.call(grid);
				
				if(iterate(nextX,nextY,callback,wait)){
					return true;
				} else {
					//printGrid(x,y);
				}
			}
		}
		
		grid[x][y].setTraced(false);
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		callback.call(grid);
		return false;
	}
	
	private boolean tour(int x, int y){
		
		grid[x][y].setVisited(true);
		//printGrid(x,y);
		if(x==startX&&y==startY&&!first){
			return true;
		}
		first=false;
		Point[] myMoves = Arrays.copyOf(moves, moves.length);
		shuffleArray(myMoves);
		for(Point p:myMoves){
			int nextX=x+p.x;
			int nextY=y+p.y;
			if(nextX<0||nextX>=grid.length){
				continue;
			} else if (nextY<0||nextY>=grid[0].length){
				continue;
			} else if(grid[nextX][nextY].isVisited()){
				continue;
			} else {
				
				grid[x][y].addDirection(p.direction);
				if(tour(nextX,nextY)){
					return true;
				} else {
					//printGrid(x,y);
				}
			}
		}
		
		
		//grid[x][y].setVisited(false);
		return false;
		
	}
	
	public void printGrid(int px, int py){
		for(int y=0;y<grid[0].length;y++){
			for(int x=0; x<grid.length;x++){
				if(x==px&&y==py){
					System.out.print("#  ");
				} else {
					String out = "";
					if(grid[x][y].getDirections().size()==0){
						out+="0";
					}
					for(int i=0; i<grid[x][y].getDirections().size(); i++){
						Direction d = grid[x][y].getDirections().get(i);
						if(d==Direction.NONE){
							out+="0";
						} else {
							out+=d;
						}
					}
					System.out.print(out+"  ");
				}
				
			}
			System.out.println("\n");
		}
		System.out.println("\n\n\n");
	}
	
	private static <T> void shuffleArray(T[] ar)
	  {
	    // If running on Java 6 or older, use `new Random()` on RHS here
	    Random rnd = ThreadLocalRandom.current();
	    for (int i = ar.length - 1; i > 0; i--)
	    {
	      int index = rnd.nextInt(i + 1);
	      // Simple swap
	      T a = ar[index];
	      ar[index] = ar[i];
	      ar[i] = a;
	    }
	  }
	
	private static <T> void shift(T[] ar) {
		T temp = ar[0];
	    for (int i = ar.length-2; i >=0; i--) {
	    	System.out.println("A");
	    	ar[i+1] = ar[i];
	    }
	    ar[0]=temp;
	  }
	
	private class Point {
		public int x;
		public int y;
		public Direction direction;
		public Point(int x, int y){
			this.x=x;
			this.y=y;
			if(y==0&&x>0){
				direction=direction.E;
			} else if(y==0&&x<0){
				direction=direction.W;
			} else if(y<0&&x==0){
				direction=direction.N;
			} else {
				direction=direction.S;
			}
		}
	}
	
	public class CellData {
		private boolean visited;
		private boolean traced;
		private List<Direction> directions;
		private CellData(boolean visited){
			this.visited=visited;
			directions=new ArrayList<Direction>();
			traced=false;
			visited=false;
		}
		public boolean isVisited(){
			return visited;
		}
		public boolean isTraced(){
			return traced;
		}
		public void setTraced(boolean traced){
			this.traced=traced;
		}
		public List<Direction> getDirections(){
			return Collections.unmodifiableList(directions);
		}
		public void setVisited(boolean visited){
			this.visited=visited;
		}
		public void addDirection(Direction direction){
			directions.add(direction);
		}
	}
	
	public enum Direction {
		N,S,E,W,NONE;
	}
	
}
