/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces;
import java.awt.Image;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.ERROR_MESSAGE;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author cytrex
 */
public class abmFeedback extends javax.swing.JInternalFrame {
    boolean band=false,var=false;
    String Receptor=" ";//Correo receptor
    String Asunto="Sistema Y.E.S.";
    String Emisor=" ";//Correo emisor
    String Contraseña=" ";//Contraseña del correo emisor
    String nombre,rutaimagen;
    /*Para que funcione deben activar la casilla permitir "aplicaciones de terceros" en la pestaña seguridad de su cuenta (Gmail).
    Como podria implicar que vulneren la cuenta, se recomienda usar una cuenta que no sea personal y que solo envien reportes
    desde ella. Otra forma es guardar todas las contraseñas incluidas la de la conexion a la base de datos en un archivo log.txt
    dentro de los archivos de configuracion del servidor y leerlas desde alli. O podrian permitir que sea el usuario quien ingrese tanto el correo como
    la contraseña, almacenarlos en una variable y limpiarlas al salir. Personalmente yo considero que al ser una institucion pequeña (Y.E.S.)
    el riesgo es minimo ya que se deberia de al menos acceder fisicamente al computador para ejecutar un script desde un USB; Ademas hemos creado
    este programa para que sea lo mas facil y automatizadamente posible de usar.
    !!!!!ATENCION!!!!!!
    Esta funcionalidad podria dejar de funcionar en cualquier momento debido a que dependemos de terceros, si ellos cambian su politica
    de conxeion deberan ajustarse a sus terminos y condiciones y efectuar los cambios necesarios
    */
    /**
     * Creates new form abmFeedback
     */
    public abmFeedback() {
        initComponents();
        txtarcomentario.setLineWrap(true);//Permite el salto de linea en el text area// se aplica al correo
        txtarcomentario.setWrapStyleWord(true);//Se impide la division de palabras en el text area
    }
    
