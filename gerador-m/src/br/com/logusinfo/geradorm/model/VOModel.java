package br.com.logusinfo.geradorm.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VOModel implements Model {

	private List<FieldModel> fields = new ArrayList<FieldModel>(0);
	private Set<String> imports = new HashSet<String>();
	private String entityName;	
	private String packageName;
	private String description;
		
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<FieldModel> getFields() {
		return fields;
	}

	public Set<String> getImports() {
		return imports;
	}
	
	public String getEntityName() {
		return entityName;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}

	public void setImports(Set<String> imports) {
		this.imports = imports;
	}
	
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

}
