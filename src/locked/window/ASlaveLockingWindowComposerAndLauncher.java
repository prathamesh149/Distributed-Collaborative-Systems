package locked.window;

import shared.window.ACoupler;
import shared.window.AWindowComposerAndLauncher;
import shared.window.AnEventHandlerAndForwarder;
import shared.window.UserWindow;
import util.annotations.Tags;
import util.awt.AnExtendibleAWTEventQueue;
import util.session.Communicator;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;

@Tags({ ApplicationTags.REPLICATED_WINDOW, InteractiveTags.COMPOSER })
public class ASlaveLockingWindowComposerAndLauncher {

	public static final String DEFAULT_APPLICATION_NAME = "REPLICATED_WINDOW";

	protected Communicator communicator;
	protected PeerMessageListener aCoupler;
	protected PeerMessageListener aSlaveCoupler;
	
	protected ALocalModelCache cache;
	protected AnExtendibleAWTEventQueue queue;

	public String getApplicationName() {
		return DEFAULT_APPLICATION_NAME;
	}


	public void composeAndLaunch(String[] args) {
		compose(args);
		launch();
	}

	public void compose(String[] args) {
		communicator = createCommunicator(args);

		queue = AnExtendibleAWTEventQueue.getEventQueue();
		cache = new ALocalModelCache(communicator);
		
		addCollaborationFunctions(queue,cache);
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

	protected void addCollaborationFunctions(AnExtendibleAWTEventQueue queue, ALocalModelCache cache) {
		addACoupler(queue);
		addASlaveCoupler(cache);
	}

	protected void addACoupler(AnExtendibleAWTEventQueue queue) {
		
		aCoupler = new ACoupler(queue);
		communicator.addPeerMessageListener(aCoupler);
	}
	
	protected void addASlaveCoupler(ALocalModelCache cache) {
		
		aSlaveCoupler = new ASlaveCoupler(cache);
		communicator.addPeerMessageListener(aSlaveCoupler);
	}

	protected void doJoin() {
		communicator.join();
	}

	public void launch() {
				
		LockingUserWindow window = new LockingUserWindow(cache);
		//Pass the window to cache so that it can update current Lock Holder
		cache.setWindow(window);
		
		//This order matters whether to send first resize == creation event to broadcast or not
		window.createAndShowFrames();
		queue.addEventQueueHandler(cache);
		
		queue.addVetoableChangeListener(cache);
	}
	
	
}
