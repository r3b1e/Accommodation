import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class HelpPage extends JFrame {

    public HelpPage() {
        // Set up the frame
        setTitle("Help - Smart PG Accommodation Platform");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        // Create a panel to hold the title and the close button
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(new Color(41, 128, 185)); // Blue background

        // Main title for the help page
        JLabel titleLabel = new JLabel("Help - Smart PG Accommodation Platform", JLabel.CENTER);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setOpaque(false); // Use the panel background color
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        // Close button on the right side of the title
        JButton closeButton = new JButton("X");
        closeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 16));
        closeButton.setForeground(Color.WHITE);
        closeButton.setBackground(new Color(231, 76, 60)); // Red background
        closeButton.setFocusPainted(false);
        closeButton.setPreferredSize(new Dimension(50, 50));
        closeButton.setBorder(BorderFactory.createEmptyBorder());
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the help page
            }
        });
        titlePanel.add(closeButton, BorderLayout.EAST);

        // Add the title panel to the top of the frame
        add(titlePanel, BorderLayout.NORTH);

        // Panel to hold help sections
        JPanel helpPanel = new JPanel();
        helpPanel.setLayout(new GridLayout(5, 1, 10, 10));
        helpPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Create sections with different colors
        helpPanel.add(createHelpSection("User Registration",
                "To register, click on 'Login In -> Sign Up' and fill in your details like name, email, and password.",
                new Color(46, 204, 113))); // Green

        helpPanel.add(createHelpSection("Login",
                "Enter your username and password in the login screen to access the platform.",
                new Color(231, 76, 60))); // Red

        helpPanel.add(createHelpSection("Search for PG",
                "Use the search bar to filter PG accommodations based on Name of an Institute.",
                new Color(230, 126, 34))); // Orange

        helpPanel.add(createHelpSection("Roommate Matching",
                "Find compatible roommates by specifying preferences.",
                new Color(155, 89, 182))); // Purple

        helpPanel.add(createHelpSection("Contact Support",
                "For any assistance, contact us at supportsmartpg@gmail.com or call +1234567890.",
                new Color(52, 152, 219))); // Light Blue

        // Add the help panel to a scroll pane for better navigation
        JScrollPane scrollPane = new JScrollPane(helpPanel);
        add(scrollPane, BorderLayout.CENTER);

        // Set the frame to be visible
        setVisible(true);
    }

    // Method to create a help section with a title, description, and background color
    private JPanel createHelpSection(String title, String description, Color bgColor) {
        JPanel sectionPanel = new JPanel();
        sectionPanel.setLayout(new BorderLayout());
        sectionPanel.setBackground(bgColor);
        sectionPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel titleLabel = new JLabel(title, JLabel.LEFT);
        titleLabel.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 18));
        titleLabel.setForeground(Color.WHITE);

        JTextArea descriptionArea = new JTextArea(description);
        descriptionArea.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 14));
        descriptionArea.setWrapStyleWord(true);
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);
        descriptionArea.setOpaque(false); // Transparent background

        sectionPanel.add(titleLabel, BorderLayout.NORTH);
        sectionPanel.add(descriptionArea, BorderLayout.CENTER);

        return sectionPanel;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HelpPage::new);
    }
}
