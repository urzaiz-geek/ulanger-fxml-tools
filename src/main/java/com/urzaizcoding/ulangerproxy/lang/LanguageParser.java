package com.urzaizcoding.ulangerproxy.lang;

import static com.urzaizcoding.ulangerproxy.exceptions.ExceptionWrappers.mapperWrapper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.urzaizcoding.ulangerproxy.exceptions.ExceptionWrappers.ThrowingFunction;
import com.urzaizcoding.ulangerproxy.exceptions.FileDescriptionNotMatchedException;
import com.urzaizcoding.ulangerproxy.exceptions.IdNotFoundInClassException;
import com.urzaizcoding.ulangerproxy.exceptions.IllegalFileFormatException;
import com.urzaizcoding.ulangerproxy.log.Logger;

public class LanguageParser {
	private final ArrayList<LClass> classLanguage;
	private LanguageDescription languageDescription;
	private final URI languageFilePath;

	public LanguageParser(URI langFilePath) throws IOException, IllegalFileFormatException {
		this.languageFilePath = langFilePath;
		this.classLanguage = new ArrayList<>();
		parse();
	}

	/**
	 * retrieve all the declared and inherited fields names of a given class
	 * 
	 * @param rclass is the class that we desire to obtain fields
	 * @return {@link ArrayList} of String describing those fields
	 */
	public static final List<String> getIds(Class<?> rclass) {
		return getAllFields(rclass).stream().map(Field::getName).collect(Collectors.toList());
	}

	public static List<Field> getInheritedFields(Class<?> rClass) {
		List<Field> listFields = new LinkedList<>();

		Class<?> superClass = rClass.getSuperclass();
		while (!superClass.equals(Object.class)) {

			listFields.addAll(Arrays.stream(superClass.getDeclaredFields())
					.filter(f -> f.getModifiers() == Modifier.PROTECTED).collect(Collectors.toList()));
			superClass = superClass.getSuperclass();
		}
		return listFields;
	}

	/**
	 * Retrieve all the actual declared and inherited fields of the given class
	 * (private members of superclasses are ignored)
	 * 
	 * @param rClass : the class for whom we want the field list.
	 * @return a {@link List} with {@link Field} objects
	 */
	public static List<Field> getAllFields(Class<?> rClass) {
		return Stream.concat(Arrays.stream(rClass.getDeclaredFields()), getInheritedFields(rClass).stream())
				.collect(Collectors.toList());
	}

	/**
	 * retrieves the class of a field from his name and his container class
	 * 
	 * @param container : the class containing the field
	 * @param fieldName : the field name
	 * @return
	 * @throws IdNotFoundInClassException
	 */
	public static Class<?> getFieldClass(Class<?> container, String fieldName) throws IdNotFoundInClassException {

		return getField(container, fieldName).getType();
	}

	/**
	 * retrieve a particular field in a class given his name and his container class, this method will ascend th class hierarchy to find the
	 * requested field in superclasses if exists. NOTE: This method find only the accessible fields (protected one in superclass)
	 * @param container : the class that may contain the field
	 * @param fieldName : the requested field name as declared in his class
	 * @return a {@link Field} object describing the field 
	 * @throws IdNotFoundInClassException when the file does'nt exists int the class or in the superClasses
	 */
	public static Field getField(Class<?> container, String fieldName) throws IdNotFoundInClassException {
		Optional<Field> retour = getAllFields(container).stream().filter(f -> f.getName().equals(fieldName))
				.findFirst();
		if (retour.isPresent()) {
			return retour.get();
		} else {
			throw new IdNotFoundInClassException(fieldName, container.getName());
		}

	}

