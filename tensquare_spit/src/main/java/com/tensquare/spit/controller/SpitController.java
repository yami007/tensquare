package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import com.tensquare.spit.utils.StringRedisUtil;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/spit")
@CrossOrigin
public class SpitController {
    @Autowired
    private SpitService spitService;
    @Autowired
    private StringRedisUtil stringRedisUtil;

    /**
     * 增加吐槽信息
     *
     * @param spit
     * @return
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result save(@RequestBody Spit spit) {
        spitService.save(spit);
        return new Result(true, StatusCode.OK, "增加吐槽信息成功");
    }

    /**
     * 删除吐槽信息
     *
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/{spitId}", method = RequestMethod.DELETE)
    public Result delete(@PathVariable String spitId) {
        spitService.deleteById(spitId);
        return new Result(true, StatusCode.OK, "删除吐槽信息成功");
    }

    /**
     * 修改吐槽信息
     *
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/{spitId}", method = RequestMethod.PUT)
    public Result updateById(@PathVariable String spitId, @RequestBody Spit spit) {
        spitService.updateById(spitId, spit);
        return new Result(true, StatusCode.OK, "修改吐槽信息成功");
    }

    /**
     * 查询所有吐槽信息
     *
     * @return
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result getAll() {
        List<Spit> spitList = spitService.getAll();
        return new Result(true, StatusCode.OK, "修改吐槽信息成功", spitList);
    }

    /**
     * 根据id查询吐槽信息
     *
     * @param spitId
     * @return
     */
    @RequestMapping(value = "/{spitId}", method = RequestMethod.GET)
    public Result getByid(@PathVariable String spitId) {
        Spit spit = spitService.getByid(spitId);
        return new Result(true, StatusCode.OK, "修改吐槽信息成功", spit);
    }

    /**
     * 根据父id分页查询吐槽信息
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}", method = RequestMethod.GET)
    public Result getByPerentid(@PathVariable String parentid, @PathVariable int page, @PathVariable int size) {
        Page<Spit> spitPage = spitService.findByparentid(parentid, page, size);
        PageResult<Spit> pageResult = new PageResult<>();
        pageResult.setRows(spitPage.getContent());
        pageResult.setTotal((long) spitPage.getTotalElements());
        return new Result(true, StatusCode.OK, "根据上级id查询吐槽列表", pageResult);
    }

    /**
     * 吐槽点赞
     * @param
     */
    @RequestMapping(value="/thumbup/{spitId}",method= RequestMethod.PUT)
    public Result thumbup(@PathVariable String spitId ){
        String userId ="yami";
        String str = (String) stringRedisUtil.get("thumbup_" + userId + spitId);
        if(!StringUtils.isEmpty(str)){
            return new Result(false,StatusCode.REMOTEERROR,"您已经点过赞了");
        }
        spitService.thumbup(spitId);
        stringRedisUtil.set("thumbup_" + userId + spitId,"1");
        return new Result(true,StatusCode.OK,"已赞");
    }

}
