package ot;
import trace.ot.OTIMTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;
import im.AliceIM;

@Tags({DistributedTags.CLIENT_1, ApplicationTags.IM, DistributedTags.OT})
public class AliceOTIM extends AliceIM {
	
	public static void main(String[] args) {
		Tracer.showInfo(true);
		OTIMTracerSetter.traceIM();
		
		OTTimeStampingIMComposerAndLauncher composerAndLauncher = new OTTimeStampingIMComposerAndLauncher();
		delayAndLaunch(composerAndLauncher);
	}
	
	private static void delayAndLaunch(OTTimeStampingIMComposerAndLauncher composerAndLauncher) {
		String[] launcherArgs = {SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,  APPLICATION_NAME,  Communicator.RELAYED};
		composerAndLauncher.compose(launcherArgs);

		//Add delay here
		//composerAndLauncher.getCommunicator().setMinimumDelayToServer(10000);
				
		composerAndLauncher.launch();
	
	}
	
}