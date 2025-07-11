package proyectobd.ListaDeseoItemsGui;

import dao.ListaDeseoItemDAO;
import dao.ListaDeseoDAO;
import dao.VarianteProductoDAO;
import dto.ListaDeseoItemDTO;
import dto.ListaDeseoDTO;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackListaDeseoItem;

public class Mnt_ListaDeseoItems_GuiController implements Initializable {

    FeedbackListaDeseoItem fu = new FeedbackListaDeseoItem();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<ListaDeseoDTO> cmb_lista;
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
        ListaDeseoItemDAO dao = new ListaDeseoItemDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                ListaDeseoItemDTO dto = new ListaDeseoItemDTO();
                dto.setListaDeseosId(cmb_lista.getValue().getId());
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
            ListaDeseoItemDTO dto = (ListaDeseoItemDTO) stage.getUserData();
            dto.setListaDeseosId(cmb_lista.getValue().getId());
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
            ObservableList<ListaDeseoDTO> listas = FXCollections.observableArrayList();
            ListaDeseoDAO ldao = new ListaDeseoDAO();
            listas.addAll(ldao.ListarListas());
            cmb_lista.setItems(listas);

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
        ListaDeseoItemDTO dto = (ListaDeseoItemDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(ListaDeseoDTO l : cmb_lista.getItems()){
                if(l.getId() == dto.getListaDeseosId()){ cmb_lista.setValue(l); break; }
            }
            for(VarianteProductoDTO v : cmb_variante.getItems()){
                if(v.getId() == dto.getVarianteId()){ cmb_variante.setValue(v); break; }
            }
            txt_cantidad.setText(Integer.toString(dto.getCantidad()));
        }
    }

    private boolean validarDatos(){
        if(cmb_lista.getValue()==null){
            fu.datosInvalidos("Lista: seleccione un valor v\u00e1lido.");
            return false;
        }
        if(cmb_variante.getValue()==null){
            fu.datosInvalidos("Variante: seleccione un valor v\u00e1lido.");
            return false;
        }
        try{
            int c = Integer.parseInt(txt_cantidad.getText());
            if(c <= 0){
                fu.datosInvalidos("Cantidad debe ser positiva");
                return false;
            }
        }catch(Exception e){
            fu.datosInvalidos("Cantidad inv\u00e1lida");
            return false;
        }
        return true;
    }
}
