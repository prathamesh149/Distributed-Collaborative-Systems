package im;

import java.io.Serializable;

import trace.echo.ListEditInfo;
import trace.echo.modular.OperationName;

public interface TopicListEdit<ElementType> extends Serializable {	
	int getIndex();
	void setIndex(int anIndex);
	ElementType getElement();
	void setElement(ElementType anElement);
	OperationName getOperationName();
	void setOperationName(OperationName name);
	public String getList() ;
	public void setList(String list) ;
	TopicListEdit copy();
	public ListEditInfo toListEditInfo();

}