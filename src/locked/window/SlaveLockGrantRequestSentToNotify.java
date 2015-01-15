package locked.window;

import java.io.Serializable;

public class SlaveLockGrantRequestSentToNotify implements Serializable{
	String user;
	
	public SlaveLockGrantRequestSentToNotify(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
}
