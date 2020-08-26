/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
import conexion.Conexion;
import static interfaces.abmPrincipal.panelA;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author cytrex
 */
public class abmPagar extends javax.swing.JInternalFrame {
    Connection cnx = Conexion.conectar();
    ResultSet rs,rss;
    Object[]datoscero=new Object[4];
    DefaultTableModel modelocero=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    Object[]datosuno=new Object[4];
    DefaultTableModel modelouno=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    Object[]datosdos=new Object[6];
    DefaultTableModel modelodos=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    //Variables para calcular el interes
    Date date=new Date();
    LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    SimpleDateFormat mostrar=new SimpleDateFormat("dd MMM YYYY' - hs-'HH'.'mm'.'ss'.'SSS");
    int diaactual=localDate.getDayOfMonth();
    int mesactual=localDate.getMonthValue();
    int añoactual=localDate.getYear();
    //Variables generales
    int idcur,idalu,idca,idc;
    int añoins,dnialu,ciclos,cuotas;
    int diavenc,mesvenc,añovenc;
    String fechavenc,ruta,nombrecurso,añocurso,fechainteres;
    String estadoimpaga="IMPAGA",estadopagado="PAGADO";
    float interesalu,interescurso,gendeuda,deudaalu,deuda;
    float preciocuota,importetabla;
    //Varables para calcular el pago
    float restante,variable,variabledos,variabletres,importe;
    int interes,cantidadmeses,cantidadcuota;
    int maxmes,contmes;
    String estadomes;
    //Variables para el reporte
    String legajo,nombre,apellido,mespago,pdfhora;
    Date fechapagado;
    int maxpago,minpago;
    
    public int cboxitem=0;
    boolean perdonar,band,banddos;
    InputStream binario;
    /**
     * Creates new form pagos
     */
    public abmPagar() {
        initComponents();  
    }
    
