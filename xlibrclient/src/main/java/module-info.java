module com.xlibrclient {
	requires javafx.controls;
	requires javafx.fxml;
	requires jakarta.xml.bind;


	opens com.xlibrpkg to javafx.fxml;
	exports com.xlibrpkg;
}