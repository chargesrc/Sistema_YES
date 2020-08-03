/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
import static interfaces.abmPrincipal.panelA;
import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
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
public class abmEstado extends javax.swing.JInternalFrame {
    Connection cnx=conexion.Conexion.conectar();
    ResultSet rs,rss;
    Object[] dato=new Object[4];
    DefaultTableModel modelo=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    Object[] datocur=new Object[5];
    DefaultTableModel modelodos=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    Object[] datoest=new Object[6];
    DefaultTableModel modelotres=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    Date date=new Date();
    LocalDate localDate=date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    SimpleDateFormat mostrar=new SimpleDateFormat("dd MMM YYYY' - hs-'HH'.'mm'.'ss'.'SSS");
    
    int idca,idalu,idper,idcur,idpi;
    int añoinsc,dni,maxpago;
    float deudains,importe,precio,resto,variabledeuda;
    String curso,cursando,nombre,apellido,estado,legajo,fechapins,pdfhora;
    Date fechapago;
    public int cboxitem=0;
    boolean band=false,var=false;
    /**
     * Creates new form abmEstado
     */
    public abmEstado() {
        initComponents();
    }
    
    public void mostrarPersona(){
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        modelo.addColumn("LEGAJO");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("DNI");
        try {
            tblalumno.setModel(modelo);
            rs=clase.Estado.MostrarPersona(cnx);
            while(rs.next()){
                dato[0]=rs.getString("legajo");
                dato[1]=rs.getString("nombre");
                dato[2]=rs.getString("apellido");
                dato[3]=rs.getInt("dni");
                modelo.addRow(dato);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarCursos(){
        modelodos.setColumnCount(0);
        modelodos.setRowCount(0);
        modelodos.addColumn("Curso");
        modelodos.addColumn("Año Insc.");
        modelodos.addColumn("Cursando");
        modelodos.addColumn("Estado");
        modelodos.addColumn("Deuda Insc.");
        try{
            tblcurso.setModel(modelodos);
            idalu=clase.Estado.buscarIDAlu(cnx, dni);
            idper=clase.Estado.leerIdPersona(cnx, dni);
            rs=clase.Estado.mostrarCursos(cnx,idper);
            while(rs.next()){
                datocur[0]=rs.getString("nombre");
                datocur[1]=rs.getInt("añoLectivo");
                datocur[2]=rs.getString("añoCurso");
                datocur[3]=rs.getString("estado");
                datocur[4]=rs.getFloat("deudaInscripcion");
                modelodos.addRow(datocur);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarEstado(){
        modelotres.setColumnCount(0);
        modelotres.setRowCount(0);
        modelotres.addColumn("Nombre");
        modelotres.addColumn("Est. Academico");
        modelotres.addColumn("Precio de Inscripcion");
        modelotres.addColumn("Estado");
        modelotres.addColumn("Importe");
        modelotres.addColumn("Fecha de Pago");
        try{
            tblcuotaIns.setModel(modelotres);
            idalu=clase.Estado.buscarIDAlu(cnx, dni);
            idcur=clase.Estado.leerIDCurso(cnx, curso);
            idca=clase.Estado.buscarIDCA(cnx, idalu, idcur, añoinsc, cursando);
            rs=clase.Estado.mostrarEstado(cnx, idca);
            while(rs.next()){
                datoest[0]=rs.getString("nombre");
                datoest[1]=rs.getString("egresado");
                datoest[2]=rs.getFloat("precioInscripcion");
                datoest[3]=rs.getString("estado");
                datoest[4]=rs.getFloat("importe");
                datoest[5]=rs.getDate("fechaPago");
                modelotres.addRow(datoest);
            }
            precio=Float.parseFloat(datoest[2].toString());
            importe=Float.parseFloat((datoest[4]).toString());
            fechapago=(Date) datoest[5];
            fechapins=String.valueOf(fechapago);
            band = fechapago==null;//si fechapago=null devuelve true en caso contrario false
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void buscarIDPrecio(){
        if(band==true){
            idpi=0;
        }
        else{
            try{
                idpi=clase.Estado.leerIDPrecio(cnx, idca, (java.sql.Date) fechapago, estado);
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
    }
    
    public void actualizarDeuda(){
        try{
            deudains=clase.Estado.buscarDeuda(cnx, idca);
            txtdeudainsc.setText("$ ".concat(String.valueOf(deudains)));
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void pagarIns(){
        importe=Float.parseFloat(txtimporte.getText());
        try{
            if(importe>=0){
                if(estado.equalsIgnoreCase("IMPAGA")){
                    if((importe<precio)&&(importe>=0)&&(chboxdeuda.isSelected()==false)){
                        resto=precio-importe;
                        variabledeuda=resto;
                        txtcambio.setText("$ 0.0");
                        clase.Estado.insertarPagoIns(cnx, dtepago.getDate(), importe, "PAGADO", idca);
                        clase.Estado.actualizarDeuda(cnx, resto, idca);
                        JOptionPane.showMessageDialog(this, "Se agrego $ ".concat(String.valueOf(resto)).concat(" de deuda al\nAlumno/a: ").concat(nombre.concat(" ".concat(apellido)).concat("\ndni: ".concat(String.valueOf(dni)))));
                        mostrarCursos();
                        mostrarEstado();
                        actualizarDeuda();
                        limpiarPago();
                        recuperarDatosInforme();
                        generarComprobante();
                    }
                    if((importe==precio)&&(importe>=0)&&(chboxdeuda.isSelected()==false)){
                        txtcambio.setText("$ 0.0");
                        clase.Estado.insertarPagoIns(cnx, dtepago.getDate(), importe, "PAGADO", idca);
                        mostrarCursos();
                        mostrarEstado();
                        limpiarPago();
                        recuperarDatosInforme();
                        generarComprobante();
                    }
                    if((importe>precio)&&(importe>=0)&&(chboxdeuda.isSelected()==false)){
                        resto=importe-precio;
                        txtcambio.setText("$ ".concat(String.valueOf(resto)));
                        clase.Estado.insertarPagoIns(cnx, dtepago.getDate(), importe, "PAGADO", idca);
                        mostrarCursos();
                        mostrarEstado();
                        limpiarPago();
                        recuperarDatosInforme();
                        generarComprobante();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "La cuota de inscripcion ya esta pagada!!!\nSi desea pagar la deuda seleccione la casilla 'Pagar Deuda de Inscripcion'","WARNING",WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No se permiten numeros negativos");
                txtimporte.requestFocus(true);
            } 
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void pagarDeu(){
        importe=Float.parseFloat(txtimporte.getText());
        try{
            if(importe>=0){
                if ((estado.equalsIgnoreCase("PAGADO"))&&(chboxdeuda.isSelected()==true)&&(deudains>0)){
                    if((importe<deudains)&&(importe>=0)&&(chboxdeuda.isSelected()==true)){
                        resto=deudains-importe;
                        txtcambio.setText("$ 0.0");
                        clase.Estado.actualizarDeuda(cnx, resto, idca);
                        JOptionPane.showMessageDialog(this, "Se pago $ ".concat(String.valueOf(importe)).concat(" de deuda al\nAlumno/a: ").concat(nombre.concat(" ").concat(apellido)).concat("\ndni: ").concat(String.valueOf(dni)).concat("\nAun existe $ ").concat(String.valueOf(resto)).concat(" de deuda sin pagar"));
                        mostrarCursos();
                        mostrarEstado();
                        clnPagar();
                        actualizarDeuda();
                        limpiarPago(); 
                    }
                    if((importe==deudains)&&(importe>=0)&&(chboxdeuda.isSelected()==true)){
                        txtcambio.setText("$ 0.0");
                        clase.Estado.actualizarDeuda(cnx, (float)0, idca);
                        actualizarDeuda();
                        JOptionPane.showMessageDialog(this, "Se pago toda la deuda\ndel alumno/a: ".concat(nombre.concat(" ").concat(apellido)).concat("\ndni: ").concat(String.valueOf(dni)));
                        mostrarCursos();
                        mostrarEstado();
                        clnPagar();
                        limpiarPago();
                    }
                    if((importe>deudains)&&(importe>=0)&&(chboxdeuda.isSelected()==true)){
                        resto=importe-deudains;
                        txtcambio.setText("$ ".concat(String.valueOf(resto)));
                        clase.Estado.actualizarDeuda(cnx, (float)0, idca);
                        actualizarDeuda();
                        JOptionPane.showMessageDialog(this, "Se pago toda la deuda\ndel alumno/a: ".concat(nombre.concat(" ").concat(apellido)).concat("\ndni: ").concat(String.valueOf(dni)));
                        mostrarCursos();
                        mostrarEstado();
                        clnPagar();
                        limpiarPago();
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No hay Deuda de Inscripcion","WARNING",WARNING_MESSAGE);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No se permiten numeros negativos","ERROR",ERROR_MESSAGE);
                txtimporte.requestFocus(true);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void generarComprobante(){
        int op=JOptionPane.showConfirmDialog(this, "Desea generar el comprobante?");
        switch(op){
            case 0:
                HashMap<String, Object> parametros = new HashMap<>();
                //Parametros del alumno
                parametros.put("nombre", nombre);
                parametros.put("apellido", apellido);
                parametros.put("legajo", legajo);
                parametros.put("dni", dni);
                //Parametros del pago
                parametros.put("curso", curso);
                parametros.put("cursando", cursando);
                parametros.put("precioins", precio);
                parametros.put("fechapago", fechapins);
                parametros.put("deudains", variabledeuda);
                parametros.put("importe", importe);
                try{    
                    JasperDesign jd=JRXmlLoader.load(new File("").getAbsolutePath()+"/src/reportes/rpComprobanteInsc.jrxml");
                    JasperReport report=JasperCompileManager.compileReport(jd);
                    JasperPrint print=JasperFillManager.fillReport(report, parametros, new JREmptyDataSource());
                    JasperViewer jv=new JasperViewer(print,false);
                    jv.setTitle("Comprobante de Pago");
                    jv.setVisible(true);
                    //Creamos el directorio
                    File carp=new File("C:\\Users\\Public\\Pictures\\sistemaYES\\"+nombre+" "+apellido+" - "+dni);
                    if(!carp.exists()){
                        carp.mkdirs();
                    }
                    //Guarda en pdf
                    pdfhora=String.valueOf(mostrar.format(date));
                    String dest=("C:/Users/Public/Pictures/sistemaYES/"+nombre+" "+apellido+" - "+dni+"/"+pdfhora+".pdf");
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
    
    public void recuperarDatosInforme(){
        try{
            rss=clase.Estado.buscarInforme(cnx, idca);
            while(rss.next()){
                fechapago=rss.getDate(1);
                fechapins=String.valueOf(fechapago);
                importe=rss.getFloat(2);
            }            
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void bloquearFunciones(){
        btnpagar.setEnabled(false);
        btncancelar.setEnabled(false);
        txtcambio.setEditable(false);
        txtdeudainsc.setEditable(false);
    }
    
    public void habilitarFunciones(){
        btnpagar.setEnabled(true);
        btncancelar.setEnabled(true);
    }
    
    public void btnCancelar(){
        txtdeudainsc.setText("");
        txtimporte.setText("");
        txtcambio.setText("");
        chboxdeuda.setSelected(false);
        modelodos.setRowCount(0);
        modelotres.setRowCount(0);
        tblalumno.clearSelection();
    }
    
    public void bloqCanc(){
        btnpagar.setEnabled(false);
        btncancelar.setEnabled(false);
        txtbuscar.requestFocus(true);
    }
    
    public void cleanSeleccion(){
        txtdeudainsc.setText("");
        txtimporte.setText("");
        txtcambio.setText("");
        chboxdeuda.setSelected(false);
        modelodos.setRowCount(0);
        modelotres.setRowCount(0);
        btnpagar.setEnabled(false);
        btncancelar.setEnabled(false);
    }
    
    public void limpiarPago(){
        txtimporte.setText("");
        btnpagar.setEnabled(false);
//        modelotres.setRowCount(0);
    }
    
    public void clnPagar(){
        txtdeudainsc.setText("$ ".concat(String.valueOf(deudains)));
        chboxdeuda.setSelected(false);
//        tblcuotaIns.clearSelection();
    }
    
    public void borrarcurso(){
        txtimporte.setText("");
        txtdeudainsc.setText("");
        txtcambio.setText("");
        chboxdeuda.setSelected(false);
        btnpagar.setEnabled(false);
        btncancelar.setEnabled(false);
        modelotres.setRowCount(0);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        Popupalu = new javax.swing.JPopupMenu();
        PagarItem = new javax.swing.JMenuItem();
        EditarItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        RecuperarItem = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        MostrarItem = new javax.swing.JMenuItem();
        Popupcur = new javax.swing.JPopupMenu();
        CompItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        BorrarItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        OcultarItem = new javax.swing.JMenuItem();
        Popudat = new javax.swing.JPopupMenu();
        CompInsItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        BorrarPagoItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblbuscar = new javax.swing.JLabel();
        cboxpor = new javax.swing.JComboBox<>();
        txtbuscar = new javax.swing.JTextField();
        lblicono = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblalumno = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcuotaIns = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        txtimporte = new javax.swing.JTextField();
        lblimporte = new javax.swing.JLabel();
        lblcambio = new javax.swing.JLabel();
        txtcambio = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        txtdeudainsc = new javax.swing.JTextField();
        chboxdeuda = new javax.swing.JCheckBox();
        dtepago = new com.toedter.calendar.JDateChooser();
        lblfechaPago = new javax.swing.JLabel();
        btncerrar = new javax.swing.JButton();
        btnpagar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblcurso = new javax.swing.JTable();

        PagarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money.png"))); // NOI18N
        PagarItem.setText("Ir a Pagar");
        PagarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarItemActionPerformed(evt);
            }
        });
        Popupalu.add(PagarItem);

        EditarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/copy-1.png"))); // NOI18N
        EditarItem.setText("Ir a Registros");
        EditarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EditarItemActionPerformed(evt);
            }
        });
        Popupalu.add(EditarItem);
        Popupalu.add(jSeparator4);

        RecuperarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/refresh.png"))); // NOI18N
        RecuperarItem.setText("Recuperar Cursos");
        RecuperarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecuperarItemActionPerformed(evt);
            }
        });
        Popupalu.add(RecuperarItem);
        Popupalu.add(jSeparator5);

        MostrarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/unlock.png"))); // NOI18N
        MostrarItem.setText("Mostrar");
        MostrarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MostrarItemActionPerformed(evt);
            }
        });
        Popupalu.add(MostrarItem);

        CompItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verificacion.png"))); // NOI18N
        CompItem.setText("Generar Comprobante");
        CompItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompItemActionPerformed(evt);
            }
        });
        Popupcur.add(CompItem);
        Popupcur.add(jSeparator1);

        BorrarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        BorrarItem.setText("Borrar Curso");
        BorrarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarItemActionPerformed(evt);
            }
        });
        Popupcur.add(BorrarItem);
        Popupcur.add(jSeparator2);

        OcultarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lock.png"))); // NOI18N
        OcultarItem.setText("Ocultar");
        OcultarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OcultarItemActionPerformed(evt);
            }
        });
        Popupcur.add(OcultarItem);

        CompInsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/verificacion.png"))); // NOI18N
        CompInsItem.setText("Generar Comprobante");
        CompInsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CompInsItemActionPerformed(evt);
            }
        });
        Popudat.add(CompInsItem);
        Popudat.add(jSeparator3);

        BorrarPagoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        BorrarPagoItem.setText("Deshacer Pago");
        BorrarPagoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BorrarPagoItemActionPerformed(evt);
            }
        });
        Popudat.add(BorrarPagoItem);

        setTitle("Formulario del Pago de Inscripcion");
        setPreferredSize(new java.awt.Dimension(900, 604));
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

        cboxpor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LEGAJO", "NOMBRE", "APELLIDO", "DNI" }));
        cboxpor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxporItemStateChanged(evt);
            }
        });

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

        lblicono.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        lblicono.setText("Alumnos");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblbuscar)
                .addGap(18, 18, 18)
                .addComponent(cboxpor, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtbuscar)
                .addGap(18, 18, 18)
                .addComponent(lblicono)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboxpor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblbuscar)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblicono))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tblalumno.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tblalumno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Legajo", "Nombre", "Apellido", "DNI"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
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
        tblalumno.setComponentPopupMenu(Popupalu);
        tblalumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblalumnoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblalumno);
        if (tblalumno.getColumnModel().getColumnCount() > 0) {
            tblalumno.getColumnModel().getColumn(0).setResizable(false);
            tblalumno.getColumnModel().getColumn(1).setResizable(false);
            tblalumno.getColumnModel().getColumn(2).setResizable(false);
            tblalumno.getColumnModel().getColumn(3).setResizable(false);
        }

        tblcuotaIns.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Nombre", "Est. Academico", "Precio de Inscripcion", "Estado", "Importe", "Fecha de Pago"
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
        tblcuotaIns.setComponentPopupMenu(Popudat);
        tblcuotaIns.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcuotaInsMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblcuotaIns);
        if (tblcuotaIns.getColumnModel().getColumnCount() > 0) {
            tblcuotaIns.getColumnModel().getColumn(0).setResizable(false);
            tblcuotaIns.getColumnModel().getColumn(1).setResizable(false);
            tblcuotaIns.getColumnModel().getColumn(2).setResizable(false);
            tblcuotaIns.getColumnModel().getColumn(3).setResizable(false);
            tblcuotaIns.getColumnModel().getColumn(4).setResizable(false);
            tblcuotaIns.getColumnModel().getColumn(5).setResizable(false);
        }

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Datos de Pago", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        txtimporte.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
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

        lblimporte.setText("Importe:");

        lblcambio.setText("Cambio:");

        txtcambio.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        jLabel1.setText("Deuda de Inscripcion:");

        txtdeudainsc.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N

        chboxdeuda.setText("Pagar Deuda de Inscripción");

        lblfechaPago.setText("Fecha de Pago:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtdeudainsc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(chboxdeuda)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblfechaPago)
                        .addGap(18, 18, 18)
                        .addComponent(dtepago, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(lblimporte)
                        .addGap(18, 18, 18)
                        .addComponent(txtimporte, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(42, 42, 42)
                        .addComponent(lblcambio)
                        .addGap(18, 18, 18)
                        .addComponent(txtcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(chboxdeuda)
                        .addComponent(txtdeudainsc, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(dtepago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblfechaPago, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblcambio)
                            .addComponent(txtimporte, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblimporte)
                            .addComponent(txtcambio, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        btncerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        btncerrar.setText("Cerrar");
        btncerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarActionPerformed(evt);
            }
        });

        btnpagar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money.png"))); // NOI18N
        btnpagar.setText("Pagar");
        btnpagar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnpagarActionPerformed(evt);
            }
        });

        btncancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help_ring-buoy.png"))); // NOI18N
        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        tblcurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Curso", "Año Insc.", "Cursando", "Estado", "Deuda Insc."
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Float.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblcurso.setComponentPopupMenu(Popupcur);
        tblcurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcursoMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblcurso);
        if (tblcurso.getColumnModel().getColumnCount() > 0) {
            tblcurso.getColumnModel().getColumn(0).setResizable(false);
            tblcurso.getColumnModel().getColumn(1).setResizable(false);
            tblcurso.getColumnModel().getColumn(2).setResizable(false);
            tblcurso.getColumnModel().getColumn(3).setResizable(false);
            tblcurso.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 400, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnpagar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(33, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncerrarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btncerrarActionPerformed

    private void cboxporItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxporItemStateChanged
        // TODO add your handling code here:
        cboxitem=cboxpor.getSelectedIndex();
        txtbuscar.requestFocus(true);
    }//GEN-LAST:event_cboxporItemStateChanged

    private void txtbuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtbuscarFocusGained
        // TODO add your handling code here:
        btnCancelar();
        btnpagar.setEnabled(false);
        btncancelar.setEnabled(false);
    }//GEN-LAST:event_txtbuscarFocusGained

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
                if(txtbuscar.getText().length()>=16){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)) {
                    evt.consume();
                }   break;            
            case 2:
                if(txtbuscar.getText().length()>=20){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)){
                    evt.consume();
                }   break;
            default:
                if(txtbuscar.getText().length()>=8){
                    evt.consume();
                }   if(!Character.isDigit(c)){
                    evt.consume();
                }   break;
        }
        txtbuscar.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                String cadena = (txtbuscar.getText().toUpperCase());
                txtbuscar.setText(cadena);
                repaint();
                filtro();
            }
        });
        trsFiltro = new TableRowSorter(tblalumno.getModel());
        tblalumno.setRowSorter(trsFiltro);
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscar.getText().toUpperCase()));
    }//GEN-LAST:event_txtbuscarKeyTyped
    
    private TableRowSorter trsFiltro;
    @SuppressWarnings("unchecked")
    public void filtro() {
        int columnaABuscar=0;
        if ("Legajo".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar=0;
        }
        if ("Nombre".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar=1;
        }
        if ("Apellido".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar=2;
        }
        if ("DNI".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar=3;
        }
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscar.getText(), columnaABuscar));
    }
    
    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        tblalumno.getTableHeader().setReorderingAllowed(false);
        tblcurso.getTableHeader().setReorderingAllowed(false);
        tblcuotaIns.getTableHeader().setReorderingAllowed(false);
        mostrarPersona();
        bloquearFunciones();
        dtepago.setDate(date);
    }//GEN-LAST:event_formInternalFrameOpened

    private void tblalumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblalumnoMouseClicked
        // TODO add your handling code here:
        int fila=tblalumno.rowAtPoint(evt.getPoint());
        cleanSeleccion();
        legajo=tblalumno.getValueAt(fila, 0).toString();
        nombre=tblalumno.getValueAt(fila, 1).toString();
        apellido=tblalumno.getValueAt(fila, 2).toString();
        dni=Integer.parseInt(tblalumno.getValueAt(fila, 3).toString());
        mostrarCursos();
    }//GEN-LAST:event_tblalumnoMouseClicked

    private void tblcursoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcursoMouseClicked
        // TODO add your handling code here:
        int fila=tblcurso.rowAtPoint(evt.getPoint());
        curso=tblcurso.getValueAt(fila, 0).toString();
        añoinsc=Integer.parseInt(tblcurso.getValueAt(fila, 1).toString());
        cursando=tblcurso.getValueAt(fila, 2).toString();
        estado=tblcurso.getValueAt(fila, 3).toString();
        deudains=Float.parseFloat(tblcurso.getValueAt(fila, 4).toString());
        txtdeudainsc.setText("$ ".concat(String.valueOf(deudains)));
        variabledeuda=deudains;
        mostrarEstado();
        habilitarFunciones();
        var=false;
    }//GEN-LAST:event_tblcursoMouseClicked

    private void PagarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarItemActionPerformed
        // TODO add your handling code here:
        abmPagar pa=new abmPagar();
        if(dni!=0){
            pa.txtbuscar.setText(String.valueOf(dni));
        }
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pa.getSize();
        pa.setLocation((desktopSize.width-FrameSize.width)/16,(desktopSize.height-FrameSize.height)/2);
        panelA.add(pa);
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pa.show();
    }//GEN-LAST:event_PagarItemActionPerformed

    private void BorrarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarItemActionPerformed
        // TODO add your handling code here:
        if(idca!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de borrar este curso ".concat(curso).concat("?"));
            switch(op){
                case 0:
                    try{
                        clase.Estado.borrarCursoAlumno(cnx, idca);
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    borrarcurso();
                    mostrarCursos();
                    tblalumno.clearSelection();
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
    }//GEN-LAST:event_BorrarItemActionPerformed

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        btnCancelar();
        bloqCanc();
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btnpagarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnpagarActionPerformed
        // TODO add your handling code here:
        if((dtepago.getDate()!=null)&&(!txtimporte.getText().isEmpty())&&(chboxdeuda.isSelected()==false)){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de pagar la cuota de inscripcion?");
            switch(op){
                case 0:
                    pagarIns();
                    break;
                case 1:
                    btncancelar.requestFocus(true);
                    break;
                default:
                    btnCancelar();
                    bloqCanc();
                    break;
            }
        }
        else if((dtepago.getDate()!=null)&&(!txtimporte.getText().isEmpty())&&(chboxdeuda.isSelected()==true)){
            int op1=JOptionPane.showConfirmDialog(this, "Esta seguro de pagar la deuda de inscripcion?");
            switch(op1){
                case 0:
                    pagarDeu();
                    break;
                case 1:
                    btncancelar.requestFocus(true);
                    break;
                default:
                    btnCancelar();
                    bloqCanc();
                    break;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "Complete los campos:\nFECHA DE PAGO\nIMPORTE","WARNING",WARNING_MESSAGE);
            txtimporte.requestFocus(true);
        }
    }//GEN-LAST:event_btnpagarActionPerformed

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

    private void txtimporteFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtimporteFocusGained
        // TODO add your handling code here:
        txtcambio.setText(" ");
    }//GEN-LAST:event_txtimporteFocusGained

    private void EditarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EditarItemActionPerformed
        // TODO add your handling code here:
        abmRegistros corregir=new abmRegistros();
        if(dni!=0){
            corregir.txtbuscar.setText(String.valueOf(dni));
        }
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
        if(idca!=0){
            generarComprobante();
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_CompItemActionPerformed

    private void CompInsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CompInsItemActionPerformed
        // TODO add your handling code here:
        if(var==true){
            generarComprobante();
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ninguna cuota","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_CompInsItemActionPerformed

    private void tblcuotaInsMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcuotaInsMouseClicked
        // TODO add your handling code here:
        int fila=tblcuotaIns.rowAtPoint(evt.getPoint());
        precio=Float.parseFloat(tblcuotaIns.getValueAt(fila, 2).toString());
        estado=tblcuotaIns.getValueAt(fila, 3).toString();
        importe=Float.parseFloat(tblcuotaIns.getValueAt(fila, 4).toString());
        fechapago=(Date) (tblcuotaIns.getValueAt(fila, 5));
        fechapins=String.valueOf(fechapago);
        buscarIDPrecio();
        var=true;
    }//GEN-LAST:event_tblcuotaInsMouseClicked

    private void BorrarPagoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BorrarPagoItemActionPerformed
        // TODO add your handling code here:
        if(var==true){
            try{
                int op=JOptionPane.showConfirmDialog(this, "Esta seguro de borrar el pago de la cuota?");
                switch(op){
                    case 0:
                        if(estado.equalsIgnoreCase("PAGADO")){
                            if(importe<precio){
                                txtcambio.setText("$ ".concat(String.valueOf(importe)));
                                clase.Estado.deshacerPago(cnx, "IMPAGA", idpi);
                                deudains=clase.Estado.buscarDeuda(cnx, idca);
                                variabledeuda=precio-importe;
                                resto=deudains-variabledeuda;
                                clase.Estado.actualizarDeuda(cnx, resto, idca);
                                actualizarDeuda();
                                mostrarCursos();
                                mostrarEstado();
                            }
                            else{
                                txtcambio.setText("$ ".concat(String.valueOf(precio)));
                                clase.Estado.deshacerPago(cnx, "IMPAGA", idpi);
                                actualizarDeuda();
                                mostrarCursos();
                                mostrarEstado();
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "La cuota no esta pagada");
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
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningua cuota","ERROR",ERROR_MESSAGE);
        } 
    }//GEN-LAST:event_BorrarPagoItemActionPerformed

    private void OcultarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OcultarItemActionPerformed
        // TODO add your handling code here:
        if(idca!=0){
            try {
                int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea ocultar el curso ".concat(curso).concat("?"));
                switch(op){
                    case 0:
                        clase.Estado.ocultarCuota(cnx, idca);
                        borrarcurso();
                        mostrarCursos();
                        break;
                    case 1:
                        btncerrar.requestFocus(true);
                        break;
                    default:
                        btncerrar.requestFocus(true);
                        break;
                }
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_OcultarItemActionPerformed

    private void RecuperarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecuperarItemActionPerformed
        // TODO add your handling code here:
        if(idalu!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea recuperar el o los cursos?");
            switch(op){
                case 0:
                    try {
                        clase.Estado.recuperarCursos(cnx, idalu);
                    } 
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    borrarcurso();
                    mostrarCursos();
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

    private void MostrarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MostrarItemActionPerformed
        // TODO add your handling code here:
        if(idalu!=0){
            try {
                int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea mostrar los cursos ocultos?");
                switch(op){
                    case 0:
                        clase.Estado.mostrarCuotas(cnx, idalu);
                        borrarcurso();
                        mostrarCursos();
                        break;
                    case 1:
                        btncerrar.requestFocus(true);
                        break;
                    default:
                        btncerrar.requestFocus(true);
                        break;
                }
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono a ningun alumno","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_MostrarItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem BorrarItem;
    private javax.swing.JMenuItem BorrarPagoItem;
    private javax.swing.JMenuItem CompInsItem;
    private javax.swing.JMenuItem CompItem;
    private javax.swing.JMenuItem EditarItem;
    private javax.swing.JMenuItem MostrarItem;
    private javax.swing.JMenuItem OcultarItem;
    private javax.swing.JMenuItem PagarItem;
    private javax.swing.JPopupMenu Popudat;
    private javax.swing.JPopupMenu Popupalu;
    private javax.swing.JPopupMenu Popupcur;
    private javax.swing.JMenuItem RecuperarItem;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btncerrar;
    private javax.swing.JButton btnpagar;
    private javax.swing.JComboBox<String> cboxpor;
    private javax.swing.JCheckBox chboxdeuda;
    private com.toedter.calendar.JDateChooser dtepago;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JLabel lblbuscar;
    private javax.swing.JLabel lblcambio;
    private javax.swing.JLabel lblfechaPago;
    private javax.swing.JLabel lblicono;
    private javax.swing.JLabel lblimporte;
    private javax.swing.JTable tblalumno;
    private javax.swing.JTable tblcuotaIns;
    private javax.swing.JTable tblcurso;
    public javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcambio;
    private javax.swing.JTextField txtdeudainsc;
    private javax.swing.JTextField txtimporte;
    // End of variables declaration//GEN-END:variables
}
