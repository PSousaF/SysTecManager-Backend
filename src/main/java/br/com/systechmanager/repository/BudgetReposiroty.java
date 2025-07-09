package br.com.systechmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Bid;
import br.com.systechmanager.entity.Budget;


public interface BudgetReposiroty  extends JpaRepository<Budget, Integer> {
    
    @Query(value = "select * from orcamento b where"
    	    + "    		  b.id like  ?1%"
    	    + "    		  or b.device_type like  ?1%"
    	    + "    		  or b.model like  ?1%"
    	    + "    		  or b.quantity like  ?1%"
    	    + "    		  or b.device_brand like  ?1%"
    	    + "    		  or b.serie like  ?1%"
    	    + "    		  or b.defect like  ?1%"
    	    + "    		  or b.review like  ?1%"
    	    + "    		  or b.possible_causes like  ?1%"
    	    + "    		  or b.observation like  ?1%"
    	    + "    		  or b.value_item like  ?1%"
    	    + "    		  or b.situation like  ?1%"
    	    + "    		  or b.aparroved like  ?1%"
    	    + " limit 20", nativeQuery = true)
    List<Budget> getLocalizaBuget(String codigo);
    
    
    @Query(value = "select * from orcamento b where aparroved = 'Sim' ", nativeQuery = true)
    List<Budget> getOrder();

    @Query(value = "select * from orcamento b where aparroved = 'Não' ", nativeQuery = true)
    List<Budget> getBuget();
    
    @Query(value = "select * from orcamento b where b.bid = :bid", nativeQuery = true)
    Budget findByBid(@Param("bid") String bid);

    
    @Query(value = "select * from orcamento b where situation = 'Aguardando peça'", nativeQuery = true)
    List<Budget> getPieces();
}
