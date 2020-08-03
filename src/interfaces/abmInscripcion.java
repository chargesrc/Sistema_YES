/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
import static interfaces.abmPrincipal.panelA;
import java.awt.Dimension;
import java.awt.event.KeyEvent;
import java.sql.Connection;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author joni
 */
public class abmInscripcion extends javax.swing.JInternalFrame {
    Connection conex=conexion.Conexion.conectar();
    ResultSet rs;
    Object[] datos=new Object[4];
    private final DefaultListModel modelo1=new DefaultListModel();
    DefaultTableModel modelo=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    java.util.Date date=new java.util.Date();
    LocalDate localDate=date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    
    int añoactual=localDate.getYear();
    int idca,idcurso,idalumno,años,meses=0;
    String horario,añocurso;
    boolean borrado;
    /////////////Variables para calcular la fecha de vencimiento////////////////
    ResultSet fechainsc,fechaini;
    int diains,mesins,añoins;
    int diaini,mesini,añoini;
    String diavenc,mesvenc,añovenc,fechavenc;
    int res=0,resmes=0,interes=0,messup=0,mesuno=1;
    int diacuotagen=25;//Esta variable determina el dia en que una cuota se genera o no. Si el sujeto se inscribe del 1 al 25 del mes de inscripcion se creara la cuta a pagar de ese mes. En cambio si se inscribe despues del 25 no se generara la cuota a pagar de ese mes.
    String estado="IMPAGA";
    /**
     * Creates new form Inscripcion
     */
    public abmInscripcion() {
        initComponents();  
    }
    
