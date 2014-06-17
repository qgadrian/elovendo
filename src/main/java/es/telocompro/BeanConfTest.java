//package es.telocompro;
//
//import org.apache.commons.dbcp.BasicDataSource;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.orm.jpa.JpaTransactionManager;
//import org.springframework.orm.jpa.JpaVendorAdapter;
//import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
//import org.springframework.orm.jpa.vendor.Database;
//import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
//import org.springframework.transaction.PlatformTransactionManager;
//
//import javax.sql.DataSource;
//
//@Configuration
//@EnableJpaRepositories
//public class BeanConfTest {
//
//   @Bean
//   public DataSource dataSource() {
//   	BasicDataSource ds = new BasicDataSource();
//		ds.setDriverClassName("com.mysql.jdbc.Driver");
//		ds.setUrl("jdbc:mysql://localhost:3306/telocomprotest");
//		ds.setUsername("admin");
//		ds.setPassword("admin");
//		return ds;
//   }
//
//   @Bean
//   public LocalContainerEntityManagerFactoryBean entityManagerFactory(
//   		DataSource dataSource, JpaVendorAdapter jpaVendorAdapter) {
//
//       LocalContainerEntityManagerFactoryBean lef =
//       		new LocalContainerEntityManagerFactoryBean();
//       lef.setDataSource(dataSource);
//       lef.setJpaVendorAdapter(jpaVendorAdapter);
//       lef.setPackagesToScan("es.telocompro");
//       lef.setBeanName("UserRepository");
//       return lef;
//   }
//
//   @Bean
//   public JpaVendorAdapter jpaVendorAdapter() {
//       HibernateJpaVendorAdapter hibernateJpaVendorAdapter =
//       		new HibernateJpaVendorAdapter();
//       hibernateJpaVendorAdapter.setShowSql(false);
//       hibernateJpaVendorAdapter.setGenerateDdl(true);
//       hibernateJpaVendorAdapter.setDatabase(Database.MYSQL);
//       return hibernateJpaVendorAdapter;
//   }
//
//   @Bean
//   public PlatformTransactionManager transactionManager() {
//       return new JpaTransactionManager();
//   }
//}
