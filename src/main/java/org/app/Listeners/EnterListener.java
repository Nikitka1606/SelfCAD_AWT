package org.app.Listeners;

import org.app.GraficsManager.DrawingPanel;
import org.app.Structures.FiguresKeeper;
import org.app.Interface.FigureInfo;
import org.app.Structures.Measurement;
import org.app.Figures.Figure;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Objects;

public class EnterListener extends AbstractAction {
    Measurement measurement;
    JTextField field;
    Figure figure;
    DrawingPanel panel;
    FigureInfo info;
    FiguresKeeper keeper;

    public EnterListener(Measurement measurement,
                         JTextField field,
                         Figure figure,
                         DrawingPanel panel,
                         FiguresKeeper keeper,
                         FigureInfo info) {
        super();
        this.measurement = measurement;
        this.field = field;
        this.figure = figure;
        this.panel = panel;
        this.keeper = keeper;
        this.info = info;
    }


    private static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isNumeric(field.getText())) {
            if (Objects.equals(measurement.getName(), "Hatch"))
                figure.setHatchScale(Float.parseFloat(field.getText()));
            else
                measurement.setValueInUnits(Float.parseFloat(field.getText()));

            figure.updateValuesFromInput();
            figure.updateParams();
            //figure.checkConstrains();

            keeper.update();

            info.showInfo();
            panel.repaint();
        }
    }
}

