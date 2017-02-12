package com.iainsproat.simscaletest.primenumbergenerator.server.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

/**
 * Responsible for connecting to and transacting with the sqlite database
 *
 */
public class DatabaseConnection {
	private Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
	
	public boolean addCalculation(PrimeNumberGeneratorStrategyClient.Result calculation)
	{
		boolean initialize = false;
		try{
			initialize = SQLiteJDBCLoader.initialize();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
			return false;
		}
		
		SQLiteDataSource dataSource = new SQLiteDataSource();
		Connection conn = null;
		PreparedStatement prepared = null;
		
		try
		{
			dataSource.setUrl("jdbc:sqlite:/primenumbergenerator-db.db");
			conn = dataSource.getConnection();
			prepared = generateInsertStatementForCalculation(conn, calculation);
			
			prepared.executeUpdate();
		}
		catch(SQLException e)
		{
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		finally
		{
			try
			{
				if(prepared != null)
					prepared.close();
				if(conn != null)
					conn.close();
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
			}
		}
		
		return initialize;
	}
	
	protected PreparedStatement generateInsertStatementForCalculation(Connection conn, PrimeNumberGeneratorStrategyClient.Result calculation) throws SQLException
	{
		PreparedStatement prepared =  conn.prepareStatement("INSERT INTO Calculations (TransactionID, TimeStamp, Strategy, LowerBound, UpperBound, PrimeCount, Duration) "
				+ "VALUES (?, ?, ?, ?, ?, ?, ?)");
		prepared.setString(1, UUID.randomUUID().toString());
		prepared.setLong(2, calculation.getTimeStamp());
		prepared.setString(3, calculation.getRequestedStrategy());
		prepared.setInt(4, calculation.getLowerBound());
		prepared.setInt(5, calculation.getUpperBound());
		prepared.setInt(6, calculation.getPrimeNumbers().size());
		prepared.setLong(7, calculation.getExecutionDuration());

		return prepared;

	}
}
