/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;


import dto.UsuarioDTO;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import proyectobd.ParametrosGenerales.FeedbackUsuario;




/**
 *
 * @author admin
 */
public class UsuarioDAO {
    // Instanciamos FeedbackUsuario para mostrar las alertas
    FeedbackUsuario fu=new FeedbackUsuario();
    
    // Método para obtener la lista de usuarios de la BD
    // Devolverá un array de UsuarioDTO
   public ObservableList<UsuarioDTO> ListarUsuarios(){
       //Declaramos un ArrayList para poder generar y devolver la lista de usuarios
       //List<UsuarioDTO> usuarios = new ArrayList<>();
       ObservableList<UsuarioDTO> usuarios=FXCollections.observableArrayList();
       //Creamos la instancia del objeto ConectorBD
       ConectorBD ConnBD=new ConectorBD();
       //Declaramos un resultset para recibir los resultados desde la BD
       ResultSet rs=null;
       
       //Preparamos la sentencia SQL con los campos de la BD
       String sql="SELECT idUsuario, Nombre, Apellido, Login, Password,Email,"
               + "Inactivo, Crea, FechaCrea, Modifica, FechaModifica "
               +" FROM usuario u order by u.nombre, u.apellido";
       try{
            //Abrimos la conexión y creamos una nueva sentencia SQL
            Statement st=ConnBD.AbrirConexionBD().createStatement();
            //El resultado lo recibimos en el resulset declarado previamente
            rs=st.executeQuery(sql);
            //Recorremos el resultset como un array
            while (rs.next()){
                //Defunimos un registro del tipo UsuarioDTO que se irá agregando uno a uno al ArrayList que retornaremos
                UsuarioDTO usrdto=new UsuarioDTO();
                //Vamos estableciendo los valores de la clase dto conforme vamos recorriendo el resultset
                //OJO los campos tienen que llamarse exactamente como las columnas de la base de datos
                //Capturamos el valor de idUsuario
                usrdto.setIdUsuario(rs.getInt("idUsuario"));
                //Capturamos el valor de Nombre
                usrdto.setNombre(rs.getString("Nombre"));
                //Capturamos el valor de Apellido
                usrdto.setApellido(rs.getString("Apellido"));
                //Capturamos el valor de Login
                usrdto.setLogin(rs.getString("Login"));
                //Capturamos el valor de Password
                usrdto.setPassword(rs.getString("Password"));
                //Capturamos el valor de Email
                usrdto.setEmail(rs.getString("Email"));
                //Capturamos el valor de Inactivo
                usrdto.setInactivo(rs.getBoolean("Inactivo"));
                //Capturamos el valor de Crea
                usrdto.setCrea(rs.getString("Crea"));
                //Capturamos el valor de FechaCrea
                usrdto.setFechaCrea(rs.getTimestamp("FechaCrea"));
                //Capturamos el valor de Modifica
                usrdto.setModifica(rs.getString("Modifica"));
                //Capturamos el valor de FechaModifica
                usrdto.setFechaModifica(rs.getTimestamp("FechaModifica"));
                
                //Agregamos el registro obtenido al ArrayList
                //y repetimos el proeso hasta terminar de leer el resultset
                usuarios.add(usrdto);
            } 
            //Cerramos la conexión con la BD
            ConnBD.CerrarConexionBD();

        }catch(Exception ex){
            fu.MostrarAlertas("Error: ",ex.toString());
        }
        //ObservableList<UsuarioDTO> listaUsuarios = FXCollections.observableArrayList(usuarios);
        //return listaUsuarios;
        return usuarios;
   }
  
