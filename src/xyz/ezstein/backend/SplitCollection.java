package xyz.ezstein.backend;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import xyz.ezstein.backend.app.Locator;

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
	private Path savePath;
	private transient SimpleListProperty<SplitEvent> splitEvents;
	private final HashMap<String, SplitSession> splitSessions;
	private final String name;
	
	/**
	 * Constructs a SplitCollection
	 * @param name
	 * @param splitEvents
	 * @param savePath 
	 */
	public SplitCollection(String name, ArrayList<SplitEvent> splitEvents,Path savePath){
		this.savePath=savePath;
		this.name=name;
		this.splitEvents=new SimpleListProperty<SplitEvent>(FXCollections.observableList(splitEvents));
		this.splitSessions=new HashMap<String,SplitSession>();
		this.splitEvents.addListener(new ListChangeListener<SplitEvent>(){
			@Override
			public void onChanged(ListChangeListener.Change<? extends SplitEvent> c) {
				while(c.next()){
					if(c.wasAdded()){
						for(SplitEvent se: c.getAddedSubList()){
							for(SplitSession ss: splitSessions.values()){
								se.putSession(ss, -1);
							}
						}
					}
				}
			}
		});
	}
	
	/**
	 * Constructs a SplitCollection with variable number of splitEvents
	 * @param name
	 * @param splitEvents
	 */
	public SplitCollection(String name, SplitEvent...splitEvents) {
		this(name, new ArrayList<SplitEvent>(Arrays.asList(splitEvents)));
	}
	
	/**
	 * Constructs a SplitCollection with variable number of splitEvents
	 * @param name
	 * @param splitEvents
	 * @throws IOException 
	 */
	public SplitCollection(String name, ArrayList<SplitEvent> splitEvents) {
		this(name, splitEvents,Paths.get(System.getProperty("user.home")+"/tmp.sr"));
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
	
	public Path getSavePath(){
		return savePath;
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
		for(SplitSession ss:splitSessions.values()){
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
	
	
	
	/**
	 * Constructs a new SplitSession and adds it to all events. It then changes the current session.
	 * @param sessionName
	 */
	public void newSession(String sessionName){
		SplitSession session = new SplitSession(sessionName);
		splitSessions.put(sessionName,session);
		for(SplitEvent event:splitEvents){
			event.putSession(session,-1);
		}
	}
	
	public void changeDisplayedTime(int eventIndex, long time){
		SplitEvent event = splitEvents.get(eventIndex);
		event.currentTimeProperty().set(time);
	}
	
	public void updateSession(String sessionName, int eventIndex, long time){
		SplitEvent event = splitEvents.get(eventIndex);
		SplitSession session = splitSessions.get(sessionName);
		event.putSession(session, time);
	}
	
	public void newEvent(String eventName){
		SplitEvent event = new SplitEvent(eventName);
		splitEvents.add(event);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	@Override
	public String toString(){
		String out = name+":\n";
		//for(SplitEvent se: splitEvents){
		//	out+="\t"+se+"\n";
		//}
		out+=splitEvents.stream().reduce("", (s, se)->{
			return s + se.toString() + "\n";
		}, (s1,s2)->{
			return s1+s2;
		});
		return out;
	}
	
	private Map<String,SplitSession> getSplitSessions(){
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
