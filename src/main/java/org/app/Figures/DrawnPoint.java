package org.app.Figures;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class DrawnPoint extends Figure {
    public DrawnPoint() {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    public DrawnPoint(Figure f, Figure parent) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        this.start = f.start;
        this.parentFigure.add(parent);
    }

    public DrawnPoint(Point2D.Double point, CoordinatePlane plane, Figure parent) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        setNextPoint(point, plane);
        this.parentFigure.add(parent);
    }

    public DrawnPoint(Point2D.Double point, CoordinatePlane plane) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        setNextPoint(point, plane);
    }

    public DrawnPoint(Point2D.Double point, Figure parent) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        this.start = point;
        this.parentFigure.add(parent);

        updateParams();
        gatherInfo();
    }

    public DrawnPoint(Point2D.Double point, CoordinatePlane plane, boolean visibility, Figure parent) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        setNextPoint(point, plane);
        this.parentFigure.add(parent);

        this.isDrawable = visibility;
    }

    /*public DrawnPoint(Point2D.Double point) {
        figureType = FigureType.POINT;

        updated.add(false);

        preferredColor = Color.GREEN;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();

        setNextPoint(point, plane);
        this.parentFigure.add(parent);

        this.isDrawable = visibility;
    }*/

    public boolean isCollinear() {
        return parentFigure.size() > 1;
    }

    @Override
    public void updateValuesFromInput() {
        start.x = info.get(1).getValueInPixels();
        start.y = -info.get(2).getValueInPixels();

        getInfo();
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new org.app.Structures.Measurement("Point:"));
            add(new org.app.Structures.Measurement("    x: ", start.x, "mm"));
            add(new org.app.Structures.Measurement("    y: ", -start.y, "mm"));
        }};
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        this.start = plane.toLocal(point);

        updateParams();
        gatherInfo();

    }

    @Override
    public Point2D.Double setNextPoint(Figure magnetPoint, Figure parent, CoordinatePlane plane) {
        //this.start = magnetPoint.start;
        setNextPoint(plane.toGlobal(magnetPoint.start), plane);
        magnetPoint.parentFigure.add(parent);
        updateParams();
        gatherInfo();

        return this.start;
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (isDrawable) {
            if (start != null) {
                drawPoint(g, plane.toGlobal(start), color);
            }
        }
    }

    @Override
    public void moveFigure(double dx, double dy) {
        start.x += dx;
        start.y += dy;
    }


    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (start == null)
            return false;

        return start.distance(plane.toLocal(p)) <= 5 / plane.getScale() && isDrawable;
    }

    @Override
    public boolean isDrown() {
        return start != null;
    }

    @Override
    public String toString() {
        return start.toString();
    }

    public void writeToDXF(BufferedWriter writer) throws IOException {
        writer.write("10\n" + (start.x / 10) + "\n");
        writer.write("20\n" + (-start.y / 10) + "\n");
        writer.write("30\n0.0\n");
    }
}
