package ot;

import java.io.Serializable;

public interface MessageWithOTTimeStamp extends Serializable {
	public abstract Object getMessage();	
	
	public abstract void setMessage(Object Message);	
	
	public abstract int[] getOTTimeStamp();
	
	public abstract void setOTTimeStamp(int[] t);
	
	public abstract String getUserName();
	
	public abstract void setUserName(String user);
	
	public abstract boolean isServer();
	
	public abstract void setServer(boolean value);
}
