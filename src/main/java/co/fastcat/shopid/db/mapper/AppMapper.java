package co.fastcat.shopid.db.mapper;

import co.fastcat.shopid.db.entity.App;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by swsong on 2015. 8. 17..
 */
public interface AppMapper {

    public List<App> listAll();

    public List<App> listByOrganization(@Param("orgId") String orgId);

    public List<App> listOuterApp(@Param("orgId") String orgId);

    public App select(@Param("id") String id);

    public void insert(App app);

    public void update(App app);

    public void delete(@Param("id") String id);

    public void truncate();

    public Integer getGrant(@Param("orgId") String orgId, @Param("appId") String appId);

    public void setGrant(@Param("orgId") String orgId, @Param("appId") String appId);

    public void removeGrant(@Param("orgId") String orgId, @Param("appId") String appId);

    public void setAppFileUpdatedDone(@Param("id") String id);
}
