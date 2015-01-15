package echo.modular;

import trace.echo.modular.EchoTracerSetter;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;

@Tags({ApplicationTags.ECHOER, InteractiveTags.COMPOSER})
public class AnEchoComposerAndLauncher implements EchoerComposerAndLauncher{
	
	protected SimpleList<String> history;
	protected EchoerInteractor echoInteractor;
	protected GUIInteractor guiInteractor;
	
	protected SimpleList<Character> topic;
	
	public SimpleList<String> getHistory() {
		return history;
	}

	public EchoerInteractor getEchoInteractor() {
		return echoInteractor;
	}

	public void composeAndLaunch(String[] args) {		
		compose(args);
		launch();		
	}

	public void compose(String[] args) {
		createModel();
		connectModelInteractor();
	}
	
	protected void createModel() {
		history = createHistory();
		topic = createTopic();
	}
	
	protected SimpleList<String> createHistory() {
		return new ASimpleList<String>();
	}
	
	protected SimpleList<Character> createTopic() {
		return new ASimpleTopicList<Character>();
	}
	
	public void connectModelInteractor() {
		echoInteractor = createInteractor();
		history.addObserver(echoInteractor);
			
		guiInteractor = createGUIInteractor();
		history.addObserver(guiInteractor);
		topic.addObserver(guiInteractor);
	}
	
	protected EchoerInteractor createInteractor() {
		return new AnEchoInteractor(history);
	}
	
	protected GUIInteractor createGUIInteractor() {
		return new AGUIInteractor(history,topic);
	}

	public void launch() {
		
		//Launch both the GUI and Console Views
		launchGUI();
		launchConsoleUI();
				
	}
	
	protected void launchGUI() {
		guiInteractor.createAndLaunchGUI();
	}
	
	protected void launchConsoleUI() {
		echoInteractor.doInput();
	}
	
	public static void traceUnawareLaunch(String[] args) {
		(new AnEchoComposerAndLauncher()).composeAndLaunch(args);				
	}
			
	public static void main(String[] args) {		
		EchoTracerSetter.traceEchoer();
		traceUnawareLaunch(args);
	}

}