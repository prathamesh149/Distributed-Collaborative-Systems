package im;

import trace.im.IMTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;

@Tags({DistributedTags.CLIENT_4, ApplicationTags.IM})
public class DavidIM implements ExampleIMSession{
	public static final String USER_NAME = DistributedTags.CLIENT_4;
	public static void main (String[] args) {
		String[] launcherArgs = {SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,  APPLICATION_NAME,  Communicator.DIRECT};
		Tracer.showInfo(true);
		IMTracerSetter.traceIM();
//		Tracer.setKeywordPrintStatus(ClientJoinFinished.class, true);

//		SessionTracerSetter.setSessionPrintStatus();

		(new AnIMClientComposerAndLauncher()).composeAndLaunch(launcherArgs);
	}
	
}

