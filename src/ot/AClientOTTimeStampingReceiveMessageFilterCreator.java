package ot;

import java.util.Map;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;

public class AClientOTTimeStampingReceiveMessageFilterCreator implements MessageFilterCreator<ReceivedMessage>{
	
	MessageFilter<ReceivedMessage> receivedMessageQueuer;
	
	public AClientOTTimeStampingReceiveMessageFilterCreator(Map<String,OTManager> listOTManager) {
		receivedMessageQueuer =  new AClientOTTimeStampingReceiveMessageFilter(listOTManager);
	}
	
	@Override
	public MessageFilter<ReceivedMessage> getMessageFilter() {
		return receivedMessageQueuer;
	}
}
