package proyectobd.EnviosGui;

import dao.EnvioDAO;
import dto.EnvioDTO;
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
import java.sql.Timestamp;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackEnvio;

public class Lst_Envios_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<EnvioDTO> tbl_Lista;
    @FXML private TableColumn<EnvioDTO, Integer> col_id;
    @FXML private TableColumn<EnvioDTO, String> col_orden;
    @FXML private TableColumn<EnvioDTO, String> col_direccion;
    @FXML private TableColumn<EnvioDTO, String> col_empresa;
    @FXML private TableColumn<EnvioDTO, String> col_tracking;
    @FXML private TableColumn<EnvioDTO, Timestamp> col_envio;
    @FXML private TableColumn<EnvioDTO, Timestamp> col_estimada;
    @FXML private TableColumn<EnvioDTO, Timestamp> col_entrega;

    FeedbackEnvio fu = new FeedbackEnvio();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
        call_Buscar();
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(int ordenId){
        try{
            EnvioDAO dao = new EnvioDAO();
            ObservableList<EnvioDTO> lista = dao.ListarEnvios();
            if(ordenId > 0){
                lista.removeIf(e -> e.getOrdenId() != ordenId);
            }
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_orden.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getUsuarioNombre() + " - ID " + data.getValue().getOrdenId()));
            col_direccion.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getDireccionNombre() + " - ID " + data.getValue().getDireccionId()));
            col_empresa.setCellValueFactory(new PropertyValueFactory<>("empresaEnvio"));
            col_tracking.setCellValueFactory(new PropertyValueFactory<>("codigoTracking"));
            col_envio.setCellValueFactory(new PropertyValueFactory<>("fechaEnvio"));
            col_estimada.setCellValueFactory(new PropertyValueFactory<>("fechaEntregaEstimada"));
            col_entrega.setCellValueFactory(new PropertyValueFactory<>("fechaEntregaReal"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            EnvioDAO dao = new EnvioDAO();
            ObservableList<EnvioDTO> lista = dao.ListarEnvios();
            String filtro = txt_Buscar.getText().trim().toLowerCase();
            if(!filtro.isEmpty()){
                if(filtro.matches("\\d+")){
                    int id = Integer.parseInt(filtro);
                    lista.removeIf(e -> e.getId() != id);
                }else{
                    lista.removeIf(e -> !e.toString().toLowerCase().contains(filtro));
                }
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/EnviosGui/Mnt_Envios_Gui.fxml"));
            stage.setTitle("Formulario de Mantenimiento de Envíos");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            EnvioDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/EnviosGui/Mnt_Envios_Gui.fxml"));
            stage.setTitle("Formulario de Mantenimiento de Envíos");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Borrar(){
        try{
            EnvioDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            EnvioDAO dao = new EnvioDAO();
            dao.EliminarEnvio(dto.getId());
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

}
