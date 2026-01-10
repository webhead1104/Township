import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.provider.Provider

fun DependencyHandler.implApi(dependencyNotation: Any) {
    val resolved = when (dependencyNotation) {
        is Provider<*> -> dependencyNotation.get()
        else -> dependencyNotation
    }

    if (resolved is Iterable<*>) {
        resolved.forEach { dep ->
            add("api", dep!!)
            add("testImplementation", dep)
        }
    } else {
        add("api", resolved)
        add("testImplementation", resolved)
    }
}

fun DependencyHandler.impl(dependencyNotation: Any) {
    val resolved = when (dependencyNotation) {
        is Provider<*> -> dependencyNotation.get()
        else -> dependencyNotation
    }

    if (resolved is Iterable<*>) {
        resolved.forEach { dep ->
            add("implementation", dep!!)
            add("testImplementation", dep)
        }
    } else {
        add("implementation", resolved)
        add("testImplementation", resolved)
    }
}