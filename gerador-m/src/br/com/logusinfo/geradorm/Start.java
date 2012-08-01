package br.com.logusinfo.geradorm;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashSet;

import br.com.logusinfo.geradorm.model.DAOModel;
import br.com.logusinfo.geradorm.model.FieldModel;
import br.com.logusinfo.geradorm.model.VOModel;
import freemarker.template.TemplateException;

public class Start {
	
	
	public static void main(String[] args) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException, IOException, TemplateException {		
		
		DAOModel voModel = new DAOModel();
		voModel.setVoPackageName("asdasd");
		voModel.setDaoInterfacePackageName("aasdasd");
		voModel.setEntityName("TipoEstojo");
		voModel.setDescription("Tipo de Estojo");
		voModel.setPackageName("logus.siplag.model.apoioSiplag.apoioExecucao");
		voModel.setImports(new HashSet<String>(Arrays.asList(new String[]{				
				"logus.util.model.ValidationException",
				"logus.util.model.ValueObject",
				"org.apache.commons.lang3.builder.EqualsBuilder",
				"org.apache.commons.lang3.builder.HashCodeBuilder",
				"com.logus.cache.Cacheable"
		})));
		
		voModel.getFields().add(new FieldModel("codigo","Código","Integer","", Integer.class.getName()));		
		voModel.getFields().add(new FieldModel("nome","Nome","String","", String.class.getName()));			
		
		System.out.println(new StringGenerator(voModel).generate());
	}

	
	

}
