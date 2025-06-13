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
    @FXML private ComboBox<Integer> cmb_carrito;
    @FXML private ComboBox<Integer> cmb_variante;
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
        if(!actualizar){
            try{
                CarritoItemDTO dto = new CarritoItemDTO();
                dto.setCarritoId(cmb_carrito.getValue());
                dto.setVarianteId(cmb_variante.getValue());
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
            dto.setCarritoId(cmb_carrito.getValue());
            dto.setVarianteId(cmb_variante.getValue());
            dto.setCantidad(Integer.parseInt(txt_cantidad.getText()));
            try{
                dao.ActualizarItem(dto);
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
            ObservableList<Integer> carritos = FXCollections.observableArrayList();
            CarritoDAO cdao = new CarritoDAO();
            for (CarritoDTO c : cdao.ListarCarritos()) {
                carritos.add(c.getId());
            }
            cmb_carrito.setItems(carritos);

            ObservableList<Integer> variantes = FXCollections.observableArrayList();
            VarianteProductoDAO vdao = new VarianteProductoDAO();
            for (VarianteProductoDTO v : vdao.ListarVariantes()) {
                variantes.add(v.getId());
            }
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
            cmb_carrito.setValue(dto.getCarritoId());
            cmb_variante.setValue(dto.getVarianteId());
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }
}
