package im;

import java.beans.PropertyChangeEvent;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import javax.swing.SwingUtilities;

import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.session.Communicator;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
import echo.modular.AGUIInteractor;
import echo.modular.SimpleList;
@Tags({ApplicationTags.IM, InteractiveTags.INTERACTOR})

public class AnIMGUIInteractor extends AGUIInteractor implements TableObserver {
	protected Communicator communicator;
	protected ReplicatedSimpleTable status;
	
	
	public AnIMGUIInteractor(SimpleList<String> aHistory, SimpleList<Character> aTopic, ReplicatedSimpleTable aStatus, Communicator aCommunicator) {
		super(aHistory,aTopic);
		status = aStatus;
		communicator = aCommunicator;
	}
	
	
	protected void addToHistory(String newValue) {
		((ReplicatedSimpleList) history).replicatedAdd(newValue);

	}
	
	protected void addToTopic(int index, Character newValue) {
		((ReplicatedSimpleTopicList) topic).replicatedAdd(index,newValue);
	}
	
	protected void removeFromTopic(int anIndex) {
		((ReplicatedSimpleTopicList) topic).replicatedRemove(anIndex);
	}
	
	protected String computeFeedback(String anInput) {
		return IMUtililties.remoteEcho(anInput, communicator.getClientName());
	}
	
	/*
	 * Added
	 */
	@Override
	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equalsIgnoreCase("history")) {
			processInput((String)evt.getNewValue());
		} else if (evt.getPropertyName().equalsIgnoreCase("topic")) {

			TopicListEdit edit = (TopicListEdit)evt.getNewValue();
			if (edit.getOperationName().equals(OperationName.ADD)) {
				processInputCharacter(edit.getIndex(), (Character)edit.getElement());
			} else if (edit.getOperationName().equals(OperationName.DELETE)){
				processRemoveCharacter(edit.getIndex());
			}
			
		} else if (evt.getPropertyName().equalsIgnoreCase("status")) {
			processInputStatus((String)evt.getNewValue());	
		}	
	}
	
	private void runThread1(String newValue) {
		Runnable doRun = new Runnable() {
			
			@Override
			public void run() {
				imc.getTopicTextFieldListener().setActive(false);
				imc.getMainpanel().getTopic().setText(newValue);
			}
		};
		try {
			SwingUtilities.invokeAndWait(doRun);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void runThread2() {
		Runnable doRun1 = new Runnable() {
			
			@Override
			public void run() {
				imc.getTopicTextFieldListener().setActive(true);
			}
		};
		SwingUtilities.invokeLater(doRun1);
	}
	
	//Topic Model Announced
	public void displayTopicOutput(int index, Character c) {

		StringBuffer oldText = new StringBuffer(imc.getMainpanel().getTopic().getText());
		StringBuffer newText = oldText.insert(index, c);

		//De-activating the topic text field listener while adding remote changes
		runThread1(newText.toString());
		runThread2();
	}
	
	
	public void displayTopicRemoved(int index) {
		StringBuffer oldText = new StringBuffer(imc.getMainpanel().getTopic().getText());
		StringBuffer newText = oldText.deleteCharAt(index);
				
		runThread1(newText.toString());
		runThread2();
	}
	
	/*
	 * Fired as a property change event
	 */
	public void processInputStatus(String anInput) {
		String userName = communicator.getClientName();
		addToStatus(userName, anInput);
	}
	
	public void addToStatus(String userName, String anInput) {
		status.replicatedAdd(userName,anInput);
	}
	
	public void runner(String statusOfAll) {
		Runnable doRun = new Runnable() {
			
			@Override
			public void run() {
				imc.getMainpanel().getStatusBox().setText(statusOfAll);

			}
		};
		SwingUtilities.invokeLater(doRun);
	}
	
	@Override
	public void elementAddedInStatus(String username, String value) {
		
		//Change WRONG use of model
		String statusOfAll = status.getStatusMap().toString();
		
		//Display the statuses in statuesBox
		runner(statusOfAll);
	}
}
