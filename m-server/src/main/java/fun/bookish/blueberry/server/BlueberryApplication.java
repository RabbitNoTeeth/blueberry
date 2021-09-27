package fun.bookish.blueberry.server;

import fun.bookish.blueberry.core.utils.SpringUtils;
import fun.bookish.blueberry.server.permission.PermissionCodeUtils;
import fun.bookish.blueberry.server.permission.PermissionProperties;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "fun.bookish.blueberry")
@MapperScan(basePackages = "fun.bookish.blueberry", annotationClass = Mapper.class)
public class BlueberryApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlueberryApplication.class, args);
        // 检测序列号是否有效
        checkPermissionCode();
    }

    /**
     * 检查序列号是否有效
     */
    private static void checkPermissionCode() {
        PermissionProperties permissionProperties = SpringUtils.getBeanByClass(PermissionProperties.class);
        Boolean check = permissionProperties.getCheck();
        if (!check) {
            return;
        }
        PermissionCodeUtils.check(permissionProperties.getCode());
    }

}
