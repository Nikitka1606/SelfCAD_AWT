package org.app.Figures;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class ControlTangent extends Figure {

    ControlTangent(double x, double y, Figure parentFigure, CoordinatePlane plane) {
        this.figureType = FigureType.CONTROL_TANGENT;
        this.parentFigure.add(parentFigure);
        children.add(new DrawnPoint(new Point2D.Double(x, y), this));
        children.add(new DrawnPoint(new Point2D.Double(x - 50 / plane.getScale(), y), this));
        children.add(new DrawnPoint(new Point2D.Double(x + 50 / plane.getScale(), y), this));

        preferredColor = Color.GRAY;
        selectionColor = Color.LIGHT_GRAY;
        highlighColor = Color.lightGray;
        setPreferredColor();
    }

    @Override
    public void drawMethod(Graphics2D g, CoordinatePlane plane) {
        if (isDrawable) {
            g.setColor(Color.GRAY);
            Point2D.Double p1 = plane.toGlobal(children.get(0).start);
            Point2D.Double p2 = plane.toGlobal(children.get(1).start);
            Point2D.Double p3 = plane.toGlobal(children.get(2).start);
            Line2D.Double line1 = new Line2D.Double(p1.x, p1.y, p2.x, p2.y);
            Line2D.Double line2 = new Line2D.Double(p1.x, p1.y, p3.x, p3.y);
            g.draw(line1);
            g.draw(line2);
        }
    }
}
