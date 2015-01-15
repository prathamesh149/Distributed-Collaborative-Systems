package locked.window;

import shared.window.AWindowComposerAndLauncher;
import shared.window.ExampleRWSession;
import trace.locking.LockTracerSetter;
import trace.sharedWindow.SharedWindowTracerSetter;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.DistributedTags;
import util.tags.FunctionTags;
import util.trace.Tracer;

@Tags({ DistributedTags.CLIENT_1, ApplicationTags.REPLICATED_WINDOW, FunctionTags.CONCURRENCY_CONTROLLED })
public class AliceMasterSlaveIM implements ExampleRWSession {
	public static final String USER_NAME = DistributedTags.CLIENT_1;

	public static void main(String[] args) {
		String[] launcherArgs = { SESSION_SERVER_HOST, SESSION_NAME, USER_NAME,
				APPLICATION_NAME, Communicator.RELAYED };
		Tracer.showInfo(true);
		LockTracerSetter.traceLock();
		
		// Tracer.setKeywordPrintStatus(ClientJoinFinished.class, true);
		// SessionTracerSetter.setSessionPrintStatus();

		// SessionTracerSetter.traceSession();
		// Tracer.setKeywordPrintStatus(ClientJoined.class, true);
		(new AMasterSlaveLockingWindowComposerAndLauncher()).composeAndLaunch(launcherArgs);
	}

}
