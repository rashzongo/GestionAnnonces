<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="main" />
    <g:set var="entityName" value="${message(code: 'annonce.label', default: 'Annonce')}" />
    <title><g:message code="default.show.label" args="[entityName]" /></title>
</head>
<body>
<a href="#show-annonce" class="skip" tabindex="-1"><g:message code="default.link.skip.label" default="Skip to content&hellip;"/></a>
<div class="nav" role="navigation">
    <ul>
        <li><a class="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
        <li><g:link class="list" action="index"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
        <li><g:link class="create" action="create"><g:message code="default.new.label" args="[entityName]" /></g:link></li>
    </ul>
</div>
<div id="show-annonce" class="content scaffold-show" role="main">
    <h1><g:message code="default.show.label" args="[entityName]" /></h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">${flash.message}</div>
    </g:if>
%{--            <f:display bean="annonce" />--}%

    <table bean="annonce">
        <thead>
        <tr>
            <th class="sortable"><a href="/annonce/index?sort=title&amp;max=10&amp;order=asc">Title</a></th>
            <th class="sortable"><a href="/annonce/index?sort=description&amp;max=10&amp;order=asc">Description</a></th>
            <th class="sortable"><a href="/annonce/index?sort=validTill&amp;max=10&amp;order=asc">Valid Till</a></th>
            <th class="sortable"><a href="/annonce/index?sort=illustrations&amp;max=10&amp;order=asc">Nombre d'illustrations</a></th>
            <th class="sortable"><a href="/annonce/index?sort=state&amp;max=10&amp;order=asc">State</a></th>
            <th class="sortable"><a href="/annonce/index?sort=author&amp;max=10&amp;order=asc">Author</a></th>
        </tr>
        </thead>
        %{--                <g:each in="${annonceList}" var="instance">--}%
        <tr>
            <td>${annonce.title}</td>
            <td>${annonce.description}</td>
            <td>${annonce.validTill}</td>
            <td>
                <nav>
                    <ul>
                        <g:each in="${annonce.illustrations}" var="illustration">
                            <li><img width="100" height="100" src="/assets/${illustration.filename}"></li>
                        </g:each>
                    </ul>
                </nav>
            </td>
            <td>${annonce.state}</td>
            <td><g:link controller="user" action="show" id="${annonce.author.id}">${annonce.author.username}</g:link></td>
        </tr>
        %{--                </g:each>--}%
    </table>


    <g:form resource="${this.annonce}" method="DELETE">
        <fieldset class="buttons">
            <g:link class="edit" action="edit" resource="${this.annonce}"><g:message code="default.button.edit.label" default="Edit" /></g:link>
            <input class="delete" type="submit" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
        </fieldset>
    </g:form>
</div>
</body>
</html>
