package proyectobd.CuponesGui;

import dao.CuponDAO;
import dto.CuponDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.UUID;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCupon;

public class Mnt_Cupones_GuiController implements Initializable {

    FeedbackCupon fu = new FeedbackCupon();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_codigo;
    @FXML private TextField txt_descuento;
    @FXML private DatePicker dp_expira;
    @FXML private TextField txt_uso;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarDatos();
            txt_id.setDisable(true);
            txt_codigo.setDisable(true);
        });
    }

    public void call_Grabar(){
        CuponDAO dao = new CuponDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                CuponDTO dto = new CuponDTO();
                dto.setCodigo(txt_codigo.getText());
                dto.setDescuentoPct(Integer.parseInt(txt_descuento.getText()));
                dto.setFechaExpiracion(Timestamp.valueOf(dp_expira.getValue().atStartOfDay()));
                dto.setUsoMaximo(Integer.parseInt(txt_uso.getText()));
                int id = dao.InsertarCupon(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CuponDTO dto = (CuponDTO) stage.getUserData();
            dto.setCodigo(txt_codigo.getText());
            dto.setDescuentoPct(Integer.parseInt(txt_descuento.getText()));
            dto.setFechaExpiracion(Timestamp.valueOf(dp_expira.getValue().atStartOfDay()));
            dto.setUsoMaximo(Integer.parseInt(txt_uso.getText()));
            try{
                dao.ActualizarCupon(dto);
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
        CuponDTO dto = (CuponDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_codigo.setText(dto.getCodigo());
            txt_descuento.setText(Integer.toString(dto.getDescuentoPct()));
            dp_expira.setValue(dto.getFechaExpiracion().toLocalDateTime().toLocalDate());
            txt_uso.setText(Integer.toString(dto.getUsoMaximo()));
        }else{
            dp_expira.setValue(LocalDate.now());
            txt_codigo.setText(generarCodigo());
        }
    }

    private String generarCodigo(){
        return "CPN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    private boolean validarDatos(){
        int descuento;
        int uso;
        if(dp_expira.getValue() == null){
            fu.datosInvalidos("Fecha Expiración: seleccione una fecha válida.");
            dp_expira.requestFocus();
            return false;
        }
        if(dp_expira.getValue().isBefore(LocalDate.now())){
            fu.datosInvalidos("Fecha Expiración: no puede ser anterior a la fecha actual.");
            dp_expira.requestFocus();
            return false;
        }
        try{
            descuento = Integer.parseInt(txt_descuento.getText());
            if(descuento < 0 || descuento > 100){
                fu.datosInvalidos("Descuento %: debe ser un número entre 0 y 100.");
                txt_descuento.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Descuento %: ingrese un número válido.");
            txt_descuento.requestFocus();
            return false;
        }
        try{
            uso = Integer.parseInt(txt_uso.getText());
            if(uso < 0){
                fu.datosInvalidos("Uso Máximo: no puede ser negativo.");
                txt_uso.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Uso Máximo: ingrese un número válido.");
            txt_uso.requestFocus();
            return false;
        }
        return true;
    }
}