	/**
	 * retrieves all the available languages in the language folder, the available
	 * languages are the one that matches the received application description.
	 * 
	 * @param pathToLanguageFolder : the URL to the language folder
	 * @param desc                 : the application description to be matched
	 * @return {@link ArrayList} : an ArrayList of {@link LanguageParser} containing
	 *         all the available languages retrieved for the application
	 * @throws IOException                        if access to the folder or to a
	 *                                            specified file fails
	 * @throws IllegalFileFormatException         if the ULANG doesn't respect the
	 *                                            format
	 * @throws FileDescriptionNotMatchedException if an ULANG file that doens't
	 *                                            correspond to the current
	 *                                            description is encountered
	 */
	public static final ArrayList<LanguageParser> getAvailableLanguages(File languageDir,
			ApplicationDescription desc) throws IOException, IllegalFileFormatException {

		
		FileFilter filter = (file) -> file.getName().split("\\.")[1].equalsIgnoreCase("ulang");

		ThrowingFunction<File, LanguageParser> foo = (t) -> {
			return new LanguageParser(t.toURI());
		};

		return Arrays.stream(languageDir.listFiles(filter))
				.map(mapperWrapper(foo, Logger.loggerObject, Exception.class))
				.filter(p -> p.matchApplicationDescription(desc)).collect(Collectors.toCollection(ArrayList::new));

//		for (File f : languageDir.listFiles(filter)) {
//			LanguageParser parser = new LanguageParser(f.getAbsolutePath());
//			if (parser.matchApplicationDescription(desc)) {
//				retour.add(parser);
//			}
//			else {
//				throw new FileDescriptionNotMatchedException(desc.getApplicationName(), desc.getVersion());
//			}
//		}
	}

