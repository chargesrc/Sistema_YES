/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author cytrex
 */
public class Registros {
    private static final Logger LOG = Logger.getLogger(Registros.class.getName());
    /////////////////////////////////////////////////////EDITAR HORARIO/////////////////////////////////////////////////////
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
    public static int leerIdAlumno(Connection conexion,int dni) throws Exception{
        int idper=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idAlumno FROM alumno inner join persona on alumno.idPersona=persona.idPersona WHERE persona.dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idper=rs.getInt("idAlumno");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del alumno", "ERROR", ERROR_MESSAGE);
        }
        return idper;
    }
    public static int leerIDCAalumno(Connection conexion,int idalu,int idcur,int añolec,String añocur) throws Exception{
        int idca=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idcursoAlumno FROM cursoalumno WHERE idAlumno=? and idCurso=? and añoLectivo=? and añoCurso=?");
        stmt.setInt(1, idalu);
        stmt.setInt(2, idcur);
        stmt.setInt(3, añolec);
        stmt.setString(4, añocur);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idca=rs.getInt("idcursoAlumno");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idca;
    }
    public static void editarHorario(Connection cnx,String hr,int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cursoalumno SET horario=? WHERE idcursoAlumno=? and borrarcurso=false");
        smpt.setString(1, hr);
        smpt.setInt(2,idca);
        try{
            smpt.execute();
            JOptionPane.showMessageDialog(null, "Se edito el horario correctamente");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static void editarEstado(Connection cnx,String est,int idcur) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE alumno SET egresado=? WHERE idAlumno=?");
        smpt.setString(1, est);
        smpt.setInt(2,idcur);
        try{
            smpt.execute();
            JOptionPane.showMessageDialog(null, "Se edito el estado academico correctamente");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    /////////////////////////////////////////////////////MOSTRAR PERSONA////////////////////////////////////////////////////
    public static ResultSet mostrarPersona(Connection conexion)throws Exception{
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
    public static ResultSet mostrarDatosAlumno(Connection conexion,int idca) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT cursoalumno.añoLectivo,cursoalumno.fechaInscripcion,cursoalumno.deudaInscripcion,cursoalumno.horario,cursoalumno.añoCurso,alumno.egresado,cuotainscripcion.estado FROM cursoalumno inner join alumno on cursoalumno.idAlumno=alumno.idAlumno "
                + "inner join cuotainscripcion on cursoalumno.idcursoAlumno=cuotainscripcion.idcursoAlumno WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    /////////////////////////////////////////////////////MOSTRAR CURSO//////////////////////////////////////////////////////
    public static ResultSet mostrarCursos(Connection conexion,int idper) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,cursoalumno.añoLectivo,cursoalumno.horario,cursoalumno.añoCurso FROM curso inner join cursoalumno on curso.idCurso=cursoalumno.idCurso "
                + "inner join alumno on cursoalumno.idAlumno=alumno.idAlumno WHERE alumno.idAlumno=? and cursoalumno.borrarcurso=false");
        stmt.setInt(1, idper);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    public static ResultSet mostrarDatosCurso(Connection conexion,int idcur) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.precioInscripcion,curso.fechaInicio,curso.aula FROM curso WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    ////////////////////////////////////////////////////////LISTA HORARIO///////////////////////////////////////////////////
    public static ResultSet mostrarHorario(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nomHorario FROM horario inner join cursohorario on horario.idHorario=cursohorario.idHorario " +
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
    ///////////////////////////////////////////////////////MOSTRAR PROFESOR/////////////////////////////////////////////////
    public static ResultSet mostrarProfesor(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT persona.nombre,persona.apellido FROM persona inner join profesor on persona.idPersona=profesor.idPersona "
                + "inner join curso on profesor.idProfesor=curso.idProfesor WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al mostrar el profesor","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    ///////////////////////////////////////////////////////BORRAR CURSO/////////////////////////////////////////////////////
    public static void borrarCursoAlumno(Connection conexion,int idca) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE cursoalumno SET borrarcurso=true WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se dio de baja el curso");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    /////////////////////////////////////////////////////RECUPERAR CURSOS/////////////////////////////////////////////////////
    public static void recuperarCursos(Connection conexion,int ida) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE cursoalumno SET borrarcurso=false WHERE cursoalumno.idAlumno=?");
        stmt.setInt(1, ida);
        try{
           stmt.execute();
//           JOptionPane.showMessageDialog(null,"Recuperaron los cursos");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    private Registros() {
    }
}
