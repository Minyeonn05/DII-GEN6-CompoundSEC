import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AdminPanel extends JFrame {
    private Map<String, HotelAccessGUI.CardInfo> cardAccessMap;
    private Map<String, List<String>> accessHistory;
    private JTextArea historyArea;
    private JTextField cardIdField, newNameField;
    private JComboBox<String> accessLevelComboBox;
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AdminPanel(Map<String, HotelAccessGUI.CardInfo> cardAccessMap, Map<String, List<String>> accessHistory) {
        super("Admin Panel");
        this.cardAccessMap = cardAccessMap;
        this.accessHistory = accessHistory;

        setLayout(new BorderLayout());

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        updateHistoryText();

        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new GridLayout(4, 2, 10, 10));

        modifyPanel.add(new JLabel("Card ID:"));
        cardIdField = new JTextField();
        modifyPanel.add(cardIdField);

        modifyPanel.add(new JLabel("New Name:"));
        newNameField = new JTextField();
        modifyPanel.add(newNameField);

        modifyPanel.add(new JLabel("New Access Level:"));
        accessLevelComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "All"});
        modifyPanel.add(accessLevelComboBox);

        JButton modifyButton = new JButton("Modify Card");
        modifyButton.addActionListener(e -> modifyCard());

        modifyPanel.add(modifyButton);
        add(modifyPanel, BorderLayout.SOUTH);

        setSize(600, 500);
        setVisible(true);
    }

    private void modifyCard() {
        String cardId = cardIdField.getText().trim();
        String newName = newNameField.getText().trim();
        String newAccessLevel = (String) accessLevelComboBox.getSelectedItem();

        if (!cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Card ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        HotelAccessGUI.CardInfo card = cardAccessMap.get(cardId);
        String timestamp = dateFormat.format(new Date()); // Capture timestamp

        // Allow all users to change the name
        if (!newName.isEmpty()) {
            String logEntry = timestamp + " | Name changed from " + card.getName() + " to " + newName;
            card.setName(newName);
            accessHistory.computeIfAbsent(cardId, k -> new ArrayList<>()).add(logEntry);
        }

        // Check if the current user is an admin before changing access level
        String adminId = JOptionPane.showInputDialog(this, "Enter Admin ID (leave blank if not admin):");

        if (adminId != null && adminId.equals(Config.ADMIN_ID)) {
            String logEntry = timestamp + " | Access Level changed from " + card.getAccessLevel() + " to " + newAccessLevel;
            card.setAccessLevel(newAccessLevel);
            accessHistory.computeIfAbsent(cardId, k -> new ArrayList<>()).add(logEntry);
            JOptionPane.showMessageDialog(this, "Access level updated successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else if (adminId != null) {
            JOptionPane.showMessageDialog(this, "You are not authorized to change access level!", "Error", JOptionPane.WARNING_MESSAGE);
        }

        updateHistoryText();
    }

    private void updateHistoryText() {
        StringBuilder historyText = new StringBuilder("=== Access Modification Log ===\n");

        for (var entry : cardAccessMap.entrySet()) {
            String cardId = entry.getKey();
            HotelAccessGUI.CardInfo cardInfo = entry.getValue();

            historyText.append("\nCard ID: ").append(cardId)
                    .append(" | Name: ").append(cardInfo.getName())
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

}
