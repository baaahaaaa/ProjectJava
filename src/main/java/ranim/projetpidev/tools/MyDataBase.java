package ranim.projetpidev.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String URL="jdbc:mysql://localhost:3306/testpidevjava";
    public final String USER="root";
    public final String PWD="";
    private Connection cnx;
    public static MyDataBase m;
    private MyDataBase(){

        try{
            cnx= DriverManager.getConnection(URL,USER,PWD);
            System.out.println("Connected to the database successfully");
        }catch(SQLException e){
            System.out.println(e.getMessage());}
    }
    public static MyDataBase getInstance(){
        if(m==null)
        {
            m=new MyDataBase();
        }
        return m;
    }

    public Connection getCnx() {
        return cnx;
    }
}
