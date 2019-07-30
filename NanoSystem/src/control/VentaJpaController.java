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
import modelo.Empleado;
import modelo.Factura;
import modelo.Ticket;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.DetalleDevolucion;
import modelo.Venta;

/**
 *
 * @author alega
 */
public class VentaJpaController implements Serializable {

    public VentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Venta venta) {
        if (venta.getTicketList() == null) {
            venta.setTicketList(new ArrayList<Ticket>());
        }
        if (venta.getDetalleDevolucionList() == null) {
            venta.setDetalleDevolucionList(new ArrayList<DetalleDevolucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = venta.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                venta.setIdEmpleado(idEmpleado);
            }
            Factura folioF = venta.getFolioF();
            if (folioF != null) {
                folioF = em.getReference(folioF.getClass(), folioF.getFolioF());
                venta.setFolioF(folioF);
            }
            List<Ticket> attachedTicketList = new ArrayList<Ticket>();
            for (Ticket ticketListTicketToAttach : venta.getTicketList()) {
                ticketListTicketToAttach = em.getReference(ticketListTicketToAttach.getClass(), ticketListTicketToAttach.getIdTicket());
                attachedTicketList.add(ticketListTicketToAttach);
            }
            venta.setTicketList(attachedTicketList);
            List<DetalleDevolucion> attachedDetalleDevolucionList = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucionToAttach : venta.getDetalleDevolucionList()) {
                detalleDevolucionListDetalleDevolucionToAttach = em.getReference(detalleDevolucionListDetalleDevolucionToAttach.getClass(), detalleDevolucionListDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionList.add(detalleDevolucionListDetalleDevolucionToAttach);
            }
            venta.setDetalleDevolucionList(attachedDetalleDevolucionList);
            em.persist(venta);
            if (idEmpleado != null) {
                idEmpleado.getVentaList().add(venta);
                idEmpleado = em.merge(idEmpleado);
            }
            if (folioF != null) {
                folioF.getVentaList().add(venta);
                folioF = em.merge(folioF);
            }
            for (Ticket ticketListTicket : venta.getTicketList()) {
                Venta oldFolioVentaOfTicketListTicket = ticketListTicket.getFolioVenta();
                ticketListTicket.setFolioVenta(venta);
                ticketListTicket = em.merge(ticketListTicket);
                if (oldFolioVentaOfTicketListTicket != null) {
                    oldFolioVentaOfTicketListTicket.getTicketList().remove(ticketListTicket);
                    oldFolioVentaOfTicketListTicket = em.merge(oldFolioVentaOfTicketListTicket);
                }
            }
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : venta.getDetalleDevolucionList()) {
                Venta oldFolioVentaOfDetalleDevolucionListDetalleDevolucion = detalleDevolucionListDetalleDevolucion.getFolioVenta();
                detalleDevolucionListDetalleDevolucion.setFolioVenta(venta);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
                if (oldFolioVentaOfDetalleDevolucionListDetalleDevolucion != null) {
                    oldFolioVentaOfDetalleDevolucionListDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListDetalleDevolucion);
                    oldFolioVentaOfDetalleDevolucionListDetalleDevolucion = em.merge(oldFolioVentaOfDetalleDevolucionListDetalleDevolucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Venta venta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Venta persistentVenta = em.find(Venta.class, venta.getFolioVenta());
            Empleado idEmpleadoOld = persistentVenta.getIdEmpleado();
            Empleado idEmpleadoNew = venta.getIdEmpleado();
            Factura folioFOld = persistentVenta.getFolioF();
            Factura folioFNew = venta.getFolioF();
            List<Ticket> ticketListOld = persistentVenta.getTicketList();
            List<Ticket> ticketListNew = venta.getTicketList();
            List<DetalleDevolucion> detalleDevolucionListOld = persistentVenta.getDetalleDevolucionList();
            List<DetalleDevolucion> detalleDevolucionListNew = venta.getDetalleDevolucionList();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                venta.setIdEmpleado(idEmpleadoNew);
            }
            if (folioFNew != null) {
                folioFNew = em.getReference(folioFNew.getClass(), folioFNew.getFolioF());
                venta.setFolioF(folioFNew);
            }
            List<Ticket> attachedTicketListNew = new ArrayList<Ticket>();
            for (Ticket ticketListNewTicketToAttach : ticketListNew) {
                ticketListNewTicketToAttach = em.getReference(ticketListNewTicketToAttach.getClass(), ticketListNewTicketToAttach.getIdTicket());
                attachedTicketListNew.add(ticketListNewTicketToAttach);
            }
            ticketListNew = attachedTicketListNew;
            venta.setTicketList(ticketListNew);
            List<DetalleDevolucion> attachedDetalleDevolucionListNew = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucionToAttach : detalleDevolucionListNew) {
                detalleDevolucionListNewDetalleDevolucionToAttach = em.getReference(detalleDevolucionListNewDetalleDevolucionToAttach.getClass(), detalleDevolucionListNewDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionListNew.add(detalleDevolucionListNewDetalleDevolucionToAttach);
            }
            detalleDevolucionListNew = attachedDetalleDevolucionListNew;
            venta.setDetalleDevolucionList(detalleDevolucionListNew);
            venta = em.merge(venta);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getVentaList().remove(venta);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getVentaList().add(venta);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            if (folioFOld != null && !folioFOld.equals(folioFNew)) {
                folioFOld.getVentaList().remove(venta);
                folioFOld = em.merge(folioFOld);
            }
            if (folioFNew != null && !folioFNew.equals(folioFOld)) {
                folioFNew.getVentaList().add(venta);
                folioFNew = em.merge(folioFNew);
            }
            for (Ticket ticketListOldTicket : ticketListOld) {
                if (!ticketListNew.contains(ticketListOldTicket)) {
                    ticketListOldTicket.setFolioVenta(null);
                    ticketListOldTicket = em.merge(ticketListOldTicket);
                }
            }
            for (Ticket ticketListNewTicket : ticketListNew) {
                if (!ticketListOld.contains(ticketListNewTicket)) {
                    Venta oldFolioVentaOfTicketListNewTicket = ticketListNewTicket.getFolioVenta();
                    ticketListNewTicket.setFolioVenta(venta);
                    ticketListNewTicket = em.merge(ticketListNewTicket);
                    if (oldFolioVentaOfTicketListNewTicket != null && !oldFolioVentaOfTicketListNewTicket.equals(venta)) {
                        oldFolioVentaOfTicketListNewTicket.getTicketList().remove(ticketListNewTicket);
                        oldFolioVentaOfTicketListNewTicket = em.merge(oldFolioVentaOfTicketListNewTicket);
                    }
                }
            }
            for (DetalleDevolucion detalleDevolucionListOldDetalleDevolucion : detalleDevolucionListOld) {
                if (!detalleDevolucionListNew.contains(detalleDevolucionListOldDetalleDevolucion)) {
                    detalleDevolucionListOldDetalleDevolucion.setFolioVenta(null);
                    detalleDevolucionListOldDetalleDevolucion = em.merge(detalleDevolucionListOldDetalleDevolucion);
                }
            }
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucion : detalleDevolucionListNew) {
                if (!detalleDevolucionListOld.contains(detalleDevolucionListNewDetalleDevolucion)) {
                    Venta oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion = detalleDevolucionListNewDetalleDevolucion.getFolioVenta();
                    detalleDevolucionListNewDetalleDevolucion.setFolioVenta(venta);
                    detalleDevolucionListNewDetalleDevolucion = em.merge(detalleDevolucionListNewDetalleDevolucion);
                    if (oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion != null && !oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion.equals(venta)) {
                        oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListNewDetalleDevolucion);
                        oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion = em.merge(oldFolioVentaOfDetalleDevolucionListNewDetalleDevolucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = venta.getFolioVenta();
                if (findVenta(id) == null) {
                    throw new NonexistentEntityException("The venta with id " + id + " no longer exists.");
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
            Venta venta;
            try {
                venta = em.getReference(Venta.class, id);
                venta.getFolioVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The venta with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = venta.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getVentaList().remove(venta);
                idEmpleado = em.merge(idEmpleado);
            }
            Factura folioF = venta.getFolioF();
            if (folioF != null) {
                folioF.getVentaList().remove(venta);
                folioF = em.merge(folioF);
            }
            List<Ticket> ticketList = venta.getTicketList();
            for (Ticket ticketListTicket : ticketList) {
                ticketListTicket.setFolioVenta(null);
                ticketListTicket = em.merge(ticketListTicket);
            }
            List<DetalleDevolucion> detalleDevolucionList = venta.getDetalleDevolucionList();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : detalleDevolucionList) {
                detalleDevolucionListDetalleDevolucion.setFolioVenta(null);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
            }
            em.remove(venta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Venta> findVentaEntities() {
        return findVentaEntities(true, -1, -1);
    }

    public List<Venta> findVentaEntities(int maxResults, int firstResult) {
        return findVentaEntities(false, maxResults, firstResult);
    }

    private List<Venta> findVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Venta.class));
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

    public Venta findVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Venta.class, id);
        } finally {
            em.close();
        }
    }

    public int getVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Venta> rt = cq.from(Venta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
