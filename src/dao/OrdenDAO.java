package dao;

import dto.OrdenDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class OrdenDAO {
    FeedbackOrden fu = new FeedbackOrden();

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

    public ObservableList<OrdenDTO> ListarOrdenes(){
        ObservableList<OrdenDTO> ordenes = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT o.id, o.usuario_id, u.nombre AS usuario_nombre, o.estado, o.total_bruto, o.total_descuento, o.fecha_creacion " +
                     "FROM ordenes o JOIN usuarios u ON o.usuario_id=u.id ORDER BY o.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OrdenDTO dto = new OrdenDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setEstado(rs.getString("estado"));
                dto.setTotalBruto(rs.getBigDecimal("total_bruto"));
                dto.setTotalDescuento(rs.getBigDecimal("total_descuento"));
                dto.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ordenes.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar órdenes");
        }
        return ordenes;
    }

    public ObservableList<OrdenDTO> BuscarOrdenes(String criterio){
        ObservableList<OrdenDTO> ordenes = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT o.id, o.usuario_id, u.nombre AS usuario_nombre, o.estado, o.total_bruto, o.total_descuento, o.fecha_creacion " +
                     "FROM ordenes o JOIN usuarios u ON o.usuario_id=u.id " +
                     "WHERE o.estado LIKE '%"+criterio+"%' ORDER BY o.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                OrdenDTO dto = new OrdenDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setEstado(rs.getString("estado"));
                dto.setTotalBruto(rs.getBigDecimal("total_bruto"));
                dto.setTotalDescuento(rs.getBigDecimal("total_descuento"));
                dto.setFechaCreacion(rs.getTimestamp("fecha_creacion"));
                ordenes.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "buscar órdenes");
        }
        return ordenes;
    }

    public int InsertarOrden(OrdenDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario especificado no existe");
                return 0;
            }
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
            fu.errorSQL(ex, "registrar la orden");
            return 0;
        }
    }

    public int ActualizarOrden(OrdenDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.datosInvalidos("El usuario especificado no existe");
                return 0;
            }
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
            if(registros==0){
                fu.datosInvalidos("Orden no encontrada");
            }else{
                fu.MostrarAlertas("Información del sistema", "Orden actualizada. Registros modificados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar la orden");
            return 0;
        }
    }

    public int EliminarOrden(int id){
        try{
            String sql = "DELETE FROM ordenes WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Orden no encontrada");
            }else{
                fu.MostrarAlertas("Información del sistema", "Orden eliminada. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar la orden");
            return 0;
        }
    }
}
