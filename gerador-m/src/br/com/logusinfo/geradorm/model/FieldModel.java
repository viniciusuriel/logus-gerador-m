package br.com.logusinfo.geradorm.model;

public class FieldModel {

	private String name;
	private String description;
	private String type;
	private String typeFullName;
	private String columnName;

	public FieldModel(String name, String description, String type, String columnName, String typeFullName) {
		super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.columnName = columnName;
		this.typeFullName = typeFullName;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getTypeFullName() {
		return typeFullName;
	}

	public void setTypeFullName(String typeFullName) {
		this.typeFullName = typeFullName;
	}
		
}
