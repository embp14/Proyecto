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

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<Integer> cmb_orden;
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
        OrdenItemDAO dao = new OrdenItemDAO();
        if(!actualizar){
            try{
                OrdenItemDTO dto = new OrdenItemDTO();
                dto.setOrdenId(cmb_orden.getValue());
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
            OrdenItemDTO dto = (OrdenItemDTO) stage.getUserData();
            dto.setOrdenId(cmb_orden.getValue());
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
            ObservableList<Integer> ordenes = FXCollections.observableArrayList();
            OrdenDAO odao = new OrdenDAO();
            for (OrdenDTO o : odao.ListarOrdenes()) {
                ordenes.add(o.getId());
            }
            cmb_orden.setItems(ordenes);

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
        OrdenItemDTO dto = (OrdenItemDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            cmb_orden.setValue(dto.getOrdenId());
            cmb_variante.setValue(dto.getVarianteId());
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }
}
