package locked.window;

import java.io.Serializable;

public class ASlaveLockReleaseRequest implements Serializable{
	
	String user;
	
	public ASlaveLockReleaseRequest(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
	
}
