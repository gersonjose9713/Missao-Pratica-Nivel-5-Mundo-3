/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimentos;
import model.Produtos;
import model.Usuarios;

/**
 *
 * @author Kypz
 */
public class MovimentosJpaController implements Serializable {

    public MovimentosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimentos movimentos) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Produtos produtoID = movimentos.getProdutoID();
            if (produtoID != null) {
                produtoID = em.getReference(produtoID.getClass(), produtoID.getId());
                movimentos.setProdutoID(produtoID);
            }
            Usuarios operadorID = movimentos.getOperadorID();
            if (operadorID != null) {
                operadorID = em.getReference(operadorID.getClass(), operadorID.getId());
                movimentos.setOperadorID(operadorID);
            }
            em.persist(movimentos);
            if (produtoID != null) {
                produtoID.getMovimentosCollection().add(movimentos);
                produtoID = em.merge(produtoID);
            }
            if (operadorID != null) {
                operadorID.getMovimentosCollection().add(movimentos);
                operadorID = em.merge(operadorID);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimentos movimentos) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimentos persistentMovimentos = em.find(Movimentos.class, movimentos.getId());
            Produtos produtoIDOld = persistentMovimentos.getProdutoID();
            Produtos produtoIDNew = movimentos.getProdutoID();
            Usuarios operadorIDOld = persistentMovimentos.getOperadorID();
            Usuarios operadorIDNew = movimentos.getOperadorID();
            if (produtoIDNew != null) {
                produtoIDNew = em.getReference(produtoIDNew.getClass(), produtoIDNew.getId());
                movimentos.setProdutoID(produtoIDNew);
            }
            if (operadorIDNew != null) {
                operadorIDNew = em.getReference(operadorIDNew.getClass(), operadorIDNew.getId());
                movimentos.setOperadorID(operadorIDNew);
            }
            movimentos = em.merge(movimentos);
            if (produtoIDOld != null && !produtoIDOld.equals(produtoIDNew)) {
                produtoIDOld.getMovimentosCollection().remove(movimentos);
                produtoIDOld = em.merge(produtoIDOld);
            }
            if (produtoIDNew != null && !produtoIDNew.equals(produtoIDOld)) {
                produtoIDNew.getMovimentosCollection().add(movimentos);
                produtoIDNew = em.merge(produtoIDNew);
            }
            if (operadorIDOld != null && !operadorIDOld.equals(operadorIDNew)) {
                operadorIDOld.getMovimentosCollection().remove(movimentos);
                operadorIDOld = em.merge(operadorIDOld);
            }
            if (operadorIDNew != null && !operadorIDNew.equals(operadorIDOld)) {
                operadorIDNew.getMovimentosCollection().add(movimentos);
                operadorIDNew = em.merge(operadorIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimentos.getId();
                if (findMovimentos(id) == null) {
                    throw new NonexistentEntityException("The movimentos with id " + id + " no longer exists.");
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
            Movimentos movimentos;
            try {
                movimentos = em.getReference(Movimentos.class, id);
                movimentos.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimentos with id " + id + " no longer exists.", enfe);
            }
            Produtos produtoID = movimentos.getProdutoID();
            if (produtoID != null) {
                produtoID.getMovimentosCollection().remove(movimentos);
                produtoID = em.merge(produtoID);
            }
            Usuarios operadorID = movimentos.getOperadorID();
            if (operadorID != null) {
                operadorID.getMovimentosCollection().remove(movimentos);
                operadorID = em.merge(operadorID);
            }
            em.remove(movimentos);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimentos> findMovimentosEntities() {
        return findMovimentosEntities(true, -1, -1);
    }

    public List<Movimentos> findMovimentosEntities(int maxResults, int firstResult) {
        return findMovimentosEntities(false, maxResults, firstResult);
    }

    private List<Movimentos> findMovimentosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimentos.class));
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

    public Movimentos findMovimentos(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimentos.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimentos> rt = cq.from(Movimentos.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
