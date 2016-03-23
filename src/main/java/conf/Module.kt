package conf

import com.google.inject.AbstractModule
import com.google.inject.Inject
import com.google.inject.Singleton
import ninja.template.TemplateEngineFreemarker

@Singleton
class Module(): AbstractModule() {

    override fun configure() {

        println("configure running!")
        // bind your injections here!

    }

}
