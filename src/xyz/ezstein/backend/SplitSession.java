package xyz.ezstein.backend;

import java.io.*;

public class SplitSession implements Serializable {
	
	String name;
	int id;
	public SplitSession(String name, int id){
		this.name=name;
		this.id=id;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}

	@Override
	public boolean equals(Object o){
		if(o==null){
			return false;
		} else if(o==this){
			return true;
		} else if(o instanceof SplitSession){
			SplitSession ss = (SplitSession) o;
			if(ss.getName().equals(name) && ss.getId()==id){
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		return name.hashCode()+id*100;
	}
}
