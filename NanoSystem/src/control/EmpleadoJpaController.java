/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Venta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Ticket;
import modelo.Devolucion;
import modelo.Empleado;

/**
 *
 * @author alega
 */
public class EmpleadoJpaController implements Serializable {

    public EmpleadoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Empleado empleado) {
        if (empleado.getVentaList() == null) {
            empleado.setVentaList(new ArrayList<Venta>());
        }
        if (empleado.getTicketList() == null) {
            empleado.setTicketList(new ArrayList<Ticket>());
        }
        if (empleado.getDevolucionList() == null) {
            empleado.setDevolucionList(new ArrayList<Devolucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<Venta> attachedVentaList = new ArrayList<Venta>();
            for (Venta ventaListVentaToAttach : empleado.getVentaList()) {
                ventaListVentaToAttach = em.getReference(ventaListVentaToAttach.getClass(), ventaListVentaToAttach.getFolioVenta());
                attachedVentaList.add(ventaListVentaToAttach);
            }
            empleado.setVentaList(attachedVentaList);
            List<Ticket> attachedTicketList = new ArrayList<Ticket>();
            for (Ticket ticketListTicketToAttach : empleado.getTicketList()) {
                ticketListTicketToAttach = em.getReference(ticketListTicketToAttach.getClass(), ticketListTicketToAttach.getIdTicket());
                attachedTicketList.add(ticketListTicketToAttach);
            }
            empleado.setTicketList(attachedTicketList);
            List<Devolucion> attachedDevolucionList = new ArrayList<Devolucion>();
            for (Devolucion devolucionListDevolucionToAttach : empleado.getDevolucionList()) {
                devolucionListDevolucionToAttach = em.getReference(devolucionListDevolucionToAttach.getClass(), devolucionListDevolucionToAttach.getIdDev());
                attachedDevolucionList.add(devolucionListDevolucionToAttach);
            }
            empleado.setDevolucionList(attachedDevolucionList);
            em.persist(empleado);
            for (Venta ventaListVenta : empleado.getVentaList()) {
                Empleado oldIdEmpleadoOfVentaListVenta = ventaListVenta.getIdEmpleado();
                ventaListVenta.setIdEmpleado(empleado);
                ventaListVenta = em.merge(ventaListVenta);
                if (oldIdEmpleadoOfVentaListVenta != null) {
                    oldIdEmpleadoOfVentaListVenta.getVentaList().remove(ventaListVenta);
                    oldIdEmpleadoOfVentaListVenta = em.merge(oldIdEmpleadoOfVentaListVenta);
                }
            }
            for (Ticket ticketListTicket : empleado.getTicketList()) {
                Empleado oldIdEmpleadoOfTicketListTicket = ticketListTicket.getIdEmpleado();
                ticketListTicket.setIdEmpleado(empleado);
                ticketListTicket = em.merge(ticketListTicket);
                if (oldIdEmpleadoOfTicketListTicket != null) {
                    oldIdEmpleadoOfTicketListTicket.getTicketList().remove(ticketListTicket);
                    oldIdEmpleadoOfTicketListTicket = em.merge(oldIdEmpleadoOfTicketListTicket);
                }
            }
            for (Devolucion devolucionListDevolucion : empleado.getDevolucionList()) {
                Empleado oldIdEmpleadoOfDevolucionListDevolucion = devolucionListDevolucion.getIdEmpleado();
                devolucionListDevolucion.setIdEmpleado(empleado);
                devolucionListDevolucion = em.merge(devolucionListDevolucion);
                if (oldIdEmpleadoOfDevolucionListDevolucion != null) {
                    oldIdEmpleadoOfDevolucionListDevolucion.getDevolucionList().remove(devolucionListDevolucion);
                    oldIdEmpleadoOfDevolucionListDevolucion = em.merge(oldIdEmpleadoOfDevolucionListDevolucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Empleado empleado) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado persistentEmpleado = em.find(Empleado.class, empleado.getIdEmpleado());
            List<Venta> ventaListOld = persistentEmpleado.getVentaList();
            List<Venta> ventaListNew = empleado.getVentaList();
            List<Ticket> ticketListOld = persistentEmpleado.getTicketList();
            List<Ticket> ticketListNew = empleado.getTicketList();
            List<Devolucion> devolucionListOld = persistentEmpleado.getDevolucionList();
            List<Devolucion> devolucionListNew = empleado.getDevolucionList();
            List<Venta> attachedVentaListNew = new ArrayList<Venta>();
            for (Venta ventaListNewVentaToAttach : ventaListNew) {
                ventaListNewVentaToAttach = em.getReference(ventaListNewVentaToAttach.getClass(), ventaListNewVentaToAttach.getFolioVenta());
                attachedVentaListNew.add(ventaListNewVentaToAttach);
            }
            ventaListNew = attachedVentaListNew;
            empleado.setVentaList(ventaListNew);
            List<Ticket> attachedTicketListNew = new ArrayList<Ticket>();
            for (Ticket ticketListNewTicketToAttach : ticketListNew) {
                ticketListNewTicketToAttach = em.getReference(ticketListNewTicketToAttach.getClass(), ticketListNewTicketToAttach.getIdTicket());
                attachedTicketListNew.add(ticketListNewTicketToAttach);
            }
            ticketListNew = attachedTicketListNew;
            empleado.setTicketList(ticketListNew);
            List<Devolucion> attachedDevolucionListNew = new ArrayList<Devolucion>();
            for (Devolucion devolucionListNewDevolucionToAttach : devolucionListNew) {
                devolucionListNewDevolucionToAttach = em.getReference(devolucionListNewDevolucionToAttach.getClass(), devolucionListNewDevolucionToAttach.getIdDev());
                attachedDevolucionListNew.add(devolucionListNewDevolucionToAttach);
            }
            devolucionListNew = attachedDevolucionListNew;
            empleado.setDevolucionList(devolucionListNew);
            empleado = em.merge(empleado);
            for (Venta ventaListOldVenta : ventaListOld) {
                if (!ventaListNew.contains(ventaListOldVenta)) {
                    ventaListOldVenta.setIdEmpleado(null);
                    ventaListOldVenta = em.merge(ventaListOldVenta);
                }
            }
            for (Venta ventaListNewVenta : ventaListNew) {
                if (!ventaListOld.contains(ventaListNewVenta)) {
                    Empleado oldIdEmpleadoOfVentaListNewVenta = ventaListNewVenta.getIdEmpleado();
                    ventaListNewVenta.setIdEmpleado(empleado);
                    ventaListNewVenta = em.merge(ventaListNewVenta);
                    if (oldIdEmpleadoOfVentaListNewVenta != null && !oldIdEmpleadoOfVentaListNewVenta.equals(empleado)) {
                        oldIdEmpleadoOfVentaListNewVenta.getVentaList().remove(ventaListNewVenta);
                        oldIdEmpleadoOfVentaListNewVenta = em.merge(oldIdEmpleadoOfVentaListNewVenta);
                    }
                }
            }
            for (Ticket ticketListOldTicket : ticketListOld) {
                if (!ticketListNew.contains(ticketListOldTicket)) {
                    ticketListOldTicket.setIdEmpleado(null);
                    ticketListOldTicket = em.merge(ticketListOldTicket);
                }
            }
            for (Ticket ticketListNewTicket : ticketListNew) {
                if (!ticketListOld.contains(ticketListNewTicket)) {
                    Empleado oldIdEmpleadoOfTicketListNewTicket = ticketListNewTicket.getIdEmpleado();
                    ticketListNewTicket.setIdEmpleado(empleado);
                    ticketListNewTicket = em.merge(ticketListNewTicket);
                    if (oldIdEmpleadoOfTicketListNewTicket != null && !oldIdEmpleadoOfTicketListNewTicket.equals(empleado)) {
                        oldIdEmpleadoOfTicketListNewTicket.getTicketList().remove(ticketListNewTicket);
                        oldIdEmpleadoOfTicketListNewTicket = em.merge(oldIdEmpleadoOfTicketListNewTicket);
                    }
                }
            }
            for (Devolucion devolucionListOldDevolucion : devolucionListOld) {
                if (!devolucionListNew.contains(devolucionListOldDevolucion)) {
                    devolucionListOldDevolucion.setIdEmpleado(null);
                    devolucionListOldDevolucion = em.merge(devolucionListOldDevolucion);
                }
            }
            for (Devolucion devolucionListNewDevolucion : devolucionListNew) {
                if (!devolucionListOld.contains(devolucionListNewDevolucion)) {
                    Empleado oldIdEmpleadoOfDevolucionListNewDevolucion = devolucionListNewDevolucion.getIdEmpleado();
                    devolucionListNewDevolucion.setIdEmpleado(empleado);
                    devolucionListNewDevolucion = em.merge(devolucionListNewDevolucion);
                    if (oldIdEmpleadoOfDevolucionListNewDevolucion != null && !oldIdEmpleadoOfDevolucionListNewDevolucion.equals(empleado)) {
                        oldIdEmpleadoOfDevolucionListNewDevolucion.getDevolucionList().remove(devolucionListNewDevolucion);
                        oldIdEmpleadoOfDevolucionListNewDevolucion = em.merge(oldIdEmpleadoOfDevolucionListNewDevolucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = empleado.getIdEmpleado();
                if (findEmpleado(id) == null) {
                    throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado empleado;
            try {
                empleado = em.getReference(Empleado.class, id);
                empleado.getIdEmpleado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The empleado with id " + id + " no longer exists.", enfe);
            }
            List<Venta> ventaList = empleado.getVentaList();
            for (Venta ventaListVenta : ventaList) {
                ventaListVenta.setIdEmpleado(null);
                ventaListVenta = em.merge(ventaListVenta);
            }
            List<Ticket> ticketList = empleado.getTicketList();
            for (Ticket ticketListTicket : ticketList) {
                ticketListTicket.setIdEmpleado(null);
                ticketListTicket = em.merge(ticketListTicket);
            }
            List<Devolucion> devolucionList = empleado.getDevolucionList();
            for (Devolucion devolucionListDevolucion : devolucionList) {
                devolucionListDevolucion.setIdEmpleado(null);
                devolucionListDevolucion = em.merge(devolucionListDevolucion);
            }
            em.remove(empleado);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Empleado> findEmpleadoEntities() {
        return findEmpleadoEntities(true, -1, -1);
    }

    public List<Empleado> findEmpleadoEntities(int maxResults, int firstResult) {
        return findEmpleadoEntities(false, maxResults, firstResult);
    }

    private List<Empleado> findEmpleadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Empleado.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Empleado findEmpleado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Empleado.class, id);
        } finally {
            em.close();
        }
    }

    public int getEmpleadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Empleado> rt = cq.from(Empleado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
