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
import model.Movimento;
import model.Pessoa;
import model.Produto;

/**
 *
 * @author rbgor
 */
public class MovimentoJpaController implements Serializable {
   private EntityManagerFactory emf;
    
    public MovimentoJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
 

    public MovimentoJpaController() {
        this.emf = Persistence.createEntityManagerFactory("CadastroServerPU");
       // throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Movimento movimento) throws PreexistingEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa pessoaID = movimento.getPessoaID();
            if (pessoaID != null) {
                pessoaID = em.getReference(pessoaID.getClass(), pessoaID.getPessoaID());
                movimento.setPessoaID(pessoaID);
            }
            Produto produtoID = movimento.getProdutoID();
            if (produtoID != null) {
                produtoID = em.getReference(produtoID.getClass(), produtoID.getProdutoID());
                movimento.setProdutoID(produtoID);
            }
            em.persist(movimento);
            if (pessoaID != null) {
                pessoaID.getMovimentoCollection().add(movimento);
                pessoaID = em.merge(pessoaID);
            }
            if (produtoID != null) {
                produtoID.getMovimentoCollection().add(movimento);
                produtoID = em.merge(produtoID);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            if (findMovimento(movimento.getMovimentoID()) != null) {
                throw new PreexistingEntityException("Movimento " + movimento + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Movimento movimento) throws NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Movimento persistentMovimento = em.find(Movimento.class, movimento.getMovimentoID());
            Pessoa pessoaIDOld = persistentMovimento.getPessoaID();
            Pessoa pessoaIDNew = movimento.getPessoaID();
            Produto produtoIDOld = persistentMovimento.getProdutoID();
            Produto produtoIDNew = movimento.getProdutoID();
            if (pessoaIDNew != null) {
                pessoaIDNew = em.getReference(pessoaIDNew.getClass(), pessoaIDNew.getPessoaID());
                movimento.setPessoaID(pessoaIDNew);
            }
            if (produtoIDNew != null) {
                produtoIDNew = em.getReference(produtoIDNew.getClass(), produtoIDNew.getProdutoID());
                movimento.setProdutoID(produtoIDNew);
            }
            movimento = em.merge(movimento);
            if (pessoaIDOld != null && !pessoaIDOld.equals(pessoaIDNew)) {
                pessoaIDOld.getMovimentoCollection().remove(movimento);
                pessoaIDOld = em.merge(pessoaIDOld);
            }
            if (pessoaIDNew != null && !pessoaIDNew.equals(pessoaIDOld)) {
                pessoaIDNew.getMovimentoCollection().add(movimento);
                pessoaIDNew = em.merge(pessoaIDNew);
            }
            if (produtoIDOld != null && !produtoIDOld.equals(produtoIDNew)) {
                produtoIDOld.getMovimentoCollection().remove(movimento);
                produtoIDOld = em.merge(produtoIDOld);
            }
            if (produtoIDNew != null && !produtoIDNew.equals(produtoIDOld)) {
                produtoIDNew.getMovimentoCollection().add(movimento);
                produtoIDNew = em.merge(produtoIDNew);
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = movimento.getMovimentoID();
                if (findMovimento(id) == null) {
                    throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.");
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
            Movimento movimento;
            try {
                movimento = em.getReference(Movimento.class, id);
                movimento.getMovimentoID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The movimento with id " + id + " no longer exists.", enfe);
            }
            Pessoa pessoaID = movimento.getPessoaID();
            if (pessoaID != null) {
                pessoaID.getMovimentoCollection().remove(movimento);
                pessoaID = em.merge(pessoaID);
            }
            Produto produtoID = movimento.getProdutoID();
            if (produtoID != null) {
                produtoID.getMovimentoCollection().remove(movimento);
                produtoID = em.merge(produtoID);
            }
            em.remove(movimento);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Movimento> findMovimentoEntities() {
        return findMovimentoEntities(true, -1, -1);
    }

    public List<Movimento> findMovimentoEntities(int maxResults, int firstResult) {
        return findMovimentoEntities(false, maxResults, firstResult);
    }

    private List<Movimento> findMovimentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Movimento.class));
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

    public Movimento findMovimento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Movimento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMovimentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Movimento> rt = cq.from(Movimento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
