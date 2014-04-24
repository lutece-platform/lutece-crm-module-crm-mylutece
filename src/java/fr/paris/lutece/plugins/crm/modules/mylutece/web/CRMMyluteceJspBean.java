/*
 * Copyright (c) 2002-2014, Mairie de Paris
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *  1. Redistributions of source code must retain the above copyright notice
 *     and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright notice
 *     and the following disclaimer in the documentation and/or other materials
 *     provided with the distribution.
 *
 *  3. Neither the name of 'Mairie de Paris' nor 'Lutece' nor the names of its
 *     contributors may be used to endorse or promote products derived from
 *     this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 * License 1.0
 */
package fr.paris.lutece.plugins.crm.modules.mylutece.web;

import fr.paris.lutece.plugins.crm.business.user.CRMUser;
import fr.paris.lutece.plugins.crm.modules.mylutece.service.MyLuteceUserManager;
import fr.paris.lutece.plugins.crm.service.security.CRMUserAnonymizationService;
import fr.paris.lutece.plugins.crm.service.security.IAnonymizationService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserAttributesService;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.message.AdminMessage;
import fr.paris.lutece.portal.service.message.AdminMessageService;
import fr.paris.lutece.portal.service.spring.SpringContextService;
import fr.paris.lutece.portal.service.template.AppTemplateService;
import fr.paris.lutece.portal.service.util.AppPathService;
import fr.paris.lutece.portal.web.admin.PluginAdminPageJspBean;
import fr.paris.lutece.portal.web.constants.Messages;
import fr.paris.lutece.portal.web.pluginaction.DefaultPluginActionResult;
import fr.paris.lutece.portal.web.pluginaction.IPluginActionResult;
import fr.paris.lutece.util.html.HtmlTemplate;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 *
 * CRMMyluteceJspBean
 *
 */
public class CRMMyluteceJspBean extends PluginAdminPageJspBean
{
    /** The Constant RIGHT_MANAGE_MYLUTECE_USERS. */
    public static final String RIGHT_MANAGE_MYLUTECE_USERS = "CRM_MYLUTECE_MANAGEMENT";

    // PARAMETERS
    private static final String PARAMETER_ID_CRM_USER = "idCRMUser";
    private static final String PARAMETER_SESSION = "session";

    // MARKS
    private static final String MARK_LIST_ATTRIBUTE_KEYS = "listAttributeKeys";

    // PROPERTIES
    private static final String PROPERTY_MANAGE_CRM_USERS_PAGE_TITLE = "module.crm.mylutece.manage_crm_users.pageTitle";

    // MESSAGES
    private static final String MESSAGE_CONFIRM_REMOVE_CRM_USER = "module.crm.mylutece.message.confirm.removeCRMUser";
    private static final String MESSAGE_CONFIRM_ANONYMIZE = "module.crm.mylutece.message.confirm.anonymize";
    private static final String MESSAGE_CONFIRM_REINIT_PASSWORD = "module.crm.mylutece.message.confirm.reinitPassword";

    // TEMPLATES
    private static final String TEMPLATE_MANAGE_CRM_USERS = "admin/plugins/crm/modules/mylutece/manage_crm_users.html";

    // JSP
    private static final String JSP_URL_MANAGE_USERS = "jsp/admin/plugins/crm/modules/mylutece/ManageCRMUsers.jsp";
    private static final String JSP_URL_REMOVE_CRM_USER = "jsp/admin/plugins/crm/modules/mylutece/DoRemoveCRMUser.jsp";
    private static final String JSP_URL_ANONYMIZE_CRM_USER = "jsp/admin/plugins/crm/modules/mylutece/DoAnonymizeCRMUser.jsp";
    private static final String JSP_URL_REINIT_PASSWORD = "jsp/admin/plugins/crm/modules/mylutece/DoReinitPassword.jsp";

    // VARIABLES
    private CRMMyLuteceSearchFields _userSearchFields = new CRMMyLuteceSearchFields(  );
    private CRMUserService _crmUserService = CRMUserService.getService(  );
    private CRMUserAttributesService _crmUserAttributesService = CRMUserAttributesService.getService(  );
    private IAnonymizationService _anonymizationService = SpringContextService.getBean( CRMUserAnonymizationService.BEAN_SERVICE );

