package causality;

import trace.causal.CausalTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;
import im.DavidIM;
import im.IMClientComposerAndLauncher;

@Tags({DistributedTags.CLIENT_4, ApplicationTags.IM, DistributedTags.CAUSAL})
public class DavidVectorTimeStampedIM extends DavidIM{ 
	
	public static void main(String[] args) {
		Tracer.showInfo(true);
		CausalTracerSetter.traceCausal();
		AVectorTimeStampingIMComposerAndLauncher composerAndLauncher = new AVectorTimeStampingIMComposerAndLauncher();
		
		delayAndLaunch(composerAndLauncher);
	}

	private static void delayAndLaunch(IMClientComposerAndLauncher composerAndLauncher) {
		String[] launcherArgs = {SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,  APPLICATION_NAME,  Communicator.DIRECT};
		composerAndLauncher.compose(launcherArgs);

		//Add delay here
		composerAndLauncher.getCommunicator().setMinimumDelayToPeer("Bob", 20000);
		composerAndLauncher.launch();
	
	}

}
