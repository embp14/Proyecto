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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

public class Lst_Usuarios_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<UsuarioDTO> tbl_Lista;
    @FXML private TableColumn<UsuarioDTO, Integer> col_id;
    @FXML private TableColumn<UsuarioDTO, String> col_rol;
    @FXML private TableColumn<UsuarioDTO, String> col_nombre;
    @FXML private TableColumn<UsuarioDTO, String> col_email;

    FeedbackUsuario fu = new FeedbackUsuario();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
        call_CargarDatos();
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(){
        try{
            UsuarioDAO dao = new UsuarioDAO();
            ObservableList<UsuarioDTO> lista = dao.ListarUsuarios();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_rol.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getRolNombre() +
                        " - ID " + data.getValue().getRolId()));
            col_nombre.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getNombre() +
                        " - ID " + data.getValue().getId()));
            col_email.setCellValueFactory(new PropertyValueFactory<>("email"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            UsuarioDAO dao = new UsuarioDAO();
            String filtro = txt_Buscar.getText().trim();
            ObservableList<UsuarioDTO> lista;
            if(filtro.isEmpty()){
                lista = dao.ListarUsuarios();
            }else if(filtro.matches("\\d+")){
                lista = dao.ListarUsuarios();
                int id = Integer.parseInt(filtro);
                lista.removeIf(u -> u.getId() != id);
            }else{
                lista = dao.BuscarUsuarios(filtro);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Mnt_Usuarios_Gui.fxml"));
            stage.setTitle("Mantenimiento de Usuarios");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            UsuarioDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/UsuariosGui/Mnt_Usuarios_Gui.fxml"));
            stage.setTitle("Mantenimiento de Usuarios");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Borrar(){
        try{
            UsuarioDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            UsuarioDAO dao = new UsuarioDAO();
            dao.EliminarUsuario(dto.getId());
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
