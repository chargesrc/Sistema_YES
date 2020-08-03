
package clase;
import java.awt.HeadlessException;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

public class Alumno {
    private static final Logger LOG = Logger.getLogger(Alumno.class.getName());
    /////////////////////////////////////////////////INSERTAR PERSONA///////////////////////////////////////////////////
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
    public static int leerIdPersonaLegajo(Connection conexion,String legajo) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idPersona FROM alumno WHERE legajo=?");
        stmt.setString(1, legajo);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idPersona");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id de persona del alumno", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static void insertarPersonas(Connection conexion,String dni, String apellido,String nombre,java.util.Date fechaNacimiento) throws Exception{
        java.sql.Date fechaN=new Date (fechaNacimiento.getTime());
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO persona(dni,apellido,nombre,fechaNacimiento) VALUES (?,?,?,?)");
        stmt.setString(1, dni);
        stmt.setString(2, apellido);
        stmt.setString(3, nombre);
        stmt.setDate(4,fechaN);
        try{
            stmt.execute();
//            JOptionPane.showMessageDialog(null,"Se Agrego la persona");
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
            JOptionPane.showMessageDialog(null, "Error al leer el ultimo id de la persona", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static void insertarSinFoto(Connection conexion,int idPersona,String legajo,String tutor,String cotutor,String celular,String direccion,boolean borrado, String egresado) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO alumno(idPersona,legajo,tutor,cotutor,celular,direccion,borrado,egresado) VALUES (?,?,?,?,?,?,?,?)");
        stmt.setInt(1,idPersona);
        stmt.setString(2, legajo);
        stmt.setString(3, tutor);
        stmt.setString(4,cotutor);
        stmt.setString(5,celular);
        stmt.setString(6,direccion);
        stmt.setBoolean(7, borrado);
        stmt.setString(8,egresado);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se registro el alumno correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al agregar el alumno", "ERROR", ERROR_MESSAGE);
        }
    }
    public static void insertarAlumno(Connection conexion,int idPersona,String legajo,String tutor,String cotutor,String celular,String direccion,boolean borrado, String egresado,String nombreImg,FileInputStream archivoFoto) throws Exception{   
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO alumno(idPersona,legajo,tutor,cotutor,celular,direccion,borrado,egresado,nomImagen,imagen) VALUES (?,?,?,?,?,?,?,?,?,?)");
        stmt.setInt(1,idPersona);
        stmt.setString(2, legajo);
        stmt.setString(3, tutor);
        stmt.setString(4,cotutor);
        stmt.setString(5,celular);
        stmt.setString(6,direccion);
        stmt.setBoolean(7, borrado);
        stmt.setString(8,egresado);
        stmt.setString(9,nombreImg);
        stmt.setBinaryStream(10,archivoFoto);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se registro el alumno correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage());
        }
    }
    public static void AltaAlumno(Connection conexion,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE alumno SET borrado=false WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se dio de alta el alumno");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al dar de alta al alumno","ERROR",ERROR_MESSAGE);
        }
    }
    public static boolean leerBorrado(Connection conexion,int idpersona) throws Exception{
        boolean borrado=false;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT borrado FROM alumno WHERE idPersona=?");
        stmt.setInt(1, idpersona);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                borrado=rs.getBoolean("borrado");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer estado alta persona", "ERROR", ERROR_MESSAGE);
        }
        return borrado;
    }
    //////////////////////////////////////////////////EDITAR PERSONA////////////////////////////////////////////////////
    public static void editarPersona(Connection conexion,String dni,String apellido,String nombre,java.util.Date fechaNacimiento,int dni2) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE persona SET dni=?,apellido=?,nombre=?,fechaNacimiento=? WHERE idPersona=?");
        java.sql.Date fn=new Date (fechaNacimiento.getTime());
        stmt.setString(1,dni);
        stmt.setString(2,apellido);
        stmt.setString(3,nombre);
        stmt.setDate(4, fn);
        stmt.setInt(5,dni2);
        try{
           stmt.execute();
//           JOptionPane.showMessageDialog(null,"Se Edito Correctamente");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al editar la persona","ERROR",ERROR_MESSAGE);
        }
    }
    public static void editarAlumnosinFoto(Connection conexion,String legajo,String tutor,String cotutor,String celular,String direccion,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE alumno SET legajo=?,tutor=?,cotutor=?,celular=?,direccion=? WHERE idPersona=?");
        stmt.setString(1, legajo);
        stmt.setString(2,tutor);
        stmt.setString(3,cotutor);
        stmt.setString(4,celular);
        stmt.setString(5,direccion);
        stmt.setInt(6,idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se edito correctamente");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al editar los datos del alumno","ERROR",ERROR_MESSAGE);
        }
    }
    public static void editarAlumno(Connection conexion,String legajo,String tutor,String cotutor,String celular,String direccion,String nomImagen,FileInputStream archivoFoto,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE alumno SET legajo=?,tutor=?,cotutor=?,celular=?,direccion=?,nomImagen=?,imagen=? WHERE idPersona=?");
        stmt.setString(1, legajo);
        stmt.setString(2,tutor);
        stmt.setString(3,cotutor);
        stmt.setString(4,celular);
        stmt.setString(5,direccion);
        stmt.setString(6,nomImagen);
        stmt.setBinaryStream(7, archivoFoto);
        stmt.setInt(8,idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se edito correctamente");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al editar los datos del alumno","ERROR",ERROR_MESSAGE);
        }
    }
    ////////////////////////////////////////////////////BORRAR PERSONA//////////////////////////////////////////////////
    public static void borrarAlumno(Connection conexion,int idPersona) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE alumno SET borrado=true WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se dio de baja el alumno");
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al dar de baja al alumno","ERROR",ERROR_MESSAGE);
        }
    }
    /////////////////////////////////////////////////////TABLA PERSONA//////////////////////////////////////////////////
    public static ResultSet MostrarPersona(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo,nombre,apellido,dni,fechaNacimiento FROM persona\n" +
        "inner join alumno on persona.idPersona=alumno.idPersona WHERE borrado=false ORDER BY legajo asc");
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la persona", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    //////////////////////////////////////////////////MOSTRAR PERSONA///////////////////////////////////////////////////
    public static ResultSet MostrarAlumno(Connection conexion,int idPersona)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo,tutor,cotutor,celular,direccion,nomImagen,imagen FROM alumno WHERE idPersona=?");
        stmt.setInt(1, idPersona);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar el alumno", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    ////////////////////////////////////////////////DATOS DEL ALUMNO///////////////////////////////////////////////////
    public static ResultSet traerDatosalu(Connection conexion,int idper)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT dni,legajo FROM persona inner join alumno on persona.idPersona=alumno.idPersona WHERE persona.idPersona=?");
        stmt.setInt(1, idper);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al traer el datos del alumno", "ERROR", ERROR_MESSAGE);
        }
        return rs;
     }
    public static int dniPersona(Connection conexion,int idper) throws Exception{
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
    public static String legPersona(Connection conexion,int idper) throws Exception{
        String legajo=null;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo FROM alumno inner join persona on alumno.idPersona=persona.idPersona WHERE persona.idPersona=?");
        stmt.setInt(1, idper);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                legajo=rs.getString("legajo");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el legajo de la persona", "ERROR", ERROR_MESSAGE);
        }
        return legajo;
    }
    //////////////////////////////////////////////////////ULTIMO LEGAJO///////////////////////////////////////////////////
    public static int leerUltimoIdAlumno(Connection conexion) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT max(idAlumno) FROM alumno"); 
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("max(idAlumno)");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el ultimo id del alumno", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static String capturarLegajo(Connection conexion,int idalu) throws Exception{
        String leg=null;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo FROM alumno WHERE idAlumno=?");
        stmt.setInt(1, idalu);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                leg=rs.getString("legajo");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el ultimo legajo del alumno", "ERROR", ERROR_MESSAGE);
        }
        return leg;
    }
    private Alumno() {
    }
}
