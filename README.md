### Projet Grails-REST / MBDS 2019-2020 : Plateforme de gestion d'Annonces

### Etudiants : 
- BAH Mamadou Saliou
- ZONGO S. H. Rachida


### Implémentations :

- CRUD (avec IHMs) des resoources User et Annonce

- Api RESTful sur la ressource Annonce :
        (formats pris en charge : json et xml)
        
    | Points d'entrée | description | Champs obligatoires (body) |
    | ------ | ------ | ------ |
    | GET /api/annonces/| Liste toutes les annonces de la plateforme | Aucun
    | POST /api/annonces/ | Ajoute une nouvelle annonce | title, description, validTill, authorId
    | GET /api/annonce/{id} | Recupère l'annonce associée à {id} | Aucun
    | PATCH /api/annonce/{id} | Modifie l'annonce associée à {id} | Aucun
    | PUT /api/annonce/{id} | Remplace l'annonce associée à {id} | Tous : title, description, validTill, authorId, state, dateCreated, illustrations
    | DELETE /api/annonce/{id} | Supprime l'annonce associée à {id} | Aucun

 **Les illustrations sont encodées en base64, l'outil [Base64 Image](https://www.base64-image.de) peut être utlisé
