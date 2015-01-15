package causality;

public class AMessageWithVectorTimeStamp implements MessageWithVectorTimeStamp{

	Object message;
	VectorTimeStamp vectorTimeStamp;
	
	public AMessageWithVectorTimeStamp(Object message, VectorTimeStamp vectorTimeStamp) {
		this.message = message;
		this.vectorTimeStamp = vectorTimeStamp;
	}
	
	public Object getMessage() {
		return message;
	}
	
	public VectorTimeStamp getVectorTimeStamp() {
		return vectorTimeStamp;
	}
	
}
