package echo.modular;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import trace.echo.ListEditDisplayed;
import trace.echo.ListEditInput;
import trace.echo.modular.ListEditObserved;
import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.tags.*;
import echo.monolithic.EchoUtilities;

@Tags({ApplicationTags.ECHOER, InteractiveTags.INTERACTOR})
public class AnEchoInteractor implements EchoerInteractor {

	protected SimpleList<String> history;

	public AnEchoInteractor(SimpleList<String> history) {
		this.history = history;
		
	}

	@SuppressWarnings("resource")
	@Override
	public void doInput() {
		for (;;) {
			System.out.println(EchoUtilities.PROMPT);
			Scanner scanner = new Scanner(System.in);
			String nextInput = scanner.nextLine();
			if (nextInput.equals(EchoUtilities.QUIT)) {
				processQuit();				
				break;
			} else if (nextInput.equals(EchoUtilities.HISTORY))
				printHistory();
			else
				processInput(nextInput);
		}
		
	}

	protected void processQuit() {
		System.out.println("Quitting application");
	}

	@Override
	public void printHistory() {
		
		List<String> aList = toList(history);
		StringBuilder stringBuilder = new StringBuilder();
		for (int index = 0; index < aList.size(); index++) {
			stringBuilder.append(aList.get(index));			
			stringBuilder.append((index == aList.size() - 1)? "\n":", ");
		}		
		System.out.println(stringBuilder.toString());
				
	}

	// Input Entered
	public void processInput(String anInput) {
		String aFeedback = computeFeedback(anInput);
		ListEditInput.newCase(OperationName.ADD, history.size(), aFeedback, ApplicationTags.IM, this);
		addToHistory(aFeedback);
	}

	// Model Changed
	protected void addToHistory(String newLine) {
		history.observableAdd(newLine);
	}
	
	//Change announced
	@Override
	public void elementAdded(int anIndex, Object aNewValue) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
		displayOutput(history.get(anIndex));
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
	}

	// Output displayed
	public void displayOutput(String newLine) {
		System.out.println(newLine);
	}

	@Override
	public void elementRemoved(int anIndex, Object aNewValue) {
		
	}
	
	protected String computeFeedback(String anInput) {
		return EchoUtilities.echo(anInput);
	}
	
	public static List<String> toList(SimpleList<String> aHistory) {
		List<String> retVal = new ArrayList<String>();
		for (int index = 0; index < aHistory.size(); index++) {
			retVal.add(aHistory.get(index));			
		}	
		return retVal;
	}

	@Override
	public void elementAdded(int anIndex, Object aNewValue, List list) {
		ListEditObserved.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
		displayOutput(history.get(anIndex));
		ListEditDisplayed.newCase(OperationName.ADD, anIndex, aNewValue, ApplicationTags.IM, this);
	}

}