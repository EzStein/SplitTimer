package xyz.ezstein.backend;

import java.io.*;
import java.nio.file.*;
import java.util.*;

import javafx.beans.property.*;
import javafx.collections.*;
import xyz.ezstein.backend.util.Util;
import xyz.ezstein.fx.observable.SplitTimeType;

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
	private final ArrayList<SplitSession> splitSessions;
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
		this.splitSessions=new ArrayList<SplitSession>();
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
		return Collections.unmodifiableSortedSet(new TreeSet<SplitSession>(splitSessions));
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
	
	/*
	 * FIX
	 */
	private SplitSession getBestSession(){
		SplitSession session= splitSessions.get(0);
		for(int i=0; i<splitSessions.size();i++){
			SplitSession ss=splitSessions.get(i);
			if(ss.isComplete()){
				if(getSessionTime(ss)<getSessionTime(session)){
					session = ss;
				}
			}
		}
		if(session.isComplete()==false){
			System.out.println("ERROR");
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
		splitSessions.add(session);
		for(SplitEvent event:splitEvents){
			event.putSession(session,-1);
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
		splitEvents.get(eventIndex).currentSplitTimeProperty().setSplitTime(time-bestTime);
		long dif = time-bestTime;
		long eventTime = time-getSumOfTimeUntilEvent(eventIndex-1, splitSessions.size()-1);
		
		long bestEventTime = Long.MAX_VALUE;
		for(SplitSession ss:splitSessions){
			if(ss.isComplete()){
				bestEventTime = Math.min(splitEvents.get(eventIndex).getTime(ss),bestEventTime);
			}
		}

		SplitTimeType type;
		if(time-bestTime<0 && eventTime-bestEventTime<0){
			type= SplitTimeType.LN;
		} else if(time-bestTime>0 && eventTime-bestEventTime<0){
			type= SplitTimeType.LP;
		} else if(time-bestTime<0 && eventTime-bestEventTime>0){
			type= SplitTimeType.GN;
		} else if(time-bestTime>0 && eventTime-bestEventTime>0){
			type= SplitTimeType.GP;
		} else {
			type= SplitTimeType.NEUTRAL;
		}
		
		splitEvents.get(eventIndex).currentSplitTimeProperty().setType(type);
	}
	
	
	
	private long getSumOfTimeUntilEvent(int eventIndex, SplitSession ss){
		long time=0;
		for(int i =0; i<=eventIndex; i++){
			time+=splitEvents.get(i).getTime(ss);
			
		}
		return time;
	}
	
	public long getSumOfTimeUntilEvent(int eventIndex, int sessionIndex){
		
		return getSumOfTimeUntilEvent(eventIndex, splitSessions.get(sessionIndex));
	}
	
	/**
	 * Writes an update to a given splitEvent and splitSession pair.
	 * The input time must be the time up until the given event index.
	 * @param eventIndex
	 * @param sessionIndex 
	 * @param time
	 */
	public void updateSession(int eventIndex, int sessionIndex, long time){
		SplitEvent event = splitEvents.get(eventIndex);
		SplitSession session = splitSessions.get(sessionIndex);
		event.putSession(session, time-getSumOfTimeUntilEvent(eventIndex-1, sessionIndex));
		System.out.println(sessionIndex);
		boolean complete = true;
		for(SplitEvent se : splitEvents){
			if(se.getTime(session)<=0){
				complete=false;
			}
		}
		if(complete){
			session.setComplete(true);
		}
	}
	
	/**
	 * Creates a new event ensuring that all of the splitSessions currently in use are added to it.
	 * @param eventName
	 */
	public void newEvent(String eventName){
		SplitEvent event = new SplitEvent(eventName);
		splitEvents.add(event);
		
		for(SplitSession ss: splitSessions){
			event.putSession(ss, -1);
			ss.setComplete(false);
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
		out.writeObject(savePath.toString());
	}
	
	@SuppressWarnings("unchecked")
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.splitEvents = new SimpleListProperty<SplitEvent>(FXCollections.observableList((ArrayList<SplitEvent>)in.readObject()));
		this.savePath = Paths.get((String)in.readObject());
	}
}
