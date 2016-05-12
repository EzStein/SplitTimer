package xyz.ezstein.backend;

import java.io.*;
import java.time.*;
import java.util.*;

public class SplitSession implements Serializable, Comparable<SplitSession> {
	
	private final String name;
	private final int id;
	private final Instant date;
	private SplitSession(String name, int id){
		this.name=name;
		this.id=id;
		this.date=Instant.now();
	}
	
	public SplitSession(String name){
		this(name,UUID.randomUUID().hashCode());
	}
	
	public SplitSession(){
		this("DEFAULT");
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public Instant getDate(){
		return date;
	}

	@Override
	public boolean equals(Object o){
		if(o==null){
			return false;
		} else if(o==this){
			return true;
		} else if(o instanceof SplitSession){
			SplitSession ss = (SplitSession) o;
			if(ss.getName().equals(name) && ss.getId()==id && ss.getDate().equals(date)){
				
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()+id*100+date.hashCode()*-34;
	}

	@Override
	public int compareTo(SplitSession ss) {
		if(date.compareTo(ss.getDate())==0){
			if(ss.getId()==id&&ss.getName().equals(name)){
				return 0;
			} else {
				return ss.getName().compareTo(name);
			}		
		} else {
			return date.compareTo(ss.getDate());
		}
	}
	
	@Override
	public String toString(){
		return name;
	}
}
