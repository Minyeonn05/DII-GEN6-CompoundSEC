import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Date;


// CompoundSecurityGUI.java (แก้ไขและเพิ่มส่วนที่ขาดหายไป)
class CompoundSecurityGUI extends JFrame {
    private MultiFloorAccessControl accessControlSystem;
    private JTextField cardIdField, floorField, roomField;
    private JTextArea logArea;
    private JComboBox<String> cardTypeComboBox, accessLevelComboBox;

    public CompoundSecurityGUI() {
        super("Compound Security System");
        accessControlSystem = new MultiFloorAccessControl();

        // Card Management Panel
        JPanel cardPanel = new JPanel(new GridLayout(4, 2));
        JLabel cardTypeLabel = new JLabel("Card Type:");
        cardTypeComboBox = new JComboBox<>(new String[]{"Employee", "Visitor"});
        JLabel accessLevelLabel = new JLabel("Access Level:");
        accessLevelComboBox = new JComboBox<>(new String[]{"Low", "Medium", "High", "All"});
        JButton addCardButton = new JButton("Add Card");
        addCardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardType = (String) cardTypeComboBox.getSelectedItem();
                String accessLevel = (String) accessLevelComboBox.getSelectedItem();
                Date expiryDate = new Date();

                Card card;
                if (cardType.equals("Employee")) {
                    card = new EmployeeCard(accessLevel, expiryDate);
                } else {
                    card = new VisitorCard(accessLevel, expiryDate);
                }

                accessControlSystem.manageCard(card, "Add");
                JOptionPane.showMessageDialog(CompoundSecurityGUI.this, "Card added successfully!");
            }
        });

        cardPanel.add(cardTypeLabel);
        cardPanel.add(cardTypeComboBox);
        cardPanel.add(accessLevelLabel);
        cardPanel.add(accessLevelComboBox);
        cardPanel.add(new JLabel(""));
        cardPanel.add(addCardButton);

        // Access Control Panel
        JPanel accessPanel = new JPanel(new GridLayout(3, 2));
        JLabel cardIdLabel = new JLabel("Card ID:");
        cardIdField = new JTextField();
        JLabel floorLabel = new JLabel("Floor:");
        floorField = new JTextField();
        JLabel roomLabel = new JLabel("Room:");
        roomField = new JTextField();
        JButton grantButton = new JButton("Grant Access");
        grantButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardId = cardIdField.getText();
                String floor = floorField.getText();
                String room = roomField.getText();

                Card card = accessControlSystem.cardDatabase.get(cardId);
                if (card != null) {
                    accessControlSystem.grantAccess(card, floor, room);
                } else {
                    JOptionPane.showMessageDialog(CompoundSecurityGUI.this, "Card not found!");
                }
            }
        });
        JButton denyButton = new JButton("Deny Access");
        denyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cardId = cardIdField.getText();
                String floor = floorField.getText();
                String room = roomField.getText();

                Card card = accessControlSystem.cardDatabase.get(cardId);
                if (card != null) {
                    accessControlSystem.denyAccess(card, floor, room);
                } else {
                    JOptionPane.showMessageDialog(CompoundSecurityGUI.this, "Card not found!");
                }
            }
        });

        accessPanel.add(cardIdLabel);
        accessPanel.add(cardIdField);
        accessPanel.add(floorLabel);
        accessPanel.add(floorField);
        accessPanel.add(roomLabel);
        accessPanel.add(roomField);
        accessPanel.add(grantButton);
        accessPanel.add(denyButton);

        // Log Area
        logArea = new JTextArea(10, 20);
        JScrollPane logScrollPane = new JScrollPane(logArea);

        // Main frame setup
        setLayout(new BorderLayout());
        add(cardPanel, BorderLayout.NORTH);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CompoundSecurityGUI::new);
    }
}

