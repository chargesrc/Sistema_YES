/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author cytrex
 */
public class Pagar {
    private static final Logger LOG = Logger.getLogger(Pagar.class.getName());
    ///////////////////////////////////////////////////INSERTAR PAGO////////////////////////////////////////////////////////
    public static void insertarPago(Connection cnx,int mes,java.util.Date fechapago,float imp,String estpag, int idca, String estimp) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota SET mes=?,fechaPago=?,importe=?,estado=? WHERE idCursoAlumno=? and estado=? limit 1");
        java.sql.Date fechap=new Date(fechapago.getTime());
        smpt.setInt(1, mes);
        smpt.setDate(2, fechap);
        smpt.setFloat(3, imp);
        smpt.setString(4, estpag);
        smpt.setInt(5, idca);
        smpt.setString(6, estimp);
        try{
            smpt.execute();
            JOptionPane.showMessageDialog(null, "PAGADO");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static int ultimoIDmes(Connection conexion,int idca) throws Exception{
        int idc=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT max(idCuota) FROM cuota WHERE idCursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idc=rs.getInt("max(idCuota)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idc;
    }
    public static String estadoMes(Connection conexion,int idc) throws Exception{
        String est="";
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT estado FROM cuota WHERE idCuota=?");
        stmt.setInt(1, idc);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                est=rs.getString("estado");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return est;
    }
    public static void actualizarCuota(Connection cnx,float interes,int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota SET interes=? WHERE idCursoAlumno=? and interes>0 limit 1");
        smpt.setFloat(1, interes);
        smpt.setInt(2,idca);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static int interesAlumno(Connection conexion, int idca) throws Exception{
        int cts=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT count(idCuota) FROM cuota WHERE idCursoAlumno=? and interes>0");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                cts=rs.getInt("count(idCuota)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return cts;
    }
    public static float buscarCuota(Connection cnx,int idca) throws Exception{
        float ct=0;
        ResultSet rs;
        PreparedStatement smpt=cnx.prepareStatement("SELECT interes FROM cuota WHERE idCursoAlumno=? and interes>0 limit 1");
        smpt.setInt(1,idca);
        try{
            rs=smpt.executeQuery();
            if(rs.next()){
                ct=rs.getFloat("interes");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return ct;
    }
    public static int cuotaImpaga(Connection conexion, int idca,String est) throws Exception{
        int ct=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT count(idCuota) FROM cuota WHERE idCursoAlumno=? and estado=?");
        stmt.setInt(1, idca);
        stmt.setString(2, est);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                ct=rs.getInt("count(idCuota)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return ct;
    }
    //////////////////////////////////////////////////MOSTRAR ALUMNO////////////////////////////////////////////////////
    public static ResultSet mostrarAlumno(Connection conexion) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT legajo,nombre,apellido,dni FROM persona" +
                " inner join alumno on persona.idPersona=alumno.idPersona WHERE alumno.borrado=false ORDER BY legajo asc"); 
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    ////////////////////////////////////////////////////MOSTRAR CURSO///////////////////////////////////////////////////
    public static int leerIDAlu(Connection conexion,int dni) throws Exception{
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
    public static ResultSet mostrarCurso(Connection conexion,int idalu) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,curso.cuota,cursoalumno.añoLectivo,cursoalumno.añoCurso FROM curso inner join cursoalumno on curso.idCurso=cursoalumno.idCurso "
                + "inner join alumno on cursoalumno.idAlumno=alumno.idAlumno WHERE alumno.idAlumno=? and cursoalumno.borrarcurso=false");
        stmt.setInt(1, idalu);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    ////////////////////////////////////////////////////MOSTRAR CUOTAS//////////////////////////////////////////////////
    public static ResultSet mostrarCuotas(Connection conexion,int idca) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT mes,fechaVencimiento,importe,estado,interes,fechaPago FROM cuota WHERE cuota.idCursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    //////////////////////////////////////////////////CALCULAR INTERES//////////////////////////////////////////////////
    public static ResultSet leerFechaVenc(Connection conexion,int idca,String est) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT day(fechaVencimiento),month(fechaVencimiento),year(fechaVencimiento) FROM cuota WHERE cuota.idCursoAlumno=? and cuota.estado=?");
        stmt.setInt(1, idca);
        stmt.setString(2, est);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    public static int mesPagado(Connection conexion,int idca,String fechav,String est) throws Exception{
        int idc=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idCuota FROM cuota WHERE idCursoAlumno=? and fechaVencimiento=? and estado=?");
        stmt.setInt(1, idca);
        stmt.setString(2, fechav);
        stmt.setString(3, est);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idc=rs.getInt("idCuota");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idc;
    }
    public static float buscarInteres(Connection conexion,int idc) throws Exception{
        float interes=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT interes FROM cuota WHERE idCuota=?");
        stmt.setInt(1, idc);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                interes=rs.getInt("interes");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return interes;
    }
    ///////////////////////////////////////////////GESTIONAR INTERES//////////////////////////////////////////////////////////
    public static float interesCurso(Connection conexion,int idcur) throws Exception{
        float interes=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT interesCurso FROM curso WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                interes=rs.getInt("interesCurso");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return interes;
    }
    public static void insertarInteres(Connection cnx,float interes,int idc) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota SET interes=? WHERE cuota.idCuota=?");
        smpt.setFloat(1, interes);
        smpt.setInt(2,idc);
        try{
            smpt.execute();
//            JOptionPane.showMessageDialog(null, "Se agrego el interes de la cuota del curso");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static float buscarDeuda(Connection conexion,int idca) throws Exception{
        float deuda=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT saldoAdeudor FROM cursoalumno WHERE idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                deuda=rs.getInt("saldoAdeudor");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return deuda;
    }
    public static void insertarDeuda(Connection cnx,float deuda,int idca) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cursoalumno SET saldoAdeudor=? WHERE idcursoAlumno=?");
        smpt.setFloat(1, deuda);
        smpt.setInt(2,idca);
        try{
            smpt.execute();
//            JOptionPane.showMessageDialog(null, "DEUDA AGREGADA");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    //////////////////////////////////////////////////////PERFIL ALUMNO/////////////////////////////////////////////////
    public static ResultSet buscarPerfil(Connection conexion,int idalu) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nomImagen,imagen FROM alumno WHERE idAlumno=?");
        stmt.setInt(1, idalu);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    /////////////////////////////////////////////////TABLA AÑOS/////////////////////////////////////////////////////////
    public static int buscarIDCA(Connection conexion,int idcur,int idalu,int curs,String añocurso) throws Exception{
        int idca=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idcursoAlumno FROM cursoalumno WHERE idCurso=? and idAlumno=? and añoLectivo=? and añoCurso=?");
        stmt.setInt(1, idcur);
        stmt.setInt(2, idalu);
        stmt.setInt(3, curs);
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
    public static int leerIDCurso(Connection conexion,String nombre) throws Exception{
        int idc=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idCurso FROM curso WHERE nombre=?");
        stmt.setString(1, nombre);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idc=rs.getInt("idCurso");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idc;
    }
    public static int ciclos(Connection conexion,int idca) throws Exception{
        int cl=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT count(idCursoAlumno) FROM cuota WHERE cuota.idCursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                cl=rs.getInt("count(idCursoAlumno)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return cl;
    }
    /////////////////////////////////////////////////////ITEM BORRAR PAGO///////////////////////////////////////////////////
    public static void deshacerPago(Connection cnx,String est, int idcuo) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota set mes=null,fechaPago=null,importe=null,estado=?, borrarpago=true WHERE idCuota=?");
        smpt.setString(1, est);
        smpt.setInt(2, idcuo);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    ////////////////////////////////////////////////////ITEM BORRAR DEUDA///////////////////////////////////////////////
    public static void guardarInteres(Connection cnx,float inter,int idc) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota SET faltante=? WHERE idCuota=?");
        smpt.setFloat(1, inter);
        smpt.setInt(2, idc);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static void perdonar(Connection cnx,float inter,int idc) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("UPDATE cuota SET interes=?, perdonar=true WHERE idCuota=?");
        smpt.setFloat(1, inter);
        smpt.setInt(2, idc);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    public static boolean leerPerd(Connection conexion,int idc) throws Exception{
        boolean per=false;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT perdonar FROM cuota WHERE idCuota=?");
        stmt.setInt(1, idc);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                per=rs.getBoolean("perdonar");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return per;
    }
    public static float recuperarInteres(Connection conexion,int idca) throws Exception{
        float interes=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT faltante FROM cuota WHERE idCuota=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                interes=rs.getFloat("faltante");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return interes;
    }
    public static void deshacerDeuda(Connection conexion,float interes, int idc)throws Exception{
        PreparedStatement smpt=conexion.prepareStatement("UPDATE cuota SET interes=?, perdonar=false WHERE idCuota=?");
        smpt.setFloat(1, interes);
        smpt.setInt(2, idc);
        try{
            smpt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    /////////////////////////////////////////////////////ITEM BORRAR CURSO///////////////////////////////////////////
    public static void borrarCursoAlumno(Connection conexion,int idca) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE cursoalumno SET borrarcurso=true WHERE idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se dio de baja el curso");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    ////////////////////////////////////////////////DATOS PARA EL INFORME//////////////////////////////////////////////
    public static int maxPago(Connection conexion,int idca,String est) throws Exception{
        int idc=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT max(idCuota) FROM cuota WHERE cuota.idCursoAlumno=? and cuota.estado=?");
        stmt.setInt(1, idca);
        stmt.setString(2, est);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idc=rs.getInt("max(idCuota)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idc;
    }
    public static int pagoBorrado(Connection conexion,int idca,String est) throws Exception{
        int idcb=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT min(idCuota) AS minimo FROM cuota WHERE cuota.idCursoAlumno=? and cuota.estado=? and cuota.borrarpago=true");
        stmt.setInt(1, idca);
        stmt.setString(2, est);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idcb=rs.getInt("minimo");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idcb;
    }
    public static ResultSet buscarInforme(Connection conexion,int idc) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT mes, fechavencimiento, fechapago, importe, interes FROM cuota WHERE idCuota=?");
        stmt.setInt(1, idc);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    public static void actualizarBorrado(Connection conexion,int idc)throws Exception{
        PreparedStatement smpt=conexion.prepareStatement("UPDATE cuota SET borrarpago=false WHERE idCuota=?");
        smpt.setInt(1, idc);
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
    ////////////////////////////////////////////////////NO ES NECESARIO////////////////////////////////////////////////
    public static void borrarCuotas(Connection conexion,int idca) throws Exception{ 
        PreparedStatement stmt=conexion.prepareStatement("DELETE FROM cuota WHERE cuota.idCursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            stmt.execute();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    private Pagar() {
    }
}
