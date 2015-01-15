package echo.modular;
import java.awt.*;

import javax.swing.*;

import shared.window.ComponentRegistryMap;
import echo.modular.GUIInteractor;

public class IMCreator {

	protected MainPanel mainpanel;
	protected TopicTextFieldListener topicTextFieldListener;
	
	public MainPanel getMainpanel() {
		return mainpanel;
	}

	public TopicTextFieldListener getTopicTextFieldListener() {
		return topicTextFieldListener;
	}

	// Adds components to the Panel
	public MainPanel createPanel(String title) {

		mainpanel = new MainPanel();
		mainpanel.setTitle(title);
		mainpanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainpanel.setName("IMWindow");
		
		mainpanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();

		// Adding Topic TextField
		mainpanel.setTopic(new JTextField());
		mainpanel.getTopic().setBorder(
				BorderFactory.createTitledBorder("Topic"));
		mainpanel.getTopic().setName("topic");
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 0;
		// Add to Content Pane
		mainpanel.add(mainpanel.getTopic(), c);
		topicTextFieldListener = new TopicTextFieldListener(mainpanel, true);
		mainpanel.getTopic().getDocument().addDocumentListener(topicTextFieldListener);

		// Adding Status TextField

		mainpanel.setStatus(new JTextField());
		mainpanel.getStatus().setBorder(
				BorderFactory.createTitledBorder("Status"));
		mainpanel.getStatus().setName("status");
		mainpanel.getStatus().setEditable(false);
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 1;
		// Add to Content Pane
		mainpanel.add(mainpanel.getStatus(), c);

		// Adding History TextArea
		
		mainpanel.setHistory(new JTextArea());
		mainpanel.getHistory().setEditable(false);
		mainpanel.getHistory().setName("history");

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 2;
		mainpanel.add(mainpanel.getHistory(),c);
		//mainpanel.add(mainpanel.getHistory(), c);

		// Adding Aware TextField
		mainpanel.setAware(new JTextField());
		mainpanel.getAware().setBorder(
				BorderFactory.createTitledBorder("Aware Message"));
		mainpanel.getAware().setName("aware");
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 3;
		mainpanel.add(mainpanel.getAware(), c);

		AwareTextFieldListener atfl = new AwareTextFieldListener(mainpanel);
		mainpanel.getAware().addActionListener(atfl);
		mainpanel.getAware().getDocument().addDocumentListener(atfl);

		// Adding Unaware AWT TextField
		mainpanel.setUnaware(new TextField("Unaware Message"));
		mainpanel.getUnaware().setName("unaware");
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 0.0;
		c.gridx = 0;
		c.gridy = 4;
		mainpanel.add(mainpanel.getUnaware(), c);

		mainpanel.getUnaware().addActionListener(new UnawareTextFieldListener(mainpanel));
		
		//Adding Status Box text area
		mainpanel.setStatusBox(new JTextArea());
		mainpanel.getStatusBox().setEditable(false);
		mainpanel.getStatusBox().setName("statusbox");

		c.fill = GridBagConstraints.BOTH;
		c.weighty = 1.0;
		c.weightx = 1.0;
		c.gridx = 0;
		c.gridy = 5;
		mainpanel.add(mainpanel.getStatusBox(),c);
		
		//Register the components
		ComponentRegistryMap.register(mainpanel);
		ComponentRegistryMap.register(mainpanel.getTopic());
		ComponentRegistryMap.register(mainpanel.getStatus());
		ComponentRegistryMap.register(mainpanel.getHistory());
		ComponentRegistryMap.register(mainpanel.getAware());
		ComponentRegistryMap.register(mainpanel.getUnaware());
		ComponentRegistryMap.register(mainpanel.getStatusBox());
		
		
		//Display the frame
		mainpanel.setVisible(true);
		mainpanel.setSize(300, 300);
		return mainpanel;
	}

	public JFrame launchIM(String title) {
		JFrame mp = createPanel(title);
		return mp;
	}
};