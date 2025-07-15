package dao;

import dto.CarritoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // reuse existing feedback

public class CarritoDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    private boolean existeUsuario(int usuarioId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM usuarios WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, usuarioId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el usuario");
            return false;
        }
    }

    public ObservableList<CarritoDTO> ListarCarritos(){
        ObservableList<CarritoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT c.id, c.usuario_id, u.nombre AS usuario_nombre, c.creado_en " +
                     "FROM carritos c JOIN usuarios u ON c.usuario_id=u.id ORDER BY c.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CarritoDTO dto = new CarritoDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar carritos");
        }
        return lista;
    }

    public ObservableList<CarritoDTO> BuscarCarritos(String criterio){
        ObservableList<CarritoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT c.id, c.usuario_id, u.nombre AS usuario_nombre, c.creado_en " +
                     "FROM carritos c JOIN usuarios u ON c.usuario_id=u.id " +
                     "WHERE CONCAT_WS(' ', c.id, c.usuario_id, u.nombre, c.creado_en) " +
                     "LIKE '%"+criterio+"%' ORDER BY c.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CarritoDTO dto = new CarritoDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar carritos");
        }
        return lista;
    }

    public int InsertarCarrito(CarritoDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario no existe");
                return 0;
            }
            String sql = "INSERT INTO carritos(usuario_id, creado_en) VALUES(?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setTimestamp(2, dto.getCreadoEn());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información", "Carrito registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el carrito");
            return 0;
        }
    }

    public int ActualizarCarrito(CarritoDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario no existe");
                return 0;
            }
            String sql = "UPDATE carritos SET usuario_id=?, creado_en=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setTimestamp(2, dto.getCreadoEn());
            ps.setInt(3, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Carrito no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Carrito actualizado. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el carrito");
            return 0;
        }
    }

    public int EliminarCarrito(int id){
        try{
            String sql = "DELETE FROM carritos WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Carrito no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Carrito eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el carrito");
            return 0;
        }
    }
}
