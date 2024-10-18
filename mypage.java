import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.sql.*;
import java.util.*;
import java.util.List;

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

    public class mypage extends JFrame{
        static String studentid;
        JMenuBar menuBar;
        JMenu fileMenu;
        JMenu editMenu;
        JMenu helpMenu;
        JMenuItem Addrequest;
        JMenuItem message;
        JMenuItem Favorite;
        JMenuItem logout;
        JMenuItem exit;
        ImageIcon requesticon;
        ImageIcon messageicon;
        ImageIcon favoriteicon;
        ImageIcon logouticon;
        ImageIcon exiticon;
        ImageIcon menuicon;
        ImageIcon editicon;
        ImageIcon helpicon;
        private JTable table;
        private DefaultTableModel tableModel;
        private JComboBox<String> instituteComboBox;
        private List<Student> students;
        private JButton interestedButton, favoriteButton, searchButton;

        private ImageIcon resizeIcon(ImageIcon icon, int width, int height) {
            Image img = icon.getImage();  // Get the original image
            Image resizedImage = img.getScaledInstance(width, height,  java.awt.Image.SCALE_SMOOTH); // Resize the image
            return new ImageIcon(resizedImage);  // Return the resized image as an icon
        }

        private void setMenuItemStyle(JMenuItem menuItem, ImageIcon icon, int mnemonic) {
            menuItem.setBounds(0, 0, 150, 20);
            menuItem.setFont(new Font(Font.SERIF, Font.BOLD, 25));
            menuItem.setIcon(icon);
            menuItem.setMnemonic(mnemonic);
        }
        public mypage(String userId) {
            // Fetch student data
            studentid = userId;
            students = getStudents();
            JLabel pgroom = new JLabel();
            pgroom.setBounds(0, 0, 280, 40);
//        pgroom.setBackground(Color.CYAN);
            pgroom.setOpaque(true);
//        pgroom.setText("PG ROOM");
            pgroom.setFont(new Font(Font.SERIF, Font.BOLD, 30));
            pgroom.setHorizontalAlignment(JLabel.CENTER);
            // Get unique institute names
            Set<String> institutes = new HashSet<>();
            institutes.add(""); // Placeholder for ComboBox
            for (Student student : students) {
                institutes.add(student.instituteName);
            }

            // Create ComboBox for institute names with a placeholder and increase the width
            instituteComboBox = new JComboBox<>(institutes.toArray(new String[0]));
            instituteComboBox.setRenderer(new ComboBoxRenderer("Select Institute"));
            instituteComboBox.setSelectedIndex(-1); // Ensure placeholder is displayed initially
            instituteComboBox.setPreferredSize(new Dimension(250, 30)); // Set preferred width of the ComboBox

            // Create a Search Button
            JButton searchButton = new JButton("Search");
            searchButton.addActionListener(e -> filterTableByInstitute());
//        searchButton.setBounds(10, 20, 30, 40);

            // Table columns
            String[] columns = {
                    "Student ID", "ID", "First Name", "Last Name", "Institute Name", "Number", "Gender", "Amount", "Address",
                    "Vacancy", "Room Type", "Age", "Distance", "Tell About"
            };

            // Table model
            tableModel = new DefaultTableModel(columns, 0);

            // Populate table model with student data
//            populateTableModel(students);

            // Create JTable with the data
            table = new JTable(tableModel) {
                @Override
                public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
                    Component c = super.prepareRenderer(renderer, row, column);
                    if (!isRowSelected(row)) {
                        c.setBackground(row % 2 == 0 ? new Color(13, 34, 61) : new Color(23, 42, 58)); // Alternate row colors
                        c.setForeground(Color.WHITE); // White text
                    }
                    if (column == 0 || column == 1) {
                        ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.GRAY)); // Border between columns
                    } else {
                        ((JComponent) c).setBorder(BorderFactory.createMatteBorder(1, 0, 1, 1, Color.GRAY)); // Other column borders
                    }
                    return c;
                }
            };

            // Set preferred column widths for clear visibility (adjust sizes as needed)
            table.getColumnModel().getColumn(0).setPreferredWidth(30); //Student ID
            table.getColumnModel().getColumn(1).setPreferredWidth(20); //request ID
            table.getColumnModel().getColumn(2).setPreferredWidth(70); // First Name
            table.getColumnModel().getColumn(3).setPreferredWidth(70); // Last Name
            table.getColumnModel().getColumn(4).setPreferredWidth(300); // Institute Name
            table.getColumnModel().getColumn(5).setPreferredWidth(60); // Institute Name
            table.getColumnModel().getColumn(6).setPreferredWidth(20);  // Gender
            table.getColumnModel().getColumn(7).setPreferredWidth(20);  // Amount
            table.getColumnModel().getColumn(8).setPreferredWidth(280); // Address
            table.getColumnModel().getColumn(9).setPreferredWidth(25);  // Vacancy
            table.getColumnModel().getColumn(10).setPreferredWidth(40); // Room Type
            table.getColumnModel().getColumn(11).setPreferredWidth(10);  // Age
            table.getColumnModel().getColumn(12).setPreferredWidth(30); // Distance
            table.getColumnModel().getColumn(13).setPreferredWidth(200); // Tell About

            table.setRowHeight(30);
            table.setFont(new Font("Arial", Font.PLAIN, 14));
            table.setGridColor(new Color(23, 42, 58)); // Prussian Blue

            // Set up scroll panes for both vertical and horizontal scrolling
            JScrollPane scrollPane = new JScrollPane(table, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
            scrollPane.setPreferredSize(new Dimension(800, 400)); // Set a fixed size

            // Layout the comboBox, search button, and table
            interestedButton = new JButton("Close");
            favoriteButton = new JButton("Delete");
            searchButton = new JButton("Search");

            // Add action listeners to the buttons
            interestedButton.addActionListener(e -> markAsInterested());
            favoriteButton.addActionListener(e -> deleterequest());
            searchButton.addActionListener(e -> filterTableByInstitute());

            // Create a search panel with ComboBox and buttons
            JPanel searchPanel = new JPanel();
//            searchPanel.add(instituteComboBox); // ComboBox for selecting institute
//            searchPanel.add(searchButton);      // Search button
            searchPanel.add(interestedButton);  // Interested button next to search
            searchPanel.add(favoriteButton);    // Favorite button next to interested

            // Create a top panel to hold the search panel and table
            JPanel topPanel = new JPanel(new BorderLayout());
            topPanel.add(searchPanel, BorderLayout.NORTH);  // Add search panel to the top
            topPanel.add(scrollPane, BorderLayout.CENTER);  // Table in center with scrolling

            // Add the panel to the frame
            this.add(topPanel);
//        dd search panel to the top

            // Add table to the center
            topPanel.add(scrollPane, BorderLayout.CENTER); // Table in center with scrolling

            // Add the panel to the frame
            this.add(pgroom);
            this.add(topPanel);
            filterTableByInstitute();

            // Frame settings
            this.setTitle("Student Information");
//        this.setSize(900, 600);
            this.setExtendedState(JFrame.MAXIMIZED_BOTH);
            this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            this.setLocationRelativeTo(null);
            this.setVisible(true);
        }

        private void filterTableByInstitute() {
            String selectedInstitute = (String) instituteComboBox.getSelectedItem();
                List<Student> filteredStudents = new ArrayList<>();
                for (Student student : students) {
                    // Check for null instituteName before comparing
                    if (student.requestId != null && student.studentID.equals(studentid)) {
                        filteredStudents.add(student);
                    }
                }

                // Clear table and populate with filtered students
                tableModel.setRowCount(0);
                populateTableModel(filteredStudents);

        }

        private void populateTableModel(List<Student> students) {
            for (Student student : students) {
                Object[] row = {
                        student.studentID,student.requestId, student.firstName, student.lastName, student.instituteName, student.number, student.gender, student.amount,
                        student.address, student.vacancy, student.roomType, student.age, student.distance, student.tellAbout
                };
                tableModel.addRow(row);
            }
        }

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

        // Custom renderer for the ComboBox to show placeholder
        static class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

            private String placeholder;

            public ComboBoxRenderer(String placeholder) {
                this.placeholder = placeholder;
            }

            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                if (value == null || value.equals("")) {
                    setText(placeholder);
                } else {
                    setText(value.toString());
                }

                if (isSelected) {
//              setBackground(list.getSelectionBackground

                    setBackground(list.getSelectionBackground());
                    setForeground(list.getSelectionForeground());
                } else {
                    setBackground(list.getBackground());
                    setForeground(list.getForeground());
                }

                setOpaque(true);
                return this;
            }
        }

        // Method to handle the interested button click
        private void markAsInterested() {
//            int selectedRow = table.getSelectedRow();
//            if (selectedRow != -1) {
//                String studentId = (String) table.getValueAt(selectedRow, 0);  // Assuming ID is in the first column
////            storeStudentIdInDatabase(studentId, "interested");
//                try{
//                    new dbconnect();
//                    Connection connection = dbconnect.connection; // Assuming dbconnect.connection is already available
//                    Statement statement = connection.createStatement();
//                    ResultSet resultset = statement.executeQuery("select * from user");
//                    String str = "";
//                    while (resultset.next()) {
//                        if(resultset.getString("userid").equals(studentId)){
//                            str = resultset.getString("message");
//                            break;
//                        }
//                    }
//                    if(str.equals("null")){
//                        str = "";
//                    }
//                    str = str+"*"+studentid;
//                    PreparedStatement pre = connection.prepareStatement(
//                            "UPDATE user SET message = ? WHERE userid = ?"
//                    );
//                    pre.setString(1, str); // Set the new value for the column
//                    pre.setString(2, studentId); // Set the condition value
//                    pre.executeUpdate();
//                    JOptionPane.showMessageDialog(this, "Request send Sucessfully");
//                }
//                catch (SQLException e) {
//                    e.printStackTrace();
//                    JOptionPane.showMessageDialog(this, "Error While sending the Request");
//                }
//
//            } else {
//                JOptionPane.showMessageDialog(this, "Please select a student first.");
//            }
            this.dispose();
        }

        // Method to handle the favorite button click
        private void deleterequest() {
            int selectedRow = table.getSelectedRow();
            if (selectedRow != -1) {
                String requestId = (String) table.getValueAt(selectedRow, 1);  // Assuming ID is in the first column
//            System.out.println(studentId);
//            storeStudentIdInDatabase(studentId, "favorite");
                try{
                    new dbconnect();
                    Connection connection = dbconnect.connection; // Assuming dbconnect.connection is already available
                    Statement statement = connection.createStatement();
                    PreparedStatement preparedStatement = connection.prepareStatement("Delete from students where id = ?");
                    preparedStatement.setString(1, requestId);
                    preparedStatement.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Request Deleted Sucessfully");
                    this.dispose();
                    new mypage(studentid);
                }
                catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Error Deleteing Request");
                }

            } else {
                JOptionPane.showMessageDialog(this, "Please select a request first.");
            }
        }
        public static void main(String[] args) {
            new mypage("789897");
        }
    }


