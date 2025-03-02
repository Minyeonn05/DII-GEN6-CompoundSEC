import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

    public AdminPanel(Map<String, Card> cardAccessMap, Map<String, List<String>> accessHistory) {
        super("Admin Panel");
        this.cardAccessMap = cardAccessMap;
        this.accessHistory = accessHistory;

        setLayout(new BorderLayout());

        // Create and add JTextArea for logs
        historyArea = new JTextArea();
        historyArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        // Load existing logs from file and update the panel
        loadLogs();

        // Create modification panel
        JPanel modifyPanel = new JPanel(new GridLayout(6, 2, 10, 10));

        modifyPanel.add(new JLabel("Card ID:"));
        cardIdField = new JTextField();
        modifyPanel.add(cardIdField);

        modifyPanel.add(new JLabel("New Name:"));
        newNameField = new JTextField();
        modifyPanel.add(newNameField);

        modifyPanel.add(new JLabel("New Access Level:"));
        accessLevelComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "All"});
        modifyPanel.add(accessLevelComboBox);

        // Add "Modify Card" button
        JButton modifyButton = new JButton("Modify Card");
        modifyButton.addActionListener(e -> modifyCard());
        modifyPanel.add(modifyButton);

        // Add Remove Card Button
        JButton removeCardButton = new JButton("Remove Card");
        removeCardButton.addActionListener(e -> removeCard());
        modifyPanel.add(removeCardButton);


        add(modifyPanel, BorderLayout.SOUTH);

        setSize(600, 500);
        setVisible(true);
        // Add Refresh History Button
        JButton refreshHistoryButton = new JButton("Refresh History");
        refreshHistoryButton.addActionListener(e -> {
            loadLogs();
            updateHistoryText();
        });
        add(refreshHistoryButton, BorderLayout.NORTH);

    }

    // ✅ Modify Card: Change name and access level
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

        // Restrict "Visitor" cards to "Low" access level
        if ("Visitor".equals(card.getName())) {
            card.setAccessLevel("Low");
            JOptionPane.showMessageDialog(this, "Visitors can only have Low access!", "Warning", JOptionPane.WARNING_MESSAGE);
        } else {
            card.setAccessLevel(newAccessLevel);
        }

        accessHistory.computeIfAbsent(cardId, k -> new ArrayList<>()).add(timestamp + " | Access Level changed to " + card.getAccessLevel());

        JOptionPane.showMessageDialog(this, "Modification Successful!", "Success", JOptionPane.INFORMATION_MESSAGE);

        // Update the panel and save logs
        updateHistoryText();
        saveHistoryToFile();
    }

    // ✅ Load logs from access_log.txt when Admin Panel opens
    private void loadLogs() {
        try (Scanner scanner = new Scanner(new File("access_log.txt"))) {
            StringBuilder logContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                logContent.append(scanner.nextLine()).append("\n");
            }
            historyArea.setText(logContent.toString()); // Display logs in JTextArea
        } catch (IOException e) {
            historyArea.setText("No previous logs found. Starting fresh...\n");
        }
    }

    // ✅ Update JTextArea with current access history
    private void updateHistoryText() {
        StringBuilder historyText = new StringBuilder(historyArea.getText()); // Keep current content

        for (var entry : cardAccessMap.entrySet()) {
            String cardId = entry.getKey();
            Card cardInfo = entry.getValue();

            if (!historyText.toString().contains("Card ID: " + cardId)) { // Avoid duplicates
                historyText.append("\nCard ID: ").append(cardId)
                        .append(" | Name: ").append(cardInfo.getName())
                        .append(" | Access Level: ").append(cardInfo.getAccessLevel())
                        .append(" | Card Type: ").append(cardInfo.getCardType())
                        .append("\n");

                if (accessHistory.containsKey(cardId)) {
                    for (String log : accessHistory.get(cardId)) {
                        historyText.append("   -> ").append(log).append("\n");
                    }
                }
            }
        }

        historyArea.setText(historyText.toString());
    }


    // ✅ Save current access history to access_log.txt
    private void saveHistoryToFile() {
        try (FileWriter writer = new FileWriter("access_log.txt", true)) {
            writer.write("=== Access Modification Log ===\n");

            for (var entry : cardAccessMap.entrySet()) {
                String cardId = entry.getKey();
                Card cardInfo = entry.getValue();

                writer.write("\nCard ID: " + cardId +
                        " | Name: " + cardInfo.getName() +
                        " | Access Level: " + cardInfo.getAccessLevel() +
                        " | Card Type: " + cardInfo.getCardType() +
                        "\n");

                if (accessHistory.containsKey(cardId)) {
                    for (String log : accessHistory.get(cardId)) {
                        writer.write("   -> " + log + "\n");
                    }
                }
            }
            writer.write("\n====================================\n");
            JOptionPane.showMessageDialog(this, "Log Saved Successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error Saving Log!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    private void removeCard() {
        String cardId = JOptionPane.showInputDialog(this, "Enter Card ID to Remove:");

        if (cardId == null || cardId.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Operation canceled. No Card ID provided.", "Warning", JOptionPane.WARNING_MESSAGE);
            return;
        }

        cardId = cardId.trim();

        if (!cardAccessMap.containsKey(cardId)) {
            JOptionPane.showMessageDialog(this, "Card ID not found!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Confirm before deletion
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to remove Card ID: " + cardId + "?",
                "Confirm Deletion", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Remove the card from the maps
            cardAccessMap.remove(cardId);
            accessHistory.remove(cardId);

            // Log the removal action
            logCardRemoval(cardId);

            // Update UI and log file
            updateHistoryText();
            saveHistoryToFile();

            JOptionPane.showMessageDialog(this, "Card successfully removed!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    private void logCardRemoval(String cardId) {
        try (FileWriter writer = new FileWriter("access_log.txt", true)) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write("Card ID: " + cardId + " | Removed at " + timestamp + "\n");
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error logging card removal!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

}
