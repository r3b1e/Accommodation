import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

class Interested {
    String NAME, EMAIL, MOBILE;

    public Interested(String NAME, String EMAIL, String MOBILE) {
        this.NAME = NAME;
        this.EMAIL = EMAIL;
        this.MOBILE = MOBILE;
    }
}

public class message extends JFrame {
    GridBagConstraints gbc = new GridBagConstraints();

    message(String request) {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 500);  // Increased frame size
        this.setTitle("PG ROOM");
        this.setResizable(false);

        // Panel to hold the content that will be scrollable
        JPanel contentPanel = new JPanel(new GridBagLayout());

        // Fetch the list of interested users
        List<Interested> list = addList(request);

        if (list.isEmpty()) {
            JLabel emptyLabel = new JLabel("No interested users found.");
            emptyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            contentPanel.add(emptyLabel);
        } else {
            gbc.insets = new Insets(10, 10, 10, 10);  // Increased spacing
            gbc.gridx = 0;
            gbc.gridy = 0;  // Initialize the Y position
            int i = 1;
            for (Interested interested : list) {
                JLabel messageLabel = new JLabel();

                // Set dynamic text from the Interested object
                messageLabel.setText("<html>Request " + i + "<br>Name: " + interested.NAME + "<br>Email: " + interested.EMAIL + "<br>Mobile: " + interested.MOBILE + "</html>");
                messageLabel.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));  // Increased font size
                messageLabel.setBackground(new Color(52, 152, 219));  // Blue background
                messageLabel.setForeground(new Color(255, 255, 255));
                messageLabel.setOpaque(true);  // Needed to show background color

                // Set a larger minimum size for the label to make it larger
                messageLabel.setPreferredSize(new Dimension(300, 100));  // Adjust block size
                messageLabel.setBorder(BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(Color.GRAY, 3),  // Increased border thickness
                        BorderFactory.createEmptyBorder(30, 30, 30, 30)));  // Increased padding inside the block

                // Add mouse listener to the label
                messageLabel.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Open new page with user details when clicked
                        new UserDetailsFrame(interested);
                    }
                });

                gbc.gridy++;  // Move to the next row
                contentPanel.add(messageLabel, gbc);  // Add the label to the content panel
                i++;
            }
        }

        // Wrap the content panel in a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Increase scroll speed
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);  // Increase the scroll speed by setting a higher unit increment

        // Close button at the bottom of the window
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 14));
        closeButton.setBackground(new Color(6, 221, 0));
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> this.dispose());  // Close the window on click

        // Panel to hold the button
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);

        // Adding both the scroll pane and button panel to the frame
        this.add(scrollPane, BorderLayout.CENTER);
        this.add(buttonPanel, BorderLayout.SOUTH);

        // Center the frame on the screen
        this.setLocationRelativeTo(null);

        this.setVisible(true);
    }

    private List<Interested> addList(String request) {
        List<Interested> list = new ArrayList<>();
        try {
            new dbconnect();
            Connection connection = dbconnect.connection;

            // Fetch the message for the user with ID '789897'
            try (PreparedStatement ps = connection.prepareStatement("select * from user where userid = ?")) {
                ps.setString(1, request);
                try (ResultSet resultSet = ps.executeQuery()) {
                    if (resultSet.next()) {
                        String message = resultSet.getString("message");
                        int i = 0;
                        int len = message.length();
                        if (Objects.equals(len, "null")) {
                            return list;
                        }
                        while (i < len) {
                            if (message.charAt(i) == '*') {
                                i++;
                            } else {
                                // Ensure we have enough characters to form a valid substring
                                if (i + 6 <= len) {
                                    String valid = message.substring(i, i + 6);

                                    // Fetch details for the 'valid' user ID
                                    try (PreparedStatement ps2 = connection.prepareStatement("select * from user where userid = ?")) {
                                        ps2.setString(1, valid);
                                        try (ResultSet resultSet2 = ps2.executeQuery()) {
                                            if (resultSet2.next()) {
                                                String Name = resultSet2.getString("username");
                                                String Email = resultSet2.getString("email");
                                                String Mobile = resultSet2.getString("mobileno");
                                                list.add(new Interested(Name, Email, Mobile));
                                            }
                                        }
                                    }
                                }
                                i += 6;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // New class for the user details frame
    static class UserDetailsFrame extends JFrame {
        UserDetailsFrame(Interested interested) {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(300, 200); // Increased frame size
            this.setTitle("User Details");

            JPanel panel = new JPanel(new GridLayout(4, 1));
            JLabel nameLabel = new JLabel("Name: " + interested.NAME);
            JLabel emailLabel = new JLabel("Email: " + interested.EMAIL);
            JLabel mobileLabel = new JLabel("Mobile: " + interested.MOBILE);

            nameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 18));  // Increased font size
            emailLabel.setFont(new Font(Font.SERIF, Font.BOLD, 18));  // Increased font size
            mobileLabel.setFont(new Font(Font.SERIF, Font.BOLD, 18));  // Increased font size

            panel.add(nameLabel);
            panel.add(emailLabel);
            panel.add(mobileLabel);

            // Close button to close this frame
            JButton closeButton = new JButton("Close");
            closeButton.addActionListener(e -> this.dispose());  // Close on click
            panel.add(closeButton);

            this.add(panel);
            this.setLocationRelativeTo(null);  // Center the frame on screen
            this.setVisible(true);
        }
    }

    public static void main(String[] args) {
        new message("789897");
    }
}
