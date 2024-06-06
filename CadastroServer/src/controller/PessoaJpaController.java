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
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import model.PessoaFisica;
import model.Movimento;
import model.Pessoa;

/**
 *
 * @author rbgor
 */
public class PessoaJpaController implements Serializable {

    public PessoaJpaController(EntityManagerFactory emf) {
        this.emf = emf;
    }
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pessoa pessoa) {
        if (pessoa.getPessoaJuridicaCollection() == null) {
            pessoa.setPessoaJuridicaCollection(new ArrayList<PessoaJuridica>());
        }
        if (pessoa.getPessoaFisicaCollection() == null) {
            pessoa.setPessoaFisicaCollection(new ArrayList<PessoaFisica>());
        }
        if (pessoa.getMovimentoCollection() == null) {
            pessoa.setMovimentoCollection(new ArrayList<Movimento>());
        }
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Collection<PessoaJuridica> attachedPessoaJuridicaCollection = new ArrayList<PessoaJuridica>();
            for (PessoaJuridica pessoaJuridicaCollectionPessoaJuridicaToAttach : pessoa.getPessoaJuridicaCollection()) {
                pessoaJuridicaCollectionPessoaJuridicaToAttach = em.getReference(pessoaJuridicaCollectionPessoaJuridicaToAttach.getClass(), pessoaJuridicaCollectionPessoaJuridicaToAttach.getPessoaID());
                attachedPessoaJuridicaCollection.add(pessoaJuridicaCollectionPessoaJuridicaToAttach);
            }
            pessoa.setPessoaJuridicaCollection(attachedPessoaJuridicaCollection);
            Collection<PessoaFisica> attachedPessoaFisicaCollection = new ArrayList<PessoaFisica>();
            for (PessoaFisica pessoaFisicaCollectionPessoaFisicaToAttach : pessoa.getPessoaFisicaCollection()) {
                pessoaFisicaCollectionPessoaFisicaToAttach = em.getReference(pessoaFisicaCollectionPessoaFisicaToAttach.getClass(), pessoaFisicaCollectionPessoaFisicaToAttach.getPessoaID());
                attachedPessoaFisicaCollection.add(pessoaFisicaCollectionPessoaFisicaToAttach);
            }
            pessoa.setPessoaFisicaCollection(attachedPessoaFisicaCollection);
            Collection<Movimento> attachedMovimentoCollection = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionMovimentoToAttach : pessoa.getMovimentoCollection()) {
                movimentoCollectionMovimentoToAttach = em.getReference(movimentoCollectionMovimentoToAttach.getClass(), movimentoCollectionMovimentoToAttach.getMovimentoID());
                attachedMovimentoCollection.add(movimentoCollectionMovimentoToAttach);
            }
            pessoa.setMovimentoCollection(attachedMovimentoCollection);
            em.persist(pessoa);
            for (PessoaJuridica pessoaJuridicaCollectionPessoaJuridica : pessoa.getPessoaJuridicaCollection()) {
                Pessoa oldPessoaPessoaIDOfPessoaJuridicaCollectionPessoaJuridica = pessoaJuridicaCollectionPessoaJuridica.getPessoaPessoaID();
                pessoaJuridicaCollectionPessoaJuridica.setPessoaPessoaID(pessoa);
                pessoaJuridicaCollectionPessoaJuridica = em.merge(pessoaJuridicaCollectionPessoaJuridica);
                if (oldPessoaPessoaIDOfPessoaJuridicaCollectionPessoaJuridica != null) {
                    oldPessoaPessoaIDOfPessoaJuridicaCollectionPessoaJuridica.getPessoaJuridicaCollection().remove(pessoaJuridicaCollectionPessoaJuridica);
                    oldPessoaPessoaIDOfPessoaJuridicaCollectionPessoaJuridica = em.merge(oldPessoaPessoaIDOfPessoaJuridicaCollectionPessoaJuridica);
                }
            }
            for (PessoaFisica pessoaFisicaCollectionPessoaFisica : pessoa.getPessoaFisicaCollection()) {
                Pessoa oldPessoaPessoaIDOfPessoaFisicaCollectionPessoaFisica = pessoaFisicaCollectionPessoaFisica.getPessoaPessoaID();
                pessoaFisicaCollectionPessoaFisica.setPessoaPessoaID(pessoa);
                pessoaFisicaCollectionPessoaFisica = em.merge(pessoaFisicaCollectionPessoaFisica);
                if (oldPessoaPessoaIDOfPessoaFisicaCollectionPessoaFisica != null) {
                    oldPessoaPessoaIDOfPessoaFisicaCollectionPessoaFisica.getPessoaFisicaCollection().remove(pessoaFisicaCollectionPessoaFisica);
                    oldPessoaPessoaIDOfPessoaFisicaCollectionPessoaFisica = em.merge(oldPessoaPessoaIDOfPessoaFisicaCollectionPessoaFisica);
                }
            }
            for (Movimento movimentoCollectionMovimento : pessoa.getMovimentoCollection()) {
                Pessoa oldPessoaIDOfMovimentoCollectionMovimento = movimentoCollectionMovimento.getPessoaID();
                movimentoCollectionMovimento.setPessoaID(pessoa);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
                if (oldPessoaIDOfMovimentoCollectionMovimento != null) {
                    oldPessoaIDOfMovimentoCollectionMovimento.getMovimentoCollection().remove(movimentoCollectionMovimento);
                    oldPessoaIDOfMovimentoCollectionMovimento = em.merge(oldPessoaIDOfMovimentoCollectionMovimento);
                }
            }
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Pessoa pessoa) throws IllegalOrphanException, NonexistentEntityException, Exception {
        EntityManager em = null;
        try {
            em = getEntityManager();
            em.getTransaction().begin();
            Pessoa persistentPessoa = em.find(Pessoa.class, pessoa.getPessoaID());
            Collection<PessoaJuridica> pessoaJuridicaCollectionOld = persistentPessoa.getPessoaJuridicaCollection();
            Collection<PessoaJuridica> pessoaJuridicaCollectionNew = pessoa.getPessoaJuridicaCollection();
            Collection<PessoaFisica> pessoaFisicaCollectionOld = persistentPessoa.getPessoaFisicaCollection();
            Collection<PessoaFisica> pessoaFisicaCollectionNew = pessoa.getPessoaFisicaCollection();
            Collection<Movimento> movimentoCollectionOld = persistentPessoa.getMovimentoCollection();
            Collection<Movimento> movimentoCollectionNew = pessoa.getMovimentoCollection();
            List<String> illegalOrphanMessages = null;
            for (PessoaJuridica pessoaJuridicaCollectionOldPessoaJuridica : pessoaJuridicaCollectionOld) {
                if (!pessoaJuridicaCollectionNew.contains(pessoaJuridicaCollectionOldPessoaJuridica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PessoaJuridica " + pessoaJuridicaCollectionOldPessoaJuridica + " since its pessoaPessoaID field is not nullable.");
                }
            }
            for (PessoaFisica pessoaFisicaCollectionOldPessoaFisica : pessoaFisicaCollectionOld) {
                if (!pessoaFisicaCollectionNew.contains(pessoaFisicaCollectionOldPessoaFisica)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PessoaFisica " + pessoaFisicaCollectionOldPessoaFisica + " since its pessoaPessoaID field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<PessoaJuridica> attachedPessoaJuridicaCollectionNew = new ArrayList<PessoaJuridica>();
            for (PessoaJuridica pessoaJuridicaCollectionNewPessoaJuridicaToAttach : pessoaJuridicaCollectionNew) {
                pessoaJuridicaCollectionNewPessoaJuridicaToAttach = em.getReference(pessoaJuridicaCollectionNewPessoaJuridicaToAttach.getClass(), pessoaJuridicaCollectionNewPessoaJuridicaToAttach.getPessoaID());
                attachedPessoaJuridicaCollectionNew.add(pessoaJuridicaCollectionNewPessoaJuridicaToAttach);
            }
            pessoaJuridicaCollectionNew = attachedPessoaJuridicaCollectionNew;
            pessoa.setPessoaJuridicaCollection(pessoaJuridicaCollectionNew);
            Collection<PessoaFisica> attachedPessoaFisicaCollectionNew = new ArrayList<PessoaFisica>();
            for (PessoaFisica pessoaFisicaCollectionNewPessoaFisicaToAttach : pessoaFisicaCollectionNew) {
                pessoaFisicaCollectionNewPessoaFisicaToAttach = em.getReference(pessoaFisicaCollectionNewPessoaFisicaToAttach.getClass(), pessoaFisicaCollectionNewPessoaFisicaToAttach.getPessoaID());
                attachedPessoaFisicaCollectionNew.add(pessoaFisicaCollectionNewPessoaFisicaToAttach);
            }
            pessoaFisicaCollectionNew = attachedPessoaFisicaCollectionNew;
            pessoa.setPessoaFisicaCollection(pessoaFisicaCollectionNew);
            Collection<Movimento> attachedMovimentoCollectionNew = new ArrayList<Movimento>();
            for (Movimento movimentoCollectionNewMovimentoToAttach : movimentoCollectionNew) {
                movimentoCollectionNewMovimentoToAttach = em.getReference(movimentoCollectionNewMovimentoToAttach.getClass(), movimentoCollectionNewMovimentoToAttach.getMovimentoID());
                attachedMovimentoCollectionNew.add(movimentoCollectionNewMovimentoToAttach);
            }
            movimentoCollectionNew = attachedMovimentoCollectionNew;
            pessoa.setMovimentoCollection(movimentoCollectionNew);
            pessoa = em.merge(pessoa);
            for (PessoaJuridica pessoaJuridicaCollectionNewPessoaJuridica : pessoaJuridicaCollectionNew) {
                if (!pessoaJuridicaCollectionOld.contains(pessoaJuridicaCollectionNewPessoaJuridica)) {
                    Pessoa oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica = pessoaJuridicaCollectionNewPessoaJuridica.getPessoaPessoaID();
                    pessoaJuridicaCollectionNewPessoaJuridica.setPessoaPessoaID(pessoa);
                    pessoaJuridicaCollectionNewPessoaJuridica = em.merge(pessoaJuridicaCollectionNewPessoaJuridica);
                    if (oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica != null && !oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica.equals(pessoa)) {
                        oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica.getPessoaJuridicaCollection().remove(pessoaJuridicaCollectionNewPessoaJuridica);
                        oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica = em.merge(oldPessoaPessoaIDOfPessoaJuridicaCollectionNewPessoaJuridica);
                    }
                }
            }
            for (PessoaFisica pessoaFisicaCollectionNewPessoaFisica : pessoaFisicaCollectionNew) {
                if (!pessoaFisicaCollectionOld.contains(pessoaFisicaCollectionNewPessoaFisica)) {
                    Pessoa oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica = pessoaFisicaCollectionNewPessoaFisica.getPessoaPessoaID();
                    pessoaFisicaCollectionNewPessoaFisica.setPessoaPessoaID(pessoa);
                    pessoaFisicaCollectionNewPessoaFisica = em.merge(pessoaFisicaCollectionNewPessoaFisica);
                    if (oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica != null && !oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica.equals(pessoa)) {
                        oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica.getPessoaFisicaCollection().remove(pessoaFisicaCollectionNewPessoaFisica);
                        oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica = em.merge(oldPessoaPessoaIDOfPessoaFisicaCollectionNewPessoaFisica);
                    }
                }
            }
            for (Movimento movimentoCollectionOldMovimento : movimentoCollectionOld) {
                if (!movimentoCollectionNew.contains(movimentoCollectionOldMovimento)) {
                    movimentoCollectionOldMovimento.setPessoaID(null);
                    movimentoCollectionOldMovimento = em.merge(movimentoCollectionOldMovimento);
                }
            }
            for (Movimento movimentoCollectionNewMovimento : movimentoCollectionNew) {
                if (!movimentoCollectionOld.contains(movimentoCollectionNewMovimento)) {
                    Pessoa oldPessoaIDOfMovimentoCollectionNewMovimento = movimentoCollectionNewMovimento.getPessoaID();
                    movimentoCollectionNewMovimento.setPessoaID(pessoa);
                    movimentoCollectionNewMovimento = em.merge(movimentoCollectionNewMovimento);
                    if (oldPessoaIDOfMovimentoCollectionNewMovimento != null && !oldPessoaIDOfMovimentoCollectionNewMovimento.equals(pessoa)) {
                        oldPessoaIDOfMovimentoCollectionNewMovimento.getMovimentoCollection().remove(movimentoCollectionNewMovimento);
                        oldPessoaIDOfMovimentoCollectionNewMovimento = em.merge(oldPessoaIDOfMovimentoCollectionNewMovimento);
                    }
                }
            }
            em.getTransaction().commit();
        } catch (Exception ex) {
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = pessoa.getPessoaID();
                if (findPessoa(id) == null) {
                    throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.");
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
            Pessoa pessoa;
            try {
                pessoa = em.getReference(Pessoa.class, id);
                pessoa.getPessoaID();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pessoa with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PessoaJuridica> pessoaJuridicaCollectionOrphanCheck = pessoa.getPessoaJuridicaCollection();
            for (PessoaJuridica pessoaJuridicaCollectionOrphanCheckPessoaJuridica : pessoaJuridicaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the PessoaJuridica " + pessoaJuridicaCollectionOrphanCheckPessoaJuridica + " in its pessoaJuridicaCollection field has a non-nullable pessoaPessoaID field.");
            }
            Collection<PessoaFisica> pessoaFisicaCollectionOrphanCheck = pessoa.getPessoaFisicaCollection();
            for (PessoaFisica pessoaFisicaCollectionOrphanCheckPessoaFisica : pessoaFisicaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Pessoa (" + pessoa + ") cannot be destroyed since the PessoaFisica " + pessoaFisicaCollectionOrphanCheckPessoaFisica + " in its pessoaFisicaCollection field has a non-nullable pessoaPessoaID field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Movimento> movimentoCollection = pessoa.getMovimentoCollection();
            for (Movimento movimentoCollectionMovimento : movimentoCollection) {
                movimentoCollectionMovimento.setPessoaID(null);
                movimentoCollectionMovimento = em.merge(movimentoCollectionMovimento);
            }
            em.remove(pessoa);
            em.getTransaction().commit();
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Pessoa> findPessoaEntities() {
        return findPessoaEntities(true, -1, -1);
    }

    public List<Pessoa> findPessoaEntities(int maxResults, int firstResult) {
        return findPessoaEntities(false, maxResults, firstResult);
    }

    private List<Pessoa> findPessoaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pessoa.class));
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

    public Pessoa findPessoa(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pessoa.class, id);
        } finally {
            em.close();
        }
    }

    public int getPessoaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pessoa> rt = cq.from(Pessoa.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
