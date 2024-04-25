module org.example.eclipse {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.eclipse to javafx.fxml;
    exports org.example.eclipse;
}