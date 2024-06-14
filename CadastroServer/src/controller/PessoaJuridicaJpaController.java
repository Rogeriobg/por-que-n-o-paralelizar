/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import controller.exceptions.NonexistentEntityException;
import controller.exceptions.PreexistingEntityException;
import java.io.Serializable;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import model.Pessoa;
import model.PessoaJuridica;

/**
 *
 * @author rbgor
 */
public class PessoaJuridicaJpaController implements Serializable {
 private EntityManagerFactory emf;
    public PessoaJuridicaJpaController(EntityManagerFactory emf) {
        this.emf = Persistence.createEntityManagerFactory("CadastroServerPU");
    }
   

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(PessoaJuridica pessoaJuridica) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoaPessoaID = pessoaJuridica.getPessoaPessoaID();
            if (pessoaPessoaID != null) {
                pessoaPessoaID = em.getReference(pessoaPessoaID.getClass(), pessoaPessoaID.getPessoaID());
                pessoaJuridica.setPessoaPessoaID(pessoaPessoaID);
            }
            em.persist(pessoaJuridica);
            if (pessoaPessoaID != null) {
                pessoaPessoaID.getPessoaJuridicaCollection().add(pessoaJuridica);
                pessoaPessoaID = em.merge(pessoaPessoaID);
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

    public void edit(PessoaJuridica pessoaJuridica) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            PessoaJuridica persistentPessoaJuridica = em.find(PessoaJuridica.class, pessoaJuridica.getPessoaID());
            Pessoa pessoaPessoaIDOld = persistentPessoaJuridica.getPessoaPessoaID();
            Pessoa pessoaPessoaIDNew = pessoaJuridica.getPessoaPessoaID();
            if (pessoaPessoaIDNew != null) {
                pessoaPessoaIDNew = em.getReference(pessoaPessoaIDNew.getClass(), pessoaPessoaIDNew.getPessoaID());
                pessoaJuridica.setPessoaPessoaID(pessoaPessoaIDNew);
            }
            pessoaJuridica = em.merge(pessoaJuridica);
            if (pessoaPessoaIDOld != null && !pessoaPessoaIDOld.equals(pessoaPessoaIDNew)) {
                pessoaPessoaIDOld.getPessoaJuridicaCollection().remove(pessoaJuridica);
                pessoaPessoaIDOld = em.merge(pessoaPessoaIDOld);
            }
            if (pessoaPessoaIDNew != null && !pessoaPessoaIDNew.equals(pessoaPessoaIDOld)) {
                pessoaPessoaIDNew.getPessoaJuridicaCollection().add(pessoaJuridica);
                pessoaPessoaIDNew = em.merge(pessoaPessoaIDNew);
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
            Pessoa pessoaPessoaID = pessoaJuridica.getPessoaPessoaID();
            if (pessoaPessoaID != null) {
                pessoaPessoaID.getPessoaJuridicaCollection().remove(pessoaJuridica);
                pessoaPessoaID = em.merge(pessoaPessoaID);
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
