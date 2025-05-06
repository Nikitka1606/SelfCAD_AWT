package org.app.Structures;

public class FigureInOut {
    public String first;
    public double second;
    private final String units;
    private boolean isChanged;
    private final int type; // 0 - just a line; 1 - with input; 2 - coordinate input

    public FigureInOut(String first) { // just a label
        this.first = first;
        this.second = 0;
        this.type = 0;
        this.units = "";
    }

    public FigureInOut(double second, boolean isChanged) { // just a label
        this.first = "";
        this.second = second;
        this.type = 0;
        this.units = "";
        this.isChanged = isChanged;
    }

    public FigureInOut(String first, double second, boolean isChanged) { // if changed in input
        this.first = first;
        this.second = second;
        this.type = 1;
        this.units = "mm";
        this.isChanged = isChanged;
    }

    public FigureInOut(String first, double second, String units, boolean isChanged) { // if changed in input of a non-regular value
        this.first = first;
        this.second = second;
        this.type = 2;
        this.units = units;
        this.isChanged = isChanged;
    }

    public FigureInOut(String first, double second) { // if not changed input
        this.first = first;
        this.second = second;
        this.type = 1;
        this.units = "mm";
        this.isChanged = false;
    }

    public FigureInOut(String first, double second, String units) { // if not changed in input of a non-regular value
        this.first = first;
        this.second = second;
        this.type = 2;
        this.units = units;
        this.isChanged = false;
    }

    public int getType() {
        return type;
    }
}
