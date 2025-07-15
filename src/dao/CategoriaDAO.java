package dao;

import dto.CategoriaDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackCategoria;

public class CategoriaDAO {
    FeedbackCategoria fu = new FeedbackCategoria();

    private boolean existeCategoria(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM categorias WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar la categoría");
            return false;
        }
    }

    public ObservableList<CategoriaDTO> ListarCategorias(){
        ObservableList<CategoriaDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT c.id, c.nombre, c.parent_id, p.nombre AS parent_nombre " +
                     "FROM categorias c LEFT JOIN categorias p ON c.parent_id=p.id ORDER BY c.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CategoriaDTO dto = new CategoriaDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                int pid = rs.getInt("parent_id");
                if(rs.wasNull()) dto.setParentId(null); else dto.setParentId(pid);
                dto.setParentNombre(rs.getString("parent_nombre"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar categorías");
        }
        return lista;
    }

    public ObservableList<CategoriaDTO> BuscarCategorias(String criterio){
        ObservableList<CategoriaDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT c.id, c.nombre, c.parent_id, p.nombre AS parent_nombre " +
                     "FROM categorias c LEFT JOIN categorias p ON c.parent_id=p.id " +
                     "WHERE c.nombre LIKE '%"+criterio+"%' " +
                     "OR CAST(c.id AS CHAR) LIKE '%"+criterio+"%' ORDER BY c.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                CategoriaDTO dto = new CategoriaDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                int pid = rs.getInt("parent_id");
                if(rs.wasNull()) dto.setParentId(null); else dto.setParentId(pid);
                dto.setParentNombre(rs.getString("parent_nombre"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar categorías");
        }
        return lista;
    }

    public int InsertarCategoria(CategoriaDTO dto){
        try{
            if(dto.getParentId()!=null && !existeCategoria(dto.getParentId())){
                fu.datosInvalidos("Categoría padre inexistente");
                return 0;
            }
            String sql = "INSERT INTO categorias(nombre, parent_id) VALUES(?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getNombre());
            if(dto.getParentId()==null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, dto.getParentId());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información del sistema","Categoría registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar la categoría");
            return 0;
        }
    }

    public int ActualizarCategoria(CategoriaDTO dto){
        try{
            if(dto.getParentId()!=null && !existeCategoria(dto.getParentId())){
                fu.datosInvalidos("Categoría padre inexistente");
                return 0;
            }
            String sql = "UPDATE categorias SET nombre=?, parent_id=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setString(1, dto.getNombre());
            if(dto.getParentId()==null) ps.setNull(2, java.sql.Types.INTEGER); else ps.setInt(2, dto.getParentId());
            ps.setInt(3, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema","Categoría actualizada. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la categoría");
            return 0;
        }
    }

    public int EliminarCategoria(int id){
        try{
            String sql = "DELETE FROM categorias WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema","Categoría eliminada. Registros afectados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la categoría");
            return 0;
        }
    }
}
