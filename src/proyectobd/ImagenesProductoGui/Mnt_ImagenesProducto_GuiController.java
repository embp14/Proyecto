package proyectobd.ImagenesProductoGui;

import dao.ImagenProductoDAO;
import dao.ProductoDAO;
import dto.ImagenProductoDTO;
import dto.ProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.CheckBox;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackImagenProducto;

public class Mnt_ImagenesProducto_GuiController implements Initializable {

    FeedbackImagenProducto fu = new FeedbackImagenProducto();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<ProductoDTO> cmb_producto;
    @FXML private TextField txt_url;
    @FXML private CheckBox chk_principal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            cmb_producto.valueProperty().addListener((o,oldV,newV)->{
                if(!actualizar && newV!=null){
                    chk_principal.setSelected(!existePrincipal(newV.getId()));
                }
            });
        });
    }

    public void call_Grabar(){
        ImagenProductoDAO dao = new ImagenProductoDAO();
        if(!actualizar){
            try{
                ImagenProductoDTO dto = new ImagenProductoDTO();
                dto.setProductoId(cmb_producto.getValue().getId());
                dto.setUrl(txt_url.getText());
                boolean principal = chk_principal.isSelected();
                if(!existePrincipal(cmb_producto.getValue().getId())){
                    principal = true;
                    chk_principal.setSelected(true);
                }
                dto.setEsPrincipal(principal);
                int id = dao.InsertarImagen(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ImagenProductoDTO dto = (ImagenProductoDTO) stage.getUserData();
            dto.setProductoId(cmb_producto.getValue().getId());
            dto.setUrl(txt_url.getText());
            dto.setEsPrincipal(chk_principal.isSelected());
            try{
                dao.ActualizarImagen(dto);
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
            ObservableList<ProductoDTO> productos = FXCollections.observableArrayList();
            ProductoDAO pdao = new ProductoDAO();
            productos.addAll(pdao.ListarProductos());
            cmb_producto.setItems(productos);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ImagenProductoDTO dto = (ImagenProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(ProductoDTO p : cmb_producto.getItems()){
                if(p.getId() == dto.getProductoId()){ cmb_producto.setValue(p); break; }
            }
            txt_url.setText(dto.getUrl());
            chk_principal.setSelected(dto.isEsPrincipal());
        }
    }

    private boolean existePrincipal(int productoId){
        ImagenProductoDAO dao = new ImagenProductoDAO();
        for(ImagenProductoDTO img : dao.ListarImagenes(productoId)){
            if(img.isEsPrincipal()) return true;
        }
        return false;
    }
}
