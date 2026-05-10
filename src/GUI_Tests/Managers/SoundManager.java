package GUI_Tests.Managers;

import javax.sound.sampled.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * SoundManager handles all game audio using a Singleton pattern.
 * Supports playing individual sounds or random selections from groups.
 */
public class SoundManager {
    private static SoundManager instance;
    private final Map<String, Clip> clips = new HashMap<>();
    private final Map<String, String[]> soundGroups = new HashMap<>();
    private final Random random = new Random();

    private SoundManager() {
        // 1. Load Individual Sounds
        this.loadSound("app_start", "src/GUI_Tests/Assets/Sounds/app_start.wav");
        this.loadSound("game_music", "src/GUI_Tests/Assets/Sounds/game_music.wav");
        this.loadSound("bg_music", "src/GUI_Tests/Assets/Sounds/bg_music.wav");

        this.loadSound("countdown", "src/GUI_Tests/Assets/Sounds/countdown.wav");

        this.loadSound("crush", "src/GUI_Tests/Assets/Sounds/crash.wav");
        this.loadSound("crush_mutual", "src/GUI_Tests/Assets/Sounds/crash_mutual.wav");

        this.loadSound("menu_enter", "src/GUI_Tests/Assets/Sounds/menu_enter.wav");

        this.loadSound("menu_option", "src/GUI_Tests/Assets/Sounds/menu_option.wav");

        this.loadSound("menu_turn", "src/GUI_Tests/Assets/Sounds/menu_turn.wav");

        this.loadSound("powerup_boost1", "src/GUI_Tests/Assets/Sounds/powerup_boost1.wav");
        this.loadSound("powerup_boost2", "src/GUI_Tests/Assets/Sounds/powerup_boost2.wav");

        this.loadSound("powerup_invincibility1", "src/GUI_Tests/Assets/Sounds/powerup_invincibility1.wav");
        this.loadSound("powerup_invincibility2", "src/GUI_Tests/Assets/Sounds/powerup_invincibility2.wav");

        // 2. Define Groups for Random Playback
        this.soundGroups.put("boost", new String[]{"powerup_boost1", "powerup_boost2"});
        this.soundGroups.put("invincibility", new String[]{"powerup_invincibility1", "powerup_invincibility2"});
    }

    public static synchronized SoundManager getInstance() {
        if (instance == null) {
            instance = new SoundManager();
        }
        return instance;
    }

    private void loadSound(String key, String path) {
        try {
            File file = new File(path);
            if (!file.exists()) {
                System.err.println("Sound file not found: " + path);
                return;
            }

            AudioInputStream stream = AudioSystem.getAudioInputStream(file);
            AudioFormat format = stream.getFormat();

            if (format.getSampleSizeInBits() > 16) {
                System.err.println("CRITICAL ERROR: [" + key + "] is " + format.getSampleSizeInBits() +
                        "-bit. Java only supports 8 or 16-bit .wav files.");
                return;
            }

            Clip clip = AudioSystem.getClip();
            clip.open(stream);
            this.clips.put(key, clip);

        } catch (Exception e) {
            System.err.println("Failed to load sound: " + path);
        }
    }

    /**
     * Plays a random sound from a pre-defined group.
     * If the group doesn't exist, it tries to play the key as a single sound.
     */
    public void playRandom(String groupKey) {
        if (this.soundGroups.containsKey(groupKey)) {
            String[] group = this.soundGroups.get(groupKey);
            String randomKey = group[this.random.nextInt(group.length)];
            this.play(randomKey);
        } else {
            this.play(groupKey); // Fallback to normal play
        }
    }

    public void play(String key) {
        Clip clip = this.clips.get(key);
        if (clip != null) {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
        }
    }

    public void loop(String key) {
        Clip clip = this.clips.get(key);
        if (clip != null) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void stop(String key) {
        Clip clip = this.clips.get(key);
        if (clip != null) {
            synchronized (clip) { // Prevent thread interference
                clip.stop();
                clip.flush();          // Clears the audio buffer immediately
                clip.setFramePosition(0); // Reset to start
            }
        }
    }

    public void stopGroup(String groupKey) {
        if (this.soundGroups.containsKey(groupKey)) {
            String[] group = this.soundGroups.get(groupKey);
            for (String key : group) {
                this.stop(key);
            }
        }
    }

    public void stopAll() {
        for (Clip clip : this.clips.values()) {
            if (clip != null) {
                synchronized (clip) {
                    clip.stop();
                    clip.flush();
                    clip.setFramePosition(0);
                }
            }
        }
    }
}