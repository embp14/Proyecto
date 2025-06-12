package proyectobd.OfertasGui;

import dao.OfertaDAO;
import dto.OfertaDTO;
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
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Mnt_Ofertas_GuiController implements Initializable {

    FeedbackProducto fu = new FeedbackProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_variante;
    @FXML private TextField txt_precio;
    @FXML private TextField txt_inicio;
    @FXML private TextField txt_fin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> { cargarDatos(); });
    }

    public void call_Grabar(){
        OfertaDAO dao = new OfertaDAO();
        if(!actualizar){
            try{
                OfertaDTO dto = new OfertaDTO();
                dto.setVarianteId(Integer.parseInt(txt_variante.getText()));
                dto.setPrecioDescuento(Double.parseDouble(txt_precio.getText()));
                dto.setFechaInicio(Timestamp.valueOf(txt_inicio.getText()));
                dto.setFechaFin(Timestamp.valueOf(txt_fin.getText()));
                int id = dao.InsertarOferta(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            OfertaDTO dto = (OfertaDTO) stage.getUserData();
            dto.setVarianteId(Integer.parseInt(txt_variante.getText()));
            dto.setPrecioDescuento(Double.parseDouble(txt_precio.getText()));
            dto.setFechaInicio(Timestamp.valueOf(txt_inicio.getText()));
            dto.setFechaFin(Timestamp.valueOf(txt_fin.getText()));
            try{
                dao.ActualizarOferta(dto);
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
        OfertaDTO dto = (OfertaDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_variante.setText(Integer.toString(dto.getVarianteId()));
            txt_precio.setText(Double.toString(dto.getPrecioDescuento()));
            txt_inicio.setText(dto.getFechaInicio().toString());
            txt_fin.setText(dto.getFechaFin().toString());
        }
    }
}
