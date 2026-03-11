import java.util.*;

/**
 * Use Case 10: Room Cancellation Service
 * Handles the logic for releasing rooms and updating inventory.
 */
class RoomCancellationService {

    public void cancelBooking(
            String reservationId, 
            String roomType, 
            RoomInventory inventory, 
            AddOnServiceManager serviceManager) {
        
        System.out.println("Processing cancellation for Reservation ID: " + reservationId);

        // 1. Update Inventory (Use Case 10 requirement)
        inventory.releaseRoom(roomType);

        // 2. Clear associated services (Clean-up)
        serviceManager.removeServices(reservationId);

        System.out.println("Cancellation successful. Room type '" + roomType + "' is now available.");
        System.out.println("--------------------------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Hotel Management System v10.0");
        System.out.println("      Cancellation & Inventory Management");
        System.out.println("======================================");

        // Initialize Services
        RoomInventory inventory = new RoomInventory();
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        RoomCancellationService cancellationService = new RoomCancellationService();

        // Setup initial inventory
        inventory.addRooms("Single Room", 1);
        
        // Simulate an existing booking for "Abhisheak"
        String resId = "Single-101";
        System.out.println("Initial State: Room Single-101 is occupied.");
        serviceManager.addService(resId, new AddOnService("Spa", 1500.0));

        // Perform Cancellation (Use Case 10)
        cancellationService.cancelBooking(resId, "Single Room", inventory, serviceManager);

        // Verify Inventory Update
        System.out.println("Verification: Has Single Room availability? " + inventory.hasAvailability("Single Room"));
    }
}

// --- Updated Classes for Use Case 10 ---

class RoomInventory {
    private Map<String, Integer> availableRooms = new HashMap<>();

    public void addRooms(String type, int count) { availableRooms.put(type, count); }
    
    public boolean hasAvailability(String type) { 
        return availableRooms.getOrDefault(type, 0) > 0; 
    }

    // New method for Use Case 10
    public void releaseRoom(String type) {
        int current = availableRooms.getOrDefault(type, 0);
        availableRooms.put(type, current + 1);
    }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> servicesByReservation = new HashMap<>();

    public void addService(String resId, AddOnService s) {
        servicesByReservation.computeIfAbsent(resId, k -> new ArrayList<>()).add(s);
    }

    // New method for clean-up during cancellation
    public void removeServices(String resId) {
        servicesByReservation.remove(resId);
    }
}

// --- Use Case 7 Class ---
class AddOnService {
    private String name;
    private double cost;
    public AddOnService(String name, double cost) { this.name = name; this.cost = cost; }
}
