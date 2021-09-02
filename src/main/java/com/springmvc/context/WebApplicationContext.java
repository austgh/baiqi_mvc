package com.springmvc.context;

import com.springmvc.annotation.AutoWired;
import com.springmvc.annotation.Controller;
import com.springmvc.annotation.Service;
import com.springmvc.xml.XmlPaser;

import java.io.File;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 白起老师
 * springMvc容器
 */
public class WebApplicationContext {

    /**
     *classpath:springmvc.xml
     */
    String contextConfigLocation;

    /**
     * 定义集合  用于存放 bean 的权限名|包名.类名
     */

    List<String> classNameList = new ArrayList<>();

   /* 创建Map集合用于扮演IOC容器：  key存放bean的名字   value存放bean实例*/
    public Map<String,Object> iocMap = new ConcurrentHashMap<>();

    public WebApplicationContext() {
    }

    public WebApplicationContext(String contextConfigLocation) {
           this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * 初始化Spring容器
     */
    public void onRefresh(){

        //1、进行解析springmvc配置文件操作  ==》 com.baiqi.controller,com.baiqi.service
         String pack = XmlPaser.getbasePackage(contextConfigLocation.split(":")[1]);

         String[] packs = pack.split(",");
         //2、进行包扫描
         for(String pa : packs){
             excuteScanPackage(pa);
         }

         //3、实例化容器中bean
        executeInstance();

         //4、进行 自动注入操作
        executeAutoWired();
    }

    //进行自动注入操作
    public void executeAutoWired(){

        try {
            //从容器中 取出  bean  ，然后判断 bean中是否有属性上使用 AutoWired，如果使用了搞注解，就需要进行自动注入操作
            for (Map.Entry<String, Object> entry : iocMap.entrySet()) {
                //获取容器中的bean
                Object bean = entry.getValue();
                //获取bean中的属性
                Field[] fields = bean.getClass().getDeclaredFields();
                for (Field field : fields) {
                    if(field.isAnnotationPresent(AutoWired.class)){
                        //获取注解中的value值|该值就是bean的name
                        AutoWired autoWiredAno =  field.getAnnotation(AutoWired.class);
                        String beanName = autoWiredAno.value();
                        //取消检查机制
                        field.setAccessible(true);
                        //调用set方法
                        field.set(bean,iocMap.get(beanName));
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    /**
     * 实例化容器中的bean
     */
    public void executeInstance(){

        try{
            // com.baiqi.controller.UserController      com.baiqi.service.impl.UserServiceImpl
            for (String className : classNameList) {

                Class<?> clazz =   Class.forName(className);

                if(clazz.isAnnotationPresent(Controller.class)){
                    //控制层 bean

                    String beanName = clazz.getSimpleName().substring(0,1).toLowerCase()+ clazz.getSimpleName().substring(1);
                    iocMap.put(beanName,clazz.getDeclaredConstructor().newInstance());

                }else if(clazz.isAnnotationPresent(Service.class)){
                    //Service层  bean
                    Service serviceAn = clazz.getAnnotation(Service.class);
                   String beanName = serviceAn.value();
                    iocMap.put(beanName,clazz.getDeclaredConstructor().newInstance());
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }


    }

    /**
     * 扫描包
     */
    public void excuteScanPackage(String pack){
        //   com.baiqi.controller   ==> com/baiqi/controller
        URL url = this.getClass().getClassLoader().getResource("/" + pack.replaceAll("\\.", "/"));
        String path = url.getFile();
        // /com/bruce/service
        File dir=new File(path);
        for(File f:dir.listFiles()){
            if(f.isDirectory()){
                //当前是一个文件目录  com.baiqi.service.impl
                excuteScanPackage(pack+"."+f.getName());
            }else{
                //文件目录下文件  获取全路径   UserController.class  ==> com.baiqi.controller.UserController
                String className=pack+"."+f.getName().replaceAll(".class","");
                classNameList.add(className);
            }
        }
    }

}
