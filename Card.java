// Abstract Card class
public abstract class Card {
    private String id;
    private String name;
    private String accessLevel;

    // Constructor
    public Card(String id, String name, String accessLevel) {
        this.id = id;
        this.name = name;
        this.accessLevel = accessLevel;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public String getName() {
        return name;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Abstract method for card type (implemented by subclasses)
    public abstract String getCardType();

    // String representation for easy display
    @Override
    public String toString() {
        return "Card ID: " + id + ", Name: " + name + ", Type: " + getCardType() + ", Access Level: " + accessLevel;
    }
}

// EmployeeCard subclass (allows any access level)
class EmployeeCard extends Card {
    public EmployeeCard(String id, String name, String accessLevel) {
        super(id, name, accessLevel);
    }

    @Override
    public String getCardType() {
        return "Employee";
    }
}

// VisitorCard subclass (force "Low" access level)
class VisitorCard extends Card {
    public VisitorCard(String id, String name , String accessLevel)  {
        super(id, name, "Low"); // Force Low access for visitors
    }

    @Override
    public String getCardType() {
        return "Visitor";
    }

    // Prevent changing Visitor access level
    @Override
    public void setAccessLevel(String accessLevel) {
        // Ignore changes to access level (keep "Low")
    }
}
