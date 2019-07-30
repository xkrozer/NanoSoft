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
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author alega
 */
@Entity
@Table(name = "ADMINISTRATIVO")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Administrativo.findAll", query = "SELECT a FROM Administrativo a"),
    @NamedQuery(name = "Administrativo.findByIdEmpleado", query = "SELECT a FROM Administrativo a WHERE a.idEmpleado = :idEmpleado"),
    @NamedQuery(name = "Administrativo.findByFuncion", query = "SELECT a FROM Administrativo a WHERE a.funcion = :funcion"),
    @NamedQuery(name = "Administrativo.findByDepartamento", query = "SELECT a FROM Administrativo a WHERE a.departamento = :departamento")})
public class Administrativo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "ID_EMPLEADO")
    private Integer idEmpleado;
    @Column(name = "FUNCION")
    private String funcion;
    @Column(name = "DEPARTAMENTO")
    private String departamento;

    public Administrativo() {
    }

    public Administrativo(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public Integer getIdEmpleado() {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer idEmpleado) {
        this.idEmpleado = idEmpleado;
    }

    public String getFuncion() {
        return funcion;
    }

    public void setFuncion(String funcion) {
        this.funcion = funcion;
    }

    public String getDepartamento() {
        return departamento;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEmpleado != null ? idEmpleado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Administrativo)) {
            return false;
        }
        Administrativo other = (Administrativo) object;
        if ((this.idEmpleado == null && other.idEmpleado != null) || (this.idEmpleado != null && !this.idEmpleado.equals(other.idEmpleado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "modelo.Administrativo[ idEmpleado=" + idEmpleado + " ]";
    }
    
}
