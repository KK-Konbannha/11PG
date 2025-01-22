import javafx.application.Platform;
import javafx.fxml.FXML;

public class GameClearController {

    @FXML
    void onGameClearAction() {
        Platform.exit();
    }
}
