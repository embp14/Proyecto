package dao;

import dto.DireccionDTO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackVendedor; // use generic feedback

public class DireccionDAO {
    FeedbackVendedor fu = new FeedbackVendedor();

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
            fu.MostrarAlertas("Error", ex.toString());
            return false;
        }
    }

    public ObservableList<DireccionDTO> ListarDirecciones(){
        ObservableList<DireccionDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT * FROM direcciones ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                DireccionDTO dto = new DireccionDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setAlias(rs.getString("alias"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setCiudad(rs.getString("ciudad"));
                dto.setProvincia(rs.getString("provincia"));
                dto.setCodigoPostal(rs.getString("codigo_postal"));
                dto.setTelefonoContacto(rs.getString("telefono_contacto"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return lista;
    }

    public ObservableList<DireccionDTO> BuscarDirecciones(String criterio){
        ObservableList<DireccionDTO> lista = FXCollections.observableArrayList();
        ConectorBD ConnBD = new ConectorBD();
        String sql = "SELECT * FROM direcciones WHERE ciudad LIKE '%"+criterio+"%' ORDER BY id";
        try{
            Statement st = ConnBD.AbrirConexionBD().createStatement();
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                DireccionDTO dto = new DireccionDTO();
                dto.setId(rs.getInt("id"));
                dto.setUsuarioId(rs.getInt("usuario_id"));
                dto.setAlias(rs.getString("alias"));
                dto.setDireccion(rs.getString("direccion"));
                dto.setCiudad(rs.getString("ciudad"));
                dto.setProvincia(rs.getString("provincia"));
                dto.setCodigoPostal(rs.getString("codigo_postal"));
                dto.setTelefonoContacto(rs.getString("telefono_contacto"));
                lista.add(dto);
            }
            ConnBD.CerrarConexionBD();
        }catch(Exception ex){
            fu.MostrarAlertas("Error", ex.toString());
        }
        return lista;
    }

    public int InsertarDireccion(DireccionDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.MostrarAlertas("Datos inválidos", "El usuario no existe");
                return 0;
            }
            String sql = "INSERT INTO direcciones(usuario_id, alias, direccion, ciudad, provincia, codigo_postal, telefono_contacto) VALUES(?,?,?,?,?,?,?)";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getAlias());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getCiudad());
            ps.setString(5, dto.getProvincia());
            ps.setString(6, dto.getCodigoPostal());
            ps.setString(7, dto.getTelefonoContacto());
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int codigo = 0;
            if(rs.next()) codigo = rs.getInt(1);
            fu.MostrarAlertas("Información", "Dirección registrada con ID: "+codigo);
            ConnBD.CerrarConexionBD();
            return codigo;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }

    public int ActualizarDireccion(DireccionDTO dto){
        try{
            if(!existeUsuario(dto.getUsuarioId())){
                fu.MostrarAlertas("Datos inválidos", "El usuario no existe");
                return 0;
            }
            String sql = "UPDATE direcciones SET usuario_id=?, alias=?, direccion=?, ciudad=?, provincia=?, codigo_postal=?, telefono_contacto=? WHERE id=?";
            ConectorBD ConnBD = new ConectorBD();
            PreparedStatement ps = ConnBD.AbrirConexionBD().prepareStatement(sql);
            ps.setInt(1, dto.getUsuarioId());
            ps.setString(2, dto.getAlias());
            ps.setString(3, dto.getDireccion());
            ps.setString(4, dto.getCiudad());
            ps.setString(5, dto.getProvincia());
            ps.setString(6, dto.getCodigoPostal());
            ps.setString(7, dto.getTelefonoContacto());
            ps.setInt(8, dto.getId());
            int registros = ps.executeUpdate();
            fu.MostrarAlertas("Información", "Dirección actualizada. Registros: "+registros);
            ConnBD.CerrarConexionBD();
            return registros;
        }catch(Exception ex){
            fu.MostrarAlertas("Error del sistema", ex.toString());
            return 0;
        }
    }
}
