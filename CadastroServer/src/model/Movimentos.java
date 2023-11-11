/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Kypz
 */
@Entity
@Table(name = "Movimentos")
@NamedQueries({
    @NamedQuery(name = "Movimentos.findAll", query = "SELECT m FROM Movimentos m"),
    @NamedQuery(name = "Movimentos.findById", query = "SELECT m FROM Movimentos m WHERE m.id = :id"),
    @NamedQuery(name = "Movimentos.findByTipo", query = "SELECT m FROM Movimentos m WHERE m.tipo = :tipo"),
    @NamedQuery(name = "Movimentos.findByQuantidade", query = "SELECT m FROM Movimentos m WHERE m.quantidade = :quantidade"),
    @NamedQuery(name = "Movimentos.findByPrecoUnitario", query = "SELECT m FROM Movimentos m WHERE m.precoUnitario = :precoUnitario"),
    @NamedQuery(name = "Movimentos.findByData", query = "SELECT m FROM Movimentos m WHERE m.data = :data")})
public class Movimentos implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "Tipo")
    private String tipo;
    @Column(name = "Quantidade")
    private Integer quantidade;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "PrecoUnitario")
    private BigDecimal precoUnitario;
    @Column(name = "Data")
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "ProdutoID", referencedColumnName = "ID")
    @ManyToOne
    private Produtos produtoID;
    @JoinColumn(name = "OperadorID", referencedColumnName = "ID")
    @ManyToOne
    private Usuarios operadorID;

    public Movimentos() {
    }

    public Movimentos(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
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

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Produtos getProdutoID() {
        return produtoID;
    }

    public void setProdutoID(Produtos produtoID) {
        this.produtoID = produtoID;
    }

    public Usuarios getOperadorID() {
        return operadorID;
    }

    public void setOperadorID(Usuarios operadorID) {
        this.operadorID = operadorID;
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
        if (!(object instanceof Movimentos)) {
            return false;
        }
        Movimentos other = (Movimentos) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Movimentos[ id=" + id + " ]";
    }
    
}
