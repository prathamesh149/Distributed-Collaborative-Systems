package shared.window;

import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import util.awt.GlassPaneController;

public class ACustomizableFramePointer implements ChangeListener, ItemListener {

	GlassPaneController gpc;
	
	JFrame frame;
	JCheckBox showTelePointer;
	JSlider sizeSlider;
	JSlider widthSlider;
	JSlider heightSlider;

	
	public ACustomizableFramePointer(GlassPaneController gpc) {
		this.gpc = gpc;
	}

	public JFrame createPointerFrame() {
		frame = new JFrame("AGlassPaneController");
		frame.setName("CustomizableWindow");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(new GridLayout(4, 1));

		showTelePointer = new JCheckBox("Show Tele Pointer");
		showTelePointer.setName("showTelePointer");
		showTelePointer.addItemListener(this);

		//sizeSlider = new JSlider(10,10);
		//sizeSlider.addChangeListener(this);

		widthSlider = new JSlider(10, 100);
		widthSlider.setName("widthSlider");
		widthSlider.addChangeListener(this);

		heightSlider = new JSlider(10, 100);
		heightSlider.setName("heightSlider");
		heightSlider.addChangeListener(this);

		frame.add(showTelePointer);
		//frame.add(sizeSlider);
		frame.add(widthSlider);
		frame.add(heightSlider);

		frame.setSize(300, 300);
		frame.setVisible(true);
		
		return frame;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		JSlider slider = (JSlider) e.getSource();

		if (slider == sizeSlider) {
			gpc.setPointerSize(slider.getValue());
		} else if (slider == widthSlider) {
			gpc.setPointerWidth(slider.getValue());
		} else if (slider == heightSlider) {
			gpc.setPointerHeight(slider.getValue());
		}

	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getStateChange() == ItemEvent.SELECTED) {
			gpc.setShowTelePointer(true);
		} else {
			gpc.setShowTelePointer(false);
		}

	}

}
