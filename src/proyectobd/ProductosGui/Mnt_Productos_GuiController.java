package proyectobd.ProductosGui;

import dao.ProductoDAO;
import dao.VendedorDAO;
import dao.CategoriaDAO;
import dto.ProductoDTO;
import dto.VendedorDTO;
import dto.CategoriaDTO;
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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Mnt_Productos_GuiController implements Initializable {

    FeedbackProducto fu = new FeedbackProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<Integer> cmb_vendedor;
    @FXML private ComboBox<Integer> cmb_categoria;
    @FXML private TextField txt_titulo;
    @FXML private TextField txt_descripcion;
    @FXML private TextField txt_activo;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        ProductoDAO dao = new ProductoDAO();
        if(!actualizar){
            try{
                ProductoDTO dto = new ProductoDTO();
                dto.setVendedorId(cmb_vendedor.getValue());
                dto.setCategoriaId(cmb_categoria.getValue());
                dto.setTitulo(txt_titulo.getText());
                dto.setDescripcion(txt_descripcion.getText());
                dto.setCreadoEn(new Timestamp(System.currentTimeMillis()));
                dto.setActivo(Boolean.parseBoolean(txt_activo.getText()));
                int id = dao.InsertarProducto(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ProductoDTO dto = (ProductoDTO) stage.getUserData();
            dto.setVendedorId(cmb_vendedor.getValue());
            dto.setCategoriaId(cmb_categoria.getValue());
            dto.setTitulo(txt_titulo.getText());
            dto.setDescripcion(txt_descripcion.getText());
            dto.setActivo(Boolean.parseBoolean(txt_activo.getText()));
            try{
                dao.ActualizarProducto(dto);
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
            ObservableList<Integer> vendedores = FXCollections.observableArrayList();
            VendedorDAO vdao = new VendedorDAO();
            for (VendedorDTO v : vdao.ListarVendedores()) {
                vendedores.add(v.getId());
            }
            cmb_vendedor.setItems(vendedores);

            ObservableList<Integer> categorias = FXCollections.observableArrayList();
            CategoriaDAO cdao = new CategoriaDAO();
            for (CategoriaDTO c : cdao.ListarCategorias()) {
                categorias.add(c.getId());
            }
            cmb_categoria.setItems(categorias);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ProductoDTO dto = (ProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            cmb_vendedor.setValue(dto.getVendedorId());
            cmb_categoria.setValue(dto.getCategoriaId());
            txt_titulo.setText(dto.getTitulo());
            txt_descripcion.setText(dto.getDescripcion());
            txt_activo.setText(Boolean.toString(dto.isActivo()));
        }
    }
}
