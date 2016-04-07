package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

public class SplitSession implements Serializable {
	private final ArrayList<SplitEvent> splitEvents;
	private final String name;
	
	public SplitSession(String name, ArrayList<SplitEvent> splitEvents){
		this.name=name;
		this.splitEvents=splitEvents;
	}
	public SplitSession(){
		this("default", new ArrayList<SplitEvent>());
	}
	
	public long getTime(){
		long time = 0;
		for(SplitEvent event : splitEvents){
			time += event.timeProperty().get();
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
		if(object instanceof SplitSession){
			SplitSession sp = (SplitSession) object;
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
