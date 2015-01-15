package locked.window;

import java.io.Serializable;

public class ASlaveLockGrantRequest implements Serializable{

	String user;
	
	public ASlaveLockGrantRequest(String aUser) {
		user = aUser;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}
		
}
