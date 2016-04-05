package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

public class SplitSession implements Serializable {
	private ArrayList<SplitEvent> splitEvents;
	private String name;
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
}
