package br.com.systechmanager.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
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
@Table(name = "clientes")
public class Clients {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//@Column(length = 100, nullable = false)
	private String nome;
	
	//@Column(length = 80, nullable = false)
	private String endereco;
		
	//@Column(length = 80, nullable = false)
	private String estado;
	
	//@Column(length = 100, nullable = false)
	private String telefone;
	
	//@Column(length = 100, nullable = false)
	private String email;

	//@Column(length = 20,nullable = false)
	private String cpf;

	   public Clients(Integer id, String nome, int num_cli) {
	        this.id = id;
	        this.nome = nome;
	    }
}
