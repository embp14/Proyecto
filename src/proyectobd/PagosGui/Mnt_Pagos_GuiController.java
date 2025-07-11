package proyectobd.PagosGui;

import dao.PagoDAO;
import dao.OrdenDAO;
import dto.PagoDTO;
import dto.OrdenDTO;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackPago;

public class Mnt_Pagos_GuiController implements Initializable {

    FeedbackPago fu = new FeedbackPago();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<OrdenDTO> cmb_orden;
    @FXML private ComboBox<String> cmb_metodo;
    @FXML private TextField txt_monto;
    @FXML private DatePicker dp_fecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Grabar(){
        PagoDAO dao = new PagoDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                PagoDTO dto = new PagoDTO();
                dto.setOrdenId(cmb_orden.getValue().getId());
                dto.setMetodoPago(cmb_metodo.getValue());
                dto.setMonto(Double.parseDouble(txt_monto.getText()));
                dto.setFechaPago(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
                int id = dao.InsertarPago(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            PagoDTO dto = (PagoDTO) stage.getUserData();
            dto.setOrdenId(cmb_orden.getValue().getId());
            dto.setMetodoPago(cmb_metodo.getValue());
            dto.setMonto(Double.parseDouble(txt_monto.getText()));
            dto.setFechaPago(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
            try{
                dao.ActualizarPago(dto);
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

            ObservableList<String> metodos = FXCollections.observableArrayList("Efectivo", "Tarjeta", "Transferencia");
            cmb_metodo.setItems(metodos);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        PagoDTO dto = (PagoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(OrdenDTO o : cmb_orden.getItems()){
                if(o.getId() == dto.getOrdenId()){ cmb_orden.setValue(o); break; }
            }
            cmb_metodo.setValue(dto.getMetodoPago());
            txt_monto.setText(Double.toString(dto.getMonto()));
            dp_fecha.setValue(dto.getFechaPago().toLocalDateTime().toLocalDate());
        }
        else{
            dp_fecha.setValue(java.time.LocalDate.now());
        }
    }

    private boolean validarDatos(){
        if(cmb_orden.getValue()==null){
            fu.datosInvalidos("Seleccione una orden válida");
            return false;
        }
        if(cmb_metodo.getValue()==null){
            fu.datosInvalidos("Seleccione un método de pago");
            return false;
        }
        try{
            double m = Double.parseDouble(txt_monto.getText());
            if(m < 0){
                fu.datosInvalidos("Monto no puede ser negativo");
                return false;
            }
        }catch(Exception e){
            fu.datosInvalidos("Monto inválido");
            return false;
        }
        if(dp_fecha.getValue().isBefore(java.time.LocalDate.now())){
            fu.datosInvalidos("Fecha de pago no puede ser anterior a hoy");
            return false;
        }
        return true;
    }
}
