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
import modelo.DetalleVenta;
import modelo.Producto;

/**
 *
 * @author alega
 */
public class DetalleVentaJpaController implements Serializable {

    public DetalleVentaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(DetalleVenta detalleVenta) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto codigoProducto = detalleVenta.getCodigoProducto();
            if (codigoProducto != null) {
                codigoProducto = em.getReference(codigoProducto.getClass(), codigoProducto.getCodigoProducto());
                detalleVenta.setCodigoProducto(codigoProducto);
            }
            em.persist(detalleVenta);
            if (codigoProducto != null) {
                codigoProducto.getDetalleVentaList().add(detalleVenta);
                codigoProducto = em.merge(codigoProducto);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findDetalleVenta(detalleVenta.getFolioVenta()) != null) {
                throw new PreexistingEntityException("DetalleVenta " + detalleVenta + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(DetalleVenta detalleVenta) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            DetalleVenta persistentDetalleVenta = em.find(DetalleVenta.class, detalleVenta.getFolioVenta());
            Producto codigoProductoOld = persistentDetalleVenta.getCodigoProducto();
            Producto codigoProductoNew = detalleVenta.getCodigoProducto();
            if (codigoProductoNew != null) {
                codigoProductoNew = em.getReference(codigoProductoNew.getClass(), codigoProductoNew.getCodigoProducto());
                detalleVenta.setCodigoProducto(codigoProductoNew);
            }
            detalleVenta = em.merge(detalleVenta);
            if (codigoProductoOld != null && !codigoProductoOld.equals(codigoProductoNew)) {
                codigoProductoOld.getDetalleVentaList().remove(detalleVenta);
                codigoProductoOld = em.merge(codigoProductoOld);
            }
            if (codigoProductoNew != null && !codigoProductoNew.equals(codigoProductoOld)) {
                codigoProductoNew.getDetalleVentaList().add(detalleVenta);
                codigoProductoNew = em.merge(codigoProductoNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = detalleVenta.getFolioVenta();
                if (findDetalleVenta(id) == null) {
                    throw new NonexistentEntityException("The detalleVenta with id " + id + " no longer exists.");
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
            DetalleVenta detalleVenta;
            try {
                detalleVenta = em.getReference(DetalleVenta.class, id);
                detalleVenta.getFolioVenta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The detalleVenta with id " + id + " no longer exists.", enfe);
            }
            Producto codigoProducto = detalleVenta.getCodigoProducto();
            if (codigoProducto != null) {
                codigoProducto.getDetalleVentaList().remove(detalleVenta);
                codigoProducto = em.merge(codigoProducto);
            }
            em.remove(detalleVenta);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<DetalleVenta> findDetalleVentaEntities() {
        return findDetalleVentaEntities(true, -1, -1);
    }

    public List<DetalleVenta> findDetalleVentaEntities(int maxResults, int firstResult) {
        return findDetalleVentaEntities(false, maxResults, firstResult);
    }

    private List<DetalleVenta> findDetalleVentaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(DetalleVenta.class));
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

    public DetalleVenta findDetalleVenta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(DetalleVenta.class, id);
        } finally {
            em.close();
        }
    }

    public int getDetalleVentaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<DetalleVenta> rt = cq.from(DetalleVenta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
