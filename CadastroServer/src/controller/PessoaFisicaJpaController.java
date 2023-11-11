/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoas;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.PessoaFisica;

/**
 *
 * @author Kypz
 */
public class PessoaFisicaJpaController implements Serializable {

    public PessoaFisicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaFisica pessoaFisica) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pessoas pessoasOrphanCheck = pessoaFisica.getPessoas();
        if (pessoasOrphanCheck != null) {
            PessoaFisica oldPessoaFisicaOfPessoas = pessoasOrphanCheck.getPessoaFisica();
            if (oldPessoaFisicaOfPessoas != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pessoas " + pessoasOrphanCheck + " already has an item of type PessoaFisica whose pessoas column cannot be null. Please make another selection for the pessoas field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoas = pessoaFisica.getPessoas();
            if (pessoas != null) {
                pessoas = em.getReference(pessoas.getClass(), pessoas.getId());
                pessoaFisica.setPessoas(pessoas);
            }
            em.persist(pessoaFisica);
            if (pessoas != null) {
                pessoas.setPessoaFisica(pessoaFisica);
                pessoas = em.merge(pessoas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoaFisica(pessoaFisica.getPessoaID()) != null) {
                throw new PreexistingEntityException("PessoaFisica " + pessoaFisica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaFisica pessoaFisica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaFisica persistentPessoaFisica = em.find(PessoaFisica.class, pessoaFisica.getPessoaID());
            Pessoas pessoasOld = persistentPessoaFisica.getPessoas();
            Pessoas pessoasNew = pessoaFisica.getPessoas();
            List<String> illegalOrphanMessages = null;
            if (pessoasNew != null && !pessoasNew.equals(pessoasOld)) {
                PessoaFisica oldPessoaFisicaOfPessoas = pessoasNew.getPessoaFisica();
                if (oldPessoaFisicaOfPessoas != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pessoas " + pessoasNew + " already has an item of type PessoaFisica whose pessoas column cannot be null. Please make another selection for the pessoas field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pessoasNew != null) {
                pessoasNew = em.getReference(pessoasNew.getClass(), pessoasNew.getId());
                pessoaFisica.setPessoas(pessoasNew);
            }
            pessoaFisica = em.merge(pessoaFisica);
            if (pessoasOld != null && !pessoasOld.equals(pessoasNew)) {
                pessoasOld.setPessoaFisica(null);
                pessoasOld = em.merge(pessoasOld);
            }
            if (pessoasNew != null && !pessoasNew.equals(pessoasOld)) {
                pessoasNew.setPessoaFisica(pessoaFisica);
                pessoasNew = em.merge(pessoasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoaFisica.getPessoaID();
                if (findPessoaFisica(id) == null) {
                    throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.");
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
            PessoaFisica pessoaFisica;
            try {
                pessoaFisica = em.getReference(PessoaFisica.class, id);
                pessoaFisica.getPessoaID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoaFisica with id " + id + " no longer exists.", enfe);
            }
            Pessoas pessoas = pessoaFisica.getPessoas();
            if (pessoas != null) {
                pessoas.setPessoaFisica(null);
                pessoas = em.merge(pessoas);
            }
            em.remove(pessoaFisica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoaFisica> findPessoaFisicaEntities() {
        return findPessoaFisicaEntities(true, -1, -1);
    }

    public List<PessoaFisica> findPessoaFisicaEntities(int maxResults, int firstResult) {
        return findPessoaFisicaEntities(false, maxResults, firstResult);
    }

    private List<PessoaFisica> findPessoaFisicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoaFisica.class));
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

    public PessoaFisica findPessoaFisica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoaFisica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaFisicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoaFisica> rt = cq.from(PessoaFisica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
