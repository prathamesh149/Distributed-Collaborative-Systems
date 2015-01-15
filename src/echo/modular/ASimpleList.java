package echo.modular;

import java.util.ArrayList;
import java.util.List;

import trace.echo.ListEditMade;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.OperationName;
import util.tags.ApplicationTags;
import util.tags.InteractiveTags;
import util.annotations.Tags;

public class ASimpleList<ElementType> implements SimpleList<ElementType> {
	List<ElementType> simpleList = new ArrayList();
	List<ListObserver<ElementType>> observers = new ArrayList();
	
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
	
	public void notifyAdd(List<ListObserver<ElementType>> observers, int index,
			ElementType newValue) {
		ListEditNotified.newCase(OperationName.ADD, index, newValue, ApplicationTags.IM, this);
		for (ListObserver<ElementType> observer : observers) {
			observer.elementAdded(index, newValue,simpleList);
		}
	}
	
	protected void traceAdd(int anIndex, ElementType anElement) {
		ListEditMade.newCase(OperationName.ADD, anIndex,anElement, ApplicationTags.IM, this);
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
		observers.add((ListObserver<ElementType>)anObserver);
	}

	@Override
	public ElementType remove(int anIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean remove(ElementType anElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean observableRemove(ElementType anElement) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ElementType observableRemove(int anIndex) {
		// TODO Auto-generated method stub
		return null;
	}

}
