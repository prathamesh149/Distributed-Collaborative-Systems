package echo.modular;

public interface TopicListObserver<ElementType> extends Observer<ElementType>{
	void elementAddedInTopic(int anIndex, ElementType aNewValue);

	void elementRemovedFromTopic(int anIndex, ElementType aNewValue);
}
