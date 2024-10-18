import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.*;
import java.util.Random;
import java.util.regex.Pattern;

public class Addrequest extends JFrame  implements ActionListener{
    static String FN = "";
    static String LN = "";
    static String Email = "";
    static String BirthDate = "";
    static String Age = "";
    static String Institute = "";
    static String Mobile = "";
    static String Gender = "";
    static String State = "";
    static String City = "";
    static String Pin = "";
    static String Street = "";
    static String Distance = "";
    static String Vacancy = "";
    static String Amount = "";
    static String RoomType = "";
    static String ExtraInfo = "";
    static String AdditionalAddress = "";
    static String AboutSelf = "";

    private JCheckBox termsCheckBox;
    private JTextField firstNameField;
    private JTextField lastNameField;
    private JTextField emailField;
    private JTextField birthdateField;
    private JTextField ageField;
    private JTextField instituteField;
    private JTextField mobileField;
    private JTextField stateField;
    private JTextField cityField;
    private JTextField pinField;
    private JTextField streetField;
    private JTextField distanceField;
    private JTextField vacancyField;
    private JTextField amountField;
    private JCheckBox hkCheckBox, bhk1CheckBox, bhk2CheckBox, bhk3CheckBox, bhk4CheckBox, moreCheckBox;
    private JCheckBox maleCheckBox, femaleCheckBox, otherCheckBox;
    private JTextArea extraInfoArea;
    private JTextArea additionalAddressArea;
    private JTextArea aboutSelfArea;
    private JButton createAccountButton;
    private JButton cencelrequestButton;
    private String studentId;

    // Add these patterns for validation
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");
    private static final Pattern MOBILE_PATTERN = Pattern.compile("^\\d{10}$");
    private static final Pattern PIN_PATTERN = Pattern.compile("^\\d{6}$");

    public Addrequest(String studentId) {
        this.studentId = studentId;
        // Setting up the frame
        setTitle("Add a Request");
        setSize(900, 600);
//        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Split Pane to ensure equal space between panels
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);

        // Left side panel for image
        JPanel imagePanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon backgroundImage = new ImageIcon("src/images/Designer.png"); // Provide the path to your image here
                g.drawImage(backgroundImage.getImage(), 0, 0, getWidth(), getHeight(), this);
            }
        };
        imagePanel.setLayout(new BorderLayout());
//        JLabel sloganLabel = new JLabel("PG ACCOMODATION PLATFORM", SwingConstants.CENTER);

//        sloganLabel.setForeground(Color.black);
//        sloganLabel.setFont(new Font("Serif", Font.BOLD, 25));
//        imagePanel.add(sloganLabel, BorderLayout.SOUTH);

        // Right side panel for form
        JPanel formPanel = new JPanel();
        formPanel.setLayout(new GridBagLayout());
        formPanel.setBackground(new Color(2, 21, 34)); // Background color similar to the image

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel titleLabel = new JLabel("Add a Request");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        formPanel.add(titleLabel, gbc);

        JLabel loginLabel = new JLabel("Personal Detail");
        loginLabel.setFont(new Font("Arial", Font.BOLD, 18));
        loginLabel.setForeground(Color.WHITE);
