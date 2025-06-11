package dao;

import dto.VendedorDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor;

public class VendedorDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    public ObservableList<VendedorDTO> ListarVendedores(){
        ObservableList<VendedorDTO> vendedores = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, usuario_id, nombre_tienda, descripcion, calificacion_promedio FROM vendedores ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                VendedorDTO dto = new VendedorDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setNombreTienda(rs.getString("nombre_tienda"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCalificacionPromedio(rs.getBigDecimal("calificacion_promedio"));
                vendedores.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return vendedores;
    }

    public ObservableList<VendedorDTO> BuscarVendedores(String criterio){
        ObservableList<VendedorDTO> vendedores = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, usuario_id, nombre_tienda, descripcion, calificacion_promedio FROM vendedores " +
                     "WHERE nombre_tienda LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                VendedorDTO dto = new VendedorDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setNombreTienda(rs.getString("nombre_tienda"));
                dto.setDescripcion(rs.getString("descripcion"));
                dto.setCalificacionPromedio(rs.getBigDecimal("calificacion_promedio"));
                vendedores.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return vendedores;
    }

    public int InsertarVendedor(VendedorDTO dto){
        try{
            String sql = "INSERT INTO vendedores(usuario_id, nombre_tienda, descripcion, calificacion_promedio) VALUES(?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getNombreTienda());
            ps.setString(3, dto.getDescripcion());
            ps.setBigDecimal(4, dto.getCalificacionPromedio());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Vendedor registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarVendedor(VendedorDTO dto){
        try{
            String sql = "UPDATE vendedores SET usuario_id=?, nombre_tienda=?, descripcion=?, calificacion_promedio=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getNombreTienda());
            ps.setString(3, dto.getDescripcion());
            ps.setBigDecimal(4, dto.getCalificacionPromedio());
            ps.setInt(5, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema", "Vendedor actualizado. Registros modificados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
