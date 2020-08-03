/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
 * @author cytrex
 */
public class Horario {
    private static final Logger LOG = Logger.getLogger(Horario.class.getName());
    ///////////////////////////////////////////////////INSERTAR HORARIO////////////////////////////////////////////////////
    public static int validarHorario(Connection conexion,String nombre,String nombCurso) throws Exception{
        int idcurso=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.idCurso FROM curso inner join cursohorario on curso.idCurso=cursohorario.idCurso "
                + "inner join horario on cursohorario.idHorario=horario.idHorario WHERE horario.nomHorario=? and curso.nombre=?");
        stmt.setString(1, nombre);
        stmt.setString(2, nombCurso);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idcurso=rs.getInt("curso.idCurso");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el horario del curso", "ERROR", ERROR_MESSAGE);
        }
        return idcurso;
    }
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
    public static void insertarCursoHorario(Connection conexion,int idCurso,int idHorario) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO cursoHorario(idCurso,idHorario)VALUES(?,?)");
        stmt.setInt(1,idCurso);
        stmt.setInt(2,idHorario);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se agrego el horario al curso correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al crear el curso con su horario", "ERROR", ERROR_MESSAGE);
        }
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
    public static boolean leerBorrado(Connection conexion,int idhorario) throws Exception{
        boolean dis=true;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT disponible FROM horario WHERE idHorario=?");
        stmt.setInt(1, idhorario);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                dis=rs.getBoolean("disponible");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer el borrado del horario", "ERROR", ERROR_MESSAGE);
        }
        return dis;
    }
    public static void reinsertarHorario(Connection conexion,int idHorario) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE horario SET disponible=true WHERE idHorario=?");
        stmt.setInt(1, idHorario);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Se recupero el horario");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al recuperar el horario", "ERROR", ERROR_MESSAGE);
        }
    }
    /////////////////////////////////////////////////////LISTA HORARIO//////////////////////////////////////////////////
    public static ResultSet MostrarHorario(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nomHorario FROM horario WHERE disponible=true");
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar los horarios", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    public static void insertarHorario(Connection conexion,String nomHorario,boolean disponible) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO Horario(nomHorario,disponible)VALUES(?,?)");
        stmt.setString(1,nomHorario);
        stmt.setBoolean(2, disponible);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se cargo el horario correctamente a la lista");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al crear el horario", "ERROR", ERROR_MESSAGE);
        }
    }
    //////////////////////////////////////////////////BORRAR HORARIO////////////////////////////////////////////////////
    public static int leerIdC(Connection conexion,int idhorario) throws Exception{
        int idc=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idCurso FROM cursohorario WHERE idHorario=?");
        stmt.setInt(1, idhorario);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idc=rs.getInt("idCurso");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del curso", "ERROR", ERROR_MESSAGE);
        }
        return idc;
    }
    public static void borrarHorario(Connection conexion,int idHorario) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE horario SET disponible=false WHERE idHorario=?");
        stmt.setInt(1, idHorario);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null, "Se borro el horario");
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al borrar el horario", "ERROR", ERROR_MESSAGE);
        }
    }
    
    private Horario() {
    }
}
