import javax.swing.*;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

public class AdminPanel extends JFrame {
    private final Map<String, Card> cardAccessMap;
    private final Map<String, List<String>> accessHistory;
    private final JTextArea historyArea;
    private final JTextField cardIdField, newNameField;
    private final JComboBox<String> accessLevelComboBox;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public AdminPanel(Map<String, Card> cardAccessMap,
                      Map<String, List<String>> accessHistory) {
        super("Admin Panel");
        this.cardAccessMap = cardAccessMap;
        this.accessHistory = accessHistory;

        setLayout(new BorderLayout());

        historyArea = new JTextArea();
        historyArea.setEditable(false);
        updateHistoryText();

        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        JPanel modifyPanel = new JPanel(new GridLayout(5, 2, 10, 10));

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
        modifyButton.setEnabled(true);
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

        Card card = cardAccessMap.get(cardId);
        String timestamp = dateFormat.format(new Date());

        if (!newName.isEmpty()) {
            card.setName(newName);
            accessHistory.computeIfAbsent(cardId, k -> new ArrayList<>()).add(timestamp + " | Name changed to " + newName);
        }

        if ("Visitor".equals(card.getName())) {
            card.setAccessLevel("Low");
            JOptionPane.showMessageDialog(this, "Visitors can only have Low access!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            card.setAccessLevel(newAccessLevel);
        }

        accessHistory.computeIfAbsent(cardId, k -> new ArrayList<>()).add(timestamp + " | Access Level changed to " + card.getAccessLevel());
        JOptionPane.showMessageDialog(this, "Modification Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);
        updateHistoryText();
    }

    private void updateHistoryText() {
        StringBuilder historyText = new StringBuilder("=== Access Modification Log ===\n");

        for (var entry : cardAccessMap.entrySet()) {
            String cardId = entry.getKey();
            Card cardInfo = entry.getValue();


            historyText.append("\nCard ID: ").append(cardId)
                    .append(" | Name: ").append(cardInfo.getName())
                    .append(" | Access Level: ").append(cardInfo.getAccessLevel())
                    .append(" | Card Type: ").append(cardInfo.getCardType()) // Include card type
                    .append("\n");

            // Include modification history for this card
            if (accessHistory.containsKey(cardId)) {
                for (String log : accessHistory.get(cardId)) {
                    historyText.append("   -> ").append(log).append("\n");
                }
            }
        }

        historyArea.setText(historyText.toString());
    }
}

