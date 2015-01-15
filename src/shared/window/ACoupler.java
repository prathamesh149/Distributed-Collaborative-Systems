package shared.window;

import java.awt.AWTEvent;
import java.awt.Component;

import trace.sharedWindow.AWTEventReceived;
import trace.sharedWindow.ReceivedAWTEventDispatched;
import util.awt.AWTEventQueueHandler;
import util.awt.AnExtendibleAWTEventQueue;
import util.awt.ComponentRegistry;
import util.awt.ExtendibleAWTEventQueue;
import util.awt.SerializableAWTEvent;
import util.session.PeerMessageListener;
import util.tags.ApplicationTags;

public class ACoupler implements PeerMessageListener {

	AnExtendibleAWTEventQueue queue;

	public ACoupler(AnExtendibleAWTEventQueue queue) {
		this.queue = queue;
	}

	@Override
	public void objectReceived(Object message, String userName) {

		if (message instanceof SerializableAWTEvent) {
			processReceivedEvent((SerializableAWTEvent) message);
		} 
	}

	private void processReceivedEvent(SerializableAWTEvent message) {

		//Received AWT event
		AWTEventReceived.newCase(message.getAWTEvent(), message.getSource(), "", this);
		
		// Get the component from Registry map and set the appropriate source
		// and dispatch on the queue		
		int componentId = Integer.parseInt(message.getSource());
		Component component = ComponentRegistry.getComponent(componentId);

		// queue.dispatchReceivedEvent(event);
		AWTEvent event = queue.getCommunicatedEventSupport().toDispatchedEvent(message,component);
		queue.dispatchReceivedEvent(event);
		
		//ReceivedAWTEventDispatched.newCase(event, message.getSource(), component.getName(),this);
	}

}
