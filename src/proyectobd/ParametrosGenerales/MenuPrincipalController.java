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
import proyectobd.ParametrosGenerales.FeedbackRol;
import proyectobd.ParametrosGenerales.FeedbackProducto;
import proyectobd.ParametrosGenerales.FeedbackCupon;
import proyectobd.ParametrosGenerales.FeedbackCategoria;
import proyectobd.ParametrosGenerales.FeedbackCarrito;
import proyectobd.ParametrosGenerales.FeedbackDireccion;
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
    FeedbackRol frl = new FeedbackRol();
    FeedbackProducto fpr = new FeedbackProducto();
    FeedbackCupon fcu = new FeedbackCupon();
    FeedbackCategoria fca = new FeedbackCategoria();
    FeedbackCarrito fcar = new FeedbackCarrito();
    FeedbackDireccion fdir = new FeedbackDireccion();
    FeedbackUsuario fu = new FeedbackUsuario();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void call_ProbarConexionBD(){
        //Creamos la instancia de ConectorBD
        ConectorBD ConnBD=new ConectorBD();
        // Ejecutamos el método Abrir Conexión
        if(ConnBD.AbrirConexionBD()!=null){
            fo.MostrarAlertas("Conexión exitosa","Se estableció conexión con la base de datos");
        }else{
            fo.MostrarAlertas("Conexión no exitosa","No se pudo establecer conexión con la base de datos");
        }
        // Ejecutamos el método Cerrar Conexión
        ConnBD.CerrarConexionBD();

    }

    public void call_ListarRoles(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/RolesGui/Lst_Roles_Gui.fxml"));
                stage.setTitle("Gestión de roles");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            frl.MostrarAlertas("Error del sistema", ex.toString());
        }
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

    public void call_ListarProductos(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ProductosGui/Lst_Productos_Gui.fxml"));
                stage.setTitle("Gestión de productos");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fpr.MostrarAlertas("Error", "No se pudo abrir la lista de productos: " + ex.getMessage());
        }
    }

    public void call_ListarCategorias(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CategoriasGui/Lst_Categorias_Gui.fxml"));
                stage.setTitle("Gestión de categorías");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fca.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarCupones(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CuponesGui/Lst_Cupones_Gui.fxml"));
                stage.setTitle("Gestión de cupones");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fcu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarCarritos(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CarritosGui/Lst_Carritos_Gui.fxml"));
                stage.setTitle("Gestión de carritos");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fcar.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarDirecciones(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/DireccionesGui/Lst_Direcciones_Gui.fxml"));
                stage.setTitle("Gestión de direcciones");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fdir.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarVariantes(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/VarianteProductoGui/Lst_VariantesProducto_Gui.fxml"));
                stage.setTitle("Gestión de variantes");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fpr.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarImagenes(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ImagenesProductoGui/Lst_ImagenesProducto_Gui.fxml"));
                stage.setTitle("Gestión de imágenes");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fpr.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarOfertas(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OfertasGui/Lst_Ofertas_Gui.fxml"));
                stage.setTitle("Gestión de ofertas");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fpr.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarCarritoItems(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CarritoItemsGui/Lst_CarritoItems_Gui.fxml"));
                stage.setTitle("Items de carrito");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fcar.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarListasDeseos(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListasDeseosGui/Lst_ListasDeseos_Gui.fxml"));
                stage.setTitle("Listas de deseos");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fv.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarListaItems(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ListaDeseoItemsGui/Lst_ListaDeseoItems_Gui.fxml"));
                stage.setTitle("Items de lista");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fv.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarOrdenItems(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/OrdenItemsGui/Lst_OrdenItems_Gui.fxml"));
                stage.setTitle("Items de orden");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fo.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarPagos(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/PagosGui/Lst_Pagos_Gui.fxml"));
                stage.setTitle("Pagos");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fo.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

    public void call_ListarEnvios(){
        try{
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/EnviosGui/Lst_Envios_Gui.fxml"));
                stage.setTitle("Envios");
                stage.setScene(new Scene(root));
                stage.show();
        } catch (Exception ex){
            fdir.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    
}
