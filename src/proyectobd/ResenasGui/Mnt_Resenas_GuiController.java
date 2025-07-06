package proyectobd.ResenasGui;

import dao.ResenaDAO;
import dao.ProductoDAO;
import dao.UsuarioDAO;
import dto.ResenaDTO;
import dto.ProductoDTO;
import dto.UsuarioDTO;
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
import javafx.scene.control.DatePicker;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackResena;

public class Mnt_Resenas_GuiController implements Initializable {

    FeedbackResena fu = new FeedbackResena();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<ProductoDTO> cmb_producto;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private ComboBox<Integer> cmb_rating;
    @FXML private TextField txt_comentario;
    @FXML private DatePicker dp_fecha;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        ResenaDAO dao = new ResenaDAO();

        if(cmb_producto.getValue() == null){
            fu.datosInvalidos("Seleccione un producto válido.");
            cmb_producto.requestFocus();
            return;
        }
        if(cmb_usuario.getValue() == null){
            fu.datosInvalidos("Seleccione un usuario válido.");
            cmb_usuario.requestFocus();
            return;
        }
        Integer rating = cmb_rating.getValue();
        if(rating == null){
            fu.datosInvalidos("Seleccione un rating válido.");
            cmb_rating.requestFocus();
            return;
        }
        if(dp_fecha.getValue() == null){
            fu.datosInvalidos("Seleccione una fecha válida.");
            dp_fecha.requestFocus();
            return;
        }
        if(dp_fecha.getValue().isBefore(java.time.LocalDate.now())){
            fu.datosInvalidos("La fecha no puede ser anterior a hoy.");
            dp_fecha.requestFocus();
            return;
        }

        if(!actualizar){
            try{
                ResenaDTO dto = new ResenaDTO();
                dto.setProductoId(cmb_producto.getValue().getId());
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setRating(rating);
                dto.setComentario(txt_comentario.getText());
                dto.setFecha(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
                int id = dao.InsertarResena(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            ResenaDTO dto = (ResenaDTO) stage.getUserData();
            dto.setProductoId(cmb_producto.getValue().getId());
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setRating(rating);
            dto.setComentario(txt_comentario.getText());
            dto.setFecha(Timestamp.valueOf(dp_fecha.getValue().atStartOfDay()));
            try{
                dao.ActualizarResena(dto);
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

            ObservableList<UsuarioDTO> usuarios = FXCollections.observableArrayList();
            UsuarioDAO udao = new UsuarioDAO();
            usuarios.addAll(udao.ListarUsuarios());
            cmb_usuario.setItems(usuarios);

            ObservableList<Integer> ratings = FXCollections.observableArrayList();
            for(int i=1;i<=5;i++) ratings.add(i);
            cmb_rating.setItems(ratings);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ResenaDTO dto = (ResenaDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(ProductoDTO p : cmb_producto.getItems()){
                if(p.getId() == dto.getProductoId()){ cmb_producto.setValue(p); break; }
            }
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            if(!cmb_rating.getItems().contains(dto.getRating())){
                cmb_rating.getItems().add(dto.getRating());
            }
            cmb_rating.setValue(dto.getRating());
            txt_comentario.setText(dto.getComentario());
            dp_fecha.setValue(dto.getFecha().toLocalDateTime().toLocalDate());
        }else{
            dp_fecha.setValue(java.time.LocalDate.now());
            if(!cmb_rating.getItems().isEmpty()){
                cmb_rating.getSelectionModel().selectFirst();
            }
        }
    }
}