//        loginLabel.setForeground(new Color(162, 162, 187));
//        loginLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridy++;
        formPanel.add(loginLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;

        // First Name
        firstNameField = new CustomTextField("First Name");
        gbc.gridx = 0;
        gbc.gridy++;
        formPanel.add(firstNameField, gbc);

        // Last Name
        lastNameField = new CustomTextField("Last Name");
        gbc.gridx = 1;
        formPanel.add(lastNameField, gbc);

        // Email
        emailField = new CustomTextField("Email");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(emailField, gbc);

        // Birthdate
        // birthdateField = new CustomTextField("Birthdate (YYYY-MM-DD)");
        // gbc.gridy++;
        // formPanel.add(birthdateField, gbc);

        // Age
         ageField = new CustomTextField("Age");
         gbc.gridy++;
         formPanel.add(ageField, gbc);

        // Institute
        instituteField = new CustomTextField("Institute Name");
        gbc.gridy++;
        formPanel.add(instituteField, gbc);

        // Mobile Number
        mobileField = new CustomTextField("Mobile Number");
        gbc.gridy++;
        formPanel.add(mobileField, gbc);

        // Gender (Checkboxes)
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(genderLabel, gbc);

        JPanel genderPanel = new JPanel();
        genderPanel.setBackground(new Color(2, 21, 34));

        maleCheckBox = new CustomCheckBox("Male");
        femaleCheckBox = new CustomCheckBox("Female");
        otherCheckBox = new CustomCheckBox("Other");

        genderPanel.add(maleCheckBox);
        genderPanel.add(femaleCheckBox);
        genderPanel.add(otherCheckBox);

        // Adding item listeners to ensure only one checkbox is selected at a time
        maleCheckBox.addItemListener(e -> {
            if (maleCheckBox.isSelected()) {
                femaleCheckBox.setSelected(false);
                otherCheckBox.setSelected(false);
            }
        });

        femaleCheckBox.addItemListener(e -> {
            if (femaleCheckBox.isSelected()) {
                maleCheckBox.setSelected(false);
                otherCheckBox.setSelected(false);
            }
        });

        otherCheckBox.addItemListener(e -> {
            if (otherCheckBox.isSelected()) {
                maleCheckBox.setSelected(false);
                femaleCheckBox.setSelected(false);
            }
        });

        gbc.gridx = 1;
        formPanel.add(genderPanel, gbc);

        // Address Section
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel addressLabel = new JLabel("Address Information");
        addressLabel.setFont(new Font("Arial", Font.BOLD, 18));
        addressLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        formPanel.add(addressLabel, gbc);

        // State
        stateField = new CustomTextField("State");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(stateField, gbc);

        // City
        cityField = new CustomTextField("City");
        gbc.gridx = 1;
        formPanel.add(cityField, gbc);

        // Pin
        pinField = new CustomTextField("Pin Code");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 1;
        formPanel.add(pinField, gbc);

        // Street
        streetField = new CustomTextField("Street");
        gbc.gridx = 1;
        formPanel.add(streetField, gbc);

        // Distance from College
        distanceField = new CustomTextField("Distance from College (in km)");
        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        formPanel.add(distanceField, gbc);

        // Vacancy
        vacancyField = new CustomTextField("Vacancy");
        gbc.gridy++;
        formPanel.add(vacancyField, gbc);

        // Amount
        amountField = new CustomTextField("Amount (Rent per month)");
        gbc.gridy++;
        formPanel.add(amountField, gbc);

        // Room Type (Checkboxes)
        JLabel roomTypeLabel = new JLabel("Room Type:");
        roomTypeLabel.setForeground(Color.WHITE);
        gbc.gridy++;
        formPanel.add(roomTypeLabel, gbc);

        JPanel roomTypePanel = new JPanel();
        roomTypePanel.setBackground(new Color(2, 21, 34));

        hkCheckBox = new CustomCheckBox("HK");
        bhk1CheckBox = new CustomCheckBox("1BHK");
        bhk2CheckBox = new CustomCheckBox("2BHK");
        bhk3CheckBox = new CustomCheckBox("3BHK");
        bhk4CheckBox = new CustomCheckBox("4BHK");
        moreCheckBox = new CustomCheckBox("More");

        roomTypePanel.add(hkCheckBox);
        roomTypePanel.add(bhk1CheckBox);
        roomTypePanel.add(bhk2CheckBox);
        roomTypePanel.add(bhk3CheckBox);
        roomTypePanel.add(bhk4CheckBox);
        roomTypePanel.add(moreCheckBox);

        gbc.gridy++;
        formPanel.add(roomTypePanel, gbc);

        hkCheckBox.addItemListener(e -> {
            if (hkCheckBox.isSelected()) {
                bhk1CheckBox.setSelected(false);
                bhk2CheckBox.setSelected(false);
                bhk3CheckBox.setSelected(false);
                bhk4CheckBox.setSelected(false);
                moreCheckBox.setSelected(false);
            }
        });

        bhk1CheckBox.addItemListener(e -> {
            if (bhk1CheckBox.isSelected()) {
                hkCheckBox.setSelected(false);
                bhk2CheckBox.setSelected(false);
                bhk3CheckBox.setSelected(false);
                bhk4CheckBox.setSelected(false);
                moreCheckBox.setSelected(false);
            }
        });

        bhk2CheckBox.addItemListener(e -> {
            if (bhk2CheckBox.isSelected()) {
                hkCheckBox.setSelected(false);
                bhk1CheckBox.setSelected(false);
                bhk3CheckBox.setSelected(false);
                bhk4CheckBox.setSelected(false);
                moreCheckBox.setSelected(false);
            }
        });

        bhk3CheckBox.addItemListener(e -> {
            if (bhk3CheckBox.isSelected()) {
                hkCheckBox.setSelected(false);
                bhk1CheckBox.setSelected(false);
                bhk2CheckBox.setSelected(false);
                bhk4CheckBox.setSelected(false);
                moreCheckBox.setSelected(false);
            }
        });

        bhk4CheckBox.addItemListener(e -> {
            if (bhk4CheckBox.isSelected()) {
                hkCheckBox.setSelected(false);
                bhk1CheckBox.setSelected(false);
                bhk2CheckBox.setSelected(false);
                bhk3CheckBox.setSelected(false);
                moreCheckBox.setSelected(false);
            }
        });

        moreCheckBox.addItemListener(e -> {
            if (moreCheckBox.isSelected()) {
                hkCheckBox.setSelected(false);
                bhk1CheckBox.setSelected(false);
                bhk2CheckBox.setSelected(false);
                bhk3CheckBox.setSelected(false);
                bhk4CheckBox.setSelected(false);
            }
        });


        // Additional Information Section
        gbc.gridx = 0;
        gbc.gridy++;
        JLabel additionalInfoLabel = new JLabel("Additional Information");
        additionalInfoLabel.setFont(new Font("Arial", Font.BOLD, 18));
        additionalInfoLabel.setForeground(Color.WHITE);
        gbc.gridwidth = 2;
        formPanel.add(additionalInfoLabel, gbc);

        // Additional Address
        additionalAddressArea = new JTextArea(3, 20);
        additionalAddressArea.setText("Additional Address");
        additionalAddressArea.setLineWrap(true);
        additionalAddressArea.setWrapStyleWord(true);
        additionalAddressArea.setBackground(new Color(2, 21, 34));
        additionalAddressArea.setForeground(Color.GRAY);
        additionalAddressArea.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(110, 172, 218)));
        additionalAddressArea.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (additionalAddressArea.getText().equals("Additional Address")) {
                    additionalAddressArea.setText("");
                    additionalAddressArea.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (additionalAddressArea.getText().isEmpty()) {
                    additionalAddressArea.setText("Additional Address");
                    additionalAddressArea.setForeground(Color.GRAY);
                }
            }
        });
        gbc.gridy++;
        formPanel.add(new JScrollPane(additionalAddressArea), gbc);


        // Tell about yourself (with placeholder)
        // aboutSelfArea = new JTextArea(3, 20);
        // aboutSelfArea.setText("Tell about yourself");
        // aboutSelfArea.setLineWrap(true);
        // aboutSelfArea.setWrapStyleWord(true);
        // aboutSelfArea.setBackground(new Color(2, 21, 34));
        // aboutSelfArea.setForeground(Color.GRAY);
        // aboutSelfArea.setBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(110, 172, 218)));
        // aboutSelfArea.addFocusListener(new FocusAdapter() {
        //     @Override
        //     public void focusGained(FocusEvent e) {
        //         if (aboutSelfArea.getText().equals("Tell about yourself")) {
        //             aboutSelfArea.setText("");
        //             aboutSelfArea.setForeground(Color.WHITE);
        //         }
        //     }

        //     @Override
        //     public void focusLost(FocusEvent e) {
        //         if (aboutSelfArea.getText().isEmpty()) {
        //             aboutSelfArea.setText("Tell about yourself");
        //             aboutSelfArea.setForeground(Color.GRAY);
        // //         }
        // //     }
        // // });
        // gbc.gridy++;
        // formPanel.add(new JScrollPane(aboutSelfArea), gbc);


        // Terms and Conditions Checkbox
        termsCheckBox = new CustomCheckBox("Make Sure Your Detail is Real");
        gbc.gridy++;
        formPanel.add(termsCheckBox, gbc);

        // Make Request button
        createAccountButton = new JButton("Make Request");
        createAccountButton.setBackground(new Color(110, 172, 218));
        createAccountButton.setForeground(Color.BLACK);
        gbc.gridy++;
        formPanel.add(createAccountButton, gbc);
        createAccountButton.addActionListener(this);

        // Cancel Request button
        cencelrequestButton = new JButton("Cancel Request");
        cencelrequestButton.setBackground(new Color(110, 172, 218));
        cencelrequestButton.setForeground(Color.BLACK);
        gbc.gridy++;
        formPanel.add(cencelrequestButton, gbc);
        cencelrequestButton.addActionListener(this);

        // Scroll Pane with smooth scrolling and increased speed
        JScrollPane scrollPane = new JScrollPane(formPanel);
        scrollPane.getVerticalScrollBar().setBackground(new Color(2, 21, 34));
        scrollPane.getVerticalScrollBar().setForeground(new Color(110, 172, 218));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  // Increases scroll speed
        scrollPane.setBorder(null);

        splitPane.setRightComponent(scrollPane);
        splitPane.setLeftComponent(imagePanel);
        splitPane.setDividerLocation(450);

        add(splitPane);
        this.setLocationRelativeTo(null);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cencelrequestButton) {
            dispose();
//            new StudentInformationPage(studentId);
        } else if (e.getSource() == createAccountButton) {
            if (validateInputs()) {
                insertIntoDatabase();
                dispose();
            new StudentInformationPage(studentId);
            }
        }
    }

    private boolean validateInputs() {
        if (isAnyFieldEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.");
            return false;
        }

        if (!EMAIL_PATTERN.matcher(emailField.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid email address.");
            return false;
        }

        if (!MOBILE_PATTERN.matcher(mobileField.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10-digit mobile number.");
            return false;
        }

        if (!PIN_PATTERN.matcher(pinField.getText()).matches()) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 6-digit PIN code.");
            return false;
        }

        try {
            double distance = Double.parseDouble(distanceField.getText());
            if (distance < 0) {
                JOptionPane.showMessageDialog(this, "Distance cannot be negative.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for distance.");
            return false;
        }

        try {
            int vacancy = Integer.parseInt(vacancyField.getText());
            if (vacancy <= 0) {
                JOptionPane.showMessageDialog(this, "Vacancy must be a positive number.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for vacancy.");
            return false;
        }

        try {
            double amount = Double.parseDouble(amountField.getText());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Amount must be a positive number.");
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Please enter a valid number for amount.");
            return false;
        }

        if (!isGenderSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a gender.");
            return false;
        }

        if (!isRoomTypeSelected()) {
            JOptionPane.showMessageDialog(this, "Please select a room type.");
            return false;
        }

        if (!termsCheckBox.isSelected()) {
            JOptionPane.showMessageDialog(this, "Please confirm that your details are real.");
            return false;
        }

        return true;
    }

    private boolean isAnyFieldEmpty() {
        return firstNameField.getText().equals("First Name") ||
                lastNameField.getText().equals("Last Name") ||
                emailField.getText().isEmpty() ||
                instituteField.getText().equals("Institute Name") ||
                mobileField.getText().equals("Mobile Number") ||
                stateField.getText().equals("State") ||
                cityField.getText().equals("City") ||
                pinField.getText().equals("Pin Code") ||
                streetField.getText().equals("Street") ||
                distanceField.getText().equals("Distance from College (in km)") ||
                vacancyField.getText().equals("Vacancy") ||
                amountField.getText().equals("Amount (Rent per month)") ||
                additionalAddressArea.getText().equals("Additional Address");
    }

    private boolean isGenderSelected() {
        return maleCheckBox.isSelected() || femaleCheckBox.isSelected() || otherCheckBox.isSelected();
    }

    private boolean isRoomTypeSelected() {
        return hkCheckBox.isSelected() || bhk1CheckBox.isSelected() || bhk2CheckBox.isSelected() ||
                bhk3CheckBox.isSelected() || bhk4CheckBox.isSelected() || moreCheckBox.isSelected();
    }

    private void insertIntoDatabase() {
        try {
            new dbconnect();
            Statement statement = dbconnect.statement;
            Connection connection = dbconnect.connection;
            ResultSet resultset = statement.executeQuery("SELECT * FROM students");
            PreparedStatement pre = connection.prepareStatement(
                    "INSERT INTO students (" +
                            "id, first_name, last_name, birthdate, age, institute_name, mobile_no, gender, " +
                            "state, city, pin_code, street_name, distance_college, vacancy, amount, room_typr, " +
                            "additional_address, tell_about, studentid) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"
            );
            Random random = new Random();
            int randomNumber = 1000 + random.nextInt(9000);
            pre.setString(1, Integer.toString(randomNumber));
            pre.setString(2, firstNameField.getText());
            pre.setString(3, lastNameField.getText());
            pre.setString(4, emailField.getText());
            pre.setString(5, ageField.getText());
            pre.setString(6, instituteField.getText());
            pre.setString(7, mobileField.getText());

            // Set gender
            if (maleCheckBox.isSelected()) {
                pre.setString(8, "male");
            } else if (femaleCheckBox.isSelected()) {
                pre.setString(8, "female");
            } else if (otherCheckBox.isSelected()) {
                pre.setString(8, "other");
            }

            pre.setString(9, stateField.getText());
            pre.setString(10, cityField.getText());
            pre.setString(11, pinField.getText());
            pre.setString(12, streetField.getText());
            pre.setString(13, distanceField.getText());
            pre.setString(14, vacancyField.getText());
            pre.setString(15, amountField.getText());

            // Set room_type
            if (hkCheckBox.isSelected()) {
                pre.setString(16, "RK");
            } else if (bhk1CheckBox.isSelected()) {
                pre.setString(16, "1BHK");
            } else if (bhk2CheckBox.isSelected()) {
                pre.setString(16, "2BHK");
            } else if (bhk3CheckBox.isSelected()) {
                pre.setString(16, "3BHK");
            } else if (bhk4CheckBox.isSelected()) {
                pre.setString(16, "4BHK");
            } else if (moreCheckBox.isSelected()) {
                pre.setString(16, "more");
            }

            pre.setString(17, additionalAddressArea.getText());
            pre.setString(18, "Nothing");
            pre.setString(19, studentId);
            pre.executeUpdate();

            // Show success pop-up
            showSuccessPopup();

            dispose();
            new StudentInformationPage(studentId);
        } catch (SQLException E) {
            E.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error occurred while adding request. Please try again.");
        }
    }

    private void showSuccessPopup() {
        JDialog dialog = new JDialog(this, "Request Submitted", true);
        dialog.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(2, 21, 34));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel messageLabel = new JLabel("Request Added Successfully!");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setForeground(Color.WHITE);
        panel.add(messageLabel, gbc);

        gbc.gridy = 1;
        gbc.insets = new Insets(20, 10, 10, 10);
        JButton okButton = new JButton("OK");
        okButton.setBackground(new Color(110, 172, 218));
        okButton.setForeground(Color.BLACK);
        okButton.addActionListener(e -> dialog.dispose());
        panel.add(okButton, gbc);

        dialog.add(panel, BorderLayout.CENTER);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    // Custom text field with rounded borders and placeholder functionality
    class CustomTextField extends JTextField {
        private String placeholder;

        public CustomTextField(String placeholder) {
            this.placeholder = placeholder;
            setText(placeholder);
            setOpaque(false);
            setForeground(Color.GRAY);
            setBackground(new Color(2, 21, 34));
            setBorderRadius();

            addFocusListener(new FocusAdapter() {
                @Override
                public void focusGained(FocusEvent e) {
                    if (getText().equals(placeholder)) {
                        setText("");
                        setForeground(Color.WHITE);
                    }
                }

                @Override
                public void focusLost(FocusEvent e) {
                    if (getText().isEmpty()) {
                        setText(placeholder);
                        setForeground(Color.GRAY);
                    }
                }
            });
        }

        private void setBorderRadius() {
            setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createEmptyBorder(),
                    BorderFactory.createMatteBorder(0, 0, 2, 0, new Color(110, 172, 218))));
        }
    }

    // Custom CheckBox matching the theme
    class CustomCheckBox extends JCheckBox {
        public CustomCheckBox(String text) {
            super(text);
            setBackground(new Color(2, 21, 34));
            setForeground(Color.WHITE);
        }
    }

    public static void main(String[] args) {
        new Addrequest("789897");
    }
}