package ${packageName};

import ${voPackageName}.${entityName}VO;

import logus.util.model.DataAccessObject;

public interface ${entityName}DAOInterface extends DataAccessObject<${entityName}VO> {


	public static final String TABLE_NAME = "${tableName}";
	
	public static final String ANO_EXERCICIO_CTX = "ANO_EXERCICIO_CTX";
	public static final String COD_CLIENTE_CTX= "COD_CLIENTE_CTX";
	<#list fields as field>
  	public static final String COL_${field.columnName} = "${field.columnName}";
  	</#list>
	
	
}
