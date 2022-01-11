package com.urzaizcoding.ulangerproxy.graphics;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Properties;

import com.urzaizcoding.ulangerproxy.exceptions.IncompletePathException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public final class FXUtilities {

	private static final String FXMLPATH_PROPERTY = "fxmlpath";
	private static final String HEIGHT_PROPERTY = "height";
	private static final String WIDTH_PROPERTY = "width";
	public static final String HOME_CONFIG = "home.properties";

	public static class StageSettings {

		private String fxmlPath;
		private Object controller;
		private final double width;
		private final double height;
		private boolean completePath;
		private URL fxmlcompletePath;

		private StageSettings(String fxmlPath, Object controller, double width, double height) {
			super();
			this.fxmlPath = fxmlPath;
			this.controller = controller;
			this.width = width;
			this.height = height;
		}

		public static class StageSettingsBuilder {

			private String fxmlPath;
			private Object controller;
			private double width;
			private double height;

			public StageSettingsBuilder(String fxmlPath) {
				super();
				this.fxmlPath = fxmlPath;
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

			public StageSettings build() {
				return new StageSettings(fxmlPath, controller, width, height);
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
		public void updatePath(String fpath) throws MalformedURLException,IOException {
			updatePath(new URL(fpath));
		}
		
		public void updatePath(URL fp) {
			this.fxmlcompletePath = fp;
			completePath = true;
		}

		public URL getFxmlcompletePath() {
			return fxmlcompletePath;
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
			throws NumberFormatException, NoSuchElementException {

		return new StageSettings.StageSettingsBuilder(
				Optional.ofNullable(p.getProperty(FXMLPATH_PROPERTY)).orElseThrow(NoSuchElementException::new))
						.height(Double.parseDouble(
								Optional.ofNullable(p.getProperty(WIDTH_PROPERTY)).orElseThrow(NoSuchElementException::new)))
						.width(Double.parseDouble(
								Optional.ofNullable(p.getProperty(HEIGHT_PROPERTY)).orElseThrow(NoSuchElementException::new)))
						.build();

	}

	public static Scene sceneFromSettings(StageSettings s) throws IOException, IncompletePathException {
		if(!s.isCompletePath()) {
			throw new IncompletePathException("The given path is not a fully qualified path must be upddated");
		}
		return new Scene(loadFXML(s.getFxmlcompletePath(), s.getController()), 
				s.getHeight(),
				 s.getWidth());
	}

}
