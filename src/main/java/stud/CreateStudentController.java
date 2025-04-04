package stud;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CreateStudentController {
    private StackPane rootPane;
    private Pane mainPane;

    @FXML private ChoiceBox<String> groupChoiceBox;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private Button saveButton;

    private DataManager dataManager;

    public void setPanes(StackPane rootPane, Pane mainPane) {
        this.rootPane = rootPane;
        this.mainPane = mainPane;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
        this.populateGroups();
    }

    private void populateGroups() {
        if (this.dataManager != null) {
            groupChoiceBox.setItems(FXCollections.observableArrayList(dataManager.getExistingGroups()));
        }
    }

    @FXML
    public void handleSaveStudent() {
        if (firstNameField.getText().isEmpty() || lastNameField.getText().isEmpty() || groupChoiceBox.getValue() == null) {
            System.out.println("Error: All fields must be filled!");
            return;
        }

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String group = groupChoiceBox.getValue();

        dataManager.createStudent(firstName, lastName, group);

        this.goBack();
    }


    @FXML
    public void goBack() {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(this.mainPane);
    }
}
