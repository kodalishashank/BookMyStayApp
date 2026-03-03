public class UseCase3InventorySetup {

    public static void main(String[] args) {
        RoomInventory inventory = new RoomInventory();
        
        Room single = new SingleRoom();
        Room doubleRoom = new DoubleRoom();
        Room suite = new SuiteRoom();

        RoomSearchService searchService = new RoomSearchService();
        searchService.searchAvailableRooms(inventory, single, doubleRoom, suite);
    }
}
