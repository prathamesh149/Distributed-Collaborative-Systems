package im;

import java.io.Serializable;

import trace.echo.modular.OperationName;

public interface TableEdit extends Serializable {
	public OperationName getName();
	public void setName(OperationName name);
	public String getUsername();
	public void setUsername(String username);
	public String getValue();
	public void setValue(String value);
	public String getList();
	public void setList(String list);
	
}

