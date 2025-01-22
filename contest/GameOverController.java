import javafx.fxml.FXML;

public class GameOverController {

    @FXML
    void onGameOverAction() {
        try {
            StageDB.getGameOverStage().close();
            StageDB.getGameOverSound().stop();

            StageDB.getMainStage().show();
            StageDB.getMainSound().play();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
