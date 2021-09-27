package fun.bookish.blueberry.core.utils;


import java.util.List;
import java.util.stream.Collectors;

/**
 * 实体Bean转换器
 * @author LIUXINDONG
 */
public class BeanUtils {

    private BeanUtils(){}

    /**
     * 实体类P转化为实体类K
     * @param sourceBean
     * @param targetClass
     * @param <P>
     * @param <K>
     * @return
     */
    public static <P, K> K convert(P sourceBean, Class<K> targetClass){
        K targetBean = org.springframework.beans.BeanUtils.instantiateClass(targetClass);
        org.springframework.beans.BeanUtils.copyProperties(sourceBean, targetBean);
        return targetBean;
    }

    /**
     * 实体类P集合转化为实体类K集合
     * @param sourceBeans
     * @param targetClass
     * @param <P>
     * @param <K>
     * @return
     */
    public static <P, K> List<K> convert(List<P> sourceBeans, Class<K> targetClass){
        return sourceBeans.stream().map(sourceBean -> {
            K targetBean = org.springframework.beans.BeanUtils.instantiateClass(targetClass);
            org.springframework.beans.BeanUtils.copyProperties(sourceBean, targetBean);
            return targetBean;
        }).collect(Collectors.toList());
    }

    /**
     * 将实体类P中的字段属性值赋到实体类K中相同的字段中
     * @param sourceBean
     * @param targetBean
     * @param <P>
     * @param <K>
     */
    public static <P, K> void copyProperties(P sourceBean, K targetBean){
        org.springframework.beans.BeanUtils.copyProperties(sourceBean, targetBean);
    }

    /**
     * 将实体类P中的字段属性值赋到实体类K中字段类型为editableClass的字段中
     * @param sourceBean
     * @param targetBean
     * @param editableFieldClass
     * @param <P>
     * @param <K>
     */
    public static <P, K> void copyProperties(P sourceBean, K targetBean, Class<?> editableFieldClass){
        org.springframework.beans.BeanUtils.copyProperties(sourceBean, targetBean, editableFieldClass);
    }

    /**
     * 将实体类P中的字段属性值赋到实体类K中相同的字段中, 忽略指定名称的字段
     * @param sourceBean
     * @param targetBean
     * @param ignoreProperties
     * @param <P>
     * @param <K>
     */
    public static <P, K> void copyProperties(P sourceBean, K targetBean, String... ignoreProperties){
        org.springframework.beans.BeanUtils.copyProperties(sourceBean, targetBean, ignoreProperties);
    }

}
