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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.sql.Date;

public class Requisicao {
	
	private Socket socket;
	final static String TERM = "\r\n";

	public Requisicao(Socket socket) {
		this.socket = socket;
	}

	public void run() {
		try {
			BufferedReader dados = new BufferedReader(new InputStreamReader(
					socket.getInputStream()));
			String entrada = dados.readLine();
			DataOutputStream saida = new DataOutputStream(
					socket.getOutputStream());
			String[] requisicao = entrada.split(" ");
			String arquivo = null;
			String versaoHTTP = null;
			Date modificacao;
			FileInputStream file;
			int bytes = 0;
			byte[] buffer = new byte[1024];
			
			System.out.println(entrada);

			if (requisicao[0].equals("GET")) {
				arquivo = "C:\\Users\\Ricardo Henrique\\Desktop\\Arquivo\\ServidorHTTP\\arquivo"
						+ requisicao[1];
				versaoHTTP = requisicao[2];
			}

			String temp = this.extensaoArquivo(requisicao[1]);

			//Carrega o arquivo.
            try {

                file = new FileInputStream(arquivo);

                //Carrega as informacoes do arquivo.
                File informacoes = new File(arquivo);

                //Verifica a ultima modificacao do arquivo.
                modificacao = new Date(informacoes.lastModified());
                String ultMod = modificacao.toString();

                //Verifica a data de hoje
                Date hoje = new Date(System.currentTimeMillis());
                String dataAtual = hoje.toString();

                saida.writeBytes(versaoHTTP + " 200 OK" + TERM);
                saida.writeBytes("Date: " + dataAtual + TERM);
                saida.writeBytes("Last-Modified: " + ultMod + TERM);
                saida.writeBytes("Content-Lenght: " + informacoes.length() + TERM);
                saida.writeBytes("Content-Type: " + temp + TERM);
                saida.writeBytes(TERM);

                // Copiar o arquivo requisitado dentro da cadeia de saída do socket.
                while ((bytes = (file.read(buffer))) != -1) {
                    saida.write(buffer, 0, bytes);
                }
                file.close();
            } catch (Exception e) {
                String notFound = "<h1>404 Not Found</h1>";
                saida.writeBytes(versaoHTTP + " 404 Not Found" + TERM);
                saida.writeBytes("Content-Lenght: " + notFound.length() + TERM);
                saida.writeBytes("Content-Type: text/html" + TERM);
                saida.writeBytes(TERM);
                saida.writeBytes(notFound);
            }
            saida.close();
            dados.close();
            socket.close();
        } catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	public String extensaoArquivo(String extensao) {
		String[] aux = extensao.split("\\.");
		System.out.println("Tipo de arquivo: " + aux[1]);
		if (aux[1].equals("htm") || aux[1].equals("html")) {
			return "text/html";
		}
		if (aux[1].equals("gif")) {
			return "image/gif";
		}
		if (aux[1].equals("jpeg") || aux[1].equals("jpg")) {
			return "image/jpeg"; 
		}
		return "application/octet-stream";
	}
}