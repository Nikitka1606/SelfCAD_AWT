package org.app.Interface;

import org.app.GraficsManager.DrawingPanel;
import org.app.GraficsManager.FigureDrawer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;

public class InfoSpawnInput extends JPanel {
    private final ArrayList<JTextField> inputBoxes;
    private final DrawingPanel panel;
    private JPanel inputPanel;
    private final FigureDrawer figureDrawer;
    private final int boxesCount;
    private String coordinateSystem = "global";
    private String CSType = "polygon";
    private final JButton toggleButtonAbsGlo;
    private final JButton toggleButtonPolyPolar;
    //private Rotate rotate;

    public InfoSpawnInput(int boxesCount, DrawingPanel panel, FigureDrawer figureDrawer) {
        this.panel = panel;
        this.inputBoxes = new ArrayList<>();
        this.figureDrawer = figureDrawer;
        this.boxesCount = boxesCount;
        this.setFocusable(true);

        for (int i = 0; i < boxesCount; i++) {
            inputBoxes.add(new JTextField(10));
        }

        toggleButtonAbsGlo = new JButton(coordinateSystem);
        toggleButtonPolyPolar = new JButton(CSType);

        toggleButtonPolyPolar.addActionListener(e -> {
            if (CSType.equals("polygon")) {
                CSType = "polar";
                toggleButtonPolyPolar.setText(CSType);
            } else {
                CSType = "polygon";
                toggleButtonPolyPolar.setText(CSType);
            }
            //System.out.println("Coordinate System: " + coordinateSystem);
        });

        toggleButtonAbsGlo.addActionListener(e -> {
            if (coordinateSystem.equals("local")) {
                coordinateSystem = "global";
                toggleButtonAbsGlo.setText(coordinateSystem);
            } else {
                coordinateSystem = "local";
                toggleButtonAbsGlo.setText(coordinateSystem);
            }
            //System.out.println("Coordinate System: " + coordinateSystem);
        });

        for (int i = 1; i < inputBoxes.size() - 1; i++) {
            setupKeyNavigation(inputBoxes.get(i - 1), inputBoxes.get(i), inputBoxes.get(i + 1));
        }
        setupKeyNavigation(inputBoxes.get(inputBoxes.size() - 1), inputBoxes.get(0), inputBoxes.get(1));
        setupKeyNavigation(inputBoxes.get(inputBoxes.size() - 2), inputBoxes.get(inputBoxes.size() - 1), inputBoxes.get(0));
    }

    /*public void setRotate(Rotate rotate) {
        this.rotate = rotate;
    }*/

    public void showInputs(Point cursor) {
        hideInputs();

        inputPanel = new JPanel();

        inputPanel.add(toggleButtonAbsGlo);
        inputPanel.add(toggleButtonPolyPolar);

        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBounds(cursor.x, cursor.y, 80, 25 * (boxesCount + 2));

        for (JTextField f : inputBoxes) {
            inputPanel.add(f);
            inputPanel.add(Box.createRigidArea(new Dimension(0, 1)));
        }
        panel.setLayout(null);

        panel.add(inputPanel);
        inputBoxes.get(0).requestFocus();
        panel.repaint();
    }

    public void hideInputs() {
        if (inputPanel == null)
            return;
        panel.remove(inputPanel);
        for (JTextField field : inputBoxes) {
            inputPanel.remove(field);
        }
        panel.repaint();
    }

    public List<Double> getInputs() {
        return inputBoxes.stream()
                .map(JTextField::getText)
                .filter(text -> !text.isBlank())
                .map(Double::parseDouble)
                .toList();
    }

    private void setupKeyNavigation(JTextField previousField, JTextField currentField, JTextField nextField) {
        currentField.addKeyListener(new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    nextField.requestFocus();
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    previousField.requestFocus();
                }

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    figureDrawer.setNextPoint(getInputs(), coordinateSystem, CSType);
                }
            }

            @Override
            public void keyTyped(KeyEvent e) {
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
    }
}
