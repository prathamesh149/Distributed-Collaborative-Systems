package echo.modular;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class AwareTextFieldListener implements ActionListener, DocumentListener {

	private MainPanel mainpanel;

	public AwareTextFieldListener(MainPanel mainpanel) {
		this.mainpanel = mainpanel;
	}

	// Method of ActionListener
	@Override
	public void actionPerformed(ActionEvent e) {
		String message_line = e.getActionCommand();

		mainpanel.addHistoryText(message_line);
		
		mainpanel.setAwareText("");
		
		//Enter is pressed remove the status		
		mainpanel.setStatusText("");
		
		//Enter has been pressed, wake up all threads as user has entered
		synchronized (so) {
			so.notifyAll();
		}
		
	}

	Runnable t = new Runnable() {
		public void run() {
			mainpanel.setStatusText("Typed");
		}
	};

	public class SynchronizedObject {

	}
	SynchronizedObject so = new SynchronizedObject();
	
	public class StatusText implements Runnable {

		SynchronizedObject so;

		public StatusText(SynchronizedObject so) {
			this.so = so;
		}

		@Override
		public void run() {
			long timeout = 3000;
			long timeout_expire = System.nanoTime() / 1000000 + timeout;

			try {
				synchronized (so) {
					so.notifyAll();
					so.wait(timeout);
					if (System.nanoTime() / 1000000 >= timeout_expire) {
						//Wait time of 3000 ms is over and thread is not woken up
						EventQueue.invokeLater(t);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	};

	@Override
	public void insertUpdate(DocumentEvent e) {
		// Each time a character is inserted invoke a new thread
		mainpanel.getStatus().setForeground(Color.RED);
		mainpanel.setStatusText("Typing...");
		
		new Thread(new StatusText(so)).start();
	}

	@Override
	public void removeUpdate(DocumentEvent e) {
		
	}

	public void changedUpdate(DocumentEvent e) {

	}
};
