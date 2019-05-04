package spring.mongo.setup.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.config.AbstractMongoConfiguration;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;

@Configuration
@PropertySource("classpath:/config/mongo-config.properties")
public class SpringMongoBeanConfig extends AbstractMongoConfiguration {

	@Value("${mongo.hostname}")
	private String hostname;

	@Value("${mongo.port}")
	private String port;

	@Value("${mongo.username}")
	private String username;

	@Value("${mongo.password}")
	private String password;

	@Value("${mongo.database}")
	private String database;

	@Bean
	public MongoOperations mongoTemplate(MongoClient mongoClient) throws Exception {
		MongoOperations mongoTemplate = new MongoTemplate(mongoClient, this.database);
		return mongoTemplate;
	}

	@Override
	public MongoClient mongoClient() {
		final MongoCredential mongoCredentials = MongoCredential.createCredential(this.username, this.database,
				this.password.toCharArray());
		ServerAddress serverAddress = new ServerAddress(this.hostname, Integer.parseInt(this.port));
		return new MongoClient(serverAddress, mongoCredentials, null);
	}

	@Override
	protected String getDatabaseName() {
		return this.database;
	}

	@Bean
	MongoTransactionManager transactionManager(MongoDbFactory dbFactory) {
		return new MongoTransactionManager(dbFactory);
	}
}
