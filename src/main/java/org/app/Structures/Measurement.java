package org.app.Structures;

public class Measurement {
    private final String name;
    private double value;
    private final String unit;
    private final boolean editable;
    private boolean updated = false;

    public Measurement(String name, double value, String unit) {
        this.name = name;
        this.unit = unit;
        this.value = value; // in pixels
        this.editable = true;
    }

    public Measurement(String name) {
        this.name = name;
        this.value = 0.0f;
        this.unit = "";
        this.editable = false;
    }

    // returns a value in measurement units (value = 100.0; toUnits() = 1 mm)
    public double getValueInUnits() {
        if (unit.equals("mm")) {
            return this.value / 100;
        }
        return this.value;
    }

    public void setValueInUnits(double value) {
        updated = true;
        if (unit.equals("mm")) {
            this.value = value * 100;
        } else {
            this.value = value;
        }
    }

    public double getValueInPixels() {
        updated = false;
        return value;
    }

    public boolean isEditable() {
        return editable;
    }

    public String getName() {
        return name;
    }

    public boolean isUpdated() {
        return updated;
    }
}
