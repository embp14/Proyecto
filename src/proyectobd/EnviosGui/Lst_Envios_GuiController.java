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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackEnvio;

public class Lst_Envios_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<EnvioDTO> tbl_Lista;
    @FXML private TableColumn<EnvioDTO, Integer> col_id;
    @FXML private TableColumn<EnvioDTO, Integer> col_orden;
    @FXML private TableColumn<EnvioDTO, Integer> col_direccion;
    @FXML private TableColumn<EnvioDTO, String> col_empresa;
    @FXML private TableColumn<EnvioDTO, String> col_tracking;
    @FXML private TableColumn<EnvioDTO, String> col_envio;
    @FXML private TableColumn<EnvioDTO, String> col_estimado;
    @FXML private TableColumn<EnvioDTO, String> col_entrega;

    FeedbackEnvio fu = new FeedbackEnvio();

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
            EnvioDAO dao = new EnvioDAO();
            ObservableList<EnvioDTO> lista = dao.ListarEnvios();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_orden.setCellValueFactory(new PropertyValueFactory<>("ordenId"));
            col_direccion.setCellValueFactory(new PropertyValueFactory<>("direccionId"));
            col_empresa.setCellValueFactory(new PropertyValueFactory<>("empresaEnvio"));
            col_tracking.setCellValueFactory(new PropertyValueFactory<>("codigoTracking"));
            col_envio.setCellValueFactory(new PropertyValueFactory<>("fechaEnvio"));
            col_estimado.setCellValueFactory(new PropertyValueFactory<>("fechaEntregaEstimada"));
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
            String filtro = txt_Buscar.getText().toLowerCase();
            if(!filtro.isEmpty()){
                lista = lista.filtered(e ->
                        Integer.toString(e.getOrdenId()).contains(filtro) ||
                        (e.getEmpresaEnvio() != null && e.getEmpresaEnvio().toLowerCase().contains(filtro)) ||
                        (e.getCodigoTracking() != null && e.getCodigoTracking().toLowerCase().contains(filtro))
                );
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
            stage.setTitle("Mantenimiento Envios");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
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
            stage.setTitle("Mantenimiento Envios");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
