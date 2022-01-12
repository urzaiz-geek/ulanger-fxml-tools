package com.urzaizcoding.ulangerproxy.lang;

import static org.junit.jupiter.api.Assertions.fail;
import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urzaizcoding.ulangerproxy.exceptions.IdNotFoundInClassException;
import com.urzaizcoding.ulangerproxy.graphics.BaseControllerLanguageHandlable;

import javafx.scene.Parent;

class LanguageParserTest {
	
	LanguageParser underTest;
	
	@BeforeEach
	void setUp() throws Exception {
		underTest = new LanguageParser(getClass().getResource("french.ulang").toURI());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testGetIds() {
		//given
		String [] expected = {"currentLanguage","root"};
		//when
		String [] result = LanguageParser.getIds(BaseControllerLanguageHandlable.class).toArray(new String [0]);
		//then
		assertThat(result).containsExactly(expected);
		
	}

	@Test
	void testGetFieldClass() throws Exception {
		//given
		Class<?> expected = Parent.class;
		//when
		
		Class<?> result = LanguageParser.getFieldClass(BaseControllerLanguageHandlable.class, "root");
		
		//then
		
		assertThat(expected.equals(result));
		assertThat(!result.equals(Double.class));
	}

	@Test
	void testGetAvailableLanguages() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLanguageDescription() {
		fail("Not yet implemented");
	}

	@Test
	void testGetClassLanguage() {
		
		System.out.println(underTest.getClassLanguage());
		
		assert(true);
	}

	@Test
	void testMatchApplicationDescription() {
		fail("Not yet implemented");
	}

}
