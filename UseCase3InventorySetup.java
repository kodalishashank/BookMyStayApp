import java.util.Map;

public class UseCase3InventorySetup {

    public static void main(String[] args) {
        System.out.println("=== Hotel Inventory System ===");
        
        RoomInventory inventory = new RoomInventory();
        Map<String, Integer> currentStatus = inventory.getRoomAvailability();

        for (Map.Entry<String, Integer> entry : currentStatus.entrySet()) {
            System.out.println("Room Type: " + entry.getKey() + " | Available: " + entry.getValue());
        }

        System.out.println("\nUpdating Suite availability...");
        inventory.updateAvailability("Suite", 1);
        System.out.println("New Suite Count: " + inventory.getRoomAvailability().get("Suite"));
    }
}
