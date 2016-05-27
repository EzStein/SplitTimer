package xyz.ezstein.fx.observable;

import javafx.beans.property.*;

public class SplitTime {
	private long splitTime;
	private SplitTimeType type;
	public SplitTime(long splitTime, SplitTimeType type){
		this.splitTime = splitTime;
		this.type=type;
	}
	public SplitTime(){
		this(0,SplitTimeType.NEUTRAL);
	}
	public long getSplitTime(){
		return splitTime;
	}
	public void setSplitTime(long splitTime){
		this.splitTime=splitTime;
	}
	public SplitTimeType getType(){
		return type;
	}
	public void setType(SplitTimeType type){
		this.type=type;
	}

	@Override
	public boolean equals(Object o){
		if(o==null){
			return false;
		}
		if(o==this){
			return true;
		}
		if(o instanceof SplitTime){
			SplitTime t = (SplitTime)o;
			if(t.getType()==type && t.getSplitTime()==splitTime){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return (int) (splitTime % Integer.MAX_VALUE + type.hashCode());
	}
}
