package com.apricot;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class ApricotApplication 
{
    public static void main( String[] args )
    {
    	new SpringApplicationBuilder(ApricotApplication.class).web(WebApplicationType.SERVLET).run(args);
    }
}
