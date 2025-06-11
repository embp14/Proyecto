package proyectobd.OrdenesGui;

import dao.OrdenDAO;
import dto.OrdenDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class Mnt_Ordenes_GuiController implements Initializable {

    FeedbackOrden fu = new FeedbackOrden();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_estado;
    @FXML private TextField txt_totalbruto;
    @FXML private TextField txt_totaldescuento;
    @FXML private TextField txt_fecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        OrdenDAO dao = new OrdenDAO();
        if(!actualizar){
            try{
                OrdenDTO dto = new OrdenDTO();
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setEstado(txt_estado.getText());
                dto.setTotalBruto(new java.math.BigDecimal(txt_totalbruto.getText()));
                dto.setTotalDescuento(new java.math.BigDecimal(txt_totaldescuento.getText()));
                dto.setFechaCreacion(Timestamp.valueOf(txt_fecha.getText()));
                int id = dao.InsertarOrden(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            OrdenDTO dto = (OrdenDTO) stage.getUserData();
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setEstado(txt_estado.getText());
            dto.setTotalBruto(new java.math.BigDecimal(txt_totalbruto.getText()));
            dto.setTotalDescuento(new java.math.BigDecimal(txt_totaldescuento.getText()));
            dto.setFechaCreacion(Timestamp.valueOf(txt_fecha.getText()));
            try{
                dao.ActualizarOrden(dto);
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
        OrdenDTO dto = (OrdenDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            txt_estado.setText(dto.getEstado());
            txt_totalbruto.setText(dto.getTotalBruto().toString());
            txt_totaldescuento.setText(dto.getTotalDescuento().toString());
            txt_fecha.setText(dto.getFechaCreacion().toString());
        }else{
            txt_fecha.setText(new Timestamp(System.currentTimeMillis()).toString());
        }
    }
}
