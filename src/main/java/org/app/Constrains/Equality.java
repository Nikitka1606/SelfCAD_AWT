package org.app.Constrains;

import org.app.Figures.Figure;
import org.app.Figures.FigureType;

import java.util.ArrayList;
import java.util.List;

public class Equality implements Constrain {
    private final List<Figure> members;

    public Equality(Figure l1, Figure l2) {
        if (l1.getFigureType() != FigureType.LINE || l2.getFigureType() != FigureType.LINE) {/*exception*/}

        members = new ArrayList<>();
        members.add(l1);
        members.add(l2);
        ensureEquality(l1);
    }

    private void ensureEquality(Figure figure) {
        if (figure == members.get(0)) {
            members.get(1).setBasicParameter(0, members.get(0).getBasicParameter(0));
        } else if (figure == members.get(1)) {
            members.get(0).setBasicParameter(0, members.get(1).getBasicParameter(0));
        }
        //double length = members.get(0).getBasicParameter(1) - members.get(1).getBasicParameter(1);


    }

    @Override
    public void addMember(Figure figure) {

    }

    @Override
    public void checkConstrain(Figure figure) {
        ensureEquality(figure);
    }

    @Override
    public void checkNeighbour(Figure figure) {

    }
}
