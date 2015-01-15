package locked.window;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyVetoException;
import java.beans.VetoableChangeListener;

import javax.swing.SwingUtilities;

import trace.control.UserActionDenied;
import trace.locking.SlaveLockGrantRequestMade;
import trace.locking.SlaveLockGrantRequestSent;
import trace.locking.SlaveLockReleaseRequestMade;
import trace.locking.SlaveLockReleaseRequestSent;
import trace.sharedWindow.AWTEventSent;
import util.annotations.Tags;
import util.awt.ASerializableAWTEvent;
import util.awt.AWTEventQueueHandler;
import util.awt.AWTMisc;
import util.awt.ComponentRegistry;
import util.awt.SerializableAWTEvent;
import util.session.Communicator;
import util.tags.FunctionTags;

@Tags({FunctionTags.LOCK_CONTROLS})
public class ALocalModelCache implements VetoableChangeListener, ActionListener, ItemListener, AWTEventQueueHandler{
	
	Communicator communicator;
	String lockHolder;
	boolean implicitLocking;
	boolean pendingRequest;
	LockingUserWindow window;
	
	public ALocalModelCache(Communicator theCommunicator ) {
		communicator = theCommunicator;
		implicitLocking = false;
		pendingRequest = false;
		lockHolder = "free";
	}
	
	public String getLocalUser() {
		return communicator.getClientName();
	}
	
	//Returns the current local cache value
	public String getLockHolder() {
		return lockHolder;
	}

	//Sets the local cache value
	public void setLockHolder(String theLockHolder) {
		if (theLockHolder.equals(lockHolder))
			return;
		lockHolder = theLockHolder;
	}
			
	public boolean hasLock(String theUser) {
		return theUser.equals(lockHolder);
	}
	
	
	public void freeLock(String theUser) {
		if(hasLock(theUser)) {
			lockHolder = "free";
		}
	}
	
	
	@Override
	public void vetoableChange(PropertyChangeEvent evt) throws PropertyVetoException {
		//check if the local user has the lock and veto the writes if it doesnt have 
		String localUser = getLocalUser();
		
		AWTEvent aEvent = (AWTEvent)evt.getNewValue();
		Component theComponent = (Component)aEvent.getSource();
		String windowName = SwingUtilities.getRoot(theComponent).getName();
		
		//****** Implement hasLock instead of getLock *****************
		if (!windowName.equals("AReplicatedLockController")) {						
			
			if(!hasLock(localUser)) {
				
				if (!implicitLocking) {
					UserActionDenied.newCase(this);
					String message = communicator.getClientName() + ", you dont have the lock";
					throw new PropertyVetoException(message, evt);	
				} else if (implicitLocking && (!getLockHolder().equals("free"))) {
					UserActionDenied.newCase(this);
					String message = communicator.getClientName() + ", you dont have the lock";
					throw new PropertyVetoException(message, evt);	
				}				
			}						 
		}		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String localUser = getLocalUser();
		
		if (e.getActionCommand().equalsIgnoreCase("Request Lock")) {
			SlaveLockGrantRequestMade.newCase(localUser,this);
			
			if (!hasLock(localUser)) {
				
				if (lockHolder.equals("free")) {
				
					//Send to Master and others as well
					SlaveLockGrantRequestSent.newCase(localUser, this);
					
					ASlaveLockGrantRequest slaveRequest = new ASlaveLockGrantRequest(localUser);
					communicator.toAll(slaveRequest);
					
				} else {
					SlaveLockGrantRequestSent.newCase(localUser, this);
					//Send to others to only notify them that I am requesting the lock
					SlaveLockGrantRequestSentToNotify slaveRequest = new SlaveLockGrantRequestSentToNotify(localUser);
					communicator.toAll(slaveRequest);
				}
				
								
			}
			
		} else if (e.getActionCommand().equalsIgnoreCase("Release Lock")) {
			SlaveLockReleaseRequestMade.newCase(localUser, this);
			
			if(hasLock(localUser)) {
				//Local user has lock release and inform all
				freeLock(localUser);
				
				SlaveLockReleaseRequestSent.newCase(localUser, this);
				
				ASlaveLockReleaseRequest slaveRequest = new ASlaveLockReleaseRequest(localUser);
				communicator.toAll(slaveRequest);
				
			}
			
		}		
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			implicitLocking = true;
		} else {
			implicitLocking = false;
		}
	}

	@Override
	public void newEvent(AWTEvent aEvent) {
		
		Component theComponent = (Component)aEvent.getSource();
		
		//Implement the window filter feature here
		String windowName = SwingUtilities.getRoot(theComponent).getName();
		if (windowName.equals("AReplicatedLockController")) {
			return;
		}
		
		//Generate request if implicit locking
		String localUser = getLocalUser(); 
		if (implicitLocking && !pendingRequest) {
			
			if(AWTMisc.isKeyEvent(aEvent) || AWTMisc.isMouseEvent(aEvent)) {
				
				
				//As we have vetoed the cache is not free
				//only events for the case when !hasLock(localUser) and !free come here
				
				if (!hasLock(localUser) ) {
					//Local user doesnt have the lock, send request to all
					SlaveLockGrantRequestMade.newCase(localUser,this);
					
					
					ASlaveLockGrantRequest slaveRequest = new ASlaveLockGrantRequest(localUser);
					SlaveLockGrantRequestSent.newCase(localUser, this);
					
					communicator.toAll(slaveRequest);
					pendingRequest = true;
				}
				
			}			
		}
		
		try {
		int id = ComponentRegistry.getComponentId(theComponent);
		//Send to others
			if (id != -1) {
				if (communicator == null) return;		
				SerializableAWTEvent event = new ASerializableAWTEvent(aEvent, Integer.toString(id));
				
				communicator.toOthers(event);
			}	
		} catch(Exception e) {
			System.out.println("Component not registered");
		}
		
	}

	public void setWindow(LockingUserWindow window) {
		this.window = window;		
	}

	public void updateLockholder(String user) {
		setLockHolder(user);
		//Update UI
		updateLockHolderUI(user);
	}

	

	public void updateFreeLock(String user) {
		freeLock(user);
		updateLockHolderUI("free");
		
		
	}
	
	private void updateLockHolderUI(String value) {
		Runnable runnable = new Runnable() {
			
			@Override
			public void run() {
				window.getLockController().lockHolder.setText(value);
				
			}
		};
		SwingUtilities.invokeLater(runnable);
	}
}