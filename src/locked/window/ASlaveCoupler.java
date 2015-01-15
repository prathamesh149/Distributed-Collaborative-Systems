package locked.window;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

import trace.locking.SlaveLockGrantReceived;
import trace.locking.SlaveLockGranted;
import trace.locking.SlaveLockReleaseReceived;
import trace.locking.SlaveLockReleased;
import trace.locking.SlaveMyLockGrantMadeReceived;
import trace.locking.SlaveMyLockGrantRequestReceived;
import util.session.PeerMessageListener;

public class ASlaveCoupler implements PeerMessageListener{

	ALocalModelCache cache;
	
	public ASlaveCoupler(ALocalModelCache theCache) {
		cache = theCache;
	}
	
	@Override
	public void objectReceived(Object message, String arg1) {
		if (message instanceof SlaveLockGrantRequestSentToNotify) {
			processReceivedSlaveLockGrantRequest((SlaveLockGrantRequestSentToNotify)message);
		} else if (message instanceof AMasterLockGrant) {
			processReceivedMasterLockGrant((AMasterLockGrant)message);
		} else if (message instanceof AMasterLockRelease) {
			processReceivedMasterLockRelease((AMasterLockRelease)message);
		} else if (message instanceof AMasterLockDenied) {
			processReceivedMasterLockDenied((AMasterLockDenied)message);
		}
		
	}
	
	private void processReceivedSlaveLockGrantRequest(SlaveLockGrantRequestSentToNotify message) {
		//Notify if you have the lock and other user is requesting
		String user = message.getUser();
		String localUser = cache.getLocalUser();
		
		
		if ( (!localUser.equals(user)) && cache.hasLock(localUser)) {
			SlaveMyLockGrantRequestReceived.newCase(user, this);
			
			
			Object[] options = {"OK"};
			String notification = user + " is requesting the lock you hold";
			
		    JOptionPane.showOptionDialog(null,
		                   notification,"Notification",
		                   JOptionPane.PLAIN_MESSAGE,
		                   JOptionPane.QUESTION_MESSAGE,
		                   null,
		                   options,
		                   options[0]);	
		}
		
	}
	
	private void processReceivedMasterLockGrant(AMasterLockGrant message) {
		String user = message.getUser();
		String localUser = cache.getLocalUser();
		
		SlaveLockGrantReceived.newCase(user, this);
		
		SlaveLockGranted.newCase(user, this);
		
		if (localUser.equals(user)) {
			
			SlaveMyLockGrantMadeReceived.newCase(localUser, this);
			
			
			cache.pendingRequest = false;
			cache.updateLockholder(user);
			
		} else {
			cache.updateLockholder(user);
		}
				
	}
	
	private void processReceivedMasterLockRelease(AMasterLockRelease message) {
		
		String user = message.getUser();
		
		SlaveLockReleaseReceived.newCase(cache.communicator.getClientName(), user, this);
		//System.out.println("At Master: " + user + "requested a release");
		
		//Should we check the user or just free?
		cache.updateFreeLock(user);
		
		SlaveLockReleased.newCase(user, this);
		
		//Update UI
		
	}
	
	private void processReceivedMasterLockDenied(AMasterLockDenied message) {
		String user = message.getUser();
		String localUser = cache.getLocalUser();
		
		if (localUser.equals(user)) {
			//My request was denied
			cache.pendingRequest = false;
			//System.out.println("Lock request by " + localUser + " got denied");
		}
		
	}
	
}