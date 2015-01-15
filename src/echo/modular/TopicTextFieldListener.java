package echo.modular;

import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

public class TopicTextFieldListener implements DocumentListener{

	private MainPanel mainpanel;
	private boolean active;
	
	public TopicTextFieldListener(MainPanel mainpanel, boolean value) {
		this.mainpanel = mainpanel;
		this.active = value;
	}
	
	public void setActive(boolean value) {
		active = value;
	}
	
	private void runner(Character newValue, int index) {
		Runnable doRun = new Runnable() {
			
			@Override
			public void run() {
				if (active) {
					mainpanel.addTopicCharacter(newValue, index);
				}
			}
		};
		SwingUtilities.invokeLater(doRun);
	}
	
	@Override
	public void insertUpdate(DocumentEvent e) {
		String value = null;
		Character newValue = null;
		int index = e.getOffset();
		
		try {
			value = e.getDocument().getText(e.getOffset(), 1);
			newValue = new Character(value.charAt(0));
		} catch (BadLocationException e1) {
			e1.printStackTrace();
		}
		//Edit the document is different event dispatch thread
		runner(newValue,index); 		
	}

	public void runnerThread(int index) {
		Runnable doRun = new Runnable() {
			
			@Override
			public void run() {
				if (active) {
					mainpanel.removeTopicCharacter(index);
				}
			}
		};
		SwingUtilities.invokeLater(doRun);
	}
	
	@Override
	public void removeUpdate(DocumentEvent e) {	
		runnerThread(e.getOffset());
	}

	@Override
	public void changedUpdate(DocumentEvent e) {
		
		
	}
	
}
