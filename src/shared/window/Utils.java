package shared.window;

import java.awt.Component;
import java.awt.Container;
import java.util.ArrayList;
import java.util.List;

public class Utils {
	  public static List<Component> getAllComponents(final Container c) {
	    Component[] comps = c.getComponents();
	    List<Component> compList = new ArrayList<Component>();
	    for (Component comp : comps) {
	      compList.add(comp);
	      if (comp instanceof Container) {
	        compList.addAll(getAllComponents((Container) comp));
	      }
	    }
	    return compList;
	  }

}