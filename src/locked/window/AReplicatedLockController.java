package locked.window;

import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JTextField;

public class AReplicatedLockController {

	JFrame frame;
	JTextField lockHolder;
	JButton requestLock;
	JButton releaseLock;
	JCheckBox implicitLock;
		
	public JFrame createLockControllerFrame(ALocalModelCache cache) {
		frame = new JFrame("AReplicatedLockController");
		frame.setName("AReplicatedLockController");
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(4, 1));
		
		lockHolder = new JTextField();
		lockHolder.setBorder(BorderFactory.createTitledBorder("Current Lock Holder"));
		lockHolder.setEditable(false);
		lockHolder.setSize(10, 10);
		
		requestLock = new JButton("Request Lock");
		requestLock.addActionListener(cache);
		
		releaseLock = new JButton("Release Lock");
		releaseLock.addActionListener(cache);
		
		implicitLock = new JCheckBox("Implicit Lock");
		implicitLock.addItemListener(cache);
		
		frame.add(lockHolder);
		frame.add(requestLock);
		frame.add(releaseLock);
		frame.add(implicitLock);
		
		
		frame.setSize(200, 200);
		frame.setVisible(true);
		return frame;
	}
	

}