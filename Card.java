import java.util.Date;


public abstract class Card {
    private String id;
    private String accessLevel;
    private Date expiryDate;

    public Card(String accessLevel, Date expiryDate) {
        this.accessLevel = accessLevel;
        this.expiryDate = expiryDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) { // Setter for Card ID
        this.id = id;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public abstract String getCardType(); // Abstract method for card type
}

class EmployeeCard extends Card {
    public EmployeeCard(String accessLevel, Date expiryDate) {
        super(accessLevel, expiryDate);
    }

    @Override
    public String getCardType() {
        return "Employee";
    }
}

 class VisitorCard extends Card {
    public VisitorCard(String accessLevel, Date expiryDate) {
        super(accessLevel, expiryDate);
    }

    @Override
    public String getCardType() {
        return "Visitor";
    }
}

