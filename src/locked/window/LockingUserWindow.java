package locked.window;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import shared.window.UserWindow;

public class LockingUserWindow extends UserWindow{

	protected AReplicatedLockController lockController;
	protected ALocalModelCache cache;
	
	public LockingUserWindow(ALocalModelCache cache) {
		this.cache = cache;
	}
		
	public AReplicatedLockController getLockController() {
		return lockController;
	}

	@Override
	public void createAndShowFrames() {
		super.createAndShowFrames();
		
		lockController = new AReplicatedLockController();
		lockController.createLockControllerFrame(cache);
		
	}
	
	public static void main(String[] args) {
		
	}
}
