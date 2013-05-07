/*
 * Copyright (c) 2002-2013, Mairie de Paris
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
import fr.paris.lutece.plugins.crm.business.user.CRMUserFilter;
import fr.paris.lutece.plugins.crm.service.user.CRMUserService;
import fr.paris.lutece.portal.service.admin.AccessDeniedException;
import fr.paris.lutece.portal.service.util.AppPropertiesService;
import fr.paris.lutece.portal.web.util.LocalizedPaginator;
import fr.paris.lutece.util.html.Paginator;
import fr.paris.lutece.util.url.UrlItem;

import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * ResourceExtenderSearchFields
 *
 */
public class CRMMyLuteceSearchFields implements ICRMMyLuteceSearchFields, Serializable
{
    private static final long serialVersionUID = 5171135962785175642L;

    // PROPERTIES
    private static final String PROPERTY_DEFAULT_LIST_USERS_PER_PAGE = "module.crm.mylutece.listUsers.itemsPerPage";

    // PARAMETERS
    private static final String PARAMETER_SESSION = "session";

    // MARKS
    private static final String MARK_LIST_CRM_USERS = "listCRMUsers";
    private static final String MARK_FILTER = "filter";
    private static final String MARK_PAGINATOR = "paginator";
    private static final String MARK_NB_ITEMS_PER_PAGE = "nb_items_per_page";

    // VARIABLES
    private int _nItemsPerPage;
    private int _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_USERS_PER_PAGE, 50 );
    private String _strCurrentPageIndex;
    private CRMUserFilter _filter;

    /**
     * {@inheritDoc}
     */
    @Override
    public String getCurrentPageIndex(  )
    {
        return _strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDefaultItemsPerPage(  )
    {
        return _nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setCurrentPageIndex( String strCurrentPageIndex )
    {
        _strCurrentPageIndex = strCurrentPageIndex;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setDefaultItemsPerPage( int nDefaultItemsPerPage )
    {
        _nDefaultItemsPerPage = nDefaultItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getItemsPerPage(  )
    {
        return _nItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setItemsPerPage( int nItemsPerPage )
    {
        _nItemsPerPage = nItemsPerPage;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void fillModel( String strBaseUrl, HttpServletRequest request, Map<String, Object> model )
        throws AccessDeniedException
    {
        initFilter( request );

        // SORT
        UrlItem url = new UrlItem( strBaseUrl );
        url.addParameter( PARAMETER_SESSION, PARAMETER_SESSION );

        // PAGINATOR
        _strCurrentPageIndex = Paginator.getPageIndex( request, Paginator.PARAMETER_PAGE_INDEX, _strCurrentPageIndex );
        _nDefaultItemsPerPage = AppPropertiesService.getPropertyInt( PROPERTY_DEFAULT_LIST_USERS_PER_PAGE, 50 );
        _nItemsPerPage = Paginator.getItemsPerPage( request, Paginator.PARAMETER_ITEMS_PER_PAGE, _nItemsPerPage,
                _nDefaultItemsPerPage );

        CRMUserService crmUserService = CRMUserService.getService(  );
        List<Integer> listIdsCRMUser = crmUserService.findListIdsByFilter( _filter );

        LocalizedPaginator<Integer> paginator = new LocalizedPaginator<Integer>( listIdsCRMUser, getItemsPerPage(  ),
                url.getUrl(  ), Paginator.PARAMETER_PAGE_INDEX, getCurrentPageIndex(  ), request.getLocale(  ) );

        List<CRMUser> listUsers = crmUserService.findByListIds( paginator.getPageItems(  ) );

        model.put( MARK_LIST_CRM_USERS, listUsers );
        model.put( MARK_FILTER, _filter );
        model.put( MARK_PAGINATOR, paginator );
        model.put( MARK_NB_ITEMS_PER_PAGE, Integer.toString( paginator.getItemsPerPage(  ) ) );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void initFilter( HttpServletRequest request )
    {
        if ( StringUtils.isBlank( request.getParameter( PARAMETER_SESSION ) ) || ( _filter == null ) )
        {
            _filter = new CRMUserFilter(  );
            _filter.init( request );
        }
    }
}
