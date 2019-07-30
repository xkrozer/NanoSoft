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
import modelo.DetalleVenta;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.DetalleDevolucion;
import modelo.Producto;

/**
 *
 * @author alega
 */
public class ProductoJpaController implements Serializable {

    public ProductoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Producto producto) {
        if (producto.getDetalleVentaList() == null) {
            producto.setDetalleVentaList(new ArrayList<DetalleVenta>());
        }
        if (producto.getDetalleDevolucionList() == null) {
            producto.setDetalleDevolucionList(new ArrayList<DetalleDevolucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            List<DetalleVenta> attachedDetalleVentaList = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListDetalleVentaToAttach : producto.getDetalleVentaList()) {
                detalleVentaListDetalleVentaToAttach = em.getReference(detalleVentaListDetalleVentaToAttach.getClass(), detalleVentaListDetalleVentaToAttach.getFolioVenta());
                attachedDetalleVentaList.add(detalleVentaListDetalleVentaToAttach);
            }
            producto.setDetalleVentaList(attachedDetalleVentaList);
            List<DetalleDevolucion> attachedDetalleDevolucionList = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucionToAttach : producto.getDetalleDevolucionList()) {
                detalleDevolucionListDetalleDevolucionToAttach = em.getReference(detalleDevolucionListDetalleDevolucionToAttach.getClass(), detalleDevolucionListDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionList.add(detalleDevolucionListDetalleDevolucionToAttach);
            }
            producto.setDetalleDevolucionList(attachedDetalleDevolucionList);
            em.persist(producto);
            for (DetalleVenta detalleVentaListDetalleVenta : producto.getDetalleVentaList()) {
                Producto oldCodigoProductoOfDetalleVentaListDetalleVenta = detalleVentaListDetalleVenta.getCodigoProducto();
                detalleVentaListDetalleVenta.setCodigoProducto(producto);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
                if (oldCodigoProductoOfDetalleVentaListDetalleVenta != null) {
                    oldCodigoProductoOfDetalleVentaListDetalleVenta.getDetalleVentaList().remove(detalleVentaListDetalleVenta);
                    oldCodigoProductoOfDetalleVentaListDetalleVenta = em.merge(oldCodigoProductoOfDetalleVentaListDetalleVenta);
                }
            }
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : producto.getDetalleDevolucionList()) {
                Producto oldCodigoProductoOfDetalleDevolucionListDetalleDevolucion = detalleDevolucionListDetalleDevolucion.getCodigoProducto();
                detalleDevolucionListDetalleDevolucion.setCodigoProducto(producto);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
                if (oldCodigoProductoOfDetalleDevolucionListDetalleDevolucion != null) {
                    oldCodigoProductoOfDetalleDevolucionListDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListDetalleDevolucion);
                    oldCodigoProductoOfDetalleDevolucionListDetalleDevolucion = em.merge(oldCodigoProductoOfDetalleDevolucionListDetalleDevolucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Producto producto) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Producto persistentProducto = em.find(Producto.class, producto.getCodigoProducto());
            List<DetalleVenta> detalleVentaListOld = persistentProducto.getDetalleVentaList();
            List<DetalleVenta> detalleVentaListNew = producto.getDetalleVentaList();
            List<DetalleDevolucion> detalleDevolucionListOld = persistentProducto.getDetalleDevolucionList();
            List<DetalleDevolucion> detalleDevolucionListNew = producto.getDetalleDevolucionList();
            List<DetalleVenta> attachedDetalleVentaListNew = new ArrayList<DetalleVenta>();
            for (DetalleVenta detalleVentaListNewDetalleVentaToAttach : detalleVentaListNew) {
                detalleVentaListNewDetalleVentaToAttach = em.getReference(detalleVentaListNewDetalleVentaToAttach.getClass(), detalleVentaListNewDetalleVentaToAttach.getFolioVenta());
                attachedDetalleVentaListNew.add(detalleVentaListNewDetalleVentaToAttach);
            }
            detalleVentaListNew = attachedDetalleVentaListNew;
            producto.setDetalleVentaList(detalleVentaListNew);
            List<DetalleDevolucion> attachedDetalleDevolucionListNew = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucionToAttach : detalleDevolucionListNew) {
                detalleDevolucionListNewDetalleDevolucionToAttach = em.getReference(detalleDevolucionListNewDetalleDevolucionToAttach.getClass(), detalleDevolucionListNewDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionListNew.add(detalleDevolucionListNewDetalleDevolucionToAttach);
            }
            detalleDevolucionListNew = attachedDetalleDevolucionListNew;
            producto.setDetalleDevolucionList(detalleDevolucionListNew);
            producto = em.merge(producto);
            for (DetalleVenta detalleVentaListOldDetalleVenta : detalleVentaListOld) {
                if (!detalleVentaListNew.contains(detalleVentaListOldDetalleVenta)) {
                    detalleVentaListOldDetalleVenta.setCodigoProducto(null);
                    detalleVentaListOldDetalleVenta = em.merge(detalleVentaListOldDetalleVenta);
                }
            }
            for (DetalleVenta detalleVentaListNewDetalleVenta : detalleVentaListNew) {
                if (!detalleVentaListOld.contains(detalleVentaListNewDetalleVenta)) {
                    Producto oldCodigoProductoOfDetalleVentaListNewDetalleVenta = detalleVentaListNewDetalleVenta.getCodigoProducto();
                    detalleVentaListNewDetalleVenta.setCodigoProducto(producto);
                    detalleVentaListNewDetalleVenta = em.merge(detalleVentaListNewDetalleVenta);
                    if (oldCodigoProductoOfDetalleVentaListNewDetalleVenta != null && !oldCodigoProductoOfDetalleVentaListNewDetalleVenta.equals(producto)) {
                        oldCodigoProductoOfDetalleVentaListNewDetalleVenta.getDetalleVentaList().remove(detalleVentaListNewDetalleVenta);
                        oldCodigoProductoOfDetalleVentaListNewDetalleVenta = em.merge(oldCodigoProductoOfDetalleVentaListNewDetalleVenta);
                    }
                }
            }
            for (DetalleDevolucion detalleDevolucionListOldDetalleDevolucion : detalleDevolucionListOld) {
                if (!detalleDevolucionListNew.contains(detalleDevolucionListOldDetalleDevolucion)) {
                    detalleDevolucionListOldDetalleDevolucion.setCodigoProducto(null);
                    detalleDevolucionListOldDetalleDevolucion = em.merge(detalleDevolucionListOldDetalleDevolucion);
                }
            }
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucion : detalleDevolucionListNew) {
                if (!detalleDevolucionListOld.contains(detalleDevolucionListNewDetalleDevolucion)) {
                    Producto oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion = detalleDevolucionListNewDetalleDevolucion.getCodigoProducto();
                    detalleDevolucionListNewDetalleDevolucion.setCodigoProducto(producto);
                    detalleDevolucionListNewDetalleDevolucion = em.merge(detalleDevolucionListNewDetalleDevolucion);
                    if (oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion != null && !oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion.equals(producto)) {
                        oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListNewDetalleDevolucion);
                        oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion = em.merge(oldCodigoProductoOfDetalleDevolucionListNewDetalleDevolucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = producto.getCodigoProducto();
                if (findProducto(id) == null) {
                    throw new NonexistentEntityException("The producto with id " + id + " no longer exists.");
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
            Producto producto;
            try {
                producto = em.getReference(Producto.class, id);
                producto.getCodigoProducto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The producto with id " + id + " no longer exists.", enfe);
            }
            List<DetalleVenta> detalleVentaList = producto.getDetalleVentaList();
            for (DetalleVenta detalleVentaListDetalleVenta : detalleVentaList) {
                detalleVentaListDetalleVenta.setCodigoProducto(null);
                detalleVentaListDetalleVenta = em.merge(detalleVentaListDetalleVenta);
            }
            List<DetalleDevolucion> detalleDevolucionList = producto.getDetalleDevolucionList();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : detalleDevolucionList) {
                detalleDevolucionListDetalleDevolucion.setCodigoProducto(null);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
            }
            em.remove(producto);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Producto> findProductoEntities() {
        return findProductoEntities(true, -1, -1);
    }

    public List<Producto> findProductoEntities(int maxResults, int firstResult) {
        return findProductoEntities(false, maxResults, firstResult);
    }

    private List<Producto> findProductoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Producto.class));
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

    public Producto findProducto(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Producto.class, id);
        } finally {
            em.close();
        }
    }
    
    public int findProductName(String name) {
        List <Producto> productos = findProductoEntities();
        if(productos.size()==0)
            return 0;
        int i = 0;
        while(i<productos.size()){
            if(productos.get(i).getNombre().equals(name))
                return productos.get(i).getCodigoProducto();
            i++;
        }
        return 0;
    }

    public int getProductoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Producto> rt = cq.from(Producto.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
