package ${packageName};

<#list imports as import>import ${import};
</#list>
import logus.util.model.ValidationException;
import logus.util.model.ValueObject;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import com.logus.cache.Cacheable;

/**
* VO que representa os dados de ${description}
* @author ${user}
*/
@SuppressWarnings("serial")
@Cacheable()
public class ${entityName}VO extends ValueObject {
  	<#list fields as field>
  	
	/**
	* ${field.description}
	*/
  	private ${field.type} ${field.name};  
  	</#list>
  	
  	public ${entityName}VO(){
  	
  	}
  	
  	public ${entityName}VO(
  			<#list fields as field>${field.type} ${field.name}<#if field_has_next>,
  			</#if></#list>){
  			
  		<#list fields as field>  		
  		this.${field.name} = ${field.name};
  		</#list>
  	}
  	  	
	
	/**
	*  Equals compara todos os campos do VO
	*/
	public boolean equals(Object other) {
	  return EqualsBuilder.reflectionEquals(this, other, false);    
	}
  
	/**
	 * Hash code do VO utilizando todos os campos do vo	 
	 */
	@Override
	public int hashCode() {	
	  	return HashCodeBuilder.reflectionHashCode(this, false);	
	}
	
	/**
	* Retorna a chave do ${description}
	* @return array contendo a chave do ${description}
	*/
	public Object[] keyOf() {		
		//TODO Implementar a chave
		throw new UnsupportedOperationException("Método keyOf não implementado: "+getClass().getName());
	}	
	
	/**
	* Realiza a validações do objeto que não dependem de colaboração com
	* objetos que não façam parte deste.
	*
	* @throws logus.util.model.ValidationException
	*/
	@Override
	public void validate()	throws ValidationException {
		//TODO Implementar validate
	}
	
	<#list fields as field>  	
	
  	public ${field.type} get${field.name?capitalize}(){
  		return this.${field.name};
  	}  
  	
  	public void set${field.name?capitalize}(${field.type} ${field.name}){
  		this.${field.name} = ${field.name};
  	}
  	
  	</#list>
}
