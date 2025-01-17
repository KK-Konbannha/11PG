import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    private static final String[] mapImageFiles = {
            "img/object/SPACE.png",
            "img/object/WALL.png"
    };
    public static final int TYPE_ITEM_DIG = 2;
    public static final int TYPE_ITEM_MASH = 3;
    public static final int TYPE_ITEM_POISON_MASH = 4;
    private static final String[] mapItemFiles = {
            "img/object/dig.png",
            "img/object/healingMushroom.png",
            "img/object/poisonMushroom.png"
    };

    private final Image[] mapImages;
    private final Image[] itemImages;
    private final ImageView[][] mapImageViews;
    private final ImageView[][] itemImageViews;
    private final int[][] maps;
    private final int[][] items;
    private final int width; // width of the map
    private final int height; // height of the map

    MapData(int x, int y) {
        mapImages = new Image[2];
        itemImages = new Image[mapItemFiles.length];
        mapImageViews = new ImageView[y][x];
        itemImageViews = new ImageView[y][x];
        for (int i = 0; i < 2; i ++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }
        for (int i = 0;i<mapItemFiles.length;i++) {
            itemImages[i] = new Image(mapItemFiles[i]);
        }

        width = x;
        height = y;
        maps = new int[y][x];
        items = new int[y][x];

        fillMap();
        fillItems();
        digMap(1, 3);
        placeDIG();
        placeItems();
        setImageViews();
    }

    // fill two-dimentional arrays with a given number (maps[y][x])
    private void fillMap() {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = MapData.TYPE_WALL;
            }
        }
    }
    private void fillItems() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                items[y][x] = MapData.TYPE_SPACE;
            }
        }
    }

    // dig walls for making roads
    private void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = { { 0, 1 }, { 0, -1 }, { -1, 0 }, { 1, 0 } };
        int[] tmp;

        for (int i = 0; i < dl.length; i ++) {
            int r = (int) (Math.random() * dl.length);
            tmp = dl[i];
            dl[i] = dl[r];
            dl[r] = tmp;
        }

        for (int[] ints : dl) {
            int dx = ints[0];
            int dy = ints[1];
            if (getMap(x + dx * 2, y + dy * 2) == MapData.TYPE_WALL) {
                setMap(x + dx, y + dy, MapData.TYPE_SPACE);
                digMap(x + dx * 2, y + dy * 2);
            }
        }
    }

    private void placeDIG() {
        int placedItems = 0;
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    // 条件を満たす場所に穴を配置
                    boolean isTop = isWall(x, y - 1) && /*上が壁*/ isWall(x - 1, y) && /*左が壁*/isWall(x + 1, y)/*右が壁*/ ;
                    boolean isRight = isWall(x, y -1) && /*上が壁*/isWall(x+1, y) && /*右が壁*/isWall(x, y +1) /*下が壁*/ ;
                    boolean isLeft = isWall(x, y -1) && /*上が壁*/isWall(x-1, y) && /*左が壁*/isWall(x, y +1) /*下が壁*/ ;
                    boolean isBottom = isWall(x+1, y ) && /*右が壁*/isWall(x-1, y) && /*左が壁*/isWall(x, y +1)/*下が壁*/ ;
                    if (maps[y][x] == TYPE_SPACE &&(isTop || isRight ||isLeft ||isBottom)
                        ) { 
                        items[y][x] = TYPE_ITEM_DIG; // dig.png を配置
                        placedItems++;
                        if (placedItems >= countAvailableSpots()) {
                            break;
                        }
                    }
                }
        }
        items[1][1] = TYPE_SPACE;
    }

    private void placeItems() {
        for (int i = 0; i < 5; i++) { // 5個のアイテムを配置
            int x, y;
            do {
                x = (int) (Math.random() * width);
                y = (int) (Math.random() * height);
            } while (maps[y][x] != TYPE_SPACE || items[y][x] != TYPE_SPACE); // 空白マスを選択
            items[y][x] = (Math.random() < 0.5) ? TYPE_ITEM_MASH : TYPE_ITEM_POISON_MASH;
        }
        items[1][1] = TYPE_SPACE;
    }
    

    public int getMap(int x, int y) {
        if (x < 0 || width <= x || y < 0 || height <= y) {
            return -1;
        }
        return maps[y][x];
    }

    public void setMap(int x, int y, int type) {
        if (x < 1 || width <= x - 1 || y < 1 || height <= y - 1) {
            return;
        }
        maps[y][x] = type;
    }

    public ImageView getImageView(int x, int y) {
        return mapImageViews[y][x];
    }


    public ImageView getItemImageView(int x, int y) {
        return itemImageViews[y][x];
    }


    public void setImageViews() {
        for (int y = 0; y < height; y ++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
                if (items[y][x] == TYPE_ITEM_DIG) {
                    itemImageViews[y][x] = new ImageView(itemImages[0]);
                } else if(items[y][x] == TYPE_ITEM_MASH) {
                    itemImageViews[y][x] = new ImageView(itemImages[1]);
                } else if(items[y][x] == TYPE_ITEM_POISON_MASH) {
                    itemImageViews[y][x] = new ImageView(itemImages[2]);
                } else {
                    itemImageViews[y][x] = null;
                }
            }
        }
    }

    
    

    public int[][] getItems() {
        return items;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    private int countAvailableSpots() {
        int count = 0;
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (maps[y][x] == TYPE_SPACE &&
                    isWall(x, y - 1) && // 上が壁
                    isWall(x - 1, y) && // 左が壁
                    isWall(x + 1, y)) { // 右が壁
                    count++;
                } else if (maps[y][x] == TYPE_SPACE &&
                    isWall(x, y -1) && // 上が壁
                    isWall(x+1, y) && //右が壁
                    isWall(x, y +1)){ //下が壁
                    count++;
                } else if (maps[y][x] == TYPE_SPACE &&
                    isWall(x -1, y ) && // 左が壁
                    isWall(x, y+1) && //下が壁
                    isWall(x +1, y )){ //右が壁
                    count++;
                } else if (maps[y][x] == TYPE_SPACE &&
                    isWall(x, y -1) && // 上が壁
                    isWall(x-1, y) && //左が壁
                    isWall(x, y +1)){ //下が壁
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isWall(int x, int y) {
        return getMap(x, y) == TYPE_WALL;
    }
}
