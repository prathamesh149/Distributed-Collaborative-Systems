package shared.window;

import util.annotations.Tags;
import util.awt.AnExtendibleAWTEventQueue;
import util.awt.ExtendibleAWTEventQueue;
import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;

@Tags({ ApplicationTags.REPLICATED_WINDOW, InteractiveTags.COMPOSER })
public class AWindowComposerAndLauncher {
	public static final String DEFAULT_APPLICATION_NAME = "REPLICATED_WINDOW";

	protected Communicator communicator;
	protected PeerMessageListener aCoupler;
	protected AnEventHandlerAndForwarder forwarder;
	protected AnExtendibleAWTEventQueue queue;

	public String getApplicationName() {
		return DEFAULT_APPLICATION_NAME;
	}

	public static void main(String[] args) {
		(new AWindowComposerAndLauncher()).composeAndLaunch(args);
	}

	public void composeAndLaunch(String[] args) {
		compose(args);
		launch();
	}

	public void compose(String[] args) {
		communicator = createCommunicator(args);

		//getEventQueue() has the instantiation as well
		queue = AnExtendibleAWTEventQueue.getEventQueue();
		
		
		addCollaborationFunctions(queue);
		doJoin();
	}

	public Communicator createCommunicator(String args[]) {
		checkArgs(args);
		if (args.length == 5) {
			if (args[4].equalsIgnoreCase(Communicator.DIRECT))
				CommunicatorSelector.selectDirectCommunicator();
			else if (args[4].equalsIgnoreCase(Communicator.RELAYED))
				CommunicatorSelector.selectRelayerCommunicator();
		}
		return CommunicatorSelector.getCommunicator(args[0], args[1], args[2],
				args[3]);

	}

	public void checkArgs(String[] args) {
		if (args.length < 5) {
			System.out
					.println("Please supply server host name, session name,  user name and application name as main arguments");
			System.exit(-1);
		}
	}

	protected void addCollaborationFunctions(AnExtendibleAWTEventQueue queue) {
		addACoupler(queue);
	}

	protected void addACoupler(AnExtendibleAWTEventQueue queue) {
		
		aCoupler = new ACoupler(queue);
		communicator.addPeerMessageListener(aCoupler);
	}

	protected void doJoin() {
		communicator.join();
	}
	
	public Communicator getCommunicator() {
		return communicator;
	}
	
	public void launch() {
		
		forwarder = new AnEventHandlerAndForwarder(communicator);
		
		//This order matters whether to send first resize == creation event to broadcast or not
		(new UserWindow()).createAndShowFrames();
		queue.addEventQueueHandler(forwarder);
		
	}
}
