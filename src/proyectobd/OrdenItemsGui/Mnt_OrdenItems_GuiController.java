package proyectobd.OrdenItemsGui;

import dao.OrdenItemDAO;
import dao.OrdenDAO;
import dao.VarianteProductoDAO;
import dto.OrdenItemDTO;
import dto.OrdenDTO;
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
import proyectobd.ParametrosGenerales.FeedbackOrdenItem;

public class Mnt_OrdenItems_GuiController implements Initializable {

    FeedbackOrdenItem fu = new FeedbackOrdenItem();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<OrdenDTO> cmb_orden;
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
        OrdenItemDAO dao = new OrdenItemDAO();
        if(!actualizar){
            try{
                OrdenItemDTO dto = new OrdenItemDTO();
                dto.setOrdenId(cmb_orden.getValue().getId());
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
            OrdenItemDTO dto = (OrdenItemDTO) stage.getUserData();
            dto.setOrdenId(cmb_orden.getValue().getId());
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
        OrdenItemDTO dto = (OrdenItemDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(OrdenDTO o : cmb_orden.getItems()){
                if(o.getId() == dto.getOrdenId()){ cmb_orden.setValue(o); break; }
            }
            for(VarianteProductoDTO v : cmb_variante.getItems()){
                if(v.getId() == dto.getVarianteId()){ cmb_variante.setValue(v); break; }
            }
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }
}
