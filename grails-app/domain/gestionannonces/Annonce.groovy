package gestionannonces

class Annonce {

    String title
    String description
    Date dateCreated
    Date validTill
    Boolean state = Boolean.FALSE

    static belongsTo = [author: User]

    static  hasMany = [illustrations: Illustration]

    static constraints = {
        title blank: false, nullable: false
        description blank: false, nullable: false
        validTill nullable: false
        illustrations nullable: true
    }
}
