package stud;

import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public class AddEntryController {
    private StackPane rootPane;
    private Pane mainPane;

    private DataManager dataManager;

    public void setPanes(StackPane rootPane, Pane mainPane) {
        this.rootPane = rootPane;
        this.mainPane = mainPane;
    }

    public void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @FXML
    public void goBack() {
        rootPane.getChildren().clear();
        rootPane.getChildren().add(this.mainPane);
    }
}
