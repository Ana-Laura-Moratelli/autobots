package com.autobots.automanager;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.autobots.automanager.entidades.Usuario;
import com.autobots.automanager.modelos.Perfil;
import com.autobots.automanager.repositorios.RepositorioUsuario;
import com.autobots.automanager.entidades.CredencialUsuarioSenha;
import com.autobots.automanager.entidades.Documento;
import com.autobots.automanager.entidades.Email;
import com.autobots.automanager.entidades.Empresa;
import com.autobots.automanager.entidades.Endereco;
import com.autobots.automanager.entidades.Mercadoria;
import com.autobots.automanager.entidades.Servico;
import com.autobots.automanager.entidades.Telefone;
import com.autobots.automanager.entidades.Veiculo;
import com.autobots.automanager.entidades.Venda;
import com.autobots.automanager.modelos.TipoDocumento;
import com.autobots.automanager.modelos.TipoVeiculo;
import com.autobots.automanager.repositorios.RepositorioEmpresa;

@SpringBootApplication
public class AutomanagerApplication implements CommandLineRunner {

	@Autowired
	private RepositorioUsuario repositorio;
	
	@Autowired
	private RepositorioEmpresa repositorioEmpresa;

	public static void main(String[] args) {
		SpringApplication.run(AutomanagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		BCryptPasswordEncoder codificador = new BCryptPasswordEncoder();
						    		    
		    Empresa empresa = new Empresa();
			empresa.setRazaoSocial("Car service toyota ltda");
			empresa.setNomeFantasia("Car service manutenção veicular");
			empresa.setCadastro(new Date());

			Endereco enderecoEmpresa = new Endereco();
			enderecoEmpresa.setEstado("São Paulo");
			enderecoEmpresa.setCidade("São Paulo");
			enderecoEmpresa.setBairro("Centro");
			enderecoEmpresa.setRua("Av. São João");
			enderecoEmpresa.setNumero("00");
			enderecoEmpresa.setCodigoPostal("01035-000");

			empresa.setEndereco(enderecoEmpresa);

			Telefone telefoneEmpresa = new Telefone();
			telefoneEmpresa.setDdd("011");
			telefoneEmpresa.setNumero("986454527");

			empresa.getTelefones().add(telefoneEmpresa);

			Usuario funcionario = new Usuario();
			funcionario.setNome("Pedro Vendedor");
			CredencialUsuarioSenha credencialFuncionario = new CredencialUsuarioSenha();
			credencialFuncionario.setNomeUsuario("vendedor");
		    credencialFuncionario.setSenha(codificador.encode("vendedor123"));
			credencialFuncionario.setCriacao(new Date());
		    credencialFuncionario.setUltimoAcesso(new Date());
		    funcionario.setCredencial(credencialFuncionario);
			funcionario.setNomeSocial("Dom Pedro");
			funcionario.getPerfis().add(Perfil.ROLE_VENDEDOR);

			Email emailFuncionario = new Email();
			emailFuncionario.setEndereco("a@a.com");

			funcionario.getEmails().add(emailFuncionario);

			Endereco enderecoFuncionario = new Endereco();
			enderecoFuncionario.setEstado("São Paulo");
			enderecoFuncionario.setCidade("São Paulo");
			enderecoFuncionario.setBairro("Jardins");
			enderecoFuncionario.setRua("Av. São Gabriel");
			enderecoFuncionario.setNumero("00");
			enderecoFuncionario.setCodigoPostal("01435-001");

			funcionario.setEndereco(enderecoFuncionario);

			empresa.getUsuarios().add(funcionario);

			Telefone telefoneFuncionario = new Telefone();
			telefoneFuncionario.setDdd("011");
			telefoneFuncionario.setNumero("9854633728");

			funcionario.getTelefones().add(telefoneFuncionario);

			Documento cpf = new Documento();
			cpf.setDataEmissao(new Date());
			cpf.setNumero("856473819229");
			cpf.setTipo(TipoDocumento.CPF);

			funcionario.getDocumentos().add(cpf);

		

			Usuario fornecedor = new Usuario();
			fornecedor.setNome("Componentes varejo de partes automotivas ltda");
			fornecedor.setNomeSocial("Loja do carro, vendas de componentes automotivos");
			fornecedor.getPerfis().add(Perfil.ROLE_VENDEDOR);

			Email emailFornecedor = new Email();
			emailFornecedor.setEndereco("f@f.com");

			fornecedor.getEmails().add(emailFornecedor);

		
			Documento cnpj = new Documento();
			cnpj.setDataEmissao(new Date());
			cnpj.setNumero("00014556000100");
			cnpj.setTipo(TipoDocumento.CNPJ);

			fornecedor.getDocumentos().add(cnpj);

			Endereco enderecoFornecedor = new Endereco();
			enderecoFornecedor.setEstado("Rio de Janeiro");
			enderecoFornecedor.setCidade("Rio de Janeiro");
			enderecoFornecedor.setBairro("Centro");
			enderecoFornecedor.setRua("Av. República do chile");
			enderecoFornecedor.setNumero("00");
			enderecoFornecedor.setCodigoPostal("20031-170");

			fornecedor.setEndereco(enderecoFornecedor);

			empresa.getUsuarios().add(fornecedor);
			
			Mercadoria rodaLigaLeve = new Mercadoria();
			rodaLigaLeve.setCadastro(new Date());
			rodaLigaLeve.setFabricacao(new Date()); 
			rodaLigaLeve.setNome("Roda de liga leve modelo toyota etios");
			rodaLigaLeve.setValidade(new Date());
			rodaLigaLeve.setQuantidade(30);
			rodaLigaLeve.setValor(300.0);
			rodaLigaLeve.setDescricao("Roda de liga leve original de fábrica da Toyota para modelos do tipo hatch");
			empresa.getMercadorias().add(rodaLigaLeve);
			fornecedor.getMercadorias().add(rodaLigaLeve);

			empresa.getMercadorias().add(rodaLigaLeve);

			fornecedor.getMercadorias().add(rodaLigaLeve);

			Usuario cliente1 = new Usuario();
			cliente1.setNome("Pedro Cliente");
			cliente1.setNomeSocial("Dom pedro");
			cliente1.getPerfis().add(Perfil.ROLE_CLIENTE);
			CredencialUsuarioSenha credencialCliente1 = new CredencialUsuarioSenha();
		    credencialCliente1.setNomeUsuario("cliente");
		    credencialCliente1.setSenha(codificador.encode("cliente123"));
		    credencialCliente1.setCriacao(new Date());
		    credencialCliente1.setUltimoAcesso(new Date());
		    cliente1.setCredencial(credencialCliente1);
			Email emailCliente = new Email();
			emailCliente.setEndereco("c@c.com");

			cliente1.getEmails().add(emailCliente);

			Documento cpfCliente = new Documento();
			cpfCliente.setDataEmissao(new Date());
			cpfCliente.setNumero("12584698533");
			cpfCliente.setTipo(TipoDocumento.CPF);

			cliente1.getDocumentos().add(cpfCliente);


			Endereco enderecoCliente = new Endereco();
			enderecoCliente.setEstado("São Paulo");
			enderecoCliente.setCidade("São José dos Campos");
			enderecoCliente.setBairro("Centro");
			enderecoCliente.setRua("Av. Dr. Nelson D'Ávila");
			enderecoCliente.setNumero("00");
			enderecoCliente.setCodigoPostal("12245-070");

			cliente1.setEndereco(enderecoCliente);
			
			Veiculo veiculo = new Veiculo();
			veiculo.setPlaca("ABC-0000");
			veiculo.setModelo("corolla-cross");
			veiculo.setTipo(TipoVeiculo.SUV);
			veiculo.setProprietario(cliente1);
			
			cliente1.getVeiculos().add(veiculo);
			
			empresa.getUsuarios().add(cliente1);

			Servico trocaRodas = new Servico();
			trocaRodas.setDescricao("Troca das rodas do carro por novas");
			trocaRodas.setNome("Troca de rodas");
			trocaRodas.setValor(50);

			Servico alinhamento = new Servico();
			alinhamento.setDescricao("Alinhamento das rodas do carro");
			alinhamento.setNome("Alinhamento de rodas");
			alinhamento.setValor(50);

			empresa.getServicos().add(trocaRodas);
			empresa.getServicos().add(alinhamento);

			Venda venda = new Venda();
			venda.setCadastro(new Date());
			venda.setCliente(cliente1);
			venda.getMercadorias().add(rodaLigaLeve);
			venda.setIdentificacao("1234698745");
			venda.setFuncionario(funcionario);
			venda.getServicos().add(trocaRodas);
			venda.getServicos().add(alinhamento);
			venda.setVeiculo(veiculo);
			veiculo.getVendas().add(venda);

			empresa.getVendas().add(venda);

			repositorioEmpresa.save(empresa);
			
			Mercadoria rodaLigaLeve2 = new Mercadoria();
			rodaLigaLeve2.setCadastro(new Date());
			rodaLigaLeve2.setFabricacao(new Date()); 
			rodaLigaLeve2.setNome("Roda de liga leve modelo Toyota Corolla");
			rodaLigaLeve2.setValidade(new Date());
			rodaLigaLeve2.setQuantidade(20);
			rodaLigaLeve2.setValor(350.0);
			rodaLigaLeve2.setDescricao("Roda de liga leve original de fábrica da Toyota para modelos do tipo sedan");
			empresa.getMercadorias().add(rodaLigaLeve2);
			fornecedor.getMercadorias().add(rodaLigaLeve2);
			
			Servico alinhamento2 = new Servico();
			alinhamento2.setDescricao("Alinhamento das rodas do carro");
			alinhamento2.setNome("Alinhamento de rodas");
			alinhamento2.setValor(50);
			
			Servico balanceamento = new Servico();
			balanceamento.setDescricao("balanceamento das rodas do carro");
			balanceamento.setNome("balanceamento de rodas");
			balanceamento.setValor(30);
			
			Venda venda2 = new Venda();
			venda2.setCadastro(new Date());
			venda2.setCliente(cliente1);
			venda2.getMercadorias().add(rodaLigaLeve2);
			venda2.setIdentificacao("1234698749");
			venda2.setFuncionario(funcionario);
			venda2.getServicos().add(balanceamento);
			venda2.getServicos().add(alinhamento2);
			venda2.setVeiculo(veiculo);
			veiculo.getVendas().add(venda2);

			empresa.getVendas().add(venda2);
			Usuario admin = new Usuario();
		    admin.setNome("administrador");
		    admin.getPerfis().add(Perfil.ROLE_ADMIN);
		    CredencialUsuarioSenha credencialAdmin = new CredencialUsuarioSenha();
		    credencialAdmin.setNomeUsuario("admin");
		    credencialAdmin.setSenha(codificador.encode("admin123"));
		    credencialAdmin.setCriacao(new Date());
		    credencialAdmin.setUltimoAcesso(new Date());
		    admin.setCredencial(credencialAdmin);
		    repositorio.save(admin);
			empresa.getUsuarios().add(admin);
		    
		    Usuario gerente = new Usuario();
		    gerente.setNome("gerente");
		    gerente.getPerfis().add(Perfil.ROLE_GERENTE);
		    CredencialUsuarioSenha credencialGerente = new CredencialUsuarioSenha();
		    credencialGerente.setNomeUsuario("gerente");
		    credencialGerente.setSenha(codificador.encode("gerente123"));
		    credencialGerente.setCriacao(new Date());
		    credencialGerente.setUltimoAcesso(new Date());
		    gerente.setCredencial(credencialGerente);
		    repositorio.save(gerente);
			empresa.getUsuarios().add(gerente);

			repositorioEmpresa.save(empresa);
	}
}