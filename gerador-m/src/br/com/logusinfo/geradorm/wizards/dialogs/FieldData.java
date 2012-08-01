package br.com.logusinfo.geradorm.wizards.dialogs;

import org.eclipse.jdt.core.IType;



public class FieldData {
	public IType type;
	public String name;
	public String colName;
	public String description;
	public FieldData() {
	
	}
	public FieldData(IType type, String name, String description, String colName) {
		super();
		this.type = type;
		this.name = name;
		this.colName = colName;
		this.description = description;
	}		
}