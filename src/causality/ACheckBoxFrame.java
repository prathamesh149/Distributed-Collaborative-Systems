package causality;

import java.awt.GridLayout;

import javax.swing.JCheckBox;
import javax.swing.JFrame;

public class ACheckBoxFrame {
	
	JFrame frame;

	public void createAndShowCheckBoxFrame(ACausalityManager causalityManager) {
		
		frame = new JFrame("ACheckBoxFrame");
		frame.setName("ACheckBoxFrame");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(1, 1));
		
		JCheckBox causal = new JCheckBox("Causal");
		causal.addItemListener(causalityManager);
		frame.add(causal);
		
		
		frame.setSize(200, 50);
		frame.setVisible(true);		
	}
 	
	public static void main(String[] args) {
		ACheckBoxFrame c = new ACheckBoxFrame();
		//c.createAndShowCheckBoxFrame();
	}
	
}
