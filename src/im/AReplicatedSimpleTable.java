package im;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import echo.modular.GUIInteractor;
import trace.echo.ListEditMade;
import trace.echo.modular.ListEditNotified;
import trace.echo.modular.OperationName;
import trace.im.TableChangeSent;
import trace.table.TableChangeMade;
import trace.table.TableChangeNotified;
import util.session.Communicator;
import util.tags.ApplicationTags;

public class AReplicatedSimpleTable implements ReplicatedSimpleTable {
	Map<String, String> statusMap = new HashMap<String, String>();
	List<TableObserver> observers = new ArrayList();

	Communicator communicator;

	public AReplicatedSimpleTable(Communicator theCommunicator) {
		communicator = theCommunicator;
	}

	public Map<String, String> getStatusMap() {
		return statusMap;
	}

	@Override
	public synchronized void observableAdd(String userName, String value) {
		add(userName, value);
		notifyAdd(userName, value);
	}

	public synchronized void add(String userName, String value) {
		// if status already exists change it or add it
		String oldValue = null;
		if (statusMap.get(userName) == null) {
			oldValue = "NULL";
		} else {
			oldValue = statusMap.get(userName);
		}
		traceAdd(userName, oldValue, value);
		statusMap.put(userName, value);

	}

	public void notifyAdd(String userName, String value) {

		notifyAdd(observers, userName, value);
	}

	public void notifyAdd(List<TableObserver> observers, String userName,
			String value) {
		//ListEditNotified.newCase(OperationName.ADD, index, newValue, ApplicationTags.IM, this);
		TableChangeNotified.newCase(userName, "", value, ApplicationTags.IM, this);
		for (TableObserver observer : observers) {
			
			//WRONG SEND, SEND ENTIRE MAP instead
			observer.elementAddedInStatus(userName, value);
		}
	}

	public synchronized void replicatedAdd(String userName, String value) {
		observableAdd(userName, value);
		if (communicator == null)
			return;
		TableEdit tableEdit = new ATableEdit(OperationName.ADD, userName,
				value, ApplicationTags.IM);
		
		TableChangeSent.newCase(userName, "", value, ApplicationTags.IM, this);
		communicator.toOthers(tableEdit);
	}

	public void addObserver(GUIInteractor anObserver) {
		observers.add((TableObserver) anObserver);
	}

	protected void traceAdd(String userName, String oldValue, String newValue) {
		// ListEditMade.newCase(OperationName.ADD, anIndex,anElement,
		// ApplicationTags.IM, this);
		TableChangeMade.newCase(userName, oldValue, newValue,
				ApplicationTags.IM, this);
	}

	@Override
	public String toString() {
		StringBuffer sb = null;
		for (Map.Entry<String, String> entry : statusMap.entrySet()) {
			String username = entry.getKey();
			String value = entry.getValue();
			sb.append(username + " : " + value + "\n");
		}
		return sb.toString();
	}
}
