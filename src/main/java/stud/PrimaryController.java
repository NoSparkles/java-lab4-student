package stud;

import java.io.File;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;

public class PrimaryController {
    @FXML ChoiceBox<String> importExportChoiceBox;
    @FXML Button importButton;
    @FXML Button exportButton;

    @FXML CheckBox showOnlyFilledDaysButton;
    @FXML DatePicker FromDatePicker;
    @FXML DatePicker ToDatePicker;
    @FXML ChoiceBox<String> filterByChoiceBox;
    @FXML TextField filterTextField;
    @FXML Button filterButton;
    @FXML Button addEntryButton;
    @FXML Button createStudentButton;
    @FXML Button createGroupButton;

    @FXML TableView<AttendanceRecord> attendanceTableView;

    DataManager dataManager;
    DataContainer dataContainer;

    @FXML
    public void initialize() {
        this.importExportChoiceBox.setItems(FXCollections.observableArrayList("CSV", "Excel", "PDF"));
        this.importExportChoiceBox.setValue("CSV");

        this.importExportChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.handleImportExportChange(newValue);
        });

        filterByChoiceBox.setItems(FXCollections.observableArrayList("None","Group", "Student"));
        filterByChoiceBox.setValue("None");

        this.filterByChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            this.handleFilterByChoiceBox(newValue);
        });

        this.dataManager = new DataManager();
        this.dataContainer = null;
    }

    @FXML
    public void handleImportExportChange(String newValue) {
        if (newValue.equals("PDF")) {
            this.importButton.setDisable(true);
        } else {
            this.importButton.setDisable(false);
        }
    }

    @FXML
    public void handleImportButton() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select a file");

        FileChooser.ExtensionFilter filter;
        String selectedFileType = this.importExportChoiceBox.getValue();

        switch (this.importExportChoiceBox.getValue()) {
            case "CSV":
                filter = new FileChooser.ExtensionFilter("CSV Files", "*.csv");
                break;
            case "Excel":
                filter = new FileChooser.ExtensionFilter("Excel Files", "*.xlsx", "*.xls");
                break;
            default:
                return;
        }

        fileChooser.getExtensionFilters().add(filter);

        File setectedFile = fileChooser.showOpenDialog(importButton.getScene().getWindow());
        
        if (setectedFile != null) {
            System.out.println("Selected file: " + setectedFile.getAbsolutePath());
        } else {
            System.out.println("File selection cancelled.");
        }

        if (selectedFileType.equals("CSV")) {
            this.dataContainer = this.dataManager.importFromCSV(setectedFile.getAbsolutePath());
        } else if (selectedFileType.equals("Excel")) {
            this.dataContainer = this.dataManager.importFromExcel(setectedFile.getAbsolutePath());
        }

        for (Student student : this.dataContainer.getStudents()) {
            System.out.println("Student: " + student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getGroup());
        }
        for (AttendanceRecord record : this.dataContainer.getAttendanceRecords()) {
            System.out.println("Attendance Record: " + record.getStudentId() + ", " + record.getFirstName() + ", " + record.getLastName() + ", " + record.getDate() + ", " + record.getGroup() + ", " + record.getStatus());
        }
    }

    @FXML
    public void handleExportButton() {
        System.out.println("Exporting data...");
    }

    @FXML
    public void handleShowOnlyFilledDaysButton() {
        if (this.showOnlyFilledDaysButton.isSelected()) {
            System.out.println("Showing only filled days...");
        } else {
            System.out.println("Showing all days...");
        }
    }

    @FXML
    public void handleFromDatePicker() {
        System.out.println("From date: " + this.FromDatePicker.getValue());
    }

    @FXML
    public void handleToDatePicker() {
        System.out.println("To date: " + this.ToDatePicker.getValue());
    }

    public void handleFilterByChoiceBox(String newValue) {
        System.out.println("Filtering by: " + newValue);
    }

    @FXML
    public void handleFilterTextField() {
        System.out.println("Filter text: " + this.filterTextField.getText());
    }

    @FXML
    public void handleFilterButton() {
        System.out.println("Filtering data...");
    }

    @FXML
    public void handleAddEntryButton() {
        System.out.println("Adding entry...");
    }

    @FXML
    public void handleCreateStudentButton() {
        System.out.println("Creating student...");
    }

    @FXML
    public void handleCreateGroupButton() {
        System.out.println("Creating group...");
    }
}
