/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.IllegalOrphanException;
import controller.exceptions.NonexistentEntityException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.PessoaJuridica;
import model.PessoaFisica;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.Pessoas;

/**
 *
 * @author Kypz
 */
public class PessoasJpaController implements Serializable {

    public PessoasJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoas pessoas) {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica pessoaJuridica = pessoas.getPessoaJuridica();
            if (pessoaJuridica != null) {
                pessoaJuridica = em.getReference(pessoaJuridica.getClass(), pessoaJuridica.getPessoaID());
                pessoas.setPessoaJuridica(pessoaJuridica);
            }
            PessoaFisica pessoaFisica = pessoas.getPessoaFisica();
            if (pessoaFisica != null) {
                pessoaFisica = em.getReference(pessoaFisica.getClass(), pessoaFisica.getPessoaID());
                pessoas.setPessoaFisica(pessoaFisica);
            }
            em.persist(pessoas);
            if (pessoaJuridica != null) {
                Pessoas oldPessoasOfPessoaJuridica = pessoaJuridica.getPessoas();
                if (oldPessoasOfPessoaJuridica != null) {
                    oldPessoasOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoasOfPessoaJuridica = em.merge(oldPessoasOfPessoaJuridica);
                }
                pessoaJuridica.setPessoas(pessoas);
                pessoaJuridica = em.merge(pessoaJuridica);
            }
            if (pessoaFisica != null) {
                Pessoas oldPessoasOfPessoaFisica = pessoaFisica.getPessoas();
                if (oldPessoasOfPessoaFisica != null) {
                    oldPessoasOfPessoaFisica.setPessoaFisica(null);
                    oldPessoasOfPessoaFisica = em.merge(oldPessoasOfPessoaFisica);
                }
                pessoaFisica.setPessoas(pessoas);
                pessoaFisica = em.merge(pessoaFisica);
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoas pessoas) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas persistentPessoas = em.find(Pessoas.class, pessoas.getId());
            PessoaJuridica pessoaJuridicaOld = persistentPessoas.getPessoaJuridica();
            PessoaJuridica pessoaJuridicaNew = pessoas.getPessoaJuridica();
            PessoaFisica pessoaFisicaOld = persistentPessoas.getPessoaFisica();
            PessoaFisica pessoaFisicaNew = pessoas.getPessoaFisica();
            List<String> illegalOrphanMessages = null;
            if (pessoaJuridicaOld != null && !pessoaJuridicaOld.equals(pessoaJuridicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PessoaJuridica " + pessoaJuridicaOld + " since its pessoas field is not nullable.");
            }
            if (pessoaFisicaOld != null && !pessoaFisicaOld.equals(pessoaFisicaNew)) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("You must retain PessoaFisica " + pessoaFisicaOld + " since its pessoas field is not nullable.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (pessoaJuridicaNew != null) {
                pessoaJuridicaNew = em.getReference(pessoaJuridicaNew.getClass(), pessoaJuridicaNew.getPessoaID());
                pessoas.setPessoaJuridica(pessoaJuridicaNew);
            }
            if (pessoaFisicaNew != null) {
                pessoaFisicaNew = em.getReference(pessoaFisicaNew.getClass(), pessoaFisicaNew.getPessoaID());
                pessoas.setPessoaFisica(pessoaFisicaNew);
            }
            pessoas = em.merge(pessoas);
            if (pessoaJuridicaNew != null && !pessoaJuridicaNew.equals(pessoaJuridicaOld)) {
                Pessoas oldPessoasOfPessoaJuridica = pessoaJuridicaNew.getPessoas();
                if (oldPessoasOfPessoaJuridica != null) {
                    oldPessoasOfPessoaJuridica.setPessoaJuridica(null);
                    oldPessoasOfPessoaJuridica = em.merge(oldPessoasOfPessoaJuridica);
                }
                pessoaJuridicaNew.setPessoas(pessoas);
                pessoaJuridicaNew = em.merge(pessoaJuridicaNew);
            }
            if (pessoaFisicaNew != null && !pessoaFisicaNew.equals(pessoaFisicaOld)) {
                Pessoas oldPessoasOfPessoaFisica = pessoaFisicaNew.getPessoas();
                if (oldPessoasOfPessoaFisica != null) {
                    oldPessoasOfPessoaFisica.setPessoaFisica(null);
                    oldPessoasOfPessoaFisica = em.merge(oldPessoasOfPessoaFisica);
                }
                pessoaFisicaNew.setPessoas(pessoas);
                pessoaFisicaNew = em.merge(pessoaFisicaNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoas.getId();
                if (findPessoas(id) == null) {
                    throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoas pessoas;
            try {
                pessoas = em.getReference(Pessoas.class, id);
                pessoas.getId();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoas with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            PessoaJuridica pessoaJuridicaOrphanCheck = pessoas.getPessoaJuridica();
            if (pessoaJuridicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the PessoaJuridica " + pessoaJuridicaOrphanCheck + " in its pessoaJuridica field has a non-nullable pessoas field.");
            }
            PessoaFisica pessoaFisicaOrphanCheck = pessoas.getPessoaFisica();
            if (pessoaFisicaOrphanCheck != null) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoas (" + pessoas + ") cannot be destroyed since the PessoaFisica " + pessoaFisicaOrphanCheck + " in its pessoaFisica field has a non-nullable pessoas field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(pessoas);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoas> findPessoasEntities() {
        return findPessoasEntities(true, -1, -1);
    }

    public List<Pessoas> findPessoasEntities(int maxResults, int firstResult) {
        return findPessoasEntities(false, maxResults, firstResult);
    }

    private List<Pessoas> findPessoasEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoas.class));
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

    public Pessoas findPessoas(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoas.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoasCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoas> rt = cq.from(Pessoas.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
