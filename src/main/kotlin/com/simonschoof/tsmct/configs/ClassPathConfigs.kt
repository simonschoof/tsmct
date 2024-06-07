package com.simonschoof.tsmct.configs

import com.simonschoof.tsmct.domain.AggregateRoot
import com.simonschoof.tsmct.domain.Event
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AssignableTypeFilter


// scan in org.example.package
//Set<BeanDefinition> components = provider.findCandidateComponents("org/example/package");
//for (BeanDefinition component : components)
//{
//    Class cls = Class.forName(component.getBeanClassName());
//    // use class cls found
//}

private const val BASE_PACKAGE = "com.simonschoof.tsmct"

@Configuration
class ClassPathConfig() {
    @Bean("AggregateRootBeanNames")
    fun aggregateRootBeanNames(): List<String> {
        val provider = ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(AssignableTypeFilter(AggregateRoot::class.java));
        val beans = provider.findCandidateComponents(BASE_PACKAGE)
        return beans.mapNotNull { it.beanClassName }

    }

    @Bean("EventBeanNames")
    fun eventBeanNames(): List<String> {
        val provider = ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(AssignableTypeFilter(Event::class.java));
        val beans = provider.findCandidateComponents(BASE_PACKAGE)
        return beans.mapNotNull { it.beanClassName }
    }
}