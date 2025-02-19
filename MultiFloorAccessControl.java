import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MultiFloorAccessControl {
    Map<String, Card> cardDatabase = new HashMap<>();
    private List<String> eventLog = new ArrayList<>(); // Log for history

    public void manageCard(Card card, String action) {
        if (action.equalsIgnoreCase("Add")) {
            cardDatabase.put(card.getId(), card);
            logEvent("Added card: " + card.getId() + " (" + card.getCardType() + ")");
        } else if (action.equalsIgnoreCase("Remove")) {
            cardDatabase.remove(card.getId());
            logEvent("Removed card: " + card.getId());
        }
    }

    public void grantAccess(Card card, String floor, String room) {
        logEvent("Access granted to Card ID " + card.getId() + " for Floor " + floor + ", Room " + room);
    }

    public void denyAccess(Card card, String floor, String room) {
        logEvent("Access denied to Card ID " + card.getId() + " for Floor " + floor + ", Room " + room);
    }

    private void logEvent(String event) {
        eventLog.add(event);
    }

    public List<String> getEventLog() {
        return eventLog;
    }
}
