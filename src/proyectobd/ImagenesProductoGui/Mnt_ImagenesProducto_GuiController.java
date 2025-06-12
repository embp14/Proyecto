package proyectobd.ImagenesProductoGui;

import dao.ImagenProductoDAO;
import dto.ImagenProductoDTO;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackImagenProducto;

public class Mnt_ImagenesProducto_GuiController implements Initializable {

    FeedbackImagenProducto fu = new FeedbackImagenProducto();
    private boolean actualizar = false;

    @FXML private AnchorPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private TextField txt_producto;
    @FXML private TextField txt_url;
    @FXML private TextField txt_principal;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> { cargarDatos(); });
    }

    public void call_Grabar(){
        ImagenProductoDAO dao = new ImagenProductoDAO();
        if(!actualizar){
            try{
                ImagenProductoDTO dto = new ImagenProductoDTO();
                dto.setProductoId(Integer.parseInt(txt_producto.getText()));
                dto.setUrl(txt_url.getText());
                dto.setEsPrincipal(Boolean.parseBoolean(txt_principal.getText()));
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
            dto.setProductoId(Integer.parseInt(txt_producto.getText()));
            dto.setUrl(txt_url.getText());
            dto.setEsPrincipal(Boolean.parseBoolean(txt_principal.getText()));
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

    private void cargarDatos(){
        Stage stage = (Stage) Ap_Main.getScene().getWindow();
        ImagenProductoDTO dto = (ImagenProductoDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            txt_producto.setText(Integer.toString(dto.getProductoId()));
            txt_url.setText(dto.getUrl());
            txt_principal.setText(Boolean.toString(dto.isEsPrincipal()));
        }
    }
}
