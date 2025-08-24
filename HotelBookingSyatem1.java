package NAMANPROJECTS;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

 class HotelBookingSystem1 extends JFrame {
    private List<Room> singleRooms;
    private List<Room> doubleRooms;
    private DefaultTableModel tableModel;
    private JTable roomTable;
    private JTextField guestNameField;
    private JComboBox<String> roomTypeCombo;
    private JComboBox<String> roomNumberCombo;
    private JLabel availableSingleLabel;
    private JLabel availableDoubleLabel;
    private JLabel totalRevenueLabel;

    public HotelBookingSystem1
            () {
        initializeRooms();
        setupUI();
        updateRoomAvailability();
        updateRevenue();
        updateRoomNumberCombo();
    }

    private void initializeRooms() {
        singleRooms = new ArrayList<>();
        doubleRooms = new ArrayList<>();

        for (int i = 1; i <= 25; i++) {
            singleRooms.add(new Room("S" + i, "single", 1300));
            doubleRooms.add(new Room("D" + i, "double", 2500));
        }
    }

    private void setupUI() {
        setTitle("Hotel Booking System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 750);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Header Panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(44, 62, 80));
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));
        headerPanel.setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel("🏨 Hotel Booking System", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        headerPanel.add(titleLabel, BorderLayout.CENTER);

        // Stats Panel
        JPanel statsPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        statsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        statsPanel.setBackground(new Color(236, 240, 241));

        availableSingleLabel = createStatLabel("Available Single Rooms: 25", new Color(52, 152, 219));
        availableDoubleLabel = createStatLabel("Available Double Rooms: 25", new Color(46, 204, 113));
        totalRevenueLabel = createStatLabel("Total Revenue: ₹0", new Color(231, 76, 60));

        statsPanel.add(availableSingleLabel);
        statsPanel.add(availableDoubleLabel);
        statsPanel.add(totalRevenueLabel);

        // Booking Form Panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(new CompoundBorder(
                new TitledBorder("Book a Room"),
                new EmptyBorder(10, 10, 10, 10)
        ));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Guest Name
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Guest Name:"), gbc);
        gbc.gridx = 1;
        guestNameField = new JTextField(20);
        formPanel.add(guestNameField, gbc);

        // Room Type
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Room Type:"), gbc);
        gbc.gridx = 1;
        roomTypeCombo = new JComboBox<>(new String[]{"single", "double"});
        roomTypeCombo.addActionListener(e -> updateRoomNumberCombo());
        formPanel.add(roomTypeCombo, gbc);

        // Room Number Selection
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomNumberCombo = new JComboBox<>();
        formPanel.add(roomNumberCombo, gbc);

        // Buttons
        gbc.gridx = 0; gbc.gridy = 3; gbc.gridwidth = 2;
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));

        JButton bookButton = new JButton("Book Selected Room");
        bookButton.setBackground(new Color(46, 204, 113));
        bookButton.setForeground(Color.WHITE);
        bookButton.setFont(new Font("Arial", Font.BOLD, 14));
        bookButton.addActionListener(e -> bookRoom());

        JButton cancelButton = new JButton("Cancel Booking");
        cancelButton.setBackground(new Color(231, 76, 60));
        cancelButton.setForeground(Color.WHITE);
        cancelButton.addActionListener(e -> cancelBooking());

        JButton refreshButton = new JButton("Refresh");
        refreshButton.setBackground(new Color(52, 152, 219));
        refreshButton.setForeground(Color.WHITE);
        refreshButton.addActionListener(e -> refreshData());

        buttonPanel.add(bookButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(refreshButton);
        formPanel.add(buttonPanel, gbc);

        // Room Table
        String[] columns = {"Room Number", "Type", "Price", "Status", "Guest Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        roomTable = new JTable(tableModel);
        roomTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        roomTable.getTableHeader().setReorderingAllowed(false);

        // Add mouse listener to select rooms from table
        roomTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = roomTable.getSelectedRow();
                if (row >= 0) {
                    String roomNumber = (String) tableModel.getValueAt(row, 0);
                    String roomType = (String) tableModel.getValueAt(row, 1);
                    String status = (String) tableModel.getValueAt(row, 3);

                    // Update the form with selected room info
                    roomTypeCombo.setSelectedItem(roomType);
                    updateRoomNumberCombo();
                    roomNumberCombo.setSelectedItem(roomNumber);

                    if ("Occupied".equals(status)) {
                        String guestName = (String) tableModel.getValueAt(row, 4);
                        guestNameField.setText(guestName);
                    }
                }
            }
        });

        // Custom cell renderer for better visual feedback
        roomTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                String status = (String) table.getValueAt(row, 3);
                if ("Occupied".equals(status)) {
                    c.setBackground(new Color(255, 230, 230)); // Light red for occupied
                    c.setForeground(new Color(139, 0, 0)); // Dark red text
                } else {
                    c.setBackground(new Color(230, 255, 230)); // Light green for available
                    c.setForeground(Color.BLACK);
                }

                if (isSelected) {
                    c.setBackground(new Color(200, 200, 255)); // Light blue for selection
                }

                return c;
            }
        });

        roomTable.setRowHeight(30);
        roomTable.setFont(new Font("Arial", Font.PLAIN, 14));
        roomTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane tableScrollPane = new JScrollPane(roomTable);
        tableScrollPane.setBorder(new TitledBorder("Room Status - Click on a room to select it for booking/cancellation"));

        // Main layout
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(statsPanel, BorderLayout.NORTH);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        mainPanel.add(tableScrollPane, BorderLayout.SOUTH);

        add(headerPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        loadRoomData();
    }

    private void updateRoomNumberCombo() {
        String selectedType = (String) roomTypeCombo.getSelectedItem();
        List<Room> rooms = selectedType.equals("single") ? singleRooms : doubleRooms;

        roomNumberCombo.removeAllItems();

        for (Room room : rooms) {
            if (!room.isBooked()) {
                roomNumberCombo.addItem(room.getRoomNumber());
            }
        }

        if (roomNumberCombo.getItemCount() == 0) {
            roomNumberCombo.addItem("No rooms available");
        }
    }

    private JLabel createStatLabel(String text, Color color) {
        JLabel label = new JLabel(text, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 16));
        label.setForeground(color);
        label.setBorder(new CompoundBorder(
                new LineBorder(color.darker(), 2),
                new EmptyBorder(10, 5, 10, 5)
        ));
        label.setOpaque(true);
        label.setBackground(new Color(240, 240, 240));
        return label;
    }

    private void loadRoomData() {
        tableModel.setRowCount(0);

        // Add single rooms
        for (Room room : singleRooms) {
            addRoomToTable(room);
        }

        // Add double rooms
        for (Room room : doubleRooms) {
            addRoomToTable(room);
        }
    }

    private void addRoomToTable(Room room) {
        String status = room.isBooked() ? "Occupied" : "Available";
        String guestName = room.isBooked() ? room.getGuestName() : "-";
        String price = "₹" + room.getPrice();

        tableModel.addRow(new Object[]{
                room.getRoomNumber(),
                room.getRoomType(),
                price,
                status,
                guestName
        });
    }

    private void bookRoom() {
        String guestName = guestNameField.getText().trim();
        String roomType = (String) roomTypeCombo.getSelectedItem();
        String roomNumber = (String) roomNumberCombo.getSelectedItem();

        if (guestName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter guest name", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if ("No rooms available".equals(roomNumber)) {
            JOptionPane.showMessageDialog(this,
                    "No available " + roomType + " rooms at the moment.",
                    "No Rooms Available",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Room> rooms = roomType.equals("single") ? singleRooms : doubleRooms;
        Room selectedRoom = findRoomByNumber(rooms, roomNumber);

        if (selectedRoom != null && !selectedRoom.isBooked()) {
            selectedRoom.bookRoom(guestName);
            JOptionPane.showMessageDialog(this,
                    "Room " + selectedRoom.getRoomNumber() + " booked successfully!\n" +
                            "Price: ₹" + selectedRoom.getPrice() +
                            "\nGuest: " + guestName,
                    "Booking Confirmed",
                    JOptionPane.INFORMATION_MESSAGE
            );
            refreshData();
        } else if (selectedRoom != null && selectedRoom.isBooked()) {
            JOptionPane.showMessageDialog(this,
                    "Room " + roomNumber + " is already booked.\nPlease select another room.",
                    "Room Already Booked",
                    JOptionPane.WARNING_MESSAGE
            );
        } else {
            JOptionPane.showMessageDialog(this,
                    "Room " + roomNumber + " not found.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void cancelBooking() {
        int selectedRow = roomTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a room from the table below to cancel booking",
                    "No Room Selected",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        String roomNumber = (String) tableModel.getValueAt(selectedRow, 0);
        String roomType = (String) tableModel.getValueAt(selectedRow, 1);
        String status = (String) tableModel.getValueAt(selectedRow, 3);

        // Check if the room is actually booked
        if (!"Occupied".equals(status)) {
            JOptionPane.showMessageDialog(this,
                    "Room " + roomNumber + " is not currently booked.\nCannot cancel booking for an available room.",
                    "Room Not Booked",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        List<Room> rooms = roomType.equals("single") ? singleRooms : doubleRooms;
        Room room = findRoomByNumber(rooms, roomNumber);

        if (room != null && room.isBooked()) {
            String guestName = room.getGuestName();
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Cancel booking for room " + roomNumber + "?\n" +
                            "Guest: " + guestName + "\n" +
                            "Room Type: " + roomType + "\n" +
                            "Price: ₹" + room.getPrice(),
                    "Confirm Booking Cancellation",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE
            );

            if (confirm == JOptionPane.YES_OPTION) {
                room.cancelBooking();
                JOptionPane.showMessageDialog(this,
                        "Booking cancelled successfully for room " + roomNumber + "\n" +
                                "Guest: " + guestName + " has been checked out.",
                        "Cancellation Successful",
                        JOptionPane.INFORMATION_MESSAGE
                );
                refreshData();
            }
        } else {
            JOptionPane.showMessageDialog(this,
                    "Room " + roomNumber + " is not currently booked or cannot be found.",
                    "Booking Not Found",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private Room findRoomByNumber(List<Room> rooms, String roomNumber) {
        for (Room room : rooms) {
            if (room.getRoomNumber().equals(roomNumber)) {
                return room;
            }
        }
        return null;
    }

    private void refreshData() {
        loadRoomData();
        updateRoomAvailability();
        updateRevenue();
        updateRoomNumberCombo();
        guestNameField.setText("");
        roomTable.clearSelection();
    }

    private void updateRoomAvailability() {
        long availableSingle = singleRooms.stream().filter(room -> !room.isBooked()).count();
        long availableDouble = doubleRooms.stream().filter(room -> !room.isBooked()).count();

        availableSingleLabel.setText("Available Single Rooms: " + availableSingle);
        availableDoubleLabel.setText("Available Double Rooms: " + availableDouble);

        // Update colors based on availability
        if (availableSingle == 0) {
            availableSingleLabel.setForeground(new Color(192, 57, 43));
        } else {
            availableSingleLabel.setForeground(new Color(52, 152, 219));
        }

        if (availableDouble == 0) {
            availableDoubleLabel.setForeground(new Color(192, 57, 43));
        } else {
            availableDoubleLabel.setForeground(new Color(46, 204, 113));
        }
    }

    private void updateRevenue() {
        int totalRevenue = 0;

        for (Room room : singleRooms) {
            if (room.isBooked()) {
                totalRevenue += room.getPrice();
            }
        }

        for (Room room : doubleRooms) {
            if (room.isBooked()) {
                totalRevenue += room.getPrice();
            }
        }

        totalRevenueLabel.setText("Total Revenue: ₹" + totalRevenue);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }

            HotelBookingSystem1 system = new HotelBookingSystem1();
            system.setVisible(true);
        });
    }
}

class Room {
    private String roomNumber;
    private String roomType;
    private int price;
    private boolean isBooked;
    private String guestName;

    public Room(String roomNumber, String roomType, int price) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.isBooked = false;
        this.guestName = "";
    }

    public String getRoomNumber() { return roomNumber; }
    public String getRoomType() { return roomType; }
    public int getPrice() { return price; }
    public boolean isBooked() { return isBooked; }
    public String getGuestName() { return guestName; }

    public void bookRoom(String guestName) {
        this.isBooked = true;
        this.guestName = guestName;
    }

    public void cancelBooking() {
        this.isBooked = false;
        this.guestName = "";
    }
}
