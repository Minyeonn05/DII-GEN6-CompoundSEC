import java.util.UUID;
import java.util.Date;

// Abstract Class สำหรับ Card
abstract class Card {
    String cardId;
    String accessLevel; // Low, Medium, High
    Date expiryDate;

    public Card(String accessLevel, Date expiryDate) {
        this.cardId = generateUniqueId();
        this.accessLevel = accessLevel;
        this.expiryDate = expiryDate;
    }

    private String generateUniqueId() {
        // ใช้ UUID ในการสร้าง ID ที่ไม่ซ้ำกัน
        return UUID.randomUUID().toString();
    }

    public String getCardId() {
        return cardId;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public boolean isExpired() {
        return expiryDate.before(new Date()); // ตรวจสอบวันหมดอายุ
    }
}


class EmployeeCard extends Card {
    public EmployeeCard(String accessLevel, Date expiryDate) {
        super(accessLevel, expiryDate);
    }
}


class VisitorCard extends Card {
    public VisitorCard(String accessLevel, Date expiryDate) {
        super(accessLevel, expiryDate);
    }
}