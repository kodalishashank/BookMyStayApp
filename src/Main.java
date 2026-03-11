import java.util.*;

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Welcome to Book My Stay");
        System.out.println("      Hotel Booking System v7.0");
        System.out.println("======================================");

        // 1. Initialize core services
        BookingRequestQueue requestQueue = new BookingRequestQueue();
        RoomInventory roomInventory = new RoomInventory();
        RoomAllocationService allocationService = new RoomAllocationService();
        
        // 2. Initialize Use Case 7: Add-On Service Manager
        AddOnServiceManager serviceManager = new AddOnServiceManager();

        roomInventory.addRooms("Single Room", 2);
        roomInventory.addRooms("Double Room", 2);

        // Simulate incoming booking requests
        requestQueue.addRequest(new Reservation("R001", "Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("R004", "Diana", "Double Room"));

        System.out.println("\nProcessing bookings and selecting Add-On Services...\n");

        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.pollRequest();
            if (reservation != null) {
                // Allocate the room first
                String allocatedRoomId = allocationService.allocateRoom(reservation, roomInventory);
                
                // If allocation was successful, simulate Add-On Service Selection (Use Case 7)
                if (allocatedRoomId != null) {
                    // Example: Adding Spa and Breakfast for Alice
                    if (reservation.getGuestName().equals("Alice")) {
                        serviceManager.addService(allocatedRoomId, new AddOnService("Breakfast", 500.0));
                        serviceManager.addService(allocatedRoomId, new AddOnService("Spa", 1000.0));
                    }
                    
                    // Display totals for the specific room/reservation
                    System.out.println("Add-On Service Selection");
                    System.out.println("Reservation ID: " + allocatedRoomId);
                    System.out.println("Total Add-On Cost: " + serviceManager.calculateTotalServiceCost(allocatedRoomId));
                    System.out.println("--------------------------------------");
                }
            }
        }
    }
}

/** * Use Case 7: Add-On Service Class
 * Represents an optional service like Spa, Breakfast, etc.
 */
class AddOnService {
    private String serviceName;
    private double cost;

    public AddOnService(String serviceName, double cost) {
        this.serviceName = serviceName;
        this.cost = cost;
    }

    public String getServiceName() { return serviceName; }
    public double getCost() { return cost; }
}

/**
 * Use Case 7: Add-On Service Manager
 * Maps reservation/room IDs to a list of selected services.
 */
class AddOnServiceManager {
    private Map<String, List<AddOnService>> servicesByReservation;

    public AddOnServiceManager() {
        this.servicesByReservation = new HashMap<>();
    }

    public void addService(String reservationId, AddOnService service) {
        servicesByReservation
            .computeIfAbsent(reservationId, k -> new ArrayList<>())
            .add(service);
    }

    public double calculateTotalServiceCost(String reservationId) {
        List<AddOnService> services = servicesByReservation.getOrDefault(reservationId, Collections.emptyList());
        double total = 0;
        for (AddOnService s : services) {
            total += s.getCost();
        }
        return total;
    }
}

// --- Existing Classes with Minor Updates for Integration ---

class Reservation {
    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() { return reservationId; }
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
    public void allocateRoom(String type) {
        availableRooms.put(type, availableRooms.get(type) - 1);
    }
}

class RoomAllocationService {
    private Map<String, Set<String>> assignedRoomsByType = new HashMap<>();

    public String allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();
        if (!inventory.hasAvailability(roomType)) {
            System.out.println("No availability for " + roomType);
            return null;
        }

        String roomId = generateRoomId(roomType);
        assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);
        inventory.allocateRoom(roomType);
        
        System.out.println("Room allocated: " + roomId + " for guest " + reservation.getGuestName());
        return roomId;
    }

    private String generateRoomId(String roomType) {
        Set<String> assignedIds = assignedRoomsByType.getOrDefault(roomType, Collections.emptySet());
        int nextId = 1;
        String baseId = roomType.replaceAll("\\s", "") + "-";
        while (assignedIds.contains(baseId + nextId)) { nextId++; }
        return baseId + nextId;
    }
}
