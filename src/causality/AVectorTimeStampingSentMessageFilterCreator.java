package causality;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.SentMessage;

public class AVectorTimeStampingSentMessageFilterCreator implements MessageFilterCreator<SentMessage> {

	MessageFilter<SentMessage> sentMessageQueuer;
	public AVectorTimeStampingSentMessageFilterCreator(ACausalityManager causalityManger) {
		sentMessageQueuer =  new AVectorTimeStampingSentMessageFilter(causalityManger);
	}
	
	@Override
	public MessageFilter<SentMessage> getMessageFilter() {
		return sentMessageQueuer;
	}
	
}
