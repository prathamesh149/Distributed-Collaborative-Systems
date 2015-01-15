package causality.sharedWindow;

import shared.window.CathyWindow;
import util.session.Communicator;

public class CathyVectorTimeStampedWindow extends CathyWindow {
	public static void main(String[] args) {
		AVectorTimeStampingWindowComposerAndLauncher composerAndLauncher = new AVectorTimeStampingWindowComposerAndLauncher();
		
		delayAndLaunch(composerAndLauncher);
	}

	private static void delayAndLaunch(AVectorTimeStampingWindowComposerAndLauncher composerAndLauncher) {
		String[] launcherArgs = {SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,  APPLICATION_NAME,  Communicator.DIRECT};
		composerAndLauncher.compose(launcherArgs);

		//Add delay here
		
		
		
		composerAndLauncher.launch();	
	}
}