    public void tablaAlu(){
        modelocero.setColumnCount(0);
        modelocero.setRowCount(0);
        modelocero.addColumn("LEGAJO");
        modelocero.addColumn("NOMBRE");
        modelocero.addColumn("APELLIDO");
        modelocero.addColumn("DNI");
        try{
            tablaalumno.setModel(modelocero);
            rs=clase.Pagar.mostrarAlumno(cnx);
            while(rs.next()){
                datoscero[0]=rs.getString("legajo");
                datoscero[1]=rs.getString("nombre");
                datoscero[2]=rs.getString("apellido");
                datoscero[3]=rs.getInt("dni");
                modelocero.addRow(datoscero);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void tablaAños(){
        modelouno.setColumnCount(0);
        modelouno.setRowCount(0);
        modelouno.addColumn("CURSO");
        modelouno.addColumn("CUOTA");
        modelouno.addColumn("CURSANDO");
        modelouno.addColumn("AÑO INS");
        try{
            tablaaños.setModel(modelouno);
            idalu=clase.Pagar.leerIDAlu(cnx, dnialu);
            rs=clase.Pagar.mostrarCurso(cnx,idalu);
            while(rs.next()){
                datosuno[0]=rs.getString("nombre");
                datosuno[1]=rs.getFloat("cuota");
                datosuno[2]=rs.getString("añoCurso");
                datosuno[3]=rs.getInt("añoLectivo");
                modelouno.addRow(datosuno);
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void tablaCuotas(){
        modelodos.setColumnCount(0);
        modelodos.setRowCount(0);
        modelodos.addColumn("MES");
        modelodos.addColumn("VENCIMIENTO");
        modelodos.addColumn("IMPORTE");
        modelodos.addColumn("ESTADO");
        modelodos.addColumn("INTERES");
        modelodos.addColumn("PAGADO");
        int datomesbase;
        String mestabla;
        try{
            tablapagos.setModel(modelodos);
            rs=clase.Pagar.mostrarCuotas(cnx,idca);
            while(rs.next()){
                datomesbase=rs.getInt("mes");
                switch(datomesbase){
                    case 1:
                        mestabla="ENERO";
                        break;
                    case 2:
                        mestabla="FEBRERO";
                        break;
                    case 3:
                        mestabla="MARZO";
                        break;
                    case 4:
                        mestabla="ABRIL";
                        break;
                    case 5:
                        mestabla="MAYO";
                        break;
                    case 6:
                        mestabla="JUNIO";
                        break;
                    case 7:
                        mestabla="JULIO";
                        break;
                    case 8:
                        mestabla="AGOSTO";
                        break;
                    case 9:
                        mestabla="SEPTIEMBRE";
                        break;
                    case 10:
                        mestabla="OCTUBRE";
                        break;
                    case 11:
                        mestabla="NOVIEMBRE";
                        break;
                    case 12:
                        mestabla="DICIEMBRE";
                        break;
                    default: 
                        mestabla="";
                        break;
                }
                datosdos[0]=mestabla;
                datosdos[1]=rs.getString("fechaVencimiento");
                datosdos[2]=rs.getFloat("importe");
                datosdos[3]=rs.getString("estado");
                datosdos[4]=rs.getFloat("interes");
                datosdos[5]=rs.getDate("fechaPago");
                modelodos.addRow(datosdos);
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void calcularInteres(){
        band=false;//La bandera permite saltar el primer mes (por los requerimientos del usuario el primer mes no hay deuda)
        try{
            rs=clase.Pagar.leerFechaVenc(cnx,idca,estadoimpaga);
            while(rs.next()){
                diavenc=rs.getInt("day(fechaVencimiento)");
                mesvenc=rs.getInt("month(fechaVencimiento)");
                añovenc=rs.getInt("year(fechaVencimiento)");
                fechavenc=añovenc+"-"+mesvenc+"-"+diavenc;
                if(band==true){
                    idc=clase.Pagar.mesPagado(cnx, idca, fechavenc, estadoimpaga);
                    interesalu=clase.Pagar.buscarInteres(cnx, idc);
                    perdonar=clase.Pagar.leerPerd(cnx, idc);
                    if((mesvenc<mesactual)&&(añovenc==añoactual)&&(perdonar==false)){
                        if(interesalu==0){
                            gstInteres();
                        }
                    }
                    if((mesvenc==mesactual)&&(añovenc==añoactual)&&(perdonar==false)){
                        if(diaactual>diavenc){
                            if(interesalu==0){
                                gstInteres();
                            }
                        }
                    }
                }
                band=true;
            }
            tablaCuotas();
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void gstInteres(){
        gendeuda=0;
        try {
            if(idc!=0){
                interescurso=clase.Pagar.interesCurso(cnx,idcur);
                clase.Pagar.insertarInteres(cnx,interescurso,idc);
                deudaalu=clase.Pagar.buscarDeuda(cnx,idca);
                gendeuda=deudaalu+interescurso;
                clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                actualizardeuda();
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void actualizardeuda(){
        deudaalu=0;
        try{
            deudaalu=clase.Pagar.buscarDeuda(cnx,idca);
            txtdeuda.setText("$ ".concat(String.valueOf(deudaalu)));
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void traerImagen(){
        try {
            rs=clase.Pagar.buscarPerfil(cnx, idalu);
            while(rs.next()){
                ruta=rs.getString(1);
                binario=rs.getBinaryStream(2);
            }
            if(!ruta.isEmpty()){
                ByteArrayOutputStream ouput = new ByteArrayOutputStream();
                int temp=binario.read();
                while(temp>=0){
                    ouput.write((char)temp);
                    temp=binario.read();
                }
                Image foto=Toolkit.getDefaultToolkit().createImage(ouput.toByteArray());
                foto=foto.getScaledInstance(160,125,Image.SCALE_DEFAULT);
                lblperfil.setIcon(new ImageIcon(foto));
            }
            else{
                recuperarPerfil();
            }
        }
        catch(Exception e){
//            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void recuperarPerfil(){
        Image perfil=getToolkit().getImage("src/imagenes/usuario.png");
        perfil=perfil.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        lblperfil.setIcon(new ImageIcon(perfil));
    }
    
    public void pagarMenor(){
        restante=0;gendeuda=0;
        try{
            if(importe<preciocuota){
                restante=preciocuota-importe;
                gendeuda=deudaalu+restante;
                txtcambio.setText("$ 0.0");
                recuperarEstado();//En caso de que borre el pago y desee volver a pagar obtendremos ese id
                clase.Pagar.insertarPago(cnx, mes.getMonth()+1,fpago.getDate(),importe,estadopagado,idca,estadoimpaga);
                clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);//Capturara el ultimo id de la cuota impaga que se cargara en la siguiente linea. Se hace esto por si el usuario borra una cuota de en medio dejandola rodeada de pagado. Esto permitira capturar ese id
                JOptionPane.showMessageDialog(this, "Sa agrego $ ".concat(String.valueOf(restante)).concat(" de deuda al\nalumno/a: ").concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()));
                cambiarCondicion();//Si borrar pago se cumple maxpago obtendra el id del pago borrado que es enviado como parametro para obtener los datos para el informe
                limpiarfunc();
                actualizardeuda();
                tablaCuotas();
                recuperarDatosInforme();
                generarInforme();
            }
            if(importe==preciocuota){
                txtcambio.setText("$ 0.0");
                recuperarEstado();
                clase.Pagar.insertarPago(cnx, mes.getMonth()+1,fpago.getDate(),importe,estadopagado,idca,estadoimpaga);
                maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                cambiarCondicion();
                limpiarfunc();
                tablaCuotas();
                recuperarDatosInforme();
                generarInforme();
            }
            if(importe>preciocuota){
                restante=importe-(preciocuota*cantidadcuota);
                txtcambio.setText("$ ".concat(String.valueOf(restante)));
                recuperarEstado();
                clase.Pagar.insertarPago(cnx, mes.getMonth()+1,fpago.getDate(),preciocuota,estadopagado,idca,estadoimpaga);
                maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                cambiarCondicion();
                limpiarfunc();
                tablaCuotas();
                recuperarDatosInforme();
                importetabla=importe;
                generarInforme();
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void pagarMayor(){
        contmes=1;band=false;restante=0;
        variable=0;
        try{
            if(importe>preciocuota){
                restante=importe;
                for(int i=1; i<=cantidadcuota; i++){
                    if(restante>=preciocuota){
                        maxmes=clase.Pagar.ultimoIDmes(cnx, idca);
                        estadomes=clase.Pagar.estadoMes(cnx, maxmes);
                        if(!estadomes.equalsIgnoreCase(estadopagado)){
                            if((mes.getMonth()+contmes>12)&&(band==false)){
                                mes.setMonth(0);
                                band=true;
                                contmes--;
                            }
                            recuperarEstado();
                            clase.Pagar.insertarPago(cnx, mes.getMonth()+contmes,fpago.getDate(),preciocuota,estadopagado,idca,estadoimpaga);
                            maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "No puede seguir pagando\nTodos los meses estan pagados","WARNING",WARNING_MESSAGE);
                            break;
                        }
                    }
                    else if(restante<preciocuota){
                        JOptionPane.showMessageDialog(this,"No puede seguir pagando las cuotas\nImporte Insuficiente\nCantidad de cuotas pagadas: ".concat(String.valueOf(i-1)));
                        break;
                    }
                    variable=restante;
                    restante=variable-preciocuota;
                    contmes++;
//                    recuperarDatosInforme();
                    importetabla=variable;
//                    guardarInforme();
                }
                if(restante>=0){
                    txtcambio.setText("$ ".concat(String.valueOf(restante)));
                }
                else{
                    txtcambio.setText("$ 0.0");
                }
                cambiarCondicion();
                recuperarDatosInforme();
                generarInforme();
                limpiarfunc();
                tablaCuotas();
            }
            else{
                JOptionPane.showMessageDialog(this, "No puede pagar mas de una cuota con ese importe\nRevise la cantidad de cuotas establecidas","WARNING",WARNING_MESSAGE);
                txtcantcuota.requestFocus();
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void pagarTodo(){
        contmes=1;band=false;banddos=false;
        restante=0;variable=0;variabledos=0;variabletres=0;
        try{
            if((importe>preciocuota)&&(cantidadcuota>1)){
                restante=importe;
                for (int i=1; i<=cantidadcuota; i++){
                    if(restante>=preciocuota){
                        maxmes=clase.Pagar.ultimoIDmes(cnx, idca);
                        estadomes=clase.Pagar.estadoMes(cnx, maxmes);
                        if(!estadomes.equalsIgnoreCase(estadopagado)){
                            if((mes.getMonth()+contmes>12)&&(band==false)){
                                mes.setMonth(0);
                                band=true;
                                contmes--;
                            }
                            recuperarEstado();
                            clase.Pagar.insertarPago(cnx, mes.getMonth()+contmes,fpago.getDate(),preciocuota,estadopagado,idca,estadoimpaga);
                            maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "No puede seguir pagando\nTodos los meses estan pagados","WARNING",WARNING_MESSAGE);
                            break;
                        }
                        maxmes=clase.Pagar.ultimoIDmes(cnx, idca);
                        estadomes=clase.Pagar.estadoMes(cnx, maxmes);
                        if(estadomes.equalsIgnoreCase(estadopagado)){
                            JOptionPane.showMessageDialog(this, "No puede seguir pagando\nTodos los meses estan pagados","WARNING",WARNING_MESSAGE);
                            variabledos=restante;
                            restante=variabledos-preciocuota;
                            break;
                        }
                    }
                    else if((restante<preciocuota)&&(restante>0)&&(estadomes.equalsIgnoreCase(estadoimpaga))){
                        variable=preciocuota-restante;
                        gendeuda=deudaalu+variable;
                        if((mes.getMonth()+contmes>12)&&(banddos==false)){
                            mes.setMonth(0);
                            banddos=true;
                            contmes--;
                        }
                        recuperarEstado();
                        clase.Pagar.insertarPago(cnx, mes.getMonth()+contmes,fpago.getDate(),restante, estadopagado,idca,estadoimpaga);
                        clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                        maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                        JOptionPane.showMessageDialog(this, "Se agrego $ ".concat(String.valueOf(variable)).concat(" de deuda al\nalumno/a: ").concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()));
                        if(cantidadcuota>i){
                            JOptionPane.showMessageDialog(this, "No puede seguir pagando\nImporte insuficiente a la cantidad de cuotas establecida\nSe pago ".concat(String.valueOf(i)).concat(" cuotas"));
                            variabletres=1;
                            break;
                        }
                    }
                    else if(restante<=0){
                        JOptionPane.showMessageDialog(this, "No puede seguir pagando\nImporte Insuficiente\nCantidad de cuotas pagadas: ".concat(String.valueOf(i-1)));
                        break;
                    }
                    variabledos=restante;
                    restante=variabledos-preciocuota;
                    contmes++;
                }
                if(restante>=0){
                    if(variabletres==0){
                        txtcambio.setText("$ ".concat(String.valueOf(restante)));
                    }
                    else if(variabletres==1){
                        txtcambio.setText("$ 0.0");
                    }
                }
                else{
                    txtcambio.setText("$ 0.0");
                }
                btnquitarseleccion.setEnabled(false);
                actualizardeuda();
                limpiarfunc();
                RB();
                tablaCuotas();
                cambiarCondicion();
                recuperarDatosInforme();
                generarInforme();
            }
            if((importe==preciocuota)&&(cantidadcuota==1)){
                txtcambio.setText("$ 0.0");
                recuperarEstado();
                clase.Pagar.insertarPago(cnx, mes.getMonth()+1,fpago.getDate(),importe,estadopagado,idca,estadoimpaga);
                maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                btnquitarseleccion.setEnabled(false);
                limpiarfunc();
                RB();
                tablaCuotas();
                cambiarCondicion();
                recuperarDatosInforme();
                generarInforme();
            }
            if((importe<preciocuota)&&(cantidadcuota==1)){
                restante=preciocuota-importe;
                gendeuda=deudaalu+restante;
                txtcambio.setText("$ 0.0");
                recuperarEstado();
                clase.Pagar.insertarPago(cnx, mes.getMonth()+1,fpago.getDate(),importe,estadopagado,idca,estadoimpaga);
                clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                maxpago=clase.Pagar.maxPago(cnx, idca, estadopagado);
                JOptionPane.showMessageDialog(this, "Sa agrego $ ".concat(String.valueOf(restante)).concat(" de deuda al\nalumno/a: ").concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()));
                btnquitarseleccion.setEnabled(false);
                actualizardeuda();
                limpiarfunc();
                RB();
                tablaCuotas();
                cambiarCondicion();
                recuperarDatosInforme();
                generarInforme();
            }
            if((importe>preciocuota)&&(cantidadcuota==1)){
                JOptionPane.showMessageDialog(this, "Si selecciona 'Pagar Todo el Importe'\nSe requiere de una catidad mayor de cuotas\nCuando el importe supere la cuota del curso","WARNING",WARNING_MESSAGE);
                txtcantcuota.requestFocus();
            }
            if((importe<=preciocuota)&&(cantidadcuota>1)){
                JOptionPane.showMessageDialog(this, "No puede pagar mas de una cuota con ese importe\nRevise la cantidad de cuotas establecidas","WARNING",WARNING_MESSAGE);
                txtcantcuota.requestFocus();
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }              
    }
    
    public void pagarDeuda(){
        contmes=1;band=false;restante=0;
        variable=0;variabledos=0;variabletres=0;
        try{
            deudaalu=clase.Pagar.buscarDeuda(cnx, idca);
            if(deudaalu>0){
                if(importe==deudaalu){
                    txtcambio.setText("$ 0.0");
                    clase.Pagar.insertarDeuda(cnx, 0, idca);
                    JOptionPane.showMessageDialog(this, "Se pago toda la deuda\ndel alumno/a: ".concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()));
                    for(int i=1; i<=cantidadmeses; i++){
                        clase.Pagar.actualizarCuota(cnx,0,idca);
                    }
                }
                if(importe>deudaalu){
                    restante=importe-deudaalu;
                    txtcambio.setText("$ ".concat(String.valueOf(restante)));
                    clase.Pagar.insertarDeuda(cnx, 0, idca);
                    JOptionPane.showMessageDialog(this, "Se pago toda la deuda\ndel alumno/a: ".concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()));
                    for (int k=1; k<=cantidadmeses; k++){
                        clase.Pagar.actualizarCuota(cnx,0,idca);
                    }
                }
                if(importe<deudaalu){
                    restante=deudaalu-importe;
                    txtcambio.setText("$ 0.0");
                    clase.Pagar.insertarDeuda(cnx, restante, idca);
                    JOptionPane.showMessageDialog(this, "Se pago la deuda\ndel alumno/a: ".concat(txtnomape.getText()).concat(" \ndni: ").concat(txtdni.getText()).concat("\n Aun existe $ ").concat(String.valueOf(restante)).concat(" de deuda sin pagar"));
                    interes=clase.Pagar.interesAlumno(cnx, idca);
                    interescurso=clase.Pagar.interesCurso(cnx,idcur);
                    variable=importe;
                    if(importe>=interescurso){
                        for(int i=1; i<=cantidadmeses; i++){
                            variable=variable-interescurso;
                            if((variable>0)&&(variable<interescurso)){
                                variabledos=interescurso-variable;
                                band=true;
                            }
                            if(variable<=0){
                                break;
                            }
                            contmes++;
                        }
                        if(band==true){
                            contmes--;
                        }
                        for(int k=1; k<=contmes; k++){
                            clase.Pagar.actualizarCuota(cnx, 0, idca);
                        }
                        if(band==true){
                            clase.Pagar.actualizarCuota(cnx, variabledos, idca);
                        }
                    }
                    else if(importe>0&&importe<interescurso){
                        variabletres=clase.Pagar.buscarCuota(cnx, idca);
                        variabledos=variabletres-variable;
                        clase.Pagar.actualizarCuota(cnx, variabledos, idca);
                    }    
                }
                btnquitarseleccion.setEnabled(false);
                txtimporte.setText("");
                ptodoatv();
                actualizardeuda();
                RB();
                tablaCuotas();
            }
            else{
                JOptionPane.showMessageDialog(this, "El alumno/a ".concat(txtnomape.getText()).concat("\ndni: ").concat(txtdni.getText()).concat("\nNo posee deuda"),"ERROR",ERROR_MESSAGE);
            }
        }    
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void recuperarDatosInforme(){
        try{
//          maxpago=clase.Pagar.maxPago(cnx, idca, estadoimpaga);
            rss=clase.Pagar.buscarInforme(cnx, maxpago);
            int datomes;
            while(rss.next()){
                datomes=rss.getInt(1);
                switch(datomes){
                    case 1:
                        mespago="ENERO";
                        break;
                    case 2:
                        mespago="FEBRERO";
                        break;
                    case 3:
                        mespago="MARZO";
                        break;
                    case 4:
                        mespago="ABRIL";
                        break;
                    case 5:
                        mespago="MAYO";
                        break;
                    case 6:
                        mespago="JUNIO";
                        break;
                    case 7:
                        mespago="JULIO";
                        break;
                    case 8:
                        mespago="AGOSTO";
                        break;
                    case 9:
                        mespago="SEPTIEMBRE";
                        break;
                    case 10:
                        mespago="OCTUBRE";
                        break;
                    case 11:
                        mespago="NOVIEMBRE";
                        break;
                    case 12:
                        mespago="DICIEMBRE";
                        break;
                    default: 
                        mespago="";
                        break;
                }
                fechainteres=String.valueOf(rss.getDate(2));
                fechapagado=rss.getDate(3);
                importetabla=rss.getFloat(4);
                interesalu=rss.getFloat(5);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void generarComprobante(){
        ImageIcon icono=new ImageIcon(this.getClass().getResource("/iconos/logo_yes.png"));//Obtiene el recurso de la imagen del jar y no de la ruta
        Map parametros= Collections.synchronizedMap(new HashMap());
        parametros.put("imagen", icono.getImage());
        parametros.put("nombre", nombre);
        parametros.put("apellido", apellido);
        parametros.put("legajo", legajo);
        parametros.put("dni", dnialu);
        parametros.put("curso", nombrecurso);
        parametros.put("cursando", añocurso);
        parametros.put("deuda", deudaalu);
        parametros.put("idca", idca);
        try {
//            JasperReport report=JasperCompileManager.compileReport(new File("").getAbsolutePath()+"/src/reportes/rpComprobanteGeneral.jrxml");
//            JasperReport report=JasperCompileManager.compileReport("src/reportes/rpComprobanteGeneral.jrxml");//Para evitar el mal direccionamiento del comprobante en ejecucion
            JasperReport report=JasperCompileManager.compileReport(this.getClass().getResourceAsStream("/reportes/rpComprobanteGeneral.jrxml"));//Obtenemos el recurso del jar del proyecto creado
            JasperPrint print=JasperFillManager.fillReport(report,parametros,cnx);
            JasperViewer view=new JasperViewer(print,false);
            view.setTitle("Comprobante de Pago General");
            view.setVisible(true);
            
            File carp=new File("C:\\Users\\Public\\Pictures\\sistemaYES\\"+nombre+" "+apellido+" - "+dnialu);
            if(!carp.exists()){
                carp.mkdirs();
            }
            pdfhora=String.valueOf(mostrar.format(date));
            String dest=("C:/Users/Public/Pictures/sistemaYES/"+nombre+" "+apellido+" - "+dnialu+"/"+pdfhora+".pdf");
            JasperExportManager.exportReportToPdfFile(print, dest);//Requiere iText-2.1.7 librearia com.lowagie
//            Document document = new Document();//Para librerias mas nuevas itextpdf-5.5.13.1 requiere com.itextpdf
//            try {
//                PdfWriter.getInstance(document, new FileOutputStream(dest));
//            }
//            catch (FileNotFoundException ex) {
//                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
//            }
        }
        catch (JRException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }   
    }
    @SuppressWarnings("unchecked")
    public void generarInforme(){
        int op=JOptionPane.showConfirmDialog(this, "Deseas generar el comprobante?");
        switch(op){
            case 0:
//                File imagen=new File("src/iconos/logo_yes.png");
                ImageIcon imagen=new ImageIcon(this.getClass().getResource("/iconos/logo_yes.png"));
                Map parametros = Collections.synchronizedMap(new HashMap());
//                HashMap<String, Object> parametros = new HashMap<>();
                //Parametros de la imagen
//                parametros.put("imagen", imagen.getAbsolutePath());
                parametros.put("imagen", imagen.getImage());
                //Parametros del alumno
                parametros.put("nombre", nombre);
                parametros.put("apellido", apellido);
                parametros.put("legajo", legajo);
                parametros.put("dni", dnialu);
                //Parametros del pago
                parametros.put("curso", nombrecurso);
                parametros.put("cursando", añocurso);
                parametros.put("cuota", preciocuota);
                parametros.put("interes", interesalu);
                parametros.put("fechavencimiento", fechainteres);
                parametros.put("deuda", deudaalu);
                parametros.put("fechapago", fechapagado);
                parametros.put("mes", mespago);
                parametros.put("importe", importetabla);
                try{
//                    JasperDesign jd=JRXmlLoader.load(new File("").getAbsolutePath()+"/src/reportes/rpComprobanteUnitario.jrxml");
//                    JasperDesign jd=JRXmlLoader.load("src/reportes/rpComprobanteUnitario.jrxml");//Para evitar el mal direccionamiento del comprobante en ejecucion
                    JasperDesign jd=JRXmlLoader.load(this.getClass().getResourceAsStream("/reportes/rpComprobanteUnitario.jrxml"));//pueden cargar ambos ficheros jasper cambiar JRXmlLoader por JRLoader deberan cargarlo com dato stream
                    JasperReport report=JasperCompileManager.compileReport(jd);
                    final JasperPrint print=JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
                    JasperViewer ver=new JasperViewer(print, false);//el false evita que se cierre la aplicacion cuando se cierre el comprobante
                    ver.setTitle("Comprobante de Pago");
                    ver.setVisible(true);
                    
                    File carp=new File("C:\\Users\\Public\\Pictures\\sistemaYES\\"+nombre+" "+apellido+" - "+dnialu);
                    if(!carp.exists()){
                        carp.mkdirs();
                    }
                    pdfhora=String.valueOf(mostrar.format(date));
                    String dest=("C:/Users/Public/Pictures/sistemaYES/"+nombre+" "+apellido+" - "+dnialu+"/"+pdfhora+".pdf");
                    JasperExportManager.exportReportToPdfFile(print, dest);
                }
                catch (JRException ex) {
                    JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
                }
                break;
            case 1:
                btncerrar.requestFocus(true);
                break;
            default:
                btncerrar.requestFocus(true);
                break;
        }
    }
    /*POR ALGUNA RAZON NO GUARDA MAS DE 1 ARCHIVO AL EJECUTAR LA SENTENCIA. ADEMAS DE SER LENTO EN EJECUCION LA PRIMERA VEZ QUE EJECUTA
    public void guardarInforme(){
        Map parametros = Collections.synchronizedMap(new HashMap());
//        HashMap<String, Object> parametros = new HashMap<>();
        //Parametros del alumno
        parametros.put("nombre", nombre);
        parametros.put("apellido", apellido);
        parametros.put("legajo", legajo);
        parametros.put("dni", dnialu);
        //Parametros del pago
        parametros.put("curso", nombrecurso);
        parametros.put("cursando", añocurso);
        parametros.put("cuota", preciocuota);
        parametros.put("interes", interesalu);
        parametros.put("fechavencimiento", fechainteres);
        parametros.put("deuda", deudaalu);
        parametros.put("fechapago", fechapagado);
        parametros.put("mes", mespago);
        parametros.put("importe", importetabla);
        try{
            JasperDesign jd=JRXmlLoader.load(new File("").getAbsolutePath()+"/src/reportes/rpComprobanteUnitario.jrxml");
            JasperReport report=JasperCompileManager.compileReport(jd);
            final JasperPrint print=JasperFillManager.fillReport(report, parametros, new JREmptyDataSource()); 
            File carp=new File("C:\\Users\\Public\\Pictures\\sistemaYES\\"+nombre+" "+apellido+" - "+dnialu);
            if(!carp.exists()){
                carp.mkdirs();
            }
            pdfhora=String.valueOf(mostrar.format(date));
            String dest=("C:/Users/Public/Pictures/sistemaYES/"+nombre+" "+apellido+" - "+dnialu+"/"+pdfhora+".pdf");
            JasperExportManager.exportReportToPdfFile(print, dest);
        }
        catch (JRException ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    */
    public void recuperarEstado(){
        try {
            minpago=clase.Pagar.pagoBorrado(cnx, idca, estadoimpaga);
            clase.Pagar.actualizarBorrado(cnx, minpago);
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    public void cambiarCondicion(){
        if(minpago!=0){
            maxpago=minpago;
        }
    }
    
    public void inicio(){
        txtcantcuota.setText("1");
        btnquitarseleccion.setEnabled(false);
        txtcambio.setEditable(false);
        txtdeuda.setEditable(false);
        pagardeuda.setEnabled(false);
        pagartodo.setEnabled(false);
        txtnomape.setEditable(false);
        txtdni.setEditable(false);
        txtimporte.setEnabled(false);
        fpago.setEnabled(false);
        mes.setEnabled(false);
        txtcantcuota.setEnabled(false);
        txtcambio.setEnabled(false);
    }
    
    public void clnfunc(){
        txtnomape.setText("");
        txtdni.setText("");
        txtdeuda.setText(" ");
        txtcambio.setText("");
        txtcambio.setEditable(false);
        pagardeuda.setEnabled(false);
        pagartodo.setEnabled(false);
        txtimporte.setText("");
        txtimporte.setEnabled(false);
        fpago.setEnabled(false);
        mes.setEnabled(false);
        txtcantcuota.setText("1");
        txtcantcuota.setEnabled(false);
        btnpagar.setEnabled(false);
        recuperarPerfil();
    }
    
    public void bloquearcoltabla(){
        tablaalumno.getTableHeader().setReorderingAllowed(false);
        tablapagos.getTableHeader().setReorderingAllowed(false);
        tablaaños.getTableHeader().setReorderingAllowed(false);
    }
    
    public void habilitarfunciones(){
        pagardeuda.setEnabled(true);
        pagartodo.setEnabled(true);
        txtimporte.setEnabled(true);
        txtcambio.setEnabled(true);
        txtcantcuota.setEnabled(true);
        fpago.setEnabled(true);
        mes.setEnabled(true);
    }
    
    public void limpiarfunc(){
        txtimporte.setText("");
        txtcantcuota.setText("1");
    }
    
    public void RB(){
        grupo1.clearSelection();
    }
    
    public void ptodoatv(){
        fpago.setEnabled(true);
        mes.setEnabled(true);
        txtcantcuota.setEnabled(true);
    }
    
    public void borrarCurso(){
        modelodos.setRowCount(0);
        btnquitarseleccion.setEnabled(false);
        pagardeuda.setEnabled(false);
        pagartodo.setEnabled(false);
        btnpagar.setEnabled(false);
        fpago.setEnabled(false);
        mes.setEnabled(false);
        txtcantcuota.setEnabled(false);
        txtdeuda.setText("");
        txtcambio.setText("");
    }
    
    public void recuperar(){
        modelodos.setRowCount(0);
        
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        grupo1 = new javax.swing.ButtonGroup();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        ComprobanteItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        CursoItem = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        CompItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        BorrarpagoItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        BorrarinteresItem = new javax.swing.JMenuItem();
        RestaurarItem = new javax.swing.JMenuItem();
        jPopupMenu3 = new javax.swing.JPopupMenu();
        PagarInsItem = new javax.swing.JMenuItem();
        EditarItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        RecuperarItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblbuscar = new javax.swing.JLabel();
        lblalumnos = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        cbxpor = new javax.swing.JComboBox<>();
        jPanel2 = new javax.swing.JPanel();
        lblnombreyapellido = new javax.swing.JLabel();
        lbldeuda = new javax.swing.JLabel();
        lbldni = new javax.swing.JLabel();
        txtnomape = new javax.swing.JTextField();
        txtdni = new javax.swing.JTextField();
        txtdeuda = new javax.swing.JTextField();
        pagardeuda = new javax.swing.JRadioButton();
        pagartodo = new javax.swing.JRadioButton();
        btnquitarseleccion = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        lblfechapago = new javax.swing.JLabel();
        lblmes = new javax.swing.JLabel();
        lblcuotas = new javax.swing.JLabel();
        lblimporte = new javax.swing.JLabel();
        lblcambio = new javax.swing.JLabel();
        fpago = new com.toedter.calendar.JDateChooser();
        mes = new com.toedter.calendar.JMonthChooser();
        txtcantcuota = new javax.swing.JTextField();
        txtimporte = new javax.swing.JTextField();
        txtcambio = new javax.swing.JTextField();
        btnpagar = new javax.swing.JButton();
        btncerrar = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        lbltituloperfil = new javax.swing.JLabel();
        lblperfil = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tablaaños = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablapagos = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        tablaalumno = new javax.swing.JTable();

        ComprobanteItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verificacion.png"))); // NOI18N
        ComprobanteItem.setText("Generar Comprobante");
        ComprobanteItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComprobanteItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(ComprobanteItem);
        jPopupMenu1.add(jSeparator1);

        CursoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        CursoItem.setText("Borrar Curso");
        CursoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursoItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(CursoItem);

        CompItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verificacion.png"))); // NOI18N
        CompItem.setText("Generar Comprobante");
        CompItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(CompItem);
        jPopupMenu2.add(jSeparator3);

        BorrarpagoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        BorrarpagoItem.setText("Deshacer Pago");
        BorrarpagoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarpagoItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(BorrarpagoItem);
        jPopupMenu2.add(jSeparator2);

        BorrarinteresItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/heart.png"))); // NOI18N
        BorrarinteresItem.setText("Borrar Interes");
        BorrarinteresItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarinteresItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(BorrarinteresItem);

        RestaurarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/refresh.png"))); // NOI18N
        RestaurarItem.setText("Recuperar Interes");
        RestaurarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RestaurarItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(RestaurarItem);

        PagarInsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ic_attach_money_black_18dp.png"))); // NOI18N
        PagarInsItem.setText("Pagar Inscripcion");
        PagarInsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarInsItemActionPerformed(evt);
            }
        });
        jPopupMenu3.add(PagarInsItem);

        EditarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/copy-1.png"))); // NOI18N
        EditarItem.setText("Ir a Registros");
        EditarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarItemActionPerformed(evt);
            }
        });
        jPopupMenu3.add(EditarItem);
        jPopupMenu3.add(jSeparator4);

        RecuperarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/refresh.png"))); // NOI18N
        RecuperarItem.setText("Recuperar Cursos");
        RecuperarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecuperarItemActionPerformed(evt);
            }
        });
        jPopupMenu3.add(RecuperarItem);

        setTitle("Formulario de Pagos");
        setMinimumSize(new java.awt.Dimension(200, 150));
        setName(""); // NOI18N
        setPreferredSize(new java.awt.Dimension(850, 665));
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

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblbuscar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        lblbuscar.setText("Buscar Alumno:");

        lblalumnos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        lblalumnos.setText("Alumnos");

        txtbuscar.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtbuscarFocusGained(evt);
            }
        });
        txtbuscar.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtbuscarKeyTyped(evt);
            }
        });

        cbxpor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LEGAJO", "DNI", "NOMBRE", "APELLIDO" }));
        cbxpor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxporItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblbuscar)
                .addGap(18, 18, 18)
                .addComponent(cbxpor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtbuscar)
                .addGap(18, 18, 18)
                .addComponent(lblalumnos)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblbuscar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cbxpor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar)
                    .addComponent(lblalumnos))
                .addContainerGap())
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblnombreyapellido.setText("Nombre y Apellido:");

        lbldeuda.setText("Deuda:");

        lbldni.setText("DNI:");

        txtnomape.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        txtdni.setFont(new java.awt.Font("Tahoma", 1, 12)); // NOI18N

        txtdeuda.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        grupo1.add(pagardeuda);
        pagardeuda.setText("Pagar Deuda");
        pagardeuda.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pagardeudaMouseClicked(evt);
            }
        });

        grupo1.add(pagartodo);
        pagartodo.setText("Pagar Todo el Importe");
        pagartodo.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pagartodoMouseClicked(evt);
            }
        });

        btnquitarseleccion.setText("Deshacer Seleccion");
        btnquitarseleccion.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnquitarseleccionActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbldeuda)
                    .addComponent(lblnombreyapellido))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtdeuda, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pagardeuda)
                        .addGap(43, 43, 43)
                        .addComponent(pagartodo))
                    .addComponent(txtnomape))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addComponent(lbldni)
                        .addGap(18, 18, 18)
                        .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnquitarseleccion, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtnomape, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblnombreyapellido)
                    .addComponent(lbldni)
                    .addComponent(txtdni, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtdeuda, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbldeuda)
                    .addComponent(pagardeuda)
                    .addComponent(pagartodo)
                    .addComponent(btnquitarseleccion))
                .addContainerGap(13, Short.MAX_VALUE))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        lblfechapago.setText("Fecha de pago:");

        lblmes.setText("Mes:");

        lblcuotas.setText("Cantidad de Cuotas:");

        lblimporte.setText("Importe:");

        lblcambio.setText("Cambio:");

        txtcantcuota.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtcantcuota.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtcantcuotaKeyTyped(evt);
            }
        });

        txtimporte.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        txtimporte.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtimporteFocusGained(evt);
            }
        });
        txtimporte.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtimporteKeyTyped(evt);
            }
        });

        txtcambio.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N

        btnpagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ic_monetization_on_black_18dp.png"))); // NOI18N
        btnpagar.setText("PAGAR");
        btnpagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpagarActionPerformed(evt);
            }
        });

        btncerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        btncerrar.setText("Cerrar");
        btncerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblimporte)
                    .addComponent(lblfechapago))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(fpago, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtimporte, javax.swing.GroupLayout.PREFERRED_SIZE, 133, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(43, 43, 43)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblcambio)
                    .addComponent(lblmes))
                .addGap(18, 18, 18)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(mes, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 80, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblcuotas))
                .addGap(19, 19, 19)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(txtcantcuota, javax.swing.GroupLayout.PREFERRED_SIZE, 103, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btncerrar, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btncerrar, btnpagar});

        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(txtcantcuota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(lblmes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(mes, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 24, Short.MAX_VALUE))))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel3Layout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(lblcuotas)
                                .addGap(0, 7, Short.MAX_VALUE)))
                        .addGap(18, 18, 18))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(13, 13, 13)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(fpago, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                            .addComponent(lblfechapago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblimporte)
                            .addComponent(txtimporte, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btncerrar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)
                            .addComponent(btnpagar, javax.swing.GroupLayout.DEFAULT_SIZE, 40, Short.MAX_VALUE)))
                    .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblcambio)))
                .addContainerGap())
        );

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtcambio, txtimporte});

        jPanel3Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btncerrar, btnpagar});

        jPanel4.setBorder(javax.swing.BorderFactory.createEtchedBorder(new java.awt.Color(0, 0, 0), null));

        lbltituloperfil.setText("                PERFIL DEL ALUMNO");

        lblperfil.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblperfil.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario.png"))); // NOI18N

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltituloperfil, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(28, Short.MAX_VALUE)
                .addComponent(lblperfil, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbltituloperfil)
                .addGap(1, 1, 1)
                .addComponent(lblperfil, javax.swing.GroupLayout.DEFAULT_SIZE, 129, Short.MAX_VALUE)
                .addContainerGap())
        );

        tablaaños.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tablaaños.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CURSO", "CUOTA", "CURSANDO", "AÑO INS"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Integer.class
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
        tablaaños.setComponentPopupMenu(jPopupMenu1);
        tablaaños.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaañosMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tablaaños);
        if (tablaaños.getColumnModel().getColumnCount() > 0) {
            tablaaños.getColumnModel().getColumn(0).setResizable(false);
            tablaaños.getColumnModel().getColumn(1).setResizable(false);
            tablaaños.getColumnModel().getColumn(2).setResizable(false);
            tablaaños.getColumnModel().getColumn(3).setResizable(false);
        }

        tablapagos.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tablapagos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MES", "VENCIMIENTO", "IMPORTE", "ESTADO", "INTERES", "PAGADO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class, java.lang.Float.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablapagos.setComponentPopupMenu(jPopupMenu2);
        tablapagos.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablapagosMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablapagos);
        if (tablapagos.getColumnModel().getColumnCount() > 0) {
            tablapagos.getColumnModel().getColumn(0).setResizable(false);
            tablapagos.getColumnModel().getColumn(1).setResizable(false);
            tablapagos.getColumnModel().getColumn(2).setResizable(false);
            tablapagos.getColumnModel().getColumn(3).setResizable(false);
            tablapagos.getColumnModel().getColumn(4).setResizable(false);
            tablapagos.getColumnModel().getColumn(5).setResizable(false);
        }

        tablaalumno.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tablaalumno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Legajo", "Nombre", "Apellido", "DNI"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
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
        tablaalumno.setComponentPopupMenu(jPopupMenu3);
        tablaalumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablaalumnoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tablaalumno);
        if (tablaalumno.getColumnModel().getColumnCount() > 0) {
            tablaalumno.getColumnModel().getColumn(0).setResizable(false);
            tablaalumno.getColumnModel().getColumn(1).setResizable(false);
            tablaalumno.getColumnModel().getColumn(2).setResizable(false);
            tablaalumno.getColumnModel().getColumn(3).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(23, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        inicio();
        tablaAlu();
        bloquearcoltabla();
        btnpagar.setEnabled(false);
        fpago.setDate(date);
    }//GEN-LAST:event_formInternalFrameOpened

    private void btncerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btncerrarActionPerformed

    private void btnpagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpagarActionPerformed
        // TODO add your handling code here:
        cantidadcuota=Integer.parseInt(txtcantcuota.getText());
        cantidadmeses=modelodos.getRowCount();
        cuotas=0;
        if(txtimporte.getText().isEmpty()||txtcantcuota.getText().isEmpty()||fpago.getDate()==null){
            JOptionPane.showMessageDialog(this, "Complete los campos:\nFECHA de PAGO\nCANTIDAD de CUOTAS\nIMPORTE","WARNING",WARNING_MESSAGE);
        }
        else{
            importe=Float.parseFloat(txtimporte.getText());
            if(importe>=0){
                if(cantidadcuota<=cantidadmeses){
                    try{
                        cuotas=clase.Pagar.cuotaImpaga(cnx, idca, estadoimpaga);
                        if((cuotas!=0)&&(Integer.parseInt(txtcantcuota.getText())==1)&&(pagardeuda.isSelected()==false)&&(pagartodo.isSelected()==false)){
                            int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea pagar la cuota?");
                            switch(op){
                                case 0:
                                    pagarMenor();
                                    break;
                                case 1:
                                    btncerrar.requestFocus(true);
                                    break;
                                default:
                                    clnfunc();
                                    modelouno.setRowCount(0);
                                    modelodos.setRowCount(0);
                                    break;
                            }
                        }
                        if((cuotas!=0)&&(Integer.parseInt(txtcantcuota.getText())>1)&&(pagardeuda.isSelected()==false)&&(pagartodo.isSelected()==false)){
                            int op1=JOptionPane.showConfirmDialog(this, "Esta seguro que desea pagar mas de una cuota?");
                            switch(op1){
                                case 0:
                                    pagarMayor();
                                    break;
                                case 1:
                                    btncerrar.requestFocus(true);
                                    break;
                                default:
                                    clnfunc();
                                    modelouno.setRowCount(0);
                                    modelodos.setRowCount(0);
                                    break;
                            }
                        }
                        if((cuotas!=0)&&(Integer.parseInt(txtcantcuota.getText())>=1)&&(pagardeuda.isSelected()==false)&&(pagartodo.isSelected()==true)){
                            int op2=JOptionPane.showConfirmDialog(this, "Esta seguro que desea pagar todo el importe a las cuotas?");
                            switch(op2){
                                case 0:
                                    pagarTodo();
                                    break;
                                case 1:
                                    btncerrar.requestFocus(true);
                                    break;
                                default:
                                    clnfunc();
                                    modelouno.setRowCount(0);
                                    modelodos.setRowCount(0);
                                    break;
                            }
                        }
                        if((pagardeuda.isSelected())==true&&(pagartodo.isSelected()==false)){
                            int op3=JOptionPane.showConfirmDialog(this, "Esta seguro que desea pagar la deuda del alumno/a?");
                            switch(op3){
                                case 0:
                                    pagarDeuda();
                                    break;
                                case 1:
                                    btncerrar.requestFocus(true);
                                    break;
                                default:
                                    clnfunc();
                                    modelouno.setRowCount(0);
                                    modelodos.setRowCount(0);
                                    break;
                            }
                        }
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No puede superar la cantidad de cuotas del alumno\nCantidad de cuotas del alumno/a: "+cantidadmeses,"WARNING",WARNING_MESSAGE);
                    txtcantcuota.requestFocus();
                    limpiarfunc();
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No se permiten numeros negativos","ERROR",ERROR_MESSAGE);
                txtimporte.requestFocus(true);
            }
            try{
                maxmes=clase.Pagar.ultimoIDmes(cnx, idca);
                estadomes=clase.Pagar.estadoMes(cnx, maxmes);
                if(estadomes.equalsIgnoreCase(estadopagado)){
                    JOptionPane.showMessageDialog(this,"El alumno/a: ".concat(txtnomape.getText())+" dni: "+txtdni.getText()+"\nTiene pagado todas sus cuotas "+"\nSu deuda es de $ "+deudaalu);
                    btnquitarseleccion.setEnabled(false);
                    RB();
                    limpiarfunc();
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_btnpagarActionPerformed

    private void pagardeudaMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pagardeudaMouseClicked
        // TODO add your handling code here:
        if(pagardeuda.isSelected()==true){
            fpago.setEnabled(false);
            mes.setEnabled(false);
            txtcantcuota.setText("1");
            txtcantcuota.setEnabled(false);
            btnquitarseleccion.setEnabled(true);
        }
    }//GEN-LAST:event_pagardeudaMouseClicked

    private void btnquitarseleccionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnquitarseleccionActionPerformed
        // TODO add your handling code here:
        if(pagardeuda.isSelected()==true||pagartodo.isSelected()==true){
            grupo1.clearSelection();
            fpago.setEnabled(true);
            mes.setEnabled(true);
            txtcantcuota.setEnabled(true);
            btnquitarseleccion.setEnabled(false);
        }
    }//GEN-LAST:event_btnquitarseleccionActionPerformed

    private void pagartodoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pagartodoMouseClicked
        // TODO add your handling code here:
        if(pagartodo.isSelected()==true){
            fpago.setEnabled(true);
            mes.setEnabled(true);
            txtcantcuota.setEnabled(true);
            txtcantcuota.setText("2");
            btnquitarseleccion.setEnabled(true);
        }
    }//GEN-LAST:event_pagartodoMouseClicked

    @SuppressWarnings("unchecked")
    private void txtbuscarKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarKeyTyped
        // TODO add your handling code here:
        int c=evt.getKeyChar();
        switch (cboxitem) {
            case 0:
                if(txtbuscar.getText().length()>=12){
                    evt.consume();
                }   if(!Character.isLetterOrDigit(c)){
                    evt.consume();
                }   break;
            case 1:
                if(txtbuscar.getText().length()>=8){
                    evt.consume();
                }   if(!Character.isDigit(c)){
                    evt.consume();
                }   break;
            case 2:
                if(txtbuscar.getText().length()>=16){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)) {
                    evt.consume();
                }   break;
            default:
                if(txtbuscar.getText().length()>=20){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)){
                    evt.consume();
                }   break;
        }
        txtbuscar.addKeyListener(new KeyAdapter(){
            @Override
            public void keyReleased(final KeyEvent e) {
                String cadena = (txtbuscar.getText().toUpperCase());
                txtbuscar.setText(cadena);
                repaint();
                filtro();
            }
        });
        trsFiltro = new TableRowSorter(tablaalumno.getModel());
        tablaalumno.setRowSorter(trsFiltro);
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscar.getText().toUpperCase()));
    }//GEN-LAST:event_txtbuscarKeyTyped
    
    private TableRowSorter trsFiltro;
    @SuppressWarnings("unchecked")
    public void filtro(){     
        int buscarpor=0; 
        if ("LEGAJO".equalsIgnoreCase(cbxpor.getSelectedItem().toString())){
            buscarpor=0;
        }
        if ("NOMBRE".equalsIgnoreCase(cbxpor.getSelectedItem().toString())){
            buscarpor=1;
        }
        if ("APELLIDO".equalsIgnoreCase(cbxpor.getSelectedItem().toString())){
            buscarpor=2;
        }
        if ("DNI".equalsIgnoreCase(cbxpor.getSelectedItem().toString())){
            buscarpor=3;
        }
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscar.getText(), buscarpor));
    }
    
    private void tablaalumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaalumnoMouseClicked
        // TODO add your handling code here:
        int fila=tablaalumno.rowAtPoint(evt.getPoint());
        RB();
        clnfunc();
        btnquitarseleccion.setEnabled(false);
        legajo=tablaalumno.getValueAt(fila, 0).toString();
        txtnomape.setText(tablaalumno.getValueAt(fila, 1).toString().concat(" ").concat(tablaalumno.getValueAt(fila, 2).toString()));
        nombre=tablaalumno.getValueAt(fila, 1).toString();
        apellido=tablaalumno.getValueAt(fila, 2).toString();
        txtdni.setText(tablaalumno.getValueAt(fila, 3).toString());
        dnialu=Integer.parseInt(txtdni.getText());
        tablaAños();
        traerImagen();
        btnpagar.setEnabled(true);
        modelodos.setRowCount(0);
    }//GEN-LAST:event_tablaalumnoMouseClicked

    private void txtcantcuotaKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtcantcuotaKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(txtcantcuota.getText().length()>=2){
            evt.consume();
        }
        if (!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_txtcantcuotaKeyTyped

    private void txtimporteKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtimporteKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(txtimporte.getText().length()>=12){
            evt.consume();
        }
        if (!Character.isDigit(c)&&(c>'.')){
            evt.consume();
        }
    }//GEN-LAST:event_txtimporteKeyTyped

    private void txtbuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtbuscarFocusGained
        // TODO add your handling code here:
        btnquitarseleccion.setEnabled(false);
        RB();
        clnfunc();
        tablaalumno.clearSelection();
        modelouno.setRowCount(0);
        modelodos.setRowCount(0);
    }//GEN-LAST:event_txtbuscarFocusGained

    private void txtimporteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtimporteFocusGained
        // TODO add your handling code here:
        txtcambio.setText(" ");
    }//GEN-LAST:event_txtimporteFocusGained

    private void tablaañosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablaañosMouseClicked
        // TODO add your handling code here:
        int fila=tablaaños.rowAtPoint(evt.getPoint());
        nombrecurso=tablaaños.getValueAt(fila, 0).toString();
        preciocuota=Float.parseFloat(tablaaños.getValueAt(fila, 1).toString());
        añocurso=tablaaños.getValueAt(fila, 2).toString();
        añoins=Integer.parseInt(tablaaños.getValueAt(fila, 3).toString());
        try{
            idcur=clase.Pagar.leerIDCurso(cnx, nombrecurso);
            idca=clase.Pagar.buscarIDCA(cnx, idcur, idalu, añoins, añocurso);
            ciclos=clase.Pagar.ciclos(cnx,idca);
            tablaCuotas();
            for (int i=0; i<ciclos; i++){
                calcularInteres();
            }
            actualizardeuda();
            habilitarfunciones();
            RB();
            btnquitarseleccion.setEnabled(false);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
        idc=0;
        btnpagar.setEnabled(true);
        txtcambio.setText("");
    }//GEN-LAST:event_tablaañosMouseClicked

    private void BorrarpagoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarpagoItemActionPerformed
        // TODO add your handling code here:
        gendeuda=0;variable=0;
        if(idc!=0){
            try{
                int op=JOptionPane.showConfirmDialog(this,"Esta seguro de deshacer el pago?");
                if(op==0){
                    if(estadomes.equalsIgnoreCase(estadopagado)){
                        if(importetabla<preciocuota){
                            deuda=preciocuota-importetabla;
                            deudaalu=clase.Pagar.buscarDeuda(cnx,idca);
                            if(deudaalu>=deuda){
                                txtcambio.setText("$ ".concat(String.valueOf(importetabla)));
                                clase.Pagar.deshacerPago(cnx,estadoimpaga,idc);
                                variable=preciocuota-importetabla;
                                gendeuda=deudaalu-variable;
                                clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                                actualizardeuda();
                                tablaCuotas();
                            }
                            else{
                                JOptionPane.showMessageDialog(this, "Lo sentimos...parte o la totalidad de la deuda\ndel curso a sido pagada.\n\nNo es posible deshacer el pago de la\ncuota del curso en este momento","WARNING",WARNING_MESSAGE);
                            }   
                        }
                        else{
                            txtcambio.setText("$ ".concat(String.valueOf(importetabla)));
                            clase.Pagar.deshacerPago(cnx,estadoimpaga,idc);
                            actualizardeuda();
                            tablaCuotas();
                        }
                    }
                    else{
                        JOptionPane.showMessageDialog(this, "El mes no esta pagado","ERROR",ERROR_MESSAGE);
                    }
                }
                if(op==1){
                    btncerrar.requestFocus(true);
                }
                else{
                    btnpagar.requestFocus(true);
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ninguna cuota","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BorrarpagoItemActionPerformed

    private void tablapagosMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablapagosMouseClicked
        // TODO add your handling code here:
        int fila=tablapagos.rowAtPoint(evt.getPoint());
        mespago=tablapagos.getValueAt(fila, 0).toString();
        fechainteres=tablapagos.getValueAt(fila, 1).toString();
//        importe=Float.parseFloat(tablapagos.getValueAt(fila, 2).toString());
        estadomes=tablapagos.getValueAt(fila, 3).toString();
        interesalu=Float.parseFloat(tablapagos.getValueAt(fila, 4).toString());
        fechapagado=(Date)(tablapagos.getValueAt(fila, 5));
        idc=0;
        try{
            idc=clase.Pagar.mesPagado(cnx, idca,fechainteres,estadomes);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
        if(idc!=0){
            importetabla=Float.parseFloat(tablapagos.getValueAt(fila, 2).toString());
        }
        txtcambio.setText("");
    }//GEN-LAST:event_tablapagosMouseClicked

    private void ComprobanteItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComprobanteItemActionPerformed
        // TODO add your handling code here:
        if(idca!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea generar el reporte total de las cuotas del alumno?");
            switch(op){
                case 0:
                    generarComprobante();
                    break;
                case 1:
                    btncerrar.requestFocus(true);
                    break;
                default:
                    btncerrar.requestFocus(true);
                    break;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_ComprobanteItemActionPerformed

    private void cbxporItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxporItemStateChanged
        // TODO add your handling code here:
        cboxitem=cbxpor.getSelectedIndex();
        txtbuscar.requestFocus(true);
    }//GEN-LAST:event_cbxporItemStateChanged

    private void BorrarinteresItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarinteresItemActionPerformed
        // TODO add your handling code here:
        gendeuda=0;
        if(idc!=0){
            try{
                int op=JOptionPane.showConfirmDialog(this,"Esta seguro de borrar la deuda de este mes?");
                if(op==0){
                    if(interesalu!=0){
                        clase.Pagar.guardarInteres(cnx, interesalu, idc);
                        clase.Pagar.perdonar(cnx, 0, idc);
                        deudaalu=clase.Pagar.buscarDeuda(cnx,idca);
                        gendeuda=deudaalu-interesalu;
                        clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                        tablaCuotas();
                        actualizardeuda();
                        txtcambio.setText("$ ".concat(String.valueOf(deudaalu)));
                    }
                    else{
                        perdonar=clase.Pagar.leerPerd(cnx, idc);
                        if(perdonar==true){
                            int op1=JOptionPane.showConfirmDialog(this, "La deuda de este mes fue borrada\nDesea recuperar la deuda?");
                            switch(op1){
                                case 0:
                                    interescurso=clase.Pagar.recuperarInteres(cnx, idc);
                                    gendeuda=deudaalu+interescurso;
                                    clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                                    clase.Pagar.deshacerDeuda(cnx,interescurso, idc);
                                    clase.Pagar.guardarInteres(cnx, 0, idc);
                                    tablaCuotas();
                                    actualizardeuda();
                                    break;
                                case 1:
                                    btncerrar.requestFocus(true);
                                    break;
                                default:
                                    btncerrar.requestFocus(true);
                                    break;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Aqui no hay deuda","ERROR",ERROR_MESSAGE);
                        }
                    }
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ninguna cuota","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_BorrarinteresItemActionPerformed

    private void CursoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursoItemActionPerformed
        // TODO add your handling code here:
        if(idca!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de borrar este curso ".concat(nombrecurso).concat("?"));
            switch(op){
                case 0:
                    try{
                        clase.Pagar.borrarCursoAlumno(cnx, idca);
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    borrarCurso();
                    limpiarfunc();
                    RB();
                    tablaAños();
                    break;
                case 1:
                    btncerrar.requestFocus(true);
                    break;
                default:
                    btncerrar.requestFocus(true);
                    break;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_CursoItemActionPerformed

    private void PagarInsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarInsItemActionPerformed
        // TODO add your handling code here:
        abmEstado estado=new abmEstado();
        estado.txtbuscar.setText(txtdni.getText());
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=estado.getSize();
        estado.setLocation((desktopSize.width-FrameSize.width)/27,(desktopSize.height-FrameSize.height)/15);
        panelA.add(estado);
        Animacion.Animacion.mover_derecha(0, 110, 5, 5, panelA);
        estado.show();
        estado.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_PagarInsItemActionPerformed

    private void EditarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarItemActionPerformed
        // TODO add your handling code here:
        abmRegistros corregir=new abmRegistros();
        corregir.txtbuscar.setText(txtdni.getText());
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=corregir.getSize();
        corregir.setLocation((desktopSize.width-FrameSize.width)/14,(desktopSize.height-FrameSize.height)/16);
        panelA.add(corregir);
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        corregir.show();
        corregir.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_EditarItemActionPerformed

    private void CompItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompItemActionPerformed
        // TODO add your handling code here:
        if(idc!=0){
            generarInforme();
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ninguna cuota","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_CompItemActionPerformed

    private void RestaurarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RestaurarItemActionPerformed
        // TODO add your handling code here:
        gendeuda=0;
        if(idc!=0){
            try{
                perdonar=clase.Pagar.leerPerd(cnx, idc);
                if(perdonar==true){
                    int op=JOptionPane.showConfirmDialog(this, "Desea recuperar la deuda de este mes?");
                    switch(op){
                        case 0:
                            interescurso=clase.Pagar.recuperarInteres(cnx, idc);
                            gendeuda=deudaalu+interescurso;
                            clase.Pagar.insertarDeuda(cnx,gendeuda,idca);
                            clase.Pagar.deshacerDeuda(cnx,interescurso, idc);
                            clase.Pagar.guardarInteres(cnx, 0, idc);
                            tablaCuotas();
                            actualizardeuda();
                            break;
                        case 1:
                            btncerrar.requestFocus(true);
                            break;
                        default:
                            btncerrar.requestFocus(true);
                            break;
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "Aqui no hay deuda","ERROR",ERROR_MESSAGE);
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }                
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ninguna cuota","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_RestaurarItemActionPerformed

    private void RecuperarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecuperarItemActionPerformed
        // TODO add your handling code here:
        if(idalu!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea recuperar el o los cursos?");
            switch(op){
                case 0:
                    try {
                        clase.Pagar.recuperarCursos(cnx, idalu);
                    } 
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    tablaAños();
                    break;
                case 1:
                    btncerrar.requestFocus(true);
                    break;
                default:
                    btncerrar.requestFocus(true);
                    break;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono a ningun alumno","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_RecuperarItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BorrarinteresItem;
    private javax.swing.JMenuItem BorrarpagoItem;
    private javax.swing.JMenuItem CompItem;
    private javax.swing.JMenuItem ComprobanteItem;
    private javax.swing.JMenuItem CursoItem;
    private javax.swing.JMenuItem EditarItem;
    private javax.swing.JMenuItem PagarInsItem;
    private javax.swing.JMenuItem RecuperarItem;
    private javax.swing.JMenuItem RestaurarItem;
    private javax.swing.JButton btncerrar;
    private javax.swing.JButton btnpagar;
    private javax.swing.JButton btnquitarseleccion;
    private javax.swing.JComboBox<String> cbxpor;
    private com.toedter.calendar.JDateChooser fpago;
    private javax.swing.ButtonGroup grupo1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JPopupMenu jPopupMenu3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JLabel lblalumnos;
    private javax.swing.JLabel lblbuscar;
    private javax.swing.JLabel lblcambio;
    private javax.swing.JLabel lblcuotas;
    private javax.swing.JLabel lbldeuda;
    private javax.swing.JLabel lbldni;
    private javax.swing.JLabel lblfechapago;
    private javax.swing.JLabel lblimporte;
    private javax.swing.JLabel lblmes;
    private javax.swing.JLabel lblnombreyapellido;
    private javax.swing.JLabel lblperfil;
    private javax.swing.JLabel lbltituloperfil;
    private com.toedter.calendar.JMonthChooser mes;
    private javax.swing.JRadioButton pagardeuda;
    private javax.swing.JRadioButton pagartodo;
    private javax.swing.JTable tablaalumno;
    private javax.swing.JTable tablaaños;
    private javax.swing.JTable tablapagos;
    public javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcambio;
    private javax.swing.JTextField txtcantcuota;
    private javax.swing.JTextField txtdeuda;
    private javax.swing.JTextField txtdni;
    private javax.swing.JTextField txtimporte;
    private javax.swing.JTextField txtnomape;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmPagar.class.getName());
}
