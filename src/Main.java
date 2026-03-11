import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Welcome to Book My Stay");
        System.out.println("      Hotel Booking System v8.0");
        System.out.println("======================================");

        // 1. Initialize core services
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomInventory roomInventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        // 2. Initialize Use Case 8: History and Reporting
        BookingHistory history = new BookingHistory();
        BookingReportService reportService = new BookingReportService();

        // Setup Inventory
        roomInventory.addRooms("Single Room", 2);
        roomInventory.addRooms("Double Room", 2);
        roomInventory.addRooms("Suite Room", 1);

        // Simulate incoming booking requests (Using names from provided image)
        requestQueue.addRequest(new Reservation("R101", "Abhi", "Single Room"));
        requestQueue.addRequest(new Reservation("R102", "Subha", "Double Room"));
        requestQueue.addRequest(new Reservation("R103", "Vanmathi", "Suite Room"));

        System.out.println("\nProcessing bookings and recording history...\n");

        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.pollRequest();
            if (reservation != null) {
                // Allocate the room
                String allocatedRoomId = allocationService.allocateRoom(reservation, roomInventory);
                
                // If allocation was successful, save to history
                if (allocatedRoomId != null) {
                    history.addReservation(reservation);
                    
                    // Optional: Add-on services from Use Case 7
                    if (reservation.getGuestName().equals("Abhi")) {
                        serviceManager.addService(allocatedRoomId, new AddOnService("Breakfast", 500.0));
                    }
                }
            }
        }

        // 3. Generate the Final Report (Use Case 8)
        System.out.println("\nBooking History and Reporting");
        reportService.generateReport(history);
    }
}

/**
 * Use Case 8: Booking History
 * Maintains an ordered record of confirmed reservations.
 */
class BookingHistory {
    private List<Reservation> confirmedReservations;

    public BookingHistory() {
        this.confirmedReservations = new ArrayList<>();
    }

    public void addReservation(Reservation reservation) {
        confirmedReservations.add(reservation);
    }

    public List<Reservation> getConfirmedReservations() {
        return confirmedReservations;
    }
}

/**
 * Use Case 8: Booking Report Service
 * Separates reporting logic from data storage.
 */
class BookingReportService {
    public void generateReport(BookingHistory history) {
        System.out.println("Booking History Report");
        for (Reservation res : history.getConfirmedReservations()) {
            System.out.println("Guest: " + res.getGuestName() + 
                               ", Room Type: " + res.getRoomType());
        }
    }
}

// --- Use Case 7: Add-On Service Classes ---

class AddOnService {
    private String serviceName;
    private double cost;
    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }
    public double getCost() { return cost; }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> servicesByReservation = new HashMap<>();
    public void addService(String resId, AddOnService s) {
        servicesByReservation.computeIfAbsent(resId, k -> new ArrayList<>()).add(s);
    }
}

// --- Core Core Logic Classes ---

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }
    public String getGuestName() { return guestName; }
    public String getRoomType() { return roomType; }
}

class BookingRequestQueue {
    private Queue<Reservation> queue = new LinkedList<>();
    public void addRequest(Reservation r) { queue.add(r); }
    public Reservation pollRequest() { return queue.poll(); }
    public boolean isEmpty() { return queue.isEmpty(); }
}

class RoomInventory {
    private Map<String, Integer> availableRooms = new HashMap<>();
    public void addRooms(String type, int count) { availableRooms.put(type, count); }
    public boolean hasAvailability(String type) { return availableRooms.getOrDefault(type, 0) > 0; }
    public void allocateRoom(String type) { availableRooms.put(type, availableRooms.get(type) - 1); }
}

class RoomAllocationService {
    public String allocateRoom(Reservation reservation, RoomInventory inventory) {
        String type = reservation.getRoomType();
        if (inventory.hasAvailability(type)) {
            inventory.allocateRoom(type);
            String id = type.replace(" ", "") + "-Confirmed";
            System.out.println("Room allocated for guest " + reservation.getGuestName());
            return id;
        }
        return null;
    }
}
