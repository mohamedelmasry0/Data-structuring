package com.example.batching;

import java.sql.ResultSet;
import java.sql.SQLException;




import org.springframework.jdbc.core.RowMapper;

public class ContactRowMapper implements RowMapper<Contact> {
	@Override
	public Contact mapRow(ResultSet rs, int i) throws SQLException {
		Contact contact = new Contact();
		contact.setContact_address(rs.getString("contact_address"));
		//contact.setContact_birthdate(rs.getDate("contact_birthdate"));
		//contact.setContact_city(rs.getString("contact_city"));
		//contact.setContact_country(rs.getString("contact_country"));
		contact.setContact_email(rs.getString("contact_email"));
		contact.setContact_first_name(rs.getString("contact_first_name"));
		//contact.setContact_last_name(rs.getString("contact_last_name"));
		return contact;
	}

}
