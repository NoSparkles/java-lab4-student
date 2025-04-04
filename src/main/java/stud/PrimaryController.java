package stud;

import java.io.File;
import java.io.IOException;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class PrimaryController {
    @FXML private StackPane rootPane;
    @FXML private Pane mainPane;

    @FXML private ChoiceBox<String> importExportChoiceBox;
    @FXML private Button importButton;
    @FXML private Button exportButton;

    @FXML private CheckBox showOnlyFilledDaysButton;
    @FXML private DatePicker FromDatePicker;
    @FXML private DatePicker ToDatePicker;
    @FXML private ChoiceBox<String> filterByChoiceBox;
    @FXML private TextField filterTextField;
    @FXML private Button filterButton;
    @FXML private Button addEntryButton;
    @FXML private Button createStudentButton;
    @FXML private Button createGroupButton;

    @FXML TableView<AttendanceRecord> attendanceTableView;
    @FXML private TableColumn<AttendanceRecord, String> dateColumn;
    @FXML private TableColumn<AttendanceRecord, Integer> studentIdColumn;
    @FXML private TableColumn<AttendanceRecord, String> nameColumn;
    @FXML private TableColumn<AttendanceRecord, String> groupColumn;
    @FXML private TableColumn<AttendanceRecord, String> statusColumn;

    DataManager dataManager;

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

        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDate()));
        studentIdColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getStudentId()).asObject());
        groupColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getGroup()));
        nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFirstName() + " " + cellData.getValue().getLastName()));
        statusColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStatus()));

        this.dataManager = new DataManager(this.attendanceTableView);
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

        this.dataManager.importData(setectedFile.getAbsolutePath(), selectedFileType);

        for (Student student : this.dataManager.getStudents()) {
            System.out.println("Student: " + student.getId() + ", " + student.getFirstName() + ", " + student.getLastName() + ", " + student.getGroup());
        }
        for (AttendanceRecord record : this.dataManager.getAttendanceRecords()) {
            System.out.println("Attendance Record: " + record.getStudentId() + ", " + record.getFirstName() + ", " + record.getLastName() + ", " + record.getDate() + ", " + record.getGroup() + ", " + record.getStatus());
        }

        this.dataManager.showDataInTableView();
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
        this.dataManager.filterAttendance(this.FromDatePicker.getValue(), this.ToDatePicker.getValue(), this.filterTextField.getText(), this.filterByChoiceBox.getValue(), !this.showOnlyFilledDaysButton.isSelected());
    }

    @FXML
    public void handleAddEntryButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("addEntry.fxml"));
            Pane addEntryPane = loader.load();
            this.rootPane.getChildren().clear();
            this.rootPane.getChildren().add(addEntryPane);

            AddEntryController addEntryController = loader.getController();
            addEntryController.setPanes(this.rootPane, this.mainPane);
            addEntryController.setDataManager(this.dataManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateStudentButton() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("createStudent.fxml"));
            Pane createStudentPane = loader.load();
            this.rootPane.getChildren().clear();
            this.rootPane.getChildren().add(createStudentPane);

            CreateStudentController createStudentController = loader.getController();
            createStudentController.setPanes(this.rootPane, this.mainPane);
            createStudentController.setDataManager(this.dataManager);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCreateGroupButton() {
        System.out.println("Creating group...");
    }
}
