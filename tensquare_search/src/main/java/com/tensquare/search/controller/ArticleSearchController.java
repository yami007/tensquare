package com.tensquare.search.controller;

import com.tensquare.search.pojo.Article;
import com.tensquare.search.service.ArticleSearchService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/article")
public class ArticleSearchController {
    @Autowired
    private ArticleSearchService articleSearchService;

    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Article article) {
        articleSearchService.add(article);
        return new Result(true, StatusCode.OK, "添加成功");
    }
    @RequestMapping(value = "/search/{keywords}/{page}/{size}",method =RequestMethod.GET )
    public Result findByTitleOrContentLike(@PathVariable String keywords,@PathVariable int page,@PathVariable int size){
        Page<Article> articlePage = articleSearchService.findByTitleOrContentLike(keywords, page, size);
        PageResult<Article> pageResult = new PageResult<>();
        pageResult.setTotal((long) articlePage.getTotalElements());
        pageResult.setRows(articlePage.getContent());
        return new Result(true, StatusCode.OK, "查询成功",pageResult);
    }
}
