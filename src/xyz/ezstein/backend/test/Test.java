package xyz.ezstein.backend.test;

import xyz.ezstein.backend.SplitCollection;

public class Test {
	public static void main(String[] args){
		new Test().start();
	}
	
	public void start(){
		
		SplitCollection collection2 = new SplitCollection("COLLTION 2");
		for(int i=0; i<10; i++){
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			collection2.newSession("SESSION " + i);
		}
		collection2.newEvent("EVENT 1");
		collection2.newEvent("EVENT 1");
		collection2.newEvent("EVENT 3");
		collection2.newEvent("EVENT 4");
		collection2.updateSession("SESSION 4", 0,100L);
		
		System.out.print(collection2);
	}
}
