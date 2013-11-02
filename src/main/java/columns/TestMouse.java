package columns;

import java.awt.*;
import java.awt.event.AWTEventListener;

public class TestMouse implements AWTEventListener {
    public void eventDispatched(AWTEvent event) {
        System.out.println(event);
    }

    public static void main(String[] args) {
        Toolkit.getDefaultToolkit().addAWTEventListener(new TestMouse(), 0xffffffffffffffffl);
        Window window = new Window(null);
        window.setBounds(0, 0, 100, 100);
        window.setVisible(true);
    }
}