package causality;

import trace.causal.CausalTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;
import im.AliceIM;
import im.IMClientComposerAndLauncher;

@Tags({DistributedTags.CLIENT_1, ApplicationTags.IM, DistributedTags.CAUSAL})
public class AliceVectorTimeStampedIM extends AliceIM{ 
	
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
		composerAndLauncher.getCommunicator().setMinimumDelayToPeer("Cathy", 20000);
		composerAndLauncher.getCommunicator().setMinimumDelayToPeer("David", 40000);
		
		
		composerAndLauncher.launch();
	
	}

}
