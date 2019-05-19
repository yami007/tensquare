package com.tensquare.user.service;

import com.tensquare.user.dao.UserDao;
import com.tensquare.user.pojo.User;
import com.tensquare.user.utils.StringRedisUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import utils.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * 服务层
 *
 * @author Administrator
 */
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private IdWorker idWorker;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private StringRedisUtil stringRedisUtil;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    /**
     * 查询全部列表
     *
     * @return
     */
    public List<User> findAll() {
        return userDao.findAll();
    }

    /**
     * 条件查询+分页
     *
     * @param whereMap
     * @param page
     * @param size
     * @return
     */
    public Page<User> findSearch(Map whereMap, int page, int size) {
        Specification<User> specification = createSpecification(whereMap);
        PageRequest pageRequest = PageRequest.of(page - 1, size);
        return userDao.findAll(specification, pageRequest);
    }

    /**
     * 条件查询
     *
     * @param whereMap
     * @return
     */
    public List<User> findSearch(Map whereMap) {
        Specification<User> specification = createSpecification(whereMap);
        return userDao.findAll(specification);
    }

    /**
     * 根据ID查询实体
     *
     * @param id
     * @return
     */
    public User findById(String id) {
        return userDao.findById(id).get();
    }

    /**
     * 增加
     *
     * @param user
     */
    public void add(User user) {
        user.setId(idWorker.nextId() + "");
        userDao.save(user);
    }

    /**
     * 修改
     *
     * @param user
     */
    public void update(User user) {
        userDao.save(user);
    }

    /**
     * 删除
     *
     * @param id
     */
    public void deleteById(String id) {
        userDao.deleteById(id);
    }

    /**
     * 动态条件构建
     *
     * @param searchMap
     * @return
     */
    private Specification<User> createSpecification(Map searchMap) {

        return new Specification<User>() {

            @Override
            public Predicate toPredicate(Root<User> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicateList = new ArrayList<Predicate>();
                // ID
                if (searchMap.get("id") != null && !"".equals(searchMap.get("id"))) {
                    predicateList.add(cb.like(root.get("id").as(String.class), "%" + (String) searchMap.get("id") + "%"));
                }
                // 手机号码
                if (searchMap.get("mobile") != null && !"".equals(searchMap.get("mobile"))) {
                    predicateList.add(cb.like(root.get("mobile").as(String.class), "%" + (String) searchMap.get("mobile") + "%"));
                }
                // 密码
                if (searchMap.get("password") != null && !"".equals(searchMap.get("password"))) {
                    predicateList.add(cb.like(root.get("password").as(String.class), "%" + (String) searchMap.get("password") + "%"));
                }
                // 昵称
                if (searchMap.get("nickname") != null && !"".equals(searchMap.get("nickname"))) {
                    predicateList.add(cb.like(root.get("nickname").as(String.class), "%" + (String) searchMap.get("nickname") + "%"));
                }
                // 性别
                if (searchMap.get("sex") != null && !"".equals(searchMap.get("sex"))) {
                    predicateList.add(cb.like(root.get("sex").as(String.class), "%" + (String) searchMap.get("sex") + "%"));
                }
                // 头像
                if (searchMap.get("avatar") != null && !"".equals(searchMap.get("avatar"))) {
                    predicateList.add(cb.like(root.get("avatar").as(String.class), "%" + (String) searchMap.get("avatar") + "%"));
                }
                // E-Mail
                if (searchMap.get("email") != null && !"".equals(searchMap.get("email"))) {
                    predicateList.add(cb.like(root.get("email").as(String.class), "%" + (String) searchMap.get("email") + "%"));
                }
                // 兴趣
                if (searchMap.get("interest") != null && !"".equals(searchMap.get("interest"))) {
                    predicateList.add(cb.like(root.get("interest").as(String.class), "%" + (String) searchMap.get("interest") + "%"));
                }
                // 个性
                if (searchMap.get("personality") != null && !"".equals(searchMap.get("personality"))) {
                    predicateList.add(cb.like(root.get("personality").as(String.class), "%" + (String) searchMap.get("personality") + "%"));
                }

                return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));

            }
        };

    }

    /**
     * 获取验证码
     *
     * @param mobile
     */
    public void sendsms(String mobile) {
        //获取随机验证码
        int min = 100000;
        int max = 999999;
        Random random = new Random();
        int codeInt = random.nextInt(max);
        if (codeInt < min) {
            codeInt = random.nextInt(max);
        }
        System.out.println(codeInt);
        //将手机号和验证码放到mq中
        Map<String, String> hashMap = new HashMap<>();
        hashMap.put("mobile", mobile);
        hashMap.put("code", String.valueOf(codeInt));
        rabbitTemplate.convertAndSend("yamiMES", hashMap);
        //将手机号和验证码方法到redis中
        stringRedisUtil.set("smscode_" + mobile, String.valueOf(codeInt), 5 * 60);
    }

    /**
     * 注册
     *
     * @param code
     */
    public void register(User user, String code) {
        String codeInt = (String) stringRedisUtil.get("smscode_" + user.getMobile());
        if (StringUtils.isEmpty(codeInt)) {
            throw new RuntimeException("请点击获取验证码");
        }
        if (!code.equals(codeInt)) {
            throw new RuntimeException("您输入的验证码有误");
        }
        //密码加密
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setId(idWorker.nextId() + "");
        user.setFollowcount(0);//关注数       
        user.setFanscount(0);//粉丝数
        user.setOnline(0L);//在线时长        
        user.setRegdate(new Date());//注册日期        
        user.setUpdatedate(new Date());//更新日期        
        user.setLastdate(new Date());//最后登陆日期
        userDao.save(user);
    }

    /**
     * 根据手机号和密码查询用户
     *
     * @param mobile
     * @param password
     * @return
     */
    public User findByMobile(String mobile, String password) {
        User user = userDao.findByMobile(mobile);
        if (user != null && bCryptPasswordEncoder.matches(password, user.getPassword())) {
            return user;
        } else {
            return null;
        }
    }

    /**
     * 更新粉丝数
     */
    @Transactional
    public void incFanscount(String userid, int x) {
        userDao.incFanscount(userid, x);
    }
    /**
     * 更新关注
     */
    @Transactional
    public void incFollowcount(String userid, int x){
        userDao.incFollowcount(userid,x);
    }
}
