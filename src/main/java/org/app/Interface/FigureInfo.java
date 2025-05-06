package org.app.Interface;

import org.app.Figures.CoordinatePlane;
import org.app.GraficsManager.DrawingPanel;
import org.app.Structures.FiguresKeeper;
import org.app.Listeners.EnterListener;
import org.app.Structures.Measurement;
import org.app.Figures.FigureType;
import org.app.Figures.StrokeType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class FigureInfo extends JPanel {
    private final DrawingPanel drawingPanel;
    private final FiguresKeeper figuresKeeper;
    private final List<JLabel> figureValues;
    private final CoordinatePlane coordinatePlane;
    private final List<JTextField> figureValuesInput;
    GridBagConstraints gbc = new GridBagConstraints();
    JPanel innerPanel;
    private ArrayList<Measurement> currentFigureInfo;
    private JComboBox<String> strokeComboBox;

    public FigureInfo(FiguresKeeper figuresKeeper, DrawingPanel drawingPanel, CoordinatePlane coordinatePlane) {
        super();
        this.setPreferredSize(new Dimension(200, 1080));
        this.setEnabled(false);
        this.setVisible(true);

        innerPanel = new JPanel(new GridBagLayout());
        gbc.insets = new Insets(0, 0, 0, 0);

        gbc.anchor = GridBagConstraints.WEST;
        gbc.ipadx = 10;

        this.add(innerPanel);

        this.coordinatePlane = coordinatePlane;
        this.figuresKeeper = figuresKeeper;
        this.drawingPanel = drawingPanel;

        figureValues = new ArrayList<>();
        figureValuesInput = new ArrayList<>();
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /*private void clearValues() {
        if (figureValues.size() != 0) {
            for (JLabel l : figureValues) {
                l.setVisible(false);
                this.add(l);
            }
            figureValues.clear();
            //strokeComboBox.setVisible(false);
        }
    }*/

    private void clearValues() {
        for (JLabel label : figureValues) {
            innerPanel.remove(label); // Удаляем метку из панели
        }
        figureValues.clear(); // Очищаем список меток
    }

    /*private void clearValuesInputs() {
        if (figureValuesInput.size() != 0) {
            for (JTextField l : figureValuesInput) {
                l.setVisible(false);
                this.add(l);
            }
            figureValuesInput.clear();
        }
    }*/

    private void clearValuesInputs() {
        for (JTextField inputBox : figureValuesInput) {
            innerPanel.remove(inputBox); // Удаляем текстовое поле из панели
        }
        figureValuesInput.clear(); // Очищаем список текстовых полей
    }

    public void showInfo() {
        if (figuresKeeper.getCurrentFigure() != null) {
            if (figuresKeeper.getCurrentFigure().getInfo() == null)
                return;
            clearValues();
            clearValuesInputs();
            innerPanel.removeAll();
            innerPanel.repaint();
            drawingPanel.repaint();

            currentFigureInfo = figuresKeeper.getCurrentFigure().getInfo();

            int row = 0;

            for (Measurement val : currentFigureInfo) {
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.gridwidth = 1;

                JLabel label = new JLabel(val.getName());
                figureValues.add(label);
                innerPanel.add(label, gbc);

                gbc.gridx = 1;
                JTextField inputBox = new JTextField();

                if (val.isEditable()) {
                    inputBox.setText(String.format("%.2f", val.getValueInUnits()));
                    inputBox.addActionListener(new EnterListener(val, inputBox, figuresKeeper.getCurrentFigure(), drawingPanel, figuresKeeper, this));
                    inputBox.setVisible(true);
                } else {
                    inputBox.setText(" ");
                    inputBox.setVisible(false);
                }

                inputBox.setPreferredSize(new Dimension(50, 20));
                figureValuesInput.add(inputBox);
                innerPanel.add(inputBox, gbc);

                row++;
            }
            if (figuresKeeper.getCurrentFigure().getFigureType() != FigureType.POINT) {
                // stroke type
                gbc.gridx = 0;
                gbc.gridy = row;
                gbc.gridwidth = 2;

                StrokeType[] strokeOptions = StrokeType.values();
                JComboBox<StrokeType> strokeComboBox = new JComboBox<>(strokeOptions);
                strokeComboBox.setSelectedItem(figuresKeeper.getCurrentFigure().getStroke());

                strokeComboBox.addActionListener(e -> {
                    StrokeType selectedStroke = (StrokeType) strokeComboBox.getSelectedItem();
                    if (selectedStroke != null) {
                        figuresKeeper.getCurrentFigure().setStroke(selectedStroke, coordinatePlane.getScale());
                        drawingPanel.repaint();
                    }
                });
                innerPanel.add(strokeComboBox, gbc);

                // stroke scale
                gbc.gridx = 0;
                gbc.gridy = row + 1;
                gbc.gridwidth = 2;
                JTextField inputBox = new JTextField();
                inputBox.setText(String.format("%.2f", figuresKeeper.getCurrentFigure().getHatchScale()));
                Measurement val = new Measurement("Hatch", figuresKeeper.getCurrentFigure().getHatchScale(), "pp");
                inputBox.addActionListener(new EnterListener(val, inputBox, figuresKeeper.getCurrentFigure(), drawingPanel, figuresKeeper, this));
                inputBox.setVisible(true);

                innerPanel.add(inputBox, gbc);
            }
        } else {
            clearValues();
            clearValuesInputs();
            innerPanel.removeAll();
        }

        innerPanel.revalidate();
        innerPanel.repaint();
    }
}
