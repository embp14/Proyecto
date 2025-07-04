package proyectobd.CarritoItemsGui;

import dao.CarritoItemDAO;
import dao.CarritoDAO;
import dao.VarianteProductoDAO;
import dto.CarritoItemDTO;
import dto.CarritoDTO;
import dto.VarianteProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCarritoItem;

public class Mnt_CarritoItems_GuiController implements Initializable {

    FeedbackCarritoItem fu = new FeedbackCarritoItem();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<CarritoDTO> cmb_carrito;
    @FXML private ComboBox<VarianteProductoDTO> cmb_variante;
    @FXML private TextField txt_cantidad;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
        });
    }

    public void call_Grabar(){
        CarritoItemDAO dao = new CarritoItemDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                CarritoItemDTO dto = new CarritoItemDTO();
                dto.setCarritoId(cmb_carrito.getValue().getId());
                dto.setVarianteId(cmb_variante.getValue().getId());
                dto.setCantidad(Integer.parseInt(txt_cantidad.getText()));
                int id = dao.InsertarItem(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            CarritoItemDTO dto = (CarritoItemDTO) stage.getUserData();
            dto.setCarritoId(cmb_carrito.getValue().getId());
            dto.setVarianteId(cmb_variante.getValue().getId());
            dto.setCantidad(Integer.parseInt(txt_cantidad.getText()));
            try{
                dao.ActualizarItem(dto);
                btn_Grabar.setDisable(true);
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }
    }

    private boolean validarDatos(){
        if(cmb_carrito.getValue()==null){
            fu.datosInvalidos("Carrito: seleccione un valor v\u00e1lido.");
            cmb_carrito.requestFocus();
            return false;
        }
        if(cmb_variante.getValue()==null){
            fu.datosInvalidos("Variante: seleccione un valor v\u00e1lido.");
            cmb_variante.requestFocus();
            return false;
        }
        try{
            int cant = Integer.parseInt(txt_cantidad.getText());
            if(cant <= 0){
                fu.datosInvalidos("Cantidad: ingrese un n\u00famero positivo.");
                txt_cantidad.requestFocus();
                return false;
            }
        }catch(Exception ex){
            fu.datosInvalidos("Cantidad: ingrese un n\u00famero v\u00e1lido.");
            txt_cantidad.requestFocus();
            return false;
        }
        return true;
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    private void cargarCombos(){
        try {
            ObservableList<CarritoDTO> carritos = FXCollections.observableArrayList();
            CarritoDAO cdao = new CarritoDAO();
            carritos.addAll(cdao.ListarCarritos());
            cmb_carrito.setItems(carritos);

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
        CarritoItemDTO dto = (CarritoItemDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(CarritoDTO c : cmb_carrito.getItems()){
                if(c.getId() == dto.getCarritoId()){ cmb_carrito.setValue(c); break; }
            }
            for(VarianteProductoDTO v : cmb_variante.getItems()){
                if(v.getId() == dto.getVarianteId()){ cmb_variante.setValue(v); break; }
            }
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }
}
