package ot;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class OTServerCheckBoxFrame {
	
	JFrame frame;

	public void createAndShowCheckBoxFrame(AMasterOTTimeStampingSentMessageFilter sendFilter) {
		
		frame = new JFrame("OT Server Check Box");
		frame.setName("OTCheckBox");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		
		JCheckBox transform = new JCheckBox("Transform");
		transform.addItemListener(sendFilter);
		
		frame.add(transform);
		
		frame.setSize(200, 50);
		frame.setVisible(true);		
	}
 	
}
