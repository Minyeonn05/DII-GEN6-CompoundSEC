import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class AdminPanel extends JFrame {
    private MultiFloorAccessControl accessControlSystem;

    public AdminPanel(MultiFloorAccessControl accessControlSystem) {
        super("Admin Panel");
        this.accessControlSystem = accessControlSystem;

        JTextArea logArea = new JTextArea(15, 30);
        logArea.setEditable(false);

        JButton refreshButton = new JButton("Refresh Log");
        refreshButton.addActionListener((ActionEvent e) -> {
            logArea.setText(""); // Clear existing log
            for (String log : accessControlSystem.getEventLog()) {
                logArea.append(log + "\n");
            }
        });

        JPanel panel = new JPanel();
        panel.add(refreshButton);

        setLayout(new BorderLayout());
        add(new JScrollPane(logArea), BorderLayout.CENTER);
        add(panel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(400, 300);
        setVisible(true);
    }
}
