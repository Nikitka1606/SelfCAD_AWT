package org.app.Interface;

import javax.swing.*;
import java.awt.*;

public class Icon extends JButton {
    private boolean pressed = false;

    public Icon(String text) {
        this.setSize(90, 90);
        this.setBackground(Color.WHITE);
        this.setText(text);
    }

    private void switchButton() {
        pressed = !pressed;
    }

    public boolean isPressed() {
        return pressed;
    }
}
