package it.intesys.orderservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.core.DefaultHttpLogFormatter;
import org.zalando.logbook.core.DefaultHttpLogWriter;
import org.zalando.logbook.core.DefaultSink;
import org.zalando.logbook.spring.LogbookClientHttpRequestInterceptor;

@Configuration
public class LogbookConfiguration {

    @Bean
    public Logbook buildLogbook() {
        return Logbook.builder()
                .headerFilter(HeaderFilter.none())
                .sink(new DefaultSink(new DefaultHttpLogFormatter(), new DefaultHttpLogWriter()))
                .build();
    }
}
