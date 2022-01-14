package com.urzaizcoding.ulangerproxy.graphics;

import static com.urzaizcoding.ulangerproxy.lang.LanguageParser.getAvailableLanguages;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import com.urzaizcoding.ulangerproxy.exceptions.ContextClassNotProvidedException;
import com.urzaizcoding.ulangerproxy.exceptions.IllegalFileFormatException;
import com.urzaizcoding.ulangerproxy.exceptions.IncompletePathException;
import com.urzaizcoding.ulangerproxy.lang.ApplicationDescription;
import com.urzaizcoding.ulangerproxy.lang.LanguageParser;
import com.urzaizcoding.ulangerproxy.log.Logger;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class FXUtilities {

	private static final String FXMLPATH_PROPERTY = "fxmlpath";
	private static final String HEIGHT_PROPERTY = "height";
	private static final String WIDTH_PROPERTY = "width";
	private static final String LANG_DIR_PROPERTY = "langdir";
	private static final String CONTEXT_CLASS_PROPERTY = "context";
	private static Properties defaultProperties;
	public static final String HOME_CONFIG;

	static {
		HOME_CONFIG = FXUtilities.class.getResource("defaults.properties").getFile();
		try {
			defaultProperties = loadProperties(HOME_CONFIG);
		} catch (IOException e) {
			Logger.loggerObject.writeError(FXUtilities.class.getName(),
					Logger.getExceptionMessage(e) + " Error while attempting to load default properties");
			defaultProperties = new Properties();
			defaultPropertiesInitilization();
		}
	}

	private static void defaultPropertiesInitilization() {
		defaultProperties.setProperty(FXMLPATH_PROPERTY, "root");
		defaultProperties.setProperty(HEIGHT_PROPERTY, "300");
		defaultProperties.setProperty(WIDTH_PROPERTY, "400");
		defaultProperties.setProperty(LANG_DIR_PROPERTY, "languages");
	}

	public static class StageSettings {

		private String fxmlPath;
		private Class<?> contextClassName;
		private Object controller;
		private final double width;
		private final double height;
		private boolean completePath;
		private URI fxmlcompletePath;
		private String languageDirectory;

		private StageSettings(String fxmlPath, Class<?> context, Object controller, double width, double height,
				String languageDir){
			super();
			this.fxmlPath = fxmlPath;
			this.controller = controller;
			this.width = width;
			this.height = height;
			this.languageDirectory = languageDir;
			this.contextClassName = context;
		}

		public static class StageSettingsBuilder {

			private String fxmlPath;
			private Class<?> contextClassName;
			private Object controller;
			private double width;
			private double height;
			private String languageDir;

			public StageSettingsBuilder(String fxmlPath, String contextClazz) throws ClassNotFoundException {
				super();
				this.fxmlPath = fxmlPath;
				this.contextClassName = Class.forName(contextClazz);
			}

			public StageSettingsBuilder width(double w) {
				this.width = w;
				return this;
			}

			public StageSettingsBuilder height(double h) {
				this.height = h;
				return this;
			}

			public StageSettingsBuilder controller(Object c) {
				this.controller = c;
				return this;
			}

			public StageSettingsBuilder languageDirectory(String dir) {
				this.languageDir = dir;
				return this;
			}

			public StageSettings build() {
				return new StageSettings(fxmlPath, contextClassName, controller, width, height, languageDir);
			}

		}

		public final String getFxmlPath() {
			return fxmlPath;
		}

		public final Object getController() {
			return controller;
		}

		public final double getWidth() {
			return width;
		}

		public final double getHeight() {
			return height;
		}

		public void setController(Object c) {
			this.controller = c;
		}

		public final boolean isCompletePath() {
			return completePath;
		}

		@Deprecated
		public void updateFXMLPath(String fpath) throws MalformedURLException, IOException, URISyntaxException {
			updateFXMLPath(new URI(fpath));
		}

		public void updateFXMLPath(URI fp) {
			this.fxmlcompletePath = fp;
			completePath = true;
		}

		public URI getFxmlcompletePath() {
			return fxmlcompletePath;
		}

		public String getLanguageDirectory() {
			return languageDirectory;
		}

		public void setLanguageDirectory(String languageDirectory) {
			this.languageDirectory = languageDirectory;
		}

		public Class<?> getContextClass() {
			return contextClassName;
		}

	}

	public static Parent loadFXML(URL fxmlPath) throws IOException {
		return getLoader(fxmlPath).load();
	}

	public static Parent loadFXML(String fxmlPath) throws IOException, MalformedURLException {

		return getLoader(fxmlPath).load();
	}

	public static FXMLLoader getLoader(URL fxmlPath) {
		return new FXMLLoader(fxmlPath);
	}

	public static FXMLLoader getLoader(String fxmlPath) throws MalformedURLException {
		return getLoader(new URL(fxmlPath));
	}

	public static Parent loadFXML(URL fxml, Object controler) throws IOException {
		FXMLLoader loader = getLoader(fxml);
		loader.setController(controler);
		return loader.load();
	}

	public static Parent loadFXML(String fxml, Object controler) throws IOException, MalformedURLException {
		return loadFXML(new URL(fxml), controler);
	}

	public static Properties loadProperties(String configFilePath) throws IOException {
		Properties prop = new Properties();
		InputStream stream = new FileInputStream(new File(configFilePath == null ? HOME_CONFIG : configFilePath));

		prop.load(stream);
		return prop;
	}

	public static StageSettings buildSettingsFromProperties(Properties p)
			throws NumberFormatException, ContextClassNotProvidedException, ClassNotFoundException {
		String fxml, context, langdir;
		double height, width;

		fxml = p.getProperty(FXMLPATH_PROPERTY, defaultProperties.getProperty(FXMLPATH_PROPERTY));
		context = p.getProperty(CONTEXT_CLASS_PROPERTY);
		langdir = p.getProperty(LANG_DIR_PROPERTY, defaultProperties.getProperty(LANG_DIR_PROPERTY));
		height = Double.parseDouble(p.getProperty(HEIGHT_PROPERTY, defaultProperties.getProperty(HEIGHT_PROPERTY)));
		width = Double.parseDouble(p.getProperty(WIDTH_PROPERTY, defaultProperties.getProperty(WIDTH_PROPERTY)));

		if (context == null) {
			throw new ContextClassNotProvidedException(
					"The class to use for resource finding and loading hasn't been provided");
		}
		String contextClazz;
		contextClazz = context.equals("no-context") ? null : context;

		return new StageSettings.StageSettingsBuilder(fxml, contextClazz).languageDirectory(langdir).height(height)
				.width(width).build();

	}

	public static Scene sceneFromSettings(StageSettings s)
			throws IOException, IncompletePathException, ClassNotFoundException {
		if (!s.isCompletePath() && (s.getContextClass() == null)) {
			throw new IncompletePathException(
					"The given path is not a fully qualified path must be updated or a context provided");
		}

		Class<?> contexzz = s.getContextClass();

		if (contexzz != null) {

			try {
				return new Scene(
						loadFXML(contexzz.getResource("").toExternalForm().concat(s.getFxmlPath()), s.getController()),
						s.getWidth(), s.getHeight());
			} catch (IOException e) {
				throw e;
			}
		}

		return new Scene(loadFXML(s.getFxmlcompletePath().toURL(), s.getController()), s.getWidth(), s.getHeight());
	}

	public static ArrayList<LanguageParser> availableLanguagesFromSettings(StageSettings s, ApplicationDescription desc)
			throws MalformedURLException, IOException, IllegalFileFormatException, ClassNotFoundException {

		File f = new File(s.getContextClass() == null ? s.getLanguageDirectory()
				: s.getContextClass().getResource("").getFile().concat(s.getLanguageDirectory()));
		if (f.isDirectory()) {
			return getAvailableLanguages(f, desc);
		}
		throw new IllegalStateException("The file object doesn't describe a directory");
	}

}
