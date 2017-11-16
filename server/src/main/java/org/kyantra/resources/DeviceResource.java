package org.kyantra.resources;

import io.swagger.annotations.Api;
import org.kyantra.beans.DeviceBean;
import org.kyantra.dao.DeviceDAO;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by Siddhesh Prabhugaonkar on 13-11-2017.
 */
@Path("/device")
@Api(value="device")
public class DeviceResource extends BaseResource {
    @GET
    @Path("get/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String get(@PathParam("id") Integer id){
        DeviceBean bean = DeviceDAO.getInstance().get(id);
        return gson.toJson(bean);
    }

    @POST
    @Path("update/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String update(@PathParam("id") Integer id, DeviceBean bean){
        DeviceDAO.getInstance().update(id, bean.getName(), bean.getDescription(), bean.getDeviceAttributes());
        bean = DeviceDAO.getInstance().get(bean.getId());
        return gson.toJson(bean);
    }

    @DELETE
    @Path("delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public String delete(@PathParam("id") Integer id){
        try {
            DeviceDAO.getInstance().delete(id);
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
    public String create(DeviceBean bean){ //thing_id
        try {
            String s = "Found something";
            System.out.println(gson.toJson(bean));
            DeviceBean deviceBean = DeviceDAO.getInstance().add(bean, bean.getOwnerUnit());
            return gson.toJson(deviceBean);

        }catch (Throwable t){
            t.printStackTrace();
        }
        return "{\"success\":false}";
    }
}
