import java.util.*;

/**
 * Use Case 9: Error Handling & Validation
 * Custom exception represents invalid booking scenarios.
 */
class InvalidBookingException extends Exception {
    public InvalidBookingException(String message) {
        super(message);
    }
}

/**
 * Use Case 9: Reservation Validator
 * Centralizes validation rules for guest names and room types.
 */
class ReservationValidator {
    public void validate(String guestName, String roomType, RoomInventory inventory) 
            throws InvalidBookingException {
        
        // Check if guest name is valid
        if (guestName == null || guestName.trim().isEmpty()) {
            throw new InvalidBookingException("Guest name cannot be empty.");
        }

        // Case-sensitive validation check as per image d50c7a requirements
        // System expects "Single", "Double", or "Suite"
        List<String> validTypes = Arrays.asList("Single", "Double", "Suite");
        if (!validTypes.contains(roomType)) {
            throw new InvalidBookingException("Invalid room type selected.");
        }

        // Check if room type actually exists in inventory and has stock
        if (!inventory.hasAvailability(roomType + " Room")) {
            throw new InvalidBookingException("No availability for the selected room type.");
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Booking Validation System");
        System.out.println("      Hotel Booking System v9.0");
        System.out.println("======================================");

        Scanner scanner = new Scanner(System.in);
        
        // Initialize Components
        RoomInventory inventory = new RoomInventory();
        inventory.addRooms("Single Room", 2);
        inventory.addRooms("Double Room", 2);
        
        ReservationValidator validator = new ReservationValidator();
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        BookingHistory history = new BookingHistory();

        try {
            // Get user input (Use Case 9)
            System.out.print("Enter guest name: ");
            String name = scanner.nextLine();

            System.out.print("Enter room type (Single/Double/Suite): ");
            String type = scanner.nextLine();

            // Validate Input
            validator.validate(name, type, inventory);

            // If validation passes, proceed with booking
            Reservation res = new Reservation("R" + (int)(Math.random()*1000), name, type + " Room");
            requestQueue.addRequest(res);
            
            System.out.println("Booking successful for " + name);
            history.addReservation(res);

        } catch (InvalidBookingException e) {
            // Handle domain-specific validation errors
            System.out.println("Booking failed: " + e.getMessage());
        } finally {
            // Clean up resources
            scanner.close();
        }
    }
}

// --- Supporting Classes from Previous Use Cases ---

class Reservation {
    private String id, name, type;
    public Reservation(String id, String name, String type) {
        this.id = id; this.name = name; this.type = type;
    }
    public String getGuestName() { return name; }
    public String getRoomType() { return type; }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();
    public void addRequest(Reservation r) { queue.add(r); }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();
    public void addRooms(String type, int count) { rooms.put(type, count); }
    public boolean hasAvailability(String type) { return rooms.getOrDefault(type, 0) > 0; }
}

class BookingHistory {
    private List<Reservation> list = new ArrayList<>();
    public void addReservation(Reservation r) { list.add(r); }
}
