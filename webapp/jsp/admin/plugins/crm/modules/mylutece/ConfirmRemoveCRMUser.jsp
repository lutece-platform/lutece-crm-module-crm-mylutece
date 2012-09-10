<%@ page errorPage="../../ErrorPage.jsp" %>
<jsp:useBean id="crmMylutece" scope="session" class="fr.paris.lutece.plugins.crm.modules.mylutece.web.CRMMyluteceJspBean" />
<% 
	crmMylutece.init( request, crmMylutece.RIGHT_MANAGE_MYLUTECE_USERS );
 	response.sendRedirect( crmMylutece.getConfirmRemoveCRMUser( request ) );
%>
