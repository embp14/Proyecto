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
            fu.MostrarAlertas("Error", ex.toString());
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
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    public ObservableList<EnvioDTO> ListarEnvios(){
        ObservableList<EnvioDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT * FROM envios ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                EnvioDTO dto = new EnvioDTO();
                dto.setId(rs.getInt("id"));
                dto.setOrdenId(rs.getInt("orden_id"));
                dto.setDireccionId(rs.getInt("direccion_id"));
                dto.setEmpresaEnvio(rs.getString("empresa_envio"));
                dto.setCodigoTracking(rs.getString("codigo_tracking"));
                dto.setFechaEnvio(rs.getTimestamp("fecha_envio"));
                dto.setFechaEntregaEstimada(rs.getTimestamp("fecha_entrega_estimada"));
                dto.setFechaEntregaReal(rs.getTimestamp("fecha_entrega_real"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return lista;
    }

    public int InsertarEnvio(EnvioDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeDireccion(dto.getDireccionId())){
                fu.MostrarAlertas("Datos inválidos", "Orden o dirección inexistente");
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
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarEnvio(EnvioDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId()) || !existeDireccion(dto.getDireccionId())){
                fu.MostrarAlertas("Datos inválidos", "Orden o dirección inexistente");
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
            fu.MostrarAlertas("Información", "Envío actualizado. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
