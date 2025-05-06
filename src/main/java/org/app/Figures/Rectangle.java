package org.app.Figures;

import org.app.Constrains.Constrain;
import org.app.Constrains.Perpendicular;

import java.awt.geom.Point2D;

public class Rectangle extends Figure {
    protected double width;
    protected double height;

    public Rectangle() {
        figureType = FigureType.RECTANGLE;
        maxChildren = 8;
        updated.add(false);
    }

    @Override
    public void setNextPoint(Point2D.Double point, CoordinatePlane plane) {
        if (children.size() == 0) {
            children.add(new DrawnPoint(point, plane, this)); // 0
        } else {
            children.add(new DrawnPoint(point, plane, this)); // 1

            children.add(new DrawnPoint(                             // 2
                    new Point2D.Double(point.x, plane.toGlobal(children.get(0).start).y),
                    plane, this));

            children.add(new DrawnPoint(                             // 3
                    new Point2D.Double(plane.toGlobal(children.get(0).start).x, point.y),
                    plane, this));

            width = children.get(0).start.x - children.get(1).start.x;
            height = children.get(0).start.y - children.get(1).start.y;

            children.add(new Line(children.get(0), children.get(2), this)); // 4
            children.add(new Line(children.get(2), children.get(1), this)); // 5
            children.add(new Line(children.get(1), children.get(3), this)); // 6
            children.add(new Line(children.get(3), children.get(0), this)); // 7

            Constrain perp = new Perpendicular(children.get(4), children.get(5), children.get(2));
            children.get(4).addConstrain(perp);
            children.get(5).addConstrain(perp);

            Constrain perp1 = new Perpendicular(children.get(5), children.get(6), children.get(1));
            children.get(5).addConstrain(perp1);
            children.get(6).addConstrain(perp1);

            Constrain perp2 = new Perpendicular(children.get(6), children.get(7), children.get(3));
            children.get(6).addConstrain(perp2);
            children.get(7).addConstrain(perp2);

            Constrain perp3 = new Perpendicular(children.get(7), children.get(4), children.get(0));
            children.get(7).addConstrain(perp3);
            children.get(4).addConstrain(perp3);

            checkConstrains();
            //updateParams();

            if (width < 0) width *= -1;
            if (height < 0) height *= -1;
        }
    }
}
