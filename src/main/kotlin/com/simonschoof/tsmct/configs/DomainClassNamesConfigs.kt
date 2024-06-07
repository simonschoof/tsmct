package com.simonschoof.tsmct.configs

import com.simonschoof.tsmct.domain.AggregateRoot
import com.simonschoof.tsmct.domain.Event
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider
import org.springframework.context.annotation.Configuration
import org.springframework.core.type.filter.AssignableTypeFilter

private const val BASE_PACKAGE = "com.simonschoof.tsmct"

@Configuration
class ClassPathConfig() {
    @Bean("AggregateRootClassNames")
    fun aggregateRootClassNames(): Set<String> {
        val provider = ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(AssignableTypeFilter(AggregateRoot::class.java));
        val beans = provider.findCandidateComponents(BASE_PACKAGE)
        val beansNamesList = beans.mapNotNull { it.beanClassName }
        val beansNamesSet = beansNamesList.toSet()
        if (beansNamesList.count() != beansNamesSet.count()) {
            throw Error("Domain contains aggregates with the same name!")
        }
        return beansNamesSet

    }

    @Bean("EventClassNames")
    fun eventClassNames(): Set<String> {
        val provider = ClassPathScanningCandidateComponentProvider(false);
        provider.addIncludeFilter(AssignableTypeFilter(Event::class.java));
        val beans = provider.findCandidateComponents(BASE_PACKAGE)
        val beansNamesList = beans.mapNotNull { it.beanClassName }
        val beansNamesSet = beansNamesList.toSet()
        if (beansNamesList.count() != beansNamesSet.count()) {
            throw Error("Domain contains events with the same name! Follow the convention and " +
                    "prefix the event with the aggregate name")
        }
        return beansNamesSet
    }
}