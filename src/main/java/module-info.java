module com.urzaizcoding.ulangerproxy {
	exports com.urzaizcoding.ulangerproxy.log;
	exports com.urzaizcoding.ulangerproxy.graphics;
	exports com.urzaizcoding.ulangerproxy.lang;
	exports com.urzaizcoding.ulangerproxy.exceptions;
	
	requires transitive javafx.fxml;
	requires transitive javafx.graphics;
	requires transitive javafx.controls;
	
	opens com.urzaizcoding.ulangerproxy.graphics to javafx.fxml;
}