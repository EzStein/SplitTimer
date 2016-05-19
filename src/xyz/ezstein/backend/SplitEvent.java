package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

/**
 * Represents a split event that can be viewed in a table.
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
	private transient SimpleLongProperty currentSplitTimeProperty;
	
	public SplitEvent(String name, String icon){
		this.name=new SimpleStringProperty(name);
		this.icon=new SimpleStringProperty(icon);
		times=new TreeMap<SplitSession, Long>();
		currentTimeProperty = new SimpleLongProperty(0);
		currentSplitTimeProperty = new SimpleLongProperty(0);
	}
	
	public SplitEvent(){
		this("","");
	}
	
	public SplitEvent(String name){
		this(name, "");
	}
	
	
	
	public SimpleStringProperty nameProperty(){
		return name;
	}
	public SimpleStringProperty iconProperty(){
		return icon;
	}
	public SimpleLongProperty currentTimeProperty(){
		return currentTimeProperty;
	}
	public SimpleLongProperty currentSplitTimeProperty(){
		return currentSplitTimeProperty;
	}
	
	public long getBestTime(){
		long time = Long.MAX_VALUE;
		for(long t : times.values()){
			time=Math.min(time, t);
		}
		return time;
	}
	
	public long getTime(SplitSession ss){
		return times.get(ss);
	}
	
	public void putSession(SplitSession splitSession, long time){
		times.put(splitSession, time);
	}
	
	
	
	
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