
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

public class Inscripcion {
    private static final Logger LOG = Logger.getLogger(Inscripcion.class.getName());
    ////////////////////////////////////////////INSERTAR INSCRIPCION////////////////////////////////////////////////////
    public static int leerIdAlumno(Connection conexion,int dni) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idAlumno FROM alumno inner join persona on alumno.idPersona=persona.idPersona WHERE dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idAlumno");
            }
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al leer id del alumno", "ERROR", ERROR_MESSAGE);
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
    public static void insertarCursoAlumno(Connection conexion,int idCurso,int idAlumno,int añoLectivo,float deuda,java.util.Date fechaInscripcion,Float precio,String horario,String añocurso,boolean borrar) throws Exception{
        java.sql.Date fechaIn=new Date (fechaInscripcion.getTime());
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO cursoalumno(idCurso,idAlumno,añoLectivo,saldoAdeudor,fechaInscripcion,deudaInscripcion,horario,añoCurso,borrarcurso) VALUES (?,?,?,?,?,?,?,?,?)");
        stmt.setInt(1, idCurso);
        stmt.setInt(2, idAlumno);
        stmt.setInt(3, añoLectivo);
        stmt.setFloat(4, deuda);
        stmt.setDate(5,fechaIn);
        stmt.setFloat(6, precio);
        stmt.setString(7, horario);
        stmt.setString(8, añocurso);
        stmt.setBoolean(9, borrar);
        try{
            stmt.execute();
            JOptionPane.showMessageDialog(null,"Se inscribio al alumno correctamente");
        }
        catch(HeadlessException | SQLException e){
            JOptionPane.showMessageDialog(null, "Error al inscribir el alumno", "ERROR", ERROR_MESSAGE);
        }
    }
    public static void insertarPagoIns(Connection conexion,int idca,int idalu,String estado,boolean borrar)throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("INSERT INTO cuotainscripcion(idCursoAlumno,idAlumno,estado,borrarcuota) VALUES(?,?,?,?)");
        stmt.setInt(1, idca);
        stmt.setInt(2, idalu);
        stmt.setString(3, estado);
        stmt.setBoolean(4, borrar);
        try{
            stmt.execute();
        }
        catch(SQLException  ex){
            JOptionPane.showMessageDialog(null, "No se pudo agregar parte del pago de la inscripcion");
        }
    }
    public static int ultimoIDCA(Connection conexion) throws Exception{
        int idca=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT max(idCursoAlumno) FROM cursoalumno limit 1");
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                idca=rs.getInt("max(idCursoAlumno)");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idca;
    }
    /////////////////////////////////////////////GENERAR CUOTAS INSCRIPCION/////////////////////////////////////////////
    public static ResultSet leerFechaInsCrp(Connection conexion,int idca) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT day(fechaInscripcion),month(fechaInscripcion),year(fechaInscripcion) FROM cursoalumno WHERE idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    public static ResultSet leerFechaIni(Connection conexion,int idcur) throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT day(fechaInicio),month(fechaInicio),year(fechaInicio) FROM curso WHERE idCurso=?");
        stmt.setInt(1, idcur);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return rs;
    }
    public static void insertarCuota(Connection cnx,int idcursoalu,String fechavenc,String est,float interes,float faltante,boolean despago,boolean per) throws Exception{
        PreparedStatement smpt=cnx.prepareStatement("INSERT INTO cuota (idCursoAlumno,fechaVencimiento,estado,interes,faltante,borrarpago,perdonar) VALUES (?,?,?,?,?,?,?)");
//        java.sql.Date fechav=new Date(fechavenc.getTime());
        //Enviamos la fecha como string porque se debia calcular el vencimiento
        smpt.setInt(1, idcursoalu);
        smpt.setString(2, fechavenc);
        smpt.setString(3, est);
        smpt.setFloat(4, interes);
        smpt.setFloat(5, faltante);
        smpt.setBoolean(6,despago);
        smpt.setBoolean(7, per);
        try{
            smpt.execute();
//            JOptionPane.showMessageDialog(null, "Se agrego la cuota");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    ///////////////////////////////////////////////////MOSTRAR CURSOS///////////////////////////////////////////////////
    public static ResultSet mostrarCursos(Connection conexion)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nombre,precioInscripcion,cuota,cantidad,fechaInicio FROM curso WHERE curso.idProfesor>0");
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException e){
            JOptionPane.showMessageDialog(null,"Error al mostrar los cursos","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    /////////////////////////////////////////////////////LISTA HORARIOS/////////////////////////////////////////////////
    public static ResultSet MostrarCursoHorario(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT nomHorario FROM horario inner join cursohorario on horario.idHorario=cursoHorario.idHorario\n" +
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
    ///////////////////////////////////////////COMPROBAR INSCRIPCION ALUMNO/////////////////////////////////////////////
    public static int leerDatosInsc(Connection conexion,int ida,int idcur,int añolect,String añocurso) throws Exception{
        int idca=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idcursoAlumno FROM cursoalumno inner join curso on cursoalumno.idCurso=curso.idCurso WHERE cursoalumno.idAlumno=? and cursoalumno.idCurso=? and cursoalumno.añoLectivo=? and cursoalumno.añoCurso=?");
        stmt.setInt(1, ida);
        stmt.setInt(2, idcur);
        stmt.setInt(3, añolect);
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
    //////////////////////////////////////////////////////MOSTRAR PERSONA///////////////////////////////////////////////
    public static ResultSet MostrarPersona(Connection conexion,int dni)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT alumno.legajo,persona.nombre,persona.apellido FROM persona\n" +
        "inner join alumno on persona.idPersona=alumno.idPersona WHERE dni=? and borrado=false");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
        }
        catch(SQLException e){
            JOptionPane.showMessageDialog(null, "Error al mostrar la persona", "ERROR", ERROR_MESSAGE);
        }
        return rs;
    }
    ///////////////////////////////////////////////////////MOSTRAR CURSO////////////////////////////////////////////////
    public static ResultSet mostrarCurso(Connection conexion,int idcur)throws Exception{
        ResultSet rs=null;
        PreparedStatement stmt=conexion.prepareStatement("SELECT curso.nombre,curso.precioInscripcion,cuota,cantidad,fechaInicio,dni,persona.nombre,apellido,cantidadAño,interesCurso,aula FROM curso\n" +
        "inner join profesor on curso.idProfesor=profesor.idProfesor inner join persona on profesor.idPersona=persona.idPersona WHERE curso.idCurso=?");
        stmt.setInt(1, idcur);
        try {
            rs=stmt.executeQuery();
        }
        catch (SQLException ex) {
            JOptionPane.showMessageDialog(null,"Error al mostrar el curso","ERROR",ERROR_MESSAGE);
        }
        return rs;
    }
    /////////////////////////////////////////////////RECUPERAR CURSO////////////////////////////////////////////////////
    public static int leerAño(Connection conexion,int idca) throws Exception{
        int year=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT añoLectivo FROM cursoalumno WHERE idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                year=rs.getInt("añoLectivo");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return year;
    }
    public static boolean leerCursoBorrado(Connection conexion,int idca) throws Exception{
        boolean borrado=false;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT borrarcurso FROM cursoalumno WHERE idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                borrado=rs.getBoolean("borrarcurso");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return borrado;
    }
    public static void recuperarCurso(Connection conexion,int idca) throws Exception{
        PreparedStatement stmt=conexion.prepareStatement("UPDATE cursoalumno SET borrarcurso=false WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
           stmt.execute();
           JOptionPane.showMessageDialog(null,"Se recupero el curso");
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }
    //////////////////////////////////////////////////LEER HORARIO//////////////////////////////////////////////////////
    public static String leerHorario(Connection conexion,int idca) throws Exception{
        String horario="null";
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT horario FROM cursoalumno WHERE cursoalumno.idcursoAlumno=?");
        stmt.setInt(1, idca);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                horario=rs.getString("horario");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return horario;
    }
    //////////////////////////////////////////////////NO SON NECESARIOS///////////////////////////////////////////////////
    public static int buscarPersona(Connection conexion,int dni) throws Exception{
        int id=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT alumno.idPersona FROM alumno inner join persona on alumno.idPersona=persona.idPersona WHERE persona.dni=?");
        stmt.setInt(1, dni);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                id=rs.getInt("idPersona");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return id;
    }
    public static int buscarCantidad(Connection conexion,int dni,int idcurso) throws Exception{
        int cantidad=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT cantidad FROM curso inner join cursoalumno on curso.idCurso=cursoalumno.idCurso "
                + "inner join alumno on cursoalumno.idAlumno=alumno.idAlumno "
                + "inner join persona on alumno.idPersona=persona.idPersona WHERE persona.dni=? and curso.idCurso=?");
        stmt.setInt(1, dni);
        stmt.setInt(2, idcurso);
        try{
            rs=stmt.executeQuery();
            if(rs.next()){
                cantidad=rs.getInt("cantidad");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return cantidad;
    }
    public static int buscarIDCAalumnoCuota(Connection conexion,int dni,java.util.Date fechains,String añocurso,int añolec) throws Exception{
        int idcursoalu=0;
        ResultSet rs;
        PreparedStatement stmt=conexion.prepareStatement("SELECT idcursoAlumno FROM cursoalumno inner join alumno on cursoalumno.idAlumno=alumno.idAlumno "
                + "inner join persona on alumno.idPersona=persona.idPersona WHERE persona.dni=? and cursoalumno.fechaInscripcion=? and cursoalumno.añoCurso=? and cursoalumno.añoLectivo=?");
        java.sql.Date fechaIn=new Date (fechains.getTime());
        stmt.setInt(1, dni);
        stmt.setDate(2, fechaIn);
        stmt.setString(3, añocurso);
        stmt.setInt(4, añolec);
        try{
            rs = stmt.executeQuery();
            if(rs.next()){
                idcursoalu=rs.getInt("idcursoAlumno");
            }
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
        return idcursoalu;
    }

    private Inscripcion() {
    }
}
