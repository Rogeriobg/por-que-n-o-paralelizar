package model;

import java.io.Serializable;
import java.math.BigDecimal;

public class Produto implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer produtoID;
    private String nome;
    private Integer quantidade;
    private BigDecimal precoVenda;

    public Produto() {
    }

    public Produto(Integer produtoID) {
        this.produtoID = produtoID;
    }

    public Integer getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(Integer produtoID) {
        this.produtoID = produtoID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public BigDecimal getPrecoVenda() {
        return precoVenda;
    }

    public void setPrecoVenda(BigDecimal precoVenda) {
        this.precoVenda = precoVenda;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (produtoID != null ? produtoID.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof Produto)) {
            return false;
        }
        Produto other = (Produto) object;
        if ((this.produtoID == null && other.produtoID != null) || (this.produtoID != null && !this.produtoID.equals(other.produtoID))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Produto[ produtoID=" + produtoID + " ]";
    }
}
