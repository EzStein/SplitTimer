package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;

/**
 * Holds a collection of SplitEvents which define the order of events for the split timer to work with.
 * 
 * @author Ezra Stein
 * @version 1.0
 * @since 2016
 *
 */
public class SplitCollection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient SimpleListProperty<SplitEvent> splitEvents;
	private final ArrayList<SplitSession> splitSessions;
	private final String name;
	
	/**
	 * Constructs a SplitCollection
	 * @param name
	 * @param splitEvents
	 */
	public SplitCollection(String name, ArrayList<SplitEvent> splitEvents){
		this.name=name;
		this.splitEvents=new SimpleListProperty<SplitEvent>(FXCollections.observableList(splitEvents));
		this.splitSessions=new ArrayList<SplitSession>();
	}
	
	/**
	 * Constructs a SplitCollection with variable number of splitEvents
	 * @param name
	 * @param splitEvents
	 */
	public SplitCollection(String name, SplitEvent...splitEvents){
		this(name, new ArrayList<SplitEvent>(Arrays.asList(splitEvents)));
	}
	
	/**
	 * Constructs a default SplitCollection with no splitEvents
	 */
	public SplitCollection(){
		this("default", new ArrayList<SplitEvent>());
	}
	
	/**
	 * Constructs a SplitCollection with just a name and no splitEvents
	 * @param name
	 */
	public SplitCollection(String name){
		this(name, new ArrayList<SplitEvent>());
	}
	
	/**
	 * Gets the total time for a given session.
	 * @param splitSession
	 * @return The session time.
	 */
	public long getSessionTime(SplitSession splitSession){
		long time = 0;
		for(SplitEvent event : splitEvents){
			time += event.getTime(splitSession);
		}
		return time;
	}
	
	/**
	 * Searches all the splitSessions and and finds the one with the best total time.
	 * @return the best session time.
	 */
	public long getBestSessionTime(){
		long time = Long.MAX_VALUE;
		for(SplitSession ss:splitSessions){
			time = Math.min(time, getSessionTime(ss));
		}
		return time;
	}
	
	/**
	 * Returns the sum of best event times.
	 * @return the sum of best event times.
	 */
	public long getSumOfBestEventTimes(){
		long time = 0;
		for(SplitEvent event:splitEvents){
			time+=event.getBestTime();
		}
		return time;
	}
	
	
	public SimpleListProperty<SplitEvent> splitEventsProperty(){
		return splitEvents;
	}
	
	public String getName(){
		return name;
	}
	
	private ArrayList<SplitSession> getSplitSessions(){
		return splitSessions;
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
			if(sp.splitEventsProperty().equals(splitEvents)
					&& sp.getSplitSessions().equals(splitSessions)
					&& sp.getName().equals(name)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return splitEvents.hashCode()+name.hashCode() + splitSessions.hashCode();
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(new ArrayList<SplitEvent>(splitEvents));
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.splitEvents = new SimpleListProperty(FXCollections.observableList((ArrayList<SplitEvent>)in.readObject()));
	}
}
