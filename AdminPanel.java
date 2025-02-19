import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;


public class AdminPanel extends JFrame {
    public AdminPanel(Map<String, HotelAccessGUI. CardInfo> cardAccessMap, Map<String, List<String>> accessHistory) {
        super("Admin Panel");
        setLayout(new BorderLayout());

        JTextArea historyArea = new JTextArea();
        historyArea.setEditable(false);
        updateHistoryText(historyArea, cardAccessMap, accessHistory);

        JScrollPane scrollPane = new JScrollPane(historyArea);
        add(scrollPane, BorderLayout.CENTER);

        JButton clearHistoryButton = new JButton("Clear History");
        clearHistoryButton.addActionListener(e -> {
            accessHistory.clear();
            updateHistoryText(historyArea, cardAccessMap, accessHistory);
            JOptionPane.showMessageDialog(this, "Access history cleared!", "Success", JOptionPane.INFORMATION_MESSAGE);
        });

        add(clearHistoryButton, BorderLayout.SOUTH);

        setSize(500, 400);
        setVisible(true);
    }

    private void updateHistoryText(JTextArea historyArea, Map<String, HotelAccessGUI.CardInfo> cardAccessMap, Map<String, List<String>> accessHistory) {
        StringBuilder historyText = new StringBuilder();
        for (var entry : cardAccessMap.entrySet()) {
            String cardId = entry.getKey();
            HotelAccessGUI.CardInfo cardInfo = entry.getValue();
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
}
