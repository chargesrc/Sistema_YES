
package clase;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.Date;
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
public class Curso {
    private static final Logger LOG = Logger.getLogger(Curso.class.getName());
    //////////////////////////////////////////////////INSERTAR CURSO////////////////////////////////////////////////////
    public static int leerIdProfesor(Connection conexion,int dni) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idProfesor FROM profesor\n" +
        "inner join persona on profesor.idPersona=persona.idPersona WHERE dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idProfesor");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del profesor", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static int leerIdCurso(Connection conexion,String nombre) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idCurso FROM curso WHERE nombre=?");
        stmt.setString(1, nombre);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idCurso");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del curso", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static void insertarCurso(Connection conexion,int idProfesor,String nombre,float precio,float cuota,int cantidad,java.util.Date fechaInicio,int cantidadAño,float interesCurso,String aula) throws Exception{
        java.sql.Date fechaI=new Date (fechaInicio.getTime());
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO curso(idProfesor,nombre,precioInscripcion,cuota,cantidad,fechaInicio,cantidadAño,interesCurso,aula)VALUES(?,?,?,?,?,?,?,?,?)");
        stmt.setInt(1,idProfesor);
        stmt.setString(2, nombre);
        stmt.setFloat(3, precio);
        stmt.setFloat(4,cuota);
        stmt.setInt(5,cantidad);
        stmt.setDate(6, fechaI);
        stmt.setInt(7,cantidadAño);
        stmt.setFloat(8,interesCurso);
        stmt.setString(9,aula);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se creo el curso correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al crear el curso", "ERROR", ERROR_MESSAGE);
        }
    }
    ///////////////////////////////////////////////////EDITAR CURSO/////////////////////////////////////////////////////
    public static void EditarCurso(Connection conexion,int idProfesor,String nombre,float precio,float cuota,int cantidad,java.util.Date fechaInicio,int cantaño,float interes,String aula,int idCurso) throws Exception{
        java.sql.Date fechaI=new Date (fechaInicio.getTime());
        PreparedStatement stmt=conexion.prepareStatement("UPDATE curso SET idProfesor=?,nombre=?,precioInscripcion=?,cuota=?,cantidad=?,fechaInicio=?,cantidadAño=?,interesCurso=?,aula=? WHERE idCurso=?");
        stmt.setInt(1,idProfesor);
        stmt.setString(2, nombre);
        stmt.setFloat(3, precio);
        stmt.setFloat(4,cuota);
        stmt.setInt(5,cantidad);
        stmt.setDate(6, fechaI);
        stmt.setInt(7, cantaño);
        stmt.setFloat(8, interes);
        stmt.setString(9, aula);
        stmt.setInt(10,idCurso);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se edito el curso correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al editar el curso", "ERROR", ERROR_MESSAGE);
        }
    }
    //////////////////////////////////////////////////TABLA CURSO///////////////////////////////////////////////////////
    public static ResultSet mostrarCurso(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,curso.precioInscripcion,curso.cuota,curso.cantidad,curso.fechaInicio,persona.dni,persona.nombre,persona.apellido,curso.cantidadAño,curso.interesCurso,curso.aula FROM curso\n" +
        "inner join profesor on curso.idProfesor=profesor.idProfesor\n" +
        "inner join persona on profesor.idPersona=persona.idPersona WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al mostrar el curso","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    ////////////////////////////////////////////TABLA CURSO SIN PROFESOR////////////////////////////////////////////////
    public static ResultSet mostrarCursoSinP(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,precioInscripcion,cuota,cantidad,fechaInicio,cantidadAño,interesCurso,aula FROM curso WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e) {
            JOptionPane.showMessageDialog(null,"Error al mostrar el curso","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    public static int leerIdProf(Connection conexion,int idcurso) throws Exception{
        int idprof=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idProfesor FROM curso WHERE idCurso=?");
        stmt.setInt(1, idcurso);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idprof=rs.getInt("idProfesor");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, "Error al leer id del profesor", "ERROR", ERROR_MESSAGE);
        }
        return idprof;
    }
    /////////////////////////////////////////////////////////MOSTRAR CURSOS/////////////////////////////////////////////
    public static ResultSet mostrarCursos(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,cuota,cantidad,fechaInicio FROM curso WHERE curso.idProfesor>0");
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al mostrar los cursos","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    ///////////////////////////////////////////////MOSTRAR CURSOS SIN PROFESOR//////////////////////////////////////////
    public static ResultSet mostrarCursosSinProf(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,cuota,cantidad,fechaInicio FROM curso WHERE curso.idProfesor<1");
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al mostrar los cursos sin profesor","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    //////////////////////////////////////////////////////LISTA HORARIOS////////////////////////////////////////////////
    public static ResultSet MostrarCursoHorario(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nomHorario FROM horario\n" +
        "inner join cursohorario on horario.idHorario=cursoHorario.idHorario\n" +
        "inner join curso on cursohorario.idCurso=curso.idCurso WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar los cursos con sus horarios", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    //////////////////////////////////////////////////////MOSTRAR PROFESOR//////////////////////////////////////////////
    public static ResultSet mostrarProfesor(Connection conexion,int idprof)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,apellido FROM persona \n" +
        "inner join profesor on persona.idPersona=profesor.idPersona WHERE profesor.idProfesor=?");
        stmt.setInt(1, idprof);
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al mostrar el profesor","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    //////////////////////////////////////////////////////BORRAR HORARIO////////////////////////////////////////////////
    public static int leerIdHorario(Connection conexion,String nomHorario) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idHorario FROM horario WHERE nomHorario=?");
        stmt.setString(1, nomHorario);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idHorario");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del horario", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static void BorrarCursoHorario(Connection conexion,int idCurso,int idHorario) throws Exception{ 
        PreparedStatement stmt=conexion.prepareStatement("DELETE FROM cursohorario WHERE cursohorario.idCurso=? and cursohorario.idHorario=?");
        stmt.setInt(1, idCurso);
        stmt.setInt(2, idHorario);
        try{
            stmt.execute();
//            JOptionPane.showMessageDialog(null,"Se borro el horario");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al borrar el horario", "ERROR", ERROR_MESSAGE);
        }
    }
    ///////////////////////////////////////////////////DISASOCIAR PROFESOR CURSO////////////////////////////////////////
    public static void borrarCursoProfesor(Connection conexion,int idPersona,int idcur) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE curso as c inner join profesor as p on c.idProfesor=p.idProfesor inner join persona as a on p.idPersona=a.idPersona SET c.idProfesor=? WHERE c.idCurso=?");
        stmt.setInt(1, idPersona);
        stmt.setInt(2, idcur);
        try{
           stmt.execute();
        }
        catch(HeadlessException | SQLException ex){
            JOptionPane.showMessageDialog(null,"Error al dar de baja el curso-profesor","ERROR",ERROR_MESSAGE);
        }
    }

    private Curso() {
    }
}