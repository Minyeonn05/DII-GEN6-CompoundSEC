import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class HotelAccessGUI extends JFrame {
    private JTextField cardIdField, floorField, roomField, newCardIdField, cardNameField;
    private JComboBox<String> cardTypeComboBox, accessLevelComboBox;
    private static final Map<String, Card> cardAccessMap = new HashMap<>();
    private static final Map<String, List<String>> accessHistory = new HashMap<>();

    public HotelAccessGUI() {
        super("Hotel Access Control System");
        loadLogs(); // Load logs when the program starts
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
        addCardButton.addActionListener(e -> addCard());

        // Access Request
        gbc.gridy = 5;
        gbc.gridwidth = 1;
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
        requestAccessButton.addActionListener(e -> checkAccess());

        // Modify Card Button
        gbc.gridy = 9;
        JButton modifyCardButton = new JButton("Modify Card");
        add(modifyCardButton, gbc);
        modifyCardButton.addActionListener(e -> modifyCard());

        // Admin Panel Button
        gbc.gridy = 10;
        JButton adminPanelButton = new JButton("Admin Panel");
        add(adminPanelButton, gbc);
        adminPanelButton.addActionListener(e -> showAdminLogin());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 500);
        setResizable(false);
        setVisible(true);
    }

    private void addCard() {
        String cardType = (String) cardTypeComboBox.getSelectedItem();
        String accessLevel = (String) accessLevelComboBox.getSelectedItem();
        String cardId = newCardIdField.getText().trim();
        String cardName = cardNameField.getText().trim();

        // Validate Input
        if (cardId.isEmpty() || cardName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Card ID and Card Name!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Check if Card ID already exists
        if (cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Card ID already exists! Please use a unique ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Card newCard;

        // Ensure Visitors only have Low access
        if ("Visitor".equals(cardType)) {
            if (!"Low".equals(accessLevel)) {
                JOptionPane.showMessageDialog(this, "Visitors can only have Low access!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            newCard = new VisitorCard(cardId, cardName, "Low");
        } else {
            newCard = new EmployeeCard(cardId, cardName, accessLevel);
        }

        // Store the new card and initialize access history
        cardAccessMap.put(cardId, newCard);
        accessHistory.putIfAbsent(cardId, new ArrayList<>());

        // Log the card creation
        accessHistory.get(cardId).add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " | Card Created: " + newCard);

        JOptionPane.showMessageDialog(this, "Card added successfully!\nType: " + cardType, "Success", JOptionPane.INFORMATION_MESSAGE);
    }




    private void checkAccess() {
        String cardId = cardIdField.getText().trim();
        String floorStr = floorField.getText().trim();
        String roomStr = roomField.getText().trim();

        if (cardId.isEmpty() || floorStr.isEmpty() || roomStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Invalid Card ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int floor = Integer.parseInt(floorStr);
            Card card = cardAccessMap.get(cardId);
            boolean accessGranted = isAccessAllowed(card.getAccessLevel(), floor);
            String status = accessGranted ? "Granted" : "Denied";

            logAccess(cardId, floor, roomStr, status);

            JOptionPane.showMessageDialog(this, "Access " + status, accessGranted ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid floor number!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void modifyCard() {
        // Prompt user for the Card ID
        String cardId = JOptionPane.showInputDialog(this, "Enter Card ID to Modify:");

        if (cardId == null || cardId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Modification canceled. No Card ID provided.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cardId = cardId.trim();

        // Check if the card exists
        if (!cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Card ID not found! Please enter a valid Card ID.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Card card = cardAccessMap.get(cardId);

        // Prompt for new card details
        String newName = JOptionPane.showInputDialog(this, "Enter new name for Card ID: " + cardId, card.getName());

        // Determine allowed access levels (Visitors are restricted to "Low")
        String[] accessLevels = (card instanceof VisitorCard) ? new String[]{"Low"} : new String[]{"Low", "Medium", "High", "All"};
        String newAccessLevel = (String) JOptionPane.showInputDialog(this, "Select new access level:",
                "Modify Access Level", JOptionPane.PLAIN_MESSAGE, null, accessLevels, card.getAccessLevel());

        if (newName == null || newName.trim().isEmpty() || newAccessLevel == null) {
            JOptionPane.showMessageDialog(this, "Modification canceled. Invalid input.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        newName = newName.trim();
        String oldName = card.getName();
        String oldAccessLevel = card.getAccessLevel();

        // Update and log card modifications
        boolean modified = false;

        // Update card name if changed
        if (!newName.equals(oldName)) {
            card.setName(newName);
            logModification(cardId, "Name changed from '" + oldName + "' to '" + newName + "'");
            modified = true;
        }

        // Update access level if changed (and not a Visitor)
        if (!newAccessLevel.equals(oldAccessLevel)) {
            card.setAccessLevel(newAccessLevel);
            logModification(cardId, "Access Level changed from '" + oldAccessLevel + "' to '" + newAccessLevel + "'");
            modified = true;
        }

        // Confirmation message
        if (modified) {
            JOptionPane.showMessageDialog(this, "Card updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "No changes were made to the card.", "Info", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // Helper method to log modifications to the access_log.txt file
    private void logModification(String cardId, String action) {
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        String logEntry = timestamp + " | " + action;

        // Log to access history
        accessHistory.get(cardId).add(logEntry);

        // Append to access_log.txt
        try (FileWriter writer = new FileWriter("access_log.txt", true)) {
            writer.write("Card ID: " + cardId + " | " + logEntry + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error writing to log file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }




    private void showAdminLogin() {
        JTextField adminIdField = new JTextField();
        JPasswordField adminPasswordField = new JPasswordField();
        Object[] message = {"Admin ID:", adminIdField, "Password:", adminPasswordField};

        int option = JOptionPane.showConfirmDialog(this, message, "Admin Login", JOptionPane.OK_CANCEL_OPTION);

        if (option == JOptionPane.OK_OPTION) {
            String enteredId = adminIdField.getText();
            String enteredPassword = new String(adminPasswordField.getPassword());

            if (Config.ADMIN_ID.equals(enteredId) && Config.ADMIN_PASSWORD.equals(enteredPassword)) {
                new AdminPanel(cardAccessMap, accessHistory); // Open Admin Panel
            } else {
                JOptionPane.showMessageDialog(this, "Invalid credentials!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }


    private boolean isAccessAllowed(String accessLevel, int floor) {
        return switch (accessLevel) {
            case "Low" -> floor == 1;
            case "Medium" -> floor <= 3;
            case "High" -> floor <= 5;
            case "All" -> true;
            default -> false;
        };
    }

    private void logAccess(String cardId, int floor, String room, String status) {
        accessHistory.get(cardId).add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) +
                " | Floor: " + floor + " | Room: " + room + " | Status: " + status);
    }
    private void loadLogs() {
        try (Scanner scanner = new Scanner(new File("access_log.txt"))) {
            while (scanner.hasNextLine()) {
                System.out.println(scanner.nextLine());
            }
        } catch (IOException e) {
            System.out.println("No previous logs found. Starting fresh...");
        }
    }



    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelAccessGUI::new);
    }
}


