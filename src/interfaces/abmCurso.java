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
import java.util.Date;
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
 * @author joni
 */
public class abmCurso extends javax.swing.JInternalFrame {
    Connection conex=conexion.Conexion.conectar();
    ResultSet rs;
    Object[] datos=new Object[3];
    DefaultTableModel modelo=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    DefaultTableModel modelo2=new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int filas,int columnas){
            return false;
        }
    };
    private final DefaultListModel modelo1=new DefaultListModel();
    
    int idcurso,idProfesor,cantidad,cantaños,idHorario;
    String aula;
    float cuota,interes,precio;
    /**
     * Creates new form abmCurso
     */
    public abmCurso() {
        initComponents();
    }
    
    public void mostrarCurso(){
        try {
            rs=clase.Curso.mostrarCurso(conex,idcurso);
            if(rs.next()) {
                cursoTxt.setText(rs.getString(1));
                txtprecioinsc.setText("$ ".concat(String.valueOf(rs.getFloat(2))));
                presioTxt.setText("$ ".concat(String.valueOf(rs.getFloat(3))));
                cantidadTxt.setText(rs.getString(4));
                fechaIn.setDate(rs.getDate(5));
                dniTxt.setText(rs.getString(6));
                nombreTxt.setText(rs.getString(7));
                apellidoTxt.setText(rs.getString(8));
                añosTxt.setText(String.valueOf(rs.getInt(9)));
                interesTxt.setText("$ ".concat(String.valueOf(rs.getFloat(10))));
                aulaTxt.setText(rs.getString(11));
            }
            cargarListaCursoHorario();
            limpiarbtnmc();
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarSinCurso(){
        try{
            rs=clase.Curso.mostrarCursoSinP(conex, idcurso);
            if(rs.next()){
                cursoTxt.setText(rs.getString(1));
                txtprecioinsc.setText("$ ".concat(String.valueOf(rs.getFloat(2))));
                presioTxt.setText("$ ".concat(String.valueOf(rs.getFloat(3))));
                cantidadTxt.setText(rs.getString(4));
                fechaIn.setDate(rs.getDate(5));
                añosTxt.setText(String.valueOf(rs.getInt(6)));
                interesTxt.setText("$ ".concat(String.valueOf(rs.getFloat(7))));
                aulaTxt.setText(rs.getString(8));
            }
            cargarListaCursoHorario();
            limpiarbtnmc();
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarCursos(){
        modelo.setRowCount(0);
        modelo.setColumnCount(0);
        modelo.addColumn("CURSOS");
        modelo.addColumn("CUOTA");
        modelo.addColumn("FECHA DE INICIO");
        try {
            tableCurso.setModel(modelo);
            rs=clase.Curso.mostrarCursos(conex);
            while(rs.next()){
                datos[0]=rs.getString("nombre");
                datos[1]=rs.getFloat("cuota");
                datos[2]=rs.getDate("fechaInicio");
                modelo.addRow(datos);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarCursosSinProf(){
        modelo2.setRowCount(0);
        modelo2.setColumnCount(0);
        modelo2.addColumn("CURSOS");
        modelo2.addColumn("CUOTA");
        modelo2.addColumn("FECHA DE INICIO");
        try {
            tablacursosinprof.setModel(modelo2);
            rs=clase.Curso.mostrarCursosSinProf(conex);
            while(rs.next()){
                datos[0]=rs.getString("nombre");
                datos[1]=rs.getFloat("cuota");
                datos[2]=rs.getDate("fechaInicio");
                modelo2.addRow(datos);
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarDatosCurso(){
        try {
            rs=clase.Curso.mostrarCurso(conex,idcurso);
            while(rs.next()){
                txtprecioinsc.setText("$ ".concat(String.valueOf(rs.getFloat(2))));
                cantidadTxt.setText(rs.getString(4));
                dniTxt.setText(rs.getString(6));
                nombreTxt.setText(rs.getString(7));
                apellidoTxt.setText(rs.getString(8));
                añosTxt.setText(String.valueOf(rs.getInt(9)));
                interesTxt.setText("$ ".concat(String.valueOf(rs.getFloat(10))));
                aulaTxt.setText(rs.getString(11));
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void mostrarDatosCursoSinProf(){
        try {
            rs=clase.Curso.mostrarCursoSinP(conex,idcurso);
            while(rs.next()){
                txtprecioinsc.setText("$ ".concat(String.valueOf(rs.getFloat("precioInscripcion"))));
                cantidadTxt.setText(rs.getString("cantidad"));
                añosTxt.setText(String.valueOf(rs.getInt("cantidadAño")));
                interesTxt.setText("$ ".concat(String.valueOf(rs.getFloat("interesCurso"))));
                aulaTxt.setText(rs.getString("aula"));
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void abrirHorario(){
        abmHorario h=new abmHorario();
        h.cursoHTxt.setText(cursoTxt.getText().toUpperCase());
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=h.getSize();
        h.setLocation((desktopSize.width-FrameSize.width)/63,(desktopSize.height-FrameSize.height)/80);
        panelA.add(h);
        this.dispose();
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        h.show();
        h.cursoHTxt.requestFocus(true);
    }
    
    @SuppressWarnings("unchecked")
    public void cargarListaCursoHorario(){
        try {
            rs=clase.Curso.MostrarCursoHorario(conex, idcurso);
            while(rs.next()){
                modelo1.addElement(rs.getString(1));
            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void buscarIDC(){
        try{
            idcurso=clase.Curso.leerIdCurso(conex, cursoTxt.getText());
        }
        catch(Exception e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void capturarDatos(){
        precio=Float.parseFloat(txtprecioinsc.getText().substring(1));
        cuota=Float.parseFloat(presioTxt.getText().substring(1));
        interes=Float.parseFloat(interesTxt.getText().substring(1));
        cantaños=Integer.parseInt(añosTxt.getText());
        aula=String.valueOf(cmbAula.getSelectedItem());
    }
    
    public void editarDatos(){
        precio=Float.parseFloat(txtprecioinsc.getText().substring(1));
        cuota=Float.parseFloat(presioTxt.getText().substring(1));
        interes=Float.parseFloat(interesTxt.getText().substring(1));
        cantaños=Integer.parseInt(añosTxt.getText());
    }
    
    public void limpiar(){
        cursoTxt.setText("");
        txtprecioinsc.setText("");
        presioTxt.setText("");
        cantidadTxt.setText("");
        fechaIn.setDate(null);
        dniTxt.setText("");
        nombreTxt.setText("");
        apellidoTxt.setText("");
        horarioTxt.setText("");
        interesTxt.setText("");
        aulaTxt.setText("");
        añosTxt.setText("");
        modelo1.removeAllElements();
        tableCurso.clearSelection();
        tablacursosinprof.clearSelection();
    }
    
     public void limpiarcurso(){
        cursoTxt.setText("");
        txtprecioinsc.setText("");
        presioTxt.setText("");
        cantidadTxt.setText("");
        fechaIn.setDate(null);
        horarioTxt.setText("");
        interesTxt.setText("");
        aulaTxt.setText("");
        añosTxt.setText("");
        modelo1.removeAllElements();
        tableCurso.clearSelection();
        tablacursosinprof.clearSelection();
    }
    
    public void limpiarbtnmc(){
        guardarBtn.setEnabled(false);
        editarBtn.setEnabled(true);
        cancelarBtn.setEnabled(true);
    }
    
    public void limpiarbtncanc(){
        guardarBtn.setEnabled(true);
        editarBtn.setEnabled(false);
        cancelarBtn.setEnabled(false);
        dniTxt.setEditable(true);
        quitarBtn.setEnabled(false);
        btnquitarprofe.setEnabled(false);
        cmbAula.setSelectedIndex(0);
        dniTxt.requestFocus(true);
    }
    
    public void limpiarespecial(){
        dniTxt.setEditable(true);
        quitarBtn.setEnabled(false);
        cmbAula.setSelectedIndex(0);
        dniTxt.requestFocus(true);
    }
    
    public void limpiarinicio(){
        horarioTxt.setEditable(false);
        quitarBtn.setEnabled(false);
        aulaTxt.setEditable(false);
        guardarBtn.setEnabled(true);
        editarBtn.setEnabled(false);
        cancelarBtn.setEnabled(true);
        btnquitarprofe.setEnabled(false);
    }
    
    public void limpiarbotones(){
        guardarBtn.setEnabled(true);
        editarBtn.setEnabled(false);
        cancelarBtn.setEnabled(false);
        btnquitarprofe.setEnabled(false);
    }
    
    public void bloqfunc(){
        dniTxt.setEditable(false);
        nombreTxt.setEditable(false);
        apellidoTxt.setEditable(false);
    }
    
    public void inicio(){
        horarioTxt.setEditable(false);
        nombreTxt.setEditable(false);
        apellidoTxt.setEditable(false);
        quitarBtn.setEnabled(false);
        dniTxt.setEditable(true);
        aulaTxt.setEditable(false);
    }
    
    public void otrasfunc(){
        dniTxt.setEditable(false);
        cancelarBtn.setEnabled(true);
        cursoTxt.requestFocus(true);
    }
    
    public void limpiarcprof(){
        nombreTxt.setText("");
        apellidoTxt.setText("");
        dniTxt.requestFocus();
    }
    
    public void limpiarexcep(){
        horarioTxt.setText("");
        jListHorario.clearSelection();
        quitarBtn.setEnabled(false);
        cursoTxt.requestFocus(true);
    }
    
    public void limpiarhorarios(){
        horarioTxt.setText("");
//        jListHorario.clearSelection();
        modelo1.removeAllElements();
        quitarBtn.setEnabled(false);
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
        HorarioItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        QuitarItem = new javax.swing.JMenuItem();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        HorarioSinPItem = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        lbldni = new javax.swing.JLabel();
        lblnombre = new javax.swing.JLabel();
        lblapellido = new javax.swing.JLabel();
        lblbuscarcurso = new javax.swing.JLabel();
        dniTxt = new javax.swing.JTextField();
        nombreTxt = new javax.swing.JTextField();
        apellidoTxt = new javax.swing.JTextField();
        txtbuscarcurso = new javax.swing.JTextField();
        irBtn = new javax.swing.JButton();
        btnquitarprofe = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        lblcurso = new javax.swing.JLabel();
        lblhorario = new javax.swing.JLabel();
        lblfechaini = new javax.swing.JLabel();
        lblprecio = new javax.swing.JLabel();
        lblinteres = new javax.swing.JLabel();
        lblaños = new javax.swing.JLabel();
        lblcuotas = new javax.swing.JLabel();
        cursoTxt = new javax.swing.JTextField();
        horarioTxt = new javax.swing.JTextField();
        fechaIn = new com.toedter.calendar.JDateChooser();
        presioTxt = new javax.swing.JTextField();
        interesTxt = new javax.swing.JTextField();
        añosTxt = new javax.swing.JTextField();
        cantidadTxt = new javax.swing.JTextField();
        aulaTxt = new javax.swing.JTextField();
        cmbAula = new javax.swing.JComboBox<>();
        quitarBtn = new javax.swing.JButton();
        cursoBtn = new javax.swing.JButton();
        guardarBtn = new javax.swing.JButton();
        editarBtn = new javax.swing.JButton();
        cancelarBtn = new javax.swing.JButton();
        cerrarBtn = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jListHorario = new javax.swing.JList<>();
        lblhorarios = new javax.swing.JLabel();
        lblprecioinsc = new javax.swing.JLabel();
        txtprecioinsc = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableCurso = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tablacursosinprof = new javax.swing.JTable();
        lblcursos = new javax.swing.JLabel();
        lblsincursos = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();

        HorarioItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reloj.png"))); // NOI18N
        HorarioItem.setText("Agregar Horario");
        HorarioItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorarioItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(HorarioItem);
        jPopupMenu1.add(jSeparator2);

        QuitarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        QuitarItem.setText("Quitar Profesor");
        QuitarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QuitarItemActionPerformed(evt);
            }
        });
        jPopupMenu1.add(QuitarItem);

        HorarioSinPItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reloj.png"))); // NOI18N
        HorarioSinPItem.setText("Agregar Horario");
        HorarioSinPItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorarioSinPItemActionPerformed(evt);
            }
        });
        jPopupMenu2.add(HorarioSinPItem);

        setTitle("Formulario del Curso");
        setPreferredSize(new java.awt.Dimension(875, 640));
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
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "PROFESOR", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        lbldni.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/search.png"))); // NOI18N
        lbldni.setText("DNI:");

        lblnombre.setText("Nombre:");

        lblapellido.setText("Apellido:");

        lblbuscarcurso.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lupa.png"))); // NOI18N
        lblbuscarcurso.setText("Curso:");

        dniTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                dniTxtKeyPressed(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                dniTxtKeyTyped(evt);
            }
        });

        txtbuscarcurso.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                txtbuscarcursoFocusGained(evt);
            }
        });
        txtbuscarcurso.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtbuscarcursoKeyTyped(evt);
            }
        });

        irBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/user_woman.png"))); // NOI18N
        irBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        irBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                irBtnActionPerformed(evt);
            }
        });

        btnquitarprofe.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        btnquitarprofe.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        btnquitarprofe.setText("Quitar \nProfesor");
        btnquitarprofe.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        btnquitarprofe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        btnquitarprofe.setIconTextGap(2);
        btnquitarprofe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        btnquitarprofe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnquitarprofeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lbldni)
                    .addComponent(lblnombre))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(dniTxt)
                        .addGap(18, 18, 18)
                        .addComponent(irBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 251, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 35, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblbuscarcurso, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblapellido, javax.swing.GroupLayout.Alignment.TRAILING))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtbuscarcurso, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(apellidoTxt, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(btnquitarprofe)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(irBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 25, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lbldni)
                        .addComponent(dniTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblbuscarcurso)
                        .addComponent(txtbuscarcurso, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblnombre)
                    .addComponent(nombreTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblapellido)
                    .addComponent(apellidoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(btnquitarprofe, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 10, 835, -1));

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "CURSO", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N
        jPanel2.setPreferredSize(new java.awt.Dimension(787, 290));

        lblcurso.setText("Nombre del Curso:");

        lblhorario.setText("Horario:");

        lblfechaini.setText("Fecha de Inicio:");

        lblprecio.setText("Precio del Curso:");

        lblinteres.setText("Interes del Curso:");

        lblaños.setText("Cantidad de Años:");

        lblcuotas.setText("Cantidad de Cuotas:");

        cursoTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cursoTxtKeyTyped(evt);
            }
        });

        presioTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                presioTxtKeyTyped(evt);
            }
        });

        interesTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                interesTxtKeyTyped(evt);
            }
        });

        añosTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                añosTxtKeyTyped(evt);
            }
        });

        cantidadTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                cantidadTxtKeyTyped(evt);
            }
        });

        aulaTxt.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                aulaTxtKeyTyped(evt);
            }
        });

        cmbAula.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "AULAS", "aula -  A", "aula -  B", "aula -  C", "aula -  D" }));
        cmbAula.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cmbAulaItemStateChanged(evt);
            }
        });

        quitarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/delete_2.png"))); // NOI18N
        quitarBtn.setText("Quitar");
        quitarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitarBtnActionPerformed(evt);
            }
        });

        cursoBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/book.png"))); // NOI18N
        cursoBtn.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        cursoBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cursoBtnActionPerformed(evt);
            }
        });

        guardarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save_as.png"))); // NOI18N
        guardarBtn.setText("Guardar");
        guardarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        guardarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarBtnActionPerformed(evt);
            }
        });

        editarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/options_2.png"))); // NOI18N
        editarBtn.setText("Editar");
        editarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        editarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                editarBtnActionPerformed(evt);
            }
        });

        cancelarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help_ring-buoy.png"))); // NOI18N
        cancelarBtn.setText("Cancelar");
        cancelarBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        cancelarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelarBtnActionPerformed(evt);
            }
        });

        cerrarBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete_2.png"))); // NOI18N
        cerrarBtn.setText("Cerrar");
        cerrarBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarBtnActionPerformed(evt);
            }
        });

        jPanel5.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jListHorario.setModel(modelo1
        );
        jListHorario.addListSelectionListener(new javax.swing.event.ListSelectionListener() {
            public void valueChanged(javax.swing.event.ListSelectionEvent evt) {
                jListHorarioValueChanged(evt);
            }
        });
        jScrollPane3.setViewportView(jListHorario);

        lblhorarios.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblhorarios.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reloj.png"))); // NOI18N
        lblhorarios.setText("Horarios del Curso");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(lblhorarios, javax.swing.GroupLayout.DEFAULT_SIZE, 166, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblhorarios)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        lblprecioinsc.setText("Precio de Inscripcion:");

        txtprecioinsc.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtprecioinscKeyTyped(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(37, 37, 37)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblcurso)
                            .addComponent(lblfechaini)
                            .addComponent(lblprecio)
                            .addComponent(lblaños)
                            .addComponent(cmbAula, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(cursoTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(cursoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(95, 95, 95)
                                .addComponent(lblhorario)
                                .addGap(18, 18, 18)
                                .addComponent(horarioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 146, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(quitarBtn))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(añosTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(presioTxt, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(fechaIn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblcuotas)
                                            .addComponent(lblinteres)
                                            .addComponent(lblprecioinsc))
                                        .addGap(15, 15, 15)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(interesTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                            .addComponent(cantidadTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 126, Short.MAX_VALUE)
                                            .addComponent(txtprecioinsc)))
                                    .addComponent(aulaTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 144, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(91, 91, 91)
                        .addComponent(guardarBtn)
                        .addGap(18, 18, 18)
                        .addComponent(editarBtn)
                        .addGap(18, 18, 18)
                        .addComponent(cancelarBtn)
                        .addGap(18, 18, 18)
                        .addComponent(cerrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 95, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(36, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {cancelarBtn, editarBtn, guardarBtn});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblcurso, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cursoTxt))
                    .addComponent(cursoBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(lblhorario)
                        .addComponent(horarioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(quitarBtn)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblfechaini, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(fechaIn, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(lblprecioinsc)
                                .addComponent(txtprecioinsc, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(presioTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblprecio)
                            .addComponent(lblinteres)
                            .addComponent(interesTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(añosTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblaños)
                            .addComponent(lblcuotas)
                            .addComponent(cantidadTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(aulaTxt)
                            .addComponent(cmbAula)))
                    .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(guardarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(editarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cancelarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cerrarBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(52, 52, 52))
        );

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 116, 835, 280));

        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "CURSOS", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        tableCurso.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tableCurso.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cursos", "Cuota", "Fecha Inicio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
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
        jScrollPane1.setViewportView(tableCurso);
        if (tableCurso.getColumnModel().getColumnCount() > 0) {
            tableCurso.getColumnModel().getColumn(0).setResizable(false);
            tableCurso.getColumnModel().getColumn(1).setResizable(false);
            tableCurso.getColumnModel().getColumn(2).setResizable(false);
        }

        tablacursosinprof.setFont(new java.awt.Font("Tahoma", 0, 10)); // NOI18N
        tablacursosinprof.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Cursos", "Cuota", "Fecha Inicio"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.Float.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tablacursosinprof.setComponentPopupMenu(jPopupMenu2);
        tablacursosinprof.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tablacursosinprofMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tablacursosinprof);
        if (tablacursosinprof.getColumnModel().getColumnCount() > 0) {
            tablacursosinprof.getColumnModel().getColumn(0).setResizable(false);
            tablacursosinprof.getColumnModel().getColumn(1).setResizable(false);
            tablacursosinprof.getColumnModel().getColumn(2).setResizable(false);
        }

        lblcursos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblcursos.setText("                                                        CURSOS");

        lblsincursos.setFont(new java.awt.Font("Tahoma", 1, 11)); // NOI18N
        lblsincursos.setText("                                             CURSOS SIN PROFESOR");

        jSeparator1.setForeground(new java.awt.Color(153, 153, 153));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblcursos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE))
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 4, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(lblsincursos, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap(15, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(9, 9, 9)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblcursos)
                            .addComponent(lblsincursos))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 110, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 7, Short.MAX_VALUE)))
                .addContainerGap())
        );

        getContentPane().add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 400, 835, 190));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tableCursoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableCursoMouseClicked
        int fila=tableCurso.rowAtPoint(evt.getPoint());
        cursoTxt.setText(tableCurso.getValueAt(fila,0).toString());
        presioTxt.setText(("$ ").concat(tableCurso.getValueAt(fila,1).toString()));
        fechaIn.setDate((Date)tableCurso.getValueAt(fila,2));
        buscarIDC();
        mostrarDatosCurso();
        limpiarhorarios();
        tablacursosinprof.clearSelection();
        btnquitarprofe.setEnabled(true);
        cargarListaCursoHorario();
        quitarBtn.setEnabled(false);
        limpiarbtnmc();
        bloqfunc();
    }//GEN-LAST:event_tableCursoMouseClicked

    private void HorarioItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorarioItemActionPerformed
        abmHorario h=new abmHorario();
        h.cursoHTxt.setText(cursoTxt.getText());
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=h.getSize();
        h.setLocation((desktopSize.width-FrameSize.width)/63,(desktopSize.height-FrameSize.height)/80);
        panelA.add(h);
        this.dispose();
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        h.show();
        h.cursoHTxt.requestFocus(true);
    }//GEN-LAST:event_HorarioItemActionPerformed

    private void irBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_irBtnActionPerformed
        abmProfesor pr=new abmProfesor();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pr.getSize();
        pr.setLocation((desktopSize.width-FrameSize.width)/20,(desktopSize.height-FrameSize.height)/40);
        panelA.add(pr);
        this.dispose();
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pr.show();
        pr.buscarTxt.requestFocus(true);
    }//GEN-LAST:event_irBtnActionPerformed

    private void dniTxtKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_dniTxtKeyPressed
        if(evt.getKeyCode()==KeyEvent.VK_ENTER){
            try {
                idProfesor=clase.Curso.leerIdProfesor(conex,Integer.parseInt(dniTxt.getText()) );
                rs=clase.Curso.mostrarProfesor(conex,idProfesor);
                if(rs.next()){
                    nombreTxt.setText(rs.getString(1));
                    apellidoTxt.setText(rs.getString(2));
                    otrasfunc();
                }
                else{
                    JOptionPane.showMessageDialog(this,"No existe un profesor con ese DNI ".concat(dniTxt.getText()),"ERROR",ERROR_MESSAGE);
                    limpiarcprof();
                }
            }
            catch (Exception e){
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }
        }
    }//GEN-LAST:event_dniTxtKeyPressed

    private void formInternalFrameOpened(javax.swing.event.InternalFrameEvent evt) {//GEN-FIRST:event_formInternalFrameOpened
        // TODO add your handling code here:
        if(dniTxt.getText().isEmpty()||nombreTxt.getText().isEmpty()||apellidoTxt.getText().isEmpty()){
            inicio();
            limpiarbotones();
        }
        else{
            bloqfunc();
            limpiarinicio();
        }
        mostrarCursos();
        mostrarCursosSinProf();
        tableCurso.getTableHeader().setReorderingAllowed(false);
        tablacursosinprof.getTableHeader().setReorderingAllowed(false);
    }//GEN-LAST:event_formInternalFrameOpened

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

    private void tablacursosinprofMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tablacursosinprofMouseClicked
        // TODO add your handling code here:
        int fila=tablacursosinprof.rowAtPoint(evt.getPoint());
        cursoTxt.setText(tablacursosinprof.getValueAt(fila,0).toString());
        presioTxt.setText(("$ ").concat(tablacursosinprof.getValueAt(fila,1).toString()));
        fechaIn.setDate((Date)tablacursosinprof.getValueAt(fila,2));
        buscarIDC();
        mostrarDatosCursoSinProf();
        limpiarhorarios();
        tableCurso.clearSelection();
        if(dniTxt.getText().isEmpty()||nombreTxt.getText().isEmpty()||apellidoTxt.getText().isEmpty()){
            cargarListaCursoHorario();
            inicio();
            limpiarbtnmc();
            btnquitarprofe.setEnabled(false);
            quitarBtn.setEnabled(false);
            dniTxt.requestFocus(true);
        }
        else{
            cargarListaCursoHorario();
            limpiarbtnmc();
            bloqfunc();
            btnquitarprofe.setEnabled(false);
            quitarBtn.setEnabled(false);
            cursoTxt.requestFocus(true);
        }
    }//GEN-LAST:event_tablacursosinprofMouseClicked

    private void cerrarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarBtnActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_cerrarBtnActionPerformed

    private void cancelarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelarBtnActionPerformed
        limpiar();
        limpiarbtncanc();
        mostrarCursos();
        mostrarCursosSinProf();
    }//GEN-LAST:event_cancelarBtnActionPerformed

    private void editarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_editarBtnActionPerformed
        if((cursoTxt.getText().isEmpty())||(fechaIn.getDate()==null)||(presioTxt.getText().isEmpty())
            ||(cantidadTxt.getText().isEmpty())||(interesTxt.getText().isEmpty())
            ||(añosTxt.getText().isEmpty())||(aulaTxt.getText().isEmpty())||(txtprecioinsc.getText().isEmpty())){
            JOptionPane.showMessageDialog(this, "Complete los campos","WARNING",WARNING_MESSAGE);
        }
        else{
            if((dniTxt.getText().isEmpty())||(nombreTxt.getText().isEmpty())
                ||apellidoTxt.getText().isEmpty()){
                cantidad=Integer.parseInt(cantidadTxt.getText());
                if(cantidad>=1&&cantidad<=12){//Editar un curso sin profesor
                    try{
                        editarDatos();
                        int op=JOptionPane.showConfirmDialog(this, "Estas seguro de editar los datos de este curso sin profesor?");
                        switch(op){
                            case 0:
                                clase.Curso.EditarCurso(conex, 0, cursoTxt.getText().toUpperCase(), precio, cuota, cantidad, fechaIn.getDate(), cantaños, interes, aulaTxt.getText().toUpperCase(), idcurso);
                                mostrarCursos();
                                mostrarCursosSinProf();
                                limpiar();
                                limpiarbotones();
                                limpiarespecial();
                                break;
                            case 1:
                                cursoTxt.requestFocus(true);
                                break;
                            default:
                                limpiar();
                                limpiarbotones();
                                limpiarespecial();
                                break;
                        }
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No puede superar las 12 cuotas correspondientes\na la cantidad de meses del año.\nTampoco puede establecer la cantidad de cuotas en 0.\n\nRevise la cantidad de cuotas establecidas!!!","WARNING",WARNING_MESSAGE);
                    cantidadTxt.requestFocus(true);
                }
            }
            else{
                cantidad=Integer.parseInt(cantidadTxt.getText());
                if(cantidad>=1&&cantidad<=12){//Editar un curso con profesor
                    try {
                        editarDatos();
                        idProfesor=clase.Curso.leerIdProfesor(conex, Integer.parseInt(dniTxt.getText()));
                        int op1=JOptionPane.showConfirmDialog(this, "Esta seguro de editar los datos del curso?");
                        switch (op1) {
                            case 0:
                                clase.Curso.EditarCurso(conex, idProfesor,cursoTxt.getText().toUpperCase(),precio,cuota,cantidad, fechaIn.getDate(),cantaños,interes,aulaTxt.getText().toUpperCase(),idcurso);
                                mostrarCursos();
                                mostrarCursosSinProf();
                                limpiar();
                                limpiarbotones();
                                limpiarespecial();
                                break;
                            case 1:
                                cursoTxt.requestFocus(true);
                                break;
                            default:
                                limpiar();
                                limpiarbotones();
                                limpiarespecial();
                                break;
                        }
                    }
                    catch (Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No puede superar las 12 cuotas correspondientes\na la cantidad de meses del año.\nTampoco puede establecer la cantidad de cuotas en 0.\n\nRevise la cantidad de cuotas establecidas!!!","WARNING",WARNING_MESSAGE);
                    cantidadTxt.requestFocus(true);
                }
            }
        }
    }//GEN-LAST:event_editarBtnActionPerformed

    private void guardarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarBtnActionPerformed
        if((cursoTxt.getText().isEmpty())||(fechaIn.getDate()==null)||(presioTxt.getText().isEmpty())
            ||(cantidadTxt.getText().isEmpty())||(interesTxt.getText().isEmpty())
            ||(añosTxt.getText().isEmpty())||(aulaTxt.getText().isEmpty())||(txtprecioinsc.getText().isEmpty())){
            JOptionPane.showMessageDialog(this, "Complete los campos","WARNING",WARNING_MESSAGE);
        }
        else{
            if((dniTxt.getText().isEmpty())||(nombreTxt.getText().isEmpty())
                ||apellidoTxt.getText().isEmpty()){
                cantidad=Integer.parseInt(cantidadTxt.getText());//Creacion de un curso sin un profesor asociado
                if(cantidad>=1&&cantidad<=12){
                    try{
                        capturarDatos();
                        idcurso=clase.Curso.leerIdCurso(conex, cursoTxt.getText().toUpperCase());
                        if(idcurso==0){
                            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de crear el curso ".concat(cursoTxt.getText().toUpperCase()).concat(" sin un profesor?"));
                            switch (op) {
                                case 0:
                                    clase.Curso.insertarCurso(conex, 0,cursoTxt.getText().toUpperCase(),precio,cuota, cantidad, fechaIn.getDate(),cantaños,interes,aula.toUpperCase());
                                    mostrarCursosSinProf();
                                    int op1=JOptionPane.showConfirmDialog(this,"Agregar horario al curso ".concat(cursoTxt.getText().toUpperCase()).concat("?"));
                                    if(op1==0){
                                        abrirHorario();
                                    }
                                    if(op1==1){
                                        limpiar();
                                        limpiarbotones();
                                        dniTxt.setEditable(true);
                                        cursoTxt.requestFocus(true);
                                    }
                                    else{
                                        limpiar();
                                        limpiarbotones();
                                        dniTxt.setEditable(true);
                                        cmbAula.setSelectedIndex(0);
                                        cursoTxt.requestFocus(true);
                                    }
                                    break;
                                case 1:
                                    cursoTxt.requestFocus(true);
                                    break;
                                default:
                                    limpiar();
                                    limpiarbotones();
                                    cmbAula.setSelectedIndex(0);
                                    dniTxt.requestFocus(true);
                                    break;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Ya existe un curso con ese nombre ".concat(cursoTxt.getText().toUpperCase()),"ERROR",ERROR_MESSAGE);
                            cursoTxt.requestFocus();
                        }
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No puede superar las 12 cuotas correspondientes\n a la cantidad de meses del año.\n Tampoco puede establecer la cantidad de cuotas en 0.\n\nRevise la cantidad de cuotas establecidas!!!","WARNING",WARNING_MESSAGE);
                    cantidadTxt.requestFocus(true);
                }
            }
            else{
                cantidad=Integer.parseInt(cantidadTxt.getText());//Creacion de un curso con un profesor asociado
                if(cantidad>=1&&cantidad<=12){
                    try {
                        capturarDatos();
                        idProfesor=clase.Curso.leerIdProfesor(conex, Integer.parseInt(dniTxt.getText()));
                        idcurso=clase.Curso.leerIdCurso(conex,cursoTxt.getText().toUpperCase());
                        if(idcurso==0){
                            int op2=JOptionPane.showConfirmDialog(this, "Esta seguro de crear el curso ".concat(cursoTxt.getText().toUpperCase()).concat(" asignando al profesor ").concat(nombreTxt.getText().concat(" ").concat(apellidoTxt.getText())).concat("?"));
                            switch (op2) {
                                case 0:
                                    clase.Curso.insertarCurso(conex, idProfesor,cursoTxt.getText().toUpperCase(),precio,cuota, cantidad, fechaIn.getDate(),cantaños,interes,aula.toUpperCase());
                                    mostrarCursos();
                                    int op3=JOptionPane.showConfirmDialog(this,"Agregar horario al curso ".concat(cursoTxt.getText().toUpperCase()).concat("?"));
                                    if(op3==0){
                                        abrirHorario();
                                    }
                                    if(op3==1){
                                        limpiar();
                                        limpiarbotones();
                                        dniTxt.setEditable(true);
                                        cursoTxt.requestFocus(true);
                                    }
                                    else{
                                        limpiar();
                                        limpiarbotones();
                                        dniTxt.setEditable(true);
                                        cmbAula.setSelectedIndex(0);
                                        cursoTxt.requestFocus(true);
                                    }
                                    break;
                                case 1:
                                    cursoTxt.requestFocus(true);
                                    break;
                                default:
                                    limpiar();
                                    limpiarbotones();
                                    cmbAula.setSelectedIndex(0);
                                    dniTxt.requestFocus(true);
                                    break;
                            }
                        }
                        else{
                            JOptionPane.showMessageDialog(this, "Ya existe un curso con ese nombre ".concat(cursoTxt.getText().toUpperCase()),"ERROR",ERROR_MESSAGE);
                            cursoTxt.requestFocus();
                        }
                    }
                    catch (Exception e) {
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                }
                else{
                    JOptionPane.showMessageDialog(this, "No puede superar las 12 cuotas correspondientes\n a la cantidad de meses del año.\n Tampoco puede establecer la cantidad de cuotas en 0.\n\nRevise la cantidad de cuotas establecidas!!!","WARNING",WARNING_MESSAGE);
                    cantidadTxt.requestFocus(true);
                }
            }
        }
    }//GEN-LAST:event_guardarBtnActionPerformed

    private void jListHorarioValueChanged(javax.swing.event.ListSelectionEvent evt) {//GEN-FIRST:event_jListHorarioValueChanged
        horarioTxt.setText(jListHorario.getSelectedValue());
        quitarBtn.setEnabled(true);
    }//GEN-LAST:event_jListHorarioValueChanged

    private void cursoBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cursoBtnActionPerformed
        String curso=JOptionPane.showInputDialog("Ingrese el nombre del Curso?");
        try{
            idcurso=clase.Curso.leerIdCurso(conex,curso.toUpperCase());
            idProfesor=clase.Curso.leerIdProf(conex, idcurso);
            if(idcurso!=0){
                if(idProfesor!=0){
                    limpiarhorarios();
                    mostrarCurso();
                    bloqfunc();
                    tableCurso.clearSelection();
                    tablacursosinprof.clearSelection();
                    btnquitarprofe.setEnabled(true);
                    quitarBtn.setEnabled(false);
                }
                else{
                    limpiarhorarios();
                    mostrarSinCurso();
                    tableCurso.clearSelection();
                    tablacursosinprof.clearSelection();
                    btnquitarprofe.setEnabled(false);
                    quitarBtn.setEnabled(false);
                    dniTxt.requestFocus(true);
                }
            }
            else{
                JOptionPane.showMessageDialog(this, "No existe un curso con ese nombre","ERROR",ERROR_MESSAGE);
                cursoTxt.setText(curso);
                limpiarcurso();
                limpiarbotones();
                quitarBtn.setEnabled(false);
                cursoTxt.requestFocus(true);
            }
        }
        catch(Exception e){
//            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }//GEN-LAST:event_cursoBtnActionPerformed

    private void quitarBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitarBtnActionPerformed
        int op=JOptionPane.showConfirmDialog(this, "Estas seguro de borrar este horario del curso?");
        switch(op){
            case 0:
            try {
                idHorario=clase.Curso.leerIdHorario(conex, horarioTxt.getText());
                clase.Curso.BorrarCursoHorario(conex, idcurso, idHorario);
                modelo1.removeAllElements();
                cargarListaCursoHorario();
                horarioTxt.setText("");
                quitarBtn.setEnabled(false);
            }
            catch (Exception e) {
                JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
            }   break;
            case 1:
                cursoTxt.requestFocus(true);
            break;
            default:
               limpiarexcep();
            break;
        }
    }//GEN-LAST:event_quitarBtnActionPerformed

    private void cmbAulaItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cmbAulaItemStateChanged
        int cboxindex;
        cboxindex=cmbAula.getSelectedIndex();
        switch(cboxindex){
            case 0:
                aulaTxt.setText("");
                break;
            case 1:
                aula=String.valueOf(cmbAula.getItemAt(cboxindex));
                aulaTxt.setText(aula);
                break;
            case 2:
                aula=String.valueOf(cmbAula.getItemAt(cboxindex));
                aulaTxt.setText(aula);
                break;
            case 3:
                aula=String.valueOf(cmbAula.getItemAt(cboxindex));
                aulaTxt.setText(aula);
                break;
            default:
                aula=String.valueOf(cmbAula.getItemAt(cboxindex));
                aulaTxt.setText(aula);
                break;
        }
    }//GEN-LAST:event_cmbAulaItemStateChanged

    private void aulaTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_aulaTxtKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(aulaTxt.getText().length()>=15){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_aulaTxtKeyTyped

    private void cantidadTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cantidadTxtKeyTyped
        char car = evt.getKeyChar();
        if(cantidadTxt.getText().length()>=2){
            evt.consume();
        }
        if (!Character.isDigit(car)){
            evt.consume();
        }
    }//GEN-LAST:event_cantidadTxtKeyTyped

    private void añosTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_añosTxtKeyTyped
        char car = evt.getKeyChar();
        if(añosTxt.getText().length()>=1){
            evt.consume();
        }
        if ((car <'1' || car>'5')){
            evt.consume();
        }
    }//GEN-LAST:event_añosTxtKeyTyped

    private void interesTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_interesTxtKeyTyped
        char car = evt.getKeyChar();
        if("".equals(interesTxt.getText())){
            interesTxt.setText("$ ");
        }
        if(interesTxt.getText().length()>=8){
            evt.consume();
        }
        if ((car <'0' || car>'9')&&(car>'.')){
            evt.consume();
        }
    }//GEN-LAST:event_interesTxtKeyTyped

    private void presioTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_presioTxtKeyTyped
        char car = evt.getKeyChar();
        if("".equals(presioTxt.getText())){
            presioTxt.setText("$ ");
        }
        if(presioTxt.getText().length()>=10){
            evt.consume();
        }
        if ((car <'0' || car>'9')&& (car>'.')){
            evt.consume();
        }
    }//GEN-LAST:event_presioTxtKeyTyped

    private void cursoTxtKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_cursoTxtKeyTyped
        // TODO add your handling code here:
        char c=evt.getKeyChar();
        if(cursoTxt.getText().length()>=40){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)&&!Character.isSpaceChar(c)) {
            evt.consume();
        }
    }//GEN-LAST:event_cursoTxtKeyTyped

    @SuppressWarnings("unchecked")
    private void txtbuscarcursoKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtbuscarcursoKeyTyped
        // TODO add your handling code here:
        int c=evt.getKeyChar();
        if(txtbuscarcurso.getText().length()>=20){
            evt.consume();
        }
        if(!Character.isAlphabetic(c)&&!Character.isSpaceChar(c)){
            evt.consume();
        }
        txtbuscarcurso.addKeyListener(new KeyAdapter() {
        @Override
        public void keyReleased(final KeyEvent e) {
            String cadena = (txtbuscarcurso.getText().toUpperCase());
            txtbuscarcurso.setText(cadena);
            repaint();
            filtro();
        }
        });
        trsFiltro = new TableRowSorter(tableCurso.getModel());
        tableCurso.setRowSorter(trsFiltro);
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscarcurso.getText().toUpperCase()));
    }//GEN-LAST:event_txtbuscarcursoKeyTyped
    
    private TableRowSorter trsFiltro; 
    @SuppressWarnings("unchecked")
    public void filtro() {
        int columnaABuscar=0;
        trsFiltro.setRowFilter(RowFilter.regexFilter(txtbuscarcurso.getText(), columnaABuscar));
    }
    
    private void txtbuscarcursoFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_txtbuscarcursoFocusGained
        // TODO add your handling code here:
        limpiarcurso();
        limpiarbotones();
        cmbAula.setSelectedIndex(0);
    }//GEN-LAST:event_txtbuscarcursoFocusGained

    private void btnquitarprofeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnquitarprofeActionPerformed
        // TODO add your handling code here:
        int op=JOptionPane.showConfirmDialog(this, "Esta seguro de quitar al profesor ".concat(nombreTxt.getText().concat(" ").concat(apellidoTxt.getText())).concat(" del curso ").concat(cursoTxt.getText()).concat("?"));
        switch(op){
            case 0:
                try{
                    clase.Curso.borrarCursoProfesor(conex, 0, idcurso);
                    mostrarCursos();
                    mostrarCursosSinProf();
                    limpiar();
                    limpiarbotones();
                    limpiarespecial();
                }
                catch(Exception e){
                    JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                }
                break;
            case 1:
                cursoTxt.requestFocus(true);
                break;
            default:
                limpiar();
                limpiarbotones();
                limpiarespecial();
                break;
        }
    }//GEN-LAST:event_btnquitarprofeActionPerformed

    private void txtprecioinscKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtprecioinscKeyTyped
        // TODO add your handling code here:
        char car = evt.getKeyChar();
        if("".equals(txtprecioinsc.getText())){
            txtprecioinsc.setText("$ ");
        }
        if(txtprecioinsc.getText().length()>=10){
            evt.consume();
        }
        if ((car <'0' || car>'9')&& (car>'.')){
            evt.consume();
        }
    }//GEN-LAST:event_txtprecioinscKeyTyped

    private void QuitarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QuitarItemActionPerformed
        // TODO add your handling code here:
        if (idcurso!=0){
            int op=JOptionPane.showConfirmDialog(this, "Esta seguro de quitar al profesor ".concat(nombreTxt.getText().concat(" ").concat(apellidoTxt.getText())).concat(" del curso ").concat(cursoTxt.getText()).concat("?"));
            switch(op){
                case 0:
                    try{
                        clase.Curso.borrarCursoProfesor(conex, 0, idcurso);
                        mostrarCursos();
                        mostrarCursosSinProf();
                        limpiar();
                        limpiarbotones();
                        limpiarespecial();
                    }
                    catch(Exception e){
                        JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
                    }
                    break;
                case 1:
                    cursoTxt.requestFocus(true);
                    break;
                default:
                    limpiar();
                    limpiarbotones();
                    limpiarespecial();
                    break;
            }
        }
        else{
            JOptionPane.showMessageDialog(this, "No selecciono ningun curso","ERROR",ERROR_MESSAGE);
        }
    }//GEN-LAST:event_QuitarItemActionPerformed

    private void HorarioSinPItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorarioSinPItemActionPerformed
        // TODO add your handling code here:
        abmHorario h=new abmHorario();
        h.cursoHTxt.setText(cursoTxt.getText());
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=h.getSize();
        h.setLocation((desktopSize.width-FrameSize.width)/63,(desktopSize.height-FrameSize.height)/80);
        panelA.add(h);
        this.dispose();
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        h.show();
        h.cursoHTxt.requestFocus(true);
    }//GEN-LAST:event_HorarioSinPItemActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem HorarioItem;
    private javax.swing.JMenuItem HorarioSinPItem;
    private javax.swing.JMenuItem QuitarItem;
    public javax.swing.JTextField apellidoTxt;
    public javax.swing.JTextField aulaTxt;
    private javax.swing.JTextField añosTxt;
    private javax.swing.JButton btnquitarprofe;
    private javax.swing.JButton cancelarBtn;
    private javax.swing.JTextField cantidadTxt;
    private javax.swing.JButton cerrarBtn;
    private javax.swing.JComboBox<String> cmbAula;
    private javax.swing.JButton cursoBtn;
    public javax.swing.JTextField cursoTxt;
    public javax.swing.JTextField dniTxt;
    private javax.swing.JButton editarBtn;
    public com.toedter.calendar.JDateChooser fechaIn;
    private javax.swing.JButton guardarBtn;
    private javax.swing.JTextField horarioTxt;
    private javax.swing.JTextField interesTxt;
    private javax.swing.JButton irBtn;
    public javax.swing.JList<String> jListHorario;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JLabel lblapellido;
    private javax.swing.JLabel lblaños;
    private javax.swing.JLabel lblbuscarcurso;
    private javax.swing.JLabel lblcuotas;
    private javax.swing.JLabel lblcurso;
    private javax.swing.JLabel lblcursos;
    private javax.swing.JLabel lbldni;
    private javax.swing.JLabel lblfechaini;
    private javax.swing.JLabel lblhorario;
    private javax.swing.JLabel lblhorarios;
    private javax.swing.JLabel lblinteres;
    private javax.swing.JLabel lblnombre;
    private javax.swing.JLabel lblprecio;
    private javax.swing.JLabel lblprecioinsc;
    private javax.swing.JLabel lblsincursos;
    public javax.swing.JTextField nombreTxt;
    public javax.swing.JTextField presioTxt;
    private javax.swing.JButton quitarBtn;
    private javax.swing.JTable tablacursosinprof;
    private javax.swing.JTable tableCurso;
    private javax.swing.JTextField txtbuscarcurso;
    private javax.swing.JTextField txtprecioinsc;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmCurso.class.getName());
}
