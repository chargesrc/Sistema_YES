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
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.RowFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;

/**
 *
 * @author cytrex
 */
public class abmRegistros extends javax.swing.JInternalFrame {
    Connection conex=conexion.Conexion.conectar();
    ResultSet rs,rsp;
    Object[] datos=new Object[4];
    Object[] datosuno=new Object[4];
    DefaultTableModel modelo=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    DefaultTableModel modelouno=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    DefaultListModel modelotres=new DefaultListModel();
    
    int dni,añoinsc,idcurso,idalumno,idca;
    String horarioSistema,academicoSistema,nombre,apellido,academico,añocur;
    public int cboxitem=0;
    boolean est=false;
    /**
     * Creates new form abmCorreccion
     */
    public abmRegistros() {
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
            rs=clase.Registros.mostrarPersona(conex);
            while(rs.next()){
                datos[0]=rs.getString("legajo");
                datos[1]=rs.getString("nombre");
                datos[2]=rs.getString("apellido");
                datos[3]=rs.getInt("dni");
                modelo.addRow(datos);
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarCursos(){
        modelouno.setColumnCount(0);
        modelouno.setRowCount(0);
        modelouno.addColumn("CURSO");
        modelouno.addColumn("AÑO INSC.");
        modelouno.addColumn("CURSANDO");
        modelouno.addColumn("HORARIO");
        try{
            tblcurso.setModel(modelouno);
            idalumno=clase.Registros.leerIdAlumno(conex, dni);
            rs=clase.Registros.mostrarCursos(conex,idalumno);
            while(rs.next()){
                datosuno[0]=rs.getString("nombre");
                datosuno[1]=rs.getInt("añoLectivo");
                datosuno[2]=rs.getString("añoCurso");
                datosuno[3]=rs.getString("horario");
                modelouno.addRow(datosuno);
            }
        }
        catch (Exception ex) {
            JOptionPane.showMessageDialog(this, ex.getLocalizedMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    public void cargarListaHorario(){
        try {
            idcurso=clase.Registros.leerIdCurso(conex, txtcurso.getText());
            rs=clase.Registros.mostrarHorario(conex, idcurso);
            modelotres.removeAllElements();
            while(rs.next()){
                modelotres.addElement(rs.getString(1));
            }
        }
        catch (Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void cargarProfesor(){
        try{
            rsp=clase.Registros.mostrarProfesor(conex, idcurso);
            while(rsp.next()){
                txtprofesor.setText(rsp.getString(1).concat(" ").concat(rsp.getString(2)));
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void cargarDatosCurso(){
        try{
            rsp=clase.Registros.mostrarDatosCurso(conex, idcurso);
            while(rsp.next()){
                txtprecio.setText("$ ".concat(String.valueOf(rsp.getFloat(1))));
                txtfechaini.setText(rsp.getDate(2).toString());
                txtaula.setText(rsp.getString(3));
            }
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void cargarDatosAlumno(){
        try{
            idalumno=clase.Registros.leerIdAlumno(conex, dni);
            idcurso=clase.Registros.leerIdCurso(conex, txtcurso.getText());
            idca=clase.Registros.leerIDCAalumno(conex, idalumno, idcurso, añoinsc, añocur);
            rsp=clase.Registros.mostrarDatosAlumno(conex,idca);
            while(rsp.next()){
                txtañoins.setText(String.valueOf(rsp.getInt(1)));
                txtfechains.setText(rsp.getDate(2).toString());
                txtdeuda.setText("$ ".concat(String.valueOf(rsp.getFloat(3))));
                txthorario.setText(rsp.getString(4));
                txtcursando.setText(rsp.getString(5));
                txtacademico.setText(rsp.getString(6));
                txtestado.setText(rsp.getString(7));
            }
            horarioSistema=txthorario.getText();
            academicoSistema=txtacademico.getText();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void obteneridca(){
        try{
            idalumno=clase.Registros.leerIdAlumno(conex, dni);
            idcurso=clase.Registros.leerIdCurso(conex, txtcurso.getText());
            idca=clase.Registros.leerIDCAalumno(conex, idalumno, idcurso, añoinsc, añocur);
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void inicio(){
        txtcurso.setEditable(false);
        txtprofesor.setEditable(false);
        txtacademico.setEditable(false);
        txtaula.setEditable(false);
        txthorario.setEditable(false);
        txtprecio.setEditable(false);
        txtestado.setEditable(false);
        txtdeuda.setEditable(false);
        txtcursando.setEditable(false);
        txtañoins.setEditable(false);
        txtfechains.setEditable(false);
        txtfechaini.setEditable(false);
        btneditar.setEnabled(false);
        btncancelar.setEnabled(false);
    }
    
    public void limpiarcanc(){
        txtcurso.setText("");
        txtprofesor.setText("");
        txtacademico.setText("");
        txtaula.setText("");
        txthorario.setText("");
        txtprecio.setText("");
        txtestado.setText("");
        txtdeuda.setText("");
        txtcursando.setText("");
        txtañoins.setText("");
        txtfechains.setText("");
        txtfechaini.setText("");
        modelotres.removeAllElements();
        cboxacademico.setSelectedIndex(0);
        btneditar.setEnabled(false);
        btncancelar.setEnabled(false);
    }
    
    public void cancelaropc(){
        modelouno.setRowCount(0);
        tblalumno.clearSelection();
        txtbuscar.requestFocus(true);
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
        borrarCursoItem = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        PagarItem = new javax.swing.JMenuItem();
        PagarInsItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        RecuperarItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lblbuscaralu = new javax.swing.JLabel();
        lblalumno = new javax.swing.JLabel();
        txtbuscar = new javax.swing.JTextField();
        cboxpor = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblalumno = new javax.swing.JTable();
        jPanel2 = new javax.swing.JPanel();
        lblcurso = new javax.swing.JLabel();
        lblprofesor = new javax.swing.JLabel();
        lblprecio = new javax.swing.JLabel();
        lblfechaini = new javax.swing.JLabel();
        lblfechains = new javax.swing.JLabel();
        lblcursando = new javax.swing.JLabel();
        lblañoins = new javax.swing.JLabel();
        txtcurso = new javax.swing.JTextField();
        txtprofesor = new javax.swing.JTextField();
        txtaula = new javax.swing.JTextField();
        txtprecio = new javax.swing.JTextField();
        txtcursando = new javax.swing.JTextField();
        txtañoins = new javax.swing.JTextField();
        txtfechaini = new javax.swing.JTextField();
        txtfechains = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        lbllistahorario = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        listahorario = new javax.swing.JList<>();
        cboxacademico = new javax.swing.JComboBox<>();
        txtacademico = new javax.swing.JTextField();
        lblestado = new javax.swing.JLabel();
        txtestado = new javax.swing.JTextField();
        lbldeuda = new javax.swing.JLabel();
        txtdeuda = new javax.swing.JTextField();
        txthorario = new javax.swing.JTextField();
        lblhorario = new javax.swing.JLabel();
        lblaula = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblcurso = new javax.swing.JTable();
        btneditar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        btncerrar = new javax.swing.JButton();

        borrarCursoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        borrarCursoItem.setText("Borrar Curso");
        borrarCursoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                borrarCursoItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(borrarCursoItem);

        PagarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money.png"))); // NOI18N
        PagarItem.setText("Ir a Pagar");
        PagarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(PagarItem);

        PagarInsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/money_dollar.png"))); // NOI18N
        PagarInsItem.setText("Pagar Inscripcion");
        PagarInsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarInsItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(PagarInsItem);
        jPopupMenu2.add(jSeparator1);

        RecuperarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/refresh.png"))); // NOI18N
        RecuperarItem.setText("Recuperar Cursos");
        RecuperarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RecuperarItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(RecuperarItem);

        setTitle("Formulario de Registros");
        setPreferredSize(new java.awt.Dimension(850, 580));
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

        lblbuscaralu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        lblbuscaralu.setText("Buscar Alumno:");

        lblalumno.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        lblalumno.setText("Alumnos");

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

        cboxpor.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "LEGAJO", "DNI", "NOMBRE", "APELLIDO" }));
        cboxpor.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxporItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblbuscaralu)
                .addGap(18, 18, 18)
                .addComponent(cboxpor, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtbuscar)
                .addGap(18, 18, 18)
                .addComponent(lblalumno)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblbuscaralu)
                    .addComponent(cboxpor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtbuscar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblalumno))
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
        tblalumno.setComponentPopupMenu(jPopupMenu2);
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Datos de Inscripcion", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        lblcurso.setText("Curso:");

        lblprofesor.setText("Profesor:");

        lblprecio.setText("Precio:");

        lblfechaini.setText("Fecha de Inicio:");

        lblfechains.setText("Fecha de Inscripcion:");

        lblcursando.setText("Cursando el (año):");

        lblañoins.setText("Año de Inscripcion:");

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        lbllistahorario.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbllistahorario.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reloj.png"))); // NOI18N
        lbllistahorario.setText("HORARIOS DISPONIBLES");

        listahorario.setModel(modelotres);
        listahorario.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                listahorarioValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(listahorario);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(lbllistahorario, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lbllistahorario)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        cboxacademico.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Est. Academico", "Cursando", "Egresado" }));
        cboxacademico.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cboxacademicoItemStateChanged(evt);
            }
        });

        lblestado.setText("Estado del curso:");

        lbldeuda.setText("Deuda de Inscripcion:");

        lblhorario.setText("Horario:");

        lblaula.setText("Aula:");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(lblcursando)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtcursando, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                                    .addGap(22, 22, 22)
                                    .addComponent(lblfechains)
                                    .addGap(18, 18, 18)
                                    .addComponent(txtfechains, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lblestado)
                                    .addComponent(lblhorario)
                                    .addComponent(cboxacademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtestado, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(txtacademico)
                                    .addComponent(txthorario))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addComponent(lblfechaini)
                                        .addGap(17, 17, 17))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(lblaula)
                                            .addComponent(lblprecio))
                                        .addGap(18, 18, 18)))
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtaula, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtfechaini, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(lbldeuda)
                                    .addComponent(lblañoins))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtañoins, javax.swing.GroupLayout.DEFAULT_SIZE, 150, Short.MAX_VALUE)
                                    .addComponent(txtdeuda)))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(lblcurso)
                        .addGap(18, 18, 18)
                        .addComponent(txtcurso)
                        .addGap(18, 18, 18)
                        .addComponent(lblprofesor)
                        .addGap(18, 18, 18)
                        .addComponent(txtprofesor, javax.swing.GroupLayout.PREFERRED_SIZE, 325, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblcurso)
                    .addComponent(txtcurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblprofesor)
                    .addComponent(txtprofesor, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtaula, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtacademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboxacademico, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblaula))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txthorario, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblhorario)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtprecio, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblprecio))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtestado, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblestado))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtdeuda, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lbldeuda)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtcursando, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblcursando))
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtañoins, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(lblañoins)))
                        .addGap(14, 14, 14)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblfechaini)
                            .addComponent(lblfechains)
                            .addComponent(txtfechaini, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtfechains, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        tblcurso.setFont(new java.awt.Font("Arial", 0, 10)); // NOI18N
        tblcurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "CURSO", "AÑO INSC.", "CURSANDO", "HORARIO"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class
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
        tblcurso.setComponentPopupMenu(jPopupMenu1);
        tblcurso.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblcursoMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblcurso);
        if (tblcurso.getColumnModel().getColumnCount() > 0) {
            tblcurso.getColumnModel().getColumn(0).setResizable(false);
            tblcurso.getColumnModel().getColumn(1).setResizable(false);
            tblcurso.getColumnModel().getColumn(2).setResizable(false);
            tblcurso.getColumnModel().getColumn(3).setResizable(false);
        }

        btneditar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/options_2.png"))); // NOI18N
        btneditar.setText("Editar");
        btneditar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btneditarActionPerformed(evt);
            }
        });

        btncancelar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help_ring-buoy.png"))); // NOI18N
        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        btncerrar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        btncerrar.setText("Cerrar");
        btncerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncerrarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btneditar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btncerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 160, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btncerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btncancelar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btneditar, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
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
        int columnaABuscar = 0;
        if ("Legajo".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar = 0;
        }
        if ("Nombre".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar = 1;
        }
        if ("Apellido".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar = 2;
        }
        if ("DNI".equalsIgnoreCase(cboxpor.getSelectedItem().toString())) {
            columnaABuscar = 3;
        }
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscar.getText(), columnaABuscar));
    }
    
    private void tblalumnoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblalumnoMouseClicked
        // TODO add your handling code here:
        int fila=tblalumno.rowAtPoint(evt.getPoint());
        limpiarcanc();
        nombre=tblalumno.getValueAt(fila,1).toString();
        apellido=tblalumno.getValueAt(fila,2).toString();
        dni=Integer.parseInt(tblalumno.getValueAt(fila,3).toString());
        mostrarCursos();
    }//GEN-LAST:event_tblalumnoMouseClicked

    private void tblcursoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblcursoMouseClicked
        // TODO add your handling code here:
        int fila=tblcurso.rowAtPoint(evt.getPoint());
        txtcurso.setText(String.valueOf(tblcurso.getValueAt(fila, 0)));
        añoinsc=Integer.parseInt(tblcurso.getValueAt(fila, 1).toString());
        añocur=tblcurso.getValueAt(fila, 2).toString();
        
        cargarListaHorario();
        cargarProfesor();
        cargarDatosCurso();
        cargarDatosAlumno();
        btneditar.setEnabled(true);
        btncancelar.setEnabled(true);
    }//GEN-LAST:event_tblcursoMouseClicked

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        inicio();
        tblalumno.getTableHeader().setReorderingAllowed(false);
        tblcurso.getTableHeader().setReorderingAllowed(false);
        mostrarPersona();
        txtbuscar.requestFocus(true);
    }//GEN-LAST:event_formInternalFrameOpened

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        limpiarcanc();
        cancelaropc();
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btneditarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btneditarActionPerformed
        // TODO add your handling code here:
        if(txtcurso.getText().isEmpty()||txtprofesor.getText().isEmpty()||txtacademico.getText().isEmpty()||txthorario.getText().isEmpty()||txtaula.getText().isEmpty()
                ||txtprecio.getText().isEmpty()||txtestado.getText().isEmpty()||txtdeuda.getText().isEmpty()||txtcursando.getText().isEmpty()||txtañoins.getText().isEmpty()
                ||txtfechains.getText().isEmpty()||txtfechaini.getText().isEmpty()){
            JOptionPane.showMessageDialog(this, "Complete los campos","WARNING",WARNING_MESSAGE);
        }
        else{
            try{
                obteneridca();
                if(!academicoSistema.equalsIgnoreCase(txtacademico.getText())){
                    int op=JOptionPane.showConfirmDialog(this, "Esta seguro de editar el estado academico del alumno?");
                    switch(op){
                        case 0:
                            clase.Registros.editarEstado(conex, txtacademico.getText(), idalumno);
                            est=true;
                            break;
                        case 1:
                            btncancelar.requestFocus(true);
                            break;
                        default:
                            txtbuscar.requestFocus(true);
                            break;
                    }
                }
                if(!horarioSistema.equals(txthorario.getText())){
                    int op1=JOptionPane.showConfirmDialog(this, "Esta seguro de editar el horario ".concat(horarioSistema).concat(" por ").concat(txthorario.getText()).concat("?"));
                    switch(op1){
                        case 0:
                            clase.Registros.editarHorario(conex, txthorario.getText(), idca);
                            limpiarcanc();
                            cancelaropc();
                            est=false;
                            txtbuscar.requestFocus(true);
                            break;
                        case 1:
                            btncancelar.requestFocus(true);
                            break;
                        default:
                            limpiarcanc();
                            cancelaropc();
                            txtbuscar.requestFocus(true);
                            break;
                    }
                }
                else if(academicoSistema.equalsIgnoreCase(txtacademico.getText())&&horarioSistema.equals(txthorario.getText())){
                    JOptionPane.showMessageDialog(this, "El horario ".concat(txthorario.getText()).concat(" es el que esta cargado en el alumno ").concat(nombre).concat(" ").concat(apellido).concat(" inscripto en el curso ").concat(txtcurso.getText()).concat("\nEl estado academico del alumno es el mismo que esta cargado"),"WARNING",WARNING_MESSAGE);
                }
                if(est==true){
                    limpiarcanc();
                    cancelaropc();
                    est=false;
                    txtbuscar.requestFocus(true);
                } 
            }
            catch(Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_btneditarActionPerformed

    private void listahorarioValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_listahorarioValueChanged
        // TODO add your handling code here:
        txthorario.setText(listahorario.getSelectedValue());
    }//GEN-LAST:event_listahorarioValueChanged

    private void txtbuscarFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtbuscarFocusGained
        // TODO add your handling code here:
        limpiarcanc();
        cancelaropc();
    }//GEN-LAST:event_txtbuscarFocusGained

    private void borrarCursoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_borrarCursoItemActionPerformed
        // TODO add your handling code here:
        obteneridca();
        if(idcurso!=0&&idalumno!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de borrar este curso ".concat(txtcurso.getText()).concat("?"));
            switch(op){
                case 0:
                    try{
                        clase.Registros.borrarCursoAlumno(conex, idca);
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    limpiarcanc();
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
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERRROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_borrarCursoItemActionPerformed

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

    private void PagarInsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarInsItemActionPerformed
        // TODO add your handling code here:
        abmEstado pagarins=new abmEstado();
        if(dni!=0){
            pagarins.txtbuscar.setText(String.valueOf(dni));
        }
        this.dispose();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pagarins.getSize();
        pagarins.setLocation((desktopSize.width-FrameSize.width)/27,(desktopSize.height-FrameSize.height)/15);
        panelA.add(pagarins);
        Animacion.Animacion.mover_derecha(0, 110, 5, 5, panelA);
        pagarins.show();
        pagarins.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_PagarInsItemActionPerformed

    private void cboxacademicoItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cboxacademicoItemStateChanged
        // TODO add your handling code here:
        int cboxindex;
        cboxindex=cboxacademico.getSelectedIndex();
        switch(cboxindex){
            case 0:
                txtacademico.setText("");
                break;
            case 1:
                academico=String.valueOf(cboxacademico.getItemAt(cboxindex)).toUpperCase();
                txtacademico.setText(academico);
                break;
            default:
                academico=String.valueOf(cboxacademico.getItemAt(cboxindex)).toUpperCase();
                txtacademico.setText(academico);
                break;
        }
    }//GEN-LAST:event_cboxacademicoItemStateChanged

    private void RecuperarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RecuperarItemActionPerformed
        // TODO add your handling code here:
        if(idalumno!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro que desea recuperar el o los cursos?");
            switch(op){
                case 0:
                    try {
                        clase.Registros.recuperarCursos(conex, idalumno);
                    } 
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    limpiarcanc();
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
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem PagarInsItem;
    private javax.swing.JMenuItem PagarItem;
    private javax.swing.JMenuItem RecuperarItem;
    private javax.swing.JMenuItem borrarCursoItem;
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btncerrar;
    private javax.swing.JButton btneditar;
    private javax.swing.JComboBox<String> cboxacademico;
    private javax.swing.JComboBox<String> cboxpor;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblalumno;
    private javax.swing.JLabel lblaula;
    private javax.swing.JLabel lblañoins;
    private javax.swing.JLabel lblbuscaralu;
    private javax.swing.JLabel lblcursando;
    private javax.swing.JLabel lblcurso;
    private javax.swing.JLabel lbldeuda;
    private javax.swing.JLabel lblestado;
    private javax.swing.JLabel lblfechaini;
    private javax.swing.JLabel lblfechains;
    private javax.swing.JLabel lblhorario;
    private javax.swing.JLabel lbllistahorario;
    private javax.swing.JLabel lblprecio;
    private javax.swing.JLabel lblprofesor;
    private javax.swing.JList<String> listahorario;
    private javax.swing.JTable tblalumno;
    private javax.swing.JTable tblcurso;
    private javax.swing.JTextField txtacademico;
    private javax.swing.JTextField txtaula;
    private javax.swing.JTextField txtañoins;
    public javax.swing.JTextField txtbuscar;
    private javax.swing.JTextField txtcursando;
    private javax.swing.JTextField txtcurso;
    private javax.swing.JTextField txtdeuda;
    private javax.swing.JTextField txtestado;
    private javax.swing.JTextField txtfechaini;
    private javax.swing.JTextField txtfechains;
    private javax.swing.JTextField txthorario;
    private javax.swing.JTextField txtprecio;
    private javax.swing.JTextField txtprofesor;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmRegistros.class.getName());
}
