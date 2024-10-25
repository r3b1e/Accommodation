import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import javax.swing.border.Border;

public class Home extends JFrame {
    GridBagConstraints gbc = new GridBagConstraints();
    static String studentid;
    private JPanel sidePanel, contentPanel, headerPanel;
    private JButton optionsBtn, addRequestBtn, messageBtn, favoriteBtn, logoutBtn, exitBtn, searchBtn;
    private JLabel titleLabel;
    private final int SIDE_PANEL_WIDTH = 200;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    public Home() {
        setTitle("PG ROOM");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Create header panel
        createHeaderPanel();

        // Create content panel
        createContentPanel();

        // Create side panel with buttons from old UI
        createSidePanelWithOldButtons();

        feeddata();

        setVisible(true);
    }

    private void feeddata() {
        List<Student> list = getStudents();

        if (list.isEmpty()) {
            JLabel emptyLabel = new JLabel("No interested users found.");
            emptyLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
            contentPanel.add(emptyLabel);
        } else {
            gbc.insets = new Insets(10, 10, 10, 10);  // Set insets for spacing
            gbc.fill = GridBagConstraints.HORIZONTAL; // Ensure components stretch horizontally
            gbc.weightx = 1.0; // Give equal width to all components
            gbc.gridy = 0;  // Start at the first row

            int i = 1;
            for (Student std : list) {
//                JLabel messageLabel = new JLabel();
//                messageLabel.setText("<html>Request " + i + "<br>Name: " + std.firstName + "<br>Email: " + std.lastName + "<br>Mobile: " + std.number + "</html>");
//                messageLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));
//                messageLabel.setBackground(new Color(2, 21, 34));
//                messageLabel.setForeground(new Color(110, 172, 218));
//                messageLabel.setOpaque(true);  // Needed to show background color
//
//                // Apply rounded border to the label
//                messageLabel.setBorder(new RoundedBorder(30));  // Set radius to 20 for a rounded effect

                PGCard card = new PGCard(std.instituteName, std.address, "₹" + std.amount, std.roomType);
//                pgCards.add(card);

                // Add mouse listener to the label
                card.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        // Open new page with user details when clicked
                        new UserDetailsFrame(std);
                    }
                });

                // Set the position for each block
                // Alternate between 0 and 1 for two blocks per row
                contentPanel.add(card, gbc);  // Add the label to the content panel
gbc.gridx = i % 3;
                gbc.gridy = i / 3;
//                if (i % 2 == 0) {
//                    gbc.gridy++;  // Move to the next row after adding two blocks
//                }
                i++;
            }
        }

        // Wrap the content panel in a JScrollPane to enable scrolling
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Increase scroll speed
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUnitIncrement(16);  // Increase the scroll speed by setting a higher unit increment

        // Add the scroll pane to the frame
        this.add(scrollPane);
        revalidate();  // Refresh the UI
        repaint();
    }



    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(50, 50, 50));
        headerPanel.setPreferredSize(new Dimension(getWidth(), 50));

        // Create options button
        optionsBtn = new JButton("Options");
        optionsBtn.setPreferredSize(new Dimension(100, 30));
        optionsBtn.setBackground(new Color(200, 200, 200)); // Lighter background for contrast
        optionsBtn.setForeground(Color.BLACK); // Changed to black for visibility
        optionsBtn.setFocusPainted(false);
        optionsBtn.addActionListener(e -> toggleSidePanel());

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setOpaque(false);
        leftPanel.add(optionsBtn);

        // Create title label
        titleLabel = new JLabel("PG ROOMS");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

        // Create search button (top-right)
        searchBtn = new JButton("Search");
        searchBtn.setPreferredSize(new Dimension(100, 30));
        searchBtn.setBackground(new Color(200, 200, 200)); // Lighter background for contrast
        searchBtn.setForeground(Color.BLACK); // Changed to black for visibility
        searchBtn.setFocusPainted(false);
        searchBtn.addActionListener(e -> performSearch()); // Action for search

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setOpaque(false);
        rightPanel.add(searchBtn);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
    }

    private void createContentPanel() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setLayout(new GridBagLayout()); // Use GridBagLayout instead of BorderLayout
        add(contentPanel, BorderLayout.CENTER);
    }


    private void createSidePanelWithOldButtons() {
        sidePanel = new JPanel();
        sidePanel.setPreferredSize(new Dimension(SIDE_PANEL_WIDTH, getHeight()));
        sidePanel.setBackground(new Color(50, 50, 50));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));

        addRequestBtn = createSideButton("Make Request", "src/images/Addrequest.png");
        messageBtn = createSideButton("Message", "src/images/Message.png");
        favoriteBtn = createSideButton("Favorite", "src/images/Favorite.png");
        logoutBtn = createSideButton("Logout", "src/images/Logout.png");
        exitBtn = createSideButton("Exit", "src/images/Exit.png");

        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(addRequestBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(messageBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(favoriteBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(logoutBtn);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(exitBtn);
        sidePanel.add(Box.createVerticalGlue());

        sidePanel.setVisible(false);
        add(sidePanel, BorderLayout.WEST);
    }

    private JButton createSideButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setIcon(resizeIcon(new ImageIcon(iconPath), 24, 24));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(70, 70, 70));
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(180, 40));
        button.addActionListener(this::handleButtonAction);
        return button;
    }

    private void toggleSidePanel() {
        sidePanel.setVisible(!sidePanel.isVisible());
        revalidate();
        repaint();
    }

    private void handleButtonAction(ActionEvent e) {
        if (e.getSource() == addRequestBtn) {
            new Addrequest("738126");
        } else if (e.getSource() == messageBtn) {
            // message logic here
        } else if (e.getSource() == favoriteBtn) {
            // Favorite logic here
        } else if (e.getSource() == logoutBtn) {
            dispose();
            new login();
        } else if (e.getSource() == exitBtn) {
            System.exit(0);
        }
    }

    private void performSearch() {
        // Logic for search button action
        JOptionPane.showMessageDialog(this, "Search button clicked");
    }

    private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }


    class RoundedBorder implements Border {
        private int radius;

        RoundedBorder(int radius) {
            this.radius = radius;
        }

        public Insets getBorderInsets(Component c) {
            return new Insets(this.radius + 10, this.radius + 1, this.radius + 1, this.radius + 1);
        }

        public boolean isBorderOpaque() {
            return true;
        }

        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            g.setColor(Color.GRAY); // You can set the border color here
            g.drawRoundRect(x, y, width - 1, height - 1, radius, radius); // Drawing a round rectangle
        }
    }
