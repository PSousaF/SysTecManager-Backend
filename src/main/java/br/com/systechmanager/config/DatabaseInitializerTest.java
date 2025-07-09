package br.com.systechmanager.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.Statement;

@Configuration
//@Profile("dev")
public class DatabaseInitializerTest {

    @Bean
    public CommandLineRunner initDatabase(DataSource dataSource) {
        return args -> {
            try (Connection conn = dataSource.getConnection()) {
                Statement stmt = conn.createStatement();
                
                createTables(stmt);
                
                insertTestData(stmt);
                
                System.out.println("Banco de dados inicializado com os teste!");
            }
        };
    }

    private void createTables(Statement stmt) throws Exception {
        stmt.execute("CREATE TABLE IF NOT EXISTS fornecedores (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "tipo VARCHAR(50)," +
                "num_loja INT," +
                "num_cli INT," +
                "endereco VARCHAR(200)," +
                "estado VARCHAR(100)," +
                "telefone VARCHAR(20)," +
                "email VARCHAR(100)," +
                "pecas VARCHAR(200)," +
                "ativo INT DEFAULT 1)");

        stmt.execute("CREATE TABLE IF NOT EXISTS usuarios (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "username VARCHAR(50) UNIQUE," +
                "password VARCHAR(100)," +
                "endereco VARCHAR(200)," +
                "estado VARCHAR(100)," +
                "telefone VARCHAR(20)," +
                "email VARCHAR(100)," +
                "cargo VARCHAR(50)," +
                "cpf VARCHAR(14) UNIQUE," +
                "ativo INT DEFAULT 1," +
                "permissao INT DEFAULT 0)");

        stmt.execute("CREATE TABLE IF NOT EXISTS orcamento (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "device_type VARCHAR(100)," +
                "model VARCHAR(100)," +
                "quantity INT," +
                "id_resp INT," +
                "id_cli INT," +
                "device_brand VARCHAR(100)," +
                "serie VARCHAR(100)," +
                "pieces VARCHAR(200)," +
                "detail VARCHAR(100)," +
                "defect TEXT," +
                "review TEXT," +
                "possible_causes TEXT," +
                "observation TEXT," +
                "value_item VARCHAR(50)," +
                "cost VARCHAR(50)," +
                "situation VARCHAR(50)," +
                "aparroved VARCHAR(50)," +
                "finish VARCHAR(50)," +
                "stating VARCHAR(50)," +
                "bid VARCHAR(50))");     
        
        stmt.execute("CREATE TABLE IF NOT EXISTS clientes (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "nome VARCHAR(100) NOT NULL," +
                "endereco VARCHAR(200)," +
                "telefone VARCHAR(20)," +
                "estado VARCHAR(100) DEFAULT 'SP'," +
                "cpf VARCHAR(14) UNIQUE," +
                "email VARCHAR(100))");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS databid (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "bid VARCHAR(50) NOT NULL," +
                "item VARCHAR(200)," +
                "data LONGTEXT)");
        
        stmt.execute("CREATE TABLE IF NOT EXISTS movimentacao_financeira (" +
        		    "id BIGINT AUTO_INCREMENT PRIMARY KEY," +
        		    "tipo VARCHAR(10) NOT NULL," +
        		    "valor DECIMAL(10, 2) NOT NULL," +
        		    "data DATE NOT NULL," +
        		    "descricao VARCHAR(255)," +
        		    "value_item BIGINT," +
        		    "feito boolean," +
        		    "FOREIGN KEY (value_item) REFERENCES orcamento(id));");

    }

