package im;

import echo.modular.SimpleList;
import echo.modular.ListObserver;

public interface ReplicatedSimpleList<ElementType> extends SimpleList<ElementType> {
	void replicatedAdd(ElementType newVal);


}
