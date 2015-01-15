package im;


import java.lang.annotation.ElementType;
import java.util.ArrayList;
import java.util.List;

import trace.echo.modular.OperationName;
import trace.im.ListEditSent;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.trace.session.AddressedSentMessageInfo;
import echo.modular.ASimpleTopicList;
import echo.modular.TopicListObserver;

public class AReplicatedSimpleTopicList<ElementType> extends ASimpleTopicList<ElementType> implements ReplicatedSimpleTopicList<ElementType> {
	Communicator communicator;
	List<TopicListObserver<ElementType>> replicatingObservers = new ArrayList();
	
	public AReplicatedSimpleTopicList(Communicator theCommunicator) {
		communicator = theCommunicator;
	}
	
	//This method sends the topic character to other users
	public synchronized void replicatedAdd(int anIndex, ElementType anElement) {
		super.add(anIndex, anElement);		
		if (communicator == null) return;
		TopicListEdit listEdit = new ATopicListEdit<ElementType>(OperationName.ADD, anIndex, anElement, ApplicationTags.EDITOR);
		ListEditSent.newCase(
				communicator.getClientName(),
				listEdit.getOperationName(), 
				listEdit.getIndex(), 
				listEdit.getElement(), 
				listEdit.getList(),
			AddressedSentMessageInfo.OTHERS, this);
		communicator.toOthers(listEdit);
		notifyReplicatingObservers(anIndex, anElement);

	}
	
	public synchronized void replicatedRemove(int anIndex) {
		ElementType anElement = simpleList.get(anIndex);
		super.remove(anIndex);		
		if (communicator == null) return;
		TopicListEdit listEdit = new ATopicListEdit<ElementType>(OperationName.DELETE, anIndex, anElement , ApplicationTags.EDITOR);
		ListEditSent.newCase(
				communicator.getClientName(),
				listEdit.getOperationName(), 
				listEdit.getIndex(), 
				listEdit.getElement(), 
				listEdit.getList(),
			AddressedSentMessageInfo.OTHERS, this);
		communicator.toOthers(listEdit);
		notifyReplicatingObservers(anIndex, anElement);

	}
	
	public void notifyReplicatingObservers(int index, ElementType newValue) {
		notifyAdd(replicatingObservers, index, newValue);	
	}

}
