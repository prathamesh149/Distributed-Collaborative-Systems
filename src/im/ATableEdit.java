package im;

import trace.echo.modular.OperationName;

public class ATableEdit implements TableEdit{
	OperationName name;	
	String username;
	String value;
	String list;
	
	public ATableEdit(OperationName theName, String aUsername, String aValue , String aList) {
		
		name = theName;
		username = aUsername;
		value = aValue;
		list = aList;
	}	
		
	public OperationName getName() {
		return name;
	}

	public void setName(OperationName name) {
		this.name = name;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getList() {
		return list;
	}

	public void setList(String list) {
		this.list = list;
	}


}
