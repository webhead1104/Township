package me.webhead1104.tools.wikiScraper.annotations;

import me.webhead1104.tools.wikiScraper.core.Scraper;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares that a scraper depends on other scrapers. Those dependencies will be
 * executed first and their results will be made available to the dependent scraper.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DependsOn {
    Class<? extends Scraper<?>>[] value();
}
