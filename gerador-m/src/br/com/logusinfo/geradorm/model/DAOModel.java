package br.com.logusinfo.geradorm.model;


public class DAOModel extends VOModel {
	
	private String daoInterfacePackageName;
	private String voPackageName;
	
	public String getDaoInterfacePackageName() {
		return daoInterfacePackageName;
	}
	public void setDaoInterfacePackageName(String daoInterfacePackageName) {
		this.daoInterfacePackageName = daoInterfacePackageName;
	}
	public String getVoPackageName() {
		return voPackageName;
	}
	public void setVoPackageName(String voPackageName) {
		this.voPackageName = voPackageName;
	}	
	
}
