package demo.point.edge.config

import com.zaxxer.hikari.HikariDataSource
import demo.point.edge.common.Constant.Companion.TRANSACTIONAL_ORDER
import demo.point.edge.common.shard.ReadOption
import demo.point.edge.common.shard.ShardGroupSource
import demo.point.edge.common.shard.ShardKeyResolver
import demo.point.edge.common.shard.ShardMetadata
import demo.point.edge.config.properties.ShardGroupProperty
import demo.point.edge.config.properties.ShardNodeConfig
import mu.KotlinLogging
import org.jooq.ConnectionProvider
import org.jooq.SQLDialect
import org.jooq.impl.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement
import javax.sql.DataSource

@Configuration
@EnableScheduling
@EnableTransactionManagement(order = TRANSACTIONAL_ORDER)
class PersistenceConfiguration {

    private val logger = KotlinLogging.logger {}

    @Bean
    fun query(configuration: DefaultConfiguration): DefaultDSLContext =
            DefaultDSLContext(configuration)

    @Bean
    fun configuration(connectionProvider: ConnectionProvider): DefaultConfiguration =
            DefaultConfiguration().apply {
                set(connectionProvider)
                set(DefaultExecuteListenerProvider(DefaultExecuteListener()))
                set(SQLDialect.MARIADB)
            }

    @Bean
    fun connectionProvider(lazyDataSource: DataSource): DataSourceConnectionProvider =
            DataSourceConnectionProvider(TransactionAwareDataSourceProxy(lazyDataSource))

    @Bean
    fun transactionManager(lazyDataSource: DataSource): PlatformTransactionManager =
            DataSourceTransactionManager(lazyDataSource)

    @Bean
    fun lazyDataSource(shardGroupSource: AbstractRoutingDataSource): DataSource =
            LazyConnectionDataSourceProxy(shardGroupSource)

    @Bean
    fun shardGroupSource(shardGroupProperty: ShardGroupProperty): AbstractRoutingDataSource {
        val dataSources = mutableMapOf<Any, Any>()
        shardGroupProperty.nodes.associateTo(dataSources) {
            val sourceKey = "${it.groupId}_${if (it.readOnly) ReadOption.RO else ReadOption.RW}"
            logger.info { "sourceKey = $sourceKey" }
            sourceKey to generateDataSource(it)
        }

        logger.info { "dataSources = $dataSources" }
        val shardMetadata = shardGroupProperty.nodes
                .map { ShardMetadata(it.groupId, it.minKey, it.maxKey) }
                .toList()
        return ShardGroupSource(dataSources, ShardKeyResolver(shardGroupProperty.strategy, shardMetadata))
    }

    private fun generateDataSource(config: ShardNodeConfig): HikariDataSource =
            HikariDataSource().apply {
                jdbcUrl = config.url
                username = config.username
                password = config.password
                driverClassName = config.driverClassName
            }
}