    /**
     * Gets the manage my lutece users.
     *
     * @param request the request
     * @param response the response
     * @return the manage my lutece users
     * @throws AccessDeniedException the access denied exception
     */
    public IPluginActionResult getManageMyLuteceUsers( HttpServletRequest request, HttpServletResponse response )
        throws AccessDeniedException
    {
        setPageTitleProperty( PROPERTY_MANAGE_CRM_USERS_PAGE_TITLE );

        Map<String, Object> model = new HashMap<String, Object>(  );
        _userSearchFields.fillModel( getUrlManageUsers( request, true ).getUrl(  ), request, model );
        model.put( MARK_LIST_ATTRIBUTE_KEYS, _crmUserAttributesService.getUserAttributeKeys(  ) );

        HtmlTemplate template = AppTemplateService.getTemplate( TEMPLATE_MANAGE_CRM_USERS, request.getLocale(  ), model );

        IPluginActionResult result = new DefaultPluginActionResult(  );

        result.setHtmlContent( getAdminPage( template.getHtml(  ) ) );

        return result;
    }

    /**
     * Gets the confirm remove crm user.
     *
     * @param request the request
     * @return the confirm remove crm user
     */
    public String getConfirmRemoveCRMUser( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_REMOVE_CRM_USER );
        url.addParameter( PARAMETER_ID_CRM_USER, strIdCRMUser );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REMOVE_CRM_USER, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the confirm anonymize crm user.
     *
     * @param request the request
     * @return the confirm anonymize crm user
     */
    public String getConfirmAnonymizeCRMUser( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_ANONYMIZE_CRM_USER );
        url.addParameter( PARAMETER_ID_CRM_USER, strIdCRMUser );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_ANONYMIZE, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Gets the confirm reinit password.
     *
     * @param request the request
     * @return the confirm reinit password
     */
    public String getConfirmReinitPassword( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        UrlItem url = new UrlItem( JSP_URL_REINIT_PASSWORD );
        url.addParameter( PARAMETER_ID_CRM_USER, strIdCRMUser );

        return AdminMessageService.getMessageUrl( request, MESSAGE_CONFIRM_REINIT_PASSWORD, url.getUrl(  ),
            AdminMessage.TYPE_CONFIRMATION );
    }

    /**
     * Do remove crm user.
     *
     * @param request the request
     * @return the string
     */
    public String doRemoveCRMUser( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdCRMUser = Integer.parseInt( strIdCRMUser );
        CRMUser user = _crmUserService.findByPrimaryKey( nIdCRMUser );

        if ( user != null )
        {
            _crmUserService.remove( nIdCRMUser );
            MyLuteceUserManager.doRemoveMyLuteceUser( user.getUserGuid(  ), request, getLocale(  ) );
        }

        return getUrlManageUsers( request, true ).getUrl(  );
    }

    /**
     * Do anonymize crm user.
     *
     * @param request the request
     * @return the string
     */
    public String doAnonymizeCRMUser( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdCRMUser = Integer.parseInt( strIdCRMUser );
        CRMUser user = _crmUserService.findByPrimaryKey( nIdCRMUser );

        if ( user != null )
        {
            _anonymizationService.anonymizeUser( user.getIdCRMUser(  ), getLocale(  ) );
            MyLuteceUserManager.doAnonymizeMyLuteceUser( user.getUserGuid(  ), request, getLocale(  ) );
        }

        return getUrlManageUsers( request, true ).getUrl(  );
    }

    /**
     * Do reinit password.
     *
     * @param request the request
     * @return the string
     */
    public String doReinitPassword( HttpServletRequest request )
    {
        String strIdCRMUser = request.getParameter( PARAMETER_ID_CRM_USER );

        if ( StringUtils.isBlank( strIdCRMUser ) || !StringUtils.isNumeric( strIdCRMUser ) )
        {
            return AdminMessageService.getMessageUrl( request, Messages.MANDATORY_FIELDS, AdminMessage.TYPE_STOP );
        }

        int nIdCRMUser = Integer.parseInt( strIdCRMUser );
        CRMUser user = _crmUserService.findByPrimaryKey( nIdCRMUser );

        if ( user != null )
        {
            MyLuteceUserManager.doReinitPassword( user.getUserGuid(  ), request, getLocale(  ) );
        }

        return getUrlManageUsers( request, true ).getUrl(  );
    }

    // PRIVATE METHODS

    /**
     * Gets the url manage users.
     *
     * @param request the request
     * @param bSession the b session
     * @return the url manage users
     */
    private UrlItem getUrlManageUsers( HttpServletRequest request, boolean bSession )
    {
        UrlItem url = new UrlItem( AppPathService.getBaseUrl( request ) + JSP_URL_MANAGE_USERS );

        if ( bSession )
        {
            url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );
        }

        return url;
    }
}
