package im;

import trace.echo.modular.OperationName;
import trace.im.ListEditReceived;
import trace.im.TableChangeReceived;
import util.session.CommunicatorSelector;
import util.session.PeerMessageListener;
import util.tags.ApplicationTags;
import echo.modular.SimpleList;

public class AHistoryInCoupler implements PeerMessageListener {
	protected SimpleList<String> history;
	protected ReplicatedSimpleTable status;
	protected SimpleList<Character> topic;
	
	public AHistoryInCoupler(SimpleList<String>  theEchoer, SimpleList<Character> aTopic, ReplicatedSimpleTable aStatus) {
		history = theEchoer;
		topic  = aTopic;
		status = aStatus;
	}
	
	public void objectReceived(Object message, String userName) {
		// need for integration with RPC
		if (message instanceof TopicListEdit) {
			processReceivedTopicListEdit((TopicListEdit<Character>) message, userName);
		} else if (message instanceof ListEdit) {
			processReceivedListEdit((ListEdit<String>) message, userName);
		} else if (message instanceof TableEdit) {
			processReceivedTableEdit((TableEdit) message, userName);
		}
	}
	protected void processReceivedListEdit (ListEdit<String> aRemoteEdit, String aUserName) {
		if (!aRemoteEdit.getList().equals(ApplicationTags.IM))
			return;
		ListEditReceived.newCase(
				CommunicatorSelector.getProcessName(),
				aRemoteEdit.getOperationName(), 
				aRemoteEdit.getIndex(), 
				aRemoteEdit.getElement(),
				aRemoteEdit.getList(),
				aUserName, 
				this);
		String anInput = aRemoteEdit.getElement();
		history.observableAdd(normalizedIndex(history, aRemoteEdit.getIndex()), anInput);	
	}
	
	protected static int normalizedIndex(SimpleList aHistory, int index) {
		int size = aHistory.size();
		return index > size?size: index;
	}
	
	protected void processReceivedTopicListEdit (TopicListEdit<Character> aRemoteEdit, String aUserName) {
		if (!aRemoteEdit.getList().equals(ApplicationTags.EDITOR))
			return;
		ListEditReceived.newCase(
				CommunicatorSelector.getProcessName(),
				aRemoteEdit.getOperationName(), 
				aRemoteEdit.getIndex(), 
				aRemoteEdit.getElement(),
				aRemoteEdit.getList(),
				aUserName, 
				this);
		Character anInput = aRemoteEdit.getElement();
		if ( aRemoteEdit.getOperationName().equals(OperationName.ADD) ) {
			topic.observableAdd(normalizedIndex(topic, aRemoteEdit.getIndex()), anInput);	
		} else if ( aRemoteEdit.getOperationName().equals(OperationName.DELETE) ) {
			topic.observableRemove(normalizedIndex(topic, aRemoteEdit.getIndex()));
		}		
	}
	
		
	protected void processReceivedTableEdit( TableEdit aRemoteEdit, String aUserName ) {
		if (!aRemoteEdit.getList().equals(ApplicationTags.IM))
			return;
		
		String username = aRemoteEdit.getUsername();
		String value = aRemoteEdit.getValue();
		
		TableChangeReceived.newCase(username, "", value , ApplicationTags.IM, this);
		status.observableAdd(username, value);
		
	}
}
