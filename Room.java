public abstract class Room {

    protected int numberOfBeds;
    protected int squareFeet;
    protected double pricePerNight;

    public Room(int numberOfBeds, int squareFeet, double pricePerNight) {
        this.numberOfBeds = numberOfBeds;
        this.squareFeet = squareFeet;
        this.pricePerNight = pricePerNight;
    }

    public void displayRoomDetails() {
        System.out.println(" - Beds: " + numberOfBeds);
        System.out.println(" - Size: " + squareFeet + " sq ft");
        System.out.println(" - Price: $" + pricePerNight + " per night");
    }
}
