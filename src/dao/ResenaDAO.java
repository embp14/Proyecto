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

    public ObservableList<ResenaDTO> ListarResenas(){
        ObservableList<ResenaDTO> resenas = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, producto_id, usuario_id, rating, comentario, fecha FROM reseñas ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ResenaDTO dto = new ResenaDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setRating(rs.getInt("rating"));
                dto.setComentario(rs.getString("comentario"));
                dto.setFecha(rs.getTimestamp("fecha"));
                resenas.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error: "+ex.toString());
        }
        return resenas;
    }

    public ObservableList<ResenaDTO> BuscarResenas(String criterio){
        ObservableList<ResenaDTO> resenas = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, producto_id, usuario_id, rating, comentario, fecha FROM reseñas " +
                     "WHERE comentario LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ResenaDTO dto = new ResenaDTO();
                dto.setId(rs.getInt("id"));
                dto.setProductoId(rs.getInt("producto_id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setRating(rs.getInt("rating"));
                dto.setComentario(rs.getString("comentario"));
                dto.setFecha(rs.getTimestamp("fecha"));
                resenas.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error: "+ex.toString());
        }
        return resenas;
    }

    public int InsertarResena(ResenaDTO dto){
        try{
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
            fu.MostrarAlertas("Error del sistema"+ex.toString());
            return 0;
        }
    }

    public int ActualizarResena(ResenaDTO dto){
        try{
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
            fu.MostrarAlertas("Información del sistema", "Reseña actualizada. Registros modificados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema"+ex.toString());
            return 0;
        }
    }
}
