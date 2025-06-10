/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyectobd.ParametrosGenerales;

import dao.ConectorBD;
import dao.UsuarioDAO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class MenuPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    //Instanciamos FeedbackUsuario para mostrar las alertas
    FeedbackUsuario fu=new FeedbackUsuario();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void call_ProbarConexionBD(){
        //Creamos la instancia de ConectorBD
        ConectorBD ConnBD=new ConectorBD();
        // Ejecutamos el método Abrir Conexión
        ConnBD.AbrirConexionBD();
        // Ejecutamos el método Cerrar Conexión
        ConnBD.CerrarConexionBD();
        
    }
    
    public void call_ListarUsuarios(){
        try{
                // Genero el stage
                Stage stage_Lst_Usuarios=new Stage();
                //Cargamos la interface gráfica para la lista de usuarios:
                Parent root=FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Lst_Usuarios_Gui.fxml"));
                // Establecemos el título de la ventana
                stage_Lst_Usuarios.setTitle("Gestión de usuarios del sistema");
                //Establecemos la escena que se mostrará en el escenario
                stage_Lst_Usuarios.setScene(new Scene(root));
                //Mostramos el escenario
                stage_Lst_Usuarios.show();
        } catch (Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    
}
