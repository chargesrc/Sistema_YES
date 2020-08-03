/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
import static interfaces.abmPrincipal.panelA;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.RowFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
/**
 *
 * @author joni
 */
public class abmAlumno extends javax.swing.JInternalFrame {
    Connection conex=conexion.Conexion.conectar();
    ResultSet rs;
    Object[] datos=new Object[5];
    DefaultTableModel modelo=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    
    int dnialumno,persona,legajo,idpersona;
    int dni,valdni,idalumno;
    boolean borrado;
    public int cboxitem=0;
    String rutaSistema,leg,valleg,var;
    
    InputStream binary;
    /**
     * Creates new form RegistrarAlumno
     */
    public abmAlumno() {
        initComponents();
    }
    /*Plugis de java Instalados y activados por defecto 41 Plugins
    Hudson UI, Subversion, Spellchecker English Dictionaries, Hudson, Git, CSS Source Model, Docker Editor, Local History,
    Local Tasks, Bugzilla, Spellchecker, IDE Platform, Mercurial, Database, IDE Branding, Docker UI, Embedded Browser UI - JavaFX...(Bloqueado),
    NetBeans Plugin Development, HTML Custom, External Libraries UI, JavaFX 2 Support, JavaFX 2 Scene Builder, Maven, Java, Ant,
    Spring Beans, Java Profiler, Hibernate,Java Persistence, GUI Builder, Java Debugger, JUnit(Bloqueado), Intent API (Bloqueado),
    Editor Identation Support (Bloqueado), TestNG (Bloqueado), Notifications, RPC Plataform (Bloqueado), Service Registry, Ant(Tools),
    CSS Preprocessors, XML Entity Catalog UI;
    Plugins de la Comunidad java
    reportwizard, PHP WordPress Blog/CMS, Orchid Swing Components Library, Additional Java hints, Emmet, NBMongo;
    Para Android Developments
    Controls.js Support for Java; Controls.js Support; DukeScrip Project Wizard; Gluon Plugin (Dependences: Gradle Support);
    Kotlin (Android); CodenameOnePlugin
    Plugins instalado manualmente
    iReport: jasperreports-extensions, jasperreports-components, jasperserver-plugin, ireport-designer
    */
    public void mostrarPersona(){
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        modelo.addColumn("LEGAJO");
        modelo.addColumn("NOMBRE");
        modelo.addColumn("APELLIDO");
        modelo.addColumn("DNI");
        modelo.addColumn("Fecha Nacimiento");
        try {
            tableAlumno.setModel(modelo);
            rs=clase.Alumno.MostrarPersona(conex);
            while(rs.next()){
                datos[0]=rs.getString("legajo");
                datos[1]=rs.getString("nombre");
                datos[2]=rs.getString("apellido");
                datos[3]=rs.getInt("dni");
                datos[4]=rs.getDate("fechaNacimiento");
                modelo.addRow(datos);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarAlumno(){
        try {
            dnialumno=Integer.parseInt(dniTxt.getText());
            idpersona=clase.Alumno.leerIdPersona(conex,dnialumno);
            rs=clase.Alumno.MostrarAlumno(conex, idpersona);
            while(rs.next()){
                legajoTxt.setText(rs.getString(1));
                tutorTxt.setText(rs.getString(2));
                cotutorTxt.setText(rs.getString(3));
                celularTxt.setText(rs.getString(4));
                direccionTxt.setText(rs.getString(5));
                rutaTxt.setText(rs.getString(6));
                binary=rs.getBinaryStream(7);
            }
            rutaSistema=rutaTxt.getText();//Para evitar estar guardando la imagen cada vez que se edita con la misma imagen
            //////////////Cargar imagen desde la Base de Datos /////////////////
            if(!rutaTxt.getText().isEmpty()){
                ByteArrayOutputStream ouput=new ByteArrayOutputStream();
                int temp=binary.read();
                while(temp>=0){
                    ouput.write((char)temp);
                    temp=binary.read();
                }
                Image imagen=Toolkit.getDefaultToolkit().createImage(ouput.toByteArray());
                imagen=imagen.getScaledInstance(160, 125, Image.SCALE_DEFAULT);
                lbImagen.setIcon(new ImageIcon(imagen));
            }
            else{
                recuperarPerfil();
            }
            //ESTE OTRO METODO REQUIERE DE LA "RUTA ABSOLUTA" DE LA IMAGEN QUE VIENE DESDE LA BASE DE DATOS. NO LA USAMOS PORQUE ES MEJOR CARGARLA DESDE LA BASE DE DATOS
//            Image foto=getToolkit().createImage(rutaTxt.getText());
//            foto=foto.getScaledInstance(160,160,Image.SCALE_DEFAULT);
//            lbImagen.setIcon(new ImageIcon(foto));
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void directorioImagen(){
        JFileChooser file=new JFileChooser();
        file.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("Formato de Archivos JPEG(*.JPEG;*.PNG;*.JPG)","jpeg","png","jpg");
        file.addChoosableFileFilter(filtro);
        file.setFileFilter(filtro);
        //file.setCurrentDirectory("Direccion");Definimos el directorio por defecto cuando se abra la ventana para buscar el archivo
        int vent=file.showOpenDialog(this);
        if(vent==JFileChooser.APPROVE_OPTION){
            File archivo=file.getSelectedFile();
            String Orig=archivo.getPath();
            Path Origen=Paths.get(Orig);
//            String paths=System.getProperty("user.dir")+"Imagenes";//Cuando elegimos esta ruta nos dirige a la ruta del proyecto donde se ejecuta el programa
            File carp=new File("C:\\Users\\Public\\Pictures\\sistemaYES\\perfiles");//Usamos esta ruta porque es la crea el sistema operativo como publicas ademas de no tener el "nombre de usuario" nos permite "leer" y "escribir" sin permisos de administrador
            if(!carp.exists()){//Usamos mkdirs() en lugar de mkdir() porque crearemos dos carpetas "sistemaYES" y "perfiles". Ademas de que mkdirs() creara una sola carpeta si es que una de las carpetas ya esta creada
                carp.mkdirs();//funcion que nos permite crear la carpeta.Creara el directorio si no existe.Tambien es posible crear varias carpetas a la vez con .mkdirs()
            }
            String Dest=("C:/Users/Public/Pictures/sistemaYES/perfiles/")+archivo.getName();
            Path Destino=Paths.get(Dest);
            rutaTxt.setText(String.valueOf(Destino));
            try{
                Files.copy(Origen, Destino, REPLACE_EXISTING);//Copiamos el nuevo archivo con la clase Files, reemplazamos si ya existe
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
            Image foto=getToolkit().createImage(archivo.toString());
            foto=foto.getScaledInstance(160,125,Image.SCALE_DEFAULT);
            lbImagen.setIcon(new ImageIcon(foto));
        }
    }
    
    public void proyectoImagen(){//Requerira de permisos de administrador una vez instalado en C:\Programs Files....
        JFileChooser archivo=new JFileChooser();
        archivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//Indicamos que queremos buscar en carpetas y archivos. Se pueden "archivos" y "directorios" por separado
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("Formato de Archivos JPEG(*.JPEG;*.PNG;*.JPG)","jpeg","png","jpg");
        archivo.addChoosableFileFilter(filtro);//Filtro de las extensiones de imagenes que queremos buscar
        archivo.setFileFilter(filtro);//Dejamos por default nuestro filtro.En caso contrario quedara en "Todos los archivos"
//        archivo.setDialogTitle("Abrir achivo");//Titulo de la ventana  que se abrira. Si no se pone nada, carga por default "abrir"
        int ventana=archivo.showOpenDialog(this);
        if(ventana==JFileChooser.APPROVE_OPTION){
            File file=archivo.getSelectedFile();//Sirve para comprobar si se selecciono un archivo. Devuelve "null" si no se selecciono nada.Tambien podemos obtener la ruta del archivo
            String Orig=file.getPath();//Otro modo de obtener la ruta de la imagen
            Path Origen=Paths.get(Orig);//Ruta de origen de la imagen
            String Dest=System.getProperty("user.dir")+"/src/perfiles/"+file.getName();//Direccion donde se guardara la imagen.Podria haber problema si no se tiene derechos de "escribir" o "leer" una vez instalado el programa en C:\Program Files...
            Path Destino=Paths.get(Dest);//Destino de la copia de la imagen
            rutaTxt.setText(String.valueOf(Destino));//Carga la ruta de la imagen (Destino) en el cuadro de texto que se guardara en la Base de Datos
            try{
                Files.copy(Origen, Destino, REPLACE_EXISTING);//Clase que realizara el procedimiento de la copia de la imagen. Reemplazara si la imagen ya existe
            }
            catch(IOException e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
            //rutaTxt.setText(String.valueOf(file.getName()));//Ruta para el usuario. Solo muestra el nombre de la imagen
            //rutaSistema=file.getAbsolutePath();//Capturamos la "direccion absoluta" del archivo imagen
            //ESTE MISMO METODO PUEDE SER USADO PARA MOSTRAR LA IMAGEN AL LABEL. SE DEBE TENER EN CUENTA QUE LA RUTA DE LA IMAGEN SEA LA "RUTA ABSOLUTA"
            Image foto=getToolkit().createImage(file.toString());//Mapea y crea la imagen en el objeto foto
            foto= foto.getScaledInstance(160,125,Image.SCALE_DEFAULT);//Redimensionamos la imagen. De preferencia usamos la escala del label que la contendra
            lbImagen.setIcon(new ImageIcon(foto));
        }
    }
    
    public void recuperarPerfil(){
        Image perfil=getToolkit().getImage("src/imagenes/usuario.png");
        perfil=perfil.getScaledInstance(100, 100, Image.SCALE_DEFAULT);
        lbImagen.setIcon(new ImageIcon(perfil));
    }
    
    public void traerDatos(){
        try{
            rs=clase.Alumno.traerDatosalu(conex, idpersona);
            while(rs.next()){
                dni=rs.getInt(1);
                leg=rs.getString(2);
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void editarAlu(){
        try{
            dnialumno=Integer.parseInt(dniTxt.getText());
            clase.Alumno.editarPersona(conex,dniTxt.getText(),apellidoTxt.getText().toUpperCase(),nombreTxt.getText().toUpperCase(), fechaNacimientoDate.getDate(),idpersona);
            if(rutaTxt.getText().isEmpty()||rutaTxt.getText().equals(rutaSistema)){
                clase.Alumno.editarAlumnosinFoto(conex, legajoTxt.getText().toUpperCase(), tutorTxt.getText().toUpperCase(), cotutorTxt.getText().toUpperCase(), celularTxt.getText(),direccionTxt.getText().toUpperCase(), idpersona);
                mostrarPersona();
                limpiar();
                limpiarbotones();
                limpiarespecial();
                ultimoLegajo();
            }
            else{
                if(rutaTxt.getText().length()<=500&&!rutaTxt.getText().equals(rutaSistema)){
                    FileInputStream archivoFoto;
                    archivoFoto = new FileInputStream(rutaTxt.getText());
                    clase.Alumno.editarAlumno(conex, legajoTxt.getText().toUpperCase(),tutorTxt.getText().toUpperCase(), cotutorTxt.getText().toUpperCase(),celularTxt.getText(), direccionTxt.getText().toUpperCase(),rutaTxt.getText(),archivoFoto,idpersona);
                    mostrarPersona();
                    limpiar();
                    limpiarbotones();
                    limpiarespecial();
                    ultimoLegajo();
                }
                else{
                    JOptionPane.showMessageDialog(this,"La ruta o el nombre del archivo es demasiado largo","ERROR",ERROR_MESSAGE);
                }
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void ultimoLegajo(){
        try{
            idalumno=clase.Alumno.leerUltimoIdAlumno(conex);
            if(idalumno!=0){
                var=clase.Alumno.capturarLegajo(conex, idalumno);
                txtultimolgo.setText(var);
            }
            else{
                txtultimolgo.setText("");
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
        
    public void limpiar(){
        dniTxt.setText("");
        nombreTxt.setText("");
        apellidoTxt.setText("");
        legajoTxt.setText("");
        direccionTxt.setText("");
        celularTxt.setText("");
        tutorTxt.setText("");
        cotutorTxt.setText("");
        fechaNacimientoDate.setDate(null);
        rutaTxt.setText("");
        recuperarPerfil();
        tableAlumno.clearSelection();
    }
    
    public void limpiarbotones(){
        editarBtn.setEnabled(false);
        guardarBtn.setEnabled(true);
        borrarBtn.setEnabled(false);
        cancelarBtn.setEnabled(false);
    }
    
    public void limpiarbtnmc(){
        guardarBtn.setEnabled(false);
        editarBtn.setEnabled(true);
        cancelarBtn.setEnabled(true);
        borrarBtn.setEnabled(true);
    }
    
    public void limpiarespecial(){
        cmbOpciones.setSelectedIndex(0);
        nombreTxt.requestFocus(true);
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
        InscribirItem = new javax.swing.JMenuItem();
        PagarInsItem = new javax.swing.JMenuItem();
        PagosItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblbuscar = new javax.swing.JLabel();
        lblalumnos = new javax.swing.JLabel();
        cmbOpciones = new javax.swing.JComboBox<>();
        buscarTxt = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableAlumno = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        lbltituloimg = new javax.swing.JLabel();
        lbImagen = new javax.swing.JLabel();
        rutaTxt = new javax.swing.JTextField();
        seleccionarBtn = new javax.swing.JButton();
        cerrarBtn = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblnombre = new javax.swing.JLabel();
        lbllegajo = new javax.swing.JLabel();
        lblapellido = new javax.swing.JLabel();
        lbltutor = new javax.swing.JLabel();
        lbldni = new javax.swing.JLabel();
        lblcotutor = new javax.swing.JLabel();
        lblfechanac = new javax.swing.JLabel();
        lblcecular = new javax.swing.JLabel();
        lbldireccion = new javax.swing.JLabel();
        nombreTxt = new javax.swing.JTextField();
        legajoTxt = new javax.swing.JTextField();
        apellidoTxt = new javax.swing.JTextField();
        tutorTxt = new javax.swing.JTextField();
        dniTxt = new javax.swing.JTextField();
        cotutorTxt = new javax.swing.JTextField();
        fechaNacimientoDate = new com.toedter.calendar.JDateChooser();
        celularTxt = new javax.swing.JTextField();
        direccionTxt = new javax.swing.JTextField();
        guardarBtn = new javax.swing.JButton();
        editarBtn = new javax.swing.JButton();
        borrarBtn = new javax.swing.JButton();
        cancelarBtn = new javax.swing.JButton();
        lblultimolgo = new javax.swing.JLabel();
        txtultimolgo = new javax.swing.JTextField();

        InscribirItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/credit_card.png"))); // NOI18N
        InscribirItem.setText("Inscribir Alumno");
        InscribirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InscribirItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(InscribirItem);

        PagarInsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money_dollar.png"))); // NOI18N
        PagarInsItem.setText("Pagar Inscripcion");
        PagarInsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarInsItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(PagarInsItem);

        PagosItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money.png"))); // NOI18N
        PagosItem.setText("Ir a Pagar");
        PagosItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagosItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(PagosItem);

        setTitle("Formulario de Alumnos");
        setPreferredSize(new java.awt.Dimension(900, 490));
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
        lblbuscar.setText("Buscar Alumno");

        lblalumnos.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        lblalumnos.setText("Alumnos");

        cmbOpciones.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LEGAJO", "DNI", "NOMBRE", "APELLIDO" }));
        cmbOpciones.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbOpcionesItemStateChanged(evt);
            }
        });

        buscarTxt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                buscarTxtFocusGained(evt);
            }
        });
        buscarTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                buscarTxtKeyTyped(evt);
            }
        });

        tableAlumno.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tableAlumno.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Legajo", "Nombre", "Apellido", "DNI", "Fecha de Nacimiento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class, java.lang.Object.class
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
        tableAlumno.setComponentPopupMenu(jPopupMenu1);
        tableAlumno.getTableHeader().setReorderingAllowed(false);
        tableAlumno.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableAlumnoMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableAlumno);
        if (tableAlumno.getColumnModel().getColumnCount() > 0) {
            tableAlumno.getColumnModel().getColumn(0).setResizable(false);
            tableAlumno.getColumnModel().getColumn(1).setResizable(false);
            tableAlumno.getColumnModel().getColumn(2).setResizable(false);
            tableAlumno.getColumnModel().getColumn(3).setResizable(false);
            tableAlumno.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 552, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(lblbuscar)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cmbOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buscarTxt)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblalumnos)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblbuscar)
                    .addComponent(buscarTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbOpciones, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblalumnos))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        lbltituloimg.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbltituloimg.setText("PERFIL DEL ALUMNO");

        lbImagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbImagen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/usuario.png"))); // NOI18N

        seleccionarBtn.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        seleccionarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/foto.png"))); // NOI18N
        seleccionarBtn.setText("Seleccionar Imagen");
        seleccionarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                seleccionarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbltituloimg, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(rutaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(seleccionarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 141, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lbImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(6, 6, 6)
                .addComponent(lbltituloimg)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lbImagen, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seleccionarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(rutaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        cerrarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        cerrarBtn.setText("Cerrar");
        cerrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarBtnActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Datos del Alumno", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        lblnombre.setText("Nombre:");

        lbllegajo.setText("Legajo:");

        lblapellido.setText("Apellido:");

        lbltutor.setText("Tutor:");

        lbldni.setText("DNI:");

        lblcotutor.setText("Cotutor:");

        lblfechanac.setText("Fecha Nacimiento:");

        lblcecular.setText("Celular:");

        lbldireccion.setText("Direccion:");

        nombreTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                nombreTxtKeyTyped(evt);
            }
        });

        legajoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                legajoTxtKeyTyped(evt);
            }
        });

        apellidoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                apellidoTxtKeyTyped(evt);
            }
        });

        tutorTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                tutorTxtKeyTyped(evt);
            }
        });

        dniTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dniTxtKeyTyped(evt);
            }
        });

        cotutorTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cotutorTxtKeyTyped(evt);
            }
        });

        celularTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                celularTxtKeyTyped(evt);
            }
        });

        direccionTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                direccionTxtKeyTyped(evt);
            }
        });

        guardarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save_as.png"))); // NOI18N
        guardarBtn.setText("Guardar");
        guardarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnActionPerformed(evt);
            }
        });

        editarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/options_2.png"))); // NOI18N
        editarBtn.setText("Editar");
        editarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarBtnActionPerformed(evt);
            }
        });

        borrarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/eraser.png"))); // NOI18N
        borrarBtn.setText("Borrar");
        borrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarBtnActionPerformed(evt);
            }
        });

        cancelarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help_ring-buoy.png"))); // NOI18N
        cancelarBtn.setText("Cancelar");
        cancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblfechanac, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                .addComponent(lbldni, javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(lblapellido, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblnombre, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(lbldireccion))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(50, 50, 50))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(apellidoTxt, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(dniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(fechaNacimientoDate, javax.swing.GroupLayout.PREFERRED_SIZE, 173, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblcecular)
                                    .addComponent(lblcotutor))
                                .addGap(14, 14, 14)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cotutorTxt)
                                    .addComponent(celularTxt)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lbltutor)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(lbllegajo, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(tutorTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 173, Short.MAX_VALUE)
                                    .addComponent(legajoTxt)))))
                    .addComponent(direccionTxt))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(guardarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(borrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(guardarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(editarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(borrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(lblnombre)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(legajoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbllegajo)
                                .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(11, 11, 11)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(tutorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbltutor, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(apellidoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblapellido)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(45, 45, 45)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(lblcecular)
                                    .addComponent(celularTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(cotutorTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(dniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbldni)
                                        .addComponent(lblcotutor)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(fechaNacimientoDate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblfechanac))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(direccionTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbldireccion))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblultimolgo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblultimolgo.setText("Ultimo Legajo");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cerrarBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                            .addComponent(lblultimolgo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(txtultimolgo))))
                .addGap(29, 29, 29))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblultimolgo)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtultimolgo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(cerrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void seleccionarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_seleccionarBtnActionPerformed
        directorioImagen();
    }//GEN-LAST:event_seleccionarBtnActionPerformed

    private void InscribirItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InscribirItemActionPerformed
        abmInscripcion registrar=new abmInscripcion();
        registrar.legajoTxt.setText(legajoTxt.getText());
        registrar.dniTxt.setText(dniTxt.getText());
        registrar.nombreTxt.setText(nombreTxt.getText());
        registrar.apellidoTxt.setText(apellidoTxt.getText());
        this.dispose();
        panelA.add(registrar);
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=registrar.getSize();
        registrar.setLocation((desktopSize.width-FrameSize.width)/50,(desktopSize.height-FrameSize.height)/10);
        Animacion.Animacion.mover_derecha(0,110, 5, 5, panelA);
        registrar.show();
        registrar.cursoTxt.requestFocus(true);
    }//GEN-LAST:event_InscribirItemActionPerformed

    private void PagosItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagosItemActionPerformed
        abmPagar pa=new abmPagar();
        pa.txtbuscar.setText(dniTxt.getText());
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pa.getSize();
        pa.setLocation((desktopSize.width-FrameSize.width)/16,(desktopSize.height-FrameSize.height)/2);
        this.dispose();
        panelA.add(pa);
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pa.show();
    }//GEN-LAST:event_PagosItemActionPerformed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        tableAlumno.getTableHeader().setReorderingAllowed(false);
        rutaTxt.setEditable(false);
        txtultimolgo.setEditable(false);
        mostrarPersona();
        limpiarbotones();
        ultimoLegajo();
    }//GEN-LAST:event_formInternalFrameOpened

    private void cerrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cerrarBtnActionPerformed

    @SuppressWarnings("unchecked")
    private void buscarTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_buscarTxtKeyTyped
        int c=evt.getKeyChar();
        switch (cboxitem) {
            case 0:
                if(buscarTxt.getText().length()>=12){
                    evt.consume();
                }   if(!Character.isLetterOrDigit(c)){
                    evt.consume();
                }   break;
            case 1:
                if(buscarTxt.getText().length()>=8){
                    evt.consume();
                }   if(!Character.isDigit(c)){
                    evt.consume();
                }   break;
            case 2:
                if(buscarTxt.getText().length()>=16){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)) {
                    evt.consume();
                }   break;
            default:
                if(buscarTxt.getText().length()>=20){
                    evt.consume();
                }   if(!Character.isAlphabetic(c)){
                    evt.consume();
                }   break;
        }
        buscarTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(final KeyEvent e) {
                String cadena = (buscarTxt.getText().toUpperCase());
                buscarTxt.setText(cadena);
                repaint();
                filtro();
            }
        });
        trsFiltro = new TableRowSorter(tableAlumno.getModel());
        tableAlumno.setRowSorter(trsFiltro);
        trsFiltro.setRowFilter(RowFilter.regexFilter(buscarTxt.getText().toUpperCase()));
    }//GEN-LAST:event_buscarTxtKeyTyped

    private TableRowSorter trsFiltro;
    @SuppressWarnings("unchecked")
    public void filtro() {
        int columnaABuscar = 0;
        if ("Legajo".equalsIgnoreCase(cmbOpciones.getSelectedItem().toString())) {
            columnaABuscar = 0;
        }
        if ("Nombre".equalsIgnoreCase(cmbOpciones.getSelectedItem().toString())) {
            columnaABuscar = 1;
        }
        if ("Apellido".equalsIgnoreCase(cmbOpciones.getSelectedItem().toString())) {
            columnaABuscar = 2;
        }
        if ("DNI".equalsIgnoreCase(cmbOpciones.getSelectedItem().toString())) {
            columnaABuscar = 3;
        }
        trsFiltro.setRowFilter(RowFilter.regexFilter(buscarTxt.getText(), columnaABuscar));
    }
    
    private void buscarTxtFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_buscarTxtFocusGained
        limpiar();
        limpiarbotones();
    }//GEN-LAST:event_buscarTxtFocusGained

    private void tableAlumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableAlumnoMouseClicked
        int fila=tableAlumno.rowAtPoint(evt.getPoint());
