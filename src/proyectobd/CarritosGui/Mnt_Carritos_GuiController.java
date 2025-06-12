package proyectobd.CarritosGui;

import dao.CarritoDAO;
import dto.CarritoDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCarrito;

public class Mnt_Carritos_GuiController implements Initializable {

    FeedbackCarrito fu = new FeedbackCarrito();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Guardar;
    @FXML private TextField txt_usuario;
    @FXML private DatePicker dp_creado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
        });
    }

    public void call_Guardar(){
        CarritoDAO dao = new CarritoDAO();
        if(!actualizar){
            try{
                CarritoDTO dto = new CarritoDTO();
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
                int id = dao.InsertarCarrito(dto);
                if(id>0){
                    btn_Guardar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CarritoDTO dto = (CarritoDTO) stage.getUserData();
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setCreadoEn(Timestamp.valueOf(dp_creado.getValue().atStartOfDay()));
            try{
                dao.ActualizarCarrito(dto);
                btn_Guardar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        CarritoDTO dto = (CarritoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            dp_creado.setValue(dto.getCreadoEn().toLocalDateTime().toLocalDate());
        }else{
            dp_creado.setValue(java.time.LocalDate.now());
        }
    }
}
