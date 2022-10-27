package com.apricot.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.apricot.model.Member;

@Repository
public interface MemberDao extends JpaRepository<Member, Integer>{

}
