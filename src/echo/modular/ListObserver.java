package echo.modular;

import java.util.List;

public interface ListObserver<ElementType> extends Observer<ElementType>{
	void elementAdded(int anIndex, ElementType aNewValue);
	
	void elementAdded(int anIndex, ElementType aNewValue, List list);
		
	void elementRemoved(int anIndex, ElementType aNewValue);
}