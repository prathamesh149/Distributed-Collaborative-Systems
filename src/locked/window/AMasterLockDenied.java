package locked.window;

import java.io.Serializable;

public class AMasterLockDenied implements Serializable{
	String user;
	
	public AMasterLockDenied(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
