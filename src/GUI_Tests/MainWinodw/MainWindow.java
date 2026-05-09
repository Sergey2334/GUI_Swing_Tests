package GUI_Tests.MainWinodw;

import GUI_Tests.MainWinodw.ClientArea.ClientArea;
import GUI_Tests.MainWinodw.TitleBar.TitleBar;
import GUI_Tests.Utilities.MainWindowUtilities.WindowDragger;
import GUI_Tests.Utilities.MainWindowUtilities.WindowGlassResizer;
import GUI_Tests.Utilities.MyUtils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class MainWindow extends JFrame {
    private final String name = "Main Window";
    private TitleBar titleBar;
    private ClientArea clientArea;

    public MainWindow(String title) {
        this.initializeWindow(title);
        this.initializeWindowDraggable();
        this.initializeWindowResizable();

        // Fix the Animation Stalling Bug
        this.setupResizeListener();

        MyUtils.printSuccessfulInitialization(this.name);
    }

    private void initializeWindow(String title) {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Basic Constraints
        this.setMinimumSize(new Dimension(MyUtils.WINDOW_MIN_WIDTH, MyUtils.WINDOW_MIN_HEIGHT));
        MyUtils.applyRoundedCorners(this, MyUtils.ROUNDED_CORNERS_RADIUS);

        // Styling
        this.getContentPane().setBackground(MyUtils.COLOR_BLACK1);
        this.setBackground(MyUtils.COLOR_BLACK1);

        this.setTitle(title);
        MyUtils.setWindowIcon(this);

        // Component Assembly
        this.titleBar = new TitleBar(title);
        this.clientArea = new ClientArea();

        this.add(this.titleBar, BorderLayout.NORTH);
        this.add(this.clientArea, BorderLayout.CENTER);

        this.pack();
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }

    private void setupResizeListener() {
        // This ensures that when you drag the window edge,
        // the GameLauncher knows to refresh its background and grid.
        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                // Re-validate the layout so the game fills the new space
                MainWindow.this.revalidate();
                MainWindow.this.repaint();
            }
        });
    }

    private void initializeWindowDraggable() {
        // Updated to use our new simpler API from WindowDragger
        WindowDragger.makeDraggable(this.titleBar);
    }

    private void initializeWindowResizable() {
        WindowGlassResizer glassResizer = new WindowGlassResizer(this);
        this.setGlassPane(glassResizer);
        this.getGlassPane().setVisible(true);
    }

    // --- GETTERS (Using the built-in JFrame data) ---

    public Rectangle getWindowBounds() {
        return this.getBounds();
    }
}