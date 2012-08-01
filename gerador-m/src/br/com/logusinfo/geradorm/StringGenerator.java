package br.com.logusinfo.geradorm;

import java.io.CharArrayWriter;
import java.util.Map;

import org.eclipse.core.variables.IValueVariable;
import org.eclipse.core.variables.VariablesPlugin;

import br.com.logusinfo.geradorm.model.DAOIfModel;
import br.com.logusinfo.geradorm.model.DAOModel;
import br.com.logusinfo.geradorm.model.Model;
import br.com.logusinfo.geradorm.model.VOModel;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;

@SuppressWarnings("rawtypes")
public class StringGenerator {
	
	private Configuration cfg;	
	private Map root = null;
	private Template temp = null;

	@SuppressWarnings("unchecked")
	public StringGenerator(Model model) {
		try {
			root = Util.convertModel(model);			
			root.put("user", getUserName());			
			cfg = new Configuration();
			cfg.setClassForTemplateLoading(StringGenerator.class, "/");		
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			cfg.setObjectWrapper(new DefaultObjectWrapper());
			if(model instanceof DAOModel){
				temp = cfg.getTemplate("templates/DAO.ftl");
			} else if (model instanceof DAOIfModel){
				temp = cfg.getTemplate("templates/DAOInterface.ftl");
			} else if (model instanceof VOModel){
				temp = cfg.getTemplate("templates/VO.ftl");	
			}
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}			
	}
	
	public String generate(){
		CharArrayWriter caw = new CharArrayWriter();			
	    try {	    	
			temp.process(root, caw);
		} catch (Exception e) {
			throw new RuntimeException(e); 
		}
	    caw.flush();
	    return caw.toString();
	}
	
	private String getUserName(){
		String s = null;
		s = System.getProperty("user.name");
		if(	s!= null){
			return s;
		}
		s = System.getProperty("user");
		if(	s!= null){
			return s;
		}
		IValueVariable var;
		var  = VariablesPlugin.getDefault().getStringVariableManager().getValueVariable("user");
		if(var != null){
			return var.getValue();
		}
		var = VariablesPlugin.getDefault().getStringVariableManager().getValueVariable("user.name");
		if(var != null){
			return var.getValue();
		}
		return " ";
	}
}
