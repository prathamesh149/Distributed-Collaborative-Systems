package locked.window;

import java.io.Serializable;

public class AMasterLockGrant implements Serializable {

	String user;
	
	public AMasterLockGrant(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
