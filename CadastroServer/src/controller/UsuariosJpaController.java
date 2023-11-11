/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Movimentos;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Usuarios;

/**
 *
 * @author Kypz
 */
public class UsuariosJpaController implements Serializable {
    
    public Usuarios findUsuario(String login, String senha) {
        EntityManager em = getEntityManager();
        try {
            Query query = em.createQuery("SELECT u FROM Usuarios u WHERE u.nome = :login AND u.senha = :senha");
            query.setParameter("login", login);
            query.setParameter("senha", senha);
            List<Usuarios> results = query.getResultList();
            if (!results.isEmpty()) {
                return results.get(0);
            }
            return null;
        } finally {
            em.close();
        }
    }

    public UsuariosJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuarios usuarios) {
        if (usuarios.getMovimentosCollection() == null) {
            usuarios.setMovimentosCollection(new ArrayList<Movimentos>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<Movimentos> attachedMovimentosCollection = new ArrayList<Movimentos>();
            for (Movimentos movimentosCollectionMovimentosToAttach : usuarios.getMovimentosCollection()) {
                movimentosCollectionMovimentosToAttach = em.getReference(movimentosCollectionMovimentosToAttach.getClass(), movimentosCollectionMovimentosToAttach.getId());
                attachedMovimentosCollection.add(movimentosCollectionMovimentosToAttach);
            }
            usuarios.setMovimentosCollection(attachedMovimentosCollection);
            em.persist(usuarios);
            for (Movimentos movimentosCollectionMovimentos : usuarios.getMovimentosCollection()) {
                Usuarios oldOperadorIDOfMovimentosCollectionMovimentos = movimentosCollectionMovimentos.getOperadorID();
                movimentosCollectionMovimentos.setOperadorID(usuarios);
                movimentosCollectionMovimentos = em.merge(movimentosCollectionMovimentos);
                if (oldOperadorIDOfMovimentosCollectionMovimentos != null) {
                    oldOperadorIDOfMovimentosCollectionMovimentos.getMovimentosCollection().remove(movimentosCollectionMovimentos);
                    oldOperadorIDOfMovimentosCollectionMovimentos = em.merge(oldOperadorIDOfMovimentosCollectionMovimentos);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Usuarios usuarios) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Usuarios persistentUsuarios = em.find(Usuarios.class, usuarios.getId());
            Collection<Movimentos> movimentosCollectionOld = persistentUsuarios.getMovimentosCollection();
            Collection<Movimentos> movimentosCollectionNew = usuarios.getMovimentosCollection();
            Collection<Movimentos> attachedMovimentosCollectionNew = new ArrayList<Movimentos>();
            for (Movimentos movimentosCollectionNewMovimentosToAttach : movimentosCollectionNew) {
                movimentosCollectionNewMovimentosToAttach = em.getReference(movimentosCollectionNewMovimentosToAttach.getClass(), movimentosCollectionNewMovimentosToAttach.getId());
                attachedMovimentosCollectionNew.add(movimentosCollectionNewMovimentosToAttach);
            }
            movimentosCollectionNew = attachedMovimentosCollectionNew;
            usuarios.setMovimentosCollection(movimentosCollectionNew);
            usuarios = em.merge(usuarios);
            for (Movimentos movimentosCollectionOldMovimentos : movimentosCollectionOld) {
                if (!movimentosCollectionNew.contains(movimentosCollectionOldMovimentos)) {
                    movimentosCollectionOldMovimentos.setOperadorID(null);
                    movimentosCollectionOldMovimentos = em.merge(movimentosCollectionOldMovimentos);
                }
            }
            for (Movimentos movimentosCollectionNewMovimentos : movimentosCollectionNew) {
                if (!movimentosCollectionOld.contains(movimentosCollectionNewMovimentos)) {
                    Usuarios oldOperadorIDOfMovimentosCollectionNewMovimentos = movimentosCollectionNewMovimentos.getOperadorID();
                    movimentosCollectionNewMovimentos.setOperadorID(usuarios);
                    movimentosCollectionNewMovimentos = em.merge(movimentosCollectionNewMovimentos);
                    if (oldOperadorIDOfMovimentosCollectionNewMovimentos != null && !oldOperadorIDOfMovimentosCollectionNewMovimentos.equals(usuarios)) {
                        oldOperadorIDOfMovimentosCollectionNewMovimentos.getMovimentosCollection().remove(movimentosCollectionNewMovimentos);
                        oldOperadorIDOfMovimentosCollectionNewMovimentos = em.merge(oldOperadorIDOfMovimentosCollectionNewMovimentos);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = usuarios.getId();
                if (findUsuarios(id) == null) {
                    throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.");
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
            Usuarios usuarios;
            try {
                usuarios = em.getReference(Usuarios.class, id);
                usuarios.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuarios with id " + id + " no longer exists.", enfe);
            }
            Collection<Movimentos> movimentosCollection = usuarios.getMovimentosCollection();
            for (Movimentos movimentosCollectionMovimentos : movimentosCollection) {
                movimentosCollectionMovimentos.setOperadorID(null);
                movimentosCollectionMovimentos = em.merge(movimentosCollectionMovimentos);
            }
            em.remove(usuarios);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Usuarios> findUsuariosEntities() {
        return findUsuariosEntities(true, -1, -1);
    }

    public List<Usuarios> findUsuariosEntities(int maxResults, int firstResult) {
        return findUsuariosEntities(false, maxResults, firstResult);
    }

    private List<Usuarios> findUsuariosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuarios.class));
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

    public Usuarios findUsuarios(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuarios.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuariosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuarios> rt = cq.from(Usuarios.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
