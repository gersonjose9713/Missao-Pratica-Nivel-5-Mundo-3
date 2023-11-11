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
import model.PessoaJuridica;

/**
 *
 * @author Kypz
 */
public class PessoaJuridicaJpaController implements Serializable {

    public PessoaJuridicaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaJuridica pessoaJuridica) throws IllegalOrphanException, PreexistingEntityException, Exception {
        List<String> illegalOrphanMessages = null;
        Pessoas pessoasOrphanCheck = pessoaJuridica.getPessoas();
        if (pessoasOrphanCheck != null) {
            PessoaJuridica oldPessoaJuridicaOfPessoas = pessoasOrphanCheck.getPessoaJuridica();
            if (oldPessoaJuridicaOfPessoas != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("The Pessoas " + pessoasOrphanCheck + " already has an item of type PessoaJuridica whose pessoas column cannot be null. Please make another selection for the pessoas field.");
            }
        }
        if (illegalOrphanMessages != null) {
            throw new IllegalOrphanException(illegalOrphanMessages);
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoas = pessoaJuridica.getPessoas();
            if (pessoas != null) {
                pessoas = em.getReference(pessoas.getClass(), pessoas.getId());
                pessoaJuridica.setPessoas(pessoas);
            }
            em.persist(pessoaJuridica);
            if (pessoas != null) {
                pessoas.setPessoaJuridica(pessoaJuridica);
                pessoas = em.merge(pessoas);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findPessoaJuridica(pessoaJuridica.getPessoaID()) != null) {
                throw new PreexistingEntityException("PessoaJuridica " + pessoaJuridica + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(PessoaJuridica pessoaJuridica) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica persistentPessoaJuridica = em.find(PessoaJuridica.class, pessoaJuridica.getPessoaID());
            Pessoas pessoasOld = persistentPessoaJuridica.getPessoas();
            Pessoas pessoasNew = pessoaJuridica.getPessoas();
            List<String> illegalOrphanMessages = null;
            if (pessoasNew != null && !pessoasNew.equals(pessoasOld)) {
                PessoaJuridica oldPessoaJuridicaOfPessoas = pessoasNew.getPessoaJuridica();
                if (oldPessoaJuridicaOfPessoas != null) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("The Pessoas " + pessoasNew + " already has an item of type PessoaJuridica whose pessoas column cannot be null. Please make another selection for the pessoas field.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pessoasNew != null) {
                pessoasNew = em.getReference(pessoasNew.getClass(), pessoasNew.getId());
                pessoaJuridica.setPessoas(pessoasNew);
            }
            pessoaJuridica = em.merge(pessoaJuridica);
            if (pessoasOld != null && !pessoasOld.equals(pessoasNew)) {
                pessoasOld.setPessoaJuridica(null);
                pessoasOld = em.merge(pessoasOld);
            }
            if (pessoasNew != null && !pessoasNew.equals(pessoasOld)) {
                pessoasNew.setPessoaJuridica(pessoaJuridica);
                pessoasNew = em.merge(pessoasNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoaJuridica.getPessoaID();
                if (findPessoaJuridica(id) == null) {
                    throw new NonexistentEntityException("The pessoaJuridica with id " + id + " no longer exists.");
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
            PessoaJuridica pessoaJuridica;
            try {
                pessoaJuridica = em.getReference(PessoaJuridica.class, id);
                pessoaJuridica.getPessoaID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoaJuridica with id " + id + " no longer exists.", enfe);
            }
            Pessoas pessoas = pessoaJuridica.getPessoas();
            if (pessoas != null) {
                pessoas.setPessoaJuridica(null);
                pessoas = em.merge(pessoas);
            }
            em.remove(pessoaJuridica);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<PessoaJuridica> findPessoaJuridicaEntities() {
        return findPessoaJuridicaEntities(true, -1, -1);
    }

    public List<PessoaJuridica> findPessoaJuridicaEntities(int maxResults, int firstResult) {
        return findPessoaJuridicaEntities(false, maxResults, firstResult);
    }

    private List<PessoaJuridica> findPessoaJuridicaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(PessoaJuridica.class));
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

    public PessoaJuridica findPessoaJuridica(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(PessoaJuridica.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaJuridicaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<PessoaJuridica> rt = cq.from(PessoaJuridica.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
