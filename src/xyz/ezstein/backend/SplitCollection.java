package xyz.ezstein.backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import xyz.ezstein.backend.util.Util;

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
	private transient Path savePath;
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
		/*this.splitEvents.addListener(new ListChangeListener<SplitEvent>(){
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
		});*/
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
	 */
	public SplitCollection(String name, ArrayList<SplitEvent> splitEvents) {
		this(name, splitEvents,null);
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
	 * Returns the save path of this splitCollection
	 * @return the save path of this splitCollection
	 */
	public Path getSavePath(){
		return savePath;
	}
	
	/**
	 * Sets the save path
	 * @param path
	 */
	public void setSavePath(Path path){
		savePath=path;
	}
	
	/**
	 * Returns a read only sorted set of splitSessions
	 * @return a read only sorted set of splitSessions
	 */
	public Set<SplitSession> getUnmodifiableSplitSessions(){
		return Collections.unmodifiableSortedSet(new TreeSet<SplitSession>(splitSessions.values()));
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
	
	/*
	 * FIX THIS METHOD!!!!!!!!!!!!!!!
	 */
	private SplitSession getBestSession(){
		SplitSession session=new ArrayList<SplitSession>(splitSessions.values()).get(0);
		for(SplitSession ss:splitSessions.values()){
			if(getSessionTime(ss)<getSessionTime(session)){
				session = ss;
			}
		}
		return session;
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
	
	/**
	 * Returns a list of splitEvents as an observable value. Consider attempting to make this unmodifiable.
	 * @return a list of splitEvents.
	 */
	public SimpleListProperty<SplitEvent> splitEventsProperty(){
		return splitEvents;
	}
	
	/**
	 * Returns the name
	 * @return the name
	 */
	public String getName(){
		return name;
	}
	
	
	
	/**
	 * Constructs a new SplitSession and adds it to all events.
	 * @param sessionName
	 */
	public void newSession(String sessionName){
		SplitSession session = new SplitSession(sessionName);
		splitSessions.put(sessionName,session);
		for(SplitEvent event:splitEvents){
			event.putSession(session,Long.MAX_VALUE);
		}
	}
	
	/**
	 * Changes the time displayed by a given splitEvent
	 * @param eventIndex
	 * @param time
	 */
	public void changeDisplayedTime(int eventIndex, long time){
		SplitEvent event = splitEvents.get(eventIndex);
		event.currentTimeProperty().set(time);
	}
	
	public void changeDisplayedSplitTime(int eventIndex, long time){
		long bestTime = getSumOfTimeUntilEvent(eventIndex, getBestSession());
		System.out.println(eventIndex+ " " + Util.nanosToReadable( bestTime));
		splitEvents.get(eventIndex).currentSplitTimeProperty().set(time-bestTime);
	}
	
	private long getSumOfTimeUntilEvent(int eventIndex, SplitSession ss){
		long time=0;
		for(int i =0; i<=eventIndex; i++){
			time+=splitEvents.get(i).getTime(ss);
		}
		return time;
	}
	
	public long getSumOfTimeUntilEvent(int eventIndex, String splitSession){
		return getSumOfTimeUntilEvent(eventIndex, splitSessions.get(splitSession));
	}
	
	/**
	 * Writes an update to a given splitEvent and splitSession pair.
	 * @param sessionName
	 * @param eventIndex
	 * @param time
	 */
	public void updateSession(String sessionName, int eventIndex, long time){
		SplitEvent event = splitEvents.get(eventIndex);
		SplitSession session = splitSessions.get(sessionName);
		event.putSession(session, time);
	}
	
	/**
	 * Creates a new event ensuring that all of the splitSessions currently in use are added to it.
	 * @param eventName
	 */
	public void newEvent(String eventName){
		SplitEvent event = new SplitEvent(eventName);
		splitEvents.add(event);
		for(SplitSession ss: splitSessions.values()){
			event.putSession(ss, -1);
		}
		
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
		out.writeObject(savePath.toString());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.splitEvents = new SimpleListProperty<SplitEvent>(FXCollections.observableList((ArrayList<SplitEvent>)in.readObject()));
		this.savePath = Paths.get((String)in.readObject());
	}
}
