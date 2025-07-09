package br.com.systechmanager.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "usuarios")
public class Users   {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	//@Column(length = 100, nullable = false)
	private String nome;
	
	@Column(length = 100, nullable = false)
	@NotEmpty(message = "{field.user.mandatory}")
	private String username;
	
	@Column(length = 80, nullable = false)
	@NotEmpty(message = "{field.password.mandatory}")
	@JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
	private String password;
	
	//@Column(length = 80, nullable = false)
	private String endereco;
		
	/*//@Column(length = 80, nullable = false)
	private String numero;
	
	//@Column(length = 80, nullable = false)
	private String bairro;
	
	//@Column(length = 80, nullable = false)
	private String cidade;

	//@Column(length = 100, nullable = false)
	private String estado;*/
	
	//@Column(length = 100, nullable = false)
	private String telefone;
	
	//@Column(length = 100, nullable = false)
	private String email;
	
	//@Column(length = 100, nullable = false)
	private String cargo;

	//@Column(length = 20,nullable = false)
	private String cpf;
	
	//@Column(length = 2, nullable = false)
	private int ativo;

	//@Column(length = 2, nullable = false)
	private int permissao;
	

	   public Users(Integer id, String nome) {
	        this.id = id;
	        this.nome = nome;
	    }
}

