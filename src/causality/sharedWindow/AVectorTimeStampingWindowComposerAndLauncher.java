package causality.sharedWindow;

import causality.ACausalityManager;
import causality.ACheckBoxFrame;
import causality.ADeVectorTimeStampingReceivedMessageFilterCreator;
import causality.AVectorTimeStampingSentMessageFilterCreator;
import shared.window.AWindowComposerAndLauncher;
import util.session.Communicator;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;
import util.session.ReceivedMessageFilterSelector;
import util.session.SentMessage;
import util.session.SentMessageFilterSelector;

public class AVectorTimeStampingWindowComposerAndLauncher extends AWindowComposerAndLauncher {
	protected ACausalityManager causalityManager;
	
	@Override 
	public void launch() {
		(new ACheckBoxFrame()).createAndShowCheckBoxFrame(causalityManager);
		super.launch();
	}

	@Override
	public Communicator createCommunicator(String args[]) {
		// set factories used to create communicator
		causalityManager = new ACausalityManager(args[2]);
		
		MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator = new ADeVectorTimeStampingReceivedMessageFilterCreator(causalityManager);
		MessageFilterCreator<SentMessage> sentMessageQueuerCreator = new AVectorTimeStampingSentMessageFilterCreator(causalityManager);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		// create communicator
		Communicator communicator = super.createCommunicator(args);
		communicator.addSessionMessageListener(causalityManager);
		
		return communicator;
	}

}
