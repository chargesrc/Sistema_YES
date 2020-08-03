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
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;

/**
 *
 * @author cytrex
 */
public class Estado {
    /////////////////////////////////////////////////////MOSTRAR PERSONA//////////////////////////////////////////////////
    public static ResultSet MostrarPersona(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo,nombre,apellido,dni FROM persona\n" +
        "inner join alumno on persona.idPersona=alumno.idPersona WHERE borrado=false ORDER BY legajo asc");
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la persona", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    /////////////////////////////////////////////////////MOSTRAR CURSO//////////////////////////////////////////////////////
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
            JOptionPane.showMessageDialog(null, "Error al leer id del curso", "ERROR", ERROR_MESSAGE);
        }
        return id;
    }
    public static ResultSet mostrarCursos(Connection conexion,int idper) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,cursoalumno.añoLectivo,cursoalumno.deudaInscripcion,cursoalumno.añoCurso,cuotainscripcion.estado FROM curso"
                + " inner join cursoalumno on curso.idCurso=cursoalumno.idCurso inner join cuotainscripcion on cursoalumno.idCursoAlumno=cuotainscripcion.idCursoAlumno"
                + " inner join alumno on cursoalumno.idAlumno=alumno.idAlumno inner join persona on alumno.idPersona=persona.idPersona WHERE persona.idPersona=? and cursoalumno.borrarcurso=false and cuotainscripcion.borrarcuota=false");
        stmt.setInt(1, idper);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    ////////////////////////////////////////////////////MOSTRAR ESTADO///////////////////////////////////////////////////////
    public static ResultSet mostrarEstado(Connection conexion,int idca) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,alumno.egresado,curso.precioInscripcion,cuotaInscripcion.estado,cuotaInscripcion.importe,cuotaInscripcion.fechaPago FROM curso"
                + " inner join cursoalumno on curso.idCurso=cursoalumno.idCurso inner join alumno on cursoalumno.idAlumno=alumno.idAlumno inner join "
                + "cuotainscripcion on cursoalumno.idCursoAlumno=cuotainscripcion.idCursoAlumno WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    ///////////////////////////////////////////////BUSCAR IDCA///////////////////////////////////////////////////////////////
    public static int buscarIDCA(Connection conexion,int idalu,int idcur,int año,String añocurso) throws Exception{
        int idca=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idcursoAlumno FROM cursoalumno WHERE idAlumno=? and idCurso=? and añoLectivo=? and añoCurso=?");
        stmt.setInt(1, idalu);
        stmt.setInt(2, idcur);
        stmt.setInt(3, año);
        stmt.setString(4, añocurso);
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
    public static int buscarIDAlu(Connection conexion,int dni) throws Exception{
        int idalu=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idAlumno FROM alumno inner join persona on alumno.idPersona=persona.idPersona WHERE persona.dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idalu=rs.getInt("idAlumno");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idalu;
    }
    public static int leerIDCurso(Connection conexion,String nom) throws Exception{
        int idcur=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idCurso FROM curso WHERE nombre=?");
        stmt.setString(1, nom);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idcur=rs.getInt("idCurso");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idcur;
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
    /////////////////////////////////////////////////////MOSTRAR DEUDA/////////////////////////////////////////////////////////
    public static float buscarDeuda(Connection conexion,int idca) throws Exception{
        float deuda=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT deudaInscripcion FROM cursoalumno WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                deuda=rs.getInt("deudaInscripcion");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return deuda;
    }
    ////////////////////////////////////////////////////ACTUALIZAR DEUDA//////////////////////////////////////////////////////
    public static void actualizarDeuda(Connection cnx,float deuda,int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cursoalumno set deudaInscripcion=? WHERE idcursoAlumno=?");
        smpt.setFloat(1, deuda);
        smpt.setInt(2,idca);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static void insertarPagoIns(Connection cnx,java.util.Date fechapago,float imp,String est, int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuotainscripcion SET fechaPago=?,importe=?,estado=? WHERE idcursoAlumno=? limit 1");
        java.sql.Date fechap=new java.sql.Date(fechapago.getTime());
        smpt.setDate(1, fechap);
        smpt.setFloat(2, imp);
        smpt.setString(3, est);
        smpt.setInt(4, idca);
        try{
            smpt.execute();
            JOptionPane.showMessageDialog(null, "PAGADO");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    /////////////////////////////////////////////////////BORRAR PAGO ITEM//////////////////////////////////////////////////
    public static int leerIDPrecio(Connection conexion,int idca,java.sql.Date fechapago,String est) throws Exception{
        int idpi=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idPrecioInscripcion FROM cuotainscripcion WHERE idCursoAlumno=? and fechaPago=? and estado=?");
        stmt.setInt(1, idca);
        stmt.setDate(2, fechapago);
        stmt.setString(3, est);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idpi=rs.getInt("idPrecioInscripcion");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idpi;
    }
    public static void deshacerPago(Connection cnx,String est, int idpi) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuotainscripcion SET fechaPago=null,importe=null,estado=? WHERE idPrecioInscripcion=?");
        smpt.setString(1, est);
        smpt.setInt(2, idpi);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    //////////////////////////////////////////////BUSCAR PARTE INFORME//////////////////////////////////////////////////////
    public static ResultSet buscarInforme(Connection conexion,int idca) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT fechapago, importe FROM cuotainscripcion WHERE idCursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    //////////////////////////////////////////////OCULTAR CUOTA INNECESARIA/////////////////////////////////////////////////
    public static void ocultarCuota(Connection cnx,int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuotainscripcion SET borrarcuota=true WHERE idCursoAlumno=?");
        smpt.setInt(1, idca);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static void mostrarCuotas(Connection cnx,int ida) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuotainscripcion SET borrarcuota=false WHERE cuotainscripcion.idAlumno=?");
        smpt.setInt(1, ida);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    /////////////////////////////////////////////////////RECUPERAR CURSOS///////////////////////////////////////////////////
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
}
