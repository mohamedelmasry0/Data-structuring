package com.example.batching;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.batch.item.ItemProcessor;

public class ContactAgeProcessor implements ItemProcessor<Contact, Contact> {

	public Contact process(Contact contact) throws Exception {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date1 = sdf.parse("1991-01-01");
		Date date2 = sdf.parse("contact.getContact_birthdate()");
		int comparisonResult = date1.compareTo(date2);

		if (comparisonResult < 0) {
			// System.out.println("bigger than 30");
			return null;
		}

		return contact;
	}

}
