package com.tensquare.qa.controller;

import com.tensquare.qa.client.LabelClient;
import com.tensquare.qa.pojo.Problem;
import com.tensquare.qa.service.ProblemService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

/**
 * 控制器层
 *
 * @author Administrator
 */
@RestController
@CrossOrigin
@RequestMapping("/problem")
public class ProblemController {

    @Autowired
    private ProblemService problemService;

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private LabelClient labelClient;

    @RequestMapping(value = "/label/{labelid}")
    public Result getLableByid(@PathVariable String labelid) {
//        return restTemplate.getForObject("http://localhost:9001/label/"+labelid,Result.class);
        return labelClient.getById(labelid);
    }

    /**
     * 查询全部数据
     *
     * @return
     */
    @RequestMapping(value = "/findAll" ,method = RequestMethod.GET)
    public Result findAll() {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findAll());
    }

    /**
     * 根据ID查询
     *
     * @param id ID
     * @return
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public Result findById(@PathVariable String id) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findById(id));
    }

    /**
     * 分页+多条件查询
     *
     * @param searchMap 查询条件封装
     * @param page      页码
     * @param size      页大小
     * @return 分页结果
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.findSearch(searchMap, page, size);
        return new Result(true, StatusCode.OK, "查询成功", new PageResult<Problem>(pageList.getTotalElements(), pageList.getContent()));
    }

    /**
     * 根据条件查询
     *
     * @param searchMap
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findSearch(@RequestBody Map searchMap) {
        return new Result(true, StatusCode.OK, "查询成功", problemService.findSearch(searchMap));
    }

    /**
     * 增加
     *
     * @param problem
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Problem problem) {
        problemService.add(problem);
        return new Result(true, StatusCode.OK, "增加成功");
    }

    /**
     * 修改
     *
     * @param problem
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public Result update(@RequestBody Problem problem, @PathVariable String id) {
        problem.setId(id);
        problemService.update(problem);
        return new Result(true, StatusCode.OK, "修改成功");
    }

    /**
     * 删除
     *
     * @param id
     */
    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id) {
        problemService.deleteById(id);
        return new Result(true, StatusCode.OK, "删除成功");
    }

    /**
     * 最新回答列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/newlist/{lableid}/{page}/{size}", method = RequestMethod.GET)
    public Result newlist(@PathVariable String lableid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.newlist(lableid, page, size);
        PageResult<Problem> problems = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", problems);
    }

    /**
     * 热门回答列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/hotlist/{lableid}/{page}/{size}", method = RequestMethod.GET)
    public Result hotlist(@PathVariable String lableid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.hotlist(lableid, page, size);
        PageResult<Problem> problems = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", problems);
    }

    /**
     * 等待回答列表
     *
     * @param
     * @return
     */
    @RequestMapping(value = "/waitlist/{lableid}/{page}/{size}", method = RequestMethod.GET)
    public Result waitlist(@PathVariable String lableid, @PathVariable int page, @PathVariable int size) {
        Page<Problem> pageList = problemService.waitlist(lableid, page, size);
        PageResult<Problem> problems = new PageResult<>(pageList.getTotalElements(), pageList.getContent());
        return new Result(true, StatusCode.OK, "查询成功", problems);
    }

    public static void main(String[] args) throws IOException {
		/*ResourceBundle bundle = ResourceBundle.getBundle("aaa");
		String aaa = bundle.getString("aaa");
		System.out.println(aaa);*/

        Properties properties = PropertiesLoaderUtils.loadAllProperties("aaa.properties");
        String aaa = properties.getProperty("aaa");
        System.out.println(aaa);
    }
}
