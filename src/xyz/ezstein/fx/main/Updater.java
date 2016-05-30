package xyz.ezstein.fx.main;

public class Updater {
	private long startTime;
	private long currentTime;
	private long pausedTime;
	private boolean stop;
	private boolean terminated;
	private boolean paused;
	private SplitTimerController controller;
	private Object lock, pauser;
	
	public Updater(SplitTimerController controller){
		this.controller=controller;
		lock = new Object();
		pauser = new Object();
		stop=false;
		paused=false;
		terminated=true;
		pausedTime=0;
		startTime=0;
		currentTime=0;
	}
	
	
	public void runLater(){
		if(terminated){
			terminated=false;
			paused=false;
			stop=false;
			startTime=0;
			currentTime=0;
			pausedTime=0;
			new Thread(()->{
				run();
			}).start();
		}
		
	}
	
	private void run() {
		startTime=System.nanoTime();
		while(!stop){
			try {
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			synchronized(pauser){
				while(paused){
					try {
						pauser.wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			currentTime=System.nanoTime();
			controller.updateUITime(getTime());
		}
		terminated=true;
		synchronized(lock){
			lock.notifyAll();
		}
	}
	
	public long getTime(){
		long elapsed = currentTime-startTime-pausedTime;
		return elapsed;
	}
	
	public void awaitTermination(){
		synchronized(lock){
			while(!terminated){
				try {
					lock.wait();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * Attempts to stop the updater. If the updater is currently paused, this method
	 * unpauses it and proceeds to stop.
	 * This method does not block until the updater is terminated. Use awaitTermination() for that.
	 */
	public void stop(){
		if(!terminated){
			if(paused){
				unpause();
			}
			stop=true;
		}
	}
	
	public void pause(){
		if(!terminated && !paused){
			paused=true;
		}
	}
	
	public void unpause(){
		if(!terminated && paused){
			paused=false;
			pausedTime+=System.nanoTime()-currentTime;
			synchronized(pauser){
				pauser.notifyAll();
			}
		}
	}
	
	public boolean isTerminated(){
		return terminated;
	}
	
	public boolean isPaused(){
		return paused;
	}
}