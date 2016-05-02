package com.project.decider.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Lobster on 30.04.16.
 */

@Configuration
@ComponentScan({"com.project.decider.user","com.project.decider.dao"})
@Import({HibernateConfiguration.class})
public class SpringRootConfig {
}
