package shared.window;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingUtilities;

import trace.sharedWindow.AWTEventSent;
import util.awt.ASerializableAWTEvent;
import util.awt.AWTEventQueueHandler;
import util.awt.AWTMisc;
import util.awt.ComponentRegistry;
import util.awt.SerializableAWTEvent;
import util.session.Communicator;

public class AnEventHandlerAndForwarder implements AWTEventQueueHandler{

	Communicator communicator;
	
	public AnEventHandlerAndForwarder(Communicator theCommunicator) {
		communicator = theCommunicator;
	}
	
	@Override
	public void newEvent(AWTEvent aEvent) {
		Component theComponent = (Component)aEvent.getSource();
		
		//Implement the window filter feature here
		String windowName = SwingUtilities.getRoot(theComponent).getName();
		if (windowName.equalsIgnoreCase("FilterWindow")) {
			return;
		}
		
		
		try {
		int id = ComponentRegistry.getComponentId(theComponent);
		//Send to others
			if (id != -1) {
				if (communicator == null) return;		
				SerializableAWTEvent event = new ASerializableAWTEvent(aEvent, Integer.toString(id));
				AWTEventSent.newCase(aEvent, Integer.toString(id), event.getSource(), this);
				communicator.toOthers(event);
			}	
		} catch(Exception e) {
			System.out.println("Component not registered");
		}
				
	}
	
}
