package br.com.systechmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Clients;


public interface ClientsRepository  extends JpaRepository<Clients, Integer> {

    @Query(value = " select * from fornecedores c where c.id = :id", nativeQuery = true)
    Clients getClientById( @Param("id") int id );

    @Query(value = " select * from fornecedores c where c.num_cli = :num_cli", nativeQuery = true)
    List<Clients> getClientByNumCli( @Param("num_cli") int num_cli );
    
    @Query(value = "select * from clientes c where"
    	    + "    		  c.id like  ?1%"
    	    + "    		  or c.nome like  ?1%"
    	    + "    		  or c.endereco like  ?1%"
    	    + "    		  or c.estado like  ?1%"
    	    + "    		  or c.telefone like  ?1%"
    	    + "    		  or c.email like  ?1%"
    	    + "    		  or c.cpf like  ?1%"
    	    + " limit 20", nativeQuery = true)
    List<Clients> getLocaCli(String codigo);
	
}