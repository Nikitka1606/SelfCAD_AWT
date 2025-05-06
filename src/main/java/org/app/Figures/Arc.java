package org.app.Figures;

import org.app.Structures.Measurement;

import java.awt.*;
import java.awt.geom.Arc2D;
import java.awt.geom.Point2D;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Arc extends Figure {
    private double radius = 0;
    private Point2D.Double center;
    private double startAngle;
    private double endAngle;

    public Arc() {
        figureType = FigureType.ARC;

        maxChildren = 3;

        basicParameters.add(this.radius);
        basicParameters.add(this.startAngle);
        basicParameters.add(this.endAngle);

        setUpdates();

        preferredColor = Color.RED;
        selectionColor = Color.WHITE;
        highlighColor = Color.BLUE;
        setPreferredColor();
    }

    @Override
    public void updateBasicParameter(int index) {
        Point2D.Double p1 = children.get(0).start;
        Point2D.Double p2 = children.get(1).start;
        Point2D.Double p3 = children.get(2).start;

        center = calculateCircleCenter(p1, p2, p3);
        this.radius = center.distance(p1);

        double angle1 = calculateAngle(center, p1);
        double angle2 = calculateAngle(center, p2);

        double startAngle = Math.toDegrees(angle1);
        double endAngle = Math.toDegrees(angle2);

        double ang1 = calculateAngle(center, p1, p2); // больший угол
        double ang2 = calculateAngle(center, p1, p3);
        double ang3 = calculateAngle(center, p2, p3);

            /*System.out.println(Math.toDegrees(ang1));
            System.out.println(Math.toDegrees(ang2));
            System.out.println(Math.toDegrees(ang3));*/

        double sweepAngle;
        double eps = 0.000000000001;

        if (Math.abs(ang1 - (ang2 + ang3)) < eps) {
            //System.out.println("меньшая");
            if (normalizeSweepAngle(startAngle, endAngle) < normalizeSweepAngle(endAngle, startAngle)) {
                sweepAngle = normalizeSweepAngle(startAngle, endAngle);
            } else {
                sweepAngle = normalizeSweepAngle(endAngle, startAngle);
                startAngle = endAngle;
            }
        } else {
            //System.out.println("большая");
            if (normalizeSweepAngle(startAngle, endAngle) > normalizeSweepAngle(endAngle, startAngle)) {
                sweepAngle = normalizeSweepAngle(startAngle, endAngle);
            } else {
                sweepAngle = normalizeSweepAngle(endAngle, startAngle);
                startAngle = endAngle;
            }
        }

        switch (index) {
            case 0 -> basicParameters.set(0, radius);
            case 1 -> basicParameters.set(1, startAngle);
            case 2 -> basicParameters.set(2, sweepAngle);
        }
    }

    public void setCenter(Point2D.Double point2D) {
        this.center = point2D;
    }

    @Override
    public void updateChildren() {
        double k = basicParameters.get(0) / radius;

        for (Figure child : children) {
            child.start.x = center.x + (child.start.x - center.x) * k;
            child.start.y = center.y + (child.start.y - center.y) * k;
        }
    }

    @Override
    public void updateValuesFromInput() {
        basicParameters.set(0, info.get(1).getValueInPixels());
        //basicParameters.set(1,  info.get(2).getValueInPixels());
        //basicParameters.set(2,  info.get(3).getValueInPixels());

        children.get(0).updated.set(0, true);
        children.get(1).updated.set(0, true);
        children.get(2).updated.set(0, true);

        updateChildren();
        updateParams();
    }

    @Override
    public void gatherInfo() {
        info = new ArrayList<>() {{
            add(new Measurement("Arc:"));
            add(new Measurement("   radius:", basicParameters.get(0), "mm"));
            add(new Measurement("   start angle:",  basicParameters.get(1), "deg"));
                add(new Measurement("   sweep angle:",  basicParameters.get(2), "deg"));
        }};
    }

    private double calculateAngle(Point2D center, Point2D point) {
        return Math.atan2(point.getY() - center.getY(), point.getX() - center.getX());
    }

/*
    private double normalizeAngle(double angle) {
        double na = angle;
        if (na > 360) {
            while (na > 360)
                na -= 360;
            na -= 360;
        } else if (na < 0) {
            while (na < 0)
                na += 360;
        }
        return na;
    }
*/

    public double calculateAngle(Point2D.Double O, Point2D.Double P1, Point2D.Double P2) {
        double[] v1 = {P1.getX() - O.getX(), P1.getY() - O.getY()};
        double[] v2 = {P2.getX() - O.getX(), P2.getY() - O.getY()};
        double dot = v1[0] * v2[0] + v1[1] * v2[1];
        double mag1 = Math.sqrt(v1[0] * v1[0] + v1[1] * v1[1]);
        double mag2 = Math.sqrt(v2[0] * v2[0] + v2[1] * v2[1]);
        return Math.acos(dot / (mag1 * mag2));
    }

    private Point2D.Double calculateCircleCenter(Point2D.Double p1, Point2D.Double p2, Point2D.Double p3) {
        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();
        double x3 = p3.getX(), y3 = p3.getY();

        double d = 2 * (x1 * (y2 - y3) + x2 * (y3 - y1) + x3 * (y1 - y2));

        double ux = ((x1 * x1 + y1 * y1) * (y2 - y3) +
                (x2 * x2 + y2 * y2) * (y3 - y1) +
                (x3 * x3 + y3 * y3) * (y1 - y2)) / d;

        double uy = ((x1 * x1 + y1 * y1) * (x3 - x2) +
                (x2 * x2 + y2 * y2) * (x1 - x3) +
                (x3 * x3 + y3 * y3) * (x2 - x1)) / d;

        return new Point2D.Double(ux, uy);
    }

    public Point2D.Double computeCircle(double radius) {
        Point2D.Double pointA = children.get(0).start;
        Point2D.Double pointB = children.get(1).start;

        basicParameters.set(0, radius);
        double mx = (pointA.x + pointB.x) / 2;
        double my = (pointA.y + pointB.y) / 2;

        double chordLength = Math.sqrt(
                Math.pow(pointB.x - pointA.x, 2) + Math.pow(pointB.y - pointA.y, 2));
        double halfChordLength = chordLength / 2;

        if (radius <= halfChordLength) {
            throw new IllegalArgumentException("Radius is too small to form a circle with given points.");
        }

        double centerDistance = Math.sqrt(radius * radius - halfChordLength * halfChordLength);

        double vx = -(pointB.y - pointA.y);
        double vy = pointB.x - pointA.x;
        double length = Math.sqrt(vx * vx + vy * vy);
        vx /= length;
        vy /= length;

        Point2D.Double center1 = new Point2D.Double(mx + centerDistance * vx, my + centerDistance * vy);
        Point2D.Double center2 = new Point2D.Double(mx - centerDistance * vx, my - centerDistance * vy);

        this.center = center1;
        Point2D.Double chosenCenter = center1;

        double angle = Math.atan2(pointB.y - chosenCenter.y, pointB.x - chosenCenter.x);
        double perpendicularAngle = angle + Math.PI / 2; // 90° поворот
        Point2D.Double extraPoint = new Point2D.Double(
                chosenCenter.x + radius * Math.cos(perpendicularAngle),
                chosenCenter.y + radius * Math.sin(perpendicularAngle)
        );

        return extraPoint;
    }

    private double normalizeSweepAngle(double startAngle, double endAngle) {
        if (startAngle < 0) startAngle += 360;
        if (endAngle < 0) endAngle += 360;

        double sweepAngle = endAngle - startAngle;
        if (sweepAngle < 0) {
            sweepAngle += 360;
        }
        return sweepAngle;
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            if (center != null && basicParameters.get(0) > 0) {
                Point2D.Double globalCenter = plane.toGlobal(center);
                double globalRadius = basicParameters.get(0) * plane.getScale();

                int x = (int) (globalCenter.getX() - globalRadius);
                int y = (int) (globalCenter.getY() - globalRadius);
                int diameter = (int) (2 * globalRadius);

                Arc2D.Double arc = new Arc2D.Double(x, y, diameter, diameter, -basicParameters.get(1), -basicParameters.get(2), Arc2D.OPEN);
                g.draw(arc);
            }
        }
    }

    @Override
    public boolean contains(Point2D.Double p, CoordinatePlane plane) {
        if (children.size() == maxChildren) {
            Point2D.Double localPoint = plane.toLocal(p);

            return center.distance(localPoint) - 3 / plane.getScale() <= basicParameters.get(0)
                    && center.distance(localPoint) + 3 / plane.getScale() >= basicParameters.get(0)
                       /* && calculateAngle(center, plane.toLocal(localPoint)) > Math.toRadians(basicParameters.get(1))
                        && calculateAngle(center, plane.toLocal(localPoint)) < Math.toRadians(basicParameters.get(2))*/;

        }
        return false;
    }

    @Override
    public void writeToDXF(BufferedWriter writer) throws IOException {
        writer.write("10\n" + (center.x / 10) + "\n");
        writer.write("20\n" + (-center.y / 10) + "\n");
        writer.write("30\n0.0\n");

        writer.write("40\n" + (basicParameters.get(0) / 10) + "\n"); // Радиус

        // Углы
        double startAngle = basicParameters.get(1);
        double sweepAngle = basicParameters.get(2);
        double endAngle = -startAngle - sweepAngle + 90;

        startAngle = endAngle - 90;
        endAngle = -basicParameters.get(1);

        startAngle = (startAngle % 360 + 360) % 360;
        endAngle = (endAngle % 360 + 360) % 360;

        writer.write("50\n" + startAngle + "\n");
        writer.write("51\n" + endAngle + "\n");
    }
}
