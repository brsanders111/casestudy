/**
 * This DAO class is used to for Homeowner Information
 *
 * @author Cognizant
 * @contact Cognizant
 * @version 1.0
 */
package com.cts.insurance.homequote.dao;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.cts.insurance.homequote.exception.HomequoteSystemException;
import com.cts.insurance.homequote.model.Homeowner;
import com.cts.insurance.homequote.model.Location;
import com.cts.insurance.homequote.util.HomeInsuranceConstants;
import com.cts.insurance.homequote.util.SqlQueries;

public class HomeownerDAO {
	
	private final static Logger LOG = Logger.getLogger(HomeownerDAO.class);

	/**
	 * @param homeowner
	 */
	public void saveHomeowner(final Homeowner homeowner) throws HomequoteSystemException
	{
		LOG.info("HomeownerDAO.saveHomeowner - starts");
		Connection conn = null;
		PreparedStatement stmt = null;
		
		//Fill code here
		
		try {
			final AbstractDAOFactory daoFactory = AbstractDAOFactory.getDaoFactory(HomeInsuranceConstants.MYSQL);
			conn = daoFactory.getConnection();
			stmt = conn.prepareStatement(SqlQueries.SAVE_HOMEOWNER);
			stmt.setInt(1, homeowner.getQuoteId());
			stmt.setString(2, homeowner.getFirstName());
			stmt.setString(3, homeowner.getLastName());
			stmt.setString(4, homeowner.getDob());
			stmt.setString(5, homeowner.getIsRetired());
			stmt.setString(6, homeowner.getSsn());
			stmt.setString(7, homeowner.getEmailAddress());
			
			stmt.executeUpdate();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally {
			try {
			stmt.close();
			conn.close();
		 }
			catch(SQLException e)
			{
				LOG.error("Exception while trying to close Connection : " + e.getMessage());
			}
			LOG.info("HomeownerDAO.saveHomeowner - ends");
		}	
	}
	

	/**
	 * @param homeowner
	 */
	public Homeowner getHomeowner(final int quoteId) throws HomequoteSystemException
	{
		LOG.info("HomeownerDAO.getHomeowner - starts");
		Connection conn = null;
		Homeowner homeowner = null;
		ResultSet resultSet = null;
		PreparedStatement stmt = null;
		//Fill code here
		
		final AbstractDAOFactory daoFactory = AbstractDAOFactory.getDaoFactory(HomeInsuranceConstants.MYSQL);
		try {
			conn = daoFactory.getConnection();
			stmt = conn.prepareStatement(SqlQueries.GET_HOMEOWNER);
			stmt.setInt(1, quoteId);
			resultSet = stmt.executeQuery();
			homeowner = new Homeowner();
			while (resultSet.next()) {
				homeowner.setQuoteId(resultSet.getInt(1));//QUOTE_ID
				homeowner.setFirstName(resultSet.getString(2));//RESIDENCE_TYPE
				homeowner.setLastName(resultSet.getString(3));//ADDRESS_LINE_1
				homeowner.setDob(resultSet.getString(4));//ADDRESS_LINE_2
				homeowner.setIsRetired(resultSet.getString(5));//CITY
				homeowner.setSsn(resultSet.getString(6));//STATE
				homeowner.setEmailAddress(resultSet.getString(7));//ZIP
				
			}
			resultSet.close();
			stmt.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally
		{
			try
			{
				resultSet.close();
				stmt.close();
				conn.close();
			}
			catch (SQLException e)
			{
				LOG.error("Exception while trying to close Connection : " + e.getMessage() );
			}
		}
		LOG.info("LocationDAO.getLocation - ends");
		
	
		return homeowner; //return Object
	}

}
