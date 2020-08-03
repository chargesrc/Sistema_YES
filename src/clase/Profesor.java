
package clase;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
/**
 *
 * @author joni
 */
public class Profesor {
    private static final Logger LOG = Logger.getLogger(Profesor.class.getName());
    ////////////////////////////////////////////////////INSERTAR PROFESOR///////////////////////////////////////////////
    public static int leerIdPersona(Connection conexion,int dni) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idPersona FROM persona WHERE dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idPersona");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id de persona", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static int dniProfesor(Connection conexion,int idper) throws Exception{
        int dni=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT dni FROM persona WHERE idPersona=?");
        stmt.setInt(1, idper);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                dni=rs.getInt("dni");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el dni de la persona", "ERROR", ERROR_MESSAGE);
        }
        return dni;
    }
    public static void insertarPersonas(Connection conexion,String dni, String apellido,String nombre) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO persona(dni,apellido,nombre) VALUES (?,?,?)");
        stmt.setString(1, dni);
        stmt.setString(2, apellido);
        stmt.setString(3, nombre);
        try{
            stmt.execute();
            //JOptionPane.showMessageDialog(null,"Se agrego la persona correctamente");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al crear nueva persona", "ERROR", ERROR_MESSAGE);
        }
    }
    public static int leerUltimoIdPersona(Connection conexion) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT max(idPersona) FROM persona");
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("max(idPersona)");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el ultimo id del profesor", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static void insertarProfesor(Connection conexion,int idPersona,String celular,String direccion,boolean borrado) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO profesor(idPersona,celular,direccion,borrado) VALUES(?,?,?,?)");
        stmt.setInt(1,idPersona);
        stmt.setString(2,celular);
        stmt.setString(3,direccion);
        stmt.setBoolean(4, borrado);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se registro el profesor correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al agregar el profesor", "ERROR", ERROR_MESSAGE);
        }
    }
    public static void altaProfesor(Connection conexion,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE profesor SET borrado=false WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se reinscribio el profesor");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al reinscribir al profesor","ERROR",ERROR_MESSAGE);
        }
    }
    public static boolean leerBorrado(Connection conexion,int idpersona) throws Exception{
        boolean borrado=false;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT borrado FROM profesor WHERE idPersona=?");
        stmt.setInt(1, idpersona);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                borrado=rs.getBoolean("borrado");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el estado del profesor", "ERROR", ERROR_MESSAGE);
        }
        return borrado;
    }
    ///////////////////////////////////////////////BORRAR PROFESOR//////////////////////////////////////////////////////
    public static void borrarProfesor(Connection conexion,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE profesor SET borrado=true WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se dio de baja el profesor");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al dar de baja al profesor","ERROR",ERROR_MESSAGE);
        }
    }
    public static void borrarCursoProfesor(Connection conexion,int idPersona,int dni) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE curso as c inner join profesor as p on c.idProfesor=p.idProfesor inner join persona as a on p.idPersona=a.idPersona SET c.idProfesor=? WHERE a.dni=?");
        stmt.setInt(1, idPersona);
        stmt.setInt(2, dni);
        try{
           stmt.execute();
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al dar de baja el curso-profesor","ERROR",ERROR_MESSAGE);
        }
    }
    ////////////////////////////////////////////////////EDITAR PROFESOR/////////////////////////////////////////////////
    public static void editarPersona(Connection conexion,String dni,String apellido,String nombre,int idper) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE persona SET dni=?,apellido=?,nombre=? WHERE idPersona=?");
        stmt.setString(1,dni);
        stmt.setString(2,apellido);
        stmt.setString(3,nombre);
        stmt.setInt(4,idper);
        try{
           stmt.execute();
           //JOptionPane.showMessageDialog(null,"Se edito al profesor correctamente");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al editar la persona","ERROR",ERROR_MESSAGE);
        }
    }
    public static void editarProfesor(Connection conexion,int idPersona,String celular,String direccion,int idAlumno) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE profesor SET idPersona=?,celular=?,direccion=? WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        stmt.setString(2,celular);
        stmt.setString(3,direccion);
        stmt.setInt(4,idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se edito el profesor correctamente");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al editar los datos del profesor","ERROR",ERROR_MESSAGE);
        }
    }
    ///////////////////////////////////////////////////MOSTRAR PROFESOR///////////////////////////////////////////////// 
    public static ResultSet MostrarPersona(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,apellido,dni FROM persona\n" +
        "inner join profesor on persona.idPersona=profesor.idPersona WHERE borrado=false ORDER BY apellido asc");
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la persona", "ERROR", ERROR_MESSAGE);
        }
        return rs;
     }
    public static ResultSet MostrarProfesor(Connection conexion,int idPersona)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT celular,direccion FROM profesor WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar los datos del profesor", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }

    private Profesor() {
    }
}
