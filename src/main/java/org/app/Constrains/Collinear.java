package org.app.Constrains;

import org.app.Figures.Figure;

import java.util.ArrayList;
import java.util.List;

public class Collinear implements Constrain {
    private final List<Figure> members;
    private final Figure mainPoint;

    public Collinear(Figure strong, Figure weak) {
        this.members = new ArrayList<>();
        this.mainPoint = strong;
        this.members.add(mainPoint);

        addMember(weak);

        System.out.println(members);
    }

    @Override
    public void addMember(Figure weak) {
        members.add(weak);
        weak.getParentFigure().get(0).setFigureChildren(weak, mainPoint);
    }

    @Override
    public void checkConstrain(Figure figure) {

    }

    @Override
    public void checkNeighbour(Figure figure) {

    }
}
