package stud;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class AddEntryController {
    private StackPane rootPane;
    private Pane mainPane;

    private DataManager dataManager;

    @FXML private ChoiceBox<String> studentChoiceBox;
    @FXML private DatePicker datePicker;
    @FXML private ChoiceBox<String> statusChoiceBox;

    @FXML
    public void initialize() {
        statusChoiceBox.setItems(FXCollections.observableArrayList(
            "Present", "Absent"
        ));

        statusChoiceBox.setValue("Present"); // Set default value
    }


    public void setPanes(StackPane rootPane, Pane mainPane) {
        this.rootPane = rootPane;
        this.mainPane = mainPane;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.populateStudents();
    }

    private void populateStudents() {
    studentChoiceBox.setItems(FXCollections.observableArrayList(
        dataManager.getStudents().stream()
            .map(s -> s.getFirstName() + " " + s.getLastName())
            .collect(Collectors.toList()) // Corrected method
    ));
}

    @FXML
    public void handleSaveEntry() {
        if (studentChoiceBox.getValue() == null || datePicker.getValue() == null || statusChoiceBox.getValue() == null) {
            System.out.println("Error: All fields must be filled!");
            return;
        }

        String selectedStudent = studentChoiceBox.getValue();
        String date = datePicker.getValue().toString();
        String status = statusChoiceBox.getValue();

        Student student = dataManager.getStudents().stream()
            .filter(s -> (s.getFirstName() + " " + s.getLastName()).equals(selectedStudent))
            .findFirst()
            .orElse(null);

        if (student == null) {
            System.out.println("Error: Student not found!");
            return;
        }

        AttendanceRecord newRecord = new AttendanceRecord(student.getId(), date, status);
        dataManager.addAttendanceRecord(newRecord);

        System.out.println("New Entry Saved: " + newRecord);
        goBack();
    }

    @FXML
    public void goBack() {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(this.mainPane);
    }
}
