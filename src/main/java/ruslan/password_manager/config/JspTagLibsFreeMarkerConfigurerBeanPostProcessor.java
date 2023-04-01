package ruslan.password_manager.config;

import freemarker.ext.jsp.TaglibFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.util.Arrays;
import java.util.regex.Pattern;

@Component
public class JspTagLibsFreeMarkerConfigurerBeanPostProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof FreeMarkerConfigurer) {
            FreeMarkerConfigurer freeMarkerConfigurer = (FreeMarkerConfigurer) bean;
            TaglibFactory taglibFactory = freeMarkerConfigurer.getTaglibFactory();

            TaglibFactory.ClasspathMetaInfTldSource classpathMetaInfTldSource =
                    new TaglibFactory.ClasspathMetaInfTldSource(Pattern.compile(".*"));

            taglibFactory.setMetaInfTldSources(Arrays.asList(classpathMetaInfTldSource));
        }
        return bean;
    }
}
