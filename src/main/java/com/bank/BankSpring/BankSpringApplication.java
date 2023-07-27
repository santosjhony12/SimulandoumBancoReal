package com.bank.BankSpring;

import com.bank.BankSpring.Model.Cliente.Cliente;
import com.bank.BankSpring.Model.Cliente.ClienteService;
import com.bank.BankSpring.Model.ContaCorrente.ContaCorrente;
import com.bank.BankSpring.Model.ContaCorrente.CorrenteService;
import com.bank.BankSpring.Model.ContaPoupanca.ContaPoupanca;
import com.bank.BankSpring.Model.ContaPoupanca.PoupancaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class BankSpringApplication implements CommandLineRunner {
	/*INJEÇÃO DE DEPENDENCIA - É NECESSÁRIA TODA VEZ QUE FOR INSERIR UM SERVIÇO*/
	private final ClienteService clienteService;
	private final PoupancaService poupancaService;
	private final CorrenteService correnteService;


	@Autowired
	public BankSpringApplication(ClienteService clienteService, PoupancaService poupancaService, CorrenteService correnteService){
		this.clienteService = clienteService;
		this.poupancaService = poupancaService;
		this.correnteService = correnteService;
	}
	public static void main(String[] args) {
		SpringApplication.run(BankSpringApplication.class, args);
	}

	Cliente cliente = new Cliente();
	public int login(String cpf, String senha){
		cliente = clienteService.buscarClientePorCpf(cpf);
		if (cliente != null && senha.equals(cliente.getSenha())){
			return 1;
		}else{
			System.out.println("\nAcesso Negado!");
			return 0;
		}
	}

	@Override
	public void run(String... args) throws Exception {
		/*INSTANCIAS*/
		String cpfScanner = null;
		Cliente cliente = new Cliente();
		ContaPoupanca contaPoupanca = new ContaPoupanca();
		ContaCorrente contaCorrente	= new ContaCorrente();
		Scanner scanner = new Scanner(System.in);

		/*DECLARAÇÃO DE VARIAVEIS DE CONTROLE*/
		int acesso = 0;
		String acao;
		double saldoCorrente = 0, saldoPoupanca = 0, chequeEspecial = 0, saldoTotal = 0;


		/* INICIO DO PROGRAMA */
		tracejado();
		System.out.println("\nBEM VINDO AO SEU BANCO DIGITAL\n");
		System.out.println("O QUE GOSTARIA DE FAZER HOJE?");
		System.out.println("1 - Cadastro\n2 - Entrar na sua conta\n3 - SAIR");
		String resposta = scanner.next();

		tracejado();

		if (resposta.equals("1")){
			System.out.println("\nVOCÊ FEZ UMA ÓTIMA ESCOLHA EM FAZER PARTE DESSE NEGÓCIO!\nVAMOS COMEÇAR O SEU CADASTRO!!");
			scanner.nextLine();
			System.out.println("\nDigite o seu nome: ");
			String nome = scanner.nextLine();
			cliente.setName(nome);

			System.out.println("\nDigite o seu CPF: ");
			String cpf = scanner.next();
			cliente.setCpf(cpf);

			System.out.println("\nDigite sua senha:");
			String senha = scanner.next();
			cliente.setSenha(senha);
			clienteService.inserirCliente(cliente);

			String conta = "0";
			while(!conta.equals("1") && !conta.equals("2") && !conta.equals("3")){
				System.out.println("\nQual tipo de conta gostaria de ter: ");
				System.out.println("\n1 - POUPANÇA\n2 - CORRENTE\n3 - POUPANÇA E CORRENTE");
				conta = scanner.next();
				if (!conta.equals("1") && !conta.equals("2") && !conta.equals("3")){
					System.out.println("OPÇÃO INVALIDA!");
				}
			}
			if (conta.equals("1")){
				try{
					contaPoupanca.setTipo("P");
					contaPoupanca.setCpf(cpf);
					contaPoupanca.setSaldo(0);
					poupancaService.inserirContaPoupanca(contaPoupanca);
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if(conta.equals("2")){
				try {
					contaCorrente.setTipo("C");
					contaCorrente.setChequeEspecial(100);
					contaCorrente.setSaldo(0);
					contaCorrente.setCpf(cpf);
					correnteService.inserirContaCorrente(contaCorrente);
					System.out.println("Parabéns! Você ganhou um cheque especial de R$ 100,00");;
				}catch (Exception e){
					e.printStackTrace();
				}
			}else if(conta.equals("3")){
				try{
					try{
						contaPoupanca.setTipo("P");
						contaPoupanca.setCpf(cpf);
						contaPoupanca.setSaldo(0);
						poupancaService.inserirContaPoupanca(contaPoupanca);
					}catch (Exception e){
						e.printStackTrace();
					}
					try {
						contaCorrente.setTipo("C");
						contaCorrente.setChequeEspecial(100);
						contaCorrente.setSaldo(0);
						contaCorrente.setCpf(cpf);
						correnteService.inserirContaCorrente(contaCorrente);
						System.out.println("Parabéns! Você ganhou um cheque especial de R$ 100,00");;
					}catch (Exception e){
						e.printStackTrace();
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			}


			System.out.println("\nEstamos registrando...");
			System.out.println("\nCADASTRO REALIZADO COM SUCESSO!!");

			System.out.println("\n\nPara iniciar sessão, digite 1. Para sair, digite 2: ");
			resposta = scanner.next();

			if (resposta.equals("1")){
				System.out.println("Digite seu CPF: ");
				cpfScanner = scanner.next();

				System.out.println("Digite sua senha: ");
				String senhaScanner = scanner.next();
				acesso = login(cpfScanner, senhaScanner);
			}

		} else if (resposta.equals("2")) {
			System.out.println("BEM VINDO DE VOLTA! ESTAMOS FELIZES EM TE RECEBE-LO NOVAMENTE");
			System.out.println("Digite seu CPF: ");
			cpfScanner = scanner.next();

			System.out.println("Digite sua senha: ");
			String senhaScanner = scanner.next();
			acesso = login(cpfScanner, senhaScanner);
		}else{
			System.out.println("ATÉ MAIS");
		}


		if (acesso == 1) {
			cliente = clienteService.buscarClientePorCpf(cpfScanner);
			contaPoupanca = poupancaService.buscarDadosPorCpf(cpfScanner);
			contaCorrente = correnteService.findByCpf(cpfScanner);
			tracejado();
			System.out.println("\nBEM VINDO "+cliente.getName()+"\nCPF:" + cliente.getCpf());
			questionarioLogado();
			acao = scanner.next();
			tracejado();

			if (acao.equals("1")) {
				if (contaPoupanca != null & contaCorrente != null) {
					System.out.println("SALDO CONTA CORRENTE: R$ " + contaCorrente.getSaldo());
					System.out.println("SALDO CONTA POUPANÇA: R$ " + contaPoupanca.getSaldo());
					System.out.println("VALOR CHEQUE ESPECIAL: R$ "+ contaCorrente.getChequeEspecial());

					/*CALCULO SALDO TOTAL*/
					chequeEspecial = contaCorrente.getChequeEspecial();
					saldoPoupanca = contaPoupanca.getSaldo();
					saldoCorrente = contaCorrente.getSaldo();
					saldoTotal = saldoPoupanca+saldoCorrente+chequeEspecial;
					System.out.println("SALDO TOTAL: R$ "+saldoTotal);

				} else if (contaPoupanca != null) {
					System.out.println("SALDO CONTA POUPANÇA: R$ " + contaPoupanca.getSaldo());
				} else if (contaCorrente != null) {
					System.out.println("SALDO CONTA CORRENTE: R$ " + contaCorrente.getSaldo());
					System.out.println("VALOR CHEQUE ESPECIAL: R$ "+ contaCorrente.getChequeEspecial());

					/*CALCULO SALDO TOTAL*/
					chequeEspecial = contaCorrente.getChequeEspecial();
					saldoCorrente = contaCorrente.getSaldo();
					saldoTotal = saldoCorrente+chequeEspecial;
					System.out.println("SALDO TOTAL: R$ "+saldoTotal);
				}
				tracejado();
			} else if (acao.equals("2")) {
				if (contaPoupanca != null & contaCorrente != null){
					System.out.printf("Selecione qual conta deseja realizar a transferência: \n1 - Corrente\n2 - Poupança");
					acao = scanner.next();
					System.out.printf("Informe a conta que deseja transferir: ");
					String contaTransferir = scanner.next();
					double valorTransferencia = 0;

					if (acao.equals("1")){
						System.out.println("Saldo disponível: R$ "+contaCorrente.getSaldo());
						saldoCorrente = contaCorrente.getSaldo();

						while(valorTransferencia > saldoCorrente || valorTransferencia <= 0){
							System.out.println("Informe o valor da transferência: ");
							valorTransferencia = scanner.nextDouble();

							if (valorTransferencia > saldoCorrente){
								System.out.println("O valor é maior que o há disponível.");
							}
							else if (valorTransferencia<=0){
								System.out.printf("O valor a ser transferido não pode ser menor ou igual a 0.");
							}
							else if (valorTransferencia >0 & valorTransferencia<=saldoCorrente){
								System.out.println("Transferido.");
							}
						}
					} else if (acao.equals("2")) {
						System.out.printf("SALDO DISPONIVEL: R$ "+contaPoupanca.getSaldo());
						saldoPoupanca = contaPoupanca.getSaldo();
						while(valorTransferencia > saldoPoupanca || valorTransferencia <= 0){
							System.out.println("Informe o valor da transferência: ");
							valorTransferencia = scanner.nextDouble();

							if (valorTransferencia > saldoPoupanca){
								System.out.println("O valor é maior que o há disponível.");
							}
							else if (valorTransferencia<=0){
								System.out.printf("O valor a ser transferido não pode ser menor ou igual a 0.");
							}
							else if (valorTransferencia >0 & valorTransferencia<=saldoCorrente){
								System.out.println("Transferido.");
							}
						}
					}

				}else if(contaCorrente != null){
					System.out.printf("Informe a conta que deseja transferir: ");
					String contaTransferir = scanner.next();

					double valorTransferencia = 0;
					saldoCorrente = contaCorrente.getSaldo();

					while(valorTransferencia > saldoCorrente || valorTransferencia <= 0){
						System.out.println("Informe o valor da transferência: ");
						valorTransferencia = scanner.nextDouble();

						if (valorTransferencia > saldoCorrente){
							System.out.println("O valor é maior que o há disponível.");
						}
						else if (valorTransferencia<=0){
							System.out.printf("O valor a ser transferido não pode ser menor ou igual a 0.");
						}
						else if (valorTransferencia >0 & valorTransferencia<=saldoCorrente){
							System.out.println("Transferido.");
						}
					}
				}

			}

		}



		/*
		* PARA INSERIR O CLIENTE BASTA CHAMAR A FUNÇÃO INSERIRCLIENTE DO
		* CLIENTESERVICE E PASSAR POR PARAMETRO A CLASSE CLIENTE. OBS.: NÃO PODE ESQUECER DE SETAR OS VALORES DA CLASSE
		* cliente.setName("João");
		* cliente.setCpf("123");
		* cliente.setSenha("123);
		* clienteService.inserirCliente(cliente);
		* */


		/* BUSCANDO CLIENTE POR CPF
		cliente = clienteService.buscarClientePorCpf(cpf);

		if (cliente != null){
			System.out.println("Cliente Encontrado");
			System.out.println("ID: "+cliente.getId());
			System.out.println("Nome: "+cliente.getName());
			System.out.println("CPF: "+cliente.getCpf());
			System.out.println(cliente);
		}else{
			System.out.println("Cliente não encontrado!");
		}*/


		/*UPDATE DE SENHA POR CPF
		cliente = clienteService.buscarClientePorCpf(cpf);

		if(cliente != null){
			System.out.println("CLIENTE ENCONTRADO! ");
			System.out.println("Nova senha: ");
			String senha = scanner.next();
			cliente.setSenha(senha);
			clienteService.atualizarSenha(cliente);

		}else{
			System.out.println("CLIENTE NÃO ENCONTRADO!");
		}
*/

		/*DELETE
		try {
			clienteService.excluirClientePorCpf(cpf);
			System.out.println("Cliente excluído com sucesso!");
		} catch (ClienteNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		*/
	}

	/* FUNÇÕES STATICAS PARA VISUAL*/
	public static void tracejado(){
		System.out.println("====================================================");
	}
	public static void questionarioLogado(){
		System.out.println("\nO que deseja realizar?");
		System.out.println("1 - Consultar Saldo");
		System.out.println("2 - Transfência");
		System.out.println("3 - Sacar");
		System.out.println("4 - Alterar Senha");
		System.out.println("5 - Excluir conta");
		System.out.println("6 - SAIR\n");
	}

}
