package xyz.ezstein.backend;

import java.io.*;
import java.util.*;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SplitEvent implements Serializable {
	private SimpleStringProperty name;
	private SimpleStringProperty icon;
	private SimpleIntegerProperty positionId;
	private HashMap<Integer, Long> times;
	
	public SplitEvent(String name, String icon, int positionId){
		this.name=new SimpleStringProperty(name);
		this.icon=new SimpleStringProperty(icon);
		this.positionId=new SimpleIntegerProperty(positionId);
		times=new HashMap<Integer, Long>();
	}
	
	public SplitEvent(){
		this("abc","123",0);
	}
	
	public SimpleStringProperty nameProperty(){
		
		return name;
	}
	public SimpleStringProperty iconProperty(){
		return icon;
	}
	public SimpleLongProperty timeProperty(int i){
		return new SimpleLongProperty(times.get(i));
	}
	
	public long getBestTime(){
		long time = Long.MAX_VALUE;
		for(long t : times.values()){
			time=Math.min(time, t);
		}
		return time;
	}
	
	public SimpleIntegerProperty positionIdProperty(){
		return positionId;
	}
	
	public HashMap<Integer, Long> getTimes(){
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
}