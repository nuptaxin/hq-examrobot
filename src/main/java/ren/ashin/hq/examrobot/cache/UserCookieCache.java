package ren.ashin.hq.examrobot.cache;

import java.io.IOException;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;

import ren.ashin.hq.examrobot.bean.HqUser;
import ren.ashin.hq.examrobot.util.CookieUtil;

import com.google.common.collect.Maps;

/**
 * @ClassName: UserCookieCache
 * @Description: TODO
 * @author renzx
 * @date Mar 13, 2017
 */
public class UserCookieCache {
    private Map<Long, CookieStore> cookieMap = Maps.newHashMap();
    private static UserCookieCache userCookieCache = new UserCookieCache();

    private UserCookieCache() {}

    public static UserCookieCache getInstance() {
        return userCookieCache;
    }

    public CookieStore getCookieByUser(HqUser user) {
        if (cookieMap.containsKey(user.getId())) {
            return cookieMap.get(user.getId());
        }
        CookieStore userCookie = updateCookieByUser(user);
        return userCookie;
    }
    
    public CookieStore updateCookieByUser(HqUser user) {
        CookieStore userCookie = null;
        try {
            userCookie = CookieUtil.getCookieStore(user.getuId(), user.getPassword());
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        cookieMap.put(user.getId(), userCookie);
        return userCookie;
    }

    public void clearAll() {
        cookieMap.clear();
    }
}
