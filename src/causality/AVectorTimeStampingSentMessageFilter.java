package causality;

import trace.causal.LocalCountIncrementedInSiteVectorTimeStamp;
import trace.causal.VectorTimeStampedMessageSent;
import trace.ot.OTListEditBuffered;
import trace.ot.OTListEditFlipped;
import trace.ot.UserOTTimeStampInfo;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.SentMessage;
import util.session.ServerMessageFilter;
import util.session.ServerMessageFilterCreator;

public class AVectorTimeStampingSentMessageFilter implements MessageFilter<SentMessage> {

	MessageProcessor<SentMessage> sentMessageProcessor;
	ACausalityManager causalityManager;
	
	public AVectorTimeStampingSentMessageFilter(ACausalityManager causalityManager) {
		this.causalityManager = causalityManager;
	}
	
	// downstream node in the send pipline
	@Override
	public void setMessageProcessor(MessageProcessor<SentMessage> theMesssageProcessor) {
		sentMessageProcessor = theMesssageProcessor;
	}
	
	// called by communicator when a new message is submitted by client
	// time stamp the message and send it to the next stage outwards
	@Override
	public void filterMessage(SentMessage aSentMessage) {
		
		if(aSentMessage.isUserMessage() && causalityManager.isCausal()) {
			Object userMessage = aSentMessage.getUserMessage();
			
			causalityManager.incMyTimeStamp();
			VectorTimeStamp v = causalityManager.getMyTimeStamp().deepCopy();
			
			LocalCountIncrementedInSiteVectorTimeStamp.newCase(v.getVector(), this);

			MessageWithVectorTimeStamp msg = new AMessageWithVectorTimeStamp(userMessage,v); 
			aSentMessage.setUserMessage(msg);
			sentMessageProcessor.processMessage(aSentMessage);
			
			VectorTimeStampedMessageSent.newCase(aSentMessage, v.getVector(), this);
			
		} else {
			sentMessageProcessor.processMessage(aSentMessage);
		}		
	}
		
}
