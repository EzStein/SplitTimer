package xyz.ezstein.fx.observable;

import javafx.beans.property.*;

import java.util.*;

import javafx.beans.*;
import javafx.beans.value.*;
public class SimpleSplitTimeProperty implements ObservableValue<SplitTime> {
	private SplitTime splitTime;
	private Set<InvalidationListener> invalidationListeners;
	private Set<ChangeListener<? super SplitTime>> changeListeners;
	private SimpleSplitTimeProperty thisObject;
	public SimpleSplitTimeProperty(SplitTime splitTime){
		this.splitTime = splitTime;
		this.invalidationListeners=new HashSet<InvalidationListener>();
		this.changeListeners = new HashSet<ChangeListener<? super SplitTime>>();
		thisObject = this;
	}
	
	@Override
	public void addListener(InvalidationListener listener) {
		invalidationListeners.add(listener);
	}

	@Override
	public void removeListener(InvalidationListener listener) {
		invalidationListeners.remove(listener);
	}

	@Override
	public void addListener(ChangeListener<? super SplitTime> listener) {
		changeListeners.add(listener);
		
	}

	@Override
	public void removeListener(ChangeListener<? super SplitTime> listener) {
		changeListeners.remove(listener);
	}

	@Override
	public SplitTime getValue() {
		return splitTime;
	}
	
	public long getSplitTime(){
		return splitTime.getSplitTime();
	}
	public void setSplitTime(long splitTime) {
		SplitTime oldValue = this.splitTime;
		this.splitTime.setSplitTime(splitTime);
		invalidationListeners.stream().forEach((listener)->{
			listener.invalidated(thisObject);
		});
		changeListeners.stream().forEach((listener)->{
			listener.changed(thisObject, oldValue, this.splitTime);
		});
	}
	public SplitTimeType getType(){
		return splitTime.getType();
	}
	public void setType(SplitTimeType type){
		SplitTime oldValue = this.splitTime;
		splitTime.setType(type);
		invalidationListeners.stream().forEach((listener)->{
			listener.invalidated(thisObject);
		});
		changeListeners.stream().forEach((listener)->{
			listener.changed(thisObject, oldValue, this.splitTime);
		});
	}

}
