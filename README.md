# 🏢 Folha de Pagamento - Servidor RMI

> **Trabalho Prático I — Sistemas Distribuídos**  
> **Tema:** Folha de Pagamento utilizando Java RMI (Remote Method Invocation)  
> **Instituição:** Universidade Estadual de Minas Gerais (UEMG)

---

## 📌 Sobre o Projeto

Este repositório contém a aplicação **Servidor** desenvolvida para o sistema de gerenciamento de **Folha de Pagamento**. A aplicação disponibiliza serviços remotos utilizando **Java RMI**, permitindo que a aplicação cliente consulte pagamentos, realize o cadastro de funcionários e efetue demissões com registro de justificativa em banco de dados relacional.

---

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 25
* **Tecnologia de Comunicação Remota:** Java RMI (`java.rmi`)
* **Banco de Dados:** MySQL
* **Conectividade:** JDBC (`com.mysql.cj.jdbc.Driver`)

---

## 🛠️ Arquitetura do Projeto

O servidor está organizado na seguinte estrutura de pacotes:

```text
src/
├── RMI/
│   ├── Servidor.java         # Classe principal (Inicialização do Registry RMI na porta 1500)
│   └── Conexao.java          # Gerenciador de conexão com o banco de dados MySQL
├── interfaces/
│   ├── InterfaceFuncionario.java # Interface remota para gestão de funcionários
│   └── InterfacePagamento.java   # Interface remota para consulta de pagamentos
├── implementadores/
│   ├── ServicoFuncionario.java  # Implementação remota da gestão de funcionários
│   └── ServicoPagamento.java    # Implementação remota de pagamentos
├── modelos/
│   ├── Funcionario.java         # Entidade Funcionario (Serializable)
│   ├── Cargo.java               # Entidade Cargo (Serializable)
│   └── Pagamento.java           # Entidade Pagamento (Serializable)
└── componentes/
    ├── Departamento.java        # Entidade Departamento (Serializable)
    ├── Departamentos.java       # Enum de Departamentos
    └── NomeCargo.java          # Enum de Cargos
```

---

## 📡 Serviços Remotos Disponíveis

### 1. `InterfaceFuncionario`
* **`cadastrarFuncionario()`**: Realiza o cadastro de um novo funcionário e seu respectivo cargo no sistema.
* **`demitirFuncionario(int funcionarioID, String justificativa)`**: Remove o registro do funcionário e salva a justificativa de desligamento na tabela `Demissoes`.

### 2. `InterfacePagamento`
* **`consultarPagamento(int funcionarioID)`**: Retorna o valor do pagamento associado ao funcionário informado.

---

## 🗄️ Configuração do Banco de Dados

O servidor utiliza um banco de dados MySQL chamado `rmi_guigui_chan`.

### Script de Criação (`schema.sql`)

```sql
CREATE DATABASE rmi_guigui_chan;
USE rmi_guigui_chan;

CREATE TABLE Cargo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    salario FLOAT DEFAULT 1675,
    departamento VARCHAR(10) NOT NULL,
    horario_chegada VARCHAR(5) NOT NULL,
    horario_saida VARCHAR(5) NOT NULL
);

CREATE TABLE Funcionario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(25) NOT NULL,
    cargo INT NOT NULL,
    CONSTRAINT cargo_do_funcionario FOREIGN KEY (cargo) REFERENCES Cargo(id)
);

CREATE TABLE Pagamento (
    id INT PRIMARY KEY AUTO_INCREMENT,
    salario_receptor FLOAT DEFAULT 1675,
    aliquota_utilizada FLOAT NOT NULL,
    imposto_cobrado FLOAT NOT NULL,
    pagamento_pratico FLOAT NOT NULL
);

CREATE TABLE Demissoes (
    id INT PRIMARY KEY AUTO_INCREMENT,
    funcionario_demitido VARCHAR(25) NOT NULL,
    razao_demissao VARCHAR(500) NOT NULL
);
```

---

## ⚙️ Variáveis de Ambiente

O projeto aceita configuração via variáveis de ambiente. Caso não estejam definidas, o sistema utilizará valores padrão (fallback):

| Variável | Descrição | Valor Padrão (Fallback) |
| :--- | :--- | :--- |
| `RMI_URL` | URL de Conexão JDBC | `jdbc:mysql://localhost/rmi_guigui_chan` |
| `DB_USER` | Usuário do MySQL | `root` |
| `DB_PASSWORD` | Senha do MySQL | `123456` |
| `FUNCIONARIO_RMI` | Identificador RMI do serviço de funcionário | `numeroServicoFuncionario` |
| `PAGAMENTO_RMI` | Identificador RMI do serviço de pagamento | `numeroServicoPagamento` |

---

## 🏁 Como Executar o Servidor

1. **Pré-requisitos:**
   * JDK 25 instalado e configurado nas variáveis de sistema.
   * Servidor MySQL em execução na porta `3306`.

2. **Configuração do Banco:**
   * Execute o script SQL acima no seu SGDB MySQL.

3. **Compilação e Execução:**
   * Compile e execute a classe `RMI.Servidor`.
   * O servidor iniciará o **RMI Registry** na porta **1500** e registrará os serviços.
   * A mensagem `"Servidor iniciado."` indicará que o serviço está pronto para receber conexões do cliente.

---

## 🔗 Repositório do Cliente

A interface gráfica do cliente para interação com este servidor pode ser encontrada no repositório:  
👉 [[#]](https://github.com/Guilherme-Porto-Silva/FolhaPagamentoClienteCorrigida/tree/main)

---

## 👥 Autores


* **Guilherme Porto e Silva**
* **Fillip Will de Oliveira Amaral**
* **Giovana Silva Manço**
