package main.database;

import main.models.Fornecedor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FornecedorDAO {

    // Método para buscar todos os fornecedores
    public List<Fornecedor> buscarTodos() throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT id_fornecedor, id_empresa, cnpj, razao_social, telefone, e_mail, " +
                    "rua, numero, bairro, cidade, estado, cep, data_cadastro, data_atualizacao " +
                    "FROM fornecedor ORDER BY razao_social";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                Fornecedor fornecedor = new Fornecedor();
                fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                fornecedor.setIdEmpresa(rs.getInt("id_empresa"));
                fornecedor.setCnpj(rs.getString("cnpj"));
                fornecedor.setRazaoSocial(rs.getString("razao_social"));
                fornecedor.setTelefone(rs.getString("telefone"));
                fornecedor.setEmail(rs.getString("e_mail"));
                fornecedor.setRua(rs.getString("rua"));
                fornecedor.setNumero(rs.getString("numero"));
                fornecedor.setBairro(rs.getString("bairro"));
                fornecedor.setCidade(rs.getString("cidade"));
                fornecedor.setEstado(rs.getString("estado"));
                fornecedor.setCep(rs.getString("cep"));
                fornecedor.setDataCadastro(rs.getTimestamp("data_cadastro") != null ?
                    rs.getTimestamp("data_cadastro").toLocalDateTime() : null);
                fornecedor.setDataAtualizacao(rs.getTimestamp("data_atualizacao") != null ?
                    rs.getTimestamp("data_atualizacao").toLocalDateTime() : null);

                fornecedores.add(fornecedor);
            }
        }
        return fornecedores;
    }

    // Método para buscar fornecedor por ID
    public Fornecedor buscarPorId(int idFornecedor) throws SQLException {
        String sql = "SELECT id_fornecedor, id_empresa, cnpj, razao_social, telefone, e_mail, " +
                    "rua, numero, bairro, cidade, estado, cep, data_cadastro, data_atualizacao " +
                    "FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFornecedor);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                    fornecedor.setIdEmpresa(rs.getInt("id_empresa"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setRazaoSocial(rs.getString("razao_social"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("e_mail"));
                    fornecedor.setRua(rs.getString("rua"));
                    fornecedor.setNumero(rs.getString("numero"));
                    fornecedor.setBairro(rs.getString("bairro"));
                    fornecedor.setCidade(rs.getString("cidade"));
                    fornecedor.setEstado(rs.getString("estado"));
                    fornecedor.setCep(rs.getString("cep"));
                    fornecedor.setDataCadastro(rs.getTimestamp("data_cadastro") != null ?
                        rs.getTimestamp("data_cadastro").toLocalDateTime() : null);
                    fornecedor.setDataAtualizacao(rs.getTimestamp("data_atualizacao") != null ?
                        rs.getTimestamp("data_atualizacao").toLocalDateTime() : null);

                    return fornecedor;
                }
            }
        }
        return null;
    }

    // Método para buscar fornecedores por razão social (para busca)
    public List<Fornecedor> buscarPorRazaoSocial(String razaoSocial) throws SQLException {
        List<Fornecedor> fornecedores = new ArrayList<>();
        String sql = "SELECT id_fornecedor, id_empresa, cnpj, razao_social, telefone, e_mail, " +
                    "rua, numero, bairro, cidade, estado, cep, data_cadastro, data_atualizacao " +
                    "FROM fornecedor WHERE UPPER(razao_social) LIKE UPPER(?) ORDER BY razao_social";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, "%" + razaoSocial + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Fornecedor fornecedor = new Fornecedor();
                    fornecedor.setIdFornecedor(rs.getInt("id_fornecedor"));
                    fornecedor.setIdEmpresa(rs.getInt("id_empresa"));
                    fornecedor.setCnpj(rs.getString("cnpj"));
                    fornecedor.setRazaoSocial(rs.getString("razao_social"));
                    fornecedor.setTelefone(rs.getString("telefone"));
                    fornecedor.setEmail(rs.getString("e_mail"));
                    fornecedor.setRua(rs.getString("rua"));
                    fornecedor.setNumero(rs.getString("numero"));
                    fornecedor.setBairro(rs.getString("bairro"));
                    fornecedor.setCidade(rs.getString("cidade"));
                    fornecedor.setEstado(rs.getString("estado"));
                    fornecedor.setCep(rs.getString("cep"));
                    fornecedor.setDataCadastro(rs.getTimestamp("data_cadastro") != null ?
                        rs.getTimestamp("data_cadastro").toLocalDateTime() : null);
                    fornecedor.setDataAtualizacao(rs.getTimestamp("data_atualizacao") != null ?
                        rs.getTimestamp("data_atualizacao").toLocalDateTime() : null);

                    fornecedores.add(fornecedor);
                }
            }
        }
        return fornecedores;
    }

    // Método para inserir fornecedor
    public boolean inserir(Fornecedor fornecedor) throws SQLException {
        String sql = "INSERT INTO fornecedor (id_empresa, cnpj, razao_social, senha_hash, telefone, e_mail, " +
                    "rua, numero, bairro, cidade, estado, cep) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, fornecedor.getIdEmpresa());
            stmt.setString(2, fornecedor.getCnpj());
            stmt.setString(3, fornecedor.getRazaoSocial());
            stmt.setString(4, fornecedor.getSenhaHash() != null ? fornecedor.getSenhaHash() : "");
            stmt.setString(5, fornecedor.getTelefone());
            stmt.setString(6, fornecedor.getEmail());
            stmt.setString(7, fornecedor.getRua());
            stmt.setString(8, fornecedor.getNumero());
            stmt.setString(9, fornecedor.getBairro());
            stmt.setString(10, fornecedor.getCidade());
            stmt.setString(11, fornecedor.getEstado());
            stmt.setString(12, fornecedor.getCep());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    // Método para atualizar fornecedor
    public boolean atualizar(Fornecedor fornecedor) throws SQLException {
        String sql = "UPDATE fornecedor SET cnpj = ?, razao_social = ?, telefone = ?, e_mail = ?, " +
                    "rua = ?, numero = ?, bairro = ?, cidade = ?, estado = ?, cep = ? " +
                    "WHERE id_fornecedor = ?";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, fornecedor.getCnpj());
            stmt.setString(2, fornecedor.getRazaoSocial());
            stmt.setString(3, fornecedor.getTelefone());
            stmt.setString(4, fornecedor.getEmail());
            stmt.setString(5, fornecedor.getRua());
            stmt.setString(6, fornecedor.getNumero());
            stmt.setString(7, fornecedor.getBairro());
            stmt.setString(8, fornecedor.getCidade());
            stmt.setString(9, fornecedor.getEstado());
            stmt.setString(10, fornecedor.getCep());
            stmt.setInt(11, fornecedor.getIdFornecedor());

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    // Método para excluir fornecedor
    public boolean excluir(int idFornecedor) throws SQLException {
        String sql = "DELETE FROM fornecedor WHERE id_fornecedor = ?";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idFornecedor);

            int linhasAfetadas = stmt.executeUpdate();
            return linhasAfetadas > 0;
        }
    }

    // Método para contar total de fornecedores
    public int contarTotal() throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM fornecedor";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            if (rs.next()) {
                return rs.getInt("total");
            }
        }
        return 0;
    }

    // Método para verificar se CNPJ já existe
    public boolean cnpjExiste(String cnpj, Integer idExcluir) throws SQLException {
        String sql = "SELECT COUNT(*) as total FROM fornecedor WHERE cnpj = ?";
        if (idExcluir != null) {
            sql += " AND id_fornecedor != ?";
        }

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, cnpj);
            if (idExcluir != null) {
                stmt.setInt(2, idExcluir);
            }

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("total") > 0;
                }
            }
        }
        return false;
    }

    // Classe interna para compatibilidade com código existente
    public static class FornecedorCompatibilidade {
        private int idFornecedor;
        private String razaoSocial;

        public FornecedorCompatibilidade(int idFornecedor, String razaoSocial) {
            this.idFornecedor = idFornecedor;
            this.razaoSocial = razaoSocial;
        }

        public int getIdFornecedor() {
            return idFornecedor;
        }

        public String getRazaoSocial() {
            return razaoSocial;
        }

        @Override
        public String toString() {
            return razaoSocial;
        }
    }

    // Método para compatibilidade com código existente
    public List<FornecedorCompatibilidade> buscarTodosCompatibilidade() throws SQLException {
        List<FornecedorCompatibilidade> fornecedores = new ArrayList<>();
        String sql = "SELECT id_fornecedor, razao_social FROM fornecedor ORDER BY razao_social";

        try (Connection conn = DatabaseConnection.getConnectionMercado();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                FornecedorCompatibilidade fornecedor = new FornecedorCompatibilidade(
                    rs.getInt("id_fornecedor"),
                    rs.getString("razao_social")
                );
                fornecedores.add(fornecedor);
            }
        }
        return fornecedores;
    }
}
