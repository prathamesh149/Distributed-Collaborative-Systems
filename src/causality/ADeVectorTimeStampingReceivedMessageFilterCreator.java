package causality;

import util.session.MessageFilter;
import util.session.MessageFilterCreator;
import util.session.ReceivedMessage;

public class ADeVectorTimeStampingReceivedMessageFilterCreator  implements MessageFilterCreator<ReceivedMessage>{
	MessageFilter<ReceivedMessage> receivedMessageQueuer;

	public ADeVectorTimeStampingReceivedMessageFilterCreator(ACausalityManager causalityManager) {
		receivedMessageQueuer =  new ADeVectorTimeStampingReceivedMessageFilter(causalityManager);
	}
	
	@Override
	public MessageFilter<ReceivedMessage> getMessageFilter() {
		return receivedMessageQueuer;
	}

}