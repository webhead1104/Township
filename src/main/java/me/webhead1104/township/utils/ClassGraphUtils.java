package me.webhead1104.township.utils;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import lombok.experimental.UtilityClass;
import me.webhead1104.township.Township;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ClassGraphUtils {

    public static <T> List<T> getImplementedClasses(Class<T> clazz, String packageName) {
        ClassGraph graph = new ClassGraph().acceptPackages(packageName).enableAllInfo();

        List<T> resultList = new ArrayList<>();
        try (ScanResult result = graph.scan()) {
            result.getClassesImplementing(clazz).loadClasses().forEach(foundClass -> {
                try {
                    Constructor<?> constructor = foundClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    //noinspection unchecked
                    T instance = (T) constructor.newInstance();
                    resultList.add(instance);
                } catch (Exception e) {
                    Township.logger.error("An error occurred whilst getting implemented classes for class '{}'!", clazz.getName(), e);
                }
            });
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst getting implemented classes for class '{}'!", clazz.getName(), e);
        }
        return resultList;
    }

    public static <T> List<T> getExtendedClasses(Class<T> clazz, String packageName) {
        ClassGraph graph = new ClassGraph().acceptPackages(packageName).enableAllInfo();

        List<T> resultList = new ArrayList<>();
        try (ScanResult result = graph.scan()) {
            result.getSubclasses(clazz).loadClasses().forEach(foundClass -> {
                try {
                    Constructor<?> constructor = foundClass.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    //noinspection unchecked
                    T instance = (T) constructor.newInstance();
                    resultList.add(instance);
                } catch (Exception e) {
                    Township.logger.error("An error occurred whilst getting extended classes for class '{}'!", clazz.getName(), e);
                }
            });
        } catch (Exception e) {
            Township.logger.error("An error occurred whilst getting extended classes for class '{}'!", clazz.getName(), e);
        }
        return resultList;
    }
}
