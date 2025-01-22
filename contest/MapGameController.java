import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;

    @FXML
    public Circle circle_1;
    @FXML
    public Circle circle_2;
    @FXML
    public Circle circle_3;
    @FXML
    public Circle circle_4;
    @FXML
    public Circle circle_5;

    public void initMap() {
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        drawMap(chara);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        circle_1.setFill(Color.RED);
        circle_2.setFill(Color.RED);
        circle_3.setFill(Color.RED);
        circle_4.setFill(Color.RED);
        circle_5.setFill(Color.RED);
        initMap();
    }

    // Draw the map
    public void drawMap(MoveChara c) {
        int flagUp = 0;
        int flagDown = 0;
        int flagLeft = 0;
        int flagRight = 0;

        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y++) {
            for (int x = 0; x < mapData.getWidth(); x++) {
                if (x == cx && y == cy) {
                    if (mapData.getMap(x, y) == MapData.TYPE_HOLE) {
                        System.out.println("You've just fallen into a hole!");
                        initMap();
                        return;
                    } else if (mapData.getMap(x, y) == MapData.TYPE_HEALING_MUSH) {
                        System.out.println("You've got a healing mushroom!");
                        mapData.setMap(x, y, MapData.TYPE_SPACE);
                        mapData.setImageView(x, y, MapData.TYPE_SPACE);

                        StageDB.healSound().setOnEndOfMedia(() -> {
                            StageDB.healSound().seek(StageDB.healSound().getStartTime());
                            StageDB.healSound().stop();
                        });
                        StageDB.healSound().play();

                        if (chara.HP == 4) {
                            chara.HP++;
                            circle_5.setFill(Color.RED);
                        } else if (chara.HP == 3) {
                            chara.HP++;
                            circle_4.setFill(Color.RED);
                        } else if (chara.HP == 2) {
                            chara.HP++;
                            circle_3.setFill(Color.RED);
                        } else if (chara.HP == 1) {
                            chara.HP++;
                            circle_2.setFill(Color.RED);
                        }
                    } else if (mapData.getMap(x, y) == MapData.TYPE_POISON_MUSH) {
                        System.out.println("You've got a poison mushroom!");
                        mapData.setMap(x, y, MapData.TYPE_SPACE);
                        mapData.setImageView(x, y, MapData.TYPE_SPACE);

                        StageDB.damageSound().setOnEndOfMedia(() -> {
                            StageDB.damageSound().seek(StageDB.healSound().getStartTime());
                            StageDB.damageSound().stop();
                        });
                        StageDB.damageSound().play();

                        if (chara.HP == 5) {
                            chara.HP--;
                            circle_5.setFill(Color.TRANSPARENT);
                        } else if (chara.HP == 4) {
                            chara.HP--;
                            circle_4.setFill(Color.TRANSPARENT);
                        } else if (chara.HP == 3) {
                            chara.HP--;
                            circle_3.setFill(Color.TRANSPARENT);
                        } else if (chara.HP == 2) {
                            chara.HP--;
                            circle_2.setFill(Color.TRANSPARENT);
                        } else if (chara.HP == 1) {
                            chara.HP--;
                            circle_1.setFill(Color.TRANSPARENT);
                        }
                    } else if (mapData.getMap(x, y) == MapData.TYPE_GOAL) {
                        System.out.println("Congratulations! Game completed!");

                        StageDB.getMainStage().close();
                        StageDB.getMainSound().stop();

                        StageDB.getGameClearStage().show();
                    } else if (mapData.getMap(x, y) == MapData.TYPE_ARROW_UP) {
                        flagUp++;
                    } else if (mapData.getMap(x, y) == MapData.TYPE_ARROW_DOWN) {
                        flagDown++;
                    } else if (mapData.getMap(x, y) == MapData.TYPE_ARROW_LEFT) {
                        flagLeft++;
                    } else if (mapData.getMap(x, y) == MapData.TYPE_ARROW_RIGHT) {
                        flagRight++;
                    }

                    mapGrid.add(c.getCharaImageView(), x, y);
                } else {
                    mapGrid.add(mapData.getImageView(x, y), x, y);
                }
            }
        }
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Thread.sleep(100);
                return null;
            }
        };

        if (flagUp == 1) {
            task.setOnSucceeded(event -> upButtonAction());
            new Thread(task).start();
        } else if (flagDown == 1) {
            task.setOnSucceeded(event -> downButtonAction());
            new Thread(task).start();
        } else if (flagLeft == 1) {
            task.setOnSucceeded(event -> leftButtonAction());
            new Thread(task).start();
        } else if (flagRight == 1) {
            task.setOnSucceeded(event -> rightButtonAction());
            new Thread(task).start();
        }

        if (circle_1.getFill() == Color.TRANSPARENT) {
            task.setOnSucceeded(event -> {
                StageDB.getMainStage().close();
                StageDB.getMainSound().stop();

                circle_1.setFill(Color.RED);
                circle_2.setFill(Color.RED);
                circle_3.setFill(Color.RED);
                circle_4.setFill(Color.RED);
                circle_5.setFill(Color.RED);

                chara.HP = 5;

                StageDB.getGameOverSound().play();
                StageDB.getGameOverStage().show();
            });
            new Thread(task).start();
        }
    }

    // Get users' key actions
    public void keyAction(KeyEvent event) {
        KeyCode key = event.getCode();
        System.out.println("keycode:" + key);
        if (key == KeyCode.A) {
            leftButtonAction();
        } else if (key == KeyCode.S) {
            downButtonAction();
        } else if (key == KeyCode.W) {
            upButtonAction();
        } else if (key == KeyCode.D) {
            rightButtonAction();
        }
    }


    // Operations for going the slime up
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(0, -1);
        drawMap(chara);

    }

    // Operations for going the slime down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
        drawMap(chara);

    }

    // Operations for going the slime right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(-1, 0);
        drawMap(chara);

    }

    // Operations for going the slime right
    public void rightButtonAction() {
        printAction("RIGHT");
        chara.setCharaDirection(MoveChara.TYPE_RIGHT);
        chara.move(1, 0);
        drawMap(chara);

    }


    @FXML
    public void func1ButtonAction() {
        try {
            System.out.println("func1");
            StageDB.getMainStage().hide();
            StageDB.getMainSound().stop();
            StageDB.getGameOverSound().play();
            StageDB.getGameOverStage().show();
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @FXML
    public void func2ButtonAction() {
        System.out.println("func2: Redraw Map!!");

        initMap();
    }

    @FXML
    public void func3ButtonAction() {
        System.out.println("func3: Nothing to do");
    }

    @FXML
    public void func4ButtonAction() {
        System.out.println("func4: Nothing to do");

    }

    // Print actions of user inputs
    public void printAction(String actionString) {
        System.out.println("Action: " + actionString);
    }

}
