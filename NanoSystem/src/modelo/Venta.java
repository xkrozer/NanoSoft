/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author alega
 */
@Entity
@Table(name = "VENTA")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Venta.findAll", query = "SELECT v FROM Venta v"),
    @NamedQuery(name = "Venta.findByFolioVenta", query = "SELECT v FROM Venta v WHERE v.folioVenta = :folioVenta"),
    @NamedQuery(name = "Venta.findByFecha", query = "SELECT v FROM Venta v WHERE v.fecha = :fecha")})
public class Venta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "FOLIO_VENTA")
    private Integer folioVenta;
    @Column(name = "FECHA")
    @Temporal(TemporalType.DATE)
    private Date fecha;
    @JoinColumn(name = "ID_EMPLEADO", referencedColumnName = "ID_EMPLEADO")
    @ManyToOne
    private Empleado idEmpleado;
    @JoinColumn(name = "FOLIO_F", referencedColumnName = "FOLIO_F")
    @ManyToOne
    private Factura folioF;
    @OneToMany(mappedBy = "folioVenta")
    private List<Ticket> ticketList;
    @OneToMany(mappedBy = "folioVenta")
    private List<DetalleDevolucion> detalleDevolucionList;

    public Venta() {
    }

    public Venta(Integer folioVenta) {
        this.folioVenta = folioVenta;
    }

    public Integer getFolioVenta() {
        return folioVenta;
    }

    public void setFolioVenta(Integer folioVenta) {
        this.folioVenta = folioVenta;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Empleado getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Empleado idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Factura getFolioF() {
        return folioF;
    }

    public void setFolioF(Factura folioF) {
        this.folioF = folioF;
    }

    @XmlTransient
    public List<Ticket> getTicketList() {
        return ticketList;
    }

    public void setTicketList(List<Ticket> ticketList) {
        this.ticketList = ticketList;
    }

    @XmlTransient
    public List<DetalleDevolucion> getDetalleDevolucionList() {
        return detalleDevolucionList;
    }

    public void setDetalleDevolucionList(List<DetalleDevolucion> detalleDevolucionList) {
        this.detalleDevolucionList = detalleDevolucionList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (folioVenta != null ? folioVenta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Venta)) {
            return false;
        }
        Venta other = (Venta) object;
        if ((this.folioVenta == null && other.folioVenta != null) || (this.folioVenta != null && !this.folioVenta.equals(other.folioVenta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Venta[ folioVenta=" + folioVenta + " ]";
    }
    
}