    private void insertTestData(Statement stmt) throws Exception {
        stmt.execute("INSERT INTO fornecedores (nome, tipo, num_loja, num_cli, endereco, estado, telefone, email, pecas, ativo) VALUES " +
                "('Auto Peças Master', 'Automotivo', 5, 1200, 'Rua das Indústrias, 123, Industrial - São Paulo', 'SP', '(11) 1234-5678', 'contato@autopecas.com', 'Motor, Câmbio, Freios', 1), " +
                "('Peças Rápidas', 'Automotivo', 3, 850, 'Av. Comercial, 456, Centro - Rio de Janeiro','RJ', '(21) 9876-5432', 'vendas@pecasrapidas.com', 'Suspensão, Direção', 0), " +
                "('Distribuidora de Peças', 'Atacado', 1, 3200, 'Rua dos Fornecedores, 789, Comercial - Belo Horizonte', 'MG', '(31) 4567-8901', 'atendimento@distribuidora.com', 'Todos os tipos', 1), " +
                "('Peças Nacionais', 'Nacional', 8, 1500, 'Av. das Nações, 101, Nacional - Curitiba', 'PR', '(41) 2345-6789', 'sac@pecasnacionais.com', 'Peças originais', 1), " +
                "('Importadora Auto', 'Importado', 2, 980, 'Rua dos Importados, 202, Internacional - Porto Alegre', 'RS', '(51) 3456-7890', 'import@auto.com', 'Peças importadas', 0), " +
                "('Fornecedor Express', 'Diversos', 4, 2100, 'Av. Rápida, 303, Veloz - Salvador', 'BA', '(71) 5678-9012', 'express@fornecedor.com', 'Acessórios', 1), " +
                "('Peças Premium', 'Luxo', 6, 450, 'Rua Exclusiva, 404, Nobre - Brasília', 'DF', '(61) 6789-0123', 'premium@pecas.com', 'Peças premium', 1), " +
                "('Atacado Veículos', 'Atacado', 7, 1800, 'Av. dos Veículos, 505, Automotivo - Recife', 'PE', '(81) 7890-1234', 'atacado@veiculos.com', 'Peças em geral', 0), " +
                "('Distribuidor Nacional', 'Distribuidor', 9, 2500, 'Rua Nacional, 606, Distrito - Fortaleza', 'CE', '(85) 8901-2345', 'nacional@distribuidor.com', 'Peças diversas', 1), " +
                "('Fornecedor Ouro', 'Especializado', 10, 600, 'Av. das Peças, 707, Ouro - Manaus', 'AM', '(92) 9012-3456', 'ouro@fornecedor.com', 'Peças especiais', 1)");

        stmt.execute("INSERT INTO usuarios (nome, username, password, endereco, estado, telefone, email, cargo, cpf, ativo, permissao) VALUES " +
                "('Admin Master', 'admin', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Rua Principal, 100, Centro', 'São Paulo', '(11) 1111-1111', 'admin@empresa.com', 'Administrador', '111.111.111-11', 1, 2), " +
                "('Gerente Geral', 'gerente', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Av. Gerencial, 200, Gerência', 'São Paulo', '(11) 2222-2222', 'gerente@empresa.com', 'Gerente', '222.222.222-22', 1, 1), " +
                "('Vendedor 1', 'vendedor1', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Rua das Vendas, 300, Comercial', 'São Paulo', '(11) 3333-3333', 'vendedor1@empresa.com', 'Vendedor', '333.333.333-33', 0, 0), " +
                "('Vendedor 2', 'vendedor2', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Av. Comercial, 400, Vendas', 'Rio de Janeiro', '(21) 4444-4444', 'vendedor2@empresa.com', 'Vendedor', '444.444.444-44', 1, 0), " +
                "('Estoquista', 'estoquista', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Rua do Estoque, 500, Armazém', 'Belo Horizonte', '(31) 5555-5555', 'estoquista@empresa.com', 'Estoquista', '555.555.555-55', 1, 0), " +
                "('Financeiro', 'financeiro', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Av. Financeira, 600, Bancário', 'Curitiba', '(41) 6666-6666', 'financeiro@empresa.com', 'Financeiro', '666.666.666-66', 0, 0), " +
                "('Suporte TI', 'suporte', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Rua da Tecnologia, 700, Tecnológico', 'Porto Alegre', '(51) 7777-7777', 'suporte@empresa.com', 'TI', '777.777.777-77', 1, 0), " +
                "('Atendente', 'atendente', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Av. Atendimento, 800, Central', 'Salvador', '(71) 8888-8888', 'atendente@empresa.com', 'Atendente', '888.888.888-88', 1, 0), " +
                "('Analista', 'Sousa', '$2a$10$.DfPhLP.RH3Y0QmbUaOzT.JQj9rvO8UKZHvLcr5ajSnAPPbXKix4C', 'Rua Analítica, 900, Análise', 'Brasília', '(61) 9999-9999', 'analista@empresa.com', 'Analista', '999.999.999-99', 1, 1), " +
                "('Admin Master', 'Pedro', '$2a$10$.DfPhLP.RH3Y0QmbUaOzT.JQj9rvO8UKZHvLcr5ajSnAPPbXKix4C', 'Rua Vasconcelos, 333, Análise', 'Brasília', '(61) 9999-9999', 'analista@empresa.com', 'Analista', '999.999.999-90', 1, 0), " +
                "('Visitante', 'visitante', '$2a$10$XptfskLsT1l/bRTLRiiCgejHqOpgXFreUnNUa35gJdCr2v2QbVFzu', 'Av. Visitante, 1000, Visitantes', 'Recife', '(81) 1010-1010', 'visitante@empresa.com', 'Visitante', '101.101.101-10', 1, 0)");

        stmt.execute("INSERT INTO clientes (nome, endereco, telefone, email, cpf, estado) VALUES " +
                "('John Doe', 'Rua Principal, 111, Centro - São Paulo', '(11) 1111-2222', 'john@example.com', '112.111.111-11', 'RS'), " +
                "('João Silva', 'Av. Gerencial, 222, Gerência - São Paulo', '(11) 2222-3333', 'joao@example.com', '223.222.222-22', 'SP'), " +
                "('Sandra Regina', 'Rua das Vendas, 333, Comercial - São Paulo', '(11) 3333-4444', 'sandra@example.com', '334.333.333-33', 'SP'), " +
                "('Maria Dolores', 'Av. Comercial, 444, Vendas - Rio de Janeiro', '(21) 4444-5555', 'maria@example.com', '445.444.444-44', 'RJ'), " +
                "('José Alencar', 'Rua do Estoque, 555, Armazém - Belo Horizonte', '(31) 5555-6666', 'jose@example.com', '556.555.555-55', 'SP'), " +
                "('Amanda Beatriz', 'Av. Financeira, 66, Bancário - Curitiba', '(41) 6666-7777', 'amanda@example.com', '667.666.666-66', 'SP'), " +
                "('Susana Vieira', 'Rua da Tecnologia, 777, Tecnológico - Porto Alegre', '(51) 7777-8888', 'susana@example.com', '778.777.777-77', 'RS'), " +
                "('Paulo Gomes', 'Av. Atendimento, 888, Central - Salvador', '(71) 8888-9999', 'paulo@example.com', '889.888.888-88', 'SP'), " +
                "('Saul Hudson', 'Rua Analítica, 999, Análise - Brasília', '(61) 9999-0000', 'saul@example.com', '990.999.999-99', 'RJ'), " +
                "('Pedro Sousa', 'Av. Visitante, 1010, Visitantes - Recife', '(81) 1010-1111', 'pedro@example.com', '101.101.101-10', 'MG')");

        stmt.execute("INSERT INTO orcamento (device_type, model, quantity, id_resp, id_cli, device_brand, serie, defect, review, possible_causes, observation, value_item, situation, aparroved, bid, stating, pieces) VALUES " +
                "('Notebook', 'Dell XPS 15', 1, 1, 1, 'Dell', 'SN12345', 'Não liga', 'Troca de fonte', 'Fonte queimada', 'Cliente trouxe sem carregador', '1200.00', 'Em análise', 'Sim', 'BID2023001', 'Não', 'Resistor 100Oms')," +
                "('Smartphone', 'iPhone 13', 1, 2, 2, 'Apple', 'SN67890', 'Tela quebrada', 'Substituição de tela', 'Queda do aparelho', 'Cliente solicitou tela original', '800.00', 'Aprovado', 'Sim', 'BID2023002', 'Sim', 'Capacitor 25v')," +
                "('Tablet', 'Samsung Galaxy Tab S7', 2, 2, 3, 'Samsung', 'SN54321', 'Bateria não carrega', 'Substituição de bateria', 'Bateria defeituosa', 'Aparelhos com mesmo problema', '450.00', 'Aguardando peça', 'Não', 'BID2023003', 'Não', 'Transistor Tip41c')," +
                "('Desktop', 'HP Pavilion', 3, 1, 2, 'HP', 'SN98765', 'Superaquecimento', 'Limpeza e troca de pasta térmica', 'Acúmulo de poeira', 'Cliente relatou lentidão', '200.00', 'Concluído', 'Sim', 'BID2023004', 'Sim', 'Pasta Térmica')," +
                "('Monitor', 'LG UltraFine 4K', 1, 5, 6, 'LG', 'SN13579', 'Manchas na tela', 'Substituição do painel', 'Defeito no LCD', 'Em garantia - verificar com fabricante', '0.00', 'Em garantia', 'Pendente', 'BID2023005', 'Não', 'Fonte 24v 3Amp')," +
                "('Impressora', 'Epson L3150', 1, 7, 9, 'Epson', 'SN24680', 'Não imprime', 'Limpeza de cabeçote', 'Tinta seca nos bicos', 'Cliente não usou por 3 meses', '150.00', 'Concluído', 'Sim', 'BID2023006', 'Sim', 'Capacitor 12v 105C')," +
                "('Notebook', 'Lenovo ThinkPad', 1, 4, 4, 'Lenovo', 'SN11223', 'Teclado não funciona', 'Substituição do teclado', 'Derramamento de líquido', 'Teclas grudando', '350.00', 'Em reparo', 'Sim', 'BID2023007', 'Não', 'Capacitor 6v')," +
                "('Smartphone', 'Samsung Galaxy S21', 1, 2, 7, 'Samsung', 'SN44556', 'Problema de conexão', 'Troca de módulo Wi-Fi', 'Defeito na antena', 'Não conecta a redes 5GHz', '300.00', 'Aprovado', 'Sim', 'BID2023008', 'Sim', 'Resistor 1k')," +
                "('Notebook', 'Acer Nitro 5', 1, 6, 5, 'Acer', 'SN77889', 'Desempenho ruim', 'Upgrade de memória', 'Memória insuficiente', 'Cliente quer upgrade para 16GB', '400.00', 'Concluído', 'Sim', 'BID2023009', 'Sim', 'Não')," +
                "('Smartwatch', 'Apple Watch Series 6', 1, 3, 7, 'Apple', 'SN00112', 'Tela sensível', 'Calibração do touchscreen', 'Problema no digitalizador', 'Área inferior não responde', '250.00', 'Em análise', 'Não', 'BID2023010', 'Não', 'Transistor 58COP')," +
                "('Notebook', 'Asus ZenBook', 1, 9, 2, 'Asus', 'SN33445', 'Problema no áudio', 'Substituição de placa de som', 'Saída de áudio danificada', 'Som apenas pelo lado direito', '280.00', 'Aguardando peça', 'Sim', 'BID2023011', 'Não', 'Transistor PJS83')," +
                "('Console', 'PlayStation 5', 1, 2, 7, 'Sony', 'SN66778', 'Não lê discos', 'Substituição do drive', 'Mecanismo de leitura danificado', 'Discos ficam presos', '500.00', 'Em reparo', 'Sim', 'BID2023012', 'Sim', 'Capacitor 70v')," +
                "('Smartphone', 'Xiaomi Redmi Note 10', 1, 5, 5, 'Xiaomi', 'SN99001', 'Problema na câmera', 'Substituição do módulo da câmera', 'Lente traseira quebrada', 'Fotos saem borradas', '180.00', 'Concluído', 'Sim', 'BID2023013', 'Sim', 'Fonte 12v 1.2Amp')," +
                "('Notebook', 'MacBook Pro M1', 1, 9, 1, 'Apple', 'SN22334', 'Problema no teclado', 'Substituição do teclado', 'Tecla space não funciona', 'Em garantia internacional', '0.00', 'Em garantia', 'Pendente', 'BID2023014', 'Não', 'Resistor 500Oms')," +
                "('Monitor', 'Dell 27\" 4K', 1, 8, 8, 'Dell', 'SN55667', 'Pixels mortos', 'Substituição do painel', 'Defeito de fabricação', '3 pixels pretos na área central', '700.00', 'Aprovado', 'Sim', 'BID2023015', 'Sim', 'Display 9O4DU')");
        
        stmt.execute("INSERT INTO movimentacao_financeira (tipo, valor, data, descricao, value_item, feito) VALUES"
        		+ "('ENTRADA', 40.00, '2025-07-01', 'Pagamento de cliente João', 1, false),"
        		+ "('SAIDA', 10.00, '2025-07-01', 'Compra de peça A', 1, true),"
        		+ "('ENTRADA', 70.00, '2025-07-02', 'Pagamento de cliente Maria', 2, false),"
        		+ "('SAIDA', 7.00, '2025-07-02', 'Entrega', 2, false),"
        		+ "('ENTRADA', 50.00, '2025-07-03', 'Cliente Lucas', 3, false),"
        		+ "('SAIDA', 5.00, '2025-07-03', 'Adiantamento de frete', 3, true),"
        		+ "('ENTRADA', 55.00, '2025-07-04', 'Cliente Ana', 4, true),"
        		+ "('SAIDA', 8.00, '2025-07-04', 'Peça substituta', 4, false),"
        		+ "('ENTRADA', 90.00, '2025-07-05', 'Serviço premium', 5, false),"
        		+ "('SAIDA', 15.00, '2025-07-05', 'Reembolso', 5, false),"
        		+ "('ENTRADA', 120.00, '2025-07-06', 'Venda de software', 6, true),"
        		+ "('SAIDA', 25.00, '2025-07-06', 'Licença de software', 6, false),"
        		+ "('ENTRADA', 35.00, '2025-07-07', 'Consultoria técnica', 7, true),"
        		+ "('SAIDA', 12.00, '2025-07-07', 'Material de escritório', 7, false),"
        		+ "('ENTRADA', 80.00, '2025-07-08', 'Serviço de manutenção', 8, false),"
        		+ "('SAIDA', 30.00, '2025-07-08', 'Ferramentas novas', 8, true),"
        		+ "('ENTRADA', 60.00, '2025-07-09', 'Aluguel de equipamento', 9, false),"
        		+ "('SAIDA', 18.00, '2025-07-09', 'Combustível', 9, false),"
        		+ "('ENTRADA', 45.00, '2025-07-10', 'Aula particular', 10, true),"
        		+ "('SAIDA', 7.50, '2025-07-10', 'Lanche para reunião', 10, false),"
        		+ "('ENTRADA', 150.00, '2025-07-11', 'Projeto web', 11, false),"
        		+ "('SAIDA', 40.00, '2025-07-11', 'Hospedagem de site', 11, true),"
        		+ "('ENTRADA', 20.00, '2025-07-12', 'Venda de livro', 12, true),"
        		+ "('SAIDA', 5.00, '2025-07-12', 'Envio de encomenda', 12, false),"
        		+ "('ENTRADA', 95.00, '2025-07-13', 'Instalação de rede', 13, true),"
        		+ "('SAIDA', 22.00, '2025-07-13', 'Cabos de rede', 13, false),"
        		+ "('ENTRADA', 75.00, '2025-07-14', 'Suporte técnico remoto', 14, false),"
        		+ "('SAIDA', 10.00, '2025-07-14', 'Assinatura de software', 14, true),"
        		+ "('ENTRADA', 110.00, '2025-07-15', 'Treinamento corporativo', 15, false),"
        		+ "('SAIDA', 30.00, '2025-07-15', 'Material didático', 15, true);");
    }
}




