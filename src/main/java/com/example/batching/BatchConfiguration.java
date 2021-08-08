package com.example.batching;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.oxm.xstream.XStreamMarshaller;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
	@Autowired
	public JobBuilderFactory jobBuilderFactory;

	@Autowired
	public StepBuilderFactory stepBuilderFactory;




	@Bean
	public JdbcCursorItemReader<Contact> reader() {
		JdbcCursorItemReader<Contact> reader = new JdbcCursorItemReader<Contact>();
		reader.setDataSource(dataSource());
		reader.setSql("select contact_email, contact_address, contact_first_name from contact");
		reader.setRowMapper(new ContactRowMapper());
		return  reader;
	}
	@Bean
	public DataSource dataSource(){
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName("com.mysql.jdbc.Driver");
		dataSource.setUsername("postgres");
		dataSource.setPassword("123");
		dataSource.setUrl("jdbc:postgresql://localhost:5432/batch"); //TODO complete
		return dataSource;
	}

	/*@Bean
	public ContactEmailProcessor processor() {
		return new ContactEmailProcessor();

	}*/
	

	@Bean
	public FlatFileItemWriter<Contact> writer() {
		FlatFileItemWriter<Contact> writer= new FlatFileItemWriter <Contact>();
		writer.setResource(new ClassPathResource("contact.csv"));
		writer.setLineAggregator(new DelimitedLineAggregator<Contact>() {{
			setDelimiter(",");
			setFieldExtractor(new BeanWrapperFieldExtractor<Contact>() {{
				setNames(new String[] {"contact_email", "contact_address", "contact_first_name"});
			}});
		}});
		return writer;
	}
	
	@Bean
	public StaxEventItemWriter<Contact> itemWriterXML() {
		 StaxEventItemWriter<Contact> writer = new StaxEventItemWriter<Contact>();
		 writer.setResource(new FileSystemResource("C://Users/users/eclipse-workspace/batching/target/classes/contacts.xml"));
		 Map<String, String> alaisMap = new HashMap<String, String>();
		 alaisMap.put("Contact", "com.example.batching.Contact");
		 XStreamMarshaller marshaller = new XStreamMarshaller();
		 marshaller.setAliases(alaisMap);
	     writer.setMarshaller(marshaller);  
	     writer.setRootTagName("contacts");
	     writer.setOverwriteOutput(true);
	     
	 
	        return writer;
	    }
	

	@Bean
	public Job exportContactJob() {
		return jobBuilderFactory.get("exportContactJob")
				.incrementer(new RunIdIncrementer())
				
				.flow(step1())
				.end()
				.build();
	}

	@Bean
	public Step step1() {
		return stepBuilderFactory.get("step1")
				.<Contact, Contact> chunk(10)
				.reader(reader())
				//.processor(processor())
				.writer(writer())
				.build();
	}
	/*@Bean
    public ItemProcessor<Contact, Contact> processor() {
        return new ContactAgeProcessor();
    }*/


}
