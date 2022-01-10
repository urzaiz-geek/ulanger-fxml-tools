package com.urzaizcoding.ulangerproxy;

import com.urzaizcoding.ulangerproxy.LanguageParser.LField;

public interface LanguageHandlable {
	public void setText(LField field) throws Exception;
	public void updateFieldsAnText() throws Exception;
}
