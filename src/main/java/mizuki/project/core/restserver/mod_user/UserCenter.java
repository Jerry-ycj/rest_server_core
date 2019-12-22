package mizuki.project.core.restserver.mod_user;

import mizuki.project.core.restserver.mod_user.bean.User;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserCenter {
    // todo 需要和redis同步
    private Map<Integer,User> users = new HashMap<>();

    public void add(User user){
        if(user!=null) users.put(user.getId(),user);
    }

    public void del(User user){
        if(user!=null) users.remove(user.getId());
    }

    public User get(User user){
        if(user==null) return null;
        return users.get(user.getId());
    }

}
