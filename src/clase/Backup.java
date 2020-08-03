/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clase;
import conexion.Conexion;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.INFORMATION_MESSAGE;
import javax.swing.filechooser.FileNameExtensionFilter;
/**
 *
 * @author cytrex
 */
public class Backup {
    private static final Logger LOG = Logger.getLogger(Backup.class.getName());
    Connection cnx=Conexion.conectar();
    Date hoy=new Date();
    SimpleDateFormat fecha=new SimpleDateFormat("dd-MM-yyyy");
    String sfecha;
    String base="YES";
    String user="root";
    String psw="root";
    String cmd="null";
    
    public void generarBackup(Connection conexion){
        JFileChooser archivo=new JFileChooser();//Creamos el objeto JFileChooser
        archivo.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);//Indicamos que puede acceder a archivos y directorios
        FileNameExtensionFilter filtro=new FileNameExtensionFilter("Archivos SQL","sql");//Extension de archivos que se guardara
//        archivo.setFileFilter(filtro);//Añadimos el filtro
        archivo.addChoosableFileFilter(filtro);//Añadimos por defecto la extension del archivo.De lo contrario por defecto es todos los archivos
        sfecha=fecha.format(hoy);
        int seleccion;
        seleccion = archivo.showSaveDialog(null);//Abrimos la ventana de "guardar"
        if(seleccion==JFileChooser.APPROVE_OPTION){//Comprobamos si hace click en aceptar
            File archivoBD=archivo.getSelectedFile();//Comprobamos si se selecciono el archivo//No es necesario. Ambas realizan la misma funcion
            String ruta=archivoBD.getAbsolutePath();//Obtenemos la direccion absoluta del archivo.Aunque contains en "if" requiere de un string
            if(!ruta.contains(".sql")){//Si la ruta del archivo creado no contiene ".sql" le añadimos dicha extension
                ruta=ruta.concat(".sql");//Le añadimos .sql al archivo guardado
                try{
                    //cmd = "mysqldump -u "+user+" -p"+psw+" --add-drop-database --force --extended-insert --create-options --databases "+base+" -r"+ruta;
                    cmd="C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u "+user+" -p"+psw+" --add-drop-database --force --extended-insert --create-options --databases "+base+" -r"+ruta;
                    Runtime proceso=Runtime.getRuntime();//Proceso que ejecuta el guardado de la BASE DE DATOS
                    Process child=proceso.exec(cmd);//Se corre bajo la consola
                    JOptionPane.showMessageDialog(null, "Copia de seguridad creada", "INFORMATION", INFORMATION_MESSAGE);
                }
                catch (IOException ex){
                    JOptionPane.showInternalMessageDialog(null, ex.getLocalizedMessage());
                }
            }
            else{//En caso de que el usuario escriba el nombre del archivo + la extension ".sql"
                try{
                    cmd="C:\\Program Files\\MySQL\\MySQL Server 8.0\\bin\\mysqldump -u "+user+" -p"+psw+" --add-drop-database --force --extended-insert --create-options --databases "+base+" -r"+ruta;
                    Runtime proceso=Runtime.getRuntime();
                    Process child=proceso.exec(cmd);
                    JOptionPane.showMessageDialog(null, "Copia de seguridad creada", "INFORMATION", INFORMATION_MESSAGE);
                }
                catch(IOException ex){
                    JOptionPane.showInternalMessageDialog(null, ex.getLocalizedMessage());
                }
            }
        }   
    }
}
