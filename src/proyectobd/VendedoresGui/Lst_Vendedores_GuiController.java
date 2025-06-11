package proyectobd.VendedoresGui;

import dao.VendedorDAO;
import dto.VendedorDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackVendedor;

public class Lst_Vendedores_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<VendedorDTO> tbl_Lista;
    @FXML private TableColumn<VendedorDTO, Integer> col_id;
    @FXML private TableColumn<VendedorDTO, Integer> col_usuario;
    @FXML private TableColumn<VendedorDTO, String> col_nombre;
    @FXML private TableColumn<VendedorDTO, Number> col_calificacion;

    FeedbackVendedor fu = new FeedbackVendedor();

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
            VendedorDAO dao = new VendedorDAO();
            ObservableList<VendedorDTO> lista = dao.ListarVendedores();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_usuario.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
            col_nombre.setCellValueFactory(new PropertyValueFactory<>("nombreTienda"));
            col_calificacion.setCellValueFactory(new PropertyValueFactory<>("calificacionPromedio"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            VendedorDAO dao = new VendedorDAO();
            ObservableList<VendedorDTO> lista = dao.BuscarVendedores(txt_Buscar.getText());
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