//        legajoTxt.setText(tableAlumno.getValueAt(fila,0).toString());
        nombreTxt.setText(tableAlumno.getValueAt(fila,1).toString());
        apellidoTxt.setText(tableAlumno.getValueAt(fila,2).toString());
        dniTxt.setText(tableAlumno.getValueAt(fila,3).toString());
        fechaNacimientoDate.setDate((Date)tableAlumno.getValueAt(fila,4));
        mostrarAlumno();
        limpiarbtnmc();
        traerDatos();
    }//GEN-LAST:event_tableAlumnoMouseClicked

    private void nombreTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_nombreTxtKeyTyped
        char c=evt.getKeyChar();
        if(nombreTxt.getText().length()>=45){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_nombreTxtKeyTyped

    private void dniTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dniTxtKeyTyped
        int c=evt.getKeyChar();
        if(dniTxt.getText().length()>=8){
            evt.consume();
        }
        if (!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_dniTxtKeyTyped

    private void apellidoTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_apellidoTxtKeyTyped
        char c=evt.getKeyChar();
        if(apellidoTxt.getText().length()>=40){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_apellidoTxtKeyTyped

    private void celularTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_celularTxtKeyTyped
        int c=evt.getKeyChar();
        if(celularTxt.getText().length()>=15){
            evt.consume();
        }
        if (!Character.isDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_celularTxtKeyTyped

    private void cotutorTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cotutorTxtKeyTyped
        char c=evt.getKeyChar();
        if(cotutorTxt.getText().length()>=45){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)&&!Character.isSpaceChar(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_cotutorTxtKeyTyped

    private void legajoTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_legajoTxtKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(legajoTxt.getText().length()>=20){
            evt.consume();
        }
        if(!Character.isLetterOrDigit(c)){
            evt.consume();
        }
    }//GEN-LAST:event_legajoTxtKeyTyped

    private void tutorTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tutorTxtKeyTyped
        char c=evt.getKeyChar();
        if(tutorTxt.getText().length()>=45){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)&&!Character.isSpaceChar(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_tutorTxtKeyTyped

    private void direccionTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_direccionTxtKeyTyped
        // TODO add your handling code here:
        if(direccionTxt.getText().length()>=50){
            evt.consume();
        }
    }//GEN-LAST:event_direccionTxtKeyTyped

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        if((nombreTxt.getText().isEmpty())||(apellidoTxt.getText().isEmpty())||(dniTxt.getText().isEmpty())
            ||(legajoTxt.getText().isEmpty())||(tutorTxt.getText().isEmpty())||(cotutorTxt.getText().isEmpty())
            ||(celularTxt.getText().isEmpty())||(direccionTxt.getText().isEmpty())||(fechaNacimientoDate.getDate()==null)){
            JOptionPane.showMessageDialog(this, "Complete los campos","WARNING",WARNING_MESSAGE);
        }
        else{
            try{
                //validar si la persona ya existe
                persona=clase.Alumno.leerIdPersona(conex, Integer.parseInt(dniTxt.getText()));
                legajo=clase.Alumno.leerIdPersonaLegajo(conex, legajoTxt.getText());
                if(persona==0&&legajo==0){
                    int op=JOptionPane.showConfirmDialog(this, "Estas seguro de registrar al alumno?");
                    switch (op) {
                        case 0:
                            clase.Alumno.insertarPersonas(conex,dniTxt.getText(),apellidoTxt.getText().toUpperCase(),nombreTxt.getText().toUpperCase(), fechaNacimientoDate.getDate());
                            idpersona=clase.Alumno.leerUltimoIdPersona(conex);
                            //validar si contiene foto
                            if(rutaTxt.getText().isEmpty()){
                                clase.Alumno.insertarSinFoto(conex, idpersona, legajoTxt.getText().toUpperCase(),tutorTxt.getText().toUpperCase(),cotutorTxt.getText().toUpperCase(),celularTxt.getText(),direccionTxt.getText().toUpperCase(),false,"CURSANDO");
                                mostrarPersona();
                                limpiar();
                                limpiarespecial();
                                ultimoLegajo();
                            }
                            else{
                                if(rutaTxt.getText().length()<=500){
                                    FileInputStream archivoFoto;
                                    archivoFoto=new FileInputStream(rutaTxt.getText());
                                    clase.Alumno.insertarAlumno(conex, idpersona, legajoTxt.getText().toUpperCase(),tutorTxt.getText().toUpperCase(),cotutorTxt.getText().toUpperCase(),celularTxt.getText(),direccionTxt.getText().toUpperCase(),false,"CURSANDO",rutaTxt.getText(),archivoFoto);
                                    mostrarPersona();
                                    limpiar();
                                    limpiarespecial();
                                    ultimoLegajo();
                                }
                                else{
                                    JOptionPane.showMessageDialog(this,"La ruta o el nombre del archivo es demasiado largo","ERROR",ERROR_MESSAGE);
                                }
                            }
                            break;
                        case 1:
                            nombreTxt.requestFocus(true);
                            break;
                        default:
                            limpiar();
                            limpiarespecial();
                            break;
                    }
                }
                else{
                    borrado=clase.Alumno.leerBorrado(conex, persona);
                    if(borrado==true){
                        int op=JOptionPane.showConfirmDialog(this, "Ya existe un alumno con ese DNI ".concat(dniTxt.getText())+"\nQue talvez fue eliminado"+"\n"+"Desea reinscribir al alumno?");
                        if(op==0){
                            clase.Alumno.AltaAlumno(conex, persona);
                            mostrarPersona();
                            limpiar();
                            limpiarespecial();
                        }
                        if(op==1){
                            nombreTxt.requestFocus(true);
                        }
                        else{
                            limpiar();
                            limpiarbotones();
                            limpiarespecial();
                        }
                    }
                    else{
                        if(persona!=0){
                            JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese DNI ".concat(dniTxt.getText())+"\n Intenta buscarlo!!!");
                            cmbOpciones.setSelectedIndex(0);
                            buscarTxt.requestFocus(true);
                        }
                        else if(legajo!=0){
                            JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese Legajo ".concat(legajoTxt.getText()).concat("\n Buscalo o cambie el legajo!!!"));
//                            JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese Legajo ".concat(legajoTxt.getText())+"\n Intenta buscarlo!!!");
                            cmbOpciones.setSelectedIndex(0);
                            legajoTxt.requestFocus(true);
//                            buscarTxt.requestFocus(true);
                        }
                    }
                }
            }
            catch(Exception ex){
                JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_guardarBtnActionPerformed

    private void editarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBtnActionPerformed
        if((nombreTxt.getText().isEmpty())||(apellidoTxt.getText().isEmpty())||(dniTxt.getText().isEmpty())
            ||(legajoTxt.getText().isEmpty())||(tutorTxt.getText().isEmpty())||(cotutorTxt.getText().isEmpty())
            ||(celularTxt.getText().isEmpty())||(direccionTxt.getText().isEmpty())||(fechaNacimientoDate.getDate()==null)){
            JOptionPane.showMessageDialog(this, "Complete los campos","WARNING",WARNING_MESSAGE);
        }
        else{
            try {
                int op=JOptionPane.showConfirmDialog(this,"Esta seguro de editar los datos?");
                switch (op) {
                    case 0:
                        if((Integer.parseInt(dniTxt.getText())==dni)&&(legajoTxt.getText().equalsIgnoreCase(leg))){
                            editarAlu();
                        }
                        else{
                            persona=clase.Alumno.leerIdPersona(conex, Integer.parseInt(dniTxt.getText()));
                            legajo=clase.Alumno.leerIdPersonaLegajo(conex, legajoTxt.getText());
                            if(persona==0&&legajo==0){
                                editarAlu();
                            }
                            else{
                                if(persona!=0){
                                    valdni=clase.Alumno.dniPersona(conex, persona);
                                    if(valdni==dni){
                                        editarAlu();
                                        break;      
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese dni ".concat(dniTxt.getText()),"WARNING",WARNING_MESSAGE);
                                        dniTxt.requestFocus(true);
                                        break;
                                    }
                                }
                                if(legajo!=0){
                                    valleg=clase.Alumno.legPersona(conex, legajo);
                                    if(valleg.equalsIgnoreCase(leg)){
                                        editarAlu();
                                        break;
                                    }
                                    else{
                                        JOptionPane.showMessageDialog(this, "Ya existe un alumno con ese legajo ".concat(legajoTxt.getText()),"WARNING",WARNING_MESSAGE);
                                        legajoTxt.requestFocus(true);
                                        break;
                                    }
                                }
                            }
                        }
                        break;
                    case 1:
                        nombreTxt.requestFocus(true);
                        break;
                    default:
                        limpiar();
                        limpiarbotones();
                        limpiarespecial();
                        break;
                }
            }
            catch (Exception ex){
                JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_editarBtnActionPerformed

    private void borrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarBtnActionPerformed
        try {
            int op=JOptionPane.showConfirmDialog(this,"Esta seguro de dar de baja al alumno ".concat(nombreTxt.getText()).concat(" ").concat(apellidoTxt.getText()).concat("\nDNI: ").concat(dniTxt.getText()).concat(" ?"));
            switch (op) {
                case 0:
                    clase.Alumno.borrarAlumno(conex, idpersona);
                    mostrarPersona();
                    limpiar();
                    limpiarbotones();
                    limpiarespecial();
                    break;
                case 1:
                    nombreTxt.requestFocus(true);
                    break;
                default:
                    limpiar();
                    limpiarbotones();
                    limpiarespecial();
                    break;
            }
        }
        catch(Exception ex){
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_borrarBtnActionPerformed

    private void cancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarBtnActionPerformed
        limpiar();
        limpiarbotones();
        limpiarespecial();
    }//GEN-LAST:event_cancelarBtnActionPerformed

    private void cmbOpcionesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbOpcionesItemStateChanged
        // TODO add your handling code here:
        cboxitem=cmbOpciones.getSelectedIndex();
        buscarTxt.requestFocus(true);
    }//GEN-LAST:event_cmbOpcionesItemStateChanged

    private void PagarInsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarInsItemActionPerformed
        // TODO add your handling code here:
        abmEstado estado=new abmEstado();
        estado.txtbuscar.setText(dniTxt.getText());
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=estado.getSize();
        estado.setLocation((desktopSize.width-FrameSize.width)/27,(desktopSize.height-FrameSize.height)/15);
        panelA.add(estado);
        Animacion.Animacion.mover_derecha(0, 110, 5, 5, panelA);
        estado.show();
        estado.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_PagarInsItemActionPerformed
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem InscribirItem;
    private javax.swing.JMenuItem PagarInsItem;
    private javax.swing.JMenuItem PagosItem;
    public javax.swing.JTextField apellidoTxt;
    private javax.swing.JButton borrarBtn;
    public javax.swing.JTextField buscarTxt;
    private javax.swing.JButton cancelarBtn;
    private javax.swing.JTextField celularTxt;
    private javax.swing.JButton cerrarBtn;
    private javax.swing.JComboBox<String> cmbOpciones;
    private javax.swing.JTextField cotutorTxt;
    private javax.swing.JTextField direccionTxt;
    public javax.swing.JTextField dniTxt;
    private javax.swing.JButton editarBtn;
    private com.toedter.calendar.JDateChooser fechaNacimientoDate;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbImagen;
    private javax.swing.JLabel lblalumnos;
    private javax.swing.JLabel lblapellido;
    private javax.swing.JLabel lblbuscar;
    private javax.swing.JLabel lblcecular;
    private javax.swing.JLabel lblcotutor;
    private javax.swing.JLabel lbldireccion;
    private javax.swing.JLabel lbldni;
    private javax.swing.JLabel lblfechanac;
    private javax.swing.JLabel lbllegajo;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lbltituloimg;
    private javax.swing.JLabel lbltutor;
    private javax.swing.JLabel lblultimolgo;
    private javax.swing.JTextField legajoTxt;
    public javax.swing.JTextField nombreTxt;
    private javax.swing.JTextField rutaTxt;
    private javax.swing.JButton seleccionarBtn;
    private javax.swing.JTable tableAlumno;
    private javax.swing.JTextField tutorTxt;
    private javax.swing.JTextField txtultimolgo;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmAlumno.class.getName());
}
