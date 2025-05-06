package org.app.Constrains;

import org.app.Figures.Figure;
import org.app.Figures.FigureType;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Perpendicular implements Constrain {
    private final List<Figure> members;
    private final Figure pivot;
    private int checked;
    private Set<Figure> visited;

    public Perpendicular(Figure l1, Figure l2, Figure pivot) {
        if (l1.getFigureType() != FigureType.LINE || l2.getFigureType() != FigureType.LINE) {/*exception*/}

        members = new ArrayList<>();
        this.pivot = pivot;
        members.add(l1);
        members.add(l2);
        //checkConstrain(l1);
        //checkConstrain(l2);
    }

    @Override
    public void addMember(Figure l) {
        members.add(l);
        checked++;
    }

    @Override
    public void checkConstrain(Figure figure) {
        pivot.fix();
        if (figure == members.get(0)) {
            members.get(1).setBasicParameter(1, members.get(0).getBasicParameter(1) - Math.PI / 2);
            //members.get(1).updateChildren();
        } else if (figure == members.get(1)) {
            members.get(0).setBasicParameter(1, members.get(1).getBasicParameter(1) - Math.PI / 2);
            //members.get(0).updateChildren();
        }
        pivot.unfix();
    }

    @Override
    public void checkNeighbour(Figure figure) {
        /*if (figure == members.get(0)) {
            members.get(1).checkConstrains();
            members.get(1).updateParams();
        }
        else {
            members.get(0).checkConstrains();
            members.get(0).updateParams();
        }*/
    }
}
