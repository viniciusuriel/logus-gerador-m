package br.com.logusinfo.geradorm.model;

import java.util.ArrayList;
import java.util.List;


public class DAOIfModel extends VOModel {

	private String tableName;
	private String entityName;
	private String voPackageName;
	private String packageName;
	private List<FieldModel> fields = new ArrayList<FieldModel>(0);
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getEntityName() {
		return entityName;
	}
	public void setEntityName(String entityName) {
		this.entityName = entityName;
	}
	public String getVoPackageName() {
		return voPackageName;
	}
	public void setVoPackageName(String voPackageName) {
		this.voPackageName = voPackageName;
	}
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public List<FieldModel> getFields() {
		return fields;
	}
	public void setFields(List<FieldModel> fields) {
		this.fields = fields;
	}
	
	
}
