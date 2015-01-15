package im;

import java.util.Map;

import echo.modular.GUIInteractor;
import echo.modular.Observer;

public interface ReplicatedSimpleTable {
	
	void replicatedAdd(String userName, String newVal);
	void observableAdd(String userName, String value);
	void addObserver(GUIInteractor guiInteractor);
	
	public Map<String, String> getStatusMap();
}
