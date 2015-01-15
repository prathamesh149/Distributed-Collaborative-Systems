package ot;

import im.AnIMClientComposerAndLauncher;

import java.util.HashMap;
import java.util.Map;

import causality.ACheckBoxFrame;
import util.session.Communicator;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;
import util.session.ReceivedMessageFilterSelector;
import util.session.SentMessage;
import util.session.SentMessageFilterSelector;

public class OTTimeStampingIMComposerAndLauncher extends
		AnIMClientComposerAndLauncher {
	
	MessageFilterCreator<ReceivedMessage> receivedMessageQueuerCreator;
	MessageFilterCreator<SentMessage> sentMessageQueuerCreator;
	
	@Override 
	public void launch() {
		(new OTClientCheckBoxFrame())
				.createAndShowCheckBoxFrame((AClientOTTimeStampingReceiveMessageFilter) receivedMessageQueuerCreator.getMessageFilter(),
						(AClientOTTimeStampingSentMessageFilter) sentMessageQueuerCreator.getMessageFilter());
		super.launch();
	}

	@Override
	public Communicator createCommunicator(String args[]) {
		// set factories used to create communicator

		Map<String, OTManager> listOTManager = new HashMap<String, OTManager>();
		listOTManager.put("topic", new OTManager(args[2], false));
		listOTManager.put("history", new OTManager(args[2], false));

		receivedMessageQueuerCreator = new AClientOTTimeStampingReceiveMessageFilterCreator(
				listOTManager);
		sentMessageQueuerCreator = new AClientOTTimeStampingSentMessageFilterCreator(
				listOTManager);
		ReceivedMessageFilterSelector
				.setMessageFilterCreator(receivedMessageQueuerCreator);
		SentMessageFilterSelector
				.setMessageFilterCreator(sentMessageQueuerCreator);
		// create communicator
		Communicator communicator = super.createCommunicator(args);

		return communicator;
	}

}
