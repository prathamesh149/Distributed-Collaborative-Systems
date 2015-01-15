package ot;

import java.util.Map;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.SentMessage;
import util.session.ServerMessageFilter;
import util.session.ServerMessageFilterCreator;

public class AMasterOTTimeStampingSentMessageFilterCreator implements ServerMessageFilterCreator {

	ServerMessageFilter masterSentMessageQueuer;
	
	public AMasterOTTimeStampingSentMessageFilterCreator(Map<String,ServerFilter> serverFilterMap) {
		masterSentMessageQueuer = new AMasterOTTimeStampingSentMessageFilter(serverFilterMap);
	}

	@Override
	public ServerMessageFilter getServerMessageFilter() {
		return masterSentMessageQueuer;
	}
	
}
