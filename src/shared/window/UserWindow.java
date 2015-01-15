package shared.window;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Container;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.TextField;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JFrame;

import echo.modular.IMCreator;
import trace.sharedWindow.ComponentTreeRegistered;
import util.awt.AnExtendibleAWTEventQueue;
import util.awt.AnExtendibleTelePointerGlassPane;
import util.awt.ComponentRegistry;
import util.awt.ExtendibleTelepointerGlassPane;
import util.awt.GlassPaneController;

public class UserWindow {

	protected JFrame window;
	protected JFrame customizableFrame;

	public void createAndShowFrames() {
		
		IMCreator imc = new IMCreator();
		window = imc.launchIM("I-Messenger");
		

		// Create a Telepointer Glass pane and a customizable frame
		AnExtendibleTelePointerGlassPane glassPane = new AnExtendibleTelePointerGlassPane(window);
				
		GlassPaneController gpc = glassPane.getGlassPaneController();
		ACustomizableFramePointer cfp = new ACustomizableFramePointer(gpc);
		customizableFrame = cfp.createPointerFrame();
		
		//Add a painter class to draw telepointer
		glassPane.addPainter(new ATelePointerPainter(glassPane));

		
		window.setGlassPane(glassPane);
		glassPane.setVisible(true);

		registerComponents(window);
		registerComponents(customizableFrame);
		
	}
	
	public void registerComponents(Container frame) {
		ComponentRegistry.register(frame);
		List<Component> a = Utils.getAllComponents(frame);
		for ( Component c: a) {
			ComponentRegistry.register(c);
		}
		System.out.println();
	}
	

}
