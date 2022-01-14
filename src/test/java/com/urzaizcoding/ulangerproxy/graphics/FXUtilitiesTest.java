package com.urzaizcoding.ulangerproxy.graphics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.Properties;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.urzaizcoding.ulangerproxy.exceptions.ContextClassNotProvidedException;
import com.urzaizcoding.ulangerproxy.graphics.FXUtilities.StageSettings;

class FXUtilitiesTest {

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testLoadFXMLURL() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadFXMLString() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLoaderURL() {
		fail("Not yet implemented");
	}

	@Test
	void testGetLoaderString() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadFXMLURLObject() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadFXMLStringObject() {
		fail("Not yet implemented");
	}

	@Test
	void testLoadProperties() throws Exception {
		// when

		Properties p = FXUtilities.loadProperties(getClass().getResource("home.properties").getFile());

		// then

		assertThat(p.getProperty("fxmlpath")).isEqualTo("fxml/login");
		assertThat(p.getProperty("width")).isEqualTo("381");
		assertThat(p.getProperty("height")).isEqualTo("382");
		assertThat(p.getProperty("langdir")).isEqualTo("languages");

	}

	@Test
	void testBuildSettingsFromProperties() {
		fail("Not yet implemented");
	}

	@Test
	void testBuildSettingsFromPropertyWhenContextNotProvided() throws Exception {

		// given

		Properties p = FXUtilities.loadProperties(getClass().getResource("home.properties").getFile());

		// when

		Throwable t = catchThrowable(() -> {
			FXUtilities.buildSettingsFromProperties(p);
		});

		// then

		assertThat(t).isInstanceOf(ContextClassNotProvidedException.class);

	}

	@Test
	void testBuildSettingsFromPropertyWhenWrongContextProvided() throws Exception {

		// given

		Properties p = FXUtilities.loadProperties(getClass().getResource("home2.properties").getFile());

		// when

		Throwable t = catchThrowable(() -> {
			FXUtilities.buildSettingsFromProperties(p);
		});

		// then

		assertThat(t).isInstanceOf(ClassNotFoundException.class);

	}

	@Test
	void testBuildSettingsFromPropertyWhenGoodContextProvided() throws Exception {
		// given

		Properties p = FXUtilities.loadProperties(getClass().getResource("home3.properties").getFile());

		// when

		StageSettings s = FXUtilities.buildSettingsFromProperties(p);
		Properties p2 = FXUtilities.loadProperties(Class.forName(p.getProperty("context")).getResource("home3.properties").getFile());
		System.out.println(p);
		System.out.println(p2);
		// then
		assertThat(p).isEqualTo(p2);
//		assertThat(s.getContextClass()).isEqualTo(this.getClass().getName());
		assertThat(s.getContextClass().equals("com.urzaizcoding.ulangerproxy.lang.LanguageParserTest"));
		
		
	}

	@Test
	void testSceneFromSettings() {
		fail("Not yet implemented");
	}

	@Test
	void testAvailableLanguagesFromSettings() {
		fail("Not yet implemented");
	}

}
