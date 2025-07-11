package dao;

import dto.ListaDeseoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // reuse feedback

public class ListaDeseoDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    private boolean existeUsuario(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM usuarios WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar el usuario");
            return false;
        }
    }

    public ObservableList<ListaDeseoDTO> ListarListas(){
        ObservableList<ListaDeseoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT l.id, l.usuario_id, u.nombre AS usuario_nombre, l.nombre, l.creado_en " +
                     "FROM listas_deseos l JOIN usuarios u ON l.usuario_id=u.id ORDER BY l.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                ListaDeseoDTO dto = new ListaDeseoDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setNombre(rs.getString("nombre"));
                dto.setCreadoEn(rs.getTimestamp("creado_en"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar listas de deseos");
        }
        return lista;
    }

    public int InsertarLista(ListaDeseoDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("Usuario inexistente");
                return 0;
            }
            String sql = "INSERT INTO listas_deseos(usuario_id, nombre, creado_en) VALUES(?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getNombre());
            ps.setTimestamp(3, dto.getCreadoEn());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Lista registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la lista");
            return 0;
        }
    }

    public int ActualizarLista(ListaDeseoDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("Usuario inexistente");
                return 0;
            }
            String sql = "UPDATE listas_deseos SET usuario_id=?, nombre=?, creado_en=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getNombre());
            ps.setTimestamp(3, dto.getCreadoEn());
            ps.setInt(4, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Lista no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Lista actualizada. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la lista");
            return 0;
        }
    }

    public int EliminarLista(int id){
        try{
            String sql = "DELETE FROM listas_deseos WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Lista no encontrada");
            }else{
                fu.MostrarAlertas("Informacion", "Lista eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la lista");
            return 0;
        }
    }
}
