package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FriendService {
    @Autowired
    private FriendDao friendDao;
    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    @Transactional
    public int addFriend(String userid, String friendid) {
        if (friendDao.selectCount(userid, friendid) > 0) {
            return 0;
        }
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIsLisk("0");
        friendDao.save(friend);

        if (friendDao.selectCount(friendid, userid) > 0) {
            friendDao.updateLike(userid, friendid, "1");
            friendDao.updateLike(friendid, userid, "1");
        }
        userClient.incFanscount(userid,1); // 增加自己的关注数
        userClient.incFollowcount(friendid,1); // 增加对方的粉丝数
        return 1;
    }

    /**
     * * 向不喜欢列表中添加记录
     *
     * @param userid
     * @param friendid
     */
    public void addNoFriend(String userid, String friendid) {
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);
        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     * @param userid
     * @param friendid
     */
    @Transactional
    public void deleteFriend(String userid, String friendid) {
        friendDao.deleteFriend(userid, friendid);
        friendDao.updateLike(friendid, userid, "0");
        addNoFriend(userid, friendid);//向不喜欢表中添加记录
        userClient.incFanscount(userid,1); // 增加自己的关注数
        userClient.incFollowcount(friendid,1); // 增加对方的粉丝数
    }

}
