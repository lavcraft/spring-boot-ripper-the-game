package com.sbr.loading;

import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class CCL extends ClassLoader {
    public Class<?> registerClassByBytes(byte[] b) {
        return defineClass("strategy0.BlazinglyFastSpider", b, 0, b.length);
    }
    @Override
    @SneakyThrows
    public Class<?> findClass(String classFile) {
        Path path = Path.of(classFile);
        byte[] b = Files.newInputStream(path).readAllBytes();
//        return defineClass(extractClassName(path.toUri().toURL()), b, 0, b.length);
        return defineClass("strategy0.BlazinglyFastSpider", b, 0, b.length);
    }

    private String extractClassName(URL url) {
        String path = Paths.get(url.getPath()).getFileName().toString();
        return path.replace(".class", "").replace("//", ".");
    }
}
