package NAMANPROJECTS;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HotelBookingFrame extends JFrame {
    private JTextField guestNameField;
    private JTextField roomNumberField;
    private JTextArea outputArea;
    private Hotel hotel;

    public HotelBookingFrame() {
        hotel = new Hotel();
        setTitle("Hotel Booking System");
        setSize(400, 300);
        setLayout(new BorderLayout());

        // Input Panel
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(3, 2));

        inputPanel.add(new JLabel("Guest Name:"));
        guestNameField = new JTextField();
        inputPanel.add(guestNameField);

        inputPanel.add(new JLabel("Room Number:"));
        roomNumberField = new JTextField();
        inputPanel.add(roomNumberField);

        JButton bookButton = new JButton("Book Room");
        bookButton.addActionListener(new BookRoomAction());
        inputPanel.add(bookButton);

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.addActionListener(new CancelBookingAction());
        inputPanel.add(cancelButton);

        add(inputPanel, BorderLayout.NORTH);

        // Output Area
        outputArea = new JTextArea();
        outputArea.setEditable(false);
        add(new JScrollPane(outputArea), BorderLayout.CENTER);
    }

    private class BookRoomAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String guestName = guestNameField.getText();
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (hotel.bookRoom(guestName, roomNumber)) {
                outputArea.append("Room " + roomNumber + " booked for " + guestName + "\n");
            } else {
                outputArea.append("Room " + roomNumber + " is not available.\n");
            }
        }
    }

    private class CancelBookingAction implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            int roomNumber = Integer.parseInt(roomNumberField.getText());
            if (hotel.cancelBooking(roomNumber)) {
                outputArea.append("Booking for room " + roomNumber + " has been canceled.\n");
            } else {
                outputArea.append("No booking found for room " + roomNumber + ".\n");
            }
        }
    }
}
