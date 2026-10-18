package ranim.projetpidev.tools;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MyDataBase {
    public final String URL=System.getenv().getOrDefault("EDUCONNECT_DB_URL", "jdbc:mysql://localhost:3307/testpidev");
    public final String USER=System.getenv().getOrDefault("EDUCONNECT_DB_USER", "root");
    public final String PWD=System.getenv().getOrDefault("EDUCONNECT_DB_PASSWORD", "");
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
        try {
            if (cnx == null || cnx.isClosed()) {
                cnx = DriverManager.getConnection(URL, USER, PWD);
                System.out.println("🔄 Connexion renouvelée");
            }
        } catch (SQLException e) {
            System.err.println("Erreur de reconnexion : " + e.getMessage());
        }
        return cnx;
    }}
