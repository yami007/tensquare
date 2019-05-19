package com.tensquare.article.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.article.pojo.Article;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ArticleDao extends JpaRepository<Article,String>,JpaSpecificationExecutor<Article>{
    @Query("UPDATE Article set state = 1 where id =?1")
    @Modifying
    void examine(String articleId) ;

    @Query("UPDATE Article set thumbup = thumbup + 1 where id =?1")
    @Modifying
    void thumbup(String articleId) ;

}
