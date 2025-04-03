module stud {
    requires javafx.controls;
    requires javafx.fxml;

    opens stud to javafx.fxml;
    exports stud;
}
