package GUI_Tests.Managers;

import GUI_Tests.Utilities.MyUtils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextureManager {
    // The Single Instance (Static)
    private static TextureManager instance;
    private final Map<String, BufferedImage> textures = new HashMap<>();

    // Private Constructor (Max OOP: Prevents other classes from using 'new')
    private TextureManager() {
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[0], "src/GUI_Tests/Assets/Images/tron_bike_cyan.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[1], "src/GUI_Tests/Assets/Images/tron_bike_green.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[2], "src/GUI_Tests/Assets/Images/tron_bike_orange.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[3],"src/GUI_Tests/Assets/Images/tron_bike_pink.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[4], "src/GUI_Tests/Assets/Images/tron_bike_purple.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[5], "src/GUI_Tests/Assets/Images/tron_bike_red.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[6], "src/GUI_Tests/Assets/Images/tron_bike_white.png");
        this.loadTexture(MyUtils.TRON_COLORS_ARRAY[7], "src/GUI_Tests/Assets/Images/tron_bike_yellow.png");
        this.loadTexture("powerup_boost", "src/GUI_Tests/Assets/Images/powerup_boost.png");
        this.loadTexture("powerup_invincible", "src/GUI_Tests/Assets/Images/powerup_invincebilty.png");
    }

    // Global Access Point
    public static TextureManager getInstance() {
        if (instance == null) {
            instance = new TextureManager();
        }
        return instance;
    }

    private void loadTexture(String key, String path) {
        try {
            File file = new File(path);
            if (file.exists()) {
                this.textures.put(key, ImageIO.read(file));
            } else {
                System.err.println("Texture file missing: " + path);
            }
        } catch (IOException e) {
            System.err.println("Error loading: " + path);
        }
    }

    public BufferedImage getTexture(String key) {
        return this.textures.get(key);
    }
}