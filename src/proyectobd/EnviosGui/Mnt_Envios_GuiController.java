package proyectobd.EnviosGui;

import dao.EnvioDAO;
import dao.OrdenDAO;
import dao.DireccionDAO;
import dto.EnvioDTO;
import dto.OrdenDTO;
import dto.DireccionDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackEnvio;

public class Mnt_Envios_GuiController implements Initializable {

    FeedbackEnvio fu = new FeedbackEnvio();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<OrdenDTO> cmb_orden;
    @FXML private ComboBox<DireccionDTO> cmb_direccion;
    @FXML private TextField txt_empresa;
    @FXML private TextField txt_tracking;
    @FXML private DatePicker dp_envio;
    @FXML private DatePicker dp_estimada;
    @FXML private DatePicker dp_entrega;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Grabar(){
        EnvioDAO dao = new EnvioDAO();
        if(!actualizar){
            try{
                EnvioDTO dto = new EnvioDTO();
                dto.setOrdenId(cmb_orden.getValue().getId());
                dto.setDireccionId(cmb_direccion.getValue().getId());
                dto.setEmpresaEnvio(txt_empresa.getText());
                dto.setCodigoTracking(txt_tracking.getText());
                dto.setFechaEnvio(Timestamp.valueOf(dp_envio.getValue().atStartOfDay()));
                dto.setFechaEntregaEstimada(Timestamp.valueOf(dp_estimada.getValue().atStartOfDay()));
                dto.setFechaEntregaReal(Timestamp.valueOf(dp_entrega.getValue().atStartOfDay()));
                int id = dao.InsertarEnvio(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            EnvioDTO dto = (EnvioDTO) stage.getUserData();
            dto.setOrdenId(cmb_orden.getValue().getId());
            dto.setDireccionId(cmb_direccion.getValue().getId());
            dto.setEmpresaEnvio(txt_empresa.getText());
            dto.setCodigoTracking(txt_tracking.getText());
            dto.setFechaEnvio(Timestamp.valueOf(dp_envio.getValue().atStartOfDay()));
            dto.setFechaEntregaEstimada(Timestamp.valueOf(dp_estimada.getValue().atStartOfDay()));
            dto.setFechaEntregaReal(Timestamp.valueOf(dp_entrega.getValue().atStartOfDay()));
            try{
                dao.ActualizarEnvio(dto);
                btn_Grabar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarCombos(){
        try {
            ObservableList<OrdenDTO> ordenes = FXCollections.observableArrayList();
            OrdenDAO odao = new OrdenDAO();
            ordenes.addAll(odao.ListarOrdenes());
            cmb_orden.setItems(ordenes);

            ObservableList<DireccionDTO> direcciones = FXCollections.observableArrayList();
            DireccionDAO ddao = new DireccionDAO();
            direcciones.addAll(ddao.ListarDirecciones());
            cmb_direccion.setItems(direcciones);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        EnvioDTO dto = (EnvioDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(OrdenDTO o : cmb_orden.getItems()){
                if(o.getId() == dto.getOrdenId()){ cmb_orden.setValue(o); break; }
            }
            for(DireccionDTO d : cmb_direccion.getItems()){
                if(d.getId() == dto.getDireccionId()){ cmb_direccion.setValue(d); break; }
            }
            txt_empresa.setText(dto.getEmpresaEnvio());
            txt_tracking.setText(dto.getCodigoTracking());
            dp_envio.setValue(dto.getFechaEnvio().toLocalDateTime().toLocalDate());
            dp_estimada.setValue(dto.getFechaEntregaEstimada().toLocalDateTime().toLocalDate());
            dp_entrega.setValue(dto.getFechaEntregaReal().toLocalDateTime().toLocalDate());
        } else {
            dp_envio.setValue(java.time.LocalDate.now());
            dp_estimada.setValue(java.time.LocalDate.now());
            dp_entrega.setValue(java.time.LocalDate.now());
        }
    }
}
