import java.util.*;

public class UseCaseBookMyStay {

    public static void main(String[] args) {

        System.out.println("======================================");
        System.out.println("      Welcome to Book My Stay");
        System.out.println("      Hotel Booking System v6.0");
        System.out.println("======================================");

        // Initialize booking queue
        BookingRequestQueue requestQueue = new BookingRequestQueue();

        // Initialize centralized room inventory
        RoomInventory roomInventory = new RoomInventory();
        roomInventory.addRooms("Single Room", 2);
        roomInventory.addRooms("Double Room", 2);
        roomInventory.addRooms("Suite Room", 1);

        // Initialize room allocation service
        RoomAllocationService allocationService = new RoomAllocationService();

        // Simulate incoming booking requests
        requestQueue.addRequest(new Reservation("R001", "Alice", "Single Room"));
        requestQueue.addRequest(new Reservation("R002", "Bob", "Suite Room"));
        requestQueue.addRequest(new Reservation("R003", "Charlie", "Single Room"));
        requestQueue.addRequest(new Reservation("R004", "Diana", "Double Room"));

        System.out.println("\nProcessing booking requests and allocating rooms...\n");

        // Process each reservation in FIFO order and allocate rooms
        while (!requestQueue.isEmpty()) {
            Reservation reservation = requestQueue.pollRequest();
            if (reservation != null) {
                allocationService.allocateRoom(reservation, roomInventory);
            }
        }

        System.out.println("\nRoom allocation complete.");
    }
}


// Represents a reservation request
class Reservation {

    private String reservationId;
    private String guestName;
    private String roomType;

    public Reservation(String reservationId, String guestName, String roomType) {
        this.reservationId = reservationId;
        this.guestName = guestName;
        this.roomType = roomType;
    }

    public String getReservationId() {
        return reservationId;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getRoomType() {
        return roomType;
    }

    public void display() {
        System.out.println("Reservation ID: " + reservationId);
        System.out.println("Guest Name: " + guestName);
        System.out.println("Requested Room: " + roomType);
        System.out.println("--------------------------------------");
    }
}

// Queue to manage booking requests in FIFO order
class BookingRequestQueue {

    private Queue<Reservation> queue;

    public BookingRequestQueue() {
        queue = new LinkedList<>();
    }

    public void addRequest(Reservation reservation) {
        queue.add(reservation);
        System.out.println("Request received for " + reservation.getGuestName()
                + " → Added to queue.");
    }

    public Reservation pollRequest() {
        return queue.poll();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}

// RoomInventory tracks available rooms by type and quantity
class RoomInventory {

    private Map<String, Integer> availableRooms;

    public RoomInventory() {
        availableRooms = new HashMap<>();
    }

    // Add number of rooms available for a room type
    public void addRooms(String roomType, int count) {
        availableRooms.put(roomType, count);
    }

    // Check if a room type has available rooms
    public boolean hasAvailability(String roomType) {
        return availableRooms.getOrDefault(roomType, 0) > 0;
    }

    // Decrease availability count when a room is assigned
    public boolean allocateRoom(String roomType) {
        int available = availableRooms.getOrDefault(roomType, 0);
        if (available > 0) {
            availableRooms.put(roomType, available - 1);
            return true;
        }
        return false;
    }
}

// RoomAllocationService confirms bookings and assigns unique room IDs
class RoomAllocationService {

    // Tracks allocated room IDs to prevent duplicates
    private Set<String> allocatedRoomIds;

    // Tracks assigned rooms by type (roomType -> set of assigned room IDs)
    private Map<String, Set<String>> assignedRoomsByType;

    public RoomAllocationService() {
        allocatedRoomIds = new HashSet<>();
        assignedRoomsByType = new HashMap<>();
    }

    // Allocates a room for the reservation, updates inventory and prints confirmation
    public void allocateRoom(Reservation reservation, RoomInventory inventory) {
        String roomType = reservation.getRoomType();

        System.out.println("Processing reservation " + reservation.getReservationId()
                + " for " + reservation.getGuestName() + " (Room type: " + roomType + ")");

        if (!inventory.hasAvailability(roomType)) {
            System.out.println("  Sorry, no available rooms for type: " + roomType);
            return;
        }

        // Generate unique room ID
        String roomId = generateRoomId(roomType);

        // Mark room as allocated
        allocatedRoomIds.add(roomId);
        assignedRoomsByType.computeIfAbsent(roomType, k -> new HashSet<>()).add(roomId);

        // Update inventory
        inventory.allocateRoom(roomType);

        // Confirm allocation
        System.out.println("  Room allocated: " + roomId + " for guest " + reservation.getGuestName());
        System.out.println("--------------------------------------");
    }

    // Generates a unique room ID per room type, e.g. "SingleRoom-1", "SingleRoom-2"
    private String generateRoomId(String roomType) {
        Set<String> assignedIds = assignedRoomsByType.getOrDefault(roomType, Collections.emptySet());

        int nextId = 1;
        String baseId = roomType.replaceAll("\\s", "") + "-";

        while (assignedIds.contains(baseId + nextId)) {
            nextId++;
        }

        return baseId + nextId;
    }
}
