package locked.window;

import util.annotations.Tags;
import util.session.Communicator;
import util.tags.FunctionTags;

@Tags({FunctionTags.LOCK_CONTROLS})
public class AMasterLockModel {
	
	Communicator communicator;
	String lockHolder;
	
	public AMasterLockModel(Communicator theCommunicator ) {
		communicator = theCommunicator;
		lockHolder = "free";
	}
		
	public boolean isLocked() {
		return (!lockHolder.equals("free"));
	}
	
	//Returns the current local cache value
	public String getLockHolder() {
		return lockHolder;
	}

	//Sets the local cache value
	public void setLockHolder(String theLockHolder) {		
		lockHolder = theLockHolder;
	}
			
	public boolean hasLock(String theUser) {
		return theUser.equals(lockHolder);
	}
		
	public void freeLock() {		
		setLockHolder("free");
	}

	public void grantRequest(String user) {
		//Checks are done in Master Coupler
		setLockHolder(user);
		AMasterLockGrant grant = new AMasterLockGrant(user);
		communicator.toAll(grant);		
	}

	public void releaseRequest(String user) {
		//Checks are done in Master Coupler
		freeLock();
		AMasterLockRelease release = new AMasterLockRelease(user);
		communicator.toAll(release);
	}

	public void denyRequest(String user) {
		AMasterLockDenied denied = new AMasterLockDenied(user);
		communicator.toAll(denied);
	}
		
}