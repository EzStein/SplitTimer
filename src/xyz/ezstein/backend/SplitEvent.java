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
	private transient SimpleIntegerProperty positionId;
	private HashMap<SplitSession, Long> times;
	private transient SimpleLongProperty currentTime;
	
	public SplitEvent(String name, String icon, int positionId){
		this.name=new SimpleStringProperty(name);
		this.icon=new SimpleStringProperty(icon);
		this.positionId=new SimpleIntegerProperty(positionId);
		times=new HashMap<SplitSession, Long>();
		currentTime = new SimpleLongProperty();
	}
	
	public SplitEvent(){
		this("","",0);
	}
	
	public void putSession(SplitSession splitSession, long time){
		times.put(splitSession, time);
		
	}
	
	public SimpleStringProperty nameProperty(){
		return name;
	}
	public SimpleStringProperty iconProperty(){
		return icon;
	}
	public SimpleLongProperty currentTimeProperty(){
		return currentTime;
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
	
	public SimpleIntegerProperty positionIdProperty(){
		return positionId;
	}
	
	private HashMap<SplitSession, Long> getTimes(){
		return times;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(name.get());
		out.writeObject(icon.get());
		out.writeInt(positionId.get());
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.name = new SimpleStringProperty((String) in.readObject());
		this.icon = new SimpleStringProperty((String) in.readObject());
		this.positionId = new SimpleIntegerProperty(in.readInt());
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
					se.positionIdProperty().equals(positionId) &&
					se.getTimes().equals(times)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()+icon.hashCode()+times.hashCode()+positionId.hashCode();
	}
	
	@Override
	public String toString(){
		String out =  name.get() + ":\n";
		for(SplitSession ss:times.keySet()){
			out+=ss+": " + times.get(ss) + "\n";
		}
		out+="\n";
		out+=positionId.get()+"\n";
		return out;
	}
}