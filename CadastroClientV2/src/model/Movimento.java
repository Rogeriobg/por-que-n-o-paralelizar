package model;

import java.io.Serializable;
import java.math.BigDecimal;




public class Movimento implements Serializable {

    private static final long serialVersionUID = 1L;
    private String movimentoID;
    private Usuario usuario;
    private Produto produtoID;
    private Pessoa pessoaID;
    private BigDecimal precoUnitario;
    private Integer quantidade;
    private Character tipo;



    public Movimento() {
    }

    public Movimento(Integer movimentoID) {
       
    }

    public String getMovimentoID() {
        return movimentoID;
    }

    public void setMovimentoID(Integer movimentoID) {
        
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
