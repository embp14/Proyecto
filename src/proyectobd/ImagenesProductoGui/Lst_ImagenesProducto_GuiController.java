package proyectobd.ImagenesProductoGui;

import dao.ImagenProductoDAO;
import dto.ImagenProductoDTO;
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

public class Lst_ImagenesProducto_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ImagenProductoDTO> tbl_Lista;
    @FXML private TableColumn<ImagenProductoDTO, Integer> col_id;
    @FXML private TableColumn<ImagenProductoDTO, Integer> col_producto;
    @FXML private TableColumn<ImagenProductoDTO, String> col_url;
    @FXML private TableColumn<ImagenProductoDTO, Boolean> col_principal;

    FeedbackProducto fu = new FeedbackProducto();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
        call_CargarDatos();
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(){
        try{
            ImagenProductoDAO dao = new ImagenProductoDAO();
            ObservableList<ImagenProductoDTO> lista = dao.ListarImagenes(0);
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_producto.setCellValueFactory(new PropertyValueFactory<>("productoId"));
            col_url.setCellValueFactory(new PropertyValueFactory<>("url"));
            col_principal.setCellValueFactory(new PropertyValueFactory<>("esPrincipal"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            ImagenProductoDAO dao = new ImagenProductoDAO();
            ObservableList<ImagenProductoDTO> lista;
            if(txt_Buscar.getText().isEmpty()){
                lista = dao.ListarImagenes(0);
            }else{
                int id = Integer.parseInt(txt_Buscar.getText());
                lista = dao.ListarImagenes(id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ImagenesProductoGui/Mnt_ImagenesProducto_Gui.fxml"));
            stage.setTitle("Mantenimiento de Imágenes");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            ImagenProductoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ImagenesProductoGui/Mnt_ImagenesProducto_Gui.fxml"));
            stage.setTitle("Mantenimiento de Imágenes");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
