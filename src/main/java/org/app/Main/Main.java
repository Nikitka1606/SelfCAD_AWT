package org.app.Main;

import org.app.Interface.Window;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(Window::new);
    }
}
