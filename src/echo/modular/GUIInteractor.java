package echo.modular;

import java.beans.PropertyChangeListener;

public interface GUIInteractor extends ListObserver, PropertyChangeListener {

	void createAndLaunchGUI();


}