    public void mostrarCursos(){
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        modelo.addColumn("NOMBRE");
        modelo.addColumn("CUOTA");
        modelo.addColumn("FECHA DE INICIO");
        modelo.addColumn("PRECIO DE INSCRIPCION");
        try {
            tableCurso.setModel(modelo);
            rs=clase.Inscripcion.mostrarCursos(conex);
            while(rs.next()){
                datos[0]=rs.getString("nombre");
                datos[1]=rs.getFloat("cuota");
                datos[2]=rs.getDate("fechaInicio");
                datos[3]=rs.getFloat("precioInscripcion");
                modelo.addRow(datos);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void cargarListaCursoHorario(){
        try {
           rs=clase.Inscripcion.MostrarCursoHorario(conex, idcurso);
           modelo1.removeAllElements();
            while(rs.next()){
                modelo1.addElement(rs.getString(1));
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void comprobarinsc(){
        añocurso=cmbAños.getSelectedItem().toString();
        años=añoYears.getYear();
        try{
            idalumno=clase.Inscripcion.leerIdAlumno(conex,Integer.parseInt(dniTxt.getText()));
            idca=clase.Inscripcion.leerDatosInsc(conex, idalumno,idcurso,años,añocurso);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarDatosIns(){
        try {
            idcurso=clase.Inscripcion.leerIdCurso(conex, cursoTxt.getText());
            rs=clase.Inscripcion.mostrarCurso(conex,idcurso);
            while(rs.next()){
                txtprecioins.setText("$ ".concat(String.valueOf(rs.getFloat(2))));
                meses=rs.getInt(4);
                profesorTxt.setText(rs.getString(7).concat(" ").concat(rs.getString(8)));
                cmbAños.removeAllItems();
                años=rs.getInt(9);
                int i=1;
                while(i<=años){
                    int j=i;
                    if(j==1){
                        cmbAños.addItem("primero");
                    }
                    if(j==2){
                        cmbAños.addItem("segundo");
                    }
                    if(j==3){
                        cmbAños.addItem("tercero");
                    }
                    if(j==4){
                        cmbAños.addItem("cuarto");
                    }
                    if(j==5){
                        cmbAños.addItem("quinto");
                    }
                    i++;
                }
                aulaTxt.setText(rs.getString(11));
            }
            limpiarDatosIns();
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarMasDatosIns(){
        try {
            idcurso=clase.Inscripcion.leerIdCurso(conex, cursoTxt.getText());
            rs=clase.Inscripcion.mostrarCurso(conex, idcurso);
            cmbAños.removeAllItems();
            if (rs.next()) {
                txtprecioins.setText("$ ".concat(String.valueOf(rs.getFloat(2))));
                presioTxt.setText("$ ".concat(String.valueOf(rs.getFloat(3))));
                meses=rs.getInt(4);
                fechaInicio.setDate((rs.getDate(5)));
//                txtfechaini.setText(rs.getDate(5).toString());//Si desea cambiar el objeto fecha para mostrar la fecha de inicio en un cuadro de texto
                profesorTxt.setText(rs.getString(7).concat(" ").concat(rs.getString(8)));
                años=rs.getInt(9);
                int i=1;
                while(i<=años){
                    int j=i;
                    if(j==1){
                        cmbAños.addItem("primero");
                    }
                    if(j==2){
                        cmbAños.addItem("segundo");
                    }
                    if(j==3){
                        cmbAños.addItem("tercero");
                    }
                    if(j==4){
                        cmbAños.addItem("cuarto");
                    }
                    if(j==5){
                        cmbAños.addItem("quinto");
                    }
                    i++;
                }
                aulaTxt.setText(rs.getString(11));
                limpiarMasDatosIns();
            }
            else{
                JOptionPane.showMessageDialog(this,"No existe el curso que ingreso: ".concat(cursoTxt.getText().toUpperCase()),"ERROR",ERROR_MESSAGE);
                limpiarcurso();
                cursoTxt.requestFocus(true);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void recuperarcurso(){
        try{
            años=clase.Inscripcion.leerAño(conex, idca);
            borrado=clase.Inscripcion.leerCursoBorrado(conex, idca);
            if(borrado==true){
                int op=JOptionPane.showConfirmDialog(this, "El curso ".concat(cursoTxt.getText()).concat(" fue eliminado del alumno ").concat(nombreTxt.getText()).concat(" ").concat(apellidoTxt.getText()).concat(" del año lectivo: ").concat(String.valueOf(años)).concat("\nDesea recuperar el curso?"));
                switch(op){
                    case 0:
                        clase.Inscripcion.recuperarCurso(conex, idca);
                        limpiartodo();
                        cancelar();
                        break;
                    case 1:
                        dniTxt.requestFocus(true);
                        break;
                    default:
                        limpiartodo();
                        cancelar();
                        break;
                }
            }
            else{
                horario=clase.Inscripcion.leerHorario(conex, idca);
                int op1=JOptionPane.showConfirmDialog(this, "El alumno ya esta inscripto en el curso ".concat(cursoTxt.getText()).concat(" del año lectivo ").concat(String.valueOf(años)).concat(" en el horario ").concat(horario).concat("\nDesea cambiar el Horario de inscripcion?"));
                switch(op1){
                    case 0:
                        abrirCorreccion();
                        break;
                    case 1:
                        dniTxt.requestFocus(true);
                        break;
                    default:
                        limpiartodo();
                        cancelar();
                        break;
                } 
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void abrirCorreccion(){
        abmRegistros corregir=new abmRegistros();
        corregir.txtbuscar.setText(dniTxt.getText());
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=corregir.getSize();
        corregir.setLocation((desktopSize.width-FrameSize.width)/14,(desktopSize.height-FrameSize.height)/16);
        panelA.add(corregir);
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        corregir.show();
        corregir.txtbuscar.requestFocus(true);
    }
    
    public void abrirPagoIns(){
        abmEstado pagarins=new abmEstado();
        pagarins.txtbuscar.setText(dniTxt.getText());
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pagarins.getSize();
        pagarins.setLocation((desktopSize.width-FrameSize.width)/27,(desktopSize.height-FrameSize.height)/15);
        panelA.add(pagarins);
        Animacion.Animacion.mover_derecha(0, 110, 5, 5, panelA);
        pagarins.show();
        pagarins.txtbuscar.requestFocus(true);
    }
    
    public void insertarPartePago(){
        try{
            idca=clase.Inscripcion.ultimoIDCA(conex);
            clase.Inscripcion.insertarPagoIns(conex, idca, idalumno, estado, false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void generarcuotas(){
        try{
            idalumno=clase.Inscripcion.leerIdAlumno(conex,Integer.parseInt(dniTxt.getText()));
            idca=clase.Inscripcion.leerDatosInsc(conex, idalumno,idcurso,años,añocurso);
            //ATENCION. NO CONSIDERAMOS EL MES "FEBRERO" COMO MES HABIL. SI DESEA AGREGAR A ESTE MES HE DEJADO COMENTARIOS HE INDICIOS QUE TE AYUDARAN A MODIFICAR MAS RAPIDO EL CODIGO
            fechainsc=clase.Inscripcion.leerFechaInsCrp(conex, idca);
            while(fechainsc.next()){
                diains=fechainsc.getInt("day(fechaInscripcion)");
                mesins=fechainsc.getInt("month(fechaInscripcion)");
                añoins=fechainsc.getInt("year(fechaInscripcion)");
            }
            fechaini=clase.Inscripcion.leerFechaIni(conex, idcurso);
            while(fechaini.next()){
                diaini=fechaini.getInt("day(fechaInicio)");
                mesini=fechaini.getInt("month(fechaInicio)");
                añoini=fechaini.getInt("year(fechaInicio)");
            }//AQUI SE PODRIA EDITAR EL DIA INICIO QUE NO SUPERE EL 28 Y EVITAR ALGUNAS CONDICIONALES SIGUIENTES PARA AGREGAR AL MES DE FEBRERO. ENTIENDO QUE ESTO PUDO SER MAS FACIL Y ESTATICO PERO TODO ESTO SE HIZO POR LOS REQUERIMIENTOS DEL USUARIO
//              try{
//              if(añoins==añoini){
                if(mesins<mesini){//Si el mes en que se inscribe es menor al mes de inicio
                    res=mesini-mesins;
                    resmes=mesins+res;
                    if(mesins==1||mesins==3||mesins==5||mesins==7||mesins==8||mesins==10||mesins==12){//Meses con 31 dias
                        if(diains==31){
                            cantidadMeses();
                        }
                        else{
                            cantidadMeses();
                        }
                    }
                    else{//Meses restantes
                        cantidadMeses();
                    }
                }
                if((mesins==mesini)&&(diains<=diacuotagen)){//Si el mes de Inscripcion es el mes de Inicio y Dia de la Inscripcion es menor o igual al Dia en que se genera la cuota
                    if(diains<diaini){//Si el dia de Inscripcion es menor al dia de Inincio
                        cantidadMesesIns();
                    }
                    else if(diains>=diaini){//Si el dia de Inscripcion es mayor o igual al dia de Inicio
                        cantidadMesesDiIns();
                    }
                }
                if((mesins==mesini)&&(diains>diacuotagen)){//Si el mes de Inscripcion es igual al mes de Inicio y el dia de Inscripcion es mayor al dia en que se genera la cuota(25)
                    if(mesins==1||mesins==3||mesins==5||mesins==7||mesins==8||mesins==10||mesins==12){//Los meses que poseen 31 dias
                        if(diains==31){//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS
                            cantidadMesesMenor();
                        }
                        else{//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS. LA MEJOR FORMA SERIA ASIGNARLE DIRECTAMENTE AL DIA DE INSCRIPCION EL 28
                            cantidadMesesMenorDiIns();
                        }       
                    }
                    else{//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS. LA MEJOR FORMA SERIA ASIGNARLE DIRECTAMENTE AL DIA DE INSCRIPCION EL 28
                        cantidadMesesMenorDiIns();
                    }      
                }
                if((mesins>mesini)&&(diains<=diacuotagen)){//Si el mes de Inscripcion es mayor al mes de Inicio y el dia de Inscripcion es menor o igual al dia en que se genera la cuota
                    res=mesins-mesini;//Calculamos la diferencia de meses
                    resmes=meses-res;
                    totalCuotas();
                }
                else if((mesins>mesini)&&(diains>diacuotagen)){//Si el mes de Inscripcion es mayor al mes de Inicio y el dia de Inscripcion es mayor al dia en que se genera la cuota(25)
                    res=mesins-mesini;
                    resmes=meses-(res+1);
                    if(mesins==1||mesins==3||mesins==5||mesins==7||mesins==8||mesins==10||mesins==12){//Listamos los meses de 31 dias
                        if(diains==31){//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS
                            totalCuotasMenor();
                        }
                        else{//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS. LA MEJOR FORMA SERIA ASIGNARLE DIRECTAMENTE AL DIA DE INSCRIPCION EL 28
                            totalCuotas();
                        }
                    }
                    else{//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS. LA MEJOR FORMA SERIA ASIGNARLE DIRECTAMENTE AL DIA DE INSCRIPCION EL 28
                        totalCuotas();
                    }
//                }
//            }
//            else{
//                JOptionPane.showMessageDialog(this,"No se puede inscribir fuera del año actual");
//            }   
//        }
//        catch(Exception ex){
//            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
//        }
            }
            resmes=0;//Para evitar mal los calculos al realizar operaciones reiniciamos el operador
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    //Se entiende que el año no incrementa porque los meses no pueden superar los 12 y ademas por que el inscripto lo hace dentro de los terminos por lo que se presume que es improvable que las cuotas superen el año lectivo actual
    public void cantidadMeses(){
        for(int i=0; i<meses; i++){
            diavenc=String.valueOf(diaini);//Tendra el dia de vencimiento, el dia de inicio del curso
            mesvenc=String.valueOf(resmes+i+1);//Incrementamos para incrementar los meses y porque la variable i esta en 0
            añovenc=String.valueOf(añoins);//Se mantiene el año
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMeses();
        }
    }
    //El mes no se calcula y se toma de la inscribion para incrementar
    public void cantidadMesesIns(){
        for (int i=0; i<meses; i++){
            diavenc=String.valueOf(diaini);
            mesvenc=String.valueOf(mesins+i+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMeses();
        }
    }
    //En esta condicion se tomara el dia de inscripcion como la proxima fecha de vencimiento de los siguientes meses
    public void cantidadMesesDiIns(){
        for (int i=0; i<meses; i++){
            diavenc=String.valueOf(diains);
            mesvenc=String.valueOf(mesins+i+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMesesDiIns();
        }
    }
    //Se le resta 1 mes devido a que la dia de la fecha de inscripcion supero el limite(25), por lo que el inscripto no debera pagar ese mes en cuestion y tambien no se generara 8 cuotas sino 7. Las cuotas bajaran si se inscriben mas adelante del mes de inicio
    public void cantidadMesesMenor(){
        for (int i=0; i<meses-1; i++){//Descontamos 1 mes de pago
            diavenc=String.valueOf(30);//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS
            mesvenc=String.valueOf(mesins+i+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMesesMenor();
        }
    }
    //El dia sera el dia en que se inscriba. Si desea que Febrero cuente como mes habil debe evitar que el dia de inscripcion supere el dia 28
    public void cantidadMesesMenorDiIns(){
        for (int i=0; i<meses-1; i++){//Descontamos el mes perdido por superar los el limite de 25
            diavenc=String.valueOf(diains);//El dia se mantiene. SERIA MEJOR TRAER YA TRAER AL DIA COMO 28 SI DESEA AGREGAR A FEBRERO DEVIDO A QUE ESTA FUNCION LA OCUPAN OTRAS CONDICIONES QUE CAMBIARIAN EL COMPORTAMIENTO DEL PROGRAMA EN ALGUNAS CONDICIONES DE INSCRIPCION
            mesvenc=String.valueOf(mesins+i+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMesesDiIns();//DE ESE MODO TAMBIEN AL TRAER EL DIA COMO 28 AFECTARA A ESTA FUNCION SIN AFECTAR A OTRAS CONDICIONES QUE LAS USAN
        }
    }
    
    public void totalCuotas(){//LO MISMO APLICA ESTA FUNCION A LA CONDICIONAL DE ARRIBA SE DEBERA TRAER YA AL DIA INSCRIPCION COMO 28 SI DESEA AGREGAR A FEBRERO PARA EVITAR UN COMPORTAMIENTO ANORMAL
        for (int i=0; i<resmes; i++){
            diavenc=String.valueOf(diains);//El dia se mantiene. Si desea aregar a Febrero evitar superar el dia 28 de inscripcion de cualquier mes.
            mesvenc=String.valueOf(mesins+i+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMesesDiIns();//DE IGUAL MODO AL ANTERIOR ESTA SE VERA AFECTADA SOLO POR EL DIA PERO NO AFECTARA A LAS DEMAS CONDICONES QUE LAS USAN
        }
    }
    
    public void totalCuotasMenor(){
        for (int k=0; k<resmes; k++){
            diavenc=String.valueOf(30);//Del 31 dia de inscripcion, se baja a 30, bajar a 28 si desea incluir Febrero. EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS
            mesvenc=String.valueOf(mesins+k+1);
            añovenc=String.valueOf(añoins);
            fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
            messup=Integer.parseInt(mesvenc);
            insertarCantidadMesesMenor();
        }
    }
    
    public void insertarCantidadMeses(){
        try{
            if(messup<=12){
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
            }
            else{
                diavenc=String.valueOf(diaini);
                mesvenc=String.valueOf(mesuno);
                añovenc=String.valueOf(añoactual+1);
                fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
                mesuno++;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void insertarCantidadMesesDiIns(){
        try{
            if(messup<=12){
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
            } 
            else{
                diavenc=String.valueOf(diains);//El dia se mantiene. SE SUPONE QUE EL DIA YA DEBERIA VENIR EN 28 SI DESEA AGREGAR AL MES DE FEBRERO
                mesvenc=String.valueOf(mesuno);//El mes es 1, lo incrementamos si supera los 12 mesese
                añovenc=String.valueOf(añoactual+1);//Año se incrementa si el mes supera los 12 meses
                fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
                mesuno++;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void insertarCantidadMesesMenor(){
        try{
            if(messup<=12){
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
            } 
            else{
                diavenc=String.valueOf(30);//EDITAR ESTA LINEA SI DESEA AGREGAR AL MES FEBRERO. EVITAR QUE SUPERE LOS 28 DIAS
                mesvenc=String.valueOf(mesuno);
                añovenc=String.valueOf(añoactual+1);//Si las cuotas superan al año lectivo
                fechavenc=(añovenc+"-"+mesvenc+"-"+diavenc);
                clase.Inscripcion.insertarCuota(conex,idca,fechavenc,estado,interes,0,false,false);
                mesuno++;
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void bloquearfunc(){
        legajoTxt.setEditable(false);
        nombreTxt.setEditable(false);
        apellidoTxt.setEditable(false);
        profesorTxt.setEditable(false);
        aulaTxt.setEditable(false);
        presioTxt.setEditable(false);
        txtprecioins.setEditable(false);
        txthorario.setEditable(false);
//        txtfechaini.setEditable(false);
        fechaInicio.getCalendarButton().setEnabled(false);
        inscripcionBtn.setEnabled(false);
        btncancelar.setEnabled(false);
    }
    
    public void limpiartodo(){
        dniTxt.setText("");
        legajoTxt.setText("");
        nombreTxt.setText("");
        apellidoTxt.setText("");
        cursoTxt.setText("");
        profesorTxt.setText("");
        aulaTxt.setText("");
        presioTxt.setText("");
        txtprecioins.setText("");
        txthorario.setText("");
//        txtfechaini.setText("");
        fechaInicio.setDate(null);
        modelo1.removeAllElements();
        cmbAños.removeAllItems();
        tableCurso.clearSelection();
    }
    
    public void limpiarcurso(){
        cursoTxt.setText("");
        profesorTxt.setText("");
        aulaTxt.setText("");
        presioTxt.setText("");
        txtprecioins.setText("");
        txthorario.setText("");
//        txtfechaini.setText("");
        fechaInicio.setDate(null);
        modelo1.removeAllElements();
        cmbAños.removeAllItems();
    }
    
    public void cancelar(){
        btncancelar.setEnabled(false);
        inscripcionBtn.setEnabled(false);
        dniTxt.setEditable(true);
        cursoTxt.setEditable(true);
        dniTxt.requestFocus(true);
    }
    
    public void limpiarDatosIns(){
        cursoTxt.setEditable(false);
        inscripcionBtn.setEnabled(true);
        btncancelar.setEnabled(true);
        inscripcionBtn.requestFocus(true);
    }
    
    public void limpiarMasDatosIns(){
        tableCurso.clearSelection();
        cursoTxt.setEditable(false);
        inscripcionBtn.setEnabled(true);
        inscripcionBtn.requestFocus(true);
    }
    
    public void limpiarDatosDni(){
        dniTxt.setEditable(true);
        dniTxt.requestFocus(true);
    }
    
    public void limpiarespecial(){
        btncancelar.setEnabled(true);
        dniTxt.setEditable(false);
        cursoTxt.requestFocus(true);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu1 = new javax.swing.JPopupMenu();
        CursoItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lbldni = new javax.swing.JLabel();
        lbllegajo = new javax.swing.JLabel();
        lblnombre = new javax.swing.JLabel();
        lblapellido = new javax.swing.JLabel();
        dniTxt = new javax.swing.JTextField();
        legajoTxt = new javax.swing.JTextField();
        nombreTxt = new javax.swing.JTextField();
        apellidoTxt = new javax.swing.JTextField();
        alumnoBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblnombrecurso = new javax.swing.JLabel();
        lblprofesor = new javax.swing.JLabel();
        lblaula = new javax.swing.JLabel();
        lblinicio = new javax.swing.JLabel();
        lblcuota = new javax.swing.JLabel();
        lblhorario = new javax.swing.JLabel();
        lblañolec = new javax.swing.JLabel();
        lblaños = new javax.swing.JLabel();
        lblfechains = new javax.swing.JLabel();
        cursoTxt = new javax.swing.JTextField();
        profesorTxt = new javax.swing.JTextField();
        aulaTxt = new javax.swing.JTextField();
        presioTxt = new javax.swing.JTextField();
        txthorario = new javax.swing.JTextField();
        añoYears = new com.toedter.calendar.JYearChooser();
        fechaInscripcion = new com.toedter.calendar.JDateChooser();
        cmbAños = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        tableCurso = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jListHorario = new javax.swing.JList<>();
        lblhorarios = new javax.swing.JLabel();
        fechaInicio = new com.toedter.calendar.JDateChooser();
        lblprecioins = new javax.swing.JLabel();
        txtprecioins = new javax.swing.JTextField();
        inscripcionBtn = new javax.swing.JButton();
        cerrarBtn = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();

        CursoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/book.png"))); // NOI18N
        CursoItem.setText("Volver al curso");
        CursoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursoItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(CursoItem);

        setTitle("Formulario de Inscripcion");
        setPreferredSize(new java.awt.Dimension(815, 625));
        addInternalFrameListener(new javax.swing.event.InternalFrameListener() {
            public void internalFrameActivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosed(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameClosing(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeactivated(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameDeiconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameIconified(javax.swing.event.InternalFrameEvent evt) {
            }
            public void internalFrameOpened(javax.swing.event.InternalFrameEvent evt) {
                formInternalFrameOpened(evt);
            }
        });

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "ALUMNO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        lbldni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/search.png"))); // NOI18N
        lbldni.setText("DNI:");

        lbllegajo.setText("Legajo:");

        lblnombre.setText("Nombre:");

        lblapellido.setText("Apellido:");

        dniTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dniTxtKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dniTxtKeyTyped(evt);
            }
        });

        alumnoBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        alumnoBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        alumnoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                alumnoBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(lblnombre))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbldni)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(dniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 215, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(alumnoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 301, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblapellido, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbllegajo, javax.swing.GroupLayout.Alignment.TRAILING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(legajoTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 255, Short.MAX_VALUE)
                    .addComponent(apellidoTxt))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(alumnoBtn, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbldni)
                        .addComponent(dniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(legajoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lbllegajo)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombre)
                    .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblapellido)
                    .addComponent(apellidoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "CURSO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        lblnombrecurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/search.png"))); // NOI18N
        lblnombrecurso.setText("Nombre del Curso:");

        lblprofesor.setText("Profesor:");

        lblaula.setText("Aula:");

        lblinicio.setText("Inicio:");

        lblcuota.setText("Cuota:");

        lblhorario.setText("Horario:");

        lblañolec.setText("Año Lectivo:");

        lblaños.setText("Años:");

        lblfechains.setText("Fecha de Inscripcion:");

        cursoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                cursoTxtKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cursoTxtKeyTyped(evt);
            }
        });

        cmbAños.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        tableCurso.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tableCurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Cuota", "Fecha de Inicio", "Precio de Inscripcion"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Object.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableCurso.setComponentPopupMenu(jPopupMenu1);
        tableCurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableCursoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tableCurso);
        if (tableCurso.getColumnModel().getColumnCount() > 0) {
            tableCurso.getColumnModel().getColumn(0).setResizable(false);
            tableCurso.getColumnModel().getColumn(1).setResizable(false);
            tableCurso.getColumnModel().getColumn(2).setResizable(false);
            tableCurso.getColumnModel().getColumn(3).setResizable(false);
        }

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jListHorario.setModel(modelo1);
        jListHorario.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListHorarioValueChanged(evt);
            }
        });
        jScrollPane1.setViewportView(jListHorario);

        lblhorarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblhorarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reloj.png"))); // NOI18N
        lblhorarios.setText("HORARIOS DISPONIBLES");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblhorarios, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 159, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lblhorarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblprecioins.setText("Precio:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblaños)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                                        .addComponent(cmbAños, javax.swing.GroupLayout.PREFERRED_SIZE, 121, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblhorario)
                                            .addComponent(lblaula))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(aulaTxt)
                                            .addComponent(txthorario, javax.swing.GroupLayout.DEFAULT_SIZE, 118, Short.MAX_VALUE))))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblnombrecurso)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cursoTxt)))
                        .addGap(24, 24, 24)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblprofesor)
                                    .addComponent(lblinicio))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblcuota)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(presioTxt))
                                    .addComponent(profesorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 257, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblañolec)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(añoYears, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(lblprecioins)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtprecioins))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(lblfechains)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(fechaInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(18, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombrecurso)
                    .addComponent(cursoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblprofesor)
                    .addComponent(profesorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblaula)
                                .addComponent(aulaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblinicio))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(presioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblcuota))
                            .addComponent(fechaInicio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblhorario)
                                .addComponent(txthorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblañolec, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(añoYears, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblprecioins)
                                .addComponent(txtprecioins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(22, 22, 22)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblaños)
                                .addComponent(cmbAños, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblfechains, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(fechaInscripcion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21))
        );

        inscripcionBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/copy-1.png"))); // NOI18N
        inscripcionBtn.setText("INSCRIBIR");
        inscripcionBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        inscripcionBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscripcionBtnActionPerformed(evt);
            }
        });

        cerrarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        cerrarBtn.setText("Cerrar");
        cerrarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cerrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarBtnActionPerformed(evt);
            }
        });

        btncancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help_ring-buoy.png"))); // NOI18N
        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(inscripcionBtn)
                        .addGap(18, 18, 18)
                        .addComponent(btncancelar)
                        .addGap(18, 18, 18)
                        .addComponent(cerrarBtn))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addContainerGap(23, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cerrarBtn, inscripcionBtn});

        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cerrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(inscripcionBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {cerrarBtn, inscripcionBtn});

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void dniTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dniTxtKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                rs=clase.Inscripcion.MostrarPersona(conex,Integer.parseInt(dniTxt.getText()));
                if (rs.next()) {
                    legajoTxt.setText(rs.getString(1));
                    nombreTxt.setText(rs.getString(2));
                    apellidoTxt.setText(rs.getString(3));
                    limpiarespecial();
                }
                else{
                    JOptionPane.showMessageDialog(this,"No existe un alumno con ese DNI ".concat(dniTxt.getText()),"ERROR",ERROR_MESSAGE);
                    dniTxt.requestFocus();
                }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_dniTxtKeyPressed

    private void alumnoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_alumnoBtnActionPerformed
        abmAlumno al=new abmAlumno();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=al.getSize();
        al.setLocation((desktopSize.width-FrameSize.width)/25,(desktopSize.height-FrameSize.height)/35);
        panelA.add(al);
        this.dispose();
        Animacion.Animacion.mover_derecha(0,110, 5, 5, panelA);
        al.show();
        al.buscarTxt.requestFocus(true);
    }//GEN-LAST:event_alumnoBtnActionPerformed

