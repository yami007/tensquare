package com.tensquare.spit.service;

import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import utils.IdWorker;

import java.util.List;
import java.util.Optional;

@Service
public class SpitService {
    @Autowired
    private SpitDao spitDao;
    @Autowired
    private IdWorker idWorker;
    @Autowired
    private MongoTemplate mongoTemplate;
    /**
     * 新增
     * @param spit
     */
    public void save(Spit spit) {
        spit.set_id(idWorker.nextId() + "");
        spitDao.save(spit);
    }

    /**
     * 删除
     * @param spitId
     */
    public void deleteById(String spitId) {
        spitDao.deleteById(spitId);
    }

    /**
     * 修改
     * @param spitId
     * @param spit
     */
    public void updateById(String spitId, Spit spit) {
        spit.set_id(spitId);
        spitDao.save(spit);
    }

    /**
     * 查询所有
     * @return
     */
    public List<Spit> getAll() {
        return spitDao.findAll();
    }

    /**
     * 根据id查询
     * @param spitId
     * @return
     */
    public Spit getByid(String spitId) {
        Optional<Spit> byId = spitDao.findById(spitId);
        Spit spit = byId.get();
        return spit;
    }
    /**
     * 根据父id分页查询吐槽信息
     * @param parentid
     * @param page
     * @param size
     * @return
     */
    public Page<Spit> findByparentid(String parentid, int page, int size) {
        Pageable pageable = PageRequest.of(page-1,size);
        return spitDao.findByparentid(parentid,pageable);
    }

    /**
     * 吐槽点赞
     * @param spitId
     */
    public void thumbup(String spitId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(spitId));
        Update update = new Update();
        update.inc("thumbup",1);
        mongoTemplate.updateFirst(query,update,"spit");
    }
}
