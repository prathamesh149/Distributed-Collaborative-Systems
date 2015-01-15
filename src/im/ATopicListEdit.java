package im;

import trace.echo.ListEditInfo;
import trace.echo.modular.OperationName;

public class ATopicListEdit<ElementType> implements TopicListEdit<ElementType>{
	OperationName name;	
	int index;
	ElementType element;
	String list;
	
	public ATopicListEdit (OperationName theName, int theIndex, ElementType theElement, String aList) {
		index = theIndex;
		element = theElement;
		name = theName;
		list = aList;
	}	
	
	public int getIndex() {
		return index;
	}
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	public ElementType getElement() {
		return element;
	}
	
	public void setElement(ElementType anElement) {
		this.element = anElement;
	}
	public OperationName getOperationName() {
		return name;
	}
	public void setOperationName(OperationName anOperationName) {
		this.name = anOperationName;
	}
	public String toString() {
		return "[" + index + "; " + element + "]"; 
	}
	public TopicListEdit copy() {
		return new ATopicListEdit(name, index, element, list);
	}
	
	public ListEditInfo toListEditInfo() {
		return new ListEditInfo(name, index, list, element );
	}
	public String getList() {
		return list;
	}
	public void setList(String list) {
		this.list = list;
	}
	
}
