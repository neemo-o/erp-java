package main.models;

import java.time.LocalDateTime;

public class Fornecedor {
    private int idFornecedor;
    private int idEmpresa;
    private String cnpj;
    private String razaoSocial;
    private String senhaHash;
    private String telefone;
    private String email;
    private String rua;
    private String numero;
    private String bairro;
    private String cidade;
    private String estado;
    private String cep;
    private LocalDateTime dataCadastro;
    private LocalDateTime dataAtualizacao;

    // Construtores
    public Fornecedor() {}

    public Fornecedor(int idFornecedor, String razaoSocial) {
        this.idFornecedor = idFornecedor;
        this.razaoSocial = razaoSocial;
    }

    public Fornecedor(int idFornecedor, int idEmpresa, String cnpj, String razaoSocial,
                     String telefone, String email, String rua, String numero,
                     String bairro, String cidade, String estado, String cep) {
        this.idFornecedor = idFornecedor;
        this.idEmpresa = idEmpresa;
        this.cnpj = cnpj;
        this.razaoSocial = razaoSocial;
        this.telefone = telefone;
        this.email = email;
        this.rua = rua;
        this.numero = numero;
        this.bairro = bairro;
        this.cidade = cidade;
        this.estado = estado;
        this.cep = cep;
    }

    // Getters e Setters
    public int getIdFornecedor() {
        return idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

    public int getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(int idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getSenhaHash() {
        return senhaHash;
    }

    public void setSenhaHash(String senhaHash) {
        this.senhaHash = senhaHash;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public LocalDateTime getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDateTime dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public LocalDateTime getDataAtualizacao() {
        return dataAtualizacao;
    }

    public void setDataAtualizacao(LocalDateTime dataAtualizacao) {
        this.dataAtualizacao = dataAtualizacao;
    }

    // Método para obter endereço completo
    public String getEnderecoCompleto() {
        StringBuilder endereco = new StringBuilder();
        if (rua != null && !rua.trim().isEmpty()) {
            endereco.append(rua);
        }
        if (numero != null && !numero.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(", ");
            endereco.append(numero);
        }
        if (bairro != null && !bairro.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(" - ");
            endereco.append(bairro);
        }
        if (cidade != null && !cidade.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(", ");
            endereco.append(cidade);
        }
        if (estado != null && !estado.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(" - ");
            endereco.append(estado);
        }
        if (cep != null && !cep.trim().isEmpty()) {
            if (endereco.length() > 0) endereco.append(" - CEP: ");
            endereco.append(cep);
        }
        return endereco.toString();
    }

    @Override
    public String toString() {
        return razaoSocial;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Fornecedor fornecedor = (Fornecedor) obj;
        return idFornecedor == fornecedor.idFornecedor;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idFornecedor);
    }
}
