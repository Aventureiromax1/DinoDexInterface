package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexao {

    private static final Logger LOGGER = Logger.getLogger(Conexao.class.getName());

    private static final String URL = "jdbc:postgresql://localhost:5432/dinossauros";
    private static final String USUARIO = "leo";
    private static final String SENHA = "leo260908";

    public Connection conectar() {
        try {
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(URL, USUARIO, SENHA);
            inicializarBancoDeDados(conn);
            return conn;
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Driver PostgreSQL não encontrado.", e);
            throw new RuntimeException("Driver PostgreSQL não encontrado.", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Erro ao conectar ao banco de dados.", e);
            throw new RuntimeException("Erro ao conectar ao banco de dados.", e);
        }
    }
    
    private void inicializarBancoDeDados(Connection conn) {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS public.dinossauro (" +
                "id SERIAL PRIMARY KEY, " +
                "nome VARCHAR(255) NOT NULL, " +
                "especie VARCHAR(255), " +
                "peso INTEGER, " +
                "altura DOUBLE PRECISION, " +
                "comprimento DOUBLE PRECISION, " +
                "comportamento TEXT, " +
                "data_criacao TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP" +
                ");";
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createTableSQL);
        } catch (SQLException e) {
            LOGGER.log(Level.WARNING, "Erro ao inicializar tabela 'dinossauro'. Pode já existir.", e);
        }
    }
}