package gestionannonces

class BootStrap {

    def init = { servletContext ->
        def userInstance = new User(username: "username",
                password: "password",
                thumbnail: new Illustration(filename: "/assets/apple-touch-icon.png"))
                .save(flush:true, failOnError:true)
        (1..5).each {

            def annonceInstance = new Annonce(
                    title: "title $it",
                    description: "description $it",
                    validTill: new Date(),
                    state: Boolean.TRUE
            )
                    .addToIllustrations(new Illustration(filename: "apple-touch-icon.png"))
                    .addToIllustrations(new Illustration(filename: "apple-touch-icon-retina.png"))
                    .addToIllustrations(new Illustration(filename: "spinner.gif"))
            userInstance.addToAnnonces(annonceInstance)


        }
        userInstance.save(flush:true, failOnError:true)
    }
    def destroy = {
    }
}
