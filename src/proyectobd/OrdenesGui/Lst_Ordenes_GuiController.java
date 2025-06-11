package proyectobd.OrdenesGui;

import dao.OrdenDAO;
import dto.OrdenDTO;
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
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class Lst_Ordenes_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<OrdenDTO> tbl_Lista;
    @FXML private TableColumn<OrdenDTO, Integer> col_id;
    @FXML private TableColumn<OrdenDTO, Integer> col_usuario;
    @FXML private TableColumn<OrdenDTO, String> col_estado;
    @FXML private TableColumn<OrdenDTO, Number> col_total;
    @FXML private TableColumn<OrdenDTO, String> col_fecha;

    FeedbackOrden fu = new FeedbackOrden();

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
            OrdenDAO dao = new OrdenDAO();
            ObservableList<OrdenDTO> lista = dao.ListarOrdenes();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_usuario.setCellValueFactory(new PropertyValueFactory<>("usuarioId"));
            col_estado.setCellValueFactory(new PropertyValueFactory<>("estado"));
            col_total.setCellValueFactory(new PropertyValueFactory<>("totalBruto"));
            col_fecha.setCellValueFactory(new PropertyValueFactory<>("fechaCreacion"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            OrdenDAO dao = new OrdenDAO();
            ObservableList<OrdenDTO> lista = dao.BuscarOrdenes(txt_Buscar.getText());
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
