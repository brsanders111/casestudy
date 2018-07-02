/**
 /**
 * Servlet for capturing Policy information
 * 
 * @author Cognizant
 * @contact Cognizant
 * @version 1.0
 */
package com.cts.insurance.homequote.servlet;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.cts.insurance.homequote.bo.PolicyBO;
import com.cts.insurance.homequote.util.HomeInsuranceConstants;

public class PolicyServlet extends HttpServlet{
	
	/**
	 * Serial version ID
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Executes the action and returns the chosen renderer
	 *
	 * @param request Http Request
	 * @param response Http Response
	 * @return renderer chosen based on the result of saving a product
	 * @throws Throwable if exception occurs while saving the product
	 */
	public void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,IOException {
		final Logger logger = Logger.getLogger(this.getClass().getName());
		try {
			//Get Request Parameters
			String forwardPage = null;
			final String action =  request.getParameter("action");
			final PolicyBO policyBO = new PolicyBO();
			if(action == null || !action.equals("change"))
			{
				final HttpSession session = request.getSession();
				final String policyEffDate = request.getParameter("policyEffDate");
				DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				DateFormat tsdf = new SimpleDateFormat("MM/dd/yyyy");
				Date date = sdf.parse(policyEffDate);
				String formattedDate = tsdf.format(date);
				Date fixedDate = tsdf.parse(formattedDate);
				Date todaysD = new Date();
				Date maxDate = new Date();
				todaysD.setTime(0);
				todaysD.setHours(0);
				todaysD.setMinutes(0);
				todaysD.setSeconds(0);
				maxDate.setDate(maxDate.getDate()+60);
				
				if(!(fixedDate.after(todaysD) && fixedDate.before(maxDate) || fixedDate.equals(todaysD) || fixedDate.equals(maxDate))) {
					throw new Exception();
				}
				
				final int quoteId = ((Integer)session.getAttribute("quoteId")).intValue();
				request.setAttribute("policy", policyBO.savePolicy(quoteId, policyEffDate, 1));
				forwardPage = HomeInsuranceConstants.CONFIRM;	
			}
			else
			{
				final String actionSelected =  request.getParameter("actionSelected");
				final String policyKey =  request.getParameter("policySelected");
				if("Renew".equals(actionSelected))
				{
					request.setAttribute("successMsg", "Policy Renewed Successfully");
					request.setAttribute("policy", policyBO.renewPolicy(policyKey));
				}
				else if("Cancel".equals(actionSelected))
				{
					request.setAttribute("successMsg", "Policy Cancelled Successfully");
					request.setAttribute("policy", policyBO.cancelPolicy(policyKey));
				}
				forwardPage = HomeInsuranceConstants.POLICY;
			}
			final RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPage);
			dispatcher.forward(request, response);
		} catch (Exception e) {
			logger.error("Exception occurred in method PropertyServlet.doPost :: "
					+ e);
			request.setAttribute("message", e.getMessage());
			final RequestDispatcher dispatcher = request.getRequestDispatcher(HomeInsuranceConstants.ERROR);
			dispatcher.forward(request, response);
		}
	}
}