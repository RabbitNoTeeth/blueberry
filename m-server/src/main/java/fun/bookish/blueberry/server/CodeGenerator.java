package fun.bookish.blueberry.server;

import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.converts.MySqlTypeConvert;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;
import com.baomidou.mybatisplus.generator.config.rules.IColumnType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

public class CodeGenerator {

    private static void doGenerate(String moduleName, String parentPackage, String tableName) {
        doGenerate("/m-server/src/main/java", moduleName, parentPackage, tableName);
    }

    private static void doGenerate(String outputDir, String moduleName, String parentPackage, String tableName) {
        // 代码生成器
        AutoGenerator mpg = new AutoGenerator();
        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + outputDir);
        gc.setAuthor("RabbitNoTeeth");
        gc.setOpen(false);
        gc.setEntityName("%sPO");
//         gc.setSwagger2(true);
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/blueberry?useSSL=false&useUnicode=true&characterEncoding=utf-8&serverTimezone=UTC&nullCatalogMeansCurrent=true");
        // dsc.setSchemaName("public");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setTypeConvert(new MySqlTypeConvertCustom());
        mpg.setDataSource(dsc);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setModuleName(moduleName);
        pc.setParent(parentPackage);
        mpg.setPackageInfo(pc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(false);
        strategy.setRestControllerStyle(true);
        strategy.setInclude(tableName);
        strategy.setControllerMappingHyphenStyle(true);
        strategy.setTablePrefix("t_");
        mpg.setStrategy(strategy);
        mpg.execute();
    }

    public static void main(String[] args) {
        doGenerate("arithmeticapplydevice", "fun.bookish.blueberry.server.videoqualitydetect", "t_video_quality_detect_arithmetic_apply_device");
    }

    /**
     * 自定义类型转换
     */
    static class MySqlTypeConvertCustom extends MySqlTypeConvert implements ITypeConvert{
        @Override
        public IColumnType processTypeConvert(GlobalConfig globalConfig, String fieldType) {
            String t = fieldType.toLowerCase();
            if (t.contains("tinyint(1)")) {
                return DbColumnType.INTEGER;
            }
            if (t.contains("datetime")) {
                return DbColumnType.STRING;
            }
            return super.processTypeConvert(globalConfig, fieldType);
        }
    }

}
