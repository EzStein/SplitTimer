package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

public class SplitCollection implements Serializable {
	private final ArrayList<SplitEvent> splitEvents;
	private final String name;
	
	public SplitCollection(String name, ArrayList<SplitEvent> splitEvents){
		this.name=name;
		this.splitEvents=splitEvents;
	}
	public SplitCollection(){
		this("default", new ArrayList<SplitEvent>());
	}
	
	public SplitCollection(String name){
		this(name, new ArrayList<SplitEvent>());
	}
	
	public long getSessionTime(int i){
		long time = 0;
		for(SplitEvent event : splitEvents){
			time += event.getTimes().get(i);
		}
		return time;
	}
	
	public long getBestSessionTime(){
		long time = Long.MAX_VALUE;
		for(int i=0; i<splitEvents.get(0).getTimes().size(); i++){
			time = Math.min(time, getSessionTime(i));
		}
		return time;
	}
	
	public long getSumOfBestEventTimes(){
		long time = 0;
		for(SplitEvent event:splitEvents){
			time+=event.getBestTime();
		}
		return time;
	}
	
	public ArrayList<SplitEvent> getSplitEvents(){
		return splitEvents;
	}
	
	public String getName(){
		return name;
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
			SplitCollection sp = (SplitCollection) object;
			if(sp.getSplitEvents().equals(splitEvents) && sp.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return splitEvents.hashCode()+name.hashCode();
	}
}
