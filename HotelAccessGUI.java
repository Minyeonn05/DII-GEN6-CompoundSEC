import javax.swing.*;
import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class HotelAccessGUI extends JFrame {
    private JTextField cardIdField, floorField, roomField, newCardIdField;
    private JComboBox<String> cardTypeComboBox, accessLevelComboBox;
    private static final Map<String, String> cardAccessMap = new HashMap<>();

    public HotelAccessGUI() {
        super("Hotel Access Control System");
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); // Add spacing
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Card Management
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Card Type:"), gbc);
        gbc.gridx = 1;
        cardTypeComboBox = new JComboBox<>(new String[]{"Employee", "Visitor"});
        add(cardTypeComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Access Level:"), gbc);
        gbc.gridx = 1;
        accessLevelComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "All"});
        add(accessLevelComboBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Card ID:"), gbc);
        gbc.gridx = 1;
        newCardIdField = new JTextField(10);
        add(newCardIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        JButton addCardButton = new JButton("Add Card");
        add(addCardButton, gbc);
        gbc.gridwidth = 1;

        addCardButton.addActionListener(e -> addCard());

        // Access Request
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(new JLabel("Enter Card ID:"), gbc);
        gbc.gridx = 1;
        cardIdField = new JTextField(10);
        add(cardIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        add(new JLabel("Floor Number:"), gbc);
        gbc.gridx = 1;
        floorField = new JTextField(10);
        add(floorField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 6;
        add(new JLabel("Room Number:"), gbc);
        gbc.gridx = 1;
        roomField = new JTextField(10);
        add(roomField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        JButton requestAccessButton = new JButton("Request Access");
        add(requestAccessButton, gbc);

        requestAccessButton.addActionListener(e -> checkAccess());

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 400);
        setVisible(true);
    }

    private void addCard() {
        String cardType = (String) cardTypeComboBox.getSelectedItem();
        String accessLevel = (String) accessLevelComboBox.getSelectedItem();
        String cardId = newCardIdField.getText().trim();

        if (cardId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid Card ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        cardAccessMap.put(cardId, accessLevel);
        JOptionPane.showMessageDialog(this, "Card added successfully! ID: " + cardId + " - Access Level: " + accessLevel, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private void checkAccess() {
        String cardId = cardIdField.getText().trim();
        String floor = floorField.getText().trim();
        String room = roomField.getText().trim();

        if (cardId.isEmpty() || floor.isEmpty() || room.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter all fields!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String accessLevel = cardAccessMap.get(cardId);
        if (accessLevel == null) {
            JOptionPane.showMessageDialog(this, "Invalid Card ID!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean accessGranted = isAccessAllowed(accessLevel, Integer.parseInt(floor));
        String message = accessGranted ? "Access Granted! Floor " + floor + " Room " + room
                : "Access Denied! Floor " + floor + " Room " + room;
        JOptionPane.showMessageDialog(this, message, accessGranted ? "Success" : "Error", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean isAccessAllowed(String accessLevel, int floor) {
        return switch (accessLevel) {
            case "All" -> true;
            case "High" -> floor <= 10;
            case "Medium" -> floor <= 5;
            case "Low" -> floor <= 2;
            default -> false;
        };
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HotelAccessGUI::new);
    }
}
