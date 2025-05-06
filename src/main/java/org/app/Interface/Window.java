package org.app.Interface;

import org.app.Figures.CoordinatePlane;
import org.app.GraficsManager.DrawingPanel;
import org.app.GraficsManager.FigureDrawer;
import org.app.Structures.FiguresKeeper;
import org.app.Listeners.KeypadListener;
import org.app.Listeners.MouseListener;
import org.app.Structures.FileManager;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Window extends JFrame {
    private final DrawingPanel drawingPanel;
    private final IconsPanel iconsPanel;
    private final FigureInfo figureInfo;
    private final InfoSpawnInput spawnInput;
    private final MouseListener mouseListener;
    private final FiguresKeeper figuresKeeper;
    private final FigureDrawer figureDrawer;
    private final CoordinatePlane coordinatePlane;
    private final FileManager fileManager;
    private final KeypadListener keypadListener;

    public Window() {
        this.pack();
        this.setSize(1920, 1080);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        coordinatePlane = new CoordinatePlane(getWidth(), getHeight());
        figuresKeeper = new FiguresKeeper(coordinatePlane);

        iconsPanel = new IconsPanel();
        drawingPanel = new DrawingPanel(figuresKeeper, coordinatePlane);
        figureInfo = new FigureInfo(figuresKeeper, drawingPanel, coordinatePlane);
        figureDrawer = new FigureDrawer(figuresKeeper, drawingPanel, iconsPanel, coordinatePlane, figureInfo);
        spawnInput = new InfoSpawnInput(2, drawingPanel, figureDrawer);
        fileManager = new FileManager(figuresKeeper);

        mouseListener = new MouseListener(drawingPanel, figureInfo, figuresKeeper, coordinatePlane, spawnInput, figureDrawer);

        try {
            keypadListener = new KeypadListener(drawingPanel, figuresKeeper, drawingPanel, figureInfo, mouseListener, figureDrawer, fileManager);
        } catch (IOException e) {
            throw new RuntimeException("Window initialization failed", e);
        }

        drawingPanel.setFocusable(true);

        //mouseListener = new MouseListener(drawingPanel, figureInfo, figuresKeeper, coordinatePlane, spawnInput, figureDrawer);

        drawingPanel.addMouseListener(mouseListener);
        drawingPanel.addMouseWheelListener(mouseListener);
        drawingPanel.addMouseMotionListener(mouseListener);
        drawingPanel.setFocusable(true);

        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(iconsPanel, BorderLayout.NORTH);
        this.add(drawingPanel, BorderLayout.CENTER);
        this.add(figureInfo, BorderLayout.WEST);
    }
}
