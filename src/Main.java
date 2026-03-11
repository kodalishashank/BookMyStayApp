import java.util.*;

/**
 * Use Case 12: Guest Feedback Class
 * Stores the review data provided by a guest.
 */
class GuestFeedback {
    private String guestName;
    private int rating; // 1 to 5
    private String comments;

    public GuestFeedback(String guestName, int rating, String comments) {
        this.guestName = guestName;
        this.rating = rating;
        this.comments = comments;
    }

    public void displayFeedback() {
        System.out.println("Guest: " + guestName + " | Rating: " + rating + "/5");
        System.out.println("Comment: " + comments);
        System.out.println("--------------------------------------");
    }
}

/**
 * Use Case 12: Feedback Service
 * Manages the collection and display of guest reviews.
 */
class FeedbackService {
    private List<GuestFeedback> feedbackList;

    public FeedbackService() {
        this.feedbackList = new ArrayList<>();
    }

    public void addFeedback(GuestFeedback feedback) {
        feedbackList.add(feedback);
        System.out.println("Feedback submitted successfully by " + feedback.getGuestName() + "!");
    }

    public void showAllFeedback() {
        System.out.println("\n--- All Guest Feedback & Ratings ---");
        if (feedbackList.isEmpty()) {
            System.out.println("No feedback available yet.");
        } else {
            for (GuestFeedback f : feedbackList) {
                f.displayFeedback();
            }
        }
    }
}

public class Main {
    public static void main(String[] args) {
        System.out.println("======================================");
        System.out.println("      Hotel Management System v12.0");
        System.out.println("      Final Phase: Feedback & Ratings");
        System.out.println("======================================");

        // 1. Initialize core components
        RoomInventory inventory = new RoomInventory();
        AddOnServiceManager serviceManager = new AddOnServiceManager();
        PaymentProcessor paymentProcessor = new PaymentProcessor();
        FeedbackService feedbackService = new FeedbackService();

        // 2. Simulate Stay Completion
        Reservation res = new Reservation("R105", "Siddharth", "Suite Room");
        
        // 3. Process Payment (Use Case 11)
        paymentProcessor.processPayment(res, serviceManager);

        // 4. Collect Feedback (Use Case 12)
        System.out.println("\nPost-Stay Feedback Collection:");
        GuestFeedback newFeedback = new GuestFeedback(
            res.getGuestName(), 
            5, 
            "Excellent service and very clean rooms!"
        );
        feedbackService.addFeedback(newFeedback);

        // 5. Display Report
        feedbackService.showAllFeedback();
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
    public String getReservationId() { return id; }
}

class RoomInventory {
    private Map<String, Integer> rooms = new HashMap<>();
    public void addRooms(String t, int c) { rooms.put(t, c); }
}

class AddOnServiceManager {
    public double calculateTotalServiceCost(String id) { return 0.0; }
}

class PaymentProcessor {
    public void processPayment(Reservation r, AddOnServiceManager s) {
        System.out.println("Processing final payment for " + r.getGuestName() + "...");
        System.out.println("Payment SUCCESSFUL.");
    }
}
