package dto;

import java.sql.Timestamp;

public class EnvioDTO {
    private int id;
    private int ordenId;
    private int direccionId;
    private String usuarioNombre;
    private String direccionNombre;
    private String empresaEnvio;
    private String codigoTracking;
    private Timestamp fechaEnvio;
    private Timestamp fechaEntregaEstimada;
    private Timestamp fechaEntregaReal;

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getOrdenId() { return ordenId; }
    public void setOrdenId(int ordenId) { this.ordenId = ordenId; }

    public int getDireccionId() { return direccionId; }
    public void setDireccionId(int direccionId) { this.direccionId = direccionId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getDireccionNombre() { return direccionNombre; }
    public void setDireccionNombre(String direccionNombre) { this.direccionNombre = direccionNombre; }

    public String getEmpresaEnvio() { return empresaEnvio; }
    public void setEmpresaEnvio(String empresaEnvio) { this.empresaEnvio = empresaEnvio; }

    public String getCodigoTracking() { return codigoTracking; }
    public void setCodigoTracking(String codigoTracking) { this.codigoTracking = codigoTracking; }

    public Timestamp getFechaEnvio() { return fechaEnvio; }
    public void setFechaEnvio(Timestamp fechaEnvio) { this.fechaEnvio = fechaEnvio; }

    public Timestamp getFechaEntregaEstimada() { return fechaEntregaEstimada; }
    public void setFechaEntregaEstimada(Timestamp fechaEntregaEstimada) { this.fechaEntregaEstimada = fechaEntregaEstimada; }

    public Timestamp getFechaEntregaReal() { return fechaEntregaReal; }
    public void setFechaEntregaReal(Timestamp fechaEntregaReal) { this.fechaEntregaReal = fechaEntregaReal; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id).append(' ');
        sb.append(ordenId).append(' ');
        sb.append(direccionId).append(' ');
        if(usuarioNombre != null) sb.append(usuarioNombre).append(' ');
        if(direccionNombre != null) sb.append(direccionNombre).append(' ');
        if(empresaEnvio != null) sb.append(empresaEnvio).append(' ');
        if(codigoTracking != null) sb.append(codigoTracking).append(' ');
        if(fechaEnvio != null) sb.append(fechaEnvio).append(' ');
        if(fechaEntregaEstimada != null) sb.append(fechaEntregaEstimada).append(' ');
        if(fechaEntregaReal != null) sb.append(fechaEntregaReal).append(' ');
        return sb.toString().trim();
    }
}
