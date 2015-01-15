package causality;

import trace.causal.VectorTimeStampCreated;
import util.session.Communicator;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;
import util.session.ReceivedMessageFilterSelector;
import util.session.SentMessage;
import util.session.SentMessageFilterSelector;
import util.session.ServerMessageFilterCreator;
import im.AnIMClientComposerAndLauncher;

public class AVectorTimeStampingIMComposerAndLauncher extends
		AnIMClientComposerAndLauncher {
	
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
		
		VectorTimeStampCreated.newCase(causalityManager.getMyTimeStamp().deepCopy().getVector(), this);
		
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