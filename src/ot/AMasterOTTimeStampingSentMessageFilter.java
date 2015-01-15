package ot;

import im.ListEdit;
import im.TopicListEdit;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.Map;

import util.session.MessageProcessor;
import util.session.SentMessage;
import util.session.ServerMessageFilter;

public class AMasterOTTimeStampingSentMessageFilter implements ServerMessageFilter, ItemListener{

	MessageProcessor<SentMessage> sentMessageProcessor;
	Map<String,ServerFilter> serverFilterMap;
	boolean causal;
	
	public AMasterOTTimeStampingSentMessageFilter(Map<String,ServerFilter> serverFilterMap) {
		this.serverFilterMap = serverFilterMap;
		causal = false;
	}
	
	@Override
	public void setMessageProcessor(MessageProcessor<SentMessage> theMesssageProcessor) {
		sentMessageProcessor = theMesssageProcessor;
		for(String list : serverFilterMap.keySet()) {
			serverFilterMap.get(list).setMessageProcessor(sentMessageProcessor);
		}
	}

	@Override
	public void userJoined(String session, String application, String user) {
		for(String list : serverFilterMap.keySet()) {
			serverFilterMap.get(list).userJoined(session,application,user);
		}		
	}

	@Override
	public void userLeft(String session, String application, String user) {
		for(String list : serverFilterMap.keySet()) {
			serverFilterMap.get(list).userLeft(session,application,user);
		}
	}
	
	@Override
	public void filterMessage(SentMessage aSentMessage) {
		//Filter the list
		if(aSentMessage.getUserMessage() instanceof MessageWithOTTimeStamp && causal) {
			
			MessageWithOTTimeStamp msg = (MessageWithOTTimeStamp) aSentMessage.getUserMessage();
			if (msg.getMessage() instanceof ListEdit) {
				serverFilterMap.get("history").filterMessage(aSentMessage);
			} else if (msg.getMessage() instanceof TopicListEdit) {
				serverFilterMap.get("topic").filterMessage(aSentMessage);
			}
			
		} else {
			sentMessageProcessor.processMessage(aSentMessage);
		}
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			causal = true;
			//reset time stamp and buffer
			for(ServerFilter sf : serverFilterMap.values()) {
				sf.reset();
			}
		} else {
			causal = false;			
			//Dont send OT transformed messages
		}		
	}	
}
