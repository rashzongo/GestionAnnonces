<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'annonce.label', default: 'Annonce')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
        <a href="#edit-annonce" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
        <div class="nav" role="navigation">
            <ul>
                <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
                <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
                <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
            </ul>
        </div>
        <div id="edit-annonce" class="content scaffold-edit" role="main">
            <h1><g:message code="default.edit.label" args="[entityName]" /></h1>
            <g:if test="${flash.message}">
            <div class="message" role="status">${flash.message}</div>
            </g:if>
            <g:hasErrors bean="${this.annonce}">
            <ul class="errors" role="alert">
                <g:eachError bean="${this.annonce}" var="error">
                <li <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message error="${error}"/></li>
                </g:eachError>
            </ul>
            </g:hasErrors>
            <g:form enctype="multipart/form-data" resource="${this.annonce}" method="POST">
%{--                <form action="save" method="PUT" enctype="multipart/form-data">--}%
                <g:hiddenField name="version" value="${this.annonce?.version}" />
                %{--<fieldset class="form">
                    <f:all bean="annonce"/>
                </fieldset>--}%


                <fieldset class="form">
                    <f:field bean="annonce" property="title">
                        <f:input bean="annonce" property="title"></f:input>
                    </f:field>
                    <f:field bean="annonce" property="description">
                        <f:input bean="annonce" property="description"></f:input>
                    </f:field>
                    <f:field bean="annonce" property="validTill">
                        <f:input bean="annonce" property="validTill"></f:input>
                    </f:field>
                    <f:field bean="annonce" property="illustrations">
                        <g:each in="${this.annonce.illustrations}" var="illus">
                            <span>
                                <g:link title="Supprimer l'image" controller="annonce" action="deleteIllustration" id="${illus.id}" params="[idAnnonce: annonce.id]">
                                    <img width="50" height="50" src="/assets/${illus.filename}" />
                                </g:link>

                            </span>
                        </g:each>
                        <input id="thumbnailFile" name="fileIllustrations" type="file" multiple />
                    </f:field>
                    <div id="illustrationsPreview"></div>
                    <f:field property="state">
                        <f:input bean="annonce" property="state"></f:input>
                    </f:field>

                    <f:field property="author">
                        <select name="author">
                            <g:each in="${gestionannonces.User.all}" var="i">
                                <option value="${i.id}">${i.username}</option>
                            </g:each>
                        </select>
                    </f:field>

                </fieldset>



                <fieldset class="buttons">
                    <input class="save" type="submit" value="${message(code: 'default.button.update.label', default: 'Update')}" />
                </fieldset>
            </g:form>
        </div>
    </body>
</html>
