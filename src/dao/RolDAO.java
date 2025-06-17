package dao;

import dto.RolDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackRol;

public class RolDAO {
    FeedbackRol fu = new FeedbackRol();

    public ObservableList<RolDTO> ListarRoles(){
        ObservableList<RolDTO> roles = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, nombre FROM roles ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                RolDTO dto = new RolDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                roles.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.error("listar roles");
        }
        return roles;
    }

    public ObservableList<RolDTO> BuscarRoles(String criterio){
        ObservableList<RolDTO> roles = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, nombre FROM roles WHERE nombre LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                RolDTO dto = new RolDTO();
                dto.setId(rs.getInt("id"));
                dto.setNombre(rs.getString("nombre"));
                roles.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.error("buscar roles");
        }
        return roles;
    }

    public int InsertarRol(RolDTO dto){
        try{
            String sql = "INSERT INTO roles(nombre) VALUES(?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getNombre());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Rol registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.error("registrar el rol");
            return 0;
        }
    }

    public int ActualizarRol(RolDTO dto){
        try{
            String sql = "UPDATE roles SET nombre=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setString(1, dto.getNombre());
            ps.setInt(2, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema", "Rol actualizado. Registros modificados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.error("actualizar el rol");
            return 0;
        }
    }

    public int EliminarRol(int id){
        try{
            String sql = "DELETE FROM roles WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema", "Rol eliminado. Registros afectados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.error("eliminar el rol");
            return 0;
        }
    }
}
