package br.com.logusinfo.geradorm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtilsBean;
import org.apache.commons.beanutils.ConvertUtilsBean;
import org.apache.commons.beanutils.PropertyUtilsBean;
import org.apache.commons.lang.StringUtils;

public class Util {

	private static final String NONSTRINGVALUE = "#nontringvalue#";
	
	
	private static BeanUtilsBean beanutils = new BeanUtilsBean(new ConvertUtilsBean(){
		@Override
		public String convert(Object value) {
			if (!(value instanceof String)){
				return NONSTRINGVALUE;				
			}
			return value.toString();
		}				
	}, new PropertyUtilsBean());
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Map<String,Object> convertModel(Object model) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException{				
		Map<String,Object> root = beanutils.describe(model);			
		for (Map.Entry<String,Object> value : root.entrySet()) {
			if (NONSTRINGVALUE.equals(value.getValue())){
				Object col = getValue(model, value.getKey());
				if (col instanceof Collection){
					List<Object> list = new ArrayList<Object>();
					for(Object o : ((Collection)col)){
						if (o instanceof String){
							list.add(o);
						} else {
							list.add(convertModel(o));	
						}					
					}
					root.put(value.getKey(), list);							
				}
			} else {
				if (!(value.getValue() instanceof String)){
					throw new IllegalArgumentException("As classes do modelo só podem conter Strings e Collections");
				}
			}
		}
		return root;
	}
	
	private static Object getValue(Object target, String property){
		try{
			for ( Method m : target.getClass().getMethods()){
				if(m.getName().equals("get"+StringUtils.capitalize(property))){
					return m.invoke(target, (Object[])null);
				}
					
			}
		} catch (Exception e){
			throw new RuntimeException(e);
		}
		return null;
	}
}
