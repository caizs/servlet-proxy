package org.caizs.servletproxy.config;

import org.caizs.nettypush.core.common.ConfigLoader;
import org.caizs.servletproxy.web.MsgServlet;
import org.apache.catalina.core.ApplicationServletRegistration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.util.Arrays;
import java.util.List;

@Configuration
public class ProxyServletConfig implements ServletContextInitializer, EmbeddedServletContainerCustomizer {

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        getConfiguredServlets().stream().forEach(s -> registerServlet(servletContext, "proxy." + s.trim() + "."));
    }

    private List<String> getConfiguredServlets() {
        return Arrays.asList(ConfigLoader.getProperty("proxy.servlets").split(","));
    }

    private void registerServlet(ServletContext servletContext, String servletName) {
        ServletRegistration.Dynamic serviceServlet = servletContext.addServlet(servletName, new ProxyServlet());
        ApplicationServletRegistration registration = (ApplicationServletRegistration) serviceServlet;
        registration.setInitParameter(ProxyServlet.P_TARGET_URI, ConfigLoader.getProperty(servletName + "target_url"));
        if (ConfigLoader.getProperty(servletName + "host") != null) {
            registration.setInitParameter(ProxyServlet.P_HOST, ConfigLoader.getProperty(servletName + "host"));
        }
        if (ConfigLoader.getProperty(servletName + "exclude") != null) {
            registration.setInitParameter(ProxyServlet.P_EXCLUDE, ConfigLoader.getProperty(servletName + "exclude"));
        }
        registration.setInitParameter(ProxyServlet.P_LOG, ConfigLoader.getProperty("proxy.logging_enabled", "false"));

        serviceServlet.addMapping(ConfigLoader.getProperty(servletName + "url_mapping"));
        serviceServlet.setLoadOnStartup(2);

    }

    @Override public void customize(ConfigurableEmbeddedServletContainer container) {
        //设置tomcat监听端口
        container.setPort(ConfigLoader.getPropertyInt("proxy.port"));
    }
}
