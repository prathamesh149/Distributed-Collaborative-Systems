package causality;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import trace.causal.ConcurrentVectorTimeStampedMessageDetected;
import trace.causal.VectorTimeStampedMessageBuffered;
import trace.causal.VectorTimeStampedMessageDelivered;
import trace.causal.VectorTimeStampedMessageReceived;
import trace.causal.VectorTimeStampedMessageRemovedFromBuffer;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.ReceivedMessage;

public class ADeVectorTimeStampingReceivedMessageFilter implements MessageFilter<ReceivedMessage>, PropertyChangeListener {

	MessageProcessor<ReceivedMessage> receivedMessageProcessor;
	ACausalityManager causalityManager;
	
	public ADeVectorTimeStampingReceivedMessageFilter(ACausalityManager causalityManager) {
		this.causalityManager = causalityManager;
		causalityManager.addPropertyChangeListener(this);
	}
		
	// called by communicator when a new message is submitted by client
	// unwrap the time stamped message and submit it to the next input stage inwards
	@Override
	public void filterMessage(ReceivedMessage aReceivedMessage) {
		if (aReceivedMessage.isUserMessage() && causalityManager.isCausal()) {
			
			MessageWithVectorTimeStamp newMessage = (MessageWithVectorTimeStamp) aReceivedMessage.getUserMessage();
			VectorTimeStamp vectorTimeStamp = newMessage.getVectorTimeStamp();
			
			VectorTimeStampedMessageReceived.newCase(aReceivedMessage, vectorTimeStamp.deepCopy().getVector(), this);
			
			//Concurrency test
			if(vectorTimeStamp.isConcurrent(causalityManager.getMyTimeStamp())) {
				System.out.println("Concurrent message from : " + aReceivedMessage.getClientName());
				aReceivedMessage.setUserMessage(newMessage.getMessage());
				receivedMessageProcessor.processMessage(aReceivedMessage);
				
				ConcurrentVectorTimeStampedMessageDetected.newCase(aReceivedMessage, vectorTimeStamp.deepCopy().getVector(), this);
				return;	
			}
			
			//Put in ordered buffer
			int aPos = causalityManager.put(aReceivedMessage);
			VectorTimeStampedMessageBuffered.newCase(aPos, aReceivedMessage, vectorTimeStamp.deepCopy().getVector(), this);
			
			//Keep processing the buffered messages until a condition
			
			while(true) {
				if(causalityManager.getOrderedBuffer().isEmpty() 
						|| !vectorTimeStamp.isSuccessorOf(causalityManager.getMyTimeStamp())) {
					return;
				}
				
				ReceivedMessage msg = causalityManager.getOrderedBuffer().remove(0);
				MessageWithVectorTimeStamp msgFromBuffer = (MessageWithVectorTimeStamp) msg.getUserMessage();
				VectorTimeStamp aVectorTimeStamp = msgFromBuffer.getVectorTimeStamp();
				
				
				VectorTimeStampedMessageRemovedFromBuffer.newCase(msg, aVectorTimeStamp.deepCopy().getVector(), this);
				
				//Need to update vectorTimeStamp
				if(!causalityManager.getOrderedBuffer().isEmpty()) {
					ReceivedMessage next = causalityManager.getOrderedBuffer().get(0);
					MessageWithVectorTimeStamp nextMsgWithStamp = (MessageWithVectorTimeStamp)next.getUserMessage();
					vectorTimeStamp = nextMsgWithStamp.getVectorTimeStamp().deepCopy();
				}
				
				
				//De-Time stamp
				MessageWithVectorTimeStamp msgWithStamp = (MessageWithVectorTimeStamp)msg.getUserMessage();				
				msg.setUserMessage(msgWithStamp.getMessage());
				receivedMessageProcessor.processMessage(msg);
				
				VectorTimeStampedMessageDelivered.newCase(msg, aVectorTimeStamp.deepCopy().getVector(), this);
				
				
				VectorTimeStamp v = msgWithStamp.getVectorTimeStamp().deepCopy();
				causalityManager.setMyTimeStamp(v);
			}						
		} else {
			receivedMessageProcessor.processMessage(aReceivedMessage);
		}
	}
	
	// downstream node in the receive pipeline
	@Override
	public void setMessageProcessor(MessageProcessor<ReceivedMessage> theMesssageProcessor) {
		receivedMessageProcessor = theMesssageProcessor;
	}

	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		
		//Process all messages in buffer
		if (!causalityManager.isCausal()) {
			while (!causalityManager.getOrderedBuffer().isEmpty()) {
				ReceivedMessage msg = causalityManager.getOrderedBuffer().remove(0);
				MessageWithVectorTimeStamp msgWithStamp = (MessageWithVectorTimeStamp)msg.getUserMessage();				
				msg.setUserMessage(msgWithStamp.getMessage());
				receivedMessageProcessor.processMessage(msg);
			}
		}
	}	
}
