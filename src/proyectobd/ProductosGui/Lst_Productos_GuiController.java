package proyectobd.ProductosGui;

import dao.ProductoDAO;
import dto.ProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackProducto;

public class Lst_Productos_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ProductoDTO> tbl_Lista;
    @FXML private TableColumn<ProductoDTO, Integer> col_id;
    @FXML private TableColumn<ProductoDTO, Integer> col_vendedor;
    @FXML private TableColumn<ProductoDTO, Integer> col_categoria;
    @FXML private TableColumn<ProductoDTO, String> col_titulo;
    @FXML private TableColumn<ProductoDTO, Boolean> col_activo;

    FeedbackProducto fu = new FeedbackProducto();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, ov, nv) -> { call_Buscar(); });
        call_CargarDatos();
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(){
        try{
            ProductoDAO dao = new ProductoDAO();
            ObservableList<ProductoDTO> lista = dao.ListarProductos();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_vendedor.setCellValueFactory(new PropertyValueFactory<>("vendedorId"));
            col_categoria.setCellValueFactory(new PropertyValueFactory<>("categoriaId"));
            col_titulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
            col_activo.setCellValueFactory(new PropertyValueFactory<>("activo"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            ProductoDAO dao = new ProductoDAO();
            ObservableList<ProductoDTO> lista = dao.ListarProductos();
            if(!txt_Buscar.getText().isEmpty()){
                int id = Integer.parseInt(txt_Buscar.getText());
                lista.removeIf(p -> p.getId() != id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ProductosGui/Mnt_Productos_Gui.fxml"));
            stage.setTitle("Mantenimiento de Productos");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            ProductoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ProductosGui/Mnt_Productos_Gui.fxml"));
            stage.setTitle("Mantenimiento de Productos");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
