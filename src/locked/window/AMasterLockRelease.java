package locked.window;

import java.io.Serializable;

public class AMasterLockRelease implements Serializable{
	
	String user;
	
	public AMasterLockRelease(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
