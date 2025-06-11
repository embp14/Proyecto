package dao;

import dto.OrdenDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackUsuario;

public class OrdenDAO {
    FeedbackUsuario fu = new FeedbackUsuario();

    public ObservableList<OrdenDTO> ListarOrdenes(){
        ObservableList<OrdenDTO> ordenes = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, usuario_id, estado, total_bruto, total_descuento, fecha_creacion FROM ordenes ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OrdenDTO dto = new OrdenDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setEstado(rs.getString("estado"));
                dto.setTotalBruto(rs.getBigDecimal("total_bruto"));
                dto.setTotalDescuento(rs.getBigDecimal("total_descuento"));
                dto.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ordenes.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error: "+ex.toString());
        }
        return ordenes;
    }

    public ObservableList<OrdenDTO> BuscarOrdenes(String criterio){
        ObservableList<OrdenDTO> ordenes = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, usuario_id, estado, total_bruto, total_descuento, fecha_creacion FROM ordenes " +
                     "WHERE estado LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OrdenDTO dto = new OrdenDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setEstado(rs.getString("estado"));
                dto.setTotalBruto(rs.getBigDecimal("total_bruto"));
                dto.setTotalDescuento(rs.getBigDecimal("total_descuento"));
                dto.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ordenes.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error: "+ex.toString());
        }
        return ordenes;
    }

    public int InsertarOrden(OrdenDTO dto){
        try{
            String sql = "INSERT INTO ordenes(usuario_id, estado, total_bruto, total_descuento, fecha_creacion) VALUES(?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getEstado());
            ps.setBigDecimal(3, dto.getTotalBruto());
            ps.setBigDecimal(4, dto.getTotalDescuento());
            ps.setTimestamp(5, dto.getFechaCreacion());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()){
                codigo = rs.getInt(1);
            }
            fu.MostrarAlertas("Información del sistema", "Orden registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema"+ex.toString());
            return 0;
        }
    }

    public int ActualizarOrden(OrdenDTO dto){
        try{
            String sql = "UPDATE ordenes SET usuario_id=?, estado=?, total_bruto=?, total_descuento=?, fecha_creacion=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getEstado());
            ps.setBigDecimal(3, dto.getTotalBruto());
            ps.setBigDecimal(4, dto.getTotalDescuento());
            ps.setTimestamp(5, dto.getFechaCreacion());
            ps.setInt(6, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información del sistema", "Orden actualizada. Registros modificados: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema"+ex.toString());
            return 0;
        }
    }
}
