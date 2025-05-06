package org.app.Constrains;

import org.app.Figures.Figure;

import java.util.List;

public interface Constrain {
    List<Figure> members = null;

    void addMember(Figure figure);

    void checkConstrain(Figure figure);

    void checkNeighbour(Figure figure);
}
