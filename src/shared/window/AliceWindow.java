package shared.window;

import trace.im.IMTracerSetter;
import trace.sharedWindow.SharedWindowTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.trace.Tracer;
import util.trace.session.ClientJoinFinished;
import util.trace.session.ServerClientJoined;
import util.trace.session.SessionTracerSetter;

@Tags({ DistributedTags.CLIENT_1, ApplicationTags.REPLICATED_WINDOW })
public class AliceWindow implements ExampleRWSession {
	public static final String USER_NAME = DistributedTags.CLIENT_1;

	public static void main(String[] args) {
		String[] launcherArgs = { SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,
				APPLICATION_NAME, Communicator.RELAYED };
		Tracer.showInfo(true);
		SharedWindowTracerSetter.traceSharedWindow();
		// Tracer.setKeywordPrintStatus(ClientJoinFinished.class, true);
		// SessionTracerSetter.setSessionPrintStatus();

		// SessionTracerSetter.traceSession();
		// Tracer.setKeywordPrintStatus(ClientJoined.class, true);
		(new AWindowComposerAndLauncher()).composeAndLaunch(launcherArgs);
	}

}