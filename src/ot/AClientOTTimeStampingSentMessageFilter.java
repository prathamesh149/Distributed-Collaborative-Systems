package ot;
import im.ListEdit;
import im.TopicListEdit;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import trace.ot.LocalSiteCountIncremented;
import trace.ot.OTListEditSent;
import util.Misc;
import util.session.MessageFilter;
import util.session.MessageProcessor;
import util.session.SentMessage;

public class AClientOTTimeStampingSentMessageFilter implements MessageFilter<SentMessage>, ItemListener{

	MessageProcessor<SentMessage> sentMessageProcessor;
	Map<String,OTManager> listOTManager;
	boolean causal;
	
	public AClientOTTimeStampingSentMessageFilter(Map<String,OTManager> listOTManager) {
		this.listOTManager = listOTManager;
		causal = false;
	}
	
	//downstream node in the send pipline
	@Override
	public void setMessageProcessor(MessageProcessor<SentMessage> theMesssageProcessor) {
		sentMessageProcessor = theMesssageProcessor;
	}

	@Override
	public void filterMessage(SentMessage aSentMessage) {
		
		if (causal) {
			Object userMessage = aSentMessage.getUserMessage();
			String listName = null;
			
			if (userMessage instanceof ListEdit) {
				listName = "history";
			} else if (userMessage instanceof TopicListEdit) {
				listName = "topic";
			} else {
				sentMessageProcessor.processMessage(aSentMessage);
				return;
			}
			
			listOTManager.get(listName).incLocalTimeStamp();			
			
			int[] timeStamp = OTUtils.deepCopy(listOTManager.get(listName).getMyTimeStamp());
			String user = listOTManager.get(listName).getMyName();
			
			MessageWithOTTimeStamp msg = new AMessageWithOTTimeStamp(userMessage,timeStamp,user);
			//msg.setServer(listOTManager.get(listName).isServer());
			msg.setServer(false);
			
			aSentMessage.setUserMessage(msg);
			
			
			sentMessageProcessor.processMessage(aSentMessage);
			
			
			
			//Store in local buffer
			listOTManager.get(listName).put((MessageWithOTTimeStamp)Misc.deepCopy(msg));
		} else {
			sentMessageProcessor.processMessage(aSentMessage);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			causal = true;
			//Reset of OT managers done in receive filters
		} else {
			causal = false;			
			//Dont send OT transformed messages
		}		
	}	
}