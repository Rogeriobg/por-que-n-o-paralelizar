package model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 *
 * @author rbgor
 */
@Entity
@Table(name = "Movimento")
@NamedQueries({
    @NamedQuery(name = "Movimento.findAll", query = "SELECT m FROM Movimento m"),
    @NamedQuery(name = "Movimento.findByMovimentoID", query = "SELECT m FROM Movimento m WHERE m.movimentoID = :movimentoID"),
    @NamedQuery(name = "Movimento.findByUsuarioID", query = "SELECT m FROM Movimento m WHERE m.usuario.usuarioID = :usuarioID"),
    @NamedQuery(name = "Movimento.findByTipo", query = "SELECT m FROM Movimento m WHERE m.tipo = :tipo"),
    @NamedQuery(name = "Movimento.findByQuantidade", query = "SELECT m FROM Movimento m WHERE m.quantidade = :quantidade"),
    @NamedQuery(name = "Movimento.findByPrecoUnitario", query = "SELECT m FROM Movimento m WHERE m.precoUnitario = :precoUnitario")
})
public class Movimento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "MovimentoID")
    private Integer movimentoID;

    @Column(name = "Tipo")
    private Character tipo;

    @Column(name = "Quantidade")
    private Integer quantidade;

    @Column(name = "PrecoUnitario")
    private BigDecimal precoUnitario;

    @JoinColumn(name = "PessoaID", referencedColumnName = "PessoaID")
    @ManyToOne
    private Pessoa pessoaID;

    @JoinColumn(name = "ProdutoID", referencedColumnName = "ProdutoID")
    @ManyToOne
    private Produto produtoID;

    @JoinColumn(name = "UsuarioID", referencedColumnName = "UsuarioID")
    @ManyToOne
    private Usuario usuario;

    public Movimento() {
    }

    public Movimento(Integer movimentoID) {
        this.movimentoID = movimentoID;
    }

    public Integer getMovimentoID() {
        return movimentoID;
    }

    public void setMovimentoID(Integer movimentoID) {
        this.movimentoID = movimentoID;
    }

    public Character getTipo() {
        return tipo;
    }

    public void setTipo(Character tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(BigDecimal precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    public Pessoa getPessoaID() {
        return pessoaID;
    }

    public void setPessoaID(Pessoa pessoaID) {
        this.pessoaID = pessoaID;
    }

    public Produto getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(Produto produtoID) {
        this.produtoID = produtoID;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (movimentoID != null ? movimentoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Movimento)) {
            return false;
        }
        Movimento other = (Movimento) object;
        return (this.movimentoID != null || other.movimentoID == null) && (this.movimentoID == null || this.movimentoID.equals(other.movimentoID));
    }

    @Override
    public String toString() {
        return "model.Movimento[ movimentoID=" + movimentoID + " ]";
    }
}