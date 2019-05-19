package com.tensquare.qa.dao;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{
    @Query("select p from Problem p where p.id in (select problemid from Pl t where labelid=?1)")
    Page<Problem> newlist(String lableid, Pageable pageable);

    /**
     * 热门回答
     * @param lableid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl t where labelid=?1) order by p.reply desc ")
    Page<Problem> hotlist(String lableid, Pageable pageable);

    /**
     * 等待回答
     * @param lableid
     * @param pageable
     * @return
     */
    @Query("select p from Problem p where p.id in (select problemid from Pl t where labelid=?1) and p.reply =0")
    Page<Problem> waitlist(String lableid, Pageable pageable);
}
