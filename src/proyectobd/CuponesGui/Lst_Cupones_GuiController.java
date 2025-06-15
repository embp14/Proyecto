package proyectobd.CuponesGui;

import dao.CuponDAO;
import dto.CuponDTO;
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
import java.sql.Timestamp;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackCupon;

public class Lst_Cupones_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<CuponDTO> tbl_Lista;
    @FXML private TableColumn<CuponDTO, Integer> col_id;
    @FXML private TableColumn<CuponDTO, String> col_codigo;
    @FXML private TableColumn<CuponDTO, Integer> col_descuento;
    @FXML private TableColumn<CuponDTO, Timestamp> col_expira;

    FeedbackCupon fu = new FeedbackCupon();

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
            CuponDAO dao = new CuponDAO();
            ObservableList<CuponDTO> lista = dao.ListarCupones();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_codigo.setCellValueFactory(new PropertyValueFactory<>("codigo"));
            col_descuento.setCellValueFactory(new PropertyValueFactory<>("descuentoPct"));
            col_expira.setCellValueFactory(new PropertyValueFactory<>("fechaExpiracion"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            CuponDAO dao = new CuponDAO();
            ObservableList<CuponDTO> lista = dao.ListarCupones();
            if(!txt_Buscar.getText().isEmpty()){
                int id = Integer.parseInt(txt_Buscar.getText());
                lista.removeIf(c -> c.getId() != id);
            }
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_NuevoRegistro(){
        try{
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CuponesGui/Mnt_Cupones_Gui.fxml"));
            stage.setTitle("Mantenimiento de Cupones");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            CuponDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/CuponesGui/Mnt_Cupones_Gui.fxml"));
            stage.setTitle("Mantenimiento de Cupones");
            stage.setScene(new Scene(root));
            stage.setUserData(dto);
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Borrar(){
        try{
            CuponDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            CuponDAO dao = new CuponDAO();
            dao.EliminarCupon(dto.getId());
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Limpiar(){
        txt_Buscar.clear();
        call_Buscar();
    }
}
