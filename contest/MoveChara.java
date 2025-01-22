import javafx.animation.AnimationTimer;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MoveChara {
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP = 3;

    public static int HP = 5;

    private int posX;
    private int posY;

    private final MapData mapData;

    private final ImageView[] charaImageViews;
    private final ImageAnimation[] charaImageAnimations;

    private int charaDirection;

    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;

        Image[][] charaImages = new Image[4][3];
        charaImageViews = new ImageView[4];
        charaImageAnimations = new ImageAnimation[4];

        for (int i = 0; i < 4; i++) {
            charaImages[i] = new Image[3];
            for (int j = 0; j < 3; j++) {
                String[] directions = {"Down", "Left", "Right", "Up"};
                String[] animationNumbers = {"1", "2", "3"};
                String pngPathPre = "img/slime/";
                String pngPathSuf = ".png";
                charaImages[i][j] = new Image(
                        pngPathPre + directions[i] + animationNumbers[j] + pngPathSuf);
            }
            charaImageViews[i] = new ImageView(charaImages[i][0]);
            charaImageAnimations[i] = new ImageAnimation(
                    charaImageViews[i], charaImages[i]);
        }

        posX = startX;
        posY = startY;

        setCharaDirection(TYPE_RIGHT); // start with right-direction
    }

    // set the slime's direction
    public void setCharaDirection(int cd) {
        charaDirection = cd;
        for (int i = 0; i < 4; i++) {
            if (i == charaDirection) {
                charaImageAnimations[i].start();
            } else {
                charaImageAnimations[i].stop();
            }
        }
    }

    // check whether the slime can move on
    private boolean isMovable(int dx, int dy) {
        return !mapData.isWall(posX + dx, posY + dy);
    }

    // move the slime
    public void move(int dx, int dy) {
        if (isMovable(dx, dy)) {
            posX += dx;
            posY += dy;
            System.out.println("chara[X,Y]:" + posX + "," + posY);
        }
    }

    // getter: direction of the slime
    public ImageView getCharaImageView() {
        return charaImageViews[charaDirection];
    }

    // getter: x-position of the slime
    public int getPosX() {
        return posX;
    }

    // getter: y-position of the slime
    public int getPosY() {
        return posY;
    }

    // Show the slime animation
    private static class ImageAnimation extends AnimationTimer {

        private final ImageView charaView;
        private final Image[] charaImages;
        private int index;

        private long startTime = 0;

        private long count = 0L;
        private boolean isPlus = true;

        public ImageAnimation(ImageView charaView, Image[] images) {
            this.charaView = charaView;
            this.charaImages = images;
            this.index = 0;
        }

        @Override
        public void handle(long now) {
            if (startTime == 0) {
                startTime = now;
            }

            long preCount = count;
            // 500[ms]
            long duration = 500 * 1000000L;
            count = (now - startTime) / duration;
            if (preCount != count) {
                if (isPlus) {
                    index++;
                } else {
                    index--;
                }
                if (index < 0 || 2 < index) {
                    index = 1;
                    isPlus = !isPlus; // true == !false, false == !true
                }
                charaView.setImage(charaImages[index]);
            }
        }
    }
}
