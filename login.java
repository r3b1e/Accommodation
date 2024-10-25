import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame implements ActionListener {
    JTextField namefield;
    JPasswordField passwordfield;
    JButton loginButton, signupButton;
    private Color accentColor = new Color(230, 126, 34);
    private Color focusColor = new Color(46, 204, 113); // Green when focused

    public login() {
        // Title label for PG Room
        JLabel pgroom = new JLabel("PG ROOM", JLabel.CENTER);
        pgroom.setBounds(0, 0, 400, 60);
        pgroom.setFont(new Font(Font.SERIF, Font.BOLD, 30));
        pgroom.setOpaque(true);

        // Subtitle for login
        JLabel loginLabel = new JLabel("Login", JLabel.CENTER);
        loginLabel.setBounds(0, 60, 400, 30);
        loginLabel.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        loginLabel.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(30, 130, 120, 30);
        usernameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        namefield = createModernTextField("Enter your username");
        namefield.setBounds(150, 130, 200, 30);

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 180, 120, 30);
        passwordLabel.setFont(new Font(Font.SERIF, Font.BOLD, 16));

        passwordfield = createModernPasswordField();
        passwordfield.setBounds(150, 180, 200, 30);

        // Login button
        loginButton = createStyledButton("Login", accentColor);
        loginButton.setBounds(70, 240, 120, 40);
        loginButton.addActionListener(this);

        // Signup button
        signupButton = createStyledButton("Sign Up", accentColor);
        signupButton.setBounds(200, 240, 120, 40);
        signupButton.addActionListener(this);

        // Adding components to the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 350);
        this.setTitle("PG ROOM");
        this.setLayout(null);
        this.setResizable(false);
        this.setLocationRelativeTo(null);

        // Add components to the frame
        this.add(pgroom);
        this.add(loginLabel);
        this.add(usernameLabel);
        this.add(namefield);
        this.add(passwordLabel);
        this.add(passwordfield);
        this.add(loginButton);
        this.add(signupButton);

        // Set frame icon
        ImageIcon pgimage = new ImageIcon("pg.png");
        this.setIconImage(pgimage.getImage());
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

        // Focus listener for repainting on focus
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.repaint();
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
                    g2.setColor(Color.GRAY);
                    g2.drawString("Enter your password", 10, 20);
                }
            }
        };
        field.setFont(new Font("Arial", Font.PLAIN, 16));
        field.setForeground(Color.BLACK);
        field.setBorder(new RoundedBorder(10));
        field.setBackground(Color.WHITE);
        field.setCaretColor(Color.DARK_GRAY);
        field.setOpaque(true);

        // Focus listener for border color change
        field.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                field.setBorder(BorderFactory.createLineBorder(focusColor, 2));
                field.repaint();
            }

            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                field.setBorder(new RoundedBorder(10));
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
        String name = namefield.getText().trim();
        String password = new String(passwordfield.getPassword());
        boolean isValid = true;

        if (e.getSource() == loginButton) {
            try {
                new dbconnect();
                Statement statement = dbconnect.statement;
                Connection connection = dbconnect.connection;
                ResultSet resultSet = statement.executeQuery("SELECT * FROM user");

                while (resultSet.next() && isValid) {
                    if (name.equals(resultSet.getString("username")) && password.equals(resultSet.getString("password"))) {
                        JOptionPane.showMessageDialog(this, "Login Successful!");
                        isValid = false;
                        this.dispose();
                        new StudentInformationPage(resultSet.getString("userid"));
                    }
                }
            } catch (SQLException e1) {
                e1.printStackTrace();
            }

            if (isValid) {
                JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
            }
        } else if (e.getSource() == signupButton) {
            dispose();
            new signup();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(login::new);
    }
}

// Rounded Border class for custom field borders
class Rounded extends AbstractBorder {
    private int radius;

    public Rounded(int radius) {
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
