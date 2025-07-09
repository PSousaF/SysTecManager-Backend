package br.com.systechmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "fornecedores")
public class Supplier {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String nome;
	private String tipo;
	private int num_loja;
	private int num_cli;
	private String endereco;
	private String telefone;
	private String email;
	private String pecas;
	private int ativo;
	

	   public Supplier(Integer id, String nome, int num_cli) {
	        this.id = id;
	        this.nome = nome;
	        this.num_cli = num_cli;
	    }
}