	/**
	 * parses the intern language ULANG file
	 * 
	 * @throws IOException
	 * @throws IllegalFileFormatException
	 */
	private void parse() throws IOException, IllegalFileFormatException {
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(new FileInputStream(new File(languageFilePath)), "utf-8"));
		Integer lineNumber = 1;
		String[] desc = new String[4];
		String param, dir;
		// the first line must be the description
		String line = reader.readLine();
		if (!line.equalsIgnoreCase(":description")) {
			throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, line);
		}
		/**
		 * fetch the file description
		 */

		while (!(line = reader.readLine()).equalsIgnoreCase(":endDescription")) {
			lineNumber++;
			param = line.split("=")[0];
			param = param.trim();
			if (!Directive.isParam("description", param)) {
				reader.close();
				throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, param);
			}
			desc[lineNumber - 2] = line.split("=")[1].trim();
		}
		this.languageDescription = new LanguageDescription(desc);

		/**
		 * parsing the rest of the file
		 */

		line = reader.readLine();
		lineNumber++;
		if (!line.equalsIgnoreCase(":beg")) {
			throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, line);
		}

		int i = 0; // we will use it to loop through the classes

		while (!(line = reader.readLine()).equalsIgnoreCase(":end")) {
			lineNumber++;
			if (!line.split("=")[0].trim().equalsIgnoreCase(":class")) {
				reader.close();
				throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, line);
			}
			classLanguage.add(new LClass(line.split("=")[1].trim()));

			while (!(line = reader.readLine()).trim().equalsIgnoreCase(":endClass")) {
				lineNumber++;
				if (!Directive.isDirective(line.trim().substring(1))) {
					reader.close();
					throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, line);
				}
				dir = line.trim().substring(1);
				String id = null, method = null, value = null;
				while (!(line = reader.readLine()).trim().equalsIgnoreCase(":end" + dir)) {
					lineNumber++;
					if (!Directive.isParam(dir, line.split("=")[0].trim())) {
						reader.close();
						throw new IllegalFileFormatException(languageFilePath.getPath(), lineNumber, line);
					}
					if (line.split("=")[0].trim().equalsIgnoreCase("id")) {
						id = line.split("=")[1].trim();
						continue;
					}
					if (line.split("=")[0].trim().equalsIgnoreCase("method")) {
						method = line.split("=")[1].trim();
						continue;
					}
					if (line.split("=")[0].trim().equalsIgnoreCase("value")) {
						value = line.split("=")[1].trim();
						value = value.substring(1, value.length() - 1);
					}
					classLanguage.get(i).getClassfields().add(new LField(id, method, value));
				}
			}
			i++;
		}

	}

	public static class LField {
		/**
		 * This class represents a field read from the language file. A field is
		 * characterized by an id an a text value
		 */
		private final String fieldId;
		private final String methodName;
		private final String value;

		public LField(String fieldId, String methodName, String value) {
			this.fieldId = fieldId;
			this.methodName = methodName;
			this.value = value;
		}

		public String getFieldId() {
			return fieldId;
		}

		public String getMethodName() {
			return methodName;
		}

		public String getValue() {
			return value;
		}

		@Override
		public String toString() {
			return "LField [fieldId=" + fieldId + ", methodName=" + methodName + ", value=" + value + "]";
		}

	}

	public static class LClass {
		/**
		 * class that represents a doc with labels and messages for whose language can
		 * be managed
		 */
		private final String className;
		private final ArrayList<LField> classFields;

		public LClass(String name) {
			this.className = name;
			classFields = new ArrayList<>();
		}

		public String getClassName() {
			return className;
		}

		public ArrayList<LField> getClassfields() {
			return classFields;
		}

		@Override
		public String toString() {
			return "LClass [className=" + className + ", classFields=" + classFields + "]";
		}

	}

	private static class Directive {
		/**
		 * this class is designed to represent directives used in the ULANG expressions
		 * language
		 */
		private final String directiveName;
		private final ArrayList<String> directiveParams;

		private static final HashSet<Directive> directivesList = new HashSet<>(Arrays
				.asList(new Directive[] { new Directive("description", "name", "application", "version", "codeName"),
						new Directive("field", "id", "value", "method"), new Directive("class"), new Directive("beg"),
						new Directive("end")

				}));

		private Directive(String name, String... params) {
			directiveName = name;
			directiveParams = new ArrayList<String>(Arrays.asList(params));
		}

		/**
		 * checks if a {@link String} received as parameter is an ulng directive
		 * 
		 * @param name
		 * @return
		 */
		public static boolean isDirective(String name) {

			return directivesList.stream().anyMatch(d -> d.directiveName.equalsIgnoreCase(name));
		}

		/**
		 * checks if a received parameter is a parameter for a received ULANG directive
		 * 
		 * @param dir   : the ULANG directive
		 * @param param : the parameter to check
		 * @return
		 */
		public static boolean isParam(String dir, String param) {
			if (!isDirective(dir)) {
				return false;
			}

			return directivesList.stream().filter(d -> d.directiveName.equalsIgnoreCase(dir)).findFirst()
					.get().directiveParams.stream().anyMatch(p -> p.equalsIgnoreCase(param));
		}
	}

	public static class LanguageDescription {
		private final String name;
		private final String codeName;
		private final String applicationOwner;
		private final String version;

		public LanguageDescription(String... desc) {
			this.applicationOwner = desc[2];
			this.name = desc[0];
			this.codeName = desc[1];
			this.version = desc[3];
		}

		public String getName() {
			return name;
		}

		public String getCodeName() {
			return codeName;
		}

		public String getApplicationOwner() {
			return applicationOwner;
		}

		public String getVersion() {
			return version;
		}

		@Override
		public String toString() {
			return "LanguageDescription [name=" + name + ", codeName=" + codeName + ", applicationOwner="
					+ applicationOwner + ", version=" + version + "]";
		}

	}

	public LanguageDescription getLanguageDescription() {
		return languageDescription;
	}

	public ArrayList<LClass> getClassLanguage() {
		return classLanguage;
	}

	public boolean matchApplicationDescription(ApplicationDescription desc) {
		if (!this.getLanguageDescription().getApplicationOwner().equalsIgnoreCase(desc.getApplicationName())) {
			return false;
		}
		if (!this.getLanguageDescription().getVersion().equalsIgnoreCase(desc.getVersion())) {
			return false;
		}
		return true;
	}
}
