package br.com.systechmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Moviments;

public interface MovimentsRepository  extends JpaRepository<Moviments, Integer> {

    @Query(value = "select * from movimentacao_financeira where tipo = 'SAIDA' ", nativeQuery = true)
    List<Moviments> getValuesOut();
    
    @Query(value = "select * from movimentacao_financeira where tipo = 'ENTRADA'", nativeQuery = true)
    List<Moviments>  getValuesIn();
		
    
    @Query(value = "UPDATE movimentacao_financeira SET feito = true where id = :id", nativeQuery = true)
    List<Moviments> paidValue(@Param("id") int id);
}
