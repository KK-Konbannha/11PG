import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MapGameController implements Initializable {
    public MapData mapData;
    public MoveChara chara;
    public GridPane mapGrid;
    public ImageView[] mapImageViews;

    public void initMap() {
        mapData = new MapData(21, 15);
        chara = new MoveChara(1, 1, mapData);
        mapImageViews = new ImageView[mapData.getHeight() * mapData.getWidth()];
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                mapImageViews[index] = mapData.getImageView(x, y);
            }
        }
        drawMap(chara);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initMap();
    }

    // Draw the map
    public void drawMap(MoveChara c) {
        int cx = c.getPosX();
        int cy = c.getPosY();
        mapGrid.getChildren().clear();
        for (int y = 0; y < mapData.getHeight(); y ++) {
            for (int x = 0; x < mapData.getWidth(); x ++) {
                int index = y * mapData.getWidth() + x;
                if (x == cx && y == cy) {
                    mapGrid.add(c.getCharaImageView(), x, y);

                    if (mapData.getItems()[cy][cx] == MapData.TYPE_ITEM_DIG) {
                        System.out.println("You found a dig item!");
                        initMap();
                        return;
                    }
                    
                } else if(mapData.getItemImageView(x,y) != null) {
                    mapGrid.add(mapData.getItemImageView(x, y),x,y);
                } else  {
                    mapGrid.add(mapImageViews[index], x, y);
                }
            }
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

    
    // Operations for going the cat up
    public void upButtonAction() {
        printAction("UP");
        chara.setCharaDirection(MoveChara.TYPE_UP);
        chara.move(0, -1);
        drawMap(chara);
        
    }

    // Operations for going the cat down
    public void downButtonAction() {
        printAction("DOWN");
        chara.setCharaDirection(MoveChara.TYPE_DOWN);
        chara.move(0, 1);
        drawMap(chara);
        
    }

    // Operations for going the cat right
    public void leftButtonAction() {
        printAction("LEFT");
        chara.setCharaDirection(MoveChara.TYPE_LEFT);
        chara.move(-1, 0);
        drawMap(chara);
       
    }

    // Operations for going the cat right
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
