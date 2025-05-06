package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class PolygonIncribed extends Figure {
    private final double radius = 0;
    private Point2D.Double center;
    private double sides = 3;

    public PolygonIncribed() {
        this.sides = Math.max(sides, 3); // Минимум 3 стороны
        figureType = FigureType.POLYGON_INC;

        maxChildren = 2;

        basicParameters.add(this.radius);
        basicParameters.add(this.sides);

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    @Override
    public void updateBasicParameter(int index) {
        if (index == 0) {
            basicParameters.set(0, children.get(0).start.distance(children.get(1).start));
            updated.set(0, true);
        }
    }

    @Override
    public void updateChildren() {
        double angleStep = 2 * Math.PI / sides;
        double startAngle = Math.atan2(children.get(1).start.x - children.get(0).start.x, children.get(1).start.y - children.get(0).start.y);
        for (int i = 0; i < children.size() - 1; i++) {
            double theta = startAngle + i * angleStep;
            children.get(i + 1).start.x = children.get(0).start.x + basicParameters.get(0) * Math.cos(theta);
            children.get(i + 1).start.y = children.get(0).start.y - basicParameters.get(0) * Math.sin(theta);
        }
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        children.add(new DrawnPoint(point, plane, true, this));

        if (children.size() == 2) {
            basicParameters.set(0, children.get(0).start.distance(children.get(1).start));
        }

    }

    @Override
    public void updateValuesFromInput() {
        basicParameters.set(0, info.get(1).getValueInPixels());
        basicParameters.set(1, info.get(2).getValueInPixels());
        sides = basicParameters.get(1);

        for (Figure child : children) {
            child.updated.set(0, true);
        }

        updateChildren();
        updateParams();
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("Polygon:"));
            add(new Measurement("   radius:", basicParameters.get(0), "mm"));
            add(new Measurement("   sides:", basicParameters.get(1), " "));
        }};
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (isDrown()) {
            Point2D.Double globalCenter = plane.toGlobal(children.get(0).start);
            double globalRadius = basicParameters.get(0) * plane.getScale();
            int[] xPoints = new int[(int) sides];
            int[] yPoints = new int[(int) sides];

            double angleStep = 2 * Math.PI / sides;
            double startAngle = Math.atan2(children.get(1).start.x - children.get(0).start.x, children.get(1).start.y - children.get(0).start.y);
            for (int i = 0; i < sides; i++) {
                double theta = startAngle + i * angleStep;
                xPoints[i] = (int) (globalCenter.x + globalRadius * Math.cos(theta));
                yPoints[i] = (int) (globalCenter.y - globalRadius * Math.sin(theta));
            }

            g.drawPolygon(xPoints, yPoints, (int) sides);
        }
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);
            return children.get(0).start.distance(localPoint) <= basicParameters.get(0);
        }
        return false;
    }

    @Override
    public void writeToDXF(BufferedWriter writer) throws IOException {
        java.util.List<Point2D.Double> vertices = calculateVertices(basicParameters.get(0));

        writer.write("66\n1\n");

        for (Point2D.Double vertex : vertices) {
            writer.write("0\nVERTEX\n");
            writer.write("8\n0\n");
            writer.write("10\n" + vertex.x + "\n");
            writer.write("20\n" + -vertex.y + "\n");
            writer.write("30\n0.0\n");
        }

        writer.write("0\nSEQEND\n");
    }

    private java.util.List<Point2D.Double> calculateVertices(double radius) {
        java.util.List<Point2D.Double> vertices = new ArrayList<>();
        double angleStep = 2 * Math.PI / sides;

        double rotation = Math.atan2(children.get(1).start.x - children.get(0).start.x, children.get(1).start.y - children.get(0).start.y);

        for (int i = 0; i < sides; i++) {
            double angle = rotation + i * angleStep;
            double descRad = radius / 10;
            double x = children.get(0).start.x / 10 + descRad * Math.cos(angle);
            double y = children.get(0).start.y / 10 - descRad * Math.sin(angle);
            vertices.add(new Point2D.Double(x, y));
        }

        vertices.add(vertices.get(0));
        return vertices;
    }
}