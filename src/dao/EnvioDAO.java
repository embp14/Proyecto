package dao;

import dto.EnvioDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // generic feedback

public class EnvioDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

    private boolean existeOrden(int ordenId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM ordenes WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, ordenId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar la orden");
            return false;
        }
    }

    private boolean existeDireccion(int direccionId){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM direcciones WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, direccionId);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar la dirección");
            return false;
        }
    }

    public ObservableList<EnvioDTO> ListarEnvios(){
        ObservableList<EnvioDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT e.id, e.orden_id, e.direccion_id, u.nombre AS usuario_nombre, d.nombre AS direccion_nombre, " +
                     "e.empresa_envio, e.codigo_tracking, e.fecha_envio, e.fecha_entrega_estimada, e.fecha_entrega_real " +
                     "FROM envios e JOIN ordenes o ON e.orden_id=o.id JOIN usuarios u ON o.usuario_id=u.id " +
                     "JOIN direcciones d ON e.direccion_id=d.id ORDER BY e.id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                EnvioDTO dto = new EnvioDTO();
                dto.setId(rs.getInt("id"));
                dto.setOrdenId(rs.getInt("orden_id"));
                dto.setDireccionId(rs.getInt("direccion_id"));
                dto.setUsuarioNombre(rs.getString("usuario_nombre"));
                dto.setDireccionNombre(rs.getString("direccion_nombre"));
                dto.setEmpresaEnvio(rs.getString("empresa_envio"));
                dto.setCodigoTracking(rs.getString("codigo_tracking"));
                dto.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                dto.setFechaEntregaEstimada(rs.getTimestamp("fecha_entrega_estimada"));
                dto.setFechaEntregaReal(rs.getTimestamp("fecha_entrega_real"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar envíos");
        }
        return lista;
    }

    public int InsertarEnvio(EnvioDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeDireccion(dto.getDireccionId())){
                fu.datosInvalidos("Orden o dirección inexistente");
                return 0;
            }
            String sql = "INSERT INTO envios(orden_id, direccion_id, empresa_envio, codigo_tracking, fecha_envio, fecha_entrega_estimada, fecha_entrega_real) VALUES(?,?,?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getOrdenId());
            ps.setInt(2, dto.getDireccionId());
            ps.setString(3, dto.getEmpresaEnvio());
            ps.setString(4, dto.getCodigoTracking());
            ps.setTimestamp(5, dto.getFechaEnvio());
            ps.setTimestamp(6, dto.getFechaEntregaEstimada());
            ps.setTimestamp(7, dto.getFechaEntregaReal());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información", "Envío registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el envío");
            return 0;
        }
    }

    public int ActualizarEnvio(EnvioDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeDireccion(dto.getDireccionId())){
                fu.datosInvalidos("Orden o dirección inexistente");
                return 0;
            }
            String sql = "UPDATE envios SET orden_id=?, direccion_id=?, empresa_envio=?, codigo_tracking=?, fecha_envio=?, fecha_entrega_estimada=?, fecha_entrega_real=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getOrdenId());
            ps.setInt(2, dto.getDireccionId());
            ps.setString(3, dto.getEmpresaEnvio());
            ps.setString(4, dto.getCodigoTracking());
            ps.setTimestamp(5, dto.getFechaEnvio());
            ps.setTimestamp(6, dto.getFechaEntregaEstimada());
            ps.setTimestamp(7, dto.getFechaEntregaReal());
            ps.setInt(8, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Envío no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Envío actualizado. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el envío");
            return 0;
        }
    }

    public int EliminarEnvio(int id){
        try{
            String sql = "DELETE FROM envios WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Envío no encontrado");
            }else{
                fu.MostrarAlertas("Información", "Envío eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el envío");
            return 0;
        }
    }
}
