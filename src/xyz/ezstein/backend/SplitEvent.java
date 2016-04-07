package xyz.ezstein.backend;

import java.io.*;

import javafx.beans.property.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class SplitEvent implements Serializable {
	private SimpleStringProperty name;
	private SimpleLongProperty time;
	private SimpleLongProperty splitTime;
	private SimpleStringProperty icon;
	private SimpleIntegerProperty positionId;
	
	public SplitEvent(String name, long time, long splitTime, String icon, int positionId){
		this.name=new SimpleStringProperty(name);
		this.time=new SimpleLongProperty(time);
		this.splitTime=new SimpleLongProperty(splitTime);
		this.icon=new SimpleStringProperty(icon);
		this.positionId=new SimpleIntegerProperty(positionId);
	}
	
	public SplitEvent(){
		this("abc", 0,0,"123",0);
	}
	
	public SimpleStringProperty nameProperty(){
		
		return name;
	}
	public SimpleStringProperty iconProperty(){
		return icon;
	}
	public SimpleLongProperty timeProperty(){
		return time;
	}
	public SimpleLongProperty splitTimeProperty(){
		return splitTime;
	}
	public SimpleIntegerProperty positionIdProperty(){
		return positionId;
	}
	
	private void writeObject(ObjectOutputStream out) throws IOException {
		out.defaultWriteObject();
		out.writeObject(name.get());
		out.writeObject(icon.get());
		out.writeLong(time.get());
		out.writeLong(splitTime.get());
		out.writeInt(positionId.get());
	}
	
	private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		this.name = new SimpleStringProperty((String) in.readObject());
		this.icon = new SimpleStringProperty((String) in.readObject());
		this.time = new SimpleLongProperty(in.readLong());
		this.splitTime = new SimpleLongProperty(in.readLong());
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
					se.timeProperty().equals(time) &&
					se.splitTimeProperty().equals(splitTime) &&
					se.positionIdProperty().equals(positionId)){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()+icon.hashCode()+time.hashCode()+splitTime.hashCode()+positionId.hashCode();
	}
}