package com.apricot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.apricot.model.Txt;

@Repository
public interface TxtDao extends JpaRepository<Txt, Integer>,JpaSpecificationExecutor<Txt>{

}
