/*
    STACK360 - Web-based Business Management System
    Copyright (C) 2024 Arahant LLC

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see https://www.gnu.org/licenses.
*/

/*
*/


/**
 * 
 */
package com.arahant.services.standard.tutorial.tutorial13;
import com.arahant.services.ServiceBase;
import com.arahant.utils.ArahantLogger;
import com.arahant.business.*;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.jws.soap.SOAPBinding.ParameterStyle;
import javax.jws.soap.SOAPBinding.Style;
import javax.jws.soap.SOAPBinding.Use;
import javax.jws.WebParam;
import org.jvnet.jax_ws_commons.thread_scope.ThreadScope;


/**
 * 
 *
 *
 */
@WebService(targetNamespace="http://operations.arahant.com",serviceName="StandardTutorialTutorial13Ops")
@SOAPBinding(style = Style.DOCUMENT, use = Use.LITERAL, parameterStyle = ParameterStyle.WRAPPED) 
@ThreadScope()
public class Tutorial13Ops extends ServiceBase {

	private static final transient ArahantLogger logger = new ArahantLogger(
			Tutorial13Ops.class);
	
	public Tutorial13Ops() {
		super();
	}
	

	@WebMethod()
	public ListQuotesReturn listQuotes(/*@WebParam(name = "in")*/final ListQuotesInput in)	{
		final ListQuotesReturn ret=new ListQuotesReturn();
		try
		{
			checkLogin(in);
			
			if (in.getShowId().equals("1"))
			{
				final ListQuotesItem []ar=new ListQuotesItem[4];
				ar[0]=new ListQuotesItem();
				ar[0].setCharacterFirstName("Jerry");
				ar[0].setCharacterLastName("Seinfeld");
				ar[0].setQuote("She's got man hands!");
				ar[1]=new ListQuotesItem();
				ar[1].setCharacterFirstName("George");
				ar[1].setCharacterLastName("Costanza");
				ar[1].setQuote("Hi, my name is George, I'm unemployed and I live with my parents.");
				ar[2]=new ListQuotesItem();
				ar[2].setCharacterFirstName("Cosmo");
				ar[2].setCharacterLastName("Kramer");
				ar[2].setQuote("It's a Festivus miracle.");
				ar[3]=new ListQuotesItem();
				ar[3].setCharacterFirstName("Sam");
				ar[3].setCharacterLastName("Putty");
				ar[3].setQuote("Feels like an Arby's night.");
				ret.setItem(ar);
			}
			if (in.getShowId().equals("2"))
			{
				final ListQuotesItem []ar=new ListQuotesItem[2];
				ar[0]=new ListQuotesItem();
				ar[0].setCharacterFirstName("Bruce");
				ar[0].setCharacterLastName("Dickinson");
				ar[0].setQuote("I gotta have more Cowbell!");
				ar[1]=new ListQuotesItem();
				ar[1].setCharacterFirstName("Barry");
				ar[1].setCharacterLastName("Gibb");
				ar[1].setQuote("Talking bout, chest hair, and crazy cool medallions.");
				
				ret.setItem(ar);
			}
			if (in.getShowId().equals("3"))
			{
				final ListQuotesItem []ar=new ListQuotesItem[4];
				ar[0]=new ListQuotesItem();
				ar[0].setCharacterFirstName("Al");
				ar[0].setCharacterLastName("Bundy");
				ar[0].setQuote("Feed me, or feed me to something. I just want to be part of the food chain");
				ar[1]=new ListQuotesItem();
				ar[1].setCharacterFirstName("Peggy");
				ar[1].setCharacterLastName("Bundy");
				ar[1].setQuote("Al just had it in his head that the cop was an impersonator. I'm sorry I put that in your head, Al.");
				ar[2]=new ListQuotesItem();
				ar[2].setCharacterFirstName("Al");
				ar[2].setCharacterLastName("Bundy");
				ar[2].setQuote("Why doesn't Willie Nelson hold a benefit for me? He could call it AlAid");
				ar[3]=new ListQuotesItem();
				ar[3].setCharacterFirstName("Kelly");
				ar[3].setCharacterLastName("Bundy");
				ar[3].setQuote("Eighty-nine bottles of beer on the wall, eighty-nine bottles of beer, if one of those bottles should happen to fall... eighty-ten bottles of beer on the wall.");
				ret.setItem(ar);
			}

		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}

	@WebMethod()
	public ListQuotableShowsReturn listQuotableShows(/*@WebParam(name = "in")*/final ListQuotableShowsInput in)	{
		final ListQuotableShowsReturn ret=new ListQuotableShowsReturn();
		try
		{
			checkLogin(in);
			
			final ListQuotableShowsItem []ar=new ListQuotableShowsItem[3];
			ar[0]=new ListQuotableShowsItem();
			ar[0].setId("1");
			ar[0].setName("Seinfeld");
			ar[1]=new ListQuotableShowsItem();
			ar[1].setId("2");
			ar[1].setName("SNL");
			ar[2]=new ListQuotableShowsItem();
			ar[2].setId("3");
			ar[2].setName("Married With Children");
			
			ret.setItem(ar);
		}
		catch (final Exception e)
		{
			handleError(hsu, e, ret, logger);
		}
		return ret;
	}
}
