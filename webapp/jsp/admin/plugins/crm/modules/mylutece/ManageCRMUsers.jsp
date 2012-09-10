<%@page import="fr.paris.lutece.portal.web.pluginaction.IPluginActionResult"%>

<jsp:useBean id="crmMylutece" scope="session" class="fr.paris.lutece.plugins.crm.modules.mylutece.web.CRMMyluteceJspBean" />

<% 
	crmMylutece.init( request, crmMylutece.RIGHT_MANAGE_MYLUTECE_USERS );
	IPluginActionResult result = crmMylutece.getManageMyLuteceUsers( request, response );
	if ( result.getRedirect(  ) != null )
	{
		response.sendRedirect( result.getRedirect(  ) );
	}
	else if ( result.getHtmlContent(  ) != null )
	{
%>
		<%@ page errorPage="../../../../ErrorPage.jsp" %>
		<jsp:include page="../../../../AdminHeader.jsp" />

		<%= result.getHtmlContent(  ) %>

		<%@ include file="../../../../AdminFooter.jsp" %>
<%
	}
%>
