package NAMANPROJECTS;


import javax.swing.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;


public class HotelBookingSystem {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HotelBookingFrame frame = new HotelBookingFrame();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);
        });
    }
}


