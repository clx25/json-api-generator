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
6. 返回类型解析(支持多泛型与嵌套泛型)

目前解析这样的一个方法的返回结果
[![5zc02j.png](https://z3.ax1x.com/2021/10/30/5zc02j.png)](https://imgtu.com/i/5zc02j)
[![5zcyq0.png](https://z3.ax1x.com/2021/10/30/5zcyq0.png)](https://imgtu.com/i/5zcyq0)
[![5zc4z9.png](https://z3.ax1x.com/2021/10/30/5zc4z9.png)](https://imgtu.com/i/5zc4z9)
[![5zcMPe.png](https://z3.ax1x.com/2021/10/30/5zcMPe.png)](https://imgtu.com/i/5zcMPe)

### 未完成的功能
1. 添加注解方式的返回类型。
2. 添加返回数据字段描述。
3. 根据有没有jakarta.validation这个包，判断是否开启jsr303注解解析，根据这些注解判断该值是不是必需。
4. 添加注释数据的刷新功能。
5. 优化接收参数类型。
6. 优化解构。
### 其他
1. 使用json来展示复杂信息好像有些麻烦，可能会改成普通页面。
2. 多线程优化。
