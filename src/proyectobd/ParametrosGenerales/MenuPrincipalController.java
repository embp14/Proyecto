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
    

    public void call_ListarVendedores(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/VendedoresGui/Lst_Vendedores_Gui.fxml"));
                stage.setTitle("Gestión de vendedores");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
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
            fu.MostrarAlertas("Error del sistema", ex.toString());
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
            fu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    
}
