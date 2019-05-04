package spring.batch.database.setup.config;

import java.security.NoSuchAlgorithmException;
import java.util.Properties;

import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.support.JobRepositoryFactoryBean;
import org.springframework.batch.support.DatabaseType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@PropertySource("classpath:/config/batch-dbconfig.properties")
public class BatchMysqlBeanConfig {
	
		
	@Value("${jdbc.driverClassName}")
	private String driverClassName;

	@Value("${jdbc.url}")
	private String url;

	@Value("${jdbc.username}")
	private String username;

	@Value("${jdbc.password}")
	private String password;

	@Value("${serverTimezone}")
	private String serverTimezone;

	@Value("${verifyServerCertificate}")
	private String verifyServerCertificate;

	@Value("${useSSL}")
	private String useSSL;
			
	@Bean
	public DriverManagerDataSource mysqlDataSource() throws NoSuchAlgorithmException {
		DriverManagerDataSource mysqlDataSource = new DriverManagerDataSource();
		mysqlDataSource.setDriverClassName(this.driverClassName);
		mysqlDataSource.setUrl(this.url);
		mysqlDataSource.setUsername(this.username);
		mysqlDataSource.setPassword(this.password);
		Properties properties = new Properties();
		properties.put("serverTimezone", this.serverTimezone);
		properties.put("verifyServerCertificate", this.verifyServerCertificate);
		properties.put("useSSL", this.useSSL);
		mysqlDataSource.setConnectionProperties(properties);
		return mysqlDataSource;
	}	
	
	@Bean
	public DataSourceTransactionManager mysqlTransactionManager(DriverManagerDataSource mysqlDataSource) {
		return new DataSourceTransactionManager(mysqlDataSource);
	}
	
	@Bean
	public JobRepositoryFactoryBean jobRepository(DriverManagerDataSource mysqlDataSource, DataSourceTransactionManager mysqlTransactionManager) {
		JobRepositoryFactoryBean jobRepository = new JobRepositoryFactoryBean();
		jobRepository.setDataSource(mysqlDataSource);
		jobRepository.setTransactionManager(mysqlTransactionManager);
		jobRepository.setDatabaseType(DatabaseType.MYSQL.getProductName());
		return jobRepository;
	}
	
	@Bean
	public JobLauncher jobLauncher(JobRepositoryFactoryBean jobRepository, ThreadPoolTaskExecutor taskExecutor) throws Exception {
		SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
		jobLauncher.setJobRepository(jobRepository.getObject());
		jobLauncher.setTaskExecutor(taskExecutor);
		return jobLauncher;
	}	

}
