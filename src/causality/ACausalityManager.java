package causality;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import trace.causal.UserAddedToVectorTimeStamp;
import util.session.ReceivedMessage;
import util.session.SessionMessageListener;

public class ACausalityManager implements SessionMessageListener, ItemListener{
	
	private VectorTimeStamp myTimeStamp;
	private List<ReceivedMessage> orderedBuffer;
	private String myName;
	private boolean causal;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
	
	public ACausalityManager(String userName) {
		myTimeStamp = new VectorTimeStamp();		
		orderedBuffer = new ArrayList<ReceivedMessage>();
		myName = userName;
		causal = false;
	}
	
	public VectorTimeStamp getMyTimeStamp() {
		return myTimeStamp;
	}

	public void setMyTimeStamp(VectorTimeStamp myTimeStamp) {
		this.myTimeStamp = myTimeStamp;
	}

	public List<ReceivedMessage> getOrderedBuffer() {
		return orderedBuffer;
	}

	public String getMyName() {
		return myName;
	}
	
	public boolean isCausal() {
		return causal;
	}

	public int put(ReceivedMessage newMessage) {
		
		boolean inserted = false;
		int aPos = 0;
		VectorTimeStamp vector = ((MessageWithVectorTimeStamp)newMessage.getUserMessage()).getVectorTimeStamp();
		int size = orderedBuffer.size();
		
		if(size == 0) {
			orderedBuffer.add(newMessage);
			inserted = true;
			aPos = 0;
		}
		
		for (int i=0; i< size; i++) {
			if (vector.isLessThan(((MessageWithVectorTimeStamp)orderedBuffer.get(i).getUserMessage()).getVectorTimeStamp())) {
				orderedBuffer.add(i, newMessage);
				aPos = i;
				inserted = true;
				break;
			}	
	
		}
		
		if(!inserted) {
			VectorTimeStamp last = ((MessageWithVectorTimeStamp)orderedBuffer.get(size-1).getUserMessage()).getVectorTimeStamp();
			if(last.isLessThan(vector)) {
				orderedBuffer.add(size, newMessage);
				aPos = size;
				inserted = true;
			}
		}
		
		return aPos;
	}
	
	public void incMyTimeStamp() {
		myTimeStamp.inc(myName);
	}
			
	@Override 
	public void clientJoined(String arg0, String arg1, String arg2,
			boolean arg3, boolean arg4, Collection<String> arg5) {
		
		myTimeStamp.getVector().put(arg0, 0);
		UserAddedToVectorTimeStamp.newCase(myTimeStamp.deepCopy().getVector(), this);
	}
	
	@Override
	public void clientLeft(String arg0, String arg1) {
		myTimeStamp.getVector().remove(arg0);		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			//Resets the vector time stamp at that site
			causal = true;
			resetMyTimeStamp();
		} else {
			causal = false;			
			this.pcs.firePropertyChange("causality", "old", "new");
			
		}		
	}

	private void resetMyTimeStamp() {
		myTimeStamp.reset();		
	}
		
}
