package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Circle extends Figure {
    protected double radius;

    public Circle() {
        figureType = FigureType.CIRCLE;

        maxChildren = 2;

        basicParameters.add(radius);

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    /*public Circle(Point2D.Double center, double radius, CoordinatePlane cp) {
        figureType = FigureType.CIRCLE;

        maxChildren = 2;

        basicParameters.add(radius);
        setNextPoint(center, cp);
        setNextPoint(new Point2D.Double(center.x + radius, center.y), cp);

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }*/

    @Override
    public void updateBasicParameter(int index) {
        if (index == 0) {
            basicParameters.set(0, children.get(0).start.distance(children.get(1).start));
            updated.set(0, true);
        }
    }

    @Override
    public void updateChildren() {
        for (int i = 1; i < children.size(); i++) {
            children.get(i).start.x = basicParameters.get(0) + children.get(0).start.x;
            children.get(i).start.y = children.get(0).start.y;
        }
    }

    @Override
    public void updateValuesFromInput() {
        //radius = info.get(1).getValueInPixels();
        basicParameters.set(0, info.get(1).getValueInPixels());

        updateChildren();
        updateParams();
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("Circle:"));
            add(new Measurement("   radius:", basicParameters.get(0), "mm"));
        }};
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double globalCenter = plane.toGlobal(children.get(0).start);
            double globalRadius = children.get(0).start.distance(children.get(1).start) * plane.getScale();

            g.drawOval((int) globalCenter.x - (int) globalRadius,
                    (int) globalCenter.y - (int) globalRadius,
                    (int) globalRadius * 2,
                    (int) globalRadius * 2);
        }
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);

            return children.get(0).start.distance(localPoint) - 3 / plane.getScale() <= basicParameters.get(0)
                    && children.get(0).start.distance(localPoint) + 3 / plane.getScale() >= basicParameters.get(0);
            //||  childrenContain(p, plane);
        }
        return false;
    }

    @Override
    public void writeToDXF(BufferedWriter writer) throws IOException {
        writer.write("10\n" + (children.get(0).start.x / 10) + "\n");
        writer.write("20\n" + (-children.get(0).start.y / 10) + "\n");
        writer.write("30\n0.0\n");
        writer.write("40\n" + (getBasicParameter(0) / 10) + "\n");
    }
}
