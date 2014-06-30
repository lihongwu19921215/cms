package com.ly.cms.action;

import com.ly.comm.Dwz;
import com.ly.comm.Page;
import com.ly.comm.ParseObj;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.ioc.loader.annotation.Inject;
import org.nutz.ioc.loader.annotation.IocBean;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.mvc.annotation.*;
import org.nutz.mvc.filter.CheckSession;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

import com.ly.cms.vo.Link;
import com.ly.cms.service.LinkService;


@IocBean
@At("/link")
@Fail("json")
@Filters(@By(type=CheckSession.class, args={"username", "/WEB-INF/login.html"}))
public class LinkAction {

	private static final Log log = Logs.getLog(LinkAction.class);
	
	@Inject
	private LinkService linkService;

    @At("/")
    @Ok("beetl:/WEB-INF/cms/link_list.html")
    public void index(@Param("..")Page p,
                      @Param("..")Link link,
                      HttpServletRequest request){
        Cnd c = new ParseObj(link).getCnd();
        List<Link> list_m = linkService.query(c, p);
        p.setRecordCount(linkService.count(c));

        request.setAttribute("list_obj", list_m);
        request.setAttribute("page", p);
        request.setAttribute("link", link);
    }

    @At
    @Ok("beetl:/WEB-INF/cms/link.html")
    public void edit(@Param("id")Long id,
                      HttpServletRequest request){
        if(id == null || id == 0){
            request.setAttribute("link", null);
        }else{
            request.setAttribute("link", linkService.fetch(id));
        }
    }

    @At
    @Ok("json")
    public Map<String,String> save( @Param("..")Link link){
        Object rtnObject;
        if (link.getId() == null || link.getId() == 0) {
            rtnObject = linkService.dao().insert(link);
        }else{
            rtnObject = linkService.dao().updateIgnoreNull(link);
        }
        return Dwz.rtnMap((rtnObject == null) ? false : true, "link", "closeCurrent");
    }

    @At
    @Ok("json")
    public Map<String,String> del(@Param("id")Long id)
    {
        int num =  linkService.delete(id);
        return Dwz.rtnMap((num > 0) ? true : false , "link", "");
    }

}
