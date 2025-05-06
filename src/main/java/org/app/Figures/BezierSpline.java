package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BezierSpline extends Figure {

    public BezierSpline() {
        figureType = FigureType.BEZIER_SPLINE;

        maxChildren = 2;

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        children.add(new DrawnPoint(point, plane, this));
        maxChildren++;
        if (isDrown()) {
            updateParams();
            getInfo();
        }
    }

    public Point2D.Double setNextPoint(Figure magnetPoint, CoordinatePlane plane) {
        setNextPoint(plane.toGlobal(magnetPoint.start), plane);
        this.start = magnetPoint.start;

        return null;
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        List<Point2D.Double> globalChildren = childrenToGlobal(plane);

        g.setColor(Color.GRAY);
        for (int i = 0; i < globalChildren.size() - 1; i++) {
            Point2D p1 = globalChildren.get(i);
            Point2D p2 = globalChildren.get(i + 1);
            Line2D.Double line = new Line2D.Double(p1.getX(), p1.getY(), p2.getX(), p2.getY());
            g.draw(line);
        }
        g.setColor(preferredColor);

        if (globalChildren.size() > 1) {
            Point2D prevPoint = globalChildren.get(0);

            for (double t = 0; t <= 1; t += 0.01) {
                Point2D point = calculateBezierPoint(t, plane);
                Line2D.Double line = new Line2D.Double(prevPoint.getX(), prevPoint.getY(), point.getX(), point.getY());
                g.draw(line);
                prevPoint = point;
            }
        }
    }

    public void setMaxChildren() {
        this.maxChildren = children.size();
    }

    private Point2D calculateBezierPoint(double t, CoordinatePlane plane) {
        List<Point2D.Double> globalChildren = childrenToGlobal(plane);
        int n = globalChildren.size() - 1;
        double x = 0, y = 0;

        for (int i = 0; i <= n; i++) {
            double coefficient = binomialCoefficient(n, i) * Math.pow(1 - t, n - i) * Math.pow(t, i);
            x += coefficient * globalChildren.get(i).x;
            y += coefficient * globalChildren.get(i).y;
        }

        return new Point2D.Double(x, y);
    }

    private long binomialCoefficient(int n, int k) {
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result = result * (n - (i - 1)) / i;
        }
        return result;
    }

    private List<Point2D.Double> childrenToGlobal(CoordinatePlane plane) {
        List<Point2D.Double> globalChildren = new ArrayList<>();
        for (Figure p : children)
            globalChildren.add(plane.toGlobal(p.start));

        return globalChildren;
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("Bezier spline"));
        }};
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);
            for (double t = 0; t <= 1; t += 0.001) {
                Point2D curvePoint = calculateBezierPoint(t, children);
                if (localPoint.distance(curvePoint) < 3 / plane.getScale()) {
                    return true;
                }
            }
        }
        return false;
    }

    private Point2D calculateBezierPoint(double t, List<Figure> controlPoints) {
        int n = controlPoints.size() - 1;
        double x = 0, y = 0;

        for (int i = 0; i <= n; i++) {
            double bernstein = binomialCoefficient(n, i) * Math.pow(t, i) * Math.pow(1 - t, n - i);
            x += bernstein * controlPoints.get(i).getX();
            y += bernstein * controlPoints.get(i).getY();
        }

        return new Point2D.Double(x, y);
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
            Point2D.Double center = ct.start;
            writer.write("10\n" + (center.x / 10) + "\n");
            writer.write("20\n" + (-center.y / 10) + "\n");
            writer.write("30\n0.0\n");
            writer.write("40\n1.0\n"); // Вес точки
        }

        for (int i = 0; i < children.size() + degree + 1; i++) {
            writer.write("40\n" + i + "\n");
        }

        writer.write("0\nXDATA\n");
        writer.write("100\nAcDbSpline\n");
    }
}
