package gestionannonces

import grails.converters.JSON
import grails.converters.XML

import java.text.SimpleDateFormat
import java.util.logging.Logger

class ApiController {
    Logger logger = Logger.getLogger(this.getClass().toString())
    AnnonceService annonceService
    def pattern = "dd-MM-yyyy"
    //TODO Modifier juste les illustrations

    // /api/annonce
    // /api/annonce/1/illustration

    def annonce() {
        def requestType = request.getHeader("Accept")
        switch(request.getMethod()) {
            case "GET" :
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get (params.id)
                if (!annonce)
                    return render (status: 404, text: "Aucune Annonce ne correspond à l'id : $params.id")

                response.withFormat {
                    json { render annonce as JSON }
                    xml { render annonce as XML }
                }
                break

            case "PATCH" :
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                if (!params.id)
                    return render (status: 400, text: "L'id de l'annonce est requis")
                def annonce = Annonce.get (params.id)
                if (!annonce)
                    return render (status: 404, text: "Aucune Annonce ne correspond à l'id : $params.id")

                if(body.dateCreated)
                    annonce.setDateCreated(new SimpleDateFormat(pattern).parse(body.dateCreated))
                if(body.validTill)
                    annonce.setValidTill(new SimpleDateFormat(pattern).parse(body.validTill))
                if(body.state)
                    annonce.setState(new Boolean(body.state))
                if(body.description)
                    annonce.setDescription(body.description)
                if(body.title)
                    annonce.setTitle(body.title)
                if(body.authorId) {
                    def author = User.get(body.authorId)
                    if(!author)
                        return render (status: 404, text: "Aucun user ne correspond à l'id : $body.authorId")
                    annonce.setAuthor(author)
                }

                def illustrations = body.illustrations
                if(illustrations) {
                    def filesName = getIllustrations(illustrations)
                    filesName.each { filename ->
                        annonce.addToIllustrations(new Illustration(filename: filename))
                    }
                }
                annonceService.save(annonce)

                return render (status: 200, text: "Annonce modifiée - id : $annonce.id")

            case "PUT" :
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                if (!params.id)
                    return render (status: 400, text: "L'id de l'annonce est requis")
                def annonce = Annonce.get (params.id)
                if (!annonce)
                    return render (status: 404, text: "Aucune Annonce ne correspond à l'id : $params.id")
                if(!body.title || !body.description || !body.validTill || !body.state
                        || !body.dateCreated || !body.authorId|| !body.illustrations)
                    return render (status: 400, text: "Des données maquent à la requêtes. " +
                            "Les attributs suivants sont obligatoires : titel, " +
                            "description, validTill, state, dateCreated, authorId, illustrations")
                annonce.setDateCreated(new SimpleDateFormat(pattern).parse(body.dateCreated))
                annonce.setValidTill(new SimpleDateFormat(pattern).parse(body.validTill))
                annonce.setState(new Boolean(body.state))
                annonce.setDescription(body.description)
                annonce.setTitle(body.title)
                def author = User.get(body.authorId)
                if(!author)
                    return render (status: 404, text: "Aucun User ne correspond à l'id : $body.authorId")
                annonce.setAuthor(author)

                //TODO Garder ???
                //Suppression des illustrations existantes
                annonce.illustrations.clear()

                def illustrations = body.illustrations
                if(illustrations) {
                    def filesName = getIllustrations(illustrations)
                    filesName.each { filename ->
                        annonce.addToIllustrations(new Illustration(filename: filename))
                    }
                }
                annonceService.save(annonce)

                return render (status: 200, text: "Annonce modifiée - id : $annonce.id")

            case "DELETE" :
                if (!params.id)
                    return render (status: 400, text: "L'id de l'annonce est requis")
                def annonce = Annonce.get (params.id)

                if (!annonce)
                    return response.status = 404
                annonce.delete(flush: true)
                //annonceService.save(annonce)

                return render (status: 200, text: "Annonce supprimée - id : $annonce.id")
            default :
                return response.status = 405
        }
        return response.status = 406
    }

    def annonces() {
        def requestType = request.getHeader("Accept")
        println(requestType)
        switch(request.getMethod()) {
            case "GET" :
                def annonces = Annonce.getAll()
                if (!annonces)
                    return response.status = 404
                response.withFormat {
                    json { render annonces as JSON }
                    xml { render annonces as XML }
                }
                break

            case "POST" :
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                logger.info("Request Body : $body")

                if(!body.title || !body.description || !body.validTill || !body.authorId)
                    return render (status: 400, text: "Les champs title, description et validTill sont requis")

                def author = User.get(body.authorId)
                if(!author)
                    return render (status: 404, text: "Aucun User ne correspond à l'id : $body.authorId")
                // Create instance
                def newAnnonce = new Annonce(
                        title: body.title,
                        description: body.description,
                        validTill: new SimpleDateFormat(pattern).parse(body.validTill),
                        dateCreated: new Date(),
                        state: Boolean.TRUE
                )
                newAnnonce.author = author

                def illustrations = body.illustrations
                if(illustrations) {
                    def filesName = getIllustrations(illustrations)
                    filesName.each { filename ->
                        newAnnonce.addToIllustrations(new Illustration(filename: filename))
                    }
                }
                annonceService.save(newAnnonce)
                return render (status: 201, text: "Annonce créée - id : $newAnnonce.id")
            default :
                return response.status = 405
        }
        return response.status = 406
    }

    def private getIllustrations(illustrations) {
        def filesName = new ArrayList<String>()
        illustrations.each { illustration ->
            def tmp = illustration.tokenize(',')
            def fileBase64 = tmp[1]
            def extension = tmp[0].tokenize(";")[0].tokenize("/")[1]
            logger.info("ext : " + extension)
            def destPath = "illustrations/" + "img_" + System.currentTimeMillis() + "." + extension
            File file = new File(grailsApplication.config.configChemin.assets_url + destPath)
            FileOutputStream out = new FileOutputStream(file)
            try {
                byte[] data = Base64.getDecoder().decode(fileBase64)
                println(file.getAbsolutePath())
                out.write(data)
            } catch (Exception e) {
                out.close()
                return response.status = 400
            }
            filesName.add(destPath)
        }
        return filesName
    }
}