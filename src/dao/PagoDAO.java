package dao;

import dto.PagoDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackOrden;

public class PagoDAO {
    FeedbackOrden fu = new FeedbackOrden();

    private boolean existeOrden(int id){
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id FROM ordenes WHERE id=?";
        try{
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            boolean existe = rs.next();
            ConnBD.CerrarConexionBD();
            return existe;
        }catch(Exception ex){
            fu.errorSQL(ex, "verificar la orden");
            return false;
        }
    }

    public ObservableList<PagoDTO> ListarPagos(){
        ObservableList<PagoDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT id, orden_id, metodo_pago, monto, fecha_pago FROM pagos ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                PagoDTO dto = new PagoDTO();
                dto.setId(rs.getInt("id"));
                dto.setOrdenId(rs.getInt("orden_id"));
                dto.setMetodoPago(rs.getString("metodo_pago"));
                dto.setMonto(rs.getDouble("monto"));
                dto.setFechaPago(rs.getTimestamp("fecha_pago"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.errorSQL(ex, "listar pagos");
        }
        return lista;
    }

    public int InsertarPago(PagoDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId())){
                fu.datosInvalidos("Orden inexistente");
                return 0;
            }
            String sql = "INSERT INTO pagos(orden_id, metodo_pago, monto, fecha_pago) VALUES(?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getOrdenId());
            ps.setString(2, dto.getMetodoPago());
            ps.setDouble(3, dto.getMonto());
            ps.setTimestamp(4, dto.getFechaPago());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Informacion", "Pago registrado con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.errorSQL(ex, "registrar el pago");
            return 0;
        }
    }

    public int ActualizarPago(PagoDTO dto){
        try{
            if(!existeOrden(dto.getOrdenId())){
                fu.datosInvalidos("Orden inexistente");
                return 0;
            }
            String sql = "UPDATE pagos SET orden_id=?, metodo_pago=?, monto=?, fecha_pago=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getOrdenId());
            ps.setString(2, dto.getMetodoPago());
            ps.setDouble(3, dto.getMonto());
            ps.setTimestamp(4, dto.getFechaPago());
            ps.setInt(5, dto.getId());
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Pago no encontrado");
            }else{
                fu.MostrarAlertas("Informacion", "Pago actualizado. Registros: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "actualizar el pago");
            return 0;
        }
    }

    public int EliminarPago(int id){
        try{
            String sql = "DELETE FROM pagos WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, id);
            int registros = ps.executeUpdate();
            if(registros==0){
                fu.datosInvalidos("Pago no encontrado");
            }else{
                fu.MostrarAlertas("Informacion", "Pago eliminado. Registros afectados: "+registros);
            }
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.errorSQL(ex, "eliminar el pago");
            return 0;
        }
    }
}
