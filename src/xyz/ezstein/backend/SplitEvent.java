package xyz.ezstein.backend;

import java.io.*;

import javafx.beans.property.*;

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
}