package gestionannonces

import grails.validation.ValidationException
import static org.springframework.http.HttpStatus.*

class AnnonceController {

    AnnonceService annonceService
    IllustrationService illustrationService

    def index(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        respond annonceService.list(params), model:[annonceCount: annonceService.count()]
    }

    def show(Long id) {
        respond annonceService.get(id)
    }

    def create() {
        respond new Annonce(params)
    }

    def save(Annonce annonce) {
        println("SAVE")
        /*def file = request.getFile('illustrations')
        file.transferTo(new File(grailsApplication.config.maconfig.assets_path+'image.png'))
        annonce.addToIllustrations(new Illustration(filename: 'image.png'))
        print(file)*/
        //Générer un nom de fichier aléatoire et vérifier qu'il n'existe pas
        //Sauvegarder le fichier

        if (annonce == null) {
            notFound()
            return
        }

        try {
            def file = request.getFiles('fileIllustrations')
            file.properties.each { println "$it.key -> $it.value" }
            //print(file.empty)
            file.each{
                println("fileoriginal: "+it.originalFilename)
                if(it.originalFilename){
                    def name = new Date()
                    name = name.getTime()
                    it.transferTo(new File(grailsApplication.config.configChemin.assets_url+"illustrations/"+name+it.originalFilename))
                    annonce.addToIllustrations(new Illustration(filename: "illustrations/"+name+it.originalFilename))
                }
            }
            annonceService.save(annonce)
        } catch (ValidationException e) {
            respond annonce.errors, view:'create'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.created.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*' { respond annonce, [status: CREATED] }
        }
    }

    def edit(Long id) {
        respond annonceService.get(id)
    }

    def update(Annonce annonce) {
        if (annonce == null) {
            notFound()
            return
        }
        try {
            annonceService.save(annonce)
        } catch (ValidationException e) {
            respond annonce.errors, view:'edit'
            return
        }

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.updated.message', args: [message(code: 'annonce.label', default: 'Annonce'), annonce.id])
                redirect annonce
            }
            '*'{ respond annonce, [status: OK] }
        }
    }

    def delete(Long id) {
        if (id == null) {
            notFound()
            return
        }

        annonceService.delete(id)

        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.deleted.message', args: [message(code: 'annonce.label', default: 'Annonce'), id])
                redirect action:"index", method:"GET"
            }
            '*'{ render status: NO_CONTENT }
        }
    }

    def deleteIllustration(Long id){
        def idAnnonce = params.idAnnonce
        if (id == null || idAnnonce == null) {
            notFound()
            return
        }
        def annonceInstance = annonceService.get(idAnnonce)
        def illustrationInstance = illustrationService.get(id)

        annonceInstance.removeFromIllustrations(illustrationInstance)
        annonceInstance.save(flush: true)
        illustrationInstance.delete(flush: true)

        println(idAnnonce)

        redirect (controller:"annonce", action:"edit", id: idAnnonce)

    }

    protected void notFound() {
        request.withFormat {
            form multipartForm {
                flash.message = message(code: 'default.not.found.message', args: [message(code: 'annonce.label', default: 'Annonce'), params.id])
                redirect action: "index", method: "GET"
            }
            '*'{ render status: NOT_FOUND }
        }
    }
}
