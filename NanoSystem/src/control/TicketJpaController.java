/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.Empleado;
import modelo.Ticket;
import modelo.Venta;

/**
 *
 * @author alega
 */
public class TicketJpaController implements Serializable {

    public TicketJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Ticket ticket) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = ticket.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                ticket.setIdEmpleado(idEmpleado);
            }
            Venta folioVenta = ticket.getFolioVenta();
            if (folioVenta != null) {
                folioVenta = em.getReference(folioVenta.getClass(), folioVenta.getFolioVenta());
                ticket.setFolioVenta(folioVenta);
            }
            em.persist(ticket);
            if (idEmpleado != null) {
                idEmpleado.getTicketList().add(ticket);
                idEmpleado = em.merge(idEmpleado);
            }
            if (folioVenta != null) {
                folioVenta.getTicketList().add(ticket);
                folioVenta = em.merge(folioVenta);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Ticket ticket) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Ticket persistentTicket = em.find(Ticket.class, ticket.getIdTicket());
            Empleado idEmpleadoOld = persistentTicket.getIdEmpleado();
            Empleado idEmpleadoNew = ticket.getIdEmpleado();
            Venta folioVentaOld = persistentTicket.getFolioVenta();
            Venta folioVentaNew = ticket.getFolioVenta();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                ticket.setIdEmpleado(idEmpleadoNew);
            }
            if (folioVentaNew != null) {
                folioVentaNew = em.getReference(folioVentaNew.getClass(), folioVentaNew.getFolioVenta());
                ticket.setFolioVenta(folioVentaNew);
            }
            ticket = em.merge(ticket);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getTicketList().remove(ticket);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getTicketList().add(ticket);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (folioVentaOld != null && !folioVentaOld.equals(folioVentaNew)) {
                folioVentaOld.getTicketList().remove(ticket);
                folioVentaOld = em.merge(folioVentaOld);
            }
            if (folioVentaNew != null && !folioVentaNew.equals(folioVentaOld)) {
                folioVentaNew.getTicketList().add(ticket);
                folioVentaNew = em.merge(folioVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = ticket.getIdTicket();
                if (findTicket(id) == null) {
                    throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.");
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
            Ticket ticket;
            try {
                ticket = em.getReference(Ticket.class, id);
                ticket.getIdTicket();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The ticket with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = ticket.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getTicketList().remove(ticket);
                idEmpleado = em.merge(idEmpleado);
            }
            Venta folioVenta = ticket.getFolioVenta();
            if (folioVenta != null) {
                folioVenta.getTicketList().remove(ticket);
                folioVenta = em.merge(folioVenta);
            }
            em.remove(ticket);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Ticket> findTicketEntities() {
        return findTicketEntities(true, -1, -1);
    }

    public List<Ticket> findTicketEntities(int maxResults, int firstResult) {
        return findTicketEntities(false, maxResults, firstResult);
    }

    private List<Ticket> findTicketEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Ticket.class));
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

    public Ticket findTicket(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Ticket.class, id);
        } finally {
            em.close();
        }
    }

    public int getTicketCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Ticket> rt = cq.from(Ticket.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
