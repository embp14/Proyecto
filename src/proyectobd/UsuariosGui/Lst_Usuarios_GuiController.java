/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyectobd.UsuariosGui;

import dao.UsuarioDAO;
import dto.UsuarioDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import proyectobd.ParametrosGenerales.FeedbackUsuario;


/**
 * FXML Controller class
 *
 * @author admin
 */



public class Lst_Usuarios_GuiController implements Initializable {
   
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Agregamos un listener para ejecutar la búsqueda de usuarios cuando se digita texto en el cuadro de búsqueda
        txt_Buscar.textProperty().addListener((Observable, oldValue, newValue) -> {
            call_BuscarUsuarios();
        });
      
        //Ejecutamos la carga de datos
       call_CargarDatos();
    }    

    // Anotaciones FXML para referenciar los objetos de la interface gráfica
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<UsuarioDTO> tbl_ListaUsuarios;    
    @FXML private TableColumn<UsuarioDTO, Integer> col_idUsuario;
    @FXML private TableColumn<UsuarioDTO, String> col_Nombre;
    @FXML private TableColumn<UsuarioDTO, String> col_Apellido;
    @FXML private TableColumn<UsuarioDTO, String> col_Login;
    @FXML private TableColumn<UsuarioDTO, String> col_Email;
    @FXML private TableColumn<UsuarioDTO, Boolean> col_Inactivo;
    
    
    //Instancio FeedbackUsuario para mostrar las alertas
    FeedbackUsuario fu=new FeedbackUsuario();
    
    public void call_CerrarVentana(){
        // Obtener el manejador del stage
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        // Cerrar la ventana
        stage.close();
    }
    
    public void call_CargarDatos(){
        try{
        //Cargo los datos en el tableview
        // Instancio la clase DAO
        UsuarioDAO UsrDAO=new UsuarioDAO();
        //Obtengo la lista de usuarios
        ObservableList<UsuarioDTO> ListaUsuarios=UsrDAO.ListarUsuarios();
        col_idUsuario.setCellValueFactory(new PropertyValueFactory<UsuarioDTO, Integer>("idUsuario"));
        col_Nombre.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Nombre"));
        col_Apellido.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Apellido"));
        col_Login.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Login"));
        col_Email.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Email"));
        col_Inactivo.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,Boolean>("Inactivo"));
        tbl_ListaUsuarios.setItems(null);
        tbl_ListaUsuarios.setItems(ListaUsuarios);
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }
    
public void call_NuevoRegistro(){
    try{
        // Genero el stage para la pantalla de mantenimiento
        Stage stage_Mnt_Usuarios=new Stage();
        //Cargamos la interface gráfica para la lista de usuarios:
        Parent root=FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Mnt_Usuarios_Gui.fxml"));
        // Establecemos el título de la ventana
        stage_Mnt_Usuarios.setTitle("Mantenimiento de usuarios del sistema");
        //Establecemos la escena que se mostrará en el escenario
        stage_Mnt_Usuarios.setScene(new Scene(root));
        //Mostramos el escenario
        stage_Mnt_Usuarios.showAndWait();
        //Actualizo la pantalla
        call_CargarDatos();
    }catch(Exception ex){
        fu.MostrarAlertas("Error del sistema:", ex.toString());
    }
}
    
    public void call_Editar(){
        try{
            //Obtengo el registro activo en la tabla para pasarlo como parámetro a la pantalla de mantenimiento
            UsuarioDTO usrdto = tbl_ListaUsuarios.getSelectionModel().getSelectedItem();
            // Genero el stage
            Stage stage_Mnt_Usuarios=new Stage();
            //Cargamos la interface gráfica para la lista de usuarios:
            Parent root=FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Mnt_Usuarios_Gui.fxml"));
            // Establecemos el título de la ventana
            stage_Mnt_Usuarios.setTitle("Mantenimiento de usuarios del sistema");
            //Establecemos la escena que se mostrará en el escenario
            stage_Mnt_Usuarios.setScene(new Scene(root));
            stage_Mnt_Usuarios.setUserData(usrdto);
            //Mostramos el escenario
            stage_Mnt_Usuarios.showAndWait();
            //Actualizo la pantalla
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error:", ex.toString());
        }

    }
    
    public void call_BuscarUsuarios(){
        try{
        //Cargar los datos en el tableview
        // Instancio la clase DAO
        UsuarioDAO UsrDAO=new UsuarioDAO();
        //Obtengo la lista de usuarios
        ObservableList<UsuarioDTO> ListaUsuarios=UsrDAO.BuscarUsuarios(txt_Buscar.getText());
        col_idUsuario.setCellValueFactory(new PropertyValueFactory<UsuarioDTO, Integer>("idUsuario"));
        col_Nombre.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Nombre"));
        col_Apellido.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Apellido"));
        col_Login.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Login"));
        col_Email.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,String>("Email"));
        col_Inactivo.setCellValueFactory(new PropertyValueFactory<UsuarioDTO,Boolean>("Inactivo"));
        //Limpio el tableview antes de cargar datos
        tbl_ListaUsuarios.setItems(null);
        //Cargo los datos en el datamodel del tableview
        tbl_ListaUsuarios.setItems(ListaUsuarios);
        }catch(Exception ex){
            //si ocurre un error, lo muestro
            fu.MostrarAlertas("Error del sistema", ex.toString());
        }
    }

}
