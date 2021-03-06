/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
import clase.Backup;
import conexion.Conexion;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Dimension;
import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.plaf.basic.BasicMenuBarUI;
/**
 *
 * @author joni
 */
public class abmPrincipal extends javax.swing.JFrame {
    Connection cnx=Conexion.conectar();
    SimpleDateFormat mostrarfecha = new SimpleDateFormat("EEEEE',' dd 'de' MMMM 'de' yyyy");
    Date tiempo=new Date();
    /**
     * Creates new form frmPrincipal
     */
    public abmPrincipal() {
//        this.hora.getHour();
//        this.minutos.getMinute();
//        this.segundos.getSecond();
        initComponents();
        jMenuBar1.setOpaque(true); 
        jMenuBar1.setUI(new BasicMenuBarUI(){
            @Override
            public void paint(Graphics g, JComponent c){
                   g.setColor(new java.awt.Color(33,33,33));
                   g.fillRect(0,0,c.getWidth(), c.getHeight());
                }
        });
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel4 = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        inscribirBtn = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        cuotaBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator4 = new javax.swing.JSeparator();
        jSeparator5 = new javax.swing.JSeparator();
        panelA = new javax.swing.JPanel();
        lblcalendario = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        editMenu = new javax.swing.JMenu();
        AlumnoItem = new javax.swing.JMenuItem();
        ProfesorItem = new javax.swing.JMenuItem();
        CursoItem = new javax.swing.JMenuItem();
        RegistroItem = new javax.swing.JMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        SalirItem = new javax.swing.JMenuItem();
        NavegarMenu = new javax.swing.JMenu();
        InscribirItem = new javax.swing.JMenuItem();
        PagarItem = new javax.swing.JMenuItem();
        PagarInsItem = new javax.swing.JMenuItem();
        HorarioItem = new javax.swing.JMenuItem();
        BackupMenu = new javax.swing.JMenu();
        AyudaMenu = new javax.swing.JMenu();
        AccesoItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        ManualItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        InformacionItem = new javax.swing.JMenuItem();
        ComMenu = new javax.swing.JMenu();
        ComentariosItem = new javax.swing.JMenuItem();
        Salir = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Sistema YES");
        setIconImage(getIconImage());
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 0, 0), 5));
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        org.jdesktop.swingx.border.DropShadowBorder dropShadowBorder1 = new org.jdesktop.swingx.border.DropShadowBorder();
        dropShadowBorder1.setShadowColor(new java.awt.Color(153, 0, 0));
        dropShadowBorder1.setShowBottomShadow(false);
        dropShadowBorder1.setShowRightShadow(false);
        jPanel1.setBorder(dropShadowBorder1);

        inscribirBtn.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        inscribirBtn.setForeground(new java.awt.Color(0, 119, 141));
        inscribirBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/notas.png"))); // NOI18N
        inscribirBtn.setText("INSCRIBIR");
        inscribirBtn.setBorder(null);
        inscribirBtn.setBorderPainted(false);
        inscribirBtn.setContentAreaFilled(false);
        inscribirBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        inscribirBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        inscribirBtn.setIconTextGap(2);
        inscribirBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        inscribirBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        inscribirBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                inscribirBtnActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tw Cen MT Condensed Extra Bold", 0, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(204, 0, 0));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jLabel1.setText(" Y.E.S");

        cuotaBtn.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
        cuotaBtn.setForeground(new java.awt.Color(0, 119, 141));
        cuotaBtn.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/beca.png"))); // NOI18N
        cuotaBtn.setText("PAGAR ");
        cuotaBtn.setBorder(null);
        cuotaBtn.setBorderPainted(false);
        cuotaBtn.setContentAreaFilled(false);
        cuotaBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        cuotaBtn.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        cuotaBtn.setIconTextGap(2);
        cuotaBtn.setVerticalAlignment(javax.swing.SwingConstants.BOTTOM);
        cuotaBtn.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        cuotaBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cuotaBtnActionPerformed(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/argentina.png"))); // NOI18N

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/reino-unido.png"))); // NOI18N

        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/USA.png"))); // NOI18N

        jSeparator2.setBackground(new java.awt.Color(204, 0, 0));

        jSeparator4.setBackground(new java.awt.Color(204, 0, 0));

        jSeparator5.setBackground(new java.awt.Color(204, 0, 0));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
            .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
            .addComponent(inscribirBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(cuotaBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 90, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(inscribirBtn)
                .addGap(18, 18, 18)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cuotaBtn)
                .addGap(18, 18, 18)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 59, Short.MAX_VALUE)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        jPanel4.add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(15, 15, 90, 670));

        panelA.setBackground(new java.awt.Color(255, 255, 255));
        panelA.setPreferredSize(new java.awt.Dimension(1040, 675));

        javax.swing.GroupLayout panelALayout = new javax.swing.GroupLayout(panelA);
        panelA.setLayout(panelALayout);
        panelALayout.setHorizontalGroup(
            panelALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 950, Short.MAX_VALUE)
        );
        panelALayout.setVerticalGroup(
            panelALayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 675, Short.MAX_VALUE)
        );

        jPanel4.add(panelA, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 10, 950, 675));

        lblcalendario.setForeground(new java.awt.Color(153, 153, 153));
        jPanel4.add(lblcalendario, new org.netbeans.lib.awtextra.AbsoluteConstraints(1080, 10, 210, 15));

        jMenuBar1.setBackground(new java.awt.Color(255, 255, 255));
        jMenuBar1.setBorder(null);

        editMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        editMenu.setForeground(new java.awt.Color(204, 204, 204));
        editMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/paste.png"))); // NOI18N
        editMenu.setMnemonic('e');
        editMenu.setText("Archivo");
        editMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        AlumnoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F1, 0));
        AlumnoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/friends_group.png"))); // NOI18N
        AlumnoItem.setMnemonic('t');
        AlumnoItem.setText("Alumno");
        AlumnoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AlumnoItemActionPerformed(evt);
            }
        });
        editMenu.add(AlumnoItem);

        ProfesorItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F2, 0));
        ProfesorItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/user_woman.png"))); // NOI18N
        ProfesorItem.setMnemonic('y');
        ProfesorItem.setText("Profesor");
        ProfesorItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ProfesorItemActionPerformed(evt);
            }
        });
        editMenu.add(ProfesorItem);

        CursoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F3, 0));
        CursoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/book.png"))); // NOI18N
        CursoItem.setMnemonic('p');
        CursoItem.setText("Curso");
        CursoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CursoItemActionPerformed(evt);
            }
        });
        editMenu.add(CursoItem);

        RegistroItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F11, 0));
        RegistroItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/copy-1.png"))); // NOI18N
        RegistroItem.setText("Registros");
        RegistroItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RegistroItemActionPerformed(evt);
            }
        });
        editMenu.add(RegistroItem);
        editMenu.add(jSeparator7);

        SalirItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F12, 0));
        SalirItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete.png"))); // NOI18N
        SalirItem.setText("Salir");
        SalirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalirItemActionPerformed(evt);
            }
        });
        editMenu.add(SalirItem);

        jMenuBar1.add(editMenu);

        NavegarMenu.setForeground(new java.awt.Color(204, 204, 204));
        NavegarMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/document_file.png"))); // NOI18N
        NavegarMenu.setText("Navegar");
        NavegarMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        InscribirItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, 0));
        InscribirItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/credit_card.png"))); // NOI18N
        InscribirItem.setText("Inscribir");
        InscribirItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InscribirItemActionPerformed(evt);
            }
        });
        NavegarMenu.add(InscribirItem);

        PagarItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, 0));
        PagarItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ic_monetization_on_black_18dp.png"))); // NOI18N
        PagarItem.setText("Pagar");
        PagarItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarItemActionPerformed(evt);
            }
        });
        NavegarMenu.add(PagarItem);

        PagarInsItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F6, 0));
        PagarInsItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ic_attach_money_black_18dp.png"))); // NOI18N
        PagarInsItem.setText("Pagar Inscripción");
        PagarInsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PagarInsItemActionPerformed(evt);
            }
        });
        NavegarMenu.add(PagarInsItem);

        HorarioItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F7, 0));
        HorarioItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/time_clock.png"))); // NOI18N
        HorarioItem.setText("Horarios");
        HorarioItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HorarioItemActionPerformed(evt);
            }
        });
        NavegarMenu.add(HorarioItem);

        jMenuBar1.add(NavegarMenu);

        BackupMenu.setForeground(new java.awt.Color(204, 204, 204));
        BackupMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/save_diskette_floppy_disk.png"))); // NOI18N
        BackupMenu.setText("Backup");
        BackupMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        BackupMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BackupMenuMouseClicked(evt);
            }
        });
        jMenuBar1.add(BackupMenu);

        AyudaMenu.setBackground(new java.awt.Color(51, 51, 51));
        AyudaMenu.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(255, 255, 255)));
        AyudaMenu.setForeground(new java.awt.Color(204, 204, 204));
        AyudaMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/help.png"))); // NOI18N
        AyudaMenu.setText("Ayuda");
        AyudaMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        AccesoItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F8, 0));
        AccesoItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/forward.png"))); // NOI18N
        AccesoItem.setText("Acceso Directo");
        AccesoItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AccesoItemActionPerformed(evt);
            }
        });
        AyudaMenu.add(AccesoItem);
        AyudaMenu.add(jSeparator1);

        ManualItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F9, 0));
        ManualItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/lamp_active.png"))); // NOI18N
        ManualItem.setText("Manual");
        ManualItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ManualItemActionPerformed(evt);
            }
        });
        AyudaMenu.add(ManualItem);
        AyudaMenu.add(jSeparator6);

        InformacionItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F10, 0));
        InformacionItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/information.png"))); // NOI18N
        InformacionItem.setText("Acerca de...");
        InformacionItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InformacionItemActionPerformed(evt);
            }
        });
        AyudaMenu.add(InformacionItem);

        jMenuBar1.add(AyudaMenu);

        ComMenu.setForeground(new java.awt.Color(204, 204, 204));
        ComMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/comment.png"))); // NOI18N
        ComMenu.setText("Feedback");
        ComMenu.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        ComentariosItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F5, java.awt.event.InputEvent.ALT_MASK));
        ComentariosItem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/ic_comment_black_18dp.png"))); // NOI18N
        ComentariosItem.setText("Comentarios");
        ComentariosItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ComentariosItemActionPerformed(evt);
            }
        });
        ComMenu.add(ComentariosItem);

        jMenuBar1.add(ComMenu);

        Salir.setForeground(new java.awt.Color(204, 204, 204));
        Salir.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/close_delete.png"))); // NOI18N
        Salir.setText("Salir");
        Salir.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        Salir.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                SalirMouseClicked(evt);
            }
        });
        jMenuBar1.add(Salir);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 1300, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, 726, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    @Override
    public Image getIconImage() {//Icono del sistema
        Image retValue = Toolkit.getDefaultToolkit().getImage(ClassLoader.getSystemResource("imagenes/output.png"));
        return retValue;
    }
    private void inscribirBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_inscribirBtnActionPerformed
        panelA.removeAll();
        abmInscripcion registrar=new abmInscripcion();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=registrar.getSize();
        registrar.setLocation((desktopSize.width-FrameSize.width)/50,(desktopSize.height-FrameSize.height)/10);
        panelA.add(registrar);
        //Ubicacion del formulario rspecto al panelA
        Animacion.Animacion.mover_derecha(0 , 110, 5, 5, panelA);
        //Animacion(reordenar al traer un nuevo panel como el de la fecha,derecha,desplazamiento,velocidad,lugar)
        registrar.show();
        registrar.dniTxt.requestFocus(true);
    }//GEN-LAST:event_inscribirBtnActionPerformed

    private void cuotaBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cuotaBtnActionPerformed
        panelA.removeAll();
        abmPagar pa=new abmPagar();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pa.getSize();
        pa.setLocation((desktopSize.width-FrameSize.width)/16,(desktopSize.height-FrameSize.height)/2);
        panelA.add(pa);
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pa.show();
//        pa.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_cuotaBtnActionPerformed

    private void AlumnoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AlumnoItemActionPerformed
        panelA.removeAll();
        abmAlumno al=new abmAlumno();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=al.getSize();
        al.setLocation((desktopSize.width-FrameSize.width)/25,(desktopSize.height-FrameSize.height)/35);
        panelA.add(al);
        Animacion.Animacion.mover_derecha(0 , 110, 5, 5, panelA);
        al.show();
        al.nombreTxt.requestFocus(true);
        //try {
            //al.setMaximum(true);
        /*} catch (PropertyVetoException ex) {
            Logger.getLogger(abmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }*/
    }//GEN-LAST:event_AlumnoItemActionPerformed

    private void ProfesorItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ProfesorItemActionPerformed
        panelA.removeAll();
        abmProfesor pr=new abmProfesor();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pr.getSize();
        pr.setLocation((desktopSize.width-FrameSize.width)/20,(desktopSize.height-FrameSize.height)/40);
        panelA.add(pr);
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pr.show();
        pr.nombreTxt.requestFocus(true);
    }//GEN-LAST:event_ProfesorItemActionPerformed
    
    private void CursoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CursoItemActionPerformed
        panelA.removeAll();
        abmCurso curs=new abmCurso();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=curs.getSize();
        curs.setLocation((desktopSize.width-FrameSize.width)/30,(desktopSize.height-FrameSize.height)/7);
        panelA.add(curs);
        Animacion.Animacion.mover_derecha(0 , 110, 5, 5, panelA);
        curs.show();
        curs.dniTxt.requestFocus(true);
    }//GEN-LAST:event_CursoItemActionPerformed

    private void SalirMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalirMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_SalirMouseClicked

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        this.setLocationRelativeTo(this);
        this.setExtendedState(MAXIMIZED_BOTH);
        this.getContentPane().setBackground(Color.darkGray);
        jPanel4.setBackground(Color.DARK_GRAY);
        panelA.setBackground(Color.DARK_GRAY);
        lblcalendario.setText(String.valueOf(mostrarfecha.format(tiempo)));
    }//GEN-LAST:event_formWindowOpened

    private void AccesoItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AccesoItemActionPerformed
        // TODO add your handling code here:
        JOptionPane.showMessageDialog(rootPane, "ATAJOS DE TECLADO\n"
            + "Menu de Opciones\n"
            + "F1 = Registrar Alumno \n"
            + "F2 = Registrar Profesor\n"
            + "F3 = Registrar Curso\n"
            + "F4 = Inscribir Alumno\n"
            + "F5 = Pagar Cuota\n"
            + "F6 = Pagar Inscripcion\n"
            + "F7 = Agregar Horarios\n"
            + "F8 = Teclas de Acceso Directo\n"
            + "F9 = Manual de Usuario\n"
            + "F10 = Acerca del Sistema\n"
            + "F11 = Editar Inscripcion\n"
            + "F12 = Salir"
        );
    }//GEN-LAST:event_AccesoItemActionPerformed

    private void InformacionItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InformacionItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        panelA.repaint();
        abmInformacion info=new abmInformacion();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=info.getSize();
        info.setLocation((desktopSize.width-FrameSize.width)/2,(desktopSize.height-FrameSize.height)/2);
        info.show();
