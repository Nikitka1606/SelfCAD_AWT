package org.app.Interface;

import org.app.Listeners.FigureIcon;
import org.app.Figures.FigureType;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class IconsPanel extends JPanel {
    /*private JButton[] toolbar = new JButton[5];
    private ArrayList<JButton> toolbar1;*/
    private final FigureIcon pointIcon;
    private final FigureIcon lineIcon;
    private final FigureIcon rectIcon;
    private final FigureIcon circleIcon;
    private final FigureIcon arcIcon;
    private final FigureIcon polygonIcon;
    private final FigureIcon splineIcon;
    private FigureType selectedFigure;

    public IconsPanel() {
        pointIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.POINT);
        }}, this);
        lineIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.LINE);
        }}, this);
        rectIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.RECTANGLE);
            add(FigureType.THREE_POINT_RECTANGLE);
        }}, this);
        circleIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.CIRCLE);
            add(FigureType.THREE_POINT_CIRCLE);
        }}, this);
        arcIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.ARC);
        }}, this);
        polygonIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.POLYGON_DESC);
            add(FigureType.POLYGON_INC);
        }}, this);
        splineIcon = new FigureIcon(new ArrayList<>() {{
            add(FigureType.BEZIER_SPLINE);
            add(FigureType.B_SPLINE);
        }}, this);

        this.setLayout(new GridLayout(1, 4));

        this.setEnabled(false);

        this.add(pointIcon);
        this.add(lineIcon);
        this.add(rectIcon);
        this.add(circleIcon);
        this.add(arcIcon);
        this.add(polygonIcon);
        this.add(splineIcon);

        this.setPreferredSize(new Dimension(1000, 100));
    }

    public FigureType getSelectedFigure() {
        return selectedFigure;
    }

    public void setSelectedFigure(FigureType ft) {
        selectedFigure = ft;
    }

    public void setCurrentFigureNull() {
        selectedFigure = null;
    }
}
