package com.iainsproat.simscaletest.primenumbergenerator.server.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sqlite.SQLiteDataSource;
import org.sqlite.SQLiteJDBCLoader;

import com.google.gson.Gson;
import com.iainsproat.simscaletest.primenumbergenerator.core.PrimeNumberGeneratorStrategyClient;

/**
 * Responsible for connecting to and transacting with the sqlite database
 *
 */
public class DatabaseConnection implements AutoCloseable
{
	private Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
	
	private static DatabaseConnection singleton;
	
	private SQLiteDataSource dataSource;
	private Connection connection;
	
	/**
	 * Creates database as a singleton so database connection can be reused.
	 * 
	 * If it wasn't a singleton the connection would be created and disposed on every request to the webserver.
	 * Additionally, Sqlite doesn't allow multi-threaded access so we don't lose any functionality by doing this (and no need for a connection pool).
	 * 
	 */
	public static DatabaseConnection getInstance()
	{
		if(singleton == null) {
			singleton = new DatabaseConnection();
		}
		
		return singleton;
	}
	
	private DatabaseConnection()
	{
		createDatabase();
	}
	
	/**
	 * Creates the connection to a jdbc database.
	 * In this example it is an in memory sqlite database.
	 */
	private void createDatabase(){
		logger.info("Creating database");
		
		try{
			SQLiteJDBCLoader.initialize();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
			return;
		}
		
		dataSource = new SQLiteDataSource();
		
		try
		{
			dataSource.setUrl("jdbc:sqlite::memory:");
			this.connection = dataSource.getConnection();
			
			createTables();
		
		}
		catch(SQLException e)
		{
			logger.error("Failed to get connection from DataSource or create tables");
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
	}
	
	/**
	 * Creates a single table 'Calculations'
	 * @throws SQLException
	 */
	private void createTables() throws SQLException {
		Statement statement = this.connection.createStatement();
		String createTableSQL = "CREATE TABLE `Calculations` ("
				+ "`PrimaryKey`	INTEGER NOT NULL UNIQUE,"
				+ "`TransactionID`	TEXT NOT NULL UNIQUE,"
				+ "`TimeStamp`	INTEGER NOT NULL,"
				+ "`Strategy`	TEXT,"
				+ "`LowerBound`	INTEGER,"
				+ "`UpperBound`	INTEGER,"
				+ "`PrimeCount`	INTEGER,"
				+ "`Duration`	INTEGER,"
				+ "`Errors`		TEXT,"
				+ "PRIMARY KEY(`PrimaryKey`)"
				+ ");";
		
		statement.executeUpdate(createTableSQL);
		statement.close();
	}

	/**
	 * Adds calculation result data to the underlying database.
	 * If it fails it logs an error statement
	 * @param calculation
	 */
	public void addCalculation(PrimeNumberGeneratorStrategyClient.Result calculation)
	{
		PreparedStatement prepared = null;
		
		try
		{
			prepared = generateInsertStatementForCalculation(calculation);
			
			prepared.executeUpdate();
			
			//FIXME to demonstrate the data is saved correctly in this example we'll dump the entire database table to the console every time!!
			printEntireCalculationsTable();
		}
		catch(SQLException e)
		{
			//FIXME we're just dumping the raw data to the log. Not secure & potential privacy breach.
			logger.error("Failed to add calculation data to the database. Calculation data: %s", new Gson().toJson(calculation));
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
		finally
		{
			try
			{
				if(prepared != null){
					prepared.close();
				}
			}
			catch(Exception e)
			{
				logger.error(e.getMessage());
				logger.error(e.getStackTrace().toString());
			}
		}
	}

	protected PreparedStatement generateInsertStatementForCalculation(PrimeNumberGeneratorStrategyClient.Result calculation) throws SQLException
	{
		PreparedStatement prepared =  this.connection
				.prepareStatement("INSERT INTO Calculations "
						+ "(TransactionID, TimeStamp, Strategy, LowerBound, UpperBound, PrimeCount, Duration, Errors) "
						+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		prepared.setString(1, UUID.randomUUID().toString());
		prepared.setLong(2, calculation.getTimeStamp());
		prepared.setString(3, calculation.getRequestedStrategy());
		prepared.setInt(4, calculation.getLowerBound());
		prepared.setInt(5, calculation.getUpperBound());
		prepared.setInt(6, calculation.getPrimeNumbers().size());
		prepared.setLong(7, calculation.getExecutionDuration());
		if(calculation.getErrors() == null){
			prepared.setString(8, "");
		}else{
			prepared.setString(8, String.join("; ", calculation.getErrors()));
		}
		return prepared;
	}
	
	/**
	 * This prints all the contents of the table
	 * @throws SQLException 
	 */
	protected void printEntireCalculationsTable() throws SQLException {
		Statement statement = this.connection.createStatement();
		String sql = "SELECT * FROM Calculations";
		ResultSet results = statement.executeQuery(sql);
		while(results.next()){
			logger.info(String.format("%s, %d, %s, %d, %d, %d, %d", 
					results.getString("TransactionID"),
					results.getLong("TimeStamp"),
					results.getString("Strategy"),
					results.getInt("LowerBound"),
					results.getInt("UpperBound"),
					results.getInt("PrimeCount"),
					results.getLong("Duration")
					));
		}
		
		results.close();
		statement.close();
	}

	@Override
	public void close() throws Exception {
		try
		{
			if(connection != null)
				connection.close();
		}
		catch(Exception e)
		{
			logger.error(e.getMessage());
			logger.error(e.getStackTrace().toString());
		}
	}
}
