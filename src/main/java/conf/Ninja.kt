package conf

import com.google.inject.Inject
import ninja.NinjaDefault
import ninja.template.TemplateEngineFreemarker

@Suppress("unused")
class Ninja : NinjaDefault() {

    @Inject
    lateinit var templateEngineFreemarker: TemplateEngineFreemarker

    override fun onFrameworkStart() {

        templateEngineFreemarker.configuration.apply {
            numberFormat = "number"
        }

        super.onFrameworkStart()
    }
}