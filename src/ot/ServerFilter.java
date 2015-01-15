package ot;

import java.util.HashMap;
import java.util.Map;

import util.session.ASentMessage;
import util.session.MessageProcessor;
import util.session.SentMessage;
import util.session.ServerMessageFilter;

public class ServerFilter implements ServerMessageFilter {

	MessageProcessor<SentMessage> sentMessageProcessor;
	Map<String,OTManager> userOTManager;
	
	public ServerFilter() {
		userOTManager = new HashMap<String,OTManager>();		
	}
	
	@Override
	public void filterMessage(SentMessage aSentMessage) {
		if (aSentMessage.getUserMessage() instanceof MessageWithOTTimeStamp) {
			
			MessageWithOTTimeStamp msg = (MessageWithOTTimeStamp) aSentMessage.getUserMessage();
			
			//The message is come from client, need to flip local and remote
			msg.setOTTimeStamp(OTUtils.flip(msg.getOTTimeStamp()));
			String sendingUser = msg.getUserName(); 
			
			//O(T)
			MessageWithOTTimeStamp transformMsg = userOTManager.get(sendingUser).transformReceived(msg);
			aSentMessage.setUserMessage(transformMsg);
			
			for(String user: userOTManager.keySet()) {
				if (!user.equals(sendingUser)) {
					
					SentMessage s = ASentMessage.toSpecificUser(aSentMessage, user);
					
					//Increment local count
					userOTManager.get(user).incLocalTimeStamp();
					int[] timeStamp = OTUtils.deepCopy(userOTManager.get(user).getMyTimeStamp());
					
					MessageWithOTTimeStamp t = (MessageWithOTTimeStamp)s.getUserMessage();
					t.setOTTimeStamp(timeStamp);
					//t.setServer(userOTManager.get(user).isServer());
					t.setServer(true);
					
					sentMessageProcessor.processMessage(s);
					
					//Store copy of sent message
					userOTManager.get(user).put(t);
				}
				
			}
			
		} else {
			sentMessageProcessor.processMessage(aSentMessage);
		}
	}

	@Override
	public void setMessageProcessor(MessageProcessor<SentMessage> theMesssageProcessor) {
		sentMessageProcessor = theMesssageProcessor;
	}

	@Override
	public void userJoined(String session, String application, String user) {
		OTManager otm = new OTManager(user, true);
		userOTManager.put(user, otm);		
	}

	@Override
	public void userLeft(String session, String application, String user) {
		userOTManager.remove(user);
	}
	
	public void reset() {
		for (OTManager m : userOTManager.values()) {
			m.resetMyTimeStamp();
			m.resetLocalBuffer();
		}
	}
}