/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyectobd.ParametrosGenerales;

import dao.ConectorBD;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackOrden;
import proyectobd.ParametrosGenerales.FeedbackResena;
import proyectobd.ParametrosGenerales.FeedbackVendedor;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

/**
 * FXML Controller class
 *
 * @author admin
 */
public class MenuPrincipalController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
    // Instanciamos feedback específicos para mostrar alertas
    FeedbackVendedor fv = new FeedbackVendedor();
    FeedbackOrden fo = new FeedbackOrden();
    FeedbackResena fr = new FeedbackResena();
    FeedbackUsuario fu = new FeedbackUsuario();
    
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
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Lst_Usuarios_Gui.fxml"));
                stage.setTitle("Gestión de usuarios");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    

    public void call_ListarVendedores(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/VendedoresGui/Lst_Vendedores_Gui.fxml"));
                stage.setTitle("Gestión de vendedores");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fv.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarOrdenes(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OrdenesGui/Lst_Ordenes_Gui.fxml"));
                stage.setTitle("Gestión de órdenes");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fo.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarResenas(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ResenasGui/Lst_Resenas_Gui.fxml"));
                stage.setTitle("Gestión de reseñas");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fr.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    
}
