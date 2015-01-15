package causality.sharedWindow;

import shared.window.AliceWindow;
import util.session.Communicator;

public class AliceVectorTimeStampedWindow extends AliceWindow {
	public static void main(String[] args) {
		AVectorTimeStampingWindowComposerAndLauncher composerAndLauncher = new AVectorTimeStampingWindowComposerAndLauncher();
		
		delayAndLaunch(composerAndLauncher);
	}

	private static void delayAndLaunch(AVectorTimeStampingWindowComposerAndLauncher composerAndLauncher) {
		String[] launcherArgs = {SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,  APPLICATION_NAME,  Communicator.DIRECT};
		composerAndLauncher.compose(launcherArgs);

		//Add delay here
		composerAndLauncher.getCommunicator().setMinimumDelayToPeer("Cathy", 20000);
		composerAndLauncher.getCommunicator().setMinimumDelayToPeer("David", 40000);
		
		
		composerAndLauncher.launch();	
	}
}
