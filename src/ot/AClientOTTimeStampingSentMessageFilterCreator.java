package ot;

import java.util.Map;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.SentMessage;

public class AClientOTTimeStampingSentMessageFilterCreator implements MessageFilterCreator<SentMessage>{
	
	MessageFilter<SentMessage> sentMessageQueuer;
	
	public AClientOTTimeStampingSentMessageFilterCreator(Map<String,OTManager> listOTManager) {
		sentMessageQueuer =  new AClientOTTimeStampingSentMessageFilter(listOTManager);
	}
	
	@Override
	public MessageFilter<SentMessage> getMessageFilter() {
		return sentMessageQueuer;
	}
}
