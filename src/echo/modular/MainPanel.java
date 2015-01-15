package echo.modular;
import im.ATopicListEdit;
import im.TopicListEdit;

import java.awt.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.annotation.ElementType;

import javax.swing.*;

import trace.echo.modular.OperationName;
import util.tags.ApplicationTags;


public class MainPanel extends JFrame {

	private static final long serialVersionUID = 1L;
	
	private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
	
	public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
	
	private JTextField topic;
	private JTextField status;
	private JTextArea history; 
	private JTextField aware;
	private TextField unaware;
	
	private JTextArea statusBox;

	

	public MainPanel() {
		
	}
	
	//Topic
	public JTextField getTopic() {
		return topic;
	}

	public void setTopic(JTextField topic) {
		this.topic = topic;
	}

	public void addTopicCharacter(Character newValue, int index) {
		//Fill oldValue by random character as its not used
		//Character oldValue = '*';
		
		TopicListEdit listEdit = new ATopicListEdit<Character>(OperationName.ADD, index, newValue, ApplicationTags.EDITOR);
		
		this.pcs.firePropertyChange("topic", null, listEdit);
		
	}
	
	public void removeTopicCharacter(int index) {
		
		TopicListEdit listEdit = new ATopicListEdit<Character>(OperationName.DELETE, index, null, ApplicationTags.EDITOR);
		//Character oldValue = '*';
		//Character newValue = '^';
		this.pcs.firePropertyChange("topic", null, listEdit);
		
	}
	
	//Status
	public JTextField getStatus() {
		return status;
	}

	public void setStatus(JTextField status) {
		this.status = status;
	}
	
	public void setStatusText(String newValue) {		
		String oldValue = status.getText();
		status.setText(newValue);		
		this.pcs.firePropertyChange("status", oldValue , newValue);
	}

	//History
	public JTextArea getHistory() {
		return history;
	}

	public void setHistory(JTextArea history) {
		this.history = history;
	}
	
	public void addHistoryText(String newValue) {
		String oldValue = history.getText();
		this.pcs.firePropertyChange("history", oldValue, newValue);
	}
	 
	//Aware
	public JTextField getAware() {
		return aware;
	}

	public void setAware(JTextField aware) {
		this.aware = aware;
	}
	
	public void setAwareText(String newValue) {
		aware.setText(newValue);
	}
		
	//Unaware
	public TextField getUnaware() {
		return unaware;
	}

	public void setUnaware(TextField unaware) {
		this.unaware = unaware;
	}
	
	public void setUnawareText(String newValue) {
		unaware.setText(newValue);
	}
	
	//Status Box
	public JTextArea getStatusBox() {
		return statusBox;
	}

	public void setStatusBox(JTextArea statusBox) {
		this.statusBox = statusBox;
	}
	
};
