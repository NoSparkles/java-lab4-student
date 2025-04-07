package stud;

import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class UpdateStudentController {
    private StackPane rootPane;
    private Pane mainPane;

    private DataManager dataManager;

    @FXML private ChoiceBox<String> studentChoiceBox;
    @FXML private TextField firstNameTextField;
    @FXML private TextField lastNameTextField;
    @FXML private ChoiceBox<String> groupChoiceBox;

    @FXML
    public void initialize() {
        // Initialize the choice boxes and labels
        studentChoiceBox.setOnAction(event -> {
            String selectedStudent = studentChoiceBox.getValue();
            if (selectedStudent != null) {
                String[] parts = selectedStudent.split(" ", 2); // Split into two parts: ID and name
                try {
                    int studentId = Integer.parseInt(parts[0]); // Convert only the first part
                    Student student = dataManager.getStudentById(studentId);
                    firstNameTextField.setText(student.getFirstName());
                    lastNameTextField.setText(student.getLastName());
                    groupChoiceBox.setValue(student.getGroup());
                } catch (NumberFormatException e) {
                    System.err.println("Error parsing student ID: " + e.getMessage());
                }
            }
        });
    }

    public void setPanes(StackPane rootPane, Pane mainPane) {
        this.rootPane = rootPane;
        this.mainPane = mainPane;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.populateStudents();
        this.populateGroups();
    }

    private void populateStudents() {
        studentChoiceBox.setItems(FXCollections.observableArrayList(
            dataManager.getStudents().stream()
                .map(s -> s.getId() + " "+ s.getFirstName() + " " + s.getLastName())
                .collect(Collectors.toList()) // Corrected method
        ));
    }

    private void populateGroups() {
        if (this.dataManager != null) {
            groupChoiceBox.setItems(FXCollections.observableArrayList(dataManager.getExistingGroups()));
        }
    }

    @FXML
    public void handleDelete() {
        String selectedStudent = studentChoiceBox.getValue();

        // Ensure a student is selected before deletion
        if (selectedStudent == null) {
            System.err.println("Error: No student selected for deletion.");
            return;
        }

        // Extract student ID from choice box format ("ID FirstName LastName")
        String[] parts = selectedStudent.split(" ");
        int studentId = Integer.parseInt(parts[0]);

        // Remove student from DataManager
        dataManager.getStudents().removeIf(student -> student.getId() == studentId);

        // Remove all attendance records associated with the student
        dataManager.getAttendanceRecords().removeIf(record -> record.getStudentId() == studentId);

        this.firstNameTextField.clear();
        this.lastNameTextField.clear();

        // Refresh UI components
        populateStudents(); // Update student choice box
        dataManager.showDataInTableView(); // Refresh attendance table

        System.out.println("Deleted student and all associated records: " + selectedStudent);
    }

    @FXML
public void handleSaveButton() {
    String selectedStudent = studentChoiceBox.getValue();

    // Ensure a student is selected before saving changes
    if (selectedStudent == null) {
        System.err.println("Error: No student selected for updating.");
        return;
    }

    // Extract student ID from choice box format ("ID FirstName LastName")
    String[] parts = selectedStudent.split(" ");
    int studentId = Integer.parseInt(parts[0]);

    // Retrieve the student from DataManager
    Student student = dataManager.getStudentById(studentId);
    
    // Ensure the student exists
    if (student == null) {
        System.err.println("Error: Student not found.");
        return;
    }

    // Update student's details with values from input fields
    student.setFirstName(firstNameTextField.getText().trim());
    student.setLastName(lastNameTextField.getText().trim());
    student.setGroup(groupChoiceBox.getValue());

    // Refresh UI components
    populateStudents(); // Update student choice box
    dataManager.showDataInTableView(); // Refresh attendance table

    System.out.println("Updated student details: " + student);
    this.goBack();
}

    @FXML
    public void goBack() {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(this.mainPane);
    }
}
