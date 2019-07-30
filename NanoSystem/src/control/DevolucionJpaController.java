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
import modelo.DetalleDevolucion;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import modelo.Devolucion;

/**
 *
 * @author alega
 */
public class DevolucionJpaController implements Serializable {

    public DevolucionJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Devolucion devolucion) {
        if (devolucion.getDetalleDevolucionList() == null) {
            devolucion.setDetalleDevolucionList(new ArrayList<DetalleDevolucion>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Empleado idEmpleado = devolucion.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado = em.getReference(idEmpleado.getClass(), idEmpleado.getIdEmpleado());
                devolucion.setIdEmpleado(idEmpleado);
            }
            List<DetalleDevolucion> attachedDetalleDevolucionList = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucionToAttach : devolucion.getDetalleDevolucionList()) {
                detalleDevolucionListDetalleDevolucionToAttach = em.getReference(detalleDevolucionListDetalleDevolucionToAttach.getClass(), detalleDevolucionListDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionList.add(detalleDevolucionListDetalleDevolucionToAttach);
            }
            devolucion.setDetalleDevolucionList(attachedDetalleDevolucionList);
            em.persist(devolucion);
            if (idEmpleado != null) {
                idEmpleado.getDevolucionList().add(devolucion);
                idEmpleado = em.merge(idEmpleado);
            }
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : devolucion.getDetalleDevolucionList()) {
                Devolucion oldIdDevOfDetalleDevolucionListDetalleDevolucion = detalleDevolucionListDetalleDevolucion.getIdDev();
                detalleDevolucionListDetalleDevolucion.setIdDev(devolucion);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
                if (oldIdDevOfDetalleDevolucionListDetalleDevolucion != null) {
                    oldIdDevOfDetalleDevolucionListDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListDetalleDevolucion);
                    oldIdDevOfDetalleDevolucionListDetalleDevolucion = em.merge(oldIdDevOfDetalleDevolucionListDetalleDevolucion);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Devolucion devolucion) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Devolucion persistentDevolucion = em.find(Devolucion.class, devolucion.getIdDev());
            Empleado idEmpleadoOld = persistentDevolucion.getIdEmpleado();
            Empleado idEmpleadoNew = devolucion.getIdEmpleado();
            List<DetalleDevolucion> detalleDevolucionListOld = persistentDevolucion.getDetalleDevolucionList();
            List<DetalleDevolucion> detalleDevolucionListNew = devolucion.getDetalleDevolucionList();
            if (idEmpleadoNew != null) {
                idEmpleadoNew = em.getReference(idEmpleadoNew.getClass(), idEmpleadoNew.getIdEmpleado());
                devolucion.setIdEmpleado(idEmpleadoNew);
            }
            List<DetalleDevolucion> attachedDetalleDevolucionListNew = new ArrayList<DetalleDevolucion>();
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucionToAttach : detalleDevolucionListNew) {
                detalleDevolucionListNewDetalleDevolucionToAttach = em.getReference(detalleDevolucionListNewDetalleDevolucionToAttach.getClass(), detalleDevolucionListNewDetalleDevolucionToAttach.getFolioDevolucion());
                attachedDetalleDevolucionListNew.add(detalleDevolucionListNewDetalleDevolucionToAttach);
            }
            detalleDevolucionListNew = attachedDetalleDevolucionListNew;
            devolucion.setDetalleDevolucionList(detalleDevolucionListNew);
            devolucion = em.merge(devolucion);
            if (idEmpleadoOld != null && !idEmpleadoOld.equals(idEmpleadoNew)) {
                idEmpleadoOld.getDevolucionList().remove(devolucion);
                idEmpleadoOld = em.merge(idEmpleadoOld);
            }
            if (idEmpleadoNew != null && !idEmpleadoNew.equals(idEmpleadoOld)) {
                idEmpleadoNew.getDevolucionList().add(devolucion);
                idEmpleadoNew = em.merge(idEmpleadoNew);
            }
            for (DetalleDevolucion detalleDevolucionListOldDetalleDevolucion : detalleDevolucionListOld) {
                if (!detalleDevolucionListNew.contains(detalleDevolucionListOldDetalleDevolucion)) {
                    detalleDevolucionListOldDetalleDevolucion.setIdDev(null);
                    detalleDevolucionListOldDetalleDevolucion = em.merge(detalleDevolucionListOldDetalleDevolucion);
                }
            }
            for (DetalleDevolucion detalleDevolucionListNewDetalleDevolucion : detalleDevolucionListNew) {
                if (!detalleDevolucionListOld.contains(detalleDevolucionListNewDetalleDevolucion)) {
                    Devolucion oldIdDevOfDetalleDevolucionListNewDetalleDevolucion = detalleDevolucionListNewDetalleDevolucion.getIdDev();
                    detalleDevolucionListNewDetalleDevolucion.setIdDev(devolucion);
                    detalleDevolucionListNewDetalleDevolucion = em.merge(detalleDevolucionListNewDetalleDevolucion);
                    if (oldIdDevOfDetalleDevolucionListNewDetalleDevolucion != null && !oldIdDevOfDetalleDevolucionListNewDetalleDevolucion.equals(devolucion)) {
                        oldIdDevOfDetalleDevolucionListNewDetalleDevolucion.getDetalleDevolucionList().remove(detalleDevolucionListNewDetalleDevolucion);
                        oldIdDevOfDetalleDevolucionListNewDetalleDevolucion = em.merge(oldIdDevOfDetalleDevolucionListNewDetalleDevolucion);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = devolucion.getIdDev();
                if (findDevolucion(id) == null) {
                    throw new NonexistentEntityException("The devolucion with id " + id + " no longer exists.");
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
            Devolucion devolucion;
            try {
                devolucion = em.getReference(Devolucion.class, id);
                devolucion.getIdDev();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The devolucion with id " + id + " no longer exists.", enfe);
            }
            Empleado idEmpleado = devolucion.getIdEmpleado();
            if (idEmpleado != null) {
                idEmpleado.getDevolucionList().remove(devolucion);
                idEmpleado = em.merge(idEmpleado);
            }
            List<DetalleDevolucion> detalleDevolucionList = devolucion.getDetalleDevolucionList();
            for (DetalleDevolucion detalleDevolucionListDetalleDevolucion : detalleDevolucionList) {
                detalleDevolucionListDetalleDevolucion.setIdDev(null);
                detalleDevolucionListDetalleDevolucion = em.merge(detalleDevolucionListDetalleDevolucion);
            }
            em.remove(devolucion);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Devolucion> findDevolucionEntities() {
        return findDevolucionEntities(true, -1, -1);
    }

    public List<Devolucion> findDevolucionEntities(int maxResults, int firstResult) {
        return findDevolucionEntities(false, maxResults, firstResult);
    }

    private List<Devolucion> findDevolucionEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Devolucion.class));
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

    public Devolucion findDevolucion(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Devolucion.class, id);
        } finally {
            em.close();
        }
    }

    public int getDevolucionCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Devolucion> rt = cq.from(Devolucion.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
