<#import "parts/common.ftlh" as c>

<@c.page>
    User edit
    <div>
        <form action="/user" method="post">
            <input type="hidden" name="id" value="${user.id}"/>
            <input type="hidden" name="_csrf" value="${_csrf.token}"/>
            <input type="text" name="username" value="${user.username}"/>
            <#list roles as role>
                <label><input type="checkbox" name="${role}" ${user.roles?seq_contains(role)?string("checked", "")}/>${role}</label>
            </#list>
            <button type="submit">Save</button>
        </form>
    </div>
</@c.page>