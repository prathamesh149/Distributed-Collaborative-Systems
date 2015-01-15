package locked.window;

import trace.locking.MasterLockGrantRequestDenied;
import trace.locking.MasterLockGrantRequestReceived;
import trace.locking.MasterLockGrantStatusSent;
import trace.locking.MasterLockGranted;
import trace.locking.MasterLockReleaseRequestReceived;
import trace.locking.MasterLockReleaseStatusSent;
import trace.locking.MasterLockReleased;
import util.session.PeerMessageListener;

public class AMasterCoupler implements PeerMessageListener{

	AMasterLockModel masterLock;
	
	public AMasterCoupler(AMasterLockModel masterLock) {
		this.masterLock = masterLock;
	}
	
	@Override
	public void objectReceived(Object message, String arg1) {
		if (message instanceof ASlaveLockGrantRequest) {
			processReceivedSlaveLockGrantRequest((ASlaveLockGrantRequest)message);
		} else if (message instanceof ASlaveLockReleaseRequest) {
			processReceivedSlaveLockReleaseRequest((ASlaveLockReleaseRequest)message);
		}
		
	}

	private void processReceivedSlaveLockGrantRequest(ASlaveLockGrantRequest message) {
		String user = message.getUser();
		
		
		MasterLockGrantRequestReceived.newCase(user, this);
		
				
		if (!masterLock.isLocked()) {
			MasterLockGranted.newCase(user, this);
			
			masterLock.grantRequest(user);
			
			MasterLockGrantStatusSent.newCase(user, this);
			
		} else {				
			MasterLockGrantRequestDenied.newCase(user, this);
			
			masterLock.denyRequest(user);
			
			MasterLockGrantStatusSent.newCase(user, this);
			
		}
		
	}
	
	private void processReceivedSlaveLockReleaseRequest(ASlaveLockReleaseRequest message) {
		String user = message.getUser();
		
		MasterLockReleaseRequestReceived.newCase(user,this);
		
		//Implementing hasLock instead of getLock ******
		if (masterLock.hasLock(user)) {
			MasterLockReleased.newCase(user, this);
			
			masterLock.releaseRequest(user);
			
			MasterLockReleaseStatusSent.newCase(user,this);
		}
		
	}

}