    public void enviarEmail(){//Esta sera una cuenta de Microsoft. Requiere tener asociado un telefono
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp-mail.outlook.com");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
 
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(Emisor, Contraseña);
                    }
                });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(Emisor));
            message.addRecipients(Message.RecipientType.TO,InternetAddress.parse(Receptor));//Se podria añadir mas de un correo
            message.setSubject(Asunto);
            message.setText(txtarcomentario.getText());
            
            try (Transport trans = session.getTransport("smtp")) {
                trans.connect(Emisor,Contraseña);
                trans.sendMessage(message,message.getAllRecipients());
                trans.close();
                JOptionPane.showMessageDialog(this, "Enviado...\n\n\nGracias por comentar!!!","INFORMATION",INFORMATION_MESSAGE);
            }
        }
        catch (MessagingException e) {
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
//            JOptionPane.showMessageDialog(this, "Compruebe la conexion a internet\n No se pudo enviar el mensaje","WARNING",WARNING_MESSAGE);
        }
    }
    /*CONFIGURACION SEGUN LA PAGINA DE GOOGLE
    POP3
    * Nombre del servidor POP pop.gmail.com
    * Puerto POP 995
    * Metodo de cifrado POP Requiere SSL
    IMAP
    * Nombre del servidor IMAP imap.gmail.com
    * Puerto IMAP 993
    * Metodo de cifrado POP Requiere SSL
    SMTP
    * Nombre del servidor SMTP smtp.gmail.com
    * Puerto 465 (se requiere SSL)
    * Puerto 587 (se requiere TLS)
    * Metodo de cifrado SMTP para TLS/STARTTLS
    */
    /*CONFIGURACION SEGUN LA PAGINA DE MICROSOFT
    Configuración POP, IMAP y SMTP para Outlook.com
    IMAP
    * Nombre del servidor IMAP Outlook.Office365.com
    * Puerto IMAP 993
    * Método de cifrado IMAPTLS
    POP
    * Nombre del servidor POP Outlook.Office365.com
    * Puerto POP 995
    * Método de cifrado POP TLS
    SMPT
    * Nombre del servidor SMTP SMTP.Office365.com
    * Puerto SMTP 587
    * Método de cifrado SMTP STARTTLS
    */
    
    public void sendEmail(){//Esta sera una cuenta de Google. Se puede agregar una contraseña para la aplicacion (permite solo enviar correo y no se puede acceder a la cuenta). Requiere verificacion de 2 pasos
        Properties pro=new Properties();
        //Almacenamos las propiedades de conexion que se necesita//usaremos smpt. Recuerden que si usan imap o pop podrian necesitar otra libreria
        pro.put("mail.smtp.host", "smtp.gmail.com");//servidor
        pro.put("mail.smtp.starttls.enable","true");//cifrado
        pro.put("mail.smtp.port", 587);//puerto
        pro.put("mail.smtp.mail.sender",Emisor);
        pro.put("mail.smtp.password", Contraseña);
        pro.put("mail.smtp.recepter", Receptor);
        pro.put("mail.smtp.auth", "true");//validamos la autenticacion
        Session sesion=Session.getDefaultInstance(pro);//La session captura todo las propiedades creadas
        
        try{
            BodyPart comentario = new MimeBodyPart();
            comentario.setText(txtarcomentario.getText());
            BodyPart adjunto = new MimeBodyPart();
            adjunto.setDataHandler(new DataHandler(new FileDataSource(rutaimagen)));
            adjunto.setFileName(nombre);
            MimeMultipart multiParte = new MimeMultipart();
            multiParte.addBodyPart(comentario);
            multiParte.addBodyPart(adjunto);
            
            MimeMessage mensaje=new MimeMessage(sesion);//Aqui se forma el mensaje que se va a enviar.Se introduce al constructor de la session al mensaje que vamos a enviar
            mensaje.setFrom(new InternetAddress((String)pro.get("mail.smtp.mail.sender")));//Recive la direccion del emisor del mensaje de tipo Innternet Address
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress((String)pro.get("mail.smtp.recepter")));//Receptor del mensaje. Cambiando .TO se puede eviar a mas de un receptor
            mensaje.setSubject(Asunto);//El asunto hara que Gmail tome a los mensajes como una linea de tiempo. Apareceran en el mismo asunto
            mensaje.setContent(multiParte);
            try (Transport t = sesion.getTransport("smtp")) {//Se encarga de establecer la conexion
                t.connect((String)pro.get("mail.smtp.mail.sender"),(String)pro.get("mail.smtp.password"));//Conectamos al emisor
                t.sendMessage(mensaje, mensaje.getAllRecipients());//Direeccion de correo al que se envia el mensaje
                t.close();//Cerramos la conexion
                JOptionPane.showMessageDialog(this, "Enviado...\n\n\nGracias por comentar!!!","INFORMATION",INFORMATION_MESSAGE);
                band=true;
            }
        }
        catch(MessagingException e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }
    
    public void enviarTexto(){
        Properties pro=new Properties();
        pro.put("mail.smtp.host", "smtp.gmail.com");
        pro.put("mail.smtp.starttls.enable","true");
        pro.put("mail.smtp.port", 587);
        pro.put("mail.smtp.mail.sender",Emisor);
        pro.put("mail.smtp.password", Contraseña);
        pro.put("mail.smtp.recepter", Receptor);
        pro.put("mail.smtp.auth", "true");
        Session sesion=Session.getDefaultInstance(pro);
        
        try{
            MimeMessage mensaje=new MimeMessage(sesion);
            mensaje.setFrom(new InternetAddress((String)pro.get("mail.smtp.mail.sender")));
            mensaje.addRecipient(Message.RecipientType.TO, new InternetAddress((String)pro.get("mail.smtp.recepter")));
            mensaje.setSubject(Asunto);
            mensaje.setText(txtarcomentario.getText());
            try (Transport t = sesion.getTransport("smtp")) {
                t.connect((String)pro.get("mail.smtp.mail.sender"),(String)pro.get("mail.smtp.password"));
                t.sendMessage(mensaje, mensaje.getAllRecipients());
                t.close();
                JOptionPane.showMessageDialog(this, "Enviado...\n\n\nGracias por comentar!!!","INFORMATION",INFORMATION_MESSAGE);
                band=true;
            }
        }
        catch(MessagingException e){
            JOptionPane.showMessageDialog(this, e.getLocalizedMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblcomentario = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtarcomentario = new javax.swing.JTextArea();
        lblimagen = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btnenviar = new javax.swing.JButton();
        btncancelar = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setTitle("Centro de Comentarios");

        lblcomentario.setText("Envíanos tus comentarios para mejorar la aplicación.");

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)), "Comentario", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(102, 102, 102))); // NOI18N

        txtarcomentario.setColumns(20);
        txtarcomentario.setFont(new java.awt.Font("Arial", 0, 12)); // NOI18N
        txtarcomentario.setRows(5);
        jScrollPane1.setViewportView(txtarcomentario);

        lblimagen.setForeground(new java.awt.Color(102, 102, 102));
        lblimagen.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lblimagen.setText("Haz clic para buscar una imagen");
        lblimagen.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));
        lblimagen.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblimagenMouseClicked(evt);
            }
        });

        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Imagen");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 340, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblimagen, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblimagen, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        btnenviar.setText("Enviar");
        btnenviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnenviarActionPerformed(evt);
            }
        });

        btncancelar.setText("Cancelar");
        btncancelar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btncancelarActionPerformed(evt);
            }
        });

        jLabel1.setText("Reporta cualquier tipo de error para mejorar la aplicacíon. No se recolectara ningun tipo de informacíon personal");

        jLabel2.setText("Si deseas una respuesta incluya por favor un medio de contacto e-mail, telefono, etc.");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(lblcomentario)
                            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel2))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnenviar)
                        .addGap(18, 18, 18)
                        .addComponent(btncancelar)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblcomentario)
                .addGap(18, 18, 18)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnenviar)
                    .addComponent(btncancelar))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btncancelarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btncancelarActionPerformed
        // TODO add your handling code here:
        this.dispose();
    }//GEN-LAST:event_btncancelarActionPerformed

    private void btnenviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnenviarActionPerformed
        // TODO add your handling code here:
        if(txtarcomentario.getText().isEmpty()){
            JOptionPane.showMessageDialog(this,"Nada que reportar?","ERROR",ERROR_MESSAGE);
            txtarcomentario.requestFocus(true);
        }
        else{
            if(var==false){
                enviarTexto();
//                enviarEmail();
            }
            else if(var==true){
                sendEmail();
            }
            txtarcomentario.setText("");
            if(band==false){
                JOptionPane.showMessageDialog(this, "Hubo un error...\n\n\nNo pudimos enviar el mensaje","WARNING",WARNING_MESSAGE);
            }
            this.dispose();
        }
    }//GEN-LAST:event_btnenviarActionPerformed

    private void lblimagenMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblimagenMouseClicked
        // TODO add your handling code here:
        lblimagen.setText("");
        JFileChooser archivo=new JFileChooser();
        archivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("Formatos de Imagen (*.jpeg;*.png;*.jpg)","jpeg","png","jpg");
        archivo.addChoosableFileFilter(filtro);
        archivo.setFileFilter(filtro);
        int ventana=archivo.showOpenDialog(this);
        if(ventana==JFileChooser.APPROVE_OPTION){
            File file=archivo.getSelectedFile();
            String Orig=file.getPath();
            Path Origen=Paths.get(Orig);
            rutaimagen=String.valueOf(Origen);
            nombre=file.getName();
            Image foto=getToolkit().createImage(file.toString());
            foto = foto.getScaledInstance(159,149,Image.SCALE_DEFAULT);
            lblimagen.setIcon(new ImageIcon(foto));
            var=true;
        }
        else{
            if(var==false){
                lblimagen.setText("Haz clic para buscar una imagen");
            }
        }
    }//GEN-LAST:event_lblimagenMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btncancelar;
    private javax.swing.JButton btnenviar;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblcomentario;
    private javax.swing.JLabel lblimagen;
    public javax.swing.JTextArea txtarcomentario;
    // End of variables declaration//GEN-END:variables
    private static final Logger LOG = Logger.getLogger(abmFeedback.class.getName());
}
