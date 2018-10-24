package com.zhgame.animalkindom.account.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.zhgame.animalkindom.account.entity.Account;

import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
	@Query("select a from Account a where a.name=:name")
	Optional<Account> getByIdentity(@Param("name")String name);
	Account getByName(String name);
}
