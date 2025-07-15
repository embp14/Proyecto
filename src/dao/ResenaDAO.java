package dao;

import dto.ResenaDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackResena;

public class ResenaDAO {
    FeedbackResena fu = new FeedbackResena();

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

    private boolean existeProducto(int productoId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM productos WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, productoId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el producto");
            return false;
        }
    }

    public ObservableList<ResenaDTO> ListarResenas(){
        ObservableList<ResenaDTO> resenas = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT r.id, r.producto_id, p.titulo AS producto_nombre, r.usuario_id, u.nombre AS usuario_nombre, r.rating, r.comentario, r.fecha " +
                     "FROM reseñas r JOIN productos p ON r.producto_id=p.id JOIN usuarios u ON r.usuario_id=u.id ORDER BY r.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ResenaDTO dto = new ResenaDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setProductoNombre(rs.getString("producto_nombre"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setRating(rs.getInt("rating"));
                dto.setComentario(rs.getString("comentario"));
                dto.setFecha(rs.getTimestamp("fecha"));
                resenas.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar reseñas");
        }
        return resenas;
    }

    public ObservableList<ResenaDTO> BuscarResenas(String criterio){
        ObservableList<ResenaDTO> resenas = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT r.id, r.producto_id, p.titulo AS producto_nombre, r.usuario_id, u.nombre AS usuario_nombre, r.rating, r.comentario, r.fecha " +
                     "FROM reseñas r JOIN productos p ON r.producto_id=p.id JOIN usuarios u ON r.usuario_id=u.id " +
                     "WHERE r.comentario LIKE '%"+criterio+"%' " +
                     "OR p.titulo LIKE '%"+criterio+"%' " +
                     "OR u.nombre LIKE '%"+criterio+"%' " +
                     "OR CAST(r.id AS CHAR) LIKE '%"+criterio+"%' ORDER BY r.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ResenaDTO dto = new ResenaDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setProductoNombre(rs.getString("producto_nombre"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setRating(rs.getInt("rating"));
                dto.setComentario(rs.getString("comentario"));
                dto.setFecha(rs.getTimestamp("fecha"));
                resenas.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar reseñas");
        }
        return resenas;
    }

    public int InsertarResena(ResenaDTO dto){
        try{
            if(!existeProducto(dto.getProductoId()) || !existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("Verifique producto y usuario");
                return 0;
            }
            String sql = "INSERT INTO reseñas(producto_id, usuario_id, rating, comentario, fecha) VALUES(?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getProductoId());
            ps.setInt(2, dto.getUsuarioId());
            ps.setInt(3, dto.getRating());
            ps.setString(4, dto.getComentario());
            ps.setTimestamp(5, dto.getFecha());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Reseña registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la reseña");
            return 0;
        }
    }

    public int ActualizarResena(ResenaDTO dto){
        try{
            if(!existeProducto(dto.getProductoId()) || !existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("Verifique producto y usuario");
                return 0;
            }
            String sql = "UPDATE reseñas SET producto_id=?, usuario_id=?, rating=?, comentario=?, fecha=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getProductoId());
            ps.setInt(2, dto.getUsuarioId());
            ps.setInt(3, dto.getRating());
            ps.setString(4, dto.getComentario());
            ps.setTimestamp(5, dto.getFecha());
            ps.setInt(6, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Reseña no encontrada");
            }else{
                fu.MostrarAlertas("Información del sistema", "Reseña actualizada. Registros modificados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la reseña");
            return 0;
        }
    }

    public int EliminarResena(int id){
        try{
            String sql = "DELETE FROM reseñas WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Reseña no encontrada");
            }else{
                fu.MostrarAlertas("Información del sistema", "Reseña eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la reseña");
            return 0;
        }
    }
}
