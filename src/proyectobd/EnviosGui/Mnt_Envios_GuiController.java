package proyectobd.EnviosGui;

import dao.EnvioDAO;
import dto.EnvioDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
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
    @FXML private TextField txt_orden;
    @FXML private TextField txt_direccion;
    @FXML private TextField txt_empresa;
    @FXML private TextField txt_tracking;
    @FXML private DatePicker dp_envio;
    @FXML private DatePicker dp_estimado;
    @FXML private DatePicker dp_entrega;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(this::cargarDatos);
    }

    public void call_Grabar(){
        EnvioDAO dao = new EnvioDAO();
        if(!actualizar){
            try{
                EnvioDTO dto = new EnvioDTO();
                dto.setOrdenId(Integer.parseInt(txt_orden.getText()));
                dto.setDireccionId(Integer.parseInt(txt_direccion.getText()));
                dto.setEmpresaEnvio(txt_empresa.getText());
                dto.setCodigoTracking(txt_tracking.getText());
                if(dp_envio.getValue() != null)
                    dto.setFechaEnvio(Timestamp.valueOf(dp_envio.getValue().atStartOfDay()));
                if(dp_estimado.getValue() != null)
                    dto.setFechaEntregaEstimada(Timestamp.valueOf(dp_estimado.getValue().atStartOfDay()));
                if(dp_entrega.getValue() != null)
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
            dto.setOrdenId(Integer.parseInt(txt_orden.getText()));
            dto.setDireccionId(Integer.parseInt(txt_direccion.getText()));
            dto.setEmpresaEnvio(txt_empresa.getText());
            dto.setCodigoTracking(txt_tracking.getText());
            if(dp_envio.getValue() != null)
                dto.setFechaEnvio(Timestamp.valueOf(dp_envio.getValue().atStartOfDay()));
            if(dp_estimado.getValue() != null)
                dto.setFechaEntregaEstimada(Timestamp.valueOf(dp_estimado.getValue().atStartOfDay()));
            if(dp_entrega.getValue() != null)
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

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        EnvioDTO dto = (EnvioDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_orden.setText(Integer.toString(dto.getOrdenId()));
            txt_direccion.setText(Integer.toString(dto.getDireccionId()));
            txt_empresa.setText(dto.getEmpresaEnvio());
            txt_tracking.setText(dto.getCodigoTracking());
            if(dto.getFechaEnvio() != null)
                dp_envio.setValue(dto.getFechaEnvio().toLocalDateTime().toLocalDate());
            if(dto.getFechaEntregaEstimada() != null)
                dp_estimado.setValue(dto.getFechaEntregaEstimada().toLocalDateTime().toLocalDate());
            if(dto.getFechaEntregaReal() != null)
                dp_entrega.setValue(dto.getFechaEntregaReal().toLocalDateTime().toLocalDate());
        }else{
            dp_envio.setValue(new Timestamp(System.currentTimeMillis()).toLocalDateTime().toLocalDate());
            dp_estimado.setValue(null);
            dp_entrega.setValue(null);
        }
    }
}
