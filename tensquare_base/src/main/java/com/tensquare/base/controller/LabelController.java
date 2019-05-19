package com.tensquare.base.controller;

import com.tensquare.base.pojo.Label;
import com.tensquare.base.redisson.utils.RedissLockUtil;
import com.tensquare.base.service.LableService;
import entity.PageResult;
import entity.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/label")
@CrossOrigin
public class LabelController {
    @Autowired
    private LableService lableService;

    /**
     * 增加
     *
     * @param lable
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Label lable) {
        lableService.save(lable);
        return new Result(true, 20000, "保存成功");
    }

    /**
     * 删除
     *
     * @param labelid
     * @return
     */
    @RequestMapping(value = "/{labelid}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String labelid) {
        lableService.deleteById(labelid);
        return new Result(true, 20000, "删除成功");
    }

    /**
     * 修改
     *
     * @param lable
     * @return
     */
    @RequestMapping(value = "/{labelid}", method = RequestMethod.PUT)
    public Result update(@PathVariable String labelid, @RequestBody Label lable) {
        lableService.update(labelid, lable);
        return new Result(true, 20000, "更新成功");
    }

    /**
     * 查询所有
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll() {
        for (int i = 1; i <= 100; i++) {
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    String key ="123";
                    RedissLockUtil.tryLock(key,4,3);
                    System.out.println("获取到了锁====开始执行操作");
                    RedissLockUtil.unlock(key);
                    System.out.println("释放了锁======");

                }
            });
            thread.start();
        }
        return new Result();

    }

    /**
     * 根据id查询label
     *
     * @param labelId
     * @return
     */
    @RequestMapping(value = "/{labelId}", method = RequestMethod.GET)
    public Result getById(@PathVariable String labelId) {
        Label label = lableService.getById(labelId);
        return new Result(true, 20000, "查询成功", label);
    }

    /**
     * 条件查询
     *
     * @return
     */
    @RequestMapping(value = "/search", method = RequestMethod.POST)
    public Result findBySearch(@RequestBody Map searchMap) {
        List<Label> list = lableService.findBySearch(searchMap);
        return new Result(true, 20000, "条件查询成功", list);
    }

    /**
     * 分页条件查询
     *
     * @return
     */
    @RequestMapping(value = "/search/{page}/{size}", method = RequestMethod.POST)
    public Result findBySearch(@PathVariable int page, @PathVariable int size, @RequestBody Map searchMap) {
        Page<Label> pagelist = lableService.findBySearch(page, size, searchMap);
        //封装结果集
        PageResult<Label> pageResult = new PageResult<>();
        pageResult.setRows(pagelist.getContent());
        pageResult.setTotal(pagelist.getTotalElements());
        return new Result(true, 20000, "分页条件查询成功", pageResult);
    }

}
