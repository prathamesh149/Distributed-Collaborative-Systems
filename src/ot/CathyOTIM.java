package ot;
import im.CathyIM;
import trace.ot.OTIMTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;

@Tags({DistributedTags.CLIENT_3, ApplicationTags.IM, DistributedTags.OT})
public class CathyOTIM extends CathyIM {
	
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
		composerAndLauncher.getCommunicator().setMinimumDelayToServer(10000);
		composerAndLauncher.launch();
	
	}
	
}