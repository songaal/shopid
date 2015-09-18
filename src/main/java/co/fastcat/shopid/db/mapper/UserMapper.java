package co.fastcat.shopid.db.mapper;

import org.apache.ibatis.annotations.Param;
import co.fastcat.shopid.db.entity.User;

import java.util.List;

public interface UserMapper {

	public User select(@Param("id") String id);

    public User selectWithPassword(User user);

	public void insert(User user);

    public void truncate();

    public List<User> selectInOrganization(String orgId);

    public void delete(@Param("id") String id);
}
