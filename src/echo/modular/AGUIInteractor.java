package echo.modular;

import im.TopicListEdit;

import java.beans.PropertyChangeEvent;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import shared.window.ACustomizableFramePointer;
import shared.window.ATelePointerPainter;
import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.awt.AnExtendibleTelePointerGlassPane;
import util.awt.ExtendibleTelepointerGlassPane;
import util.awt.GlassPaneController;
//import util.awt.AGlassPaneRedispatcher;
//import util.awt.AnExtendibleAWTEventQueue;
//import util.awt.AnExtendibleTelePointerGlassPane;
//import util.awt.ExtendibleTelepointerGlassPane;
import util.tags.ApplicationTags;
import echo.monolithic.EchoUtilities;


@SuppressWarnings("rawtypes")
public class AGUIInteractor implements ListObserver,TopicListObserver, GUIInteractor {

	protected SimpleList<String> history;
	protected SimpleList<Character> topic;
	protected IMCreator imc;
	
	/*
	 * Added 
	 */
	public AGUIInteractor(SimpleList<String> history) {
		this.history = history;
	}
	
	public AGUIInteractor(SimpleList<String> history, SimpleList<Character> topic) {
		this.history = history;
		this.topic = topic;
	}
	
	// Input Entered in History Model
	public void processInput(String anInput) {
		String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, history.size(), aFeedback, ApplicationTags.IM, this);
		addToHistory(aFeedback);
	}
	
	// History Model Changed
	protected void addToHistory(String newLine) {
		history.observableAdd(newLine);
	}

	// History change announced
	@Override
	public void elementAdded(int anIndex, Object aNewValue, List list) {
		//Code to distinguish if Topic or History announced
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
		displayOutput(anIndex,(String) aNewValue,list);
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
				
	}
	
	@Override
	public void elementAdded(int anIndex, Object aNewValue) {
		
		
	}
	
	public void elementAddedInTopic(int anIndex, Object aNewValue) {
		//Code to distinguish if Topic or History announced
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.EDITOR, this);
		
		displayTopicOutput(anIndex,(Character)aNewValue);
		
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.EDITOR, this);
	}
	
	public void elementRemovedFromTopic(int anIndex, Object aNewValue) {
		//Code to distinguish if Topic or History announced
		ListEditObserved.newCase(OperationName.DELETE, anIndex, aNewValue, ApplicationTags.EDITOR, this);
		
		displayTopicRemoved(anIndex);
		
		ListEditDisplayed.newCase(OperationName.DELETE, anIndex, aNewValue, ApplicationTags.EDITOR, this);
	}
	
	// History output displayed
	public void displayOutput(int anIndex, String line, List<String> list) {
		StringBuffer sb = new StringBuffer();
		
		for(String s:  list) {
			sb.append(s + "\n");
		}		
		
		imc.getMainpanel().getHistory().setText(sb.toString());
		//imc.getMainpanel().getHistory().append(line + "\n");
	}

	//Input Entered in Topic Model
	public void processInputCharacter(int index, Character newValue) {
		ListEditInput.newCase(OperationName.ADD, index, newValue, ApplicationTags.EDITOR, this);
		addToTopic(index, newValue);
	}
	
	//Character removed from topic model
	public void processRemoveCharacter(int index) {
		ListEditInput.newCase(OperationName.DELETE, index, topic.get(index), ApplicationTags.EDITOR, this);
		removeFromTopic(index);
	}
	
	protected void removeFromTopic(int anIndex) {
		topic.observableRemove(anIndex);
	}
	
	//Topic Model Changed
	protected void addToTopic(int index, Character newValue) {
		topic.observableAdd(index, newValue);
	}

	public void displayTopicOutput(int anIndex, Character c) {
	
	}
	
	public void displayTopicRemoved(int anIndex) {
		
	}
	
	
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
		} 	
	}

	@Override
	public void elementRemoved(int anIndex, Object aNewValue) {

	}
	
	protected String computeFeedback(String anInput) {
		return EchoUtilities.echo(anInput);
	}
	
	public void createAndLaunchGUI() {
		//Pass the guiInteractor object
		
		GUIInteractor guiInteractor = this;
		imc = new IMCreator();
		JFrame frame = imc.launchIM("I-Messenger");
		imc.getMainpanel().addPropertyChangeListener(guiInteractor);
		
		boolean showTelepointer = false; 
		
		if(showTelepointer) {
			JComponent glassPane = new AnExtendibleTelePointerGlassPane(frame);
			
			ExtendibleTelepointerGlassPane gp = (ExtendibleTelepointerGlassPane)glassPane;
			GlassPaneController gpc = gp.getGlassPaneController();
			
			//Create a slider window UI which will send all changes to GlassPaneController of 
			//AnExtendibleTelePointerGlassPane
			ACustomizableFramePointer cfp = new ACustomizableFramePointer(gpc);
			cfp.createPointerFrame();
			
			gp.addPainter(new ATelePointerPainter(gp));
		
			frame.setGlassPane(glassPane);
			glassPane.setVisible(true);
			
		}	
	}


}