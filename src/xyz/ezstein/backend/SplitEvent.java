package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

import javafx.beans.property.*;
import javafx.beans.value.*;
import xyz.ezstein.fx.observable.*;

/**
 * Represents a split event that can be viewed in a table.
 * Each property is represented by an observable value so that it can be updated immediatly by the gui.
 * It stores a:
 * name
 * icon
 * treemap of times that associated a split time with its actual value. The time represents the time to from the start of the event to the end.
 * a displayable currentTimeProperty
 * a displayable currentSplitTimeProperty
 * @author Ezra Stein
 * @since 2016
 * @version 1.0
 */
public class SplitEvent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private transient SimpleStringProperty name;
	private transient SimpleStringProperty icon;
	private final TreeMap<SplitSession, Long> times;
	private transient SimpleLongProperty currentTimeProperty;
	private transient SimpleSplitTimeProperty currentSplitTimeProperty;
	
	/**
	 * Constructs a split event with a name and icon;
	 * @param name
	 * @param icon
	 */
	public SplitEvent(String name, String icon){
		this.name=new SimpleStringProperty(name);
		this.icon=new SimpleStringProperty(icon);
		times=new TreeMap<SplitSession, Long>();
		currentTimeProperty = new SimpleLongProperty(0);
		currentSplitTimeProperty = new SimpleSplitTimeProperty(new SplitTime());
	}
	
	/**
	 * Default split event with "" name and "" icon
	 */
	public SplitEvent(){
		this("","");
	}
	
	/**
	 * Default Split event with given name.
	 * @param name
	 */
	public SplitEvent(String name){
		this(name, "");
	}
	
	/**
	 * Returns the name property for viewing.
	 * @return the name property for viewing
	 */
	public SimpleStringProperty nameProperty(){
		return name;
	}
	
	/**
	 * Returns the icon property for viewing only.
	 * @return the icon property for viewing only.
	 */
	public SimpleStringProperty iconProperty(){
		return icon;
	}
	
	/**
	 * Returns the currentTime property for edits by the splitCollection only.
	 * @return the currentTime property for edits by the splitCollection only.
	 */
	public SimpleLongProperty currentTimeProperty(){
		return currentTimeProperty;
	}
	
	/**
	 * Returns the currentSplitTime property for edits by the splitCollection.
	 * @return the currentSplitTime property for edits by the splitCollection.
	 */
	public SimpleSplitTimeProperty currentSplitTimeProperty(){
		return currentSplitTimeProperty;
	}
	
	/**
	 * Returns the best time across all splitsessions
	 * @return the best time across all splitsessions
	 */
	public long getBestTime(){
		long time = Long.MAX_VALUE;
		for(long t : times.values()){
			time=Math.min(time, t);
		}
		return time;
	}
	
	private SplitSession getBestSession(){
		ArrayList<SplitSession> splitSessions = new ArrayList<SplitSession>(times.navigableKeySet());
		SplitSession session= splitSessions.get(0);
		for(int i=0; i<splitSessions.size();i++){
			SplitSession ss=splitSessions.get(i);
			if(getTime(ss)<getTime(session)){
				session = ss;
			}
		}
		return session;
	}
	
	/**
	 * Returns a time for the given split session
	 * @param ss
	 * @return
	 */
	public long getTime(SplitSession ss){
		return times.get(ss);
	}
	
	/**
	 * Creates a new entry of splitsession with associated time.
	 * @param splitSession
	 * @param time
	 */
	public void putSession(SplitSession splitSession, long time){
		times.put(splitSession, time);
	}
	
	
	/**
	 * Private method used for comparison only
	 * @return
	 */
	private TreeMap<SplitSession, Long> getTimes(){
		return times;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(name.get());
		out.writeObject(icon.get());
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.name = new SimpleStringProperty((String) in.readObject());
		this.icon = new SimpleStringProperty((String) in.readObject());
		this.currentTimeProperty = new SimpleLongProperty(0);
		this.currentSplitTimeProperty = new SimpleSplitTimeProperty(new SplitTime());
	}
	
	@Override
	public boolean equals(Object object){
		if(object==null){
			return false;
		}
		if(object==this){
			return true;
		}
		if(object instanceof SplitEvent){
			SplitEvent se = (SplitEvent) object;
			if(se.nameProperty().equals(name) && 
					se.iconProperty().equals(icon) &&
					se.getTimes().equals(times)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()+icon.hashCode()+times.hashCode();
	}
	
	@Override
	public String toString(){
		String out =  name.get() + ":\t";
		for(SplitSession ss:times.keySet()){
			out+=ss+" (" + times.get(ss) + ")\t";
		}
		return out;
	}
	
}