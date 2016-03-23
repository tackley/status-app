package conf


import controllers.ApplicationController
import ninja.AssetsController
import ninja.Router
import ninja.application.ApplicationRoutes


@Suppress("unused")
class Routes : ApplicationRoutes {

    override fun init(router: Router) {

        router.apply {
            GET().route("/").with(ApplicationController::class.java, "index")

            GET().route("/{stage}").with(ApplicationController::class.java, "stage")

            ///////////////////////////////////////////////////////////////////////
            // Assets (pictures / javascript)
            ///////////////////////////////////////////////////////////////////////
            GET().route("/assets/webjars/{fileName: .*}").with(AssetsController::class.java, "serveWebJars")
            GET().route("/assets/{fileName: .*}").with(AssetsController::class.java, "serveStatic")

        }

        ///////////////////////////////////////////////////////////////////////
        // Index / Catchall shows index page
        ///////////////////////////////////////////////////////////////////////
        //router.GET().route("/.*").with(ApplicationController::class.java, "index")
    }

}
