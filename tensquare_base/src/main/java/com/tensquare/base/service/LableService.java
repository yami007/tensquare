package com.tensquare.base.service;

import com.tensquare.base.dao.LableDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class LableService {
    @Autowired
    private LableDao lableDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 增加标签
     *
     * @param lable
     */
    public void save(Label lable) {
        lable.setId(String.valueOf(idWorker.nextId()));
        lableDao.save(lable);
    }

    /**
     * 根据id删除label
     *
     * @param labelid
     */
    public void deleteById(String labelid) {
        lableDao.deleteById(labelid);
    }

    /**
     * 根据id更新label
     *
     * @param labelid
     * @param lable
     */
    public void update(String labelid, Label lable) {
        lable.setId(labelid);
        lableDao.save(lable);
    }

    /**
     * 查询所有label
     *
     * @return
     */
    public List<Label> findAll() {
        return lableDao.findAll();
    }

    /**
     * 根据id查询label
     *
     * @param labelid
     * @return
     */
    public Label getById(String labelid) {
        return lableDao.findById(labelid).get();
    }

    /**
     * 抽取specification
     *
     * @param searchMap
     * @return
     */
    private Specification getSpecification(Map searchMap) {
        Specification specification = new Specification() {
            @Override
            public Predicate toPredicate(Root root, CriteriaQuery criteriaQuery, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<>();
                //模糊查询
                if (!StringUtils.isEmpty(searchMap.get("labelname"))) {
                    predicateList.add(cb.like(root.get("labelname").as(String.class), "%" + searchMap.get("labelname") + "%"));
                }
                //精确条件查询
                if (!StringUtils.isEmpty(searchMap.get("state"))) {
                    predicateList.add(cb.equal(root.get("state").as(String.class), searchMap.get("state")));
                }
                //精确条件查询
                if (!StringUtils.isEmpty(searchMap.get("recommend"))) {
                    predicateList.add(cb.equal(root.get("recommend").as(String.class), searchMap.get("recommend")));
                }
                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
            }
        };
        return specification;
    }

    /**
     * 条件查询
     *
     * @param searchMap
     * @return
     */
    public List<Label> findBySearch(Map searchMap) {
        Specification specification = getSpecification(searchMap);
        return lableDao.findAll(specification);
    }

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param searchMap
     * @return
     */
    public Page<Label> findBySearch(int page, int size, Map searchMap) {
        Specification specification = getSpecification(searchMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return (Page<Label>) lableDao.findAll(specification, pageRequest);
    }
}
