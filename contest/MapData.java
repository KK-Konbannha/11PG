import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MapData {
    public static final int TYPE_SPACE = 0;
    public static final int TYPE_WALL = 1;
    public static final int TYPE_GOAL = 2;
    public static final int TYPE_HOLE = 3;
    public static final int TYPE_HEALING_MUSH = 4;
    public static final int TYPE_POISON_MUSH = 5;

    private static final String[] mapImageFiles = {
            "img/object/SPACE.png", "img/object/WALL.png", "img/object/GOAL.png",
            "img/object/HOLE.png", "img/object/HEALING_MUSH.png", "img/object/POISON_MUSH.png"};


    private final Image[] mapImages;
    private final ImageView[][] mapImageViews;
    private final int[][] maps;
    private final int width; // width of the map
    private final int height; // height of the map


    MapData(int x, int y) {
        mapImages = new Image[mapImageFiles.length];
        mapImageViews = new ImageView[y][x];
        for (int i = 0; i < mapImageFiles.length; i++) {
            mapImages[i] = new Image(mapImageFiles[i]);
        }

        width = x;
        height = y;
        maps = new int[y][x];

        fillMap();
        digMap(1, 3);

        placeGoal();
        placeHealingMush();
        placeHole();
        placePoisonMush();

        setImageViews();
    }

    // fill two-dimentional arrays with a given number (maps[y][x])
    private void fillMap() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                maps[y][x] = MapData.TYPE_WALL;
            }
        }
    }

    // dig walls for making roads
    private void digMap(int x, int y) {
        setMap(x, y, MapData.TYPE_SPACE);
        int[][] dl = {{0, 1}, {0, -1}, {-1, 0}, {1, 0}};
        int[] tmp;

        for (int i = 0; i < dl.length; i++) {
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

    private void placeGoal() {
        int[] deepestSpace = findDeepestSpace();
        setMap(deepestSpace[0], deepestSpace[1], MapData.TYPE_GOAL);
    }

    // 回復きのこを配置
    // digMap(mapの道の配置)の後に実行すること
    private void placeHealingMush() {
        // 回復きのこの数
        int HEALING_MUSH_NUM = 3;

        // 行き止まりの場所のリストを取得
        List<int[]> deadEndPlaces = findDeadEndPlace();

        // 行き止まりの場所のリストの中から何番目の場所に配置するかを決める
        List<Integer> placeNums = getRandomNum(deadEndPlaces.size(), HEALING_MUSH_NUM);

        for (Integer placeNum : placeNums) {
            int[] place = deadEndPlaces.get(placeNum);

            setMap(place[0], place[1], TYPE_HEALING_MUSH);
        }
    }

    // 落とし穴を配置
    // placeHealingMushの後に実行すること
    private void placeHole() {
        // 落とし穴の数
        int HOLE_NUM = 3;

        // 行き止まりの場所のリストを取得
        List<int[]> deadEndPlaces = findDeadEndPlace();

        // 行き止まりの場所のリストの中から何番目の場所に配置するかを決める
        List<Integer> placeNums = getRandomNum(deadEndPlaces.size(), HOLE_NUM);

        for (Integer placeNum : placeNums) {
            int[] place = deadEndPlaces.get(placeNum);

            setMap(place[0], place[1], TYPE_HOLE);
        }
    }

    // 毒きのこを配置
    // placeHoleの後に実行すること
    private void placePoisonMush() {
        // 毒きのこの数
        int POISON_MUSH_NUM = 3;

        List<int[]> spaces = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isSpace(x, y) && !(x == 1 && y == 1))
                    spaces.add(new int[]{x, y});
            }
        }

        // 道のリストの中から何番目の場所に配置するかを決める
        List<Integer> placeNums = getRandomNum(spaces.size(), POISON_MUSH_NUM);

        for (Integer placeNum : placeNums) {
            int[] space = spaces.get(placeNum);

            setMap(space[0], space[1], TYPE_POISON_MUSH);
        }
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


    public void setImageViews() {
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                mapImageViews[y][x] = new ImageView(mapImages[maps[y][x]]);
            }
        }
    }

    public void setImageView(int x, int y, int type) {
        mapImageViews[y][x] = new ImageView(mapImages[type]);
    }


    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    boolean isWall(int x, int y) {
        return getMap(x, y) == TYPE_WALL;
    }

    boolean isSpace(int x, int y) {
        return getMap(x, y) == TYPE_SPACE;
    }

    boolean isDeadEnd(int x, int y) {
        int openDirections = 0;

        final boolean canMoveUp = !isWall(x, y - 1);
        final boolean canMoveDown = !isWall(x, y + 1);
        final boolean canMoveLeft = !isWall(x - 1, y);
        final boolean canMoveRight = !isWall(x + 1, y);

        if (canMoveUp) openDirections++;
        if (canMoveDown) openDirections++;
        if (canMoveLeft) openDirections++;
        if (canMoveRight) openDirections++;

        return (openDirections == 1) && isSpace(x, y);
    }

    // 1からある数字(endNum)までの中から特定個数(retNum)の数字をランダムに取得する
    private List<Integer> getRandomNum(int endNum, int retNum) {
        ArrayList<Integer> nums = new ArrayList<>();
        for (int i = 0; i < endNum; i++) {
            nums.add(i);
        }

        Collections.shuffle(nums);

        if (nums.size() < retNum) {
            return nums;
        } else {
            return nums.subList(0, retNum);
        }
    }

    // 行き止まりの場所を列挙
    private List<int[]> findDeadEndPlace() {
        ArrayList<int[]> deadEndPlace = new ArrayList<>();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (isDeadEnd(x, y) && !(x == 1 && y == 1)) {
                    deadEndPlace.add(new int[]{x, y});
                }
            }
        }

        return deadEndPlace;
    }

    // ある地点（スタート地点）から一番遠い地点をdeepestFirstSearch（深さ優先探索）で探す
    private int[] findDeepestSpace() {
        // 開始位置
        final int startX = 1;
        final int startY = 1;

        final int[] deepestSpace = {startX, startY};
        final int[][] visited = new int[height][width];

        dfs(startX, startY, 0, visited, deepestSpace, -1);

        return deepestSpace;
    }

    // 深さ優先探索のメインの処理
    private int dfs(int x, int y, int depth, int[][] visited, int[] deepestSpace, int maxDepth) {
        // 基本条件：訪問済みの場合は探索終了
        if (visited[y][x] == 1) {
            return maxDepth;
        }

        // 訪問状態を更新
        visited[y][x] = 1;

        // 最深部の更新
        if (depth > maxDepth) {
            maxDepth = depth;
            deepestSpace[0] = x;
            deepestSpace[1] = y;
        }

        // 4方向それぞれに再帰的に探索
        int[][] directions = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] direction : directions) {
            int newX = x + direction[0];
            int newY = y + direction[1];
            if (isSpace(newX, newY)) {
                maxDepth = dfs(newX, newY, depth + 1, visited, deepestSpace, maxDepth);
            }
        }

        return maxDepth;
    }
}
