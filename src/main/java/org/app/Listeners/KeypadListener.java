package org.app.Listeners;

import org.app.GraficsManager.DrawingPanel;
import org.app.GraficsManager.FigureDrawer;
import org.app.Structures.DxfParser;
import org.app.Structures.FiguresKeeper;
import org.app.Interface.FigureInfo;
import org.app.Structures.FileManager;
import org.app.Tools.Rotate;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class KeypadListener {
    // TODO: модификаторы доступа
    FiguresKeeper keeper;
    DrawingPanel panel;
    FigureInfo figureInfo;
    FigureDrawer figureDrawer;
    FileManager fileManager;

    public KeypadListener(JComponent component, FiguresKeeper keeper, DrawingPanel panel, FigureInfo figureInfo, MouseListener mouseListener, FigureDrawer figureDrawer, FileManager fileManager) throws IOException{
        super();

        InputMap inputMap = component.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = component.getActionMap();

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), "delete");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_R, 0), "r");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), "s");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0), "a");
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_N, 0), "n");

        this.keeper = keeper;
        this.panel = panel;
        this.figureInfo = figureInfo;
        this.figureDrawer = figureDrawer;
        this.fileManager = fileManager;

        actionMap.put("delete", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keeper.deleteSelectedFigure();
                figureInfo.showInfo();
                panel.repaint();
            }
        });

        actionMap.put("r", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (mouseListener.isPointsRotating) {
                    figureDrawer.isPointsRotating = false;
                } else {
                    Rotate rotate = new Rotate(keeper.getSelectedFigure());
                    mouseListener.setRotate(rotate);
                    figureDrawer.setRotate(rotate);
                    figureDrawer.isPointsRotating = true;
                }
                System.out.println(mouseListener.isPointsRotating);
            }
        });

        actionMap.put("s", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fileManager.saveFile();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
                System.out.println("saved to: " + fileManager.getActualDir());
            }
        });

        actionMap.put("a", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File dxfFile = FileManager.loadFile();
                    InputStream inputStream = new FileInputStream(dxfFile);
                    keeper.addNewFigures(DxfParser.parseDxf(inputStream));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("new figures added");
            }
        });

        actionMap.put("n", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File dxfFile = FileManager.loadFile();
                    InputStream inputStream = new FileInputStream(dxfFile);
                    keeper.setNewFigures(DxfParser.parseDxf(inputStream));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                System.out.println("new figures opened");
            }
        });

    }
}
