package ot;

public class AMessageWithOTTimeStamp implements MessageWithOTTimeStamp{

	Object message;
	int[] otTimeStamp;
	String user;
	boolean server;
	
	public AMessageWithOTTimeStamp(Object message, int[] otTimeStamp, String user){
		this.message = message;
		this.otTimeStamp = otTimeStamp;
		this.user = user;
		//server = false;
	}
	
	@Override
	public Object getMessage() {
		return message;
	}

	@Override
	public int[] getOTTimeStamp() {
		return otTimeStamp;
	}

	@Override
	public void setOTTimeStamp(int[] otTimeStamp) {
		this.otTimeStamp = otTimeStamp;		
	}

	@Override
	public void setMessage(Object message) {
		this.message = message;
	}

	@Override
	public String getUserName() {
		return user;
	}

	@Override
	public void setUserName(String user) {
		this.user = user;
	}

	@Override
	public boolean isServer() {
		return server;
	}
	
	@Override
	public void setServer(boolean server) {
		this.server = server;
	}

}
