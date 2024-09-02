package com.sbr.web;

import com.sbr.loading.CCL;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/spiders")
@RequiredArgsConstructor
public class SpiderFactoryRegistratorController implements BeanClassLoaderAware {
    private ClassLoader classLoader;
    final CCL ccl;
    final GenericApplicationContext context;

    @PostMapping("/register")
    public ResponseEntity<SpiderFactoryRegistrationResponse> registerNewSpiderFactory(@RequestBody SpiderFactoryRegistrationRequest body) throws MalformedURLException, ClassNotFoundException {
        byte[] decoded = Base64.getDecoder().decode(body.getClassBytesBase64().getBytes(StandardCharsets.UTF_8));
        Class<?> loadedClass = ccl.registerClassByBytes(decoded);

        var registry = (BeanDefinitionRegistry) context.getBeanFactory();
        GenericBeanDefinition myBeanDefinition = new GenericBeanDefinition();
        myBeanDefinition.setBeanClass(loadedClass);
        myBeanDefinition.setScope(BeanDefinition.SCOPE_SINGLETON);
        registry.registerBeanDefinition("1234", myBeanDefinition);
        context.getBean("1234");

        return ResponseEntity.ok(SpiderFactoryRegistrationResponse.builder().resultedClassName(loadedClass.getCanonicalName()).build());
    }

    @Override
    public void setBeanClassLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

}
