package org.app.GraficsManager;

import org.app.Figures.CoordinatePlane;
import org.app.Figures.Figure;
import org.app.Structures.FiguresKeeper;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Point2D;

public class DrawingPanel extends JPanel {
    private Figure currentFigure;
    private final FiguresKeeper figuresKeeper;
    private final double zoomFactor = 0.1;
    private double scale;
    private final CoordinatePlane coordinatePlane;
    private Point2D.Double cursorPosition;


    public DrawingPanel(FiguresKeeper figuresKeeper, CoordinatePlane coordinatePlane) {
        super();

        this.setPreferredSize(new Dimension(1820, 1080));
        this.setBackground(Color.BLACK);

        this.figuresKeeper = figuresKeeper;
        this.coordinatePlane = coordinatePlane;

        this.scale = coordinatePlane.getScale();

        cursorPosition = new Point2D.Double();
    }

    public void setCursorPosition(Point2D.Double p) {
        this.cursorPosition = p;
    }

    public void zoomIn(Point2D.Double p) {
        scale = 1 + zoomFactor;
        figuresKeeper.zoomAll(scale, p);

        repaint();
    }

    public void zoomOut(Point2D.Double p) {
        scale = 1 - zoomFactor;
        figuresKeeper.zoomAll(scale, p);
        repaint();
    }

    public void setCurrentFigure(Figure currentFigure) {
        this.currentFigure = currentFigure;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        coordinatePlane.drawAxes(g2, getWidth(), getHeight());
        figuresKeeper.drawAll(g2);

        // отрисовка координат курсора относительно нуля в масштабе
        g.setColor(Color.WHITE);
        String coords = String.
                format("X: %.2f, Y: %.2f",
                        cursorPosition.x / 100,
                        -cursorPosition.y / 100);
        g.drawString(coords, 10, getHeight() - 10);
    }

}
