package trace.locking;

import trace.control.CommunicatedControlInfo;
import util.session.CommunicatorSelector;

public class CopyOfMasterLockGrantRequestReceived extends CommunicatedControlInfo{

	public CopyOfMasterLockGrantRequestReceived(String aMessage, String aProcessName,   String aDestinationOrSource, Object aFinder) {
		super(aMessage, aProcessName,  aDestinationOrSource, aFinder);
	}
	public CopyOfMasterLockGrantRequestReceived(String aMessage, CommunicatedControlInfo anInfo) {
		super(aMessage, anInfo);
	}

	public static CopyOfMasterLockGrantRequestReceived toTraceable (String aMessage) {
		CommunicatedControlInfo anInfo = CommunicatedControlInfo.toTraceable(aMessage);
		return new CopyOfMasterLockGrantRequestReceived(aMessage, anInfo);
	}
	public static CopyOfMasterLockGrantRequestReceived newCase(
			String aProcessName,
			 String aSourceOrDestination, Object aFinder) {
			
		String aMessage = toString(aProcessName,  aSourceOrDestination);
		CopyOfMasterLockGrantRequestReceived retVal = new CopyOfMasterLockGrantRequestReceived(aMessage, aProcessName, aSourceOrDestination, aFinder);
		retVal.announce();
		return retVal;
	}
	public static CopyOfMasterLockGrantRequestReceived newCase(
			 String aSourceOrDestination, Object aFinder) {
		
		return newCase(CommunicatorSelector.getProcessName(), aSourceOrDestination, aFinder);
			
		
	}

	

}
