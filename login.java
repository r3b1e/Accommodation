import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class login extends JFrame implements ActionListener {
    JTextField namefield;
    JPasswordField passwordfield;
    JButton loginbutton;
    JButton signupbutton;

    public login() {
        // Title label for PG Room
        JLabel pgroom = new JLabel("PG ROOM", JLabel.CENTER);
        pgroom.setBounds(0, 0, 400, 40);
        pgroom.setOpaque(true);
        pgroom.setFont(new Font(Font.SERIF, Font.BOLD, 30));

        // Subtitle for login
        JLabel loginicon = new JLabel("Login", JLabel.CENTER);
        loginicon.setBounds(0, 50, 400, 30);
        loginicon.setFont(new Font(Font.SERIF, Font.BOLD, 20));
        loginicon.setBorder(new EmptyBorder(0, 20, 0, 0));

        // Username label and field
        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(40, 100, 100, 30);
        usernameLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        namefield = new JTextField();
        namefield.setBounds(150, 100, 200, 30);
        namefield.setFont(new Font(Font.SERIF, Font.BOLD, 14));
        namefield.setBorder(createRoundedBorder());
        namefield.setBorder(new RoundedBorder(10));

        // Password label and field
        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(40, 150, 100, 30);
        passwordLabel.setFont(new Font(Font.SERIF, Font.BOLD, 15));

        passwordfield = new JPasswordField();
        passwordfield.setBounds(150, 150, 200, 30);
        passwordfield.setFont(new Font(Font.SERIF, Font.PLAIN, 14));
        passwordfield.setBorder(createRoundedBorder());
        passwordfield.setBorder(new RoundedBorder(10));

        // Login button
        loginbutton = new JButton("Login");
        loginbutton.setBounds(70, 210, 120, 35);
        loginbutton.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        loginbutton.setBorder(createRoundedBorder());
        loginbutton.setBackground(Color.WHITE);
        loginbutton.addActionListener(this);
        loginbutton.setBorder(new RoundedBorder(10));

        signupbutton = new JButton("Sign Up");
        signupbutton.setBounds(200, 210, 120, 35);
        signupbutton.setFont(new Font(Font.SERIF, Font.BOLD, 15));
        signupbutton.setBorder(createRoundedBorder());
        signupbutton.setBackground(Color.WHITE);
        signupbutton.addActionListener(this);
        signupbutton.setBorder(new RoundedBorder(10));

        // Adding components to the frame
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(400, 300);
        this.setTitle("PG ROOM");
        this.setLayout(null);
        this.add(pgroom);
        this.add(loginicon);
        this.add(usernameLabel);
        this.add(namefield);
        this.add(passwordLabel);
        this.add(passwordfield);
        this.add(loginbutton);
        this.add(signupbutton);
        this.setVisible(true);

        // Set frame icon
        ImageIcon pgimage = new ImageIcon("pg.png");
        this.setIconImage(pgimage.getImage());
    }

    // Method to create rounded borders for text fields and buttons
    private Border createRoundedBorder() {
        return BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.BLACK, 1, true),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        );
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        Main main = new Main();
        String name = namefield.getText();
        String password = new String(passwordfield.getPassword());
        boolean istrue = true;
        if (e.getSource() == loginbutton) {
            try{
                new dbconnect();
                Statement statement = dbconnect.statement;
                Connection connection = dbconnect.connection;
                ResultSet resultset = statement.executeQuery("select * from user");

            while(resultset.next() && istrue){
                if (name.equals(resultset.getString("username")) && password.equals(resultset.getString("password"))){
                    JOptionPane.showMessageDialog(this, "Login Successful!");
                    istrue = false;
                    this.dispose();
                    new StudentInformationPage(resultset.getString("userid"));  // Assuming Home.java is the next screen after login
                }

            }
            }
            catch(SQLException e1){
                e1.printStackTrace();
            }
             if(istrue) {
                 JOptionPane.showMessageDialog(this, "Invalid username or password. Please try again.");
             }
        }
        if(e.getSource()==signupbutton){
            dispose();
           new signup();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new login());
    }
}
