package proyectobd.OfertasGui;

import dao.OfertaDAO;
import dao.VarianteProductoDAO;
import dto.OfertaDTO;
import dto.VarianteProductoDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackOferta;

public class Mnt_Ofertas_GuiController implements Initializable {

    FeedbackOferta fu = new FeedbackOferta();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<VarianteProductoDTO> cmb_variante;
    @FXML private TextField txt_precio;
    @FXML private DatePicker dp_inicio;
    @FXML private DatePicker dp_fin;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Grabar(){
        OfertaDAO dao = new OfertaDAO();
        if(!actualizar){
            try{
                OfertaDTO dto = new OfertaDTO();
                dto.setVarianteId(cmb_variante.getValue().getId());
                dto.setPrecioDescuento(Double.parseDouble(txt_precio.getText()));
                dto.setFechaInicio(Timestamp.valueOf(dp_inicio.getValue().atStartOfDay()));
                dto.setFechaFin(Timestamp.valueOf(dp_fin.getValue().atStartOfDay()));
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
            dto.setVarianteId(cmb_variante.getValue().getId());
            dto.setPrecioDescuento(Double.parseDouble(txt_precio.getText()));
            dto.setFechaInicio(Timestamp.valueOf(dp_inicio.getValue().atStartOfDay()));
            dto.setFechaFin(Timestamp.valueOf(dp_fin.getValue().atStartOfDay()));
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

    private void cargarCombos(){
        try {
            ObservableList<VarianteProductoDTO> variantes = FXCollections.observableArrayList();
            VarianteProductoDAO vdao = new VarianteProductoDAO();
            variantes.addAll(vdao.ListarVariantes());
            cmb_variante.setItems(variantes);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        OfertaDTO dto = (OfertaDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(VarianteProductoDTO v : cmb_variante.getItems()){
                if(v.getId() == dto.getVarianteId()){ cmb_variante.setValue(v); break; }
            }
            txt_precio.setText(Double.toString(dto.getPrecioDescuento()));
            dp_inicio.setValue(dto.getFechaInicio().toLocalDateTime().toLocalDate());
            dp_fin.setValue(dto.getFechaFin().toLocalDateTime().toLocalDate());
        }
    }
}
