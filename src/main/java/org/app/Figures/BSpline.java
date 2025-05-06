package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class BSpline extends Figure {

    public BSpline() {
        figureType = FigureType.B_SPLINE;

        maxChildren = 2;

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        children.add(new ControlTangent(plane.toLocal(point).x, plane.toLocal(point).y, this, plane));
        maxChildren++;
        if (isDrown()) {
            updateParams();
            getInfo();
        }
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (children.size() > 1) {
            for (int i = 0; i < children.size() - 1; i++) {
                if (children.get(i) instanceof ControlTangent ct && children.get(i + 1) instanceof ControlTangent ct1) {
                    Point2D prevPoint = plane.toGlobal(ct.children.get(0).start);
                    for (double t = 0; t <= 1; t += 0.001) {
                        Point2D point = plane.toGlobal(calculateBezierPoint(t, ct, ct1));
                        Line2D.Double seg = new Line2D.Double(prevPoint.getX(), prevPoint.getY(), point.getX(), point.getY());
                        g.draw(seg);
                        prevPoint = point;
                    }
                }
            }
        }
    }

    public void setMaxChildren() {
        this.maxChildren = children.size();
    }

    private Point2D.Double calculateBezierPoint(double t, ControlTangent p0, ControlTangent p1) {
        double x = Math.pow(1 - t, 3) * p0.children.get(0).start.getX()
                + 3 * Math.pow(1 - t, 2) * t * p0.children.get(2).start.getX()
                + 3 * (1 - t) * Math.pow(t, 2) * p1.children.get(1).start.getX()
                + Math.pow(t, 3) * p1.children.get(0).start.getX();

        double y = Math.pow(1 - t, 3) * p0.children.get(0).start.getY()
                + 3 * Math.pow(1 - t, 2) * t * p0.children.get(2).start.getY()
                + 3 * (1 - t) * Math.pow(t, 2) * p1.children.get(1).start.getY()
                + Math.pow(t, 3) * p1.children.get(0).start.getY();

        return new Point2D.Double(x, y);
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("B-spline"));
        }};
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);

            for (int i = 0; i < children.size() - 1; i++) {
                if (children.get(i) instanceof ControlTangent ct && children.get(i + 1) instanceof ControlTangent ct1) {
                    for (double t = 0; t <= 1; t += 0.01) {
                        Point2D curvePoint = calculateBezierPoint(t, ct, ct1);
                        if (localPoint.distance(curvePoint) < 3 / plane.getScale()) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public void writeToDXF(BufferedWriter writer) throws IOException {
        int flags = 8;
        int degree = 3;
        writer.write("70\n" + (flags | 4) + "\n");
        writer.write("71\n" + degree + "\n");
        writer.write("72\n" + children.size() + "\n");
        writer.write("73\n0\n");

        for (Figure ct : children) {
            Point2D.Double center = ct.getChildren().get(0).start;
            writer.write("10\n" + (center.x / 10) + "\n");
            writer.write("20\n" + (-center.y / 10) + "\n");
            writer.write("30\n0.0\n");
            writer.write("40\n1.0\n");
        }

        for (int i = 0; i < children.size() + degree + 1; i++) {
            writer.write("40\n" + i + "\n");
        }

        writer.write("0\nXDATA\n");
        writer.write("100\nAcDbSpline\n");
        for (Figure ct : children) {
            Point2D.Double left = ct.getChildren().get(1).start;
            Point2D.Double right = ct.getChildren().get(2).start;

            writer.write("1010\n" + (left.x / 10) + "\n");
            writer.write("1020\n" + (-left.y / 10) + "\n");
            writer.write("1010\n" + (right.x / 10) + "\n");
            writer.write("1020\n" + (-right.y / 10) + "\n");
        }
    }
}
