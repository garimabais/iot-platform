package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.RoleEnum;
import org.kyantra.beans.UnitBean;
import org.kyantra.beans.UserBean;
import org.kyantra.dao.UnitDAO;
import org.kyantra.interfaces.Secure;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.security.Principal;
import java.util.List;
import java.util.Set;

/**
 * Created by Lenovo on 12-11-2017.
 */
@Path("/unit")
@Api(value="unit")
public class UnitResource extends BaseResource {

    int limit = 10;

    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        return gson.toJson(unitBean);
    }

    @GET
    @Path("list/page/{page}")
    @Produces(MediaType.APPLICATION_JSON)
    public String list(@PathParam("page") Integer page){
        Principal principal = getSecurityContext().getUserPrincipal();
        UserBean currentUser = (UserBean) principal;
        List<UnitBean> users = UnitDAO.getInstance().list(page,limit);
        return gson.toJson(users);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer id, UnitBean unitBean){
        UnitDAO.getInstance().update(id,unitBean.getUnitName(),unitBean.getDescription(),unitBean.getPhoto(),
                unitBean.getParent(), unitBean.getSubunits());
        unitBean = UnitDAO.getInstance().get(unitBean.getId());
        return gson.toJson(unitBean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id){
        try {
            UnitDAO.getInstance().delete(id);
            return "{}";
        }catch (Throwable t) {
            t.printStackTrace();
        }
        return "{}";
    }


    @POST
    @Path("create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Secure(roles = {RoleEnum.ALL,RoleEnum.WRITE}, subjectType = "unit", subjectField = "parent_id")
    public String create(UnitBean childUnit){
        try {
            String s = "Found something";
            System.out.println(gson.toJson(childUnit));
            UnitBean unit = UnitDAO.getInstance().add(childUnit.getParent(), childUnit);
            return gson.toJson(unit);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }

    @POST
    @Path("addusers/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String addUsers(@PathParam("id") Integer id, Set<UserBean> users){
        UnitDAO.getInstance().addUsers(id,users);
        UnitBean unitBean = UnitDAO.getInstance().get(id);
        return gson.toJson(unitBean);
    }
}
