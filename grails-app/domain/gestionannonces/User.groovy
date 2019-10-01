package gestionannonces

class User {
    String username
    String password
    Date dateCreated
    Date lastUpdated
    Illustration thumbnail

    static hasMany = [annonces: Annonce]

    static constraints = {
        username nullable: false, blank: false, size: 5..20
        password password:true, nullable: false, blank: false, size: 8..30
        thumbnail nullable: false
        annonces nullable: true
    }
}
