package com.urzaizcoding.ulangerproxy.lang;

import com.urzaizcoding.ulangerproxy.lang.LanguageParser.LField;

public interface LanguageHandlable {
	public void setText(LField field) throws Exception;
	public void updateFieldsAnText() throws Exception;
}
