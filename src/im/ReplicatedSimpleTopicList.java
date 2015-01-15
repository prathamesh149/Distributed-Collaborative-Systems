package im;

import echo.modular.SimpleList;

public interface ReplicatedSimpleTopicList<ElementType> extends SimpleList<ElementType> {

	void replicatedAdd(int anIndex, ElementType newValue);

	void replicatedRemove(int anIndex);
	
}
