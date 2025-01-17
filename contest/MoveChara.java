import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
import javafx.animation.AnimationTimer;

public class MoveChara {
    public static final int TYPE_DOWN = 0;
    public static final int TYPE_LEFT = 1;
    public static final int TYPE_RIGHT = 2;
    public static final int TYPE_UP = 3;

    private final String pngPathPre = "png/slime";
    private final String pngPathSuf = ".png";

    private int posX;
    private int posY;

    private MapData mapData;

    private Image[] charaImages;
    private ImageView charaImageView;
    private ImageAnimation charaImageAnimation;

    private int charaDirection;

    MoveChara(int startX, int startY, MapData mapData) {
        this.mapData = mapData;

        // 21枚のスライム画像をロード
        charaImages = new Image[21];
        for (int i = 0; i < 21; i++) {
            charaImages[i] = new Image(pngPathPre + (i + 1) + pngPathSuf);
        }
        charaImageView = new ImageView(charaImages[0]);
        charaImageAnimation = new ImageAnimation(charaImageView, charaImages);

        posX = startX;
        posY = startY;

        setCharaDirection(TYPE_RIGHT); // start with right-direction
    }

    // set the cat's direction
    public void setCharaDirection(int cd) {
        charaDirection = cd;
        charaImageAnimation.start();
    }

    // check whether the cat can move on
    private boolean isMovable(int dx, int dy) {
        if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_WALL) {
            return false;
        } else if (mapData.getMap(posX + dx, posY + dy) == MapData.TYPE_SPACE) {
            return true;
        }
        return false;
    }

    // move the cat
    public boolean move(int dx, int dy) {
        if (isMovable(dx, dy)) {
            posX += dx;
            posY += dy;
            System.out.println("chara[X,Y]:" + posX + "," + posY);
            return true;
        } else {
            return false;
        }
    }



    // getter: direction of the cat
    public ImageView getCharaImageView() {
        return charaImageView;
    }

    // getter: x-positon of the cat
    public int getPosX() {
        return posX;
    }

    // getter: y-positon of the cat
    public int getPosY() {
        return posY;
    }

    // Show the cat animation
    private class ImageAnimation extends AnimationTimer {

        private ImageView charaView = null;
        private Image[] charaImages = null;
        private int index = 0;

        private long duration = 100 * 1000000L; // 100[ms] per frame
        private long startTime = 0;

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

            if ((now - startTime) > duration) {
                index = (index + 1) % charaImages.length; // 21枚をループ
                charaView.setImage(charaImages[index]);
                startTime = now;
            }
        }
    }
}
