package es.elovendo.rest.util;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import es.elovendo.model.item.Item;

public class ItemRowMapper implements RowMapper<Item> {

	@Override
	public Item mapRow(ResultSet rs, int rowNum) throws SQLException {
//		Item item = new Item(null, null, null, null, null, null, null, false, false, false, 0, 0);
		Item item = new Item();
		item.setItemId(rs.getLong(1));
		return item;
	}

}
