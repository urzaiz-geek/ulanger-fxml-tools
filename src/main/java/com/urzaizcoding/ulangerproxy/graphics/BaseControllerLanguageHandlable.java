package com.urzaizcoding.ulangerproxy.graphics;

import static com.urzaizcoding.ulangerproxy.exceptions.ExceptionWrappers.consumerWrapper;
import static com.urzaizcoding.ulangerproxy.lang.LanguageParser.getField;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.urzaizcoding.ulangerproxy.exceptions.ExceptionWrappers.ThrowingConsumer;
import com.urzaizcoding.ulangerproxy.lang.LanguageHandlable;
import com.urzaizcoding.ulangerproxy.lang.LanguageParser;
import com.urzaizcoding.ulangerproxy.lang.LanguageParser.LField;
import com.urzaizcoding.ulangerproxy.log.Logger;

public class BaseControllerLanguageHandlable extends BaseDraggableController implements LanguageHandlable {
	
	private LanguageParser currentLanguage;
	
	
	
	public BaseControllerLanguageHandlable(LanguageParser currentLanguage) throws Exception {
		super();
		this.setCurrentLanguage(currentLanguage);
	}

	@Override
	public void setText(LField field) throws Exception {
		//get The field
		
		Field oField = getField(getClass(), field.getFieldId());
		
		
		//get The class of the field
		Class<?> fClass = oField.getType();
		
		//get the instance to modify
		
		Object instanceOfField = oField.get(this);
		
		//get the method to invoke
		
		Method methodToInvoke = fClass.getMethod(field.getMethodName(), String.class);
		
		//invoke the metod
		
		methodToInvoke.invoke(instanceOfField, field.getValue());
		
	}

	@Override
	public void updateFieldsAnText() throws Exception {
		
		ThrowingConsumer<LField> consumer = f -> setText(f);
		currentLanguage.getClassLanguage().stream()
				.filter(c -> c.getClassName().equals(this.getClass().getSimpleName()))
				.findFirst()
				.ifPresent(l -> l.getClassfields().forEach(consumerWrapper(consumer, Logger.loggerObject, Exception.class)));

	}

	public LanguageParser getCurrentLanguage() {
		return currentLanguage;
	}

	public void setCurrentLanguage(LanguageParser currentLanguage) throws Exception {
		this.currentLanguage = currentLanguage;
		updateFieldsAnText();
	}

}
