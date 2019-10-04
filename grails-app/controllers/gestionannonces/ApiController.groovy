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
                    return response.status = 404

                return response.withFormat {
                    json { render annonce as JSON }
                    xml { render annonce as XML }
                }

            case "PATCH" :
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get (params.id)
                if (!annonce)
                    return response.status = 404

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

                def illustrations = body.illustrations
                if(illustrations) {
                    def filesName = getIllustrations(illustrations)
                    filesName.each { filename ->
                        annonce.addToIllustrations(new Illustration(filename: filename))
                    }
                }

                annonceService.save(annonce)

                return response.status = 200

            case "PUT" :
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get (params.id)
                if (!annonce)
                    return response.status = 404
                if(!body.title || !body.description || !body.validTill || !body.state || !body.dateCreated )
                    return response.status = 400
                annonce.setDateCreated(new SimpleDateFormat(pattern).parse(body.dateCreated))
                annonce.setValidTill(new SimpleDateFormat(pattern).parse(body.validTill))
                annonce.setState(new Boolean(body.state))
                annonce.setDescription(body.description)
                annonce.setTitle(body.title)
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

                return response.status = 200

            case "DELETE" :
                if (!params.id)
                    return response.status = 400
                def annonce = Annonce.get (params.id)

                if (!annonce)
                    return response.status = 404
                annonce.delete(flush: true)
                //annonceService.save(annonce)

                return response.status = 200
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
                return response.withFormat {
                    json { render annonces as JSON }
                    xml { render annonces as XML }
                }

            case "POST" :
                //println("**********" + request.JSON)
                //println("**********" + params)
                def body = {}
                if(requestType.contains("json")) {
                    body = request.JSON
                }
                if(requestType.contains("xml")) {
                    body = request.XML
                }
                logger.info("Request Body : $body")

                if(!body.title || !body.description || !body.validTill || !body.authorId)
                    return response.status = 400

                def author = User.get(body.authorId)
                if(!author)
                    return response.status = 400
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
                return response.status = 201
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