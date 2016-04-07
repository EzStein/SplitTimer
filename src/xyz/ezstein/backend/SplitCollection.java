package xyz.ezstein.backend;

import java.io.Serializable;
import java.util.ArrayList;

public class SplitCollection implements Serializable {
	private ArrayList<SplitSession> splitSessions;
	private long targetTime;
	
	public SplitCollection(ArrayList<SplitSession> splitSessions, long targetTime){
		this.splitSessions=splitSessions;
		this.targetTime=targetTime;
	}
	
	public SplitCollection(){
		this(new ArrayList<SplitSession>(), 0);
	}
	
	public ArrayList<SplitSession> getSplitSessions(){
		return splitSessions;
	}
	
	public long getTargetTime(){
		return targetTime;
	}
	
	public long getBestSessionTime(){
		long time = Long.MAX_VALUE;
		for(SplitSession session: splitSessions){
			time = Math.min(time, session.getTime());
		}
		return time;
	}
	
	public long getSumOfBestEventTimes(){
		if(splitSessions.isEmpty()){
			return -1;
		}
		int events = splitSessions.get(0).getSplitEvents().size();
		long[] timesInEachEvent = new long[events];
		for(int i=0;i<events;i++){
			timesInEachEvent[i] = Long.MAX_VALUE;
		}
		for(SplitSession session: splitSessions){
			for(SplitEvent event:session.getSplitEvents()){
				timesInEachEvent[event.positionIdProperty().get()] = 
						Math.min(timesInEachEvent[event.positionIdProperty().get()], event.timeProperty().get());
			}
		}
		long totalTime=0;
		for(int i=0;i<events;i++){
			totalTime += timesInEachEvent[i];
		}
		return totalTime;
	}
	
	@Override
	public boolean equals(Object object){
		if(object==null){
			return false;
		}
		if(object==this){
			return true;
		}
		if(object instanceof SplitCollection){
			SplitCollection sc = (SplitCollection) object;
			if(sc.getSplitSessions().equals(splitSessions)&&sc.getTargetTime()==targetTime){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (int)(targetTime*123)+splitSessions.hashCode();
	}
}
