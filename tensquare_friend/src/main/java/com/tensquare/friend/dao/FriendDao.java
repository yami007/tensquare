package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface FriendDao extends JpaRepository<Friend, String>,JpaSpecificationExecutor<Friend> {
    /**
     * 根据用户ID与被关注用户ID查询记录个数
     */

    @Query("select count(f) from Friend f where f.userid=?1 and  f.friendid=?2")
    public int selectCount (String userid, String friendid);
    /**
     * 更新为互相喜欢
     */

    @Query("update Friend f set f.islike=?3 where f.userid=?1 and f.friendid=?2")
    @Modifying
    public void updateLike(String userid, String friendid, String islike);

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */

    @Query("delete from Friend f where f.userid=?1 and f.friendid=?2")
    @Modifying
    public void deleteFriend(String userid,String friendid);


}