//        info.setVisible(true);
        panelA.add(info);
    }//GEN-LAST:event_InformacionItemActionPerformed

    private void BackupMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BackupMenuMouseClicked
        // TODO add your handling code here:
        Backup bp=new Backup();
        JOptionPane.showMessageDialog(this, "Se recomienda que guarde todos los cambios y cierre\n todos los formularios antes de proceder","WARNING",WARNING_MESSAGE);
        int op=JOptionPane.showConfirmDialog(this,"Esta por generar un respaldo de los datos?");
        switch(op){
            case 0:
                bp.generarBackup(cnx);
                break;
            case 1:
                break;
        }
    }//GEN-LAST:event_BackupMenuMouseClicked

    private void SalirItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalirItemActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_SalirItemActionPerformed

    private void InscribirItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InscribirItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmInscripcion registrar=new abmInscripcion();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=registrar.getSize();
        registrar.setLocation((desktopSize.width-FrameSize.width)/50,(desktopSize.height-FrameSize.height)/10);
        panelA.add(registrar);
        Animacion.Animacion.mover_derecha(0 , 110, 5, 5, panelA);
        registrar.show();
        registrar.dniTxt.requestFocus(true);
    }//GEN-LAST:event_InscribirItemActionPerformed

    private void PagarItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmPagar pa=new abmPagar();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=pa.getSize();
        pa.setLocation((desktopSize.width-FrameSize.width)/16,(desktopSize.height-FrameSize.height)/2);
        panelA.add(pa);
        Animacion.Animacion.mover_derecha(0 , 105, 5, 5, panelA);
        pa.show();
    }//GEN-LAST:event_PagarItemActionPerformed

    private void ManualItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ManualItemActionPerformed
        // TODO add your handling code here:
        try {
            //Direccion Absoluta del archivo cuando el archivo se encuentra fuera del sistema en el equipo/Se accede desde el path donde este el archivo.Si el archivo se mueve o renombra no lo entrara el sistema
            //Direccion Realitva del archivo cuando el archivo se encuentra dentro del proyecto o sistema/Se puede acceder anteponiendo src/ruta
//            File pdf=new File ("src/manual/manual-de-usuario.pdf");//Buscamos el archivo//Solo funciona con el IDE no funciona al crear el ejecutable
//            Desktop.getDesktop().open(pdf);//Usamos el programa predeterminado que tiene asignado el sistema operativo
            //Se puede cambiar la ruta por una mas permanente
            File temp=new File(System.getProperty("java.io.tmpdir")+"um-tmp.pdf");//Accedemos y guardamos nuestro manual (pdf) como archivo temporal del sistema
            FileOutputStream salida;
            try (InputStream entrada = this.getClass().getResourceAsStream("/manual/manual-de-usuario.pdf")) {//Creamos un flujo de entrada el cual lo cargaremos con el pdf de nuestro proyecto
                salida = new FileOutputStream(temp);//Creamos un flujo de salida para poder escribir sobre el fichero temporal
                try (FileWriter pdf = new FileWriter(temp)) {//Cargamos de informacion al pdf temporal con el pdf del proyecto
                    byte[] buffer=new byte[1024*512];//Creamos un arreglo generico que soporte la informacion 1kB*512B se usa para todo tipo de archivos
                    int controlbuffer;//Controlamos la informacion de bytes
                    while((controlbuffer=entrada.read(buffer))!=-1){//Mientras haya bytes por leer se ejecutara el ciclo
                        salida.write(buffer,0,controlbuffer);//Escribimos sobre el archivo temporal los bytes leidos
                    }
                }
            }
            salida.close();//cerramos los flujos
            Desktop.getDesktop().open(temp);//Abrimos el archivo temporal creado
            //Dependiendo de la configuracion del sistema, los archivos temporales se borraran o no al apagar el equipo
            //Acceder a la carpeta temporal en SO linux es diferente "/tmp" en linux. Este SO elimina los ficheros al apagar el equipo
            //Los archivos temporales se crearan segun se necesite
        }
        catch (IOException ex) {
            JOptionPane.showMessageDialog(null, ex.getLocalizedMessage());
        }
    }//GEN-LAST:event_ManualItemActionPerformed

    private void HorarioItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HorarioItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmHorario h=new abmHorario();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=h.getSize();
        h.setLocation((desktopSize.width-FrameSize.width)/63,(desktopSize.height-FrameSize.height)/80);
        panelA.add(h);
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        h.show();
        h.cursoHTxt.requestFocus(true);
    }//GEN-LAST:event_HorarioItemActionPerformed

    private void RegistroItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RegistroItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmRegistros corregir=new abmRegistros();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=corregir.getSize();
        corregir.setLocation((desktopSize.width-FrameSize.width)/14,(desktopSize.height-FrameSize.height)/16);
        panelA.add(corregir);
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        corregir.show();
        corregir.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_RegistroItemActionPerformed

    private void PagarInsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PagarInsItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmEstado estado=new abmEstado();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=estado.getSize();
        estado.setLocation((desktopSize.width-FrameSize.width)/27,(desktopSize.height-FrameSize.height)/15);
        panelA.add(estado);
        Animacion.Animacion.mover_derecha(0, 110, 5, 5, panelA);
        estado.show();
        estado.txtbuscar.requestFocus(true);
    }//GEN-LAST:event_PagarInsItemActionPerformed

    private void ComentariosItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ComentariosItemActionPerformed
        // TODO add your handling code here:
        panelA.removeAll();
        abmFeedback com=new abmFeedback();
        Dimension desktopSize=panelA.getSize();
        Dimension FrameSize=com.getSize();
        com.setLocation((desktopSize.width-FrameSize.width)/53,(desktopSize.height-FrameSize.height)/45);
        panelA.add(com);
        Animacion.Animacion.mover_derecha(0, 105, 5, 5, panelA);
        com.show();
