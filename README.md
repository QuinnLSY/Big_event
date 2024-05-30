
# 实体功能实现逻辑
- 创建controller->定义功能->创建service接口->创建serviceimpl实现接口->创建mapper接口->调用sql实现功能;

# 定义统一的响应
- 项目通过pojo/Result定义统一响应，返回统一响应结构，方便前端进行统一处理，减少重复代码;
- 同时在exception/GlobalExceptionHandler定义全局异常处理，统一处理异常，减少重复代码;

# 拦截器
- 项目通过拦截器实现对请求的拦截，拦截器在请求到达controller之前进行拦截；
- 使用utils/JwtUtil实现token令牌制作（在pom.mxl中配置jwt的依赖）；
- 使用utils/ThreadLocalUtil实现用户线程变量，使线程之间不共享变量，独自拥有自己的token令牌，避免线程安全问题;
- 使用interceptor/LoginInterceptor，实现对登录token令牌进行拦截，拦截器在请求到达controller之前进行拦截，拦截器通过ThreadLocalUtil获取token令牌，判断token令牌是否合法，如果合法则放行，否则抛出异常;
  - 其在config/WebConfig类中配置拦截器，将拦截器添加到拦截器链中，实现除登录和注册页面外其它页面不登录无权访问; 

# 自定义注解
- 项目的文章信息中发布状态state只能是草稿或已发布，因为现有注解无法完成对中文的判断，所以需要自定义注解;
  1. 创建注解类anno/State，定义注解基本结构，使用`@Constraint(validatedBy = {StateValidation.class} // 指定用于验证的实现类)`获取注解的实现类;
  2. 创建validation/StateValidation，实现注解的实现类，实现对中文的判断，判断state的值是否是草稿或已发布;
  3. 在pojo/Article中的state变量上添加`@State`注解，实现对state的校验。

# 获取文章列表
- 在ArticleController中实现了list方法，返回List\<Article\>，返回的是数据库中的文章列表，<font face='黑体' color=#0f0 size=3>注意：这里返回的是数据库中的文章列表，而不是数据库中的文章实体</font>;
  ## PageBean分页
1. 项目使用PageBean分页，返回PageBean\<Article\>，使文章列表以分页形式展现；
2. 使用PageBean需要在pom.xml中引入pagehelper插件，并在pojo中引入PageBean类，定义分页样式;
  ## 获取文章列表的sql语句
- 普通sql语句没有查询并返回信息列表的功能，需要借助相关语句配置完成：
  1. 在resources文件夹下创建与ArticleMapper.java同目录结构（目录使用com/cjx/mapper创建），同文件名的ArticleMapper·xml文件，配置sql语句;
  2. 在ArticleMapper.xml文件中添加`<mapper namespace="com.cjx.mapper.ArticleMapper">`映射到ArticleMapper.java;

# 打包
1. 在pom.xml中引入打包插件，在<build>标签下添加配置;
2. 确保所有test文件<font face='黑体' color=#0f0 size=3>都能独立运行</font>，不能的注释掉@test或全部注释掉，因为打包时会运行所有项目文件包括test文件，本项目中的Demo（需要阿里云oss服务）和JwtTest（令牌有时效性）无法保证随时运行;
3. 检查项目文件target目录下是否生成了jar文件，如果生成了，则打包成功;
4. 前往本地项目文件夹下，打开target文件夹，找到生成的jar文件;
5. 在target文件夹下进入终端，输入java -jar xxx.jar运行jar文件，<font face='黑体' color=#0ff size=3>确保idea没有运行springboot项目，否则会冲突</font>;

# 配置文件
- 在项目的resources文件夹下有项目运行的配置application.yml文件，但打包后整个项目只部署jar文件，无法对项目内配置文件进行修改
- 因此，有以下四种办法修改配置（**按优先级升序**，以service端口为例）： 
  1. 通过运行命令行修改：java -jar xxx.jar --server.port=8081，
  2. 添加操作系统环境变量（关闭终端窗口重启终端生效，其它方法不需要重启），
  3. 在jar同目录下创建一个application.yml文件，
  4. 在原项目的resources文件夹下修改application.yml文件，重新打包。