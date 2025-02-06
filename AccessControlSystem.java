import java.util.ArrayList;
import java.util.HashMap;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

// Abstract Class สำหรับ AccessControlSystem
abstract class AccessControlSystem {
    public abstract void grantAccess(Card card, String floor, String room);
    public abstract void denyAccess(Card card, String floor, String room);
    public abstract void logAccessAttempt(Card card, String floor, String room, boolean granted);
    public abstract void manageCard(Card card, String action); // Add, Modify, Revoke
}


// Concrete Class สำหรับ MultiFloorAccessControl
class MultiFloorAccessControl extends AccessControlSystem {
    private ArrayList<String> accessLog = new ArrayList<>();
    protected HashMap<String, Card> cardDatabase = new HashMap<>();

    @Override
    public void grantAccess(Card card, String floor, String room) {
        if (cardDatabase.containsKey(card.getCardId())) { // ตรวจสอบว่ามีบัตรในฐานข้อมูลหรือไม่
            if (card.getAccessLevel().equals(floor) || card.getAccessLevel().equals("All")) {
                if (!card.isExpired()) {
                    System.out.println("Access Granted to Card " + card.getCardId() + " on Floor " + floor + ", Room " + room);
                    logAccessAttempt(card, floor, room, true);
                } else {
                    System.out.println("Access Denied: Card Expired");
                    logAccessAttempt(card, floor, room, false);
                }
            } else {
                System.out.println("Access Denied: Insufficient Privileges");
                logAccessAttempt(card, floor, room, false);
            }
        } else {
            System.out.println("Access Denied: Card not found in database");
            logAccessAttempt(card, floor, room, false);
        }
    }

    @Override
    public void denyAccess(Card card, String floor, String room) {
        System.out.println("Access Denied to Card " + card.getCardId() + " on Floor " + floor + ", Room " + room);
        logAccessAttempt(card, floor, room, false);
    }

    @Override
    public void logAccessAttempt(Card card, String floor, String room, boolean granted) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = formatter.format(new Date());
        String logEntry = timestamp + " - Card " + card.getCardId() + ", Floor " + floor + ", Room " + room + ", Access " + (granted ? "Granted" : "Denied");
        accessLog.add(logEntry);
    }

    @Override
    public void manageCard(Card card, String action) {
        if (action.equals("Add")) {
            cardDatabase.put(card.getCardId(), card);
            System.out.println("Card " + card.getCardId() + " - Added");
        } else if (action.equals("Modify")) {
            cardDatabase.replace(card.getCardId(), card);
            System.out.println("Card " + card.getCardId() + " - Modified");
        } else if (action.equals("Revoke")) {
            cardDatabase.remove(card.getCardId());
            System.out.println("Card " + card.getCardId() + " - Revoked");
        }
    }

    public ArrayList<String> getAccessLog() {
        return accessLog;
    }
}