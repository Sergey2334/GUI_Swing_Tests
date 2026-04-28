package GUI_Tests;

import javax.swing.*;
import java.awt.*;

public class MainWindow extends JFrame {
    private final String name = "Main Window";

    private int windowBoundX;
    private int windowBoundY;

    private int windowWidth;
    private int windowHeight;


    private MainWindowTitleBar mainWindowTitleBar;
    private MainWindowClientArea mainWindowClientArea;


    public MainWindow(String title) {
        this.initializeWindow(title);
        this.initializeWindowDraggable();
        this.initializeWindowResizable();

        // TEST
        this.windowBoundX = this.getBounds().x;
        this.windowBoundY = this.getBounds().y;
        this.windowWidth = this.getBounds().width;
        this.windowHeight = this.getBounds().height;

        MyUtils.printSuccessfulInitiation(this.name);
    }

    private void initializeWindow(String title) {
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(false);
        this.setLayout(new BorderLayout());

        this.setMinimumSize(new Dimension(MyUtils.WINDOW_MIN_WIDTH, MyUtils.WINDOW_MIN_HEIGHT));
        MyUtils.applyRoundedCorners(this, MyUtils.ROUNDED_CORNERS_RADIUS);
        this.setLocationRelativeTo(null);

        this.setBackground(MyUtils.COLOR_TRANSPARENT); // Does Background, but ContentPane is on Top, unless Transparent
        this.getContentPane().setBackground(MyUtils.COLOR_TRANSPARENT); // If JFrame Background is Transparent , then this won't matter

        this.setName(title);
        this.setTitle(title);
        MyUtils.setWindowIcon(this);


        this.mainWindowTitleBar = new MainWindowTitleBar(title);
        this.mainWindowClientArea = new MainWindowClientArea();
        this.add(this.mainWindowTitleBar, BorderLayout.NORTH);
        this.add(this.mainWindowClientArea, BorderLayout.CENTER);


        // TEST
        JButton b1 = new JButton("Start");
        b1.setBackground(Color.GRAY);
        b1.setForeground(Color.WHITE);
//        b1.setOpaque(true);
        b1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b1.setFocusable(true);
        b1.setFocusPainted(false);
        b1.addActionListener((e) -> {
            this.printClicked("Clicked !");
            this.setNewWindowSizeTEST();
        });
        this.add(b1, BorderLayout.EAST);


        this.pack();
        this.setVisible(true);
    }

    private void initializeWindowDraggable()
    {
        WindowDragger.makeDraggable(this, mainWindowTitleBar); // Making the Window Draggable
    }

    private void initializeWindowResizable()
    {
        WindowGlassResizer glassResizer = new WindowGlassResizer(this);
        this.setGlassPane(glassResizer);
        glassResizer.setVisible(true);
    }

    // TEST
    private void printClicked(String text) {
        System.out.println(text);
    }

    // TEST
    private void setNewWindowSizeTEST() {
        this.windowBoundX = this.getBounds().x;
        this.windowBoundY = this.getBounds().y;
        this.windowWidth = this.getBounds().width;
        this.windowHeight = this.getBounds().height;

        this.windowBoundX -= 25;
        this.windowBoundY -= 25;
        this.windowWidth += 50;
        this.windowHeight += 50;
        this.setBounds(this.windowBoundX, this.windowBoundY, this.windowWidth, this.windowHeight);
    }
}
