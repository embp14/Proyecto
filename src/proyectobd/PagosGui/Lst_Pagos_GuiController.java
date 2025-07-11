package proyectobd.PagosGui;

import dao.PagoDAO;
import dto.PagoDTO;
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
import proyectobd.ParametrosGenerales.FeedbackPago;

public class Lst_Pagos_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<PagoDTO> tbl_Lista;
    @FXML private TableColumn<PagoDTO, Integer> col_id;
    @FXML private TableColumn<PagoDTO, String> col_orden;
    @FXML private TableColumn<PagoDTO, String> col_metodo;
    @FXML private TableColumn<PagoDTO, Double> col_monto;
    @FXML private TableColumn<PagoDTO, String> col_fecha;

    FeedbackPago fu = new FeedbackPago();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        txt_Buscar.textProperty().addListener((obs, oldV, newV) -> { call_Buscar(); });
    }

    public void call_CerrarVentana(){
        Stage stage = (Stage) btn_Cerrar.getScene().getWindow();
        stage.close();
    }

    public void call_CargarDatos(){
        try{
            PagoDAO dao = new PagoDAO();
            ObservableList<PagoDTO> lista = dao.ListarPagos();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_orden.setCellValueFactory(new PropertyValueFactory<>("usuarioNombre"));
            col_metodo.setCellValueFactory(new PropertyValueFactory<>("metodoPago"));
            col_monto.setCellValueFactory(new PropertyValueFactory<>("monto"));
            col_fecha.setCellValueFactory(new PropertyValueFactory<>("fechaPago"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            PagoDAO dao = new PagoDAO();
            ObservableList<PagoDTO> lista = dao.ListarPagos();
            String filtro = txt_Buscar.getText().trim();
            if(!filtro.isEmpty()){
                if(filtro.matches("\\d+")){
                    int id = Integer.parseInt(filtro);
                    lista.removeIf(p -> p.getId() != id);
                }else{
                    fu.datosInvalidos("Ingrese un ID num\u00e9rico");
                    return;
                }
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/PagosGui/Mnt_Pagos_Gui.fxml"));
            stage.setTitle("Mantenimiento Pagos");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            PagoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/PagosGui/Mnt_Pagos_Gui.fxml"));
            stage.setTitle("Mantenimiento Pagos");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Borrar(){
        try{
            PagoDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            PagoDAO dao = new PagoDAO();
            dao.EliminarPago(dto.getId());
            call_Buscar();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }
}
