package conexion;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

public class Conexion {
    private static final Logger LOG = Logger.getLogger(Conexion.class.getName());
    public static Connection conectar(){
        Connection conexion=null;
        String servidor="jdbc:mysql://localhost:3306/YES";
        String usuario="root";
        String pass="root";
        try{
            Class.forName("com.mysql.jdbc.Driver");
            conexion=DriverManager.getConnection(servidor, usuario, pass);
        }
        catch(ClassNotFoundException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
        }
//        finally{
//            return conexion;
//        }
        return conexion;
    }
   
    public static int desconectar(Connection cnx){
        int retorno;
        try{ 
            cnx.close();
            retorno=1;
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            retorno=0;
        }
        return retorno;
    }

    private Conexion() {
    }
}
