import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class HotelAccessGUI extends JFrame {
    private JTextField cardIdField, floorField, roomField, newCardIdField, cardNameField;
    private JComboBox<String> cardTypeComboBox, accessLevelComboBox;
    private static final Map<String, CardInfo> cardAccessMap = new HashMap<>();
    private static final Map<String, List<String>> accessHistory = new HashMap<>();

    public HotelAccessGUI() {
        super("Hotel Access Control System");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;

        // Card Type
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Card Type:"), gbc);
        gbc.gridx = 1;
        cardTypeComboBox = new JComboBox<>(new String[]{"Employee", "Visitor"});
        add(cardTypeComboBox, gbc);

        // Access Level
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Access Level:"), gbc);
        gbc.gridx = 1;
        accessLevelComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "All"});
        add(accessLevelComboBox, gbc);

        // Card Name
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Card Name:"), gbc);
        gbc.gridx = 1;
        cardNameField = new JTextField(10);
        add(cardNameField, gbc);

        // Card ID
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Card ID:"), gbc);
        gbc.gridx = 1;
        newCardIdField = new JTextField(10);
        add(newCardIdField, gbc);

        // Add Card Button
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        JButton addCardButton = new JButton("Add Card");
        add(addCardButton, gbc);
        gbc.gridwidth = 1;

        addCardButton.addActionListener(e -> addCard());

        // Access Request Fields
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Enter Card ID:"), gbc);
        gbc.gridx = 1;
        cardIdField = new JTextField(10);
        add(cardIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Floor Number:"), gbc);
        gbc.gridx = 1;
        floorField = new JTextField(10);
        add(floorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomField = new JTextField(10);
        add(roomField, gbc);

        // Request Access Button
        gbc.gridx = 0;
        gbc.gridy = 8;
        gbc.gridwidth = 2;
        JButton requestAccessButton = new JButton("Request Access");
        add(requestAccessButton, gbc);
        gbc.gridwidth = 1;

        requestAccessButton.addActionListener(e -> checkAccess());

        // Admin Button
        gbc.gridx = 0;
        gbc.gridy = 9;
        gbc.gridwidth = 2; // Ensure it doesn't stretch
        gbc.weightx = 0;   // Prevents expanding
        JButton adminButton = new JButton("Admin Panel");
        adminButton.setPreferredSize(new Dimension(150, 30)); // Set fixed size
        add(adminButton, gbc);


        adminButton.addActionListener(e -> showAdminLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 550);
        setResizable(false);
        setVisible(true);

        // Modify Card Button
        gbc.gridx = 0;
        gbc.gridy = 10;
        gbc.gridwidth = 2;
        JButton modifyCardButton = new JButton("Modify Card");
        add(modifyCardButton, gbc);
        gbc.gridwidth = 1;

        modifyCardButton.addActionListener(e -> {
            boolean isAdmin = false; // Regular user
            modifyCard(isAdmin);
        });
    }

    private void addCard() {
         JTextArea adminHistoryArea = new JTextArea(15, 30);
        String cardType = (String) cardTypeComboBox.getSelectedItem();
        String accessLevel = (String) accessLevelComboBox.getSelectedItem();
        String cardId = newCardIdField.getText().trim();
        String cardName = cardNameField.getText().trim();

        if (cardId.isEmpty() || cardName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Card Name and ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cardAccessMap.put(cardId, new CardInfo(cardName, accessLevel)); // 🛑 Fix Here
        accessHistory.put(cardId, new ArrayList<>());

        JOptionPane.showMessageDialog(this, "Card added successfully!\nName: " + cardName + "\nID: " + cardId + "\nAccess Level: " + accessLevel, "Success", JOptionPane.INFORMATION_MESSAGE);

        // ✅ Update history after adding card
        updateHistoryText(adminHistoryArea);
    }


    private void checkAccess() {
        String cardId = cardIdField.getText().trim();
        String floor = floorField.getText().trim();
        String room = roomField.getText().trim();

        if (cardId.isEmpty() || floor.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CardInfo cardInfo = cardAccessMap.get(cardId);
        if (cardInfo == null) {
            JOptionPane.showMessageDialog(this, "Invalid Card ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean accessGranted = isAccessAllowed(cardInfo.accessLevel, Integer.parseInt(floor));
        String status = accessGranted ? "Granted" : "Denied";
        String message = "Access " + status + "!\nFloor " + floor + " Room " + room;

        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        accessHistory.get(cardId).add("Time: " + timestamp + " | Floor: " + floor + " | Room: " + room + " | Status: " + status);

        JOptionPane.showMessageDialog(this, message, accessGranted ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);
    }

        private boolean isAccessAllowed(String accessLevel, int floor) {
            switch (accessLevel) {
                case "Low":
                    return floor == 1; // Only allows access to floor 1
                case "Medium":
                    return floor <= 3; // Allows access to floors 1-3
                case "High":
                    return floor <= 5; // Allows access to floors 1-5
                case "All":
                    return true; // Full access
                default:
                    return false;
            }


    }

    private void showAdminLogin() {
        JTextField adminIdField = new JTextField();
        JPasswordField adminPasswordField = new JPasswordField();
        Object[] message = {
                "Admin ID:", adminIdField,
                "Password:", adminPasswordField
        };

        int option = JOptionPane.showConfirmDialog(null, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);
        if (option == JOptionPane.OK_OPTION) {
            String enteredId = adminIdField.getText();
            String enteredPassword = new String(adminPasswordField.getPassword());

            if (enteredId.equals(Config.ADMIN_ID) && enteredPassword.equals(Config.ADMIN_PASSWORD)) {
                new AdminPanel(cardAccessMap, accessHistory);;
            } else {
                JOptionPane.showMessageDialog(null, "Invalid Admin Credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public class CardInfo {
        private String name;
        private String accessLevel;

        public CardInfo(String name, String accessLevel) {
            this.name = name;
            this.accessLevel = accessLevel;
        }

        public String getName() {
            return name;
        }

        public String getAccessLevel() {
            return accessLevel;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void setAccessLevel(String accessLevel) {
            this.accessLevel = accessLevel;
        }
    }
    private void updateHistoryText(JTextArea historyArea) {
        StringBuilder historyText = new StringBuilder();

        for (var entry : cardAccessMap.entrySet()) {
            String cardId = entry.getKey();
            CardInfo cardInfo = entry.getValue();

            // 🛑 Debugging Access Level
            System.out.println("DEBUG: Card ID = " + cardId);
            System.out.println("DEBUG: Card Name = " + cardInfo.getName());
            System.out.println("DEBUG: Access Level = " + cardInfo.getAccessLevel());

            historyText.append("Card Name: ").append(cardInfo.getName())
                    .append(" | ID: ").append(cardId)
                    .append(" | Access Level: ").append(cardInfo.getAccessLevel())
                    .append("\n");

            if (accessHistory.containsKey(cardId)) {
                for (String log : accessHistory.get(cardId)) {
                    historyText.append("   -> ").append(log).append("\n");
                }
            }
        }

        historyArea.setText(historyText.toString());
    }

    private void modifyCard(boolean isAdmin) {
        String cardId = newCardIdField.getText().trim();

        if (!cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Card ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CardInfo card = cardAccessMap.get(cardId);
        String oldName = card.getName();
        String oldAccessLevel = card.getAccessLevel();

        String newName = JOptionPane.showInputDialog(this, "Enter new name:", oldName);

        if (newName != null && !newName.trim().isEmpty()) {
            card.setName(newName.trim());

            // Log the change
            logAccessModification(cardId, "Name changed from " + oldName + " to " + newName);
        }

        if (isAdmin) { // Only admins can change access levels
            String newAccessLevel = (String) JOptionPane.showInputDialog(this,
                    "Select new access level:",
                    "Modify Access Level",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Low", "Medium", "High", "All"},
                    oldAccessLevel);

            if (newAccessLevel != null && !newAccessLevel.equals(oldAccessLevel)) {
                card.setAccessLevel(newAccessLevel);

                // Log the change
                logAccessModification(cardId, "Access Level changed from " + oldAccessLevel + " to " + newAccessLevel);
            }
        }

        JOptionPane.showMessageDialog(this, "Card updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
    }
    private void logAccessModification(String cardId, String logMessage) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String fullLog = timestamp + " | " + logMessage;

        accessHistory.get(cardId).add(fullLog);
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelAccessGUI::new);
    }
}
