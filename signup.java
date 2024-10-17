import javax.swing.*;
import javax.swing.border.AbstractBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.util.Random;

public class signup extends JFrame implements ActionListener {
    JButton submitButton, resetButton, backButton, closeButton;
    JTextField namefield, emailfield, mobilefield;
    JComboBox<String> genderComboBox;
    JPasswordField passwordfield, confirmfield;
    private Color accentColor = new Color(230, 126, 34);
    private Color dangerColor = new Color(231, 76, 60);
    private Color focusColor = new Color(46, 204, 113); // Green when focused

    public signup(){
        // Back Button
        backButton = createStyledButton("Back", accentColor);
        backButton.setBounds(10, 15, 80, 35);
        backButton.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        backButton.setBackground(Color.WHITE);
        backButton.setBorder(new RoundedBorder(10));
        backButton.addActionListener(this);

        // Close Button
        closeButton = createStyledButton("Close", dangerColor);
        closeButton.setBounds(410, 15, 80, 35);
        closeButton.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        closeButton.setBackground(Color.WHITE);
        closeButton.setBorder(new RoundedBorder(10));
        closeButton.addActionListener(this);

        // Header Label
        JLabel pgroom = new JLabel("PG ROOM", SwingConstants.CENTER);
        pgroom.setBounds(0, 0, 500, 60);
        pgroom.setFont(new Font(Font.SERIF, Font.BOLD, 36));
        pgroom.setOpaque(true);

        // Name Label and Field
        JLabel fullnameLabel = new JLabel("Name:");
        fullnameLabel.setBounds(30, 130, 120, 30);
        fullnameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        namefield = createModernTextField("Enter your name");
        namefield.setBounds(165, 130, 300, 30);

        // Email Label and Field
        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(30, 180, 120, 30);
        emailLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        emailfield = createModernTextField("Enter your email");
        emailfield.setBounds(165, 180, 300, 30);

        // Mobile Label and Field
        JLabel mobileLabel = new JLabel("Mobile:");
        mobileLabel.setBounds(30, 230, 120, 30);
        mobileLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        mobilefield = createModernTextField("Enter your mobile number");
        mobilefield.setBounds(165, 230, 300, 30);

        // Gender Label and ComboBox
        JLabel genderLabel = new JLabel("Gender:");
        genderLabel.setBounds(30, 280, 120, 30);
        genderLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        String[] genders = {"Select", "Male", "Female", "Other"};
        genderComboBox = new JComboBox<>(genders);
        genderComboBox.setBounds(165, 280, 300, 30);
        genderComboBox.setBackground(Color.white);
        genderComboBox.setFont(new Font(Font.SERIF, Font.PLAIN, 16));
        genderComboBox.setBorder(new RoundedBorder(10));

        // Password Label and Field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 330, 120, 30);
        passwordLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        passwordfield = createModernPasswordField();
        passwordfield.setBounds(165, 330, 300, 30);

        // Confirm Password Label and Field
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setBounds(30, 380, 150, 30);
        confirmLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        confirmfield = createModernPasswordField();
        confirmfield.setBounds(165, 380, 300, 30);

        // Buttons
        submitButton = createStyledButton("Sign Up", accentColor);
        resetButton = createStyledButton("Reset", dangerColor);
        submitButton.setBounds(160, 440, 120, 40);
        resetButton.setBounds(340, 440, 120, 40);

        submitButton.addActionListener(this);
        resetButton.addActionListener(this);

        // Frame setup
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(500, 550);
        this.setTitle("PG ROOM");
        this.setLayout(null);
        this.setResizable(false);


        // Add components
        this.add(backButton);
        this.add(closeButton);
        this.add(pgroom);
        this.add(fullnameLabel);
        this.add(namefield);
        this.add(emailLabel);
        this.add(emailfield);
        this.add(mobileLabel);
        this.add(mobilefield);
        this.add(genderLabel);
        this.add(genderComboBox);
        this.add(passwordLabel);
        this.add(passwordfield);
        this.add(confirmLabel);
        this.add(confirmfield);
        this.add(submitButton);
        this.add(resetButton);
        
        // Center on screen
        this.setLocationRelativeTo(null);
        this.setVisible(true);
    }
    private JTextField createModernTextField(String placeholder) {
        JTextField field = new JTextField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.GRAY);  // Placeholder color
                    g2.drawString(placeholder, 10, 20);  // Placeholder position
                }
            }
        };

        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.BLACK);
        field.setBorder(new RoundedBorder(10));
        field.setBackground(Color.WHITE);
        field.setCaretColor(Color.DARK_GRAY);
        field.setOpaque(true);

        // Add focus listener to repaint the component on focus change
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.repaint();  // Repaint on focus gain to remove placeholder
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.repaint();  // Repaint on focus lost to show placeholder if needed
            }
        });

        return field;
    }


    private JPasswordField createModernPasswordField() {
        JPasswordField field = new JPasswordField() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getPassword().length == 0 && !isFocusOwner()) {
                    Graphics2D g2 = (Graphics2D) g;
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                    g2.setColor(Color.GRAY);  // Placeholder color
                    g2.drawString("Enter your password", 10, 20);  // Custom placeholder text
                }
            }
        };

        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.BLACK);
        field.setBorder(new RoundedBorder(10));
        field.setBackground(Color.WHITE);  // Password field background
        field.setCaretColor(Color.DARK_GRAY);  // Caret color
        field.setOpaque(true);

        // Add focus listener for focus effects
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createLineBorder(focusColor, 2));  // Green focus border
                field.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(new RoundedBorder(10));  // Reset border when focus is lost
                field.repaint();
            }
        });

        return field;
    }


    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (getModel().isArmed()) {
                    g.setColor(bgColor.darker());
                } else {
                    g.setColor(bgColor);
                }
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 25, 25);
                super.paintComponent(g);
            }
        };
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 16));
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setOpaque(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == submitButton) {
            String name = namefield.getText();
            String email = emailfield.getText();
            String mobile = mobilefield.getText();  // Get mobile number
            String gender = (String) genderComboBox.getSelectedItem();
            String password = new String(passwordfield.getPassword());
            String confirmPassword = new String(confirmfield.getPassword());

            if (name.isEmpty() || email.isEmpty() || mobile.isEmpty() || gender.equals("Select") || password.isEmpty() || !password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(this, "Please fill in all fields correctly.");
            }
            else{
                try {
                    new dbconnect();
                    // Statement statement = DBConnect.statement;
                    Connection connection = dbconnect.connection;
                    // ResultSet resultset = statement.executeQuery("SELECT * FROM user");
                    PreparedStatement pre = connection.prepareStatement(
                            "INSERT INTO user (userid, username, password, email, mobileno)VALUES (?, ?, ?, ?, ?)"
                    );
                    Random random = new Random();
                    int randomNumber = 100000 + random.nextInt(900000);
                    pre.setString(1, Integer.toString(randomNumber));
                    pre.setString(2, name);
                    pre.setString(3, password);
                    pre.setString(4, email);
                    pre.setString(5, mobile);
                    pre.executeUpdate();
                    dispose();
                    new login();
                }
                catch(SQLException e1){
                    JOptionPane.showMessageDialog(this, "Database Error: Unable to register. Please try again.");
                    e1.printStackTrace();
                }

            }
        } else if (e.getSource() == resetButton) {
            namefield.setText("");
            emailfield.setText("");
            mobilefield.setText("");  // Clear mobile field
            genderComboBox.setSelectedIndex(0);
            passwordfield.setText("");
            confirmfield.setText("");
        } else if (e.getSource() == backButton) {
            // Add logic for back button (e.g., go to previous screen)
            dispose();
            new login(); // Close current window
            // new PreviousScreen(); // Uncomment and replace with actual previous screen
        } else if (e.getSource() == closeButton) {
            System.exit(0); // Close the application
        }
    }

    public static void main(String[] args) {
        new signup();
    }
}

// Rounded Border class for custom field borders
class RoundedBorder extends AbstractBorder {
    private int radius;

    public RoundedBorder(int radius) {
        this.radius = radius;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        g.setColor(Color.GRAY);
        g.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(5, 10, 5, 10);
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        insets.left = insets.right = 10;
        insets.top = insets.bottom = 5;
        return insets;
    }
}