   public ObservableList<UsuarioDTO> BuscarUsuarios(String CriterioBusqueda){
       //Declaramos un ArrayList para poder generar y devolver la lista de usuarios
       //List<UsuarioDTO> usuarios = new ArrayList<>();
       ObservableList<UsuarioDTO> usuarios=FXCollections.observableArrayList();
       //Creamos la instancia del objeto ConectorBD
       ConectorBD ConnBD=new ConectorBD();
       //Declaramos un resultset para recibir los resultados desde la BD
       ResultSet rs=null;
       
       //Preparamos la sentencia SQL con los campos de la BD
       String sql="SELECT idUsuario, Nombre, Apellido, Login, Password,Email, Inactivo, Crea,FechaCrea, Modifica, FechaModifica"
               +" FROM usuario u "
               +" where u.Nombre like '%"+CriterioBusqueda+"%'"
               +" or u.Apellido like '%"+CriterioBusqueda+"%'"
               +" order by u.nombre, u.apellido";
       try{
            //Abrimos la conexión y creamos una nueva sentencia SQL
            Statement st=ConnBD.AbrirConexionBD().createStatement();
            //El resultado lo recibimos en el resulset declarado previamente
            rs=st.executeQuery(sql);
            //Recorremos el resultset como un array
            while (rs.next()){
                //Defunimos un registro del tipo UsuarioDTO que se irá agregando uno a uno al ArrayList que retornaremos
                UsuarioDTO usrdto=new UsuarioDTO();
                //Vamos estableciendo los valores de la clase dto conforme vamos recorriendo el resultset
                //OJO los campos tienen que llamarse exactamente como las columnas de la base de datos
                //Capturamos el valor de idUsuario
                usrdto.setIdUsuario(rs.getInt("idUsuario"));
                //Capturamos el valor de Nombre
                usrdto.setNombre(rs.getString("Nombre"));
                //Capturamos el valor de Apellido
                usrdto.setApellido(rs.getString("Apellido"));
                //Capturamos el valor de Login
                usrdto.setLogin(rs.getString("Login"));
                //Capturamos el valor de Password
                usrdto.setPassword(rs.getString("Password"));
                //Capturamos el valor de Email
                usrdto.setEmail(rs.getString("Email"));
                //Capturamos el valor de Inactivo
                usrdto.setInactivo(rs.getBoolean("Inactivo"));
                //Capturamos el valor de Crea
                usrdto.setCrea(rs.getString("Crea"));
                //Capturamos el valor de FechaCrea
                usrdto.setFechaCrea(rs.getTimestamp("FechaCrea"));
                //Capturamos el valor de Modifica
                usrdto.setModifica(rs.getString("Modifica"));
                //Capturamos el valor de FechaModifica
                usrdto.setFechaModifica(rs.getTimestamp("FechaModifica"));
                
                //Agregamos el registro obtenido al ArrayList
                //y repetimos el proeso hasta terminar de leer el resultset
                usuarios.add(usrdto);
            } 
            //Cerramos la conexión con la BD
            ConnBD.CerrarConexionBD();

        }catch(Exception ex){
            fu.MostrarAlertas("Error: ",ex.toString()+"\n"+sql);
        }
        //ObservableList<UsuarioDTO> listaUsuarios = FXCollections.observableArrayList(usuarios);
        //return listaUsuarios;
        return usuarios;
   }
   
 public int InsertarUsuario(UsuarioDTO usrdto){
     try{
        //Valido si el valor de inactivo es verdadero o falso para transformarlo a 0 o 1 conforme al campo en la bd MySQL
        int Inactivo = usrdto.isInactivo() ? 1 : 0;
        //Preparamos la sentencia SQL con los campos de la BD
        String sql="INSERT INTO USUARIO(Nombre, Apellido, Login, Password,Email, Inactivo)"
               +" VALUES('"
               +usrdto.getNombre()
               +"','"+usrdto.getApellido()
               +"','"+usrdto.getLogin()
               +"','"+usrdto.getPassword()
               +"','"+usrdto.getEmail()
               +"',b'"+Integer.toString(Inactivo)
               +"')";
        //Mando a la consola la sentencia SQL para depurarla en caso de tener errores
        System.out.println(sql);
        //Creamos la instancia del objeto ConectorBD
        ConectorBD ConnBD=new ConectorBD();
        //Abrimos la conexión y creamos una nueva sentencia SQL
        PreparedStatement ps=ConnBD.AbrirConexionBD().prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
        //Ejecutamos la consulta hacia la base de datos
        ps.executeUpdate();
        //Recibimos el valor del código correspondiente al campo AUTO_INCREMENT generado en la BD
        ResultSet rs=ps.getGeneratedKeys();
        //Retornamos el valor del código insertado
        int codigoInsertado=0;
        if (rs.next()) {
            codigoInsertado=rs.getInt(1);
        }
        //Cierro la conexión con la base de datos
        ConnBD.CerrarConexionBD();
        //Retorno el código insertado
        return codigoInsertado;
     }catch(Exception ex){
         fu.MostrarAlertas("Error del sistema", ex.toString());
         return 0;
     }
 }

 public int ActualizarUsuario(UsuarioDTO usrdto){
     try{
        //Valido si el valor de inactivo es verdadero o falso para transformarlo a 0 o 1 conforme al campo en la bd MySQL
        int Inactivo = usrdto.isInactivo() ? 1 : 0;
        //Preparamos la sentencia SQL con los campos de la BD
        String sql="UPDATE USUARIO "
               +" SET NOMBRE='" +usrdto.getNombre()
               +"',APELLIDO='" +usrdto.getApellido()
               +"',LOGIN='"+usrdto.getLogin()
               +"',PASSWORD='"+usrdto.getPassword()
               +"',EMAIL='"+usrdto.getEmail()
               +"', INACTIVO=b'"+Integer.toString(Inactivo)
               +"' where idUsuario="+Integer.toString(usrdto.getIdUsuario());
               
        //Mando a la consola la sentencia SQL para depurarla en caso de tener errores
        System.out.println(sql);
        //Creamos la instancia del objeto ConectorBD
        ConectorBD ConnBD=new ConectorBD();
        //Abrimos la conexión y creamos una nueva sentencia SQL
        PreparedStatement ps=ConnBD.AbrirConexionBD().prepareStatement(sql);
        //Variable para controlar si se actualizaron reistros:
        int registrosActualizados=0;
        //Ejecutamos la consulta hacia la base de datos
        registrosActualizados=ps.executeUpdate();
        //Retornamos el valor del código Actualizado
        //Cierro la conexión con la base de datos
        ConnBD.CerrarConexionBD();
        return registrosActualizados;
     }catch(Exception ex){
         fu.MostrarAlertas("Error del sistema", ex.toString());
         return 0;
     }
 } 
 
}
