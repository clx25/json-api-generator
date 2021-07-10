只写了个开头。。。

原理：
1. 项目启动时使用jdk的tools.jar包解析.java文件的注释，并且保存。
2. springmvc在启动时会解析controller，把path和对应的方法保存到requestMappingHandlerMapping的父类AbstractHandlerMethodMapping的mappingRegistry中。
3. spring在启动完成后会发布ContextRefreshedEvent事件，可以使用一个监听器处理这个事件。
4. 从requestMappingHandlerMapping获取到path与方法数据。根据全类名，方法签名，字段签名等数据去匹配保存的注释，再构造json，传到页面。

### 已完成的功能
1. 解析controller的方法注释。
2. 获取请求方式。
3. 获取请求的path，过滤部分controller。
4. 解析controller方法的@param注释，部分入参的过滤。
5. 使用json展示，由于默认的json样式不好看，所以使用了thymeleaf，在网上找了个json-viewer的js优化样式。

目前的返回样式

   [![WpYsPO.png](https://z3.ax1x.com/2021/07/10/WpYsPO.png)](https://imgtu.com/i/WpYsPO)
### 未完成的功能
1. 解析返回值注释，由于项目一般会返回一个泛型对象，里面包含状态信息，所以需要用户设置一个成功状态的模板。
2. 根据有没有jakarta.validation这个包，判断是否开启jsr303注解解析，根据这些注解判断该值是不是必需。
3. 多线程优化。
4. 添加注释数据的刷新功能。
### 其他
1. 使用json来展示复杂信息好像有些麻烦，可能会该成普通页面。