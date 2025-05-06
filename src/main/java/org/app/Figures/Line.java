package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Line extends Figure {
    public double da;
    private double length;
    private double angle;

    public Line() {
        figureType = FigureType.LINE;

        maxChildren = 2;

        basicParameters.add(this.length);
        basicParameters.add(this.angle);

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    public Line(Figure p1, Figure p2, Figure parent) {
        figureType = FigureType.LINE;

        maxChildren = 2;

        children.add(p1);
        children.add(p2);

        this.parentFigure.add(parent);

        for (Figure c : children)
            c.setParentFigure(this);

        basicParameters.add(this.length);
        basicParameters.add(this.angle);

        setUpdates();

        updateParams();
        gatherInfo();

        setNotUpdated();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    public Line(Figure p1, Figure p2) {
        figureType = FigureType.LINE;

        maxChildren = 2;

        children.add(p1);
        children.add(p2);

        for (Figure c : children)
            c.setParentFigure(this);

        basicParameters.add(this.length);
        basicParameters.add(this.angle);

        setUpdates();

        updateParams();
        gatherInfo();

        setNotUpdated();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    @Override
    public void updateBasicParameter(int index) {
        switch (index) {
            case 0 -> {
                basicParameters.set(0, children.get(0).start.distance(children.get(1).start));
                updated.set(0, true);
            }
            case 1 -> {
                angle = Math.atan2(children.get(1).start.y - children.get(0).start.y,
                        children.get(1).start.x - children.get(0).start.x);
                if (angle > Math.abs(Math.PI * 2))
                    angle %= Math.PI * 2;
                while (angle < 0) {
                    angle += Math.PI * 2;
                }
                basicParameters.set(1, angle);
                updated.set(1, true);
            }
            default -> {
            }
        }
    }

    @Override
    public void updateChildren() {
        if (!children.get(0).isFixed()) {
            children.get(0).start.x = (children.get(1).start.x - basicParameters.get(0) * Math.cos(basicParameters.get(1)));
            children.get(0).start.y = (children.get(1).start.y - basicParameters.get(0) * Math.sin(basicParameters.get(1)));
            return;
        }

        if (!children.get(1).isFixed()) {
            children.get(1).start.x = (children.get(0).start.x - basicParameters.get(0) * Math.cos(basicParameters.get(1)));
            children.get(1).start.y = (children.get(0).start.y - basicParameters.get(0) * Math.sin(basicParameters.get(1)));
        }
    }

    @Override
    public void updateValuesFromInput() {
        basicParameters.set(0, info.get(1).getValueInPixels());
        da = angle;
        angle = Math.toRadians(info.get(2).getValueInPixels());

        basicParameters.set(1, angle);
        children.get(0).updated.set(0, true);
        children.get(1).updated.set(0, true);

        updateChildren();
        updateParams();
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("Line:"));
            add(new Measurement("   length:", basicParameters.get(0), "mm"));
            add(new Measurement("   angle:", Math.toDegrees(basicParameters.get(1)), "deg"));
        }};
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {

        if (children.size() == 2) {
            Point2D.Double globalStart = plane.toGlobal(children.get(0).start);
            Point2D.Double globalEnd = plane.toGlobal(children.get(1).start);

            g.setStroke(stroke);
            g.setColor(color);

            g.drawLine((int) globalStart.x,
                    (int) globalStart.y,
                    (int) globalEnd.x,
                    (int) globalEnd.y);
        }
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);
            double distance = Line2D.ptSegDist(children.get(0).start.x, children.get(0).start.y,
                    children.get(1).start.x, children.get(1).start.y,
                    localPoint.x, localPoint.y);
            return distance < 3 / plane.getScale();
        }
        return false;
    }

    public void writeToDXF(BufferedWriter writer) throws IOException {
        writer.write("10\n" + (children.get(0).start.x / 10) + "\n");
        writer.write("20\n" + (-children.get(0).start.y / 10) + "\n");
        writer.write("11\n" + (children.get(1).start.x / 10) + "\n");
        writer.write("21\n" + (-children.get(1).start.y / 10) + "\n");
    }
}
