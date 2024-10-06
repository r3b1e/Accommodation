import java.sql.*;

public class dbconnect {
    static Connection connection;
    static Statement statement;
    dbconnect(){
        try{
            connection = DriverManager.getConnection(
                    "jdbc:mysql://127.0.0.1:3306/db",
                    "root",
                    "sanju@98"
            );
            statement = connection.createStatement();
            ResultSet resultset = statement.executeQuery("select * from user");
//            PreparedStatement pre = connection.prepareStatement("INSERT INTO students (id, first_name) VALUES (?, ?)");
//            Main main = new Main();
//            pre.setString(1, "52");
//            pre.setString(2, "sunny");
//            pre.executeUpdate();
//            while(resultset.next()){
//                System.out.println(resultset.getString("favorite"));
//                System.out.println(resultset.getString("password"));
//            }
        }
        catch(SQLException e){
            e.printStackTrace();
        }


    }

    public static void main(String[] args) {
        new dbconnect();
    }
}
