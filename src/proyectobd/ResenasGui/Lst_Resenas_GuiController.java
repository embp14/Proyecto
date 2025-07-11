package proyectobd.ResenasGui;

import dao.ResenaDAO;
import dto.ResenaDTO;
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
import proyectobd.ParametrosGenerales.FeedbackResena;

public class Lst_Resenas_GuiController implements Initializable {
    @FXML private Button btn_Cerrar;
    @FXML private Button btn_Nuevo;
    @FXML private Button btn_Editar;
    @FXML private Button btn_Borrar;
    @FXML private TextField txt_Buscar;
    @FXML private TableView<ResenaDTO> tbl_Lista;
    @FXML private TableColumn<ResenaDTO, Integer> col_id;
    @FXML private TableColumn<ResenaDTO, Integer> col_producto;
    @FXML private TableColumn<ResenaDTO, Integer> col_usuario;
    @FXML private TableColumn<ResenaDTO, Integer> col_rating;
    @FXML private TableColumn<ResenaDTO, Timestamp> col_fecha;

    FeedbackResena fu = new FeedbackResena();

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
            ResenaDAO dao = new ResenaDAO();
            ObservableList<ResenaDTO> lista = dao.ListarResenas();
            col_id.setCellValueFactory(new PropertyValueFactory<>("id"));
            col_producto.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getProductoNombre() + " (" + data.getValue().getProductoId() + ")"));
            col_usuario.setCellValueFactory(data ->
                    new javafx.beans.property.ReadOnlyStringWrapper(
                        data.getValue().getUsuarioNombre() + " (" + data.getValue().getUsuarioId() + ")"));
            col_rating.setCellValueFactory(new PropertyValueFactory<>("rating"));
            col_fecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
            tbl_Lista.setItems(lista);
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Buscar(){
        try{
            ResenaDAO dao = new ResenaDAO();
            ObservableList<ResenaDTO> lista = dao.ListarResenas();
            String filtro = txt_Buscar.getText().trim();
            if(!filtro.isEmpty()){
                if(filtro.matches("\\d+")){
                    int id = Integer.parseInt(filtro);
                    lista.removeIf(r -> r.getId() != id);
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
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ResenasGui/Mnt_Resenas_Gui.fxml"));
            stage.setTitle("Formulario de Mantenimiento de Reseñas");
            stage.setScene(new Scene(root));
            stage.showAndWait();
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    public void call_Editar(){
        try{
            ResenaDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para editar");
                return;
            }
            Stage stage = new Stage();
            Parent root = FXMLLoader.load(getClass().getResource("/proyectobd/ResenasGui/Mnt_Resenas_Gui.fxml"));
            stage.setTitle("Formulario de Mantenimiento de Reseñas");
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
            ResenaDTO dto = tbl_Lista.getSelectionModel().getSelectedItem();
            if(dto == null){
                fu.MostrarAlertas("Información", "Seleccione un registro para borrar");
                return;
            }
            ResenaDAO dao = new ResenaDAO();
            dao.EliminarResena(dto.getId());
            call_CargarDatos();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

}
