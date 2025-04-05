package stud;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class CreateGroupController {
    private StackPane rootPane;
    private Pane mainPane;

    private DataManager dataManager;

    @FXML private TextField groupNameField;
    @FXML private Button saveButton;

    @FXML private Label errorLabel;

    public void setPanes(StackPane rootPane, Pane mainPane) {
        this.rootPane = rootPane;
        this.mainPane = mainPane;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @FXML
    public void handleSaveGroup() {
        String newGroupName = groupNameField.getText().trim();

        if (newGroupName.isEmpty()) {
            errorLabel.setText("Error: Group name cannot be empty!");
            return;
        }

        // Check if the group already exists
        if (dataManager.getExistingGroups().contains(newGroupName)) {
            errorLabel.setText("Error: Group '" + newGroupName + "' already exists!");
            return;
        }

        // Add new group
        dataManager.addGroup(newGroupName);
        System.out.println("New Group Created: " + newGroupName);

        goBack();
    }

    @FXML
    public void goBack() {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(this.mainPane);
    }
}