//        com.txtarcomentario.requestFocus(true);
    }//GEN-LAST:event_ComentariosItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        try {
                //Traer nueva vista al sistema
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                Logger.getLogger(abmPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        /////////////////////////////////////////VISOR POR DEFAULT////////////////////////////////////////////////////////
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(abmPrincipal.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
        //</editor-fold>
        //</editor-fold>
        
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new abmPrincipal().setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem AccesoItem;
    private javax.swing.JMenuItem AlumnoItem;
    private javax.swing.JMenu AyudaMenu;
    private javax.swing.JMenu BackupMenu;
    private javax.swing.JMenu ComMenu;
    private javax.swing.JMenuItem ComentariosItem;
    private javax.swing.JMenuItem CursoItem;
    private javax.swing.JMenuItem HorarioItem;
    private javax.swing.JMenuItem InformacionItem;
    private javax.swing.JMenuItem InscribirItem;
    private javax.swing.JMenuItem ManualItem;
    private javax.swing.JMenu NavegarMenu;
    private javax.swing.JMenuItem PagarInsItem;
    private javax.swing.JMenuItem PagarItem;
    private javax.swing.JMenuItem ProfesorItem;
    private javax.swing.JMenuItem RegistroItem;
    private javax.swing.JMenu Salir;
    private javax.swing.JMenuItem SalirItem;
    private javax.swing.JButton cuotaBtn;
    private javax.swing.JMenu editMenu;
    private javax.swing.JButton inscribirBtn;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JPanel jPanel1;
    public javax.swing.JPanel jPanel4;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JLabel lblcalendario;
    public static javax.swing.JPanel panelA;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmPrincipal.class.getName());
}
