package org.app.Listeners;

import org.app.Interface.IconsPanel;
import org.app.Figures.FigureType;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class FigureIcon extends JButton {
    private FigureType callableFigure;
    private int curInstance;

    public FigureIcon(ArrayList<FigureType> instances, IconsPanel iconsPanel) {
        curInstance = 0;
        this.callableFigure = instances.get(0);
        setText(callableFigure.toString());
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    iconsPanel.setSelectedFigure(callableFigure);
                } else if (SwingUtilities.isRightMouseButton(e)) {
                    curInstance++;
                    if (curInstance >= instances.size())
                        curInstance = 0;

                    callableFigure = instances.get(curInstance);
                    setText(callableFigure.toString());
                    iconsPanel.setSelectedFigure(callableFigure);
                }
            }
        });
    }
}
