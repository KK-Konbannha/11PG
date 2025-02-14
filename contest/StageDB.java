import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

class StageDB {

    static private Stage mainStage = null;
    static private Stage gameOverStage = null;
    static private Stage gameClearStage = null;
    static private MediaPlayer mainSound = null;
    static private MediaPlayer gameOverSound = null;
    static private MediaPlayer healSound = null;
    static private MediaPlayer damageSound = null;
    static private Class mainClass;
    static private final String mainSoundFileName = "sound/bgm1.mp3"; // BGM by OtoLogic
    static private final String gameOverSoundFileName = "sound/miss1.mp3";
    static private final String healSoundFileName = "sound/maou_se_sound_drink01.mp3";
    static private final String damageSoundFileName = "sound/maou_damage.mp3";

    public static void setMainClass(Class mainClass) {
        StageDB.mainClass = mainClass;
    }

    public static MediaPlayer getMainSound() {
        if (mainSound == null) {
            try {
                Media m = new Media(new File(mainSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.3); // volume from 0.0 to 1.0
                mainSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return mainSound;
    }

    public static MediaPlayer getGameOverSound() {
        if (gameOverSound == null) {
            try {
                Media m = new Media(new File(gameOverSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setCycleCount(MediaPlayer.INDEFINITE); // loop play
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.7); // volume from 0.0 to 1.0
                gameOverSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return gameOverSound;
    }

    public static MediaPlayer healSound() {
        if (healSound == null) {
            try {
                Media m = new Media(new File(healSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.9); // volume from 0.0 to 1.0
                healSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return healSound;
    }

    public static MediaPlayer damageSound() {
        if (damageSound == null) {
            try {
                Media m = new Media(new File(damageSoundFileName).toURI().toString());
                MediaPlayer mp = new MediaPlayer(m);
                mp.setRate(1.0); // 1.0 = normal speed
                mp.setVolume(0.5); // volume from 0.0 to 1.0
                damageSound = mp;
            } catch (Exception io) {
                System.err.print(io.getMessage());
            }
        }
        return damageSound;
    }

    public static Stage getMainStage() {
        if (mainStage == null) {
            try {
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGame.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                mainStage = new Stage();
                mainStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return mainStage;
    }

    public static Stage getGameOverStage() {
        if (gameOverStage == null) {
            try {
                System.out.println("StageDB:getGameOverStage()");
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGameOver.fxml"));
                VBox root = loader.load();
                Scene scene = new Scene(root);
                gameOverStage = new Stage();
                gameOverStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return gameOverStage;
    }

    public static Stage getGameClearStage() {
        if (gameClearStage == null) {
            try {
                System.out.println("StageDB:getGameClearStage()");
                FXMLLoader loader = new FXMLLoader(mainClass.getResource("MapGameClear.fxml"));
                AnchorPane root = loader.load();
                Scene scene = new Scene(root);
                gameClearStage = new Stage();
                gameClearStage.setScene(scene);
            } catch (IOException ioe) {
                System.err.println(ioe);
            }
        }
        return gameClearStage;
    }
}
