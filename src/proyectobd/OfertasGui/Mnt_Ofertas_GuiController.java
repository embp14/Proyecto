package proyectobd.OfertasGui;

import dao.OfertaDAO;
import dao.VarianteProductoDAO;
import dto.OfertaDTO;
import dto.VarianteProductoDTO;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ResourceBundle;
import java.time.LocalDate;
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
import javafx.scene.layout.BorderPane;
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
            txt_id.setDisable(true);
            if(!actualizar){
                dp_inicio.setValue(LocalDate.now());
                dp_fin.setValue(LocalDate.now());
            }
        });
    }

    public void call_Grabar(){
        OfertaDAO dao = new OfertaDAO();
        if(!validarDatos()) return;
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

    private boolean validarDatos(){
        if(cmb_variante.getValue() == null){
            fu.datosInvalidos("Variante: seleccione un valor v\u00e1lido.");
            cmb_variante.requestFocus();
            return false;
        }
        try{
            double p = Double.parseDouble(txt_precio.getText());
            if(p < 0){
                fu.datosInvalidos("Descuento: no puede ser negativo.");
                txt_precio.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Descuento: ingrese un n\u00famero v\u00e1lido.");
            txt_precio.requestFocus();
            return false;
        }
        if(dp_inicio.getValue() == null){
            fu.datosInvalidos("Inicio: seleccione una fecha.");
            dp_inicio.requestFocus();
            return false;
        }
        if(dp_fin.getValue() == null){
            fu.datosInvalidos("Fin: seleccione una fecha.");
            dp_fin.requestFocus();
            return false;
        }
        if(dp_fin.getValue().isBefore(dp_inicio.getValue())){
            fu.datosInvalidos("Fin: debe ser igual o posterior al inicio.");
            dp_fin.requestFocus();
            return false;
        }
        return true;
    }
}
