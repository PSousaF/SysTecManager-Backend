package br.com.systechmanager.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.systechmanager.entity.Bid;
import br.com.systechmanager.entity.Budget;


public interface BidRepository  extends JpaRepository<Bid, Integer> {

    @Query(value = "select * from databid ", nativeQuery = true)
    List<Bid> findByAll();
    
    @Query(value = "select * from databid b where b.bid = :bid", nativeQuery = true)
    List<Bid> findByBid(@Param("bid") String bid);
	
}