package me.webhead1104.township.annotations;

import me.webhead1104.township.dataLoaders.DataLoader;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface DependsOn {

    Class<? extends DataLoader>[] value() default {};
}
