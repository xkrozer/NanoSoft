/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alega
 */
@Entity
@Table(name = "DETALLE_DEVOLUCION")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "DetalleDevolucion.findAll", query = "SELECT d FROM DetalleDevolucion d"),
    @NamedQuery(name = "DetalleDevolucion.findByFolioDevolucion", query = "SELECT d FROM DetalleDevolucion d WHERE d.folioDevolucion = :folioDevolucion"),
    @NamedQuery(name = "DetalleDevolucion.findByCantidad", query = "SELECT d FROM DetalleDevolucion d WHERE d.cantidad = :cantidad"),
    @NamedQuery(name = "DetalleDevolucion.findByMotivo", query = "SELECT d FROM DetalleDevolucion d WHERE d.motivo = :motivo")})
public class DetalleDevolucion implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "FOLIO_DEVOLUCION")
    private Integer folioDevolucion;
    @Column(name = "CANTIDAD")
    private Integer cantidad;
    @Column(name = "MOTIVO")
    private String motivo;
    @JoinColumn(name = "ID_DEV", referencedColumnName = "ID_DEV")
    @ManyToOne
    private Devolucion idDev;
    @JoinColumn(name = "CODIGO_PRODUCTO", referencedColumnName = "CODIGO_PRODUCTO")
    @ManyToOne
    private Producto codigoProducto;
    @JoinColumn(name = "FOLIO_VENTA", referencedColumnName = "FOLIO_VENTA")
    @ManyToOne
    private Venta folioVenta;

    public DetalleDevolucion() {
    }

    public DetalleDevolucion(Integer folioDevolucion) {
        this.folioDevolucion = folioDevolucion;
    }

    public Integer getFolioDevolucion() {
        return folioDevolucion;
    }

    public void setFolioDevolucion(Integer folioDevolucion) {
        this.folioDevolucion = folioDevolucion;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    public Devolucion getIdDev() {
        return idDev;
    }

    public void setIdDev(Devolucion idDev) {
        this.idDev = idDev;
    }

    public Producto getCodigoProducto() {
        return codigoProducto;
    }

    public void setCodigoProducto(Producto codigoProducto) {
        this.codigoProducto = codigoProducto;
    }

    public Venta getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(Venta folioVenta) {
        this.folioVenta = folioVenta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (folioDevolucion != null ? folioDevolucion.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof DetalleDevolucion)) {
            return false;
        }
        DetalleDevolucion other = (DetalleDevolucion) object;
        if ((this.folioDevolucion == null && other.folioDevolucion != null) || (this.folioDevolucion != null && !this.folioDevolucion.equals(other.folioDevolucion))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.DetalleDevolucion[ folioDevolucion=" + folioDevolucion + " ]";
    }
    
}
