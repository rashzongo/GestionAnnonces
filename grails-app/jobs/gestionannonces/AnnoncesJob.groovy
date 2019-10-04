package gestionannonces

class AnnoncesJob {

    AnnonceService annonceService

    static triggers = {
      simple repeatInterval: 15000l // execute job once in 5 seconds
    }
    def execute() {
        println("Executing Job...")
        def annonces = Annonce.getAll()

        annonces.each{annonce ->
           if(annonce.state == true && annonce.validTill.before(new Date())){
               println("Delete an annonce $annonce.id")
               annonce.state = false
               annonceService.save(annonce)
           }
        }
    }
}
