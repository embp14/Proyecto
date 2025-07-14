package proyectobd.VendedoresGui;

import dao.VendedorDAO;
import dao.UsuarioDAO;
import dto.VendedorDTO;
import dto.UsuarioDTO;
import java.net.URL;
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
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import proyectobd.ParametrosGenerales.FeedbackVendedor;
import proyectobd.ParametrosGenerales.TextFilter;

public class Mnt_Vendedores_GuiController implements Initializable {

    FeedbackVendedor fu = new FeedbackVendedor();
    private boolean actualizar = false;

    @FXML private BorderPane Ap_Main;
    @FXML private Button btn_Grabar;
    @FXML private Button btn_Cerrar;
    @FXML private TextField txt_id;
    @FXML private ComboBox<UsuarioDTO> cmb_usuario;
    @FXML private ComboBox<String> cmb_tienda;
    @FXML private TextField txt_descripcion;
    @FXML private TextField txt_calificacion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Platform.runLater(() -> {
            cargarCombos();
            cargarDatos();
            txt_id.setDisable(true);
        });
    }

    public void call_Grabar(){
        VendedorDAO dao = new VendedorDAO();
        if(!validarDatos()) return;
        if(!actualizar){
            try{
                VendedorDTO dto = new VendedorDTO();
                dto.setUsuarioId(cmb_usuario.getValue().getId());
                dto.setNombreTienda(cmb_tienda.getValue());
                dto.setDescripcion(txt_descripcion.getText());
                dto.setCalificacionPromedio(new java.math.BigDecimal(txt_calificacion.getText()));
                int id = dao.InsertarVendedor(dto);
                if(id>0){
                    txt_id.setText(Integer.toString(id));
                    btn_Grabar.setDisable(true);
                }
            }catch(Exception ex){
                fu.MostrarAlertas("Error", ex.toString());
            }
        }else{
            Stage stage = (Stage) Ap_Main.getScene().getWindow();
            VendedorDTO dto = (VendedorDTO) stage.getUserData();
            dto.setUsuarioId(cmb_usuario.getValue().getId());
            dto.setNombreTienda(cmb_tienda.getValue());
            dto.setDescripcion(txt_descripcion.getText());
            dto.setCalificacionPromedio(new java.math.BigDecimal(txt_calificacion.getText()));
            try{
                dao.ActualizarVendedor(dto);
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
        VendedorDTO dto = (VendedorDTO) stage.getUserData();
        if(dto != null){
            actualizar = true;
            txt_id.setText(Integer.toString(dto.getId()));
            for(UsuarioDTO u : cmb_usuario.getItems()){
                if(u.getId() == dto.getUsuarioId()){ cmb_usuario.setValue(u); break; }
            }
            cmb_tienda.setValue(dto.getNombreTienda());
            txt_descripcion.setText(dto.getDescripcion());
            txt_calificacion.setText(dto.getCalificacionPromedio().toString());
        }
    }

    private void cargarCombos(){
        try {
            ObservableList<UsuarioDTO> usuarios = FXCollections.observableArrayList();
            UsuarioDAO udao = new UsuarioDAO();
            usuarios.addAll(udao.ListarUsuarios());
            cmb_usuario.setItems(usuarios);

            ObservableList<String> tiendas = FXCollections.observableArrayList(
                "Tienda Quito", "Tienda Guayaquil", "Tienda Cuenca", "Tienda Loja");
            cmb_tienda.setItems(tiendas);
        } catch (Exception ex) {
            fu.MostrarAlertas("Error", ex.toString());
        }
    }

    private boolean validarDatos(){
        if(cmb_usuario.getValue() == null){
            fu.datosInvalidos("Usuario: seleccione un valor.");
            cmb_usuario.requestFocus();
            return false;
        }
        if(cmb_tienda.getValue() == null){
            fu.datosInvalidos("Tienda: seleccione un valor.");
            cmb_tienda.requestFocus();
            return false;
        }
        String desc = txt_descripcion.getText().trim();
        if(desc.isEmpty()){
            fu.datosInvalidos("Descripci\u00f3n: ingrese un texto.");
            txt_descripcion.requestFocus();
            return false;
        }
        if(TextFilter.contieneLenguajeInapropiado(desc)){
            fu.datosInvalidos("Descripci\u00f3n contiene lenguaje inapropiado.");
            txt_descripcion.requestFocus();
            return false;
        }
        if(txt_calificacion.getText().trim().isEmpty()){
            fu.datosInvalidos("Calificaci\u00f3n: ingrese un valor.");
            txt_calificacion.requestFocus();
            return false;
        }
        try{
            java.math.BigDecimal val = new java.math.BigDecimal(txt_calificacion.getText());
            if(val.compareTo(java.math.BigDecimal.ZERO) < 0 || val.compareTo(new java.math.BigDecimal("5")) > 0){
                fu.datosInvalidos("Calificaci\u00f3n: debe estar entre 0 y 5.");
                txt_calificacion.requestFocus();
                return false;
            }
        }catch(NumberFormatException ex){
            fu.datosInvalidos("Calificaci\u00f3n: valor num\u00e9rico inv\u00e1lido.");
            txt_calificacion.requestFocus();
            return false;
        }
        return true;
    }
}
