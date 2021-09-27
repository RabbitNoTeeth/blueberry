package fun.bookish.blueberry.core.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * spring工具类
 * @author LIUXINDONG
 */
@Component
public class SpringUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext = null;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (SpringUtils.applicationContext == null) {
            SpringUtils.applicationContext = applicationContext;
        }
    }

    /***
     * 根据一个bean的id获取容器中相应的bean
     */
    public static Object getBean(String name) throws BeansException {
        if (applicationContext.containsBean(name)) {
            return applicationContext.getBean(name);
        }
        return null;
    }

    /***
     * 根据一个bean的id获取容器中相应的bean
     */
    public static <T> T getBean(String name, @Nullable Class<T> requiredType) throws BeansException {
        if (applicationContext.containsBean(name)) {
            return applicationContext.getBean(name, requiredType);
        }
        return null;
    }

    /***
     * 根据一个bean的类型获取容器中相应的bean
     */
    public static <T> T getBeanByClass(Class<T> requiredType) throws BeansException {
        return applicationContext.getBean(requiredType);
    }

    /**
     * 根据注解获取bean
     * @param annotationType
     * @return
     */
    public static Map<String, Object> getBeansWithAnnotation(Class<? extends Annotation> annotationType){
        return applicationContext.getBeansWithAnnotation(annotationType);
    }

    /**
     * 根据类型获取bean集合
     * @param type
     * @param <T>
     * @return
     */
    public static <T> List<T> getBeansByType(Class<T> type){
        return new ArrayList<>(applicationContext.getBeansOfType(type).values());
    }

    /**
     * 如果BeanFactory包含一个与所给名称匹配的bean定义，则返回true
     */
    public static boolean containsBean(String name) {
        return applicationContext.containsBean(name);
    }

    public static ApplicationContext getApplicationContext() {
        return SpringUtils.applicationContext;
    }

}
