package ot;
import im.AListEdit;
import im.ListEdit;
import im.TopicListEdit;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import trace.echo.ListEditInfo;
import trace.ot.OTListEditFlipped;
import trace.ot.UserOTTimeStampInfo;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.ReceivedMessage;

public class AClientOTTimeStampingReceiveMessageFilter implements MessageFilter<ReceivedMessage>, ItemListener{

	MessageProcessor<ReceivedMessage> receivedMessageProcessor;
	Map<String,OTManager> listOTManager;
	boolean causal;
	
	public AClientOTTimeStampingReceiveMessageFilter(Map<String,OTManager> listOTManager) {
		this.listOTManager = listOTManager;
		causal = false;
	}

	@Override
	public void setMessageProcessor(MessageProcessor<ReceivedMessage> theMesssageProcessor) {
		receivedMessageProcessor = theMesssageProcessor;
	}
	
	@Override
	public void filterMessage(ReceivedMessage aReceivedMessage) {
		//No check of username, assumes correctly sent from server
		if (causal && aReceivedMessage.getUserMessage() instanceof MessageWithOTTimeStamp) {
		
			MessageWithOTTimeStamp msg = (MessageWithOTTimeStamp) aReceivedMessage.getUserMessage();
			String listName = null;
			ListEditInfo aListEdit = null;
			
			if (msg.getMessage() instanceof ListEdit) {
				ListEdit t = (ListEdit) msg.getMessage();
				aListEdit = new ListEditInfo("",t.getOperationName(), t.getIndex(), t.getElement(), t.getList(),this);
				
				listName = "history";				
			} else if (msg.getMessage() instanceof TopicListEdit) {
				listName = "topic";
				TopicListEdit t = (TopicListEdit) msg.getMessage();
				aListEdit = new ListEditInfo("",t.getOperationName(), t.getIndex(), t.getElement(), t.getList(),this);  
			} 
			
			//The message is come from server, need to flip local and remote
			OTManager otm = listOTManager.get(listName); 
			
			UserOTTimeStampInfo anOTTimeStamp = new UserOTTimeStampInfo("", otm.getMyName(), otm.getMyTimeStamp()[0], otm.getMyTimeStamp()[1], otm.isServer());
			OTListEditFlipped.newCase("", aListEdit, anOTTimeStamp , this);
			msg.setOTTimeStamp(OTUtils.flip(msg.getOTTimeStamp()));
			
			MessageWithOTTimeStamp transformedMsg = otm.transformReceived(msg);
						
			aReceivedMessage.setUserMessage(transformedMsg.getMessage());
			receivedMessageProcessor.processMessage(aReceivedMessage);
			
		} else {
			receivedMessageProcessor.processMessage(aReceivedMessage);
			return;
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			causal = true;
			//reset time stamp and buffer
			for(OTManager m : listOTManager.values()) {
				m.resetMyTimeStamp();
				m.resetLocalBuffer();
			}
			
		} else {
			causal = false;			
			//Dont send OT transformed messages
		}		
	}
}
