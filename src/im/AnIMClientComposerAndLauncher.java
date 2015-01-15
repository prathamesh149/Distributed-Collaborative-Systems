package im;

import util.session.CommunicatorSelector;
import util.session.Communicator;
import util.session.PeerMessageListener;
import util.session.SentMessageCreator;
import util.session.SentMessageFilterSelector;
import util.trace.Tracer;
import util.trace.session.SessionTracerSetter;
import echo.modular.AnEchoComposerAndLauncher;
import echo.modular.EchoerInteractor;
import echo.modular.GUIInteractor;
import echo.modular.SimpleList;
public class AnIMClientComposerAndLauncher extends AnEchoComposerAndLauncher implements IMClientComposerAndLauncher{
	public static final String DEFAULT_APPLICATION_NAME = "IM";

	protected Communicator communicator;	
	protected PeerMessageListener historyInCoupler;
	
	protected ReplicatedSimpleTable status;

	public String getApplicationName() {
		return DEFAULT_APPLICATION_NAME;
	}
	
	/*
	 * Added
	 */
	protected void createModel() {
		history = createHistory();
		topic = createTopic();
		status = createStatus();
	}
	
	public void connectModelInteractor() {
		echoInteractor = createInteractor();
		history.addObserver(echoInteractor);
			
		guiInteractor = createGUIInteractor();
		history.addObserver(guiInteractor);
		topic.addObserver(guiInteractor);
		status.addObserver(guiInteractor);
	}
	
	protected SimpleList<String> createHistory() {
		return new AReplicatedSimpleList<String>(communicator);
	}
	
	protected SimpleList<Character> createTopic() {
		return new AReplicatedSimpleTopicList<Character>(communicator);
	}
	
	protected ReplicatedSimpleTable createStatus() {
		return new AReplicatedSimpleTable(communicator);
	}
	
	protected EchoerInteractor createInteractor() {
		return new AnIMInteractor((ReplicatedSimpleList) history, communicator);
	}	
	
	protected GUIInteractor createGUIInteractor() {
		return new AnIMGUIInteractor((ReplicatedSimpleList) history, (ReplicatedSimpleTopicList) topic, status, communicator);
	}
	
	
	protected void addCollaborationFunctions() {
		addHistoryInCoupler();
	}	
	
	public void compose(String[] args) {
		communicator = createCommunicator(args);
		super.compose(args);
		addCollaborationFunctions();
		doJoin();
	}	
	
	protected void doJoin() {
		communicator.join();
	}

	public void checkArgs(String[] args) {
		if (args.length < 5) {
			System.out.println("Please supply server host name, session name,  user name and application name as main arguments");
			System.exit(-1);
		}
	}
	// parameters to factory
	public  Communicator createCommunicator(String args[]) {
		checkArgs(args);
		if (args.length == 5) {
			if (args[4].equalsIgnoreCase(Communicator.DIRECT))
				CommunicatorSelector.selectDirectCommunicator();
			else if (args[4].equalsIgnoreCase(Communicator.RELAYED))
				CommunicatorSelector.selectRelayerCommunicator();				
		}
		return CommunicatorSelector.getCommunicator(args[0],args[1],args[2], args[3]);
//		CommunicatorCreator communicatorFactory = ACommunicatorSelector.getCommunicatorFactory();
//		return  communicatorFactory.getCommunicator(args[0],args[1],args[2], applicationName);
	}
	protected  void addHistoryInCoupler() {
		historyInCoupler = new AHistoryInCoupler(history,topic,status);
		communicator.addPeerMessageListener(historyInCoupler);
		
	}
	public Communicator getCommunicator() {
		return communicator;
	}
	public PeerMessageListener getRemoteInputEchoer() {
		return historyInCoupler;
	}

	public static void main (String[] args) {
//		Tracer.showInfo(true);
		SessionTracerSetter.setSessionPrintStatus();
		Tracer.showInfo(true);
		(new AnIMClientComposerAndLauncher()).composeAndLaunch(args);
	}
}
