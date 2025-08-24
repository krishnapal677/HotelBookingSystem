package NAMANPROJECTS;

import java.util.HashMap;
import java.util.Map;

public class Hotel {
    private Map<Integer, String> bookings;

    public Hotel() {
        bookings = new HashMap<>();
    }

    public boolean bookRoom(String guestName, int roomNumber) {
        if (bookings.containsKey(roomNumber)) {
            return false; // Room is already booked
        }
        bookings.put(roomNumber, guestName);
        return true; // Booking successful
    }

    public boolean cancelBooking(int roomNumber) {
        if (bookings.containsKey(roomNumber)) {
            bookings.remove(roomNumber);
            return true; // Cancellation successful
        }
        return false; // No booking found
    }
}
