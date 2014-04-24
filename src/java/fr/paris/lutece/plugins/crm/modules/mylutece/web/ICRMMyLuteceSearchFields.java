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

import fr.paris.lutece.portal.service.admin.AccessDeniedException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;


/**
 *
 * IResourceExtenderSearchFields
 *
 */
public interface ICRMMyLuteceSearchFields
{
    /**
    * Get the items per page
    * @return the items per page
    */
    int getItemsPerPage(  );

    /**
     * Set the items per page
     * @param nItemsPerPage the items per page
     */
    void setItemsPerPage( int nItemsPerPage );

    /**
    * Default items per page
    * @return default items per page
    */
    int getDefaultItemsPerPage(  );

    /**
     * Default items per page
     * @param nDefaultItemsPerPage default items per page
     */
    void setDefaultItemsPerPage( int nDefaultItemsPerPage );

    /**
     * Current page index
     * @return current page index
     */
    String getCurrentPageIndex(  );

    /**
     * Current page index
     * @param strCurrentPageIndex current page index
     */
    void setCurrentPageIndex( String strCurrentPageIndex );

    /**
     * Fill model.
     *
     * @param strBaseUrl the str base url
     * @param request the request
     * @param model the model
     * @throws AccessDeniedException the access denied exception
     */
    void fillModel( String strBaseUrl, HttpServletRequest request, Map<String, Object> model )
        throws AccessDeniedException;

    /**
     * Inits the filter.
     *
     * @param request the request
     */
    void initFilter( HttpServletRequest request );
}
