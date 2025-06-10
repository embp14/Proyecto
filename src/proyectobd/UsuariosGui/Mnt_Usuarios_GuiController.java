/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyectobd.UsuariosGui;

import dao.UsuarioDAO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class Mnt_Usuarios_GuiController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    //Instancio la clase para mostrar las alertas al usuario
    FeedbackUsuario fu=new FeedbackUsuario();
    
    //Bandera para identificar si debo insertar el registro o actualizarlo
    private boolean actualizar =false;
    
    // Anotaciones FXML para referenciar los objetos de la interface gráfica
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Grabar;
    @FXML private AnchorPane Ap_Main;
    @FXML private TextField txt_id;
    @FXML private TextField txt_rolId;
    @FXML private TextField txt_nombre;
    @FXML private TextField txt_email;
    @FXML private PasswordField txt_contrasena;
    @FXML private TextField txt_creadoEn;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
//Platform.runLater permite ejecutar código luego de que la interface se cargue
        Platform.runLater(new Runnable() {
        @Override public void run() {
                //Si es que debo cargar datos, ejecuto el procedimiento de carga de datos
                //esto sirve si estoy editando un registro, para recuperar la información a editar                
                CargarDatos();
                //Deshabilito los controles que no son editables
                // El campo del código no es editable, por lo tanto se deshabilita
                txt_id.setDisable(true);
                //Los campos de auditoría no deben ser editados directamente por el usuario, por lo tanto se deshabilitan
                txt_creadoEn.setDisable(true);
              }
            });        

    }
    
    
   
    public void call_Grabar(){
       
        // Instancio la clase DAO
        UsuarioDAO usrdao=new UsuarioDAO();
                
        //Si actualizar es falso, procedemos a insertar el registro
        if(!actualizar){
            //Insertamos el registro
            try{
                //Preparamos el objeto DTO para transferencia de la información
                UsuarioDTO usrdto =new UsuarioDTO();
                usrdto.setRolId(Integer.parseInt(txt_rolId.getText()));
                usrdto.setNombre(txt_nombre.getText());
                usrdto.setEmail(txt_email.getText());
                usrdto.setContrasena(txt_contrasena.getText());
                //Los campos Crea, FechaCrea, Modifica y FechaModifica se manejan desde el trigger de la base de datos
                // Ejecutamos la insercción
                int idRegistro=usrdao.InsertarUsuario(usrdto);
                if (idRegistro>0){
                    //Establezco el valor del id generado en el objeto usrdto
                    txt_id.setText(Integer.toString(idRegistro));
                    //Aviso al usuario que se generó el registro
                    fu.MostrarAlertas("Información del sistema", "El nuevo usuario se grabó con éxtio, id generado: "+Integer.toString(idRegistro));
                    usrdto.setId(idRegistro);
                    //Deshabilito el botón de grabar para no generar registros duplicados
                    btn_Grabar.setDisable(true);
                }
            }catch (Exception ex){
                // Si ocurre un error lo muestro
                fu.MostrarAlertas("Error del sistema", ex.toString());
            }            
        }else if (actualizar){
            //Si actualizar es verdadero, actualizamos el registro
            //Actualizamos el registro
            // Obtengo el stage del AnchorPane de mi interface gráfica
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            // Recupero el objeto UsuarioDTO que pasé como parámetros desde la pantalla del listado
            UsuarioDTO usrdto =(UsuarioDTO)stage.getUserData();
            //Establezco los nuevos valores a grabar
            usrdto.setRolId(Integer.parseInt(txt_rolId.getText()));
            usrdto.setNombre(txt_nombre.getText());
            usrdto.setEmail(txt_email.getText());
            usrdto.setContrasena(txt_contrasena.getText());
            try{
                int idRegistro=usrdao.ActualizarUsuario(usrdto);
                if (idRegistro>0){
                    //Aviso al usuario que se actualizó el registro
                    fu.MostrarAlertas("Información del sistema", "El usuario se actualizó con éxtio, registros actualizados: "+Integer.toString(idRegistro));
                    //Deshabilito el botón de grabar para no generar registros duplicados
                    btn_Grabar.setDisable(true);
                }
                }catch(Exception ex){
                    // Si ocurre un error lo muestro
                    fu.MostrarAlertas("Error del sistema", ex.toString());
            }
        }
    }

    public void call_Editar(){

    }

    public void call_CerrarVentana(){
        // Obtener el manejador del stage
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        // Cerrar la ventana
        stage.close();
    }

    public void CargarDatos(){
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            UsuarioDTO usrdto =(UsuarioDTO)stage.getUserData();
            try{
                if (usrdto!=null){
                    actualizar=true;
                    txt_id.setText(Integer.toString(usrdto.getId()));
                    txt_rolId.setText(Integer.toString(usrdto.getRolId()));
                    txt_nombre.setText(usrdto.getNombre());
                    txt_email.setText(usrdto.getEmail());
                    txt_contrasena.setText(usrdto.getContrasena());
                    if(usrdto.getCreadoEn()!=null){
                        txt_creadoEn.setText(usrdto.getCreadoEn().toString());
                    }
                }
            }catch (Exception ex){
                fu.MostrarAlertas("Error del sistema", ex.toString());
            }
    }
}
