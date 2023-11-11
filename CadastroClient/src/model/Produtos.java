/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author Kypz
 */
@Entity
@Table(name = "Produtos")
@NamedQueries({
    @NamedQuery(name = "Produtos.findAll", query = "SELECT p FROM Produtos p"),
    @NamedQuery(name = "Produtos.findById", query = "SELECT p FROM Produtos p WHERE p.id = :id"),
    @NamedQuery(name = "Produtos.findByNome", query = "SELECT p FROM Produtos p WHERE p.nome = :nome"),
    @NamedQuery(name = "Produtos.findByQuantidade", query = "SELECT p FROM Produtos p WHERE p.quantidade = :quantidade"),
    @NamedQuery(name = "Produtos.findByPrecoVenda", query = "SELECT p FROM Produtos p WHERE p.precoVenda = :precoVenda")})
public class Produtos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "Nome")
    private String nome;
    @Column(name = "Quantidade")
    private Integer quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecoVenda")
    private BigDecimal precoVenda;
    @OneToMany(mappedBy = "produtoID")
    private Collection<Movimentos> movimentosCollection;

    public Produtos() {
    }

    public Produtos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public Collection<Movimentos> getMovimentosCollection() {
        return movimentosCollection;
    }

    public void setMovimentosCollection(Collection<Movimentos> movimentosCollection) {
        this.movimentosCollection = movimentosCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Produtos)) {
            return false;
        }
        Produtos other = (Produtos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Produtos[ id=" + id + " ]";
    }
    
}
