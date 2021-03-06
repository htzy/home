package com.huangshihe.home.model;

import javax.sql.DataSource;
import com.huangshihe.home.config.BaseConfig;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.generator.Generator;
import com.jfinal.plugin.c3p0.C3p0Plugin;

/**
 * Created by huangshihe on 3/20/16.
 * 在数据库表有任何变动时，运行一下 main 方法，极速响应变化进行代码重构
 */
public class _JFinalGenerator {
    public static DataSource getDataSource() {
        PropKit.use("a_little_config.txt");
        C3p0Plugin c3p0Plugin = BaseConfig.createC3p0Plugin();
        c3p0Plugin.start();
        return c3p0Plugin.getDataSource();
    }

    public static void main(String[] args) {
        // base model 所使用的包名
        String baseModelPackageName = "com.huangshihe.home.model.base";
        // base model 文件保存路径
        String baseModelOutputDir = PathKit.getWebRootPath() + "/src/main/java/com/huangshihe/home/model/base";

        System.out.println("baseModelOutputDir = " + baseModelOutputDir);

        // model 所使用的包名 (MappingKit 默认使用的包名)
        String modelPackageName = "com.huangshihe.home.model";
        // model 文件保存路径 (MappingKit 与 DataDictionary 文件默认保存路径)
        String modelOutputDir = baseModelOutputDir + "/..";

        System.out.println("modelOutputDir = " + modelOutputDir);
        // 创建生成器
        Generator generator = new Generator(getDataSource(), baseModelPackageName, baseModelOutputDir, modelPackageName, modelOutputDir);
        // 添加不需要生成的表名
        generator.addExcludedTable("adv");
        // 设置是否在 Model 中生成 dao 对象
        generator.setGenerateDaoInModel(true);
        // 设置是否生成字典文件
        generator.setGenerateDataDictionary(true);
        // 设置需要被移除的表名前缀用于生成modelName。例如表名 "osc_user"，移除前缀 "osc_"后生成的model名为 "User"而非 OscUser
        generator.setRemovedTableNamePrefixes("t_");
        // 生成
        generator.generate();
    }
}
