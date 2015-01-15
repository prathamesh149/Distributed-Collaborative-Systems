package echo.modular;

import java.util.ArrayList;
import java.util.List;

import trace.echo.ListEditMade;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.OperationName;
import util.annotations.Tags;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;


public class ASimpleTopicList<ElementType> implements SimpleList<ElementType> {
	protected List<ElementType> simpleList = new ArrayList();
	protected List<TopicListObserver<ElementType>> observers = new ArrayList();

	
	@Override
	public synchronized void add(ElementType anElement) {
		simpleList.add(simpleList.size(), anElement);
	}
	
	@Override
	public synchronized void add(int anIndex, ElementType anElement) {
		simpleList.add(anIndex, anElement);
		traceAdd(anIndex, anElement);
	}

	@Override
	public synchronized void observableAdd(int anIndex, ElementType anElement) {
		add(anIndex, anElement);
		notifyAdd(anIndex, anElement);
	}

	@Override
	public synchronized void observableAdd(ElementType anElement) {

		observableAdd(simpleList.size(), anElement);
	}

	public void notifyAdd(int index, ElementType newValue) {

		notifyAdd(observers, index, newValue);
	}
	
	public void notifyAdd(List<TopicListObserver<ElementType>> observers, int index,
			ElementType newValue) {
		ListEditNotified.newCase(OperationName.ADD, index, newValue, ApplicationTags.EDITOR, this);
		for (TopicListObserver<ElementType> observer : observers) {
			observer.elementAddedInTopic(index, newValue);
		}
	}
	
	protected void traceAdd(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.ADD, anIndex,anElement, ApplicationTags.EDITOR, this);
	}
	
	@Override
	public synchronized ElementType remove(int anIndex) {
		ElementType retVal = simpleList.remove(anIndex);
		traceRemove(anIndex, retVal);
		return retVal;
	}
	
	@Override
	public synchronized boolean remove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		remove(anIndex);
		return true;		
	}
	
	@Override
	public synchronized boolean observableRemove(ElementType anElement) {
		int anIndex = simpleList.indexOf(anElement);
		if (anIndex < 0)
			return false;
		observableRemove(anIndex);
		return true;
		
	}
	
	@Override
	public synchronized ElementType observableRemove(int anIndex) {
		
		ElementType retVal = remove(anIndex);
		notifyRemove(anIndex, retVal);
		return retVal;
	}
	
	public void notifyRemove(List<TopicListObserver<ElementType>> observers, int index, ElementType newValue) {
		ListEditNotified.newCase(OperationName.DELETE, index, newValue, ApplicationTags.EDITOR, this);
		for (TopicListObserver<ElementType> observer:observers)
			observer.elementRemovedFromTopic(index, newValue);
	}
	
	public void notifyRemove(int index, ElementType newValue) {

		notifyRemove(observers, index, newValue);
	}
	
	protected void traceRemove(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.DELETE, anIndex,anElement, ApplicationTags.EDITOR, this);
	}
	
	
	
	@Override
	public int size() {
		return simpleList.size();
	}

	@Override
	public ElementType get(int index) {
		return simpleList.get(index);
	}

	@Override
	public void addObserver(Observer<ElementType> anObserver) {
		observers.add((TopicListObserver<ElementType>)anObserver);
	}

}