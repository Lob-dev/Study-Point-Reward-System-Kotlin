package demo.point.edge.config

import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.scheduling.annotation.EnableScheduling
import javax.persistence.EntityManager

@Configuration
@EnableScheduling
@EnableJpaAuditing
class JpaConfiguration {

    @Bean
    protected fun jpaQueryFactory(entitymanager: EntityManager) = JPAQueryFactory(entitymanager)

}