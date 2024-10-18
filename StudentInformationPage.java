import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.swing.border.EmptyBorder;
import java.awt.geom.RoundRectangle2D;
import java.util.Set;

public class StudentInformationPage extends JFrame {
    private JPanel headerPanel, searchPanel, filterPanel;
    private JPanel contentPanel = new JPanel(new GridBagLayout());
    private JScrollPane scrollPane = new JScrollPane();

    private JTextField searchField;
    private JButton searchButton, filterButton, messageButton, profileButton, menuButton, addRequestButton;
    private JComboBox<String> locationFilter, priceFilter, roomTypeFilter;
    private List<PGCard> pgCards = new ArrayList<>();
    private JPanel slidePanel;
    private Timer slideTimer;
    private boolean isPanelVisible = false;
    private final int SLIDE_SPEED = 10;
    private final int PANEL_WIDTH = 200;
    private Color primaryColor = new Color(41, 128, 185);
    private Color secondaryColor = new Color(52, 152, 219);
    private Color accentColor = new Color(230, 126, 34);
    private String studentid;
    private String OwnerId = "123";
    private JComboBox<String> instituteComboBox;
    private List<String> allInstitutes;
    private List<Student> allStudents;
    List<Student> list = getStudents();

    public StudentInformationPage(String studentId) {
        System.out.println("Starting StudentInformationPage constructor");
        this.studentid = studentId;
        setTitle("PG Accommodation - Home");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false); // Prevent window resizing

        createHeaderPanel();
        createSearchPanel();
        createFilterPanel();
        createContentPanel();
        createSlidePanel();

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(searchPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(slidePanel, BorderLayout.WEST);
        slidePanel.setVisible(false);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createHeaderPanel() {
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(primaryColor);
        headerPanel.setPreferredSize(new Dimension(1000, 60));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        leftPanel.setOpaque(false);
        menuButton = createGradientButton("☰ Menu", primaryColor, secondaryColor);
        menuButton.addActionListener(e -> toggleSlidePanel());
        leftPanel.add(menuButton);

        JLabel titleLabel = new JLabel("PG Accommodation");
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        rightPanel.setOpaque(false);
        addRequestButton = createGradientButton("Add Request", primaryColor, secondaryColor);
        addRequestButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Add Request button clicked"); // Debug print
                openAddRequestPage();
            }
        });
        rightPanel.add(addRequestButton);

        headerPanel.add(leftPanel, BorderLayout.WEST);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        headerPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void toggleSlidePanel() {
        if (slideTimer != null && slideTimer.isRunning()) {
            slideTimer.stop();
        }

        slideTimer = new Timer(5, new ActionListener() {
            int width = isPanelVisible ? PANEL_WIDTH : 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                if (isPanelVisible) {
                    width -= SLIDE_SPEED;
                    if (width <= 0) {
                        width = 0;
                        isPanelVisible = false;
                        ((Timer)e.getSource()).stop();
                        slidePanel.setVisible(false);
                    }
                } else {
                    width += SLIDE_SPEED;
                    if (width >= PANEL_WIDTH) {
                        width = PANEL_WIDTH;
                        isPanelVisible = true;
                        ((Timer)e.getSource()).stop();
                    }
                }
                slidePanel.setPreferredSize(new Dimension(width, getHeight()));
                slidePanel.revalidate();
                repaint();
            }
        });

        if (!isPanelVisible) {
            slidePanel.setVisible(true);
        }
        slideTimer.start();
    }

    private void createSearchPanel() {
        searchPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchPanel.setBackground(secondaryColor);
        searchPanel.setPreferredSize(new Dimension(getWidth(), 60));

        searchField = new JTextField(30);
        searchField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(primaryColor, 2),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));


        // Get unique institute names
//        List<Student> list = getStudents();
        Set<String> institutes = new HashSet<>();
        institutes.add(""); // Placeholder for ComboBox
        for (Student student : list) {
            institutes.add(student.instituteName);
        }

        // Create ComboBox for institute names with a placeholder and increase the width
        instituteComboBox = new JComboBox<>(institutes.toArray(new String[0]));
        instituteComboBox.setRenderer(new mypage.ComboBoxRenderer("Select Institute"));
        instituteComboBox.setSelectedIndex(-1); // Ensure placeholder is displayed initially
        instituteComboBox.setPreferredSize(new Dimension(250, 30)); // Set preferred width of the ComboBox

        searchButton = createGradientButton("Search", new Color(46, 204, 113), new Color(39, 174, 96));
        filterButton = createGradientButton("Filters", new Color(230, 126, 34), new Color(211, 84, 0));

//        searchPanel.add(searchField);
        searchPanel.add(instituteComboBox);
        searchPanel.add(searchButton);
//        searchPanel.add(filterButton);

        filterButton.addActionListener(e -> toggleFilterPanel());
        searchButton.addActionListener(e -> createContentPanel());

    }

    private void createFilterPanel() {
        filterPanel = new JPanel();
        filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.Y_AXIS));
        filterPanel.setBackground(new Color(236, 240, 241));
        filterPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel filterLabel = new JLabel("Filters");
        filterLabel.setFont(new Font("Arial", Font.BOLD, 18));

        locationFilter = new JComboBox<>(new String[]{"All Locations", "Location 1", "Location 2", "Location 3"});
        priceFilter = new JComboBox<>(new String[]{"All Prices", "Under $500", "$500 - $1000", "Over $1000"});
        roomTypeFilter = new JComboBox<>(new String[]{"All Types", "Single", "Double", "Triple"});

        filterPanel.add(filterLabel);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        filterPanel.add(new JLabel("Location:"));
        filterPanel.add(locationFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(new JLabel("Price Range:"));
        filterPanel.add(priceFilter);
        filterPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        filterPanel.add(new JLabel("Room Type:"));
        filterPanel.add(roomTypeFilter);

        filterPanel.setVisible(false);
    }

    private void createContentPanel() {
        System.out.println("createContentPanel started");

        // Clear existing cards and components from the contentPanel
        pgCards.clear();
        contentPanel.removeAll();

        // Set up the layout and background
        contentPanel.setLayout(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        // Determine which list to use (filtered or original)
        List<Student> filterlist;
        String selectedInstitute = (String) instituteComboBox.getSelectedItem();
        if (selectedInstitute == null || selectedInstitute.equals("")) {
            filterlist = list;
        } else {
            filterlist = filterdate();
        }

        // Iterate over the filtered list and create PG cards
        int i = 0;
        for (Student std : filterlist) {
            OwnerId = std.studentID;
            String name = std.instituteName;
            String location = std.address;
            String price = "₹" + std.amount;
            String roomType = std.roomType;
            String institute = std.instituteName;
            String distance = std.distance + " km";
            String vacancy = std.vacancy;
            String gender = std.gender;
            String additionalAddress = std.addadress;
            String aboutSelf = std.tellAbout;
            String phoneNumber = std.number;
            String email = std.email;

            // Create a new PGCard and add it to the list
            PGCard card = new PGCard(OwnerId, name, location, price, roomType, institute, distance, vacancy, gender, additionalAddress, aboutSelf, phoneNumber, email);
            pgCards.add(card);

            // Add the card to the contentPanel using GridBagLayout
            gbc.gridx = i % 3;
            gbc.gridy = i / 3;
            contentPanel.add(card, gbc);
            i++;
        }
        scrollPane.getVerticalScrollBar().setUnitIncrement(32);
        // Refresh the contentPanel and scrollPane to reflect changes
        contentPanel.revalidate();
        contentPanel.repaint();
        scrollPane.setViewportView(contentPanel);
        scrollPane.revalidate();
        scrollPane.repaint();

        System.out.println("createContentPanel finished");
    }



    private List<Student> filterdate(){
        String selectedInstitute = (String) instituteComboBox.getSelectedItem();
            List<Student> filteredStudents = new ArrayList<>();
            for (Student student : list) {
                // Check for null instituteName before comparing
                if (student.instituteName != null && student.instituteName.equals(selectedInstitute)) {
                    filteredStudents.add(student);
                }
        }
            return filteredStudents;
    }

    private void toggleFilterPanel() {
        if (filterPanel.isVisible()) {
            remove(filterPanel);
            filterPanel.setVisible(false);
        } else {
            add(filterPanel, BorderLayout.WEST);
            filterPanel.setVisible(true);
        }
        revalidate();
        repaint();
    }

    private void createSlidePanel() {
        slidePanel = new JPanel();
        slidePanel.setLayout(new BoxLayout(slidePanel, BoxLayout.Y_AXIS));
        slidePanel.setBackground(new Color(44, 62, 80));
        slidePanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        slidePanel.setPreferredSize(new Dimension(0, getHeight()));

        String[] options = {"Help", "Messages","Delete","Log In"};
        for (String option : options) {
            JButton button = createGradientButton(option, primaryColor, secondaryColor);
            button.setAlignmentX(Component.LEFT_ALIGNMENT);
            button.setMaximumSize(new Dimension(Integer.MAX_VALUE, button.getPreferredSize().height));
            button.addActionListener(e -> handleSlideMenuOption(option));
            slidePanel.add(button);
            slidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        }

        slidePanel.setVisible(false);
    }

    private void handleSlideMenuOption(String option) {
        switch (option) {
            case "Help":
                // Implement help functionality
                JOptionPane.showMessageDialog(this, "Help functionality to be implemented.", "Help", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Messages":
                // Implement messages functionality
                if(studentid == "xxxx"){
                    JOptionPane.showMessageDialog(this, "Please Login First.", "Messages", JOptionPane.INFORMATION_MESSAGE);
                }
                else {
                    new message(studentid);
                }
//                JOptionPane.showMessageDialog(this, "Messages functionality to be implemented.", "Messages", JOptionPane.INFORMATION_MESSAGE);
                break;
            case "Delete":
                if(studentid == "xxxx"){
                    JOptionPane.showMessageDialog(this, "Please Login First.", "Messages", JOptionPane.INFORMATION_MESSAGE);
                }
                // Implement help functionality
//
//                JOptionPane.showMessageDialog(this, "Help functionality to be implemented.", "Help", JOptionPane.INFORMATION_MESSAGE);
                else {
                    new mypage(studentid);
                }
                break;
            case "Log In":
                // Implement logout functionality
                if(studentid =="xxxx"){
                    dispose();
                    new login();
                }
            else{
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to log out?", "Log Out", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    // Perform logout actions here
                    dispose();
                    new login(); // Assuming you have a Login class to return to the login screen
                }
            }
                break;
        }
    }

    private JButton createGradientButton(String text, Color color1, Color color2) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                int w = getWidth();
                int h = getHeight();
                GradientPaint gp = new GradientPaint(0, 0, color1, 0, h, color2);
                g2.setPaint(gp);
                g2.fillRoundRect(0, 0, w, h, 15, 15);
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

    private class PGCard extends JPanel {
        private String OwnerId, name, location, price, roomType, institute, distance, vacancy, gender, additionalAddress, aboutSelf, phoneNumber, email;

        public PGCard(String OwnerId, String name, String location, String price, String roomType, String institute, String distance, String vacancy, String gender, String additionalAddress, String aboutSelf, String phoneNumber, String email) {
            this.OwnerId = OwnerId;
            this.name = name;
            this.location = location;
            this.price = price;
            this.roomType = roomType;
            this.institute = institute;
            this.distance = distance;
            this.vacancy = vacancy;
            this.gender = gender;
            this.additionalAddress = additionalAddress;
            this.aboutSelf = aboutSelf;
            this.phoneNumber = phoneNumber;
            this.email = email;

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

            JPanel infoPanel = new JPanel(new GridLayout(4, 1, 5, 5));
            infoPanel.setOpaque(false);
            infoPanel.add(createInfoLabel("Location: " + location, new Color(52, 152, 219)));
            infoPanel.add(createInfoLabel("Price: " + price, new Color(46, 204, 113)));
            infoPanel.add(createInfoLabel("Room Type: " + roomType, new Color(230, 126, 34)));
            infoPanel.add(createInfoLabel("Distance: " + distance + " km", new Color(155, 89, 182)));

            JButton interestedButton = createGradientButton("Show Details", primaryColor, secondaryColor);
            interestedButton.addActionListener(e -> showDetailedPopup());

            add(nameLabel, BorderLayout.NORTH);
            add(infoPanel, BorderLayout.CENTER);
            add(interestedButton, BorderLayout.SOUTH);
        }

        private void showDetailedPopup() {
            if(studentid == "xxxx"){
                JOptionPane.showMessageDialog(this, "Please Login First.", "Messages", JOptionPane.INFORMATION_MESSAGE);
            return;
            }
            JDialog popup = new JDialog(StudentInformationPage.this, "PG Details", true);
            popup.setSize(550, 500);

            // Center the popup on the screen jamdade
            popup.setLocationRelativeTo(null);

            JPanel contentPanel = new JPanel();
            contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
            contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
            contentPanel.setBackground(new Color(240, 248, 255)); // Light blue background

            JLabel titleLabel = new JLabel(name);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            titleLabel.setForeground(new Color(41, 128, 185)); // Blue color
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            JPanel detailsPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            detailsPanel.setOpaque(false);
            detailsPanel.add(createDetailLabel("Location", location));
            detailsPanel.add(createDetailLabel("Price", price));
            detailsPanel.add(createDetailLabel("Room Type", roomType));
            detailsPanel.add(createDetailLabel("Institute", institute));
            detailsPanel.add(createDetailLabel("Distance", distance));
            detailsPanel.add(createDetailLabel("Vacancy", vacancy));
            detailsPanel.add(createDetailLabel("Gender", gender));
            detailsPanel.add(createDetailLabel("Phone", phoneNumber));
            detailsPanel.add(createDetailLabel("Email", email));
            detailsPanel.add(createDetailLabel("Additional Address", additionalAddress));
//            System.out.println(OwnerId);

            JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
            buttonPanel.setOpaque(false);

            JButton interestedButton = createGradientButton("Interested", new Color(46, 204, 113), new Color(39, 174, 96));
            interestedButton.addActionListener(e -> {
//------------------------------------------------------------
                String studentId = OwnerId;  // Assuming ID is in the first column
//            storeStudentIdInDatabase(studentId, "interested");
                try{
                    new dbconnect();
                    Connection connection = dbconnect.connection; // Assuming dbconnect.connection is already available
                    Statement statement = connection.createStatement();
                    ResultSet resultset = statement.executeQuery("select * from user");
                    String str = "";
                    while (resultset.next()) {
                        if(resultset.getString("userid").equals(studentId)){
                            str = resultset.getString("message");
                            break;
                        }
                    }
                    if(str.equals("null")){
                        str = "";
                    }
                    str = str+"*"+studentid;
                    PreparedStatement pre = connection.prepareStatement(
                            "UPDATE user SET message = ? WHERE userid = ?"
                    );
                    pre.setString(1, str); // Set the new value for the column
                    pre.setString(2, studentId); // Set the condition value
                    pre.executeUpdate();
//                    JOptionPane.showMessageDialog(this, "Request send Sucessfully");
                }
                catch (SQLException E) {
                    E.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error While sending the Request");
                }


//            else {
//                JOptionPane.showMessageDialog(this, "Please select a student first.");
//            }

                JOptionPane.showMessageDialog(popup, "You've expressed interest in this PG. The owner will be notified.", "Interest Registered", JOptionPane.INFORMATION_MESSAGE);
            });

            JButton closeButton = createGradientButton("Close", new Color(231, 76, 60), new Color(192, 57, 43));
            closeButton.addActionListener(e -> popup.dispose());

            buttonPanel.add(interestedButton);
            buttonPanel.add(closeButton);

            contentPanel.add(titleLabel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            contentPanel.add(detailsPanel);
            contentPanel.add(Box.createRigidArea(new Dimension(0, 20)));
            contentPanel.add(buttonPanel);

            popup.add(contentPanel);
            popup.setVisible(true);
        }

        private JPanel createDetailLabel(String label, String value) {
            JPanel panel = new JPanel(new BorderLayout(10, 0));
            panel.setOpaque(false);

            JLabel labelComponent = new JLabel(label + ":");
            labelComponent.setFont(new Font("Arial", Font.BOLD, 14));
            labelComponent.setForeground(new Color(52, 73, 94)); // Dark blue-gray color

            JLabel valueComponent = new JLabel(value);
            valueComponent.setFont(new Font("Arial", Font.PLAIN, 14));
            valueComponent.setForeground(new Color(44, 62, 80)); // Darker blue-gray color

            panel.add(labelComponent, BorderLayout.WEST);
            panel.add(valueComponent, BorderLayout.CENTER);

            return panel;
        }

        private JLabel createInfoLabel(String text, Color color) {
            JLabel label = new JLabel(text);
            label.setForeground(color);
            label.setFont(new Font("Arial", Font.PLAIN, 14));
            return label;
        }
    }

    private void openAddRequestPage() {
        System.out.println("openAddRequestPage method called"); // Debug print
        SwingUtilities.invokeLater(() -> {
            try {
                if(this.studentid =="xxxx"){
                    JOptionPane.showMessageDialog(this, "Please Login First.", "Messages", JOptionPane.INFORMATION_MESSAGE);

                }
                else {
                    Addrequest addRequestPage = new Addrequest(this.studentid);
                    addRequestPage.setVisible(true);
                }
                // Remove this line to keep StudentInformationPage open
                // this.setVisible(false);
                System.out.println("AddRequest page created and set visible"); // Debug print
            } catch (Exception e) {
                e.printStackTrace(); // Print any exceptions that occur
                JOptionPane.showMessageDialog(this, "Error opening Add Request page: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    class Student {
        String studentID, requestId, firstName, lastName, instituteName, number,email, gender, amount, address, vacancy, roomType, age, distance, tellAbout, addadress;

        public Student(String studentID, String requestId, String firstName, String lastName, String instituteName, String number,String email, String gender, String amount, String address,
                       String vacancy, String roomType, String age, String distance, String tellAbout, String addadress) {
            this.studentID = studentID;
            this.requestId = requestId;
            this.firstName = firstName;
            this.lastName = lastName;
            this.instituteName = instituteName;
            this.number = number;
            this.email = email;
            this.gender = gender;
            this.amount = amount;
            this.address = address;
            this.vacancy = vacancy;
            this.roomType = roomType;
            this.age = age;
            this.distance = distance;
            this.tellAbout = tellAbout;
            this.addadress = addadress;
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
                String email = resultSet.getString("birthdate");
                String gender = resultSet.getString("gender");
                String amount = resultSet.getString("amount");

                // Summarizing the address
                String streetName = resultSet.getString("street_name");
                String city = resultSet.getString("city");
                String state = resultSet.getString("state");
                String pinCode = resultSet.getString("pin_code");
                String addaddress = resultSet.getString("additional_address");
                String address = streetName + ", " + city + ", " + state + " - " + pinCode;

                String vacancy = resultSet.getString("vacancy");
                String roomType = resultSet.getString("room_typr"); // Fix typo to "room_type"
                String age = resultSet.getString("age");
                String distance = resultSet.getString("distance_college");
                String tellAbout = resultSet.getString("tell_about");

                // Add student data to the list
                students.add(new Student(studentID,requestId, firstName, lastName, instituteName, number, email, gender, amount, address, vacancy, roomType, age, distance, tellAbout, addaddress));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return students;
    }
//    ----------------------------------------------------------------------------------------------------------------------------


    public static void main(String[] args) {
        System.out.println("Starting main method");
        System.out.println("Starting StudentInformationPage");
        String studentId = "test123"; // Use a test student ID
        SwingUtilities.invokeLater(() -> {
            System.out.println("Creating StudentInformationPage");
            StudentInformationPage page = new StudentInformationPage("xxxx");
            System.out.println("Setting StudentInformationPage visible");
            page.setVisible(true);
        });
    }
}