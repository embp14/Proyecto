package proyectobd.RolesGui;

import dao.RolDAO;
import dto.RolDTO;
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
import proyectobd.ParametrosGenerales.FeedbackRol;

public class Lst_Roles_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<RolDTO> tbl_Lista;
    @FXML private TableColumn<RolDTO, Integer> col_id;
    @FXML private TableColumn<RolDTO, String> col_nombre;

    FeedbackRol fu = new FeedbackRol();

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
            RolDAO dao = new RolDAO();
            ObservableList<RolDTO> lista = dao.ListarRoles();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            RolDAO dao = new RolDAO();
            ObservableList<RolDTO> lista = dao.ListarRoles();
            if(!txt_Buscar.getText().isEmpty()){
                int id = Integer.parseInt(txt_Buscar.getText());
                lista.removeIf(r -> r.getId() != id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/RolesGui/Mnt_Roles_Gui.fxml"));
            stage.setTitle("Mantenimiento de Roles");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            RolDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/RolesGui/Mnt_Roles_Gui.fxml"));
            stage.setTitle("Mantenimiento de Roles");
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
            RolDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            RolDAO dao = new RolDAO();
            dao.EliminarRol(dto.getId());
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
