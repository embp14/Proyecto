package proyectobd.ResenasGui;

import dao.ResenaDAO;
import dto.ResenaDTO;
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
import proyectobd.ParametrosGenerales.FeedbackResena;

public class Mnt_Resenas_GuiController implements Initializable {

    FeedbackResena fu = new FeedbackResena();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_producto;
    @FXML private TextField txt_usuario;
    @FXML private TextField txt_rating;
    @FXML private TextField txt_comentario;
    @FXML private DatePicker dp_fecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        ResenaDAO dao = new ResenaDAO();
        if(!actualizar){
            try{
                ResenaDTO dto = new ResenaDTO();
                dto.setProductoId(Integer.parseInt(txt_producto.getText()));
                dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
                dto.setRating(Integer.parseInt(txt_rating.getText()));
                dto.setComentario(txt_comentario.getText());
                dto.setFecha(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
                int id = dao.InsertarResena(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ResenaDTO dto = (ResenaDTO) stage.getUserData();
            dto.setProductoId(Integer.parseInt(txt_producto.getText()));
            dto.setUsuarioId(Integer.parseInt(txt_usuario.getText()));
            dto.setRating(Integer.parseInt(txt_rating.getText()));
            dto.setComentario(txt_comentario.getText());
            dto.setFecha(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
            try{
                dao.ActualizarResena(dto);
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
        ResenaDTO dto = (ResenaDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_producto.setText(Integer.toString(dto.getProductoId()));
            txt_usuario.setText(Integer.toString(dto.getUsuarioId()));
            txt_rating.setText(Integer.toString(dto.getRating()));
            txt_comentario.setText(dto.getComentario());
            dp_fecha.setValue(dto.getFecha().toLocalDateTime().toLocalDate());
        }else{
            dp_fecha.setValue(java.time.LocalDate.now());
        }
    }
}