// -----------------------------Start for the table replacing ----------------------------------------------
    //________________________________for adding in page _____________________________________________________
    class Student {
        String studentID, requestId, firstName, lastName, instituteName, number, gender, amount, address, vacancy, roomType, age, distance, tellAbout;

        public Student(String studentID, String requestId, String firstName, String lastName, String instituteName, String number, String gender, String amount, String address,
                       String vacancy, String roomType, String age, String distance, String tellAbout) {
            this.studentID = studentID;
            this.requestId = requestId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.instituteName = instituteName;
            this.number = number;
            this.gender = gender;
            this.amount = amount;
            this.address = address;
            this.vacancy = vacancy;
            this.roomType = roomType;
            this.age = age;
            this.distance = distance;
            this.tellAbout = tellAbout;
        }
    }
    //-------------------------------------taking data from the db--------------------------------------------------
    public List<Student> getStudents() {
        List<Student> students = new ArrayList<>();
        try {
            new dbconnect();
            Connection connection = dbconnect.connection; // Assuming dbconnect.connection is already available
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

            while (resultSet.next()) {
                String studentID = resultSet.getString("studentid");
                String requestId = resultSet.getString("id");
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String instituteName = resultSet.getString("institute_name");
                String number = resultSet.getString("mobile_no");
                String gender = resultSet.getString("gender");
                String amount = resultSet.getString("amount");

                // Summarizing the address
                String streetName = resultSet.getString("street_name");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String pinCode = resultSet.getString("pin_code");
                String address = streetName + ", " + city + ", " + state + " - " + pinCode;

                String vacancy = resultSet.getString("vacancy");
                String roomType = resultSet.getString("room_typr"); // Fix typo to "room_type"
                String age = resultSet.getString("age");
                String distance = resultSet.getString("distance_college");
                String tellAbout = resultSet.getString("tell_about");

                // Add student data to the list
                students.add(new Student(studentID,requestId, firstName, lastName, instituteName, number, gender, amount, address, vacancy, roomType, age, distance, tellAbout));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }

//    --------------------------------------new page which containing full information-----------------------------------

    static class UserDetailsFrame extends JFrame {
        UserDetailsFrame(Student std) {
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            this.setSize(300, 200); // Frame size
            this.setTitle("User Details");

            JPanel panel = new JPanel(new GridLayout(3, 1));
            JLabel nameLabel = new JLabel("Name: " + std.firstName);
            JLabel emailLabel = new JLabel("Email: " + std.lastName);
            JLabel mobileLabel = new JLabel("Mobile: " + std.number);

            nameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));
            emailLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));
            mobileLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

            panel.add(nameLabel);
            panel.add(emailLabel);
            panel.add(mobileLabel);

            this.add(panel);
            this.setVisible(true);
        }
    }
//------------------------------------abhi---------------------------------
private class PGCard extends JPanel {
    public PGCard(String name, String location, String price, String roomType) {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(280, 200));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(189, 195, 199), 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Arial", Font.BOLD, 18));
        nameLabel.setForeground(new Color(41, 128, 185));

        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 5));
        infoPanel.setOpaque(false);
        infoPanel.add(createInfoLabel("Location: " + location, new Color(52, 152, 219)));
        infoPanel.add(createInfoLabel("Price: " + price, new Color(46, 204, 113)));
        infoPanel.add(createInfoLabel("Room Type: " + roomType, new Color(230, 126, 34)));

        // Changed button text from "View Details" to "Interested"
        JButton interestedButton = createGradientButton("Interested", primaryColor, secondaryColor);
        interestedButton.setPreferredSize(new Dimension(280, 30));

        add(nameLabel, BorderLayout.NORTH);
        add(infoPanel, BorderLayout.CENTER);
        add(interestedButton, BorderLayout.SOUTH);
    }

    private JLabel createInfoLabel(String text, Color color) {
        JLabel label = new JLabel(text);
        label.setForeground(color);
        label.setFont(new Font("Arial", Font.PLAIN, 14));
        return label;
    }
}


//created gradient button-------------------------------------------------------------

    private JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setPaint(new GradientPaint(
                        new Point(0, 0),
                        color1,
                        new Point(0, getHeight()),
                        color2));
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.dispose();

                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(false);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        return button;
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Home());
    }
}