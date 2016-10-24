package  com.ly.cms.service;

import com.ly.cms.vo.News;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.service.IdEntityService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.nutz.dao.Cnd;
import com.ly.comm.Page;

import java.util.List;


@IocBean(fields = { "dao" })
public class NewsService extends IdEntityService<News> {

	public static String CACHE_NAME = "news";
    public static String CACHE_COUNT_KEY = "news_count";

    public List<News> queryCache(Cnd c,Page p)
    {
        List<News> list_news = null;
        String cacheKey = "news_list_" + p.getPageCurrent();

        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(cacheKey) == null)
        {
            list_news = this.query(c, p);
            cache.put(new Element(cacheKey, list_news));
        }else{
            list_news = (List<News>)cache.get(cacheKey).getObjectValue();
        }
        return list_news;
    }

    public int listCount(Cnd c)
    {
        Long num = 0L;
        Cache cache = CacheManager.getInstance().getCache(CACHE_NAME);
        if(cache.get(CACHE_COUNT_KEY) == null)
        {
            num = Long.valueOf(this.count(c));
            cache.put(new Element(CACHE_COUNT_KEY, num));
        }else{
            num = (Long)cache.get(CACHE_COUNT_KEY).getObjectValue();
        }
        return num.intValue();
    }
}

