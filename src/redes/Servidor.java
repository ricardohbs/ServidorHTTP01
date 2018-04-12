/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package redes;

/**
 *
 * @author Ricardo Henrique
 */
package redes;


import java.net.ServerSocket;
import java.net.Socket;


public class Servidor {



	public static void main(String args[]) throws Exception {
		
	int port = 8081;
		
		ServerSocket servidor = new ServerSocket(port);

		Requisicao request;

		Socket conexao;



		while (true) {

			conexao = servidor.accept();

			request = new Requisicao(conexao);

			request.run();			


		}
	}
}

