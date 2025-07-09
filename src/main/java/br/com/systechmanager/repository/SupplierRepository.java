package br.com.systechmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Supplier;


public interface SupplierRepository  extends JpaRepository<Supplier, Integer> {

    @Query(value = " select * from fornecedores c where c.id = :id", nativeQuery = true)
    Supplier getClientById( @Param("id") int id );

    @Query(value = " select * from fornecedores c where c.num_cli = :num_cli", nativeQuery = true)
    List<Supplier> getClientByNumCli( @Param("num_cli") int num_cli );
	
}