## gcode
##代码可配置生成器，根据已存在的数据库表生成相关代码
###1.程序入口:ApplicationMain main方法，需要根据实际情况修改变量path
###2.配置目录:**${projectpath}/${dataName}**
dataName下面固定三个文件yml配置文件和一个目录，文件名字都必须固定：
- *systemConfig.yml*配置数据库、源码输出路径。
- *tableConfig.yml*配置数据库表的映射，包含表名映射、表字段映射、表是否参与生成代码等配置。
- *tempConfig.yml*表要生成的代码文件列表配置，包含包名、文件名、文件扩展名、文件类型、映射的模板等配置。
- *TempFile目录*，下面是参与代码生成的模板文件，是velocity。
    
   ${projectpath}下面配置多个${dataName}目录，表示一次性根据多个数据库生成不同的系统代码。
   
###3.目前提供了架构和适配，让以上配置文件可以可视化配置和统一管理，待开发。

