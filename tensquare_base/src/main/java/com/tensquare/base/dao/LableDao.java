package com.tensquare.base.dao;

import com.tensquare.base.pojo.Label;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * 标签接口
 */
public interface LableDao extends JpaRepository<Label,String>,JpaSpecificationExecutor<Label>{

}
