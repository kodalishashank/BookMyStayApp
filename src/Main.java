import java.util.*;

/**
 * Use Case 11: Payment Processing
 * Handles the final billing logic for a reservation.
 */
class PaymentProcessor {
    private Map<String, Double> roomRates;

    public PaymentProcessor() {
        roomRates = new HashMap<>();
        roomRates.put("Single Room", 2000.0);
        roomRates.put("Double Room", 3500.0);
        roomRates.put("Suite Room", 5000.0);
    }

    public void processPayment(Reservation reservation, AddOnServiceManager serviceManager) {
        String roomType = reservation.getRoomType();
        double baseRate = roomRates.getOrDefault(roomType, 0.0);
        double addOnTotal = serviceManager.calculateTotalServiceCost(reservation.getReservationId());
        double finalTotal = baseRate + addOnTotal;

        System.out.println("--- Final Billing Statement ---");
        System.out.println("Guest: " + reservation.getGuestName());
        System.out.println("Room Base Rate (" + roomType + "): " + baseRate);
        System.out.println("Add-On Services Total: " + addOnTotal);
        System.out.println("Total Amount Paid: " + finalTotal);
        System.out.println("Payment Status: SUCCESSFUL");
        System.out.println("-------------------------------");
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Hotel Management System v11.0");
        System.out.println("      Full Lifecycle: Booking to Billing");
        System.out.println("======================================");

        // 1. Initialize all systems
        RoomInventory inventory = new RoomInventory();
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        
        inventory.addRooms("Single Room", 5);

        // 2. Simulate a confirmed booking (R101 - Abhisheak)
        Reservation res = new Reservation("R101", "Abhisheak", "Single Room");
        System.out.println("Processing booking for: " + res.getGuestName());
        inventory.allocateRoom("Single Room");

        // 3. Add Services (Use Case 7)
        serviceManager.addService(res.getReservationId(), new AddOnService("Spa", 1500.0));
        serviceManager.addService(res.getReservationId(), new AddOnService("Breakfast", 500.0));

        // 4. Process Payment (Use Case 11)
        paymentProcessor.processPayment(res, serviceManager);
    }
}

// --- Supporting Logic from Previous Use Cases ---

class AddOnService {
    private String name;
    private double cost;
    public AddOnService(String name, double cost) { this.name = name; this.cost = cost; }
    public double getCost() { return cost; }
}

class AddOnServiceManager {
    private Map<String, List<AddOnService>> servicesByReservation = new HashMap<>();

    public void addService(String resId, AddOnService s) {
        servicesByReservation.computeIfAbsent(resId, k -> new ArrayList<>()).add(s);
    }

    public double calculateTotalServiceCost(String resId) {
        return servicesByReservation.getOrDefault(resId, new ArrayList<>())
                .stream().mapToDouble(AddOnService::getCost).sum();
    }
}

class RoomInventory {
    private Map<String, Integer> availableRooms = new HashMap<>();
    public void addRooms(String type, int count) { availableRooms.put(type, count); }
    public void allocateRoom(String type) { 
        availableRooms.put(type, availableRooms.get(type) - 1); 
    }
}

class Reservation {
    private String id, name, type;
    public Reservation(String id, String name, String type) {
        this.id = id; this.name = name; this.type = type;
    }
    public String getReservationId() { return id; }
    public String getGuestName() { return name; }
    public String getRoomType() { return type; }
}