    private void cursoTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cursoTxtKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            if((dniTxt.getText().isEmpty())||(nombreTxt.getText().isEmpty())
                ||apellidoTxt.getText().isEmpty()){
                JOptionPane.showMessageDialog(this, "Cargue a un alumno","WARNING",WARNING_MESSAGE);
                cursoTxt.setText("");
                limpiarDatosDni();
            }
            else{
                mostrarMasDatosIns();
                cargarListaCursoHorario();
            }
        }
    }//GEN-LAST:event_cursoTxtKeyPressed
    
    private void inscripcionBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscripcionBtnActionPerformed
        if((txthorario.getText().isEmpty())||(añoYears.getYear()==0)||(fechaInscripcion.getDate()==null)){
            JOptionPane.showMessageDialog(this, "Complete los campos:\n HORARIO\nAÑO LECTIVO\nFECHA DE INSCRIPCION\nAÑOS","WARNING",WARNING_MESSAGE);
        }
        else{
            comprobarinsc();
            if(idca!=0){
                recuperarcurso();
            }
            else{
                int op=JOptionPane.showConfirmDialog(this, "Esta seguro de inscribir al alumno?");
                switch (op) {
                    case 0:
                        try {
                            añocurso=String.valueOf(cmbAños.getSelectedItem());
                            clase.Inscripcion.insertarCursoAlumno(conex, idcurso,idalumno,añoYears.getYear(),(float)0,fechaInscripcion.getDate(),(float)0,txthorario.getText(),añocurso,false);
                            generarcuotas();
                            //COMPROBAR SI EL INSCRIPTO DEBE PAGAR LA CUOTA DE INSCRIPCION EN SU INSTITUCION CADA AÑO SI ES QUE ESE CURSO POSEA MAS DE UN AÑO. Tengase en cuenta que el inscripto puede abandonar y regresar al año siguiente?pagara de nuevo? en todos los años si abandona en cada uno de ellos?
                            int op1=JOptionPane.showConfirmDialog(this, "Desea pagar la inscripcion del curso ".concat(cursoTxt.getText()).concat(" con el valor de ").concat(txtprecioins.getText()).concat("?"));
                            if(op1==0){
                                insertarPartePago();
                                abrirPagoIns();
                            }
                            else{
                                insertarPartePago();
                            }
                        }
                        catch (Exception e){
                            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                        }
                        limpiartodo();
                        cancelar();
                        break;
                    case 1:
                        btncancelar.requestFocus(true);
                        break;
                    default:
                        limpiartodo();
                        cancelar();
                        break;
                }
            }
        }
    }//GEN-LAST:event_inscripcionBtnActionPerformed

    private void tableCursoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCursoMouseClicked
        int fila=tableCurso.rowAtPoint(evt.getPoint());
        if((dniTxt.getText().isEmpty())||(nombreTxt.getText().isEmpty())
                ||apellidoTxt.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Cargue a un alumno","WARNING",WARNING_MESSAGE);
            limpiarDatosDni();
            tableCurso.clearSelection();
        }
        else{
            dniTxt.setEditable(false);
            cursoTxt.setText(tableCurso.getValueAt(fila,0).toString());
            presioTxt.setText("$ ".concat(tableCurso.getValueAt(fila,1).toString()));
            fechaInicio.setDate((Date) tableCurso.getValueAt(fila, 2));
            mostrarDatosIns();
            modelo1.removeAllElements();
            cargarListaCursoHorario();
        }
    }//GEN-LAST:event_tableCursoMouseClicked

    private void jListHorarioValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListHorarioValueChanged
        horario=String.valueOf(jListHorario.getSelectedValue());
        if(jListHorario.isSelectionEmpty()){
            txthorario.setText("");
        }
        else{
            txthorario.setText(horario);
        }
    }//GEN-LAST:event_jListHorarioValueChanged

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        if(dniTxt.getText().isEmpty()||nombreTxt.getText().isEmpty()||apellidoTxt.getText().isEmpty()){
            bloquearfunc();
            dniTxt.setEditable(true);
            dniTxt.requestFocus(true);
        }
        else{
            bloquearfunc();
            dniTxt.setEditable(false);
            btncancelar.setEnabled(true);
            cursoTxt.requestFocus(true);
            
        }
        mostrarCursos();
        cmbAños.removeAllItems();
        fechaInscripcion.setDate(date);
        tableCurso.getTableHeader().setReorderingAllowed(false);
    }//GEN-LAST:event_formInternalFrameOpened

    private void cerrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cerrarBtnActionPerformed

    private void dniTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dniTxtKeyTyped
        // TODO add your handling code here:
        int c=evt.getKeyChar();
        if(dniTxt.getText().length()>=8){
            evt.consume();
        }
        if (!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_dniTxtKeyTyped

    private void cursoTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cursoTxtKeyTyped
        // TODO add your handling code here:
        int c=evt.getKeyChar();
        if(cursoTxt.getText().length()>=40){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)&&!Character.isSpaceChar(c)){
            evt.consume();
        }
    }//GEN-LAST:event_cursoTxtKeyTyped

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        limpiartodo();
        cancelar();
    }//GEN-LAST:event_btncancelarActionPerformed

    private void CursoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursoItemActionPerformed
        // TODO add your handling code here:
        abmCurso c=new abmCurso();
        c.cursoTxt.setText(cursoTxt.getText());
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=c.getSize();
        c.setLocation((desktopSize.width-FrameSize.width)/30,(desktopSize.height-FrameSize.height)/7);
        panelA.add(c);
        this.dispose();
        Animacion.Animacion.mover_derecha(0 , 110, 5, 5, panelA);
        c.show();
        c.cursoTxt.requestFocus(true);
    }//GEN-LAST:event_CursoItemActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem CursoItem;
    private javax.swing.JButton alumnoBtn;
    public javax.swing.JTextField apellidoTxt;
    private javax.swing.JTextField aulaTxt;
    private com.toedter.calendar.JYearChooser añoYears;
    public javax.swing.JButton btncancelar;
    private javax.swing.JButton cerrarBtn;
    public javax.swing.JComboBox<String> cmbAños;
    public javax.swing.JTextField cursoTxt;
    public javax.swing.JTextField dniTxt;
    private com.toedter.calendar.JDateChooser fechaInicio;
    private com.toedter.calendar.JDateChooser fechaInscripcion;
    private javax.swing.JButton inscripcionBtn;
    public javax.swing.JList<String> jListHorario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblapellido;
    private javax.swing.JLabel lblaula;
    private javax.swing.JLabel lblañolec;
    private javax.swing.JLabel lblaños;
    private javax.swing.JLabel lblcuota;
    private javax.swing.JLabel lbldni;
    private javax.swing.JLabel lblfechains;
    private javax.swing.JLabel lblhorario;
    private javax.swing.JLabel lblhorarios;
    private javax.swing.JLabel lblinicio;
    private javax.swing.JLabel lbllegajo;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lblnombrecurso;
    private javax.swing.JLabel lblprecioins;
    private javax.swing.JLabel lblprofesor;
    public javax.swing.JTextField legajoTxt;
    public javax.swing.JTextField nombreTxt;
    public javax.swing.JTextField presioTxt;
    public javax.swing.JTextField profesorTxt;
    private javax.swing.JTable tableCurso;
    private javax.swing.JTextField txthorario;
    private javax.swing.JTextField txtprecioins;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmInscripcion.class.getName());
}
