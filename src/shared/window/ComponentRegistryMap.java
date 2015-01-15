package shared.window;

import java.awt.Component;
import java.util.HashMap;
import java.util.Map;

public class ComponentRegistryMap {
	static Map<String,Component> components = new HashMap<String,Component>();
	
	public static void register(Component theFrame) {
		components.put(theFrame.getName(), theFrame);
	}
	
	public static Component getComponent(String name) {
		return components.get(name);
	}

	public static String getComponentId(Component theComponent) {
		return theComponent.getName();
	}
	
}
