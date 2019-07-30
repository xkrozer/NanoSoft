/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package control;

import control.exceptions.NonexistentEntityException;
import control.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import modelo.DetalleDevolucion;
import modelo.Devolucion;
import modelo.Producto;
import modelo.Venta;

/**
 *
 * @author alega
 */
public class DetalleDevolucionJpaController implements Serializable {

    public DetalleDevolucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleDevolucion detalleDevolucion) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Devolucion idDev = detalleDevolucion.getIdDev();
            if (idDev != null) {
                idDev = em.getReference(idDev.getClass(), idDev.getIdDev());
                detalleDevolucion.setIdDev(idDev);
            }
            Producto codigoProducto = detalleDevolucion.getCodigoProducto();
            if (codigoProducto != null) {
                codigoProducto = em.getReference(codigoProducto.getClass(), codigoProducto.getCodigoProducto());
                detalleDevolucion.setCodigoProducto(codigoProducto);
            }
            Venta folioVenta = detalleDevolucion.getFolioVenta();
            if (folioVenta != null) {
                folioVenta = em.getReference(folioVenta.getClass(), folioVenta.getFolioVenta());
                detalleDevolucion.setFolioVenta(folioVenta);
            }
            em.persist(detalleDevolucion);
            if (idDev != null) {
                idDev.getDetalleDevolucionList().add(detalleDevolucion);
                idDev = em.merge(idDev);
            }
            if (codigoProducto != null) {
                codigoProducto.getDetalleDevolucionList().add(detalleDevolucion);
                codigoProducto = em.merge(codigoProducto);
            }
            if (folioVenta != null) {
                folioVenta.getDetalleDevolucionList().add(detalleDevolucion);
                folioVenta = em.merge(folioVenta);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleDevolucion(detalleDevolucion.getFolioDevolucion()) != null) {
                throw new PreexistingEntityException("DetalleDevolucion " + detalleDevolucion + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleDevolucion detalleDevolucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleDevolucion persistentDetalleDevolucion = em.find(DetalleDevolucion.class, detalleDevolucion.getFolioDevolucion());
            Devolucion idDevOld = persistentDetalleDevolucion.getIdDev();
            Devolucion idDevNew = detalleDevolucion.getIdDev();
            Producto codigoProductoOld = persistentDetalleDevolucion.getCodigoProducto();
            Producto codigoProductoNew = detalleDevolucion.getCodigoProducto();
            Venta folioVentaOld = persistentDetalleDevolucion.getFolioVenta();
            Venta folioVentaNew = detalleDevolucion.getFolioVenta();
            if (idDevNew != null) {
                idDevNew = em.getReference(idDevNew.getClass(), idDevNew.getIdDev());
                detalleDevolucion.setIdDev(idDevNew);
            }
            if (codigoProductoNew != null) {
                codigoProductoNew = em.getReference(codigoProductoNew.getClass(), codigoProductoNew.getCodigoProducto());
                detalleDevolucion.setCodigoProducto(codigoProductoNew);
            }
            if (folioVentaNew != null) {
                folioVentaNew = em.getReference(folioVentaNew.getClass(), folioVentaNew.getFolioVenta());
                detalleDevolucion.setFolioVenta(folioVentaNew);
            }
            detalleDevolucion = em.merge(detalleDevolucion);
            if (idDevOld != null && !idDevOld.equals(idDevNew)) {
                idDevOld.getDetalleDevolucionList().remove(detalleDevolucion);
                idDevOld = em.merge(idDevOld);
            }
            if (idDevNew != null && !idDevNew.equals(idDevOld)) {
                idDevNew.getDetalleDevolucionList().add(detalleDevolucion);
                idDevNew = em.merge(idDevNew);
            }
            if (codigoProductoOld != null && !codigoProductoOld.equals(codigoProductoNew)) {
                codigoProductoOld.getDetalleDevolucionList().remove(detalleDevolucion);
                codigoProductoOld = em.merge(codigoProductoOld);
            }
            if (codigoProductoNew != null && !codigoProductoNew.equals(codigoProductoOld)) {
                codigoProductoNew.getDetalleDevolucionList().add(detalleDevolucion);
                codigoProductoNew = em.merge(codigoProductoNew);
            }
            if (folioVentaOld != null && !folioVentaOld.equals(folioVentaNew)) {
                folioVentaOld.getDetalleDevolucionList().remove(detalleDevolucion);
                folioVentaOld = em.merge(folioVentaOld);
            }
            if (folioVentaNew != null && !folioVentaNew.equals(folioVentaOld)) {
                folioVentaNew.getDetalleDevolucionList().add(detalleDevolucion);
                folioVentaNew = em.merge(folioVentaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleDevolucion.getFolioDevolucion();
                if (findDetalleDevolucion(id) == null) {
                    throw new NonexistentEntityException("The detalleDevolucion with id " + id + " no longer exists.");
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
            DetalleDevolucion detalleDevolucion;
            try {
                detalleDevolucion = em.getReference(DetalleDevolucion.class, id);
                detalleDevolucion.getFolioDevolucion();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleDevolucion with id " + id + " no longer exists.", enfe);
            }
            Devolucion idDev = detalleDevolucion.getIdDev();
            if (idDev != null) {
                idDev.getDetalleDevolucionList().remove(detalleDevolucion);
                idDev = em.merge(idDev);
            }
            Producto codigoProducto = detalleDevolucion.getCodigoProducto();
            if (codigoProducto != null) {
                codigoProducto.getDetalleDevolucionList().remove(detalleDevolucion);
                codigoProducto = em.merge(codigoProducto);
            }
            Venta folioVenta = detalleDevolucion.getFolioVenta();
            if (folioVenta != null) {
                folioVenta.getDetalleDevolucionList().remove(detalleDevolucion);
                folioVenta = em.merge(folioVenta);
            }
            em.remove(detalleDevolucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleDevolucion> findDetalleDevolucionEntities() {
        return findDetalleDevolucionEntities(true, -1, -1);
    }

    public List<DetalleDevolucion> findDetalleDevolucionEntities(int maxResults, int firstResult) {
        return findDetalleDevolucionEntities(false, maxResults, firstResult);
    }

    private List<DetalleDevolucion> findDetalleDevolucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleDevolucion.class));
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

    public DetalleDevolucion findDetalleDevolucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleDevolucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleDevolucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleDevolucion> rt = cq.from(DetalleDevolucